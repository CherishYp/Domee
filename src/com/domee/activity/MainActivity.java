package com.domee.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.domee.R;
import com.domee.adapter.DMListsAdapter;
import com.domee.adapter.DMPagerAdapter;
import com.domee.interFace.DMRefreshInterface;
import com.domee.manager.DMAccountsManager;
import com.domee.manager.DMUIManager;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import com.domee.model.DMLists;
import com.domee.model.DMListsResult;
import com.domee.model.FriendsResult;
import com.domee.model.User;
import com.domee.utils.DMConstants;
import com.domee.utils.DMGsonUtil;
import com.domee.view.DMInOutImageButton;
import com.domee.view.animation.DMGroupBtnsAnimation;
import com.domee.view.animation.DMInOutAnimation;
import com.weibo.api.FriendShipAPI;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 
 * @author duyuan
 * 
 */
@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private ViewPager mViewPager;//页卡内容
	private List<View> mListViews; // Tab页面列表
//	private ImageView tabCursor;// 动画图片
	private View tabCursor;
	private TextView tabFriendTimeline;	// 页卡头标
	private TextView tabAt;
	private TextView tabComment;
	private TextView tabProfile;
	private TextView selectedTabView;

	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpWidth;// 动画图片宽度

	private LocalActivityManager manager = null;
    private Intent ftlIntent;
    private Intent atIntent;
    private Intent commentIntent;
    private Intent profileIntent;

    private ImageView mWrietImgView;
    private ImageView mRefreshImgView;
    private ImageView mSetupImgView;

    private boolean	areBtnsShowing;
    private ViewGroup mBtnsWrapper;
    private Animation mAddBtnIn;
    private Animation mAddBtnOut;

    private Activity mActivity;
    public DMListsAdapter mAdapter;
    private ListView mListView;
    private Button mGroupBtn;
    private FrameLayout mLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        DMAccountsManager.initContext(MainActivity.this);

        mListView = (ListView) findViewById(R.id.group_list_view);
        mAdapter = new DMListsAdapter(this, mListView);
        mListView.setAdapter(mAdapter);
        mGroupBtn = (Button) findViewById(R.id.group_btn);
        //mLayout = (FrameLayout) findViewById(R.id.group_layout);

        mWrietImgView = (ImageView) findViewById(R.id.mWrietImgView);
        mWrietImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DMComposeActivity.show(MainActivity.this);
            }
        });
        mSetupImgView = (ImageView) findViewById(R.id.mSetupImgView);
        mSetupImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mLayout.setVisibility(View.VISIBLE);
                switch (currIndex) {
                    case 0:
                        mListView.setVisibility(view.VISIBLE);
                        mGroupBtn.setVisibility(View.VISIBLE);
                        loadGroup();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "unfinish", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "unfinish", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "unfinish", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }


            }
        });

        mGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mLayout.setVisibility(View.GONE);
                mListView.setVisibility(view.GONE);
                mGroupBtn.setVisibility(View.GONE);
            }
        });

		manager = new LocalActivityManager(this , true);
        manager.dispatchCreate(savedInstanceState);
        if (DMAccountsManager.getCurAccount() != null) {
            DMAccountsManager.getCurAccount().setGroupType(-1 + "");
        	initTextView();
        	initImageView();
        	initViewPaper();
        }else {
			Intent intent = new Intent();
			intent.setClass(this, DMLoginActivity.class);
			startActivity(intent);
            finish();
		}
        DMUIManager.getInstance().setMainActivity(this);
	}

	/**
     * 初始化标题
     */
	private void initTextView() {
		tabFriendTimeline = (TextView) findViewById(R.id.tab_friendtimeline_txt);
		tabAt = (TextView) findViewById(R.id.tab_at_txt);
		tabComment = (TextView) findViewById(R.id.tab_comment_txt);
		tabProfile = (TextView) findViewById(R.id.tab_profile_txt);
		tabFriendTimeline.setOnClickListener(new TabOnClickListener(0));
		tabAt.setOnClickListener(new TabOnClickListener(1));
		tabComment.setOnClickListener(new TabOnClickListener(2));
		tabProfile.setOnClickListener(new TabOnClickListener(3));
	}

    private Activity getCurActivity(){

        switch (currIndex) {
            case 0:
                mActivity = DMUIManager.getInstance().getFriendsTimelineActivity();
                break;
            case 1:
                mActivity = DMUIManager.getInstance().getmAtActivity();
                break;
            case 2:
                mActivity = DMUIManager.getInstance().getmCommentActivity();
                break;
            case 3:
                mActivity = DMUIManager.getInstance().getmProfileActivity();
                break;
            default:
                break;
        }
        return mActivity;
    }
	
	private void initViewPaper() {
		// 将要分页显示的View装入数组中
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mListViews = new ArrayList<View>();
        initIntent();
        mListViews.add(getView("ftl", ftlIntent));
        mListViews.add(getView("at", atIntent));
        mListViews.add(getView("comment", commentIntent));
        mListViews.add(getView("profile", profileIntent));
        mViewPager.setAdapter(new DMPagerAdapter(mListViews));
        mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new DMOnPageChangeListener());
	}
	
	/**
     * 通过activity获取视图
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
	
	private void initIntent() {
		ftlIntent = new Intent(this, DMFriendsTimelineActivity.class);
		atIntent = new Intent(this, DMAtActivity.class);
		commentIntent = new Intent(this, DMCommentActivity.class);
		profileIntent = new Intent(this, DMProfileActivity.class);
	}
	/**
	* 初始化动画
	*/
	private void initImageView() {
//		tabCursor = (ImageView) findViewById(R.id.tab_cursor);
		bmpWidth = 100;//BitmapFactory.decodeResource(getResources(), R.drawable.channel_sectionbar).getWidth();// 获取图片宽度
 		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;// 获取分辨率宽度		
		offset = (screenWidth/4 - bmpWidth)/2;// 计算偏移量
//		Matrix matrix = new Matrix();
//		matrix.postTranslate(offset, 0);
//		tabCursor.setImageMatrix(matrix);// 设置动画初始位置
		tabCursor = (View)findViewById(R.id.tab_cursor);
	}

    //点击按钮
    private void toggleGroupBtns() {
        if (!areBtnsShowing) {
            DMGroupBtnsAnimation.startAnimations(
                    this.mBtnsWrapper, DMInOutAnimation.Direction.IN);
//            this.composerButtonsShowHideButtonIcon
//                    .startAnimation(this.rotateStoryAddButtonIn);
        } else {
            DMGroupBtnsAnimation.startAnimations(
                    this.mBtnsWrapper, DMInOutAnimation.Direction.OUT);
//            this.composerButtonsShowHideButtonIcon
//                    .startAnimation(this.rotateStoryAddButtonOut);
        }
        areBtnsShowing = !areBtnsShowing;
    }
	/**
	* 头标点击监听
	*/
	public class TabOnClickListener implements View.OnClickListener {
		private int index = 0;
        Intent intent = new Intent();
		public TabOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
//            if (currIndex == index) {
//                switch (currIndex) {
//                    case 0:
//                        intent.setAction(DMConstants.REFRESH_ACTION);
//                        sendBroadcast(intent);
//                        break;
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//                    case 3:
//                        break;
//                }
//                mViewPager.refreshDrawableState();
//            } else {
			    selectedTabView = (TextView) v;
//            }

			//layoutParams.setMarginStart(selectedTabView.getWidth()*index);		
			//tabCursor.setLayoutParams(layoutParams);
			//tabCursor.layout(300, 0, v.getWidth()*index, 0);

		} 
	}

	/**
	 * 页卡切换监听
	 */
	public class DMOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpWidth;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3;
		@Override 
		public void onPageSelected(int arg0){
			Animation animation = null;
			switch(arg0) {
			case 0:
				selectedTabView = tabFriendTimeline;
				if(currIndex == 1){
					animation = new TranslateAnimation(one, 0, 0, 0);
				}else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}else if(currIndex == 3){
					animation = new TranslateAnimation(three, 0, 0, 0);
				}
                mSetupImgView.setImageResource(R.drawable.button_icon_group);
				break;
			case 1:
				selectedTabView = tabAt;
				if(currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}else if(currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}else if(currIndex == 3) {
                    animation = new TranslateAnimation(three, one, 0, 0);
                }
                mSetupImgView.setImageResource(R.drawable.button_icon_group);
				break;
			case 2:
				selectedTabView = tabComment;
				if(currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				}else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}else if(currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
                mSetupImgView.setImageResource(R.drawable.button_icon_group);
				break;
			case 3:
				selectedTabView = tabProfile;
				if(currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				}else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				}else if(currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
                mSetupImgView.setImageResource(R.drawable.smart_ball_setting);
				break;
			}
		   
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			//tabCursor.setMinimumWidth(selectedTabView.getWidth());
			tabCursor.startAnimation(animation);
			//tabCursor.layout(10, 0, 0, 0);
		}
		@Override
		public void onPageScrolled(int arg0,float arg1,int arg2) {
			
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	}

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };

    public void loadGroup() {
        FriendShipAPI friendShipAPI = new FriendShipAPI(DMAccountsManager.curAccessToken());
        friendShipAPI.groups(new RequestListener() {
            @Override
            public void onComplete(String s) {
                DMListsResult lr = DMGsonUtil.gson2Lists(s);
                DMLists list = new DMLists();
                list.setId(-1);
                list.setName("全部");
                LinkedList<DMLists> resultList = new LinkedList<DMLists>();
                resultList = lr.getLists();
                resultList.add(0, list);
                mAdapter.setmLists(lr.getLists());
                Message msg = handler.obtainMessage();
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