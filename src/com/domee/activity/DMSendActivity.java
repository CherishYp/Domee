package com.domee.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.domee.R;
import com.domee.model.DMStatus;
import com.domee.model.User;
import com.domee.utils.DMConstants;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

import java.io.IOException;

/**
 * Created by duyuan on 13-5-28.
 */
public class DMSendActivity extends BaseActivity {

    private ImageView mAt;
    private TextView mTextLimit;
    private EditText mStatusText;
    private ImageButton mOk;
    private ImageButton mClose;
    private CheckBox mIsCommentCurChx;
    private CheckBox mIsCommentOriChx;
    private CheckBox mIsRepostChx;
    private TextView mTitle;

    private String text;
    private int size = 0;
    private boolean mIsCommentCur;
    private boolean mIsCommentOrg;
    private boolean mIsRepost;

    private DMStatus mStatus;
    private int mFlag;

    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent();
                    intent.setAction(DMConstants.SEND_STATUS_ACTION);
                    sendBroadcast(intent);
                    finish();
                    break;
                case 1:
                    Toast.makeText(DMSendActivity.this, "发送成功", Toast.LENGTH_LONG);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    public static void show(Activity mActivity, DMStatus mStatus, int mFlag) {
        Intent intent = new Intent(mActivity, DMSendActivity.class);
        intent.putExtra("status", mStatus);
        intent.putExtra("flag", mFlag);
        mActivity.startActivityForResult(intent, 1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_send);

        mStatus = (DMStatus) getIntent().getSerializableExtra("status");
        System.out.println(mStatus);
        mFlag = getIntent().getIntExtra("flag", 0);

        mIsCommentCurChx = (CheckBox) findViewById(R.id.r_is_comment_curchx);
        mIsCommentOriChx = (CheckBox) findViewById(R.id.r_is_comment_orichx);
        mIsRepostChx = (CheckBox) findViewById(R.id.r_is_repost_chx);
        mTitle = (TextView) findViewById(R.id.r_title);
        mStatusText = (EditText) findViewById(R.id.r_status_text);
        mTextLimit = (TextView) findViewById(R.id.r_text_limit);
        mStatusText.addTextChangedListener(cWatcher);
        if (mFlag == DMConstants.FLAG_REPOST) {
            mIsCommentCurChx.setVisibility(View.VISIBLE);
            mIsCommentOriChx.setVisibility(View.VISIBLE);
            mIsRepostChx.setVisibility(View.GONE);
            mTitle.setText("转发微博");
            mStatusText.setText("//@" + mStatus.getUser().getScreen_name() + ":" + mStatus.getText());
        } else {
            mIsCommentCurChx.setVisibility(View.GONE);
            mIsCommentOriChx.setVisibility(View.GONE);
            mIsRepostChx.setVisibility(View.VISIBLE);
            mTitle.setText("评论微博");
        }

        mAt = (ImageView) findViewById(R.id.r_at_friend);
        mOk = (ImageButton) findViewById(R.id.r_ok);
        mClose = (ImageButton) findViewById(R.id.r_close);

        mAt.setOnClickListener(new BtnListener());
        mTextLimit.setOnClickListener(new BtnListener());
        mOk.setOnClickListener(new BtnListener());
        mClose.setOnClickListener(new BtnListener());
        mIsCommentCurChx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsCommentCur = b;
            }
        });
        mIsCommentOriChx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsCommentOrg = b;
            }
        });
        mIsRepostChx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsRepost = b;
            }
        });

    }

    TextWatcher cWatcher = new TextWatcher() {
        private CharSequence temp;
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            size = 140 - temp.length();
            if (size >= 0) {
                mTextLimit.setText((140 - temp.length()) + "");
            }else {
                mTextLimit.setText((140 - temp.length()) + "");
                mTextLimit.setTextColor(android.graphics.Color.RED);
            }
        }
    };

    public class BtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch (v.getId()) {
                case R.id.r_ok:
                    if (size < 0) {
                        Toast toast = new Toast(DMSendActivity.this);
                        toast = Toast.makeText(DMSendActivity.this, "字数太多，不让发送", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                    } else {
                        text = mStatusText.getText().toString();
                        System.out.println(text);
                        if (mFlag == DMConstants.FLAG_REPOST) {
                            sendStatus();
                        } else {
                            sendComment();
                        }
                    }
                    break;
                case R.id.r_close:
                    if (size != 0) {
                        new AlertDialog.Builder(DMSendActivity.this).setTitle("是否保存?")
                                .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DMSendActivity.this.finish();
                                    }
                                })
                                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DMSendActivity.this.finish();
                                    }
                                }).show();
                    } else {
                        DMSendActivity.this.finish();
                    }
                    break;
                case R.id.r_at_friend:
                    DMAtFriendActivity.show(DMSendActivity.this, new DMUserSelectedListenerImpl());
                    break;

                default:
                    break;
            }
        }
    }

    public void sendStatus() {
        WeiboAPI.COMMENTS_TYPE flag = null;
        if (mIsCommentCur && mIsCommentOrg) {
            flag = WeiboAPI.COMMENTS_TYPE.BOTH;
        } else if ((!mIsCommentCur) && (!mIsCommentOrg)) {
            flag = WeiboAPI.COMMENTS_TYPE.NONE;
        } else {
            if (mIsCommentCur) flag = WeiboAPI.COMMENTS_TYPE.CUR_STATUSES;
            if (mIsCommentOrg) flag = WeiboAPI.COMMENTS_TYPE.ORIGAL_STATUSES;
        }
        statusesAPI.repost(mStatus.getId(), text, flag, new RequestListener() {
            @Override
            public void onComplete(String s) {
                Intent intent = new Intent();
                intent.setAction(DMConstants.SEND_STATUS_ACTION);
                sendBroadcast(intent);
                finish();
            }

            @Override
            public void onIOException(IOException e) {

            }

            @Override
            public void onError(WeiboException e) {

            }
        });
    }

    public void sendComment() {
        System.out.println(mIsRepost);
        commentsAPI.create(text, mStatus.getId(), mIsRepost, new RequestListener() {
            @Override
            public void onComplete(String s) {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onIOException(IOException e) {

            }

            @Override
            public void onError(WeiboException e) {

            }
        });
        if (mIsRepost) {
            statusesAPI.repost(mStatus.getId(), text, WeiboAPI.COMMENTS_TYPE.CUR_STATUSES, new RequestListener() {
                @Override
                public void onComplete(String s) {
                    Message msg = handler.obtainMessage(0);
                    handler.sendMessage(msg);
                }

                @Override
                public void onIOException(IOException e) {

                }

                @Override
                public void onError(WeiboException e) {

                }
            });
        }
    }

    public class DMUserSelectedListenerImpl implements DMUserSelectedListener {

        @Override
        public void userSelected(Activity activity, User user) {

            int index = mStatusText.getSelectionStart();
            //将字符串转换为StringBuffer
            StringBuffer sb = new StringBuffer(mStatusText.getText().toString());
            //将字符插入光标所在的位置
            String str = " @" + user.getScreen_name() + " ";
            sb = sb.insert(index, str);
            mStatusText.setText(sb.toString());
            //设置光标的位置保持不变
            Selection.setSelection(mStatusText.getText(), index + str.length());
            activity.finish();
        }

        @Override
        public void userCancelSelected(Activity activity) {

        }
    }


}
