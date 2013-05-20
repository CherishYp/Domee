package com.domee;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.*;
import com.domee.model.Status;
import com.domee.model.User;
import com.google.gson.reflect.TypeToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import static android.text.Selection.setSelection;

public class DMComposeActivity extends BaseActivity {

	private EditText cStatusText;
	private ImageButton cOk;
	private ImageButton cClose;
	private ImageButton cCamera;
	private ImageButton cTrend;
	private ImageButton cAtFriend;
	private TextView cTextLimit;
	private String text;
	private Status status;
	private int size = 0;
	
	public static void show(Context context) {
		Intent intent = new Intent(context, DMComposeActivity.class);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_compose);
		
		cStatusText = (EditText)findViewById(R.id.cStatusText);
        cCamera = (ImageButton) findViewById(R.id.cCamera);
        cTrend = (ImageButton) findViewById(R.id.cTrend);
        cTextLimit = (TextView) findViewById(R.id.cTextLimit);
        cAtFriend = (ImageButton) findViewById(R.id.c_at_friend);
        cOk = (ImageButton) findViewById(R.id.cOk);
        cClose = (ImageButton) findViewById(R.id.cClose);
        
        cCamera.setOnClickListener(new BtnListener());
        cStatusText.addTextChangedListener(cWatcher);
        cOk.setOnClickListener(new BtnListener());
        cClose.setOnClickListener(new BtnListener());
        cAtFriend.setOnClickListener(new BtnListener());
        cTrend.setOnClickListener(new BtnListener());
	}
	
	TextWatcher cWatcher = new TextWatcher() {
		private CharSequence temp;  
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			temp = s;
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			size = 140 - temp.length();
			if (size >= 0) {
				cTextLimit.setText((140 - temp.length()) + "");
			}else {
				cTextLimit.setText((140 - temp.length()) + "");
				cTextLimit.setTextColor(android.graphics.Color.RED);
			}
		}
	};
	
	public class BtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.cOk:
				if (size < 0) {
					Toast toast = new Toast(DMComposeActivity.this);
					toast = Toast.makeText(DMComposeActivity.this, "字数太多，不让发送", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 50);
					toast.show();
				} else {
					text = cStatusText.getText().toString();
					System.out.println(text);
					sendStatus();
				}
				break;
			case R.id.cClose:
                new AlertDialog.Builder(DMComposeActivity.this).setTitle("是否保存?")
                        .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DMComposeActivity.this.finish();
                            }
                        })
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DMComposeActivity.this.finish();
                            }
                        }).show();
				break;
			case R.id.cCamera:
				Intent intent = new Intent();
				intent.setType("image/*");    //这个参数是确定要选择的内容为图片
//				intent.putExtra("crop", "circle");   //这个参数 不太懂，唯一知道的是：设置了参数，就会调用裁剪，如果不设置，就会跳过裁剪的过程。
//				intent.putExtra("aspectX", 33);  //这个是裁剪时候的 裁剪框的 X 方向的比例。
//				intent.putExtra("aspectY",43);  //同上Y 方向的比例. (注意： aspectX, aspectY ，两个值都需要为 整数，如果有一个为浮点数，就会导致比例失效。)
				//设置aspectX 与 aspectY 后，裁剪框会按照所指定的比例出现，放大缩小都不会更改。如果不指定，那么 裁剪框就可以随意调整了。
//				intent.putExtra("outputX", 50);  //返回数据的时候的 X 像素大小。
//				intent.putExtra("outputY", 100);  //返回的时候 Y 的像素大小。
				//以上两个值，设置之后会按照两个值生成一个Bitmap, 两个值就是这个bitmap的横向和纵向的像素值，如果裁剪的图像和这个像素值不符合，那么空白部分以黑色填充。
//				intent.putExtra("noFaceDetection", true); // 是否去除面部检测， 如果你需要特定的比例去裁剪图片，那么这个一定要去掉，因为它会破坏掉特定的比例。
//				intent.putExtra("return-data", true);  //是否要返回值。 一般都要。
                intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 1);
				break;
			case R.id.c_at_friend:
				DMAtFriendActivity.show(DMComposeActivity.this, new DMUserSelectedListenerImpl());
				break;
			case R.id.cTrend:
                int index = cStatusText.getSelectionStart();
                //将字符串转换为StringBuffer
                StringBuffer sb = new StringBuffer(cStatusText.getText().toString().trim());
                //将字符插入光标所在的位置
                sb = sb.insert(index, "##");
                cStatusText.setText(sb.toString());
                //设置光标的位置保持不变
                Selection.setSelection(cStatusText.getText(), index+1);
				break;
			default:
				break;
			}
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                ImageView mImageView = (ImageView) findViewById(R.id.c_image_view);
                mImageView.setVisibility(View.INVISIBLE);
                System.out.println("DMComposeActivity=============>" + bitmap);
                /* 将Bitmap设定到ImageView */
                mImageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendStatus() {
		SendStatusRequestListener listener = new SendStatusRequestListener();
		statusesAPI.update(text, null, null, listener);
	}
	
	public class SendStatusRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			status = gson.fromJson(arg0, new TypeToken<Status>() {}.getType());
			Intent intent = new Intent();
//			intent.setAction(SEND_STATUS_ACTION);
			sendBroadcast(intent);
			
//			IntentFilter filter = new IntentFilter();
//			filter.addAction(SEND_STATUS_ACTION);
//			registerReceiver(FriendsTimelineActivity.receiver, filter);
			finish();
		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stubactivityactivity
			
		}
		
	}

    public class DMUserSelectedListenerImpl implements DMUserSelectedListener {

        @Override
        public void userSelected(Activity activity, User user) {

            int index = cStatusText.getSelectionStart();
            //将字符串转换为StringBuffer
            StringBuffer sb = new StringBuffer(cStatusText.getText().toString().trim());
            //将字符插入光标所在的位置
            String str = " @" + user.getScreen_name() + " ";
            sb = sb.insert(index, str);
            cStatusText.setText(sb.toString());
            //设置光标的位置保持不变
            Selection.setSelection(cStatusText.getText(), index + str.length());
            activity.finish();
        }

        @Override
        public void userCancelSelected(Activity activity) {

        }
    }
}
