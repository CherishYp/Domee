package com.domee.activity;

import java.io.IOException;
import java.util.LinkedList;

import android.net.Uri;
import android.util.Log;
import android.widget.*;
import com.domee.R;
import com.domee.adapter.DMNAStatusAdapter;
import com.domee.manager.DMAccountsManager;
import com.domee.model.DMStatus;
import com.domee.model.DMStatusResult;
import com.domee.model.User;
import com.domee.utils.DMConstants;
import com.domee.utils.DMGsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ResourceAsColor")
public class DMUserTimelineActivity extends BaseActivity{

    private static final String TAG = "DMUserTimelineActivity";

	private User mUser;
	private View headerView;
	private ImageButton utAvatar;
	private TextView utScreenName;
	private TextView mLocation;
	private TextView utFriends;
	private TextView utFollowers;
	private ImageView utDetail;
	private ListView mListView;
	
	private DMNAStatusAdapter mAdapter;
	private long since_id;
	private long max_id;
	public long total_number;
	//给listView加的一个view
	private LinearLayout utLinearLayout;
	public Button utButton;

    //title_user_timeline
    private ImageButton tbBack;
    public TextView tbTitle;
    private static final Uri PROFILE_URI = Uri.parse(DMConstants.MENTIONS_SCHEMA);
    private String mScreenName;

	public static void show(Activity activity, User user) {
		Intent intent = new Intent();
		intent.setClass(activity, DMUserTimelineActivity.class);
		intent.putExtra("user", user);
		activity.startActivity(intent);
	}

    private void extractScreenNameFromUri() {
        Uri uri = getIntent().getData();
//        && PROFILE_URI.getScheme().equals(uri.getScheme())
        if (uri != null && PROFILE_URI.getScheme().equals(uri.getScheme())) {
            mScreenName = uri.getQueryParameter(DMConstants.PARAM_SCREEN_NAME);
            if (mScreenName != null && mScreenName.indexOf("@") != -1) {
                mScreenName = mScreenName.substring(1);
            }
            Log.d(TAG, "mScreenName from url: "+ mScreenName);
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setTheme(R.style.CustomTheme);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ac_user_timeline);     
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        //title_bar控件处理
        tbBack = (ImageButton)findViewById(R.id.tbBack);
        tbTitle = (TextView)findViewById(R.id.tbTitle);
        tbBack.setOnClickListener(new BtnListener());
        tbTitle.setText("个人主页");

        mAdapter = new DMNAStatusAdapter(DMUserTimelineActivity.this, imageLoader, options, animateFirstListener);

//		utLinearLayout = new LinearLayout(this);
//		utButton = new Button(this);
//		utButton.setText("加载更多");
//		utButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				loadMore();
//			}
//		});
//		utLinearLayout.addView(utButton);

        headerView = LayoutInflater.from(DMUserTimelineActivity.this).inflate(R.layout.header_user_timeline, null);
        utAvatar = (ImageButton) headerView.findViewById(R.id.utAvatar);
        utScreenName = (TextView) headerView.findViewById(R.id.utScreenName);
        mLocation = (TextView) headerView.findViewById(R.id.ut_location);
        utFriends = (TextView) headerView.findViewById(R.id.utFriends);
        utFollowers = (TextView) headerView.findViewById(R.id.utFollowers);
        utDetail = (ImageView) headerView.findViewById(R.id.utDetail);

        extractScreenNameFromUri();
        if (mScreenName == null) {
            Intent intent = getIntent();
            mUser = (User) intent.getSerializableExtra("user");
            if (mUser == null) {
                mUser = DMAccountsManager.curAccount.getUser();
            }
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
        } else {
            usersAPI.show(mScreenName, new UserRequestListener());
        }
		mListView = (ListView) findViewById(R.id.utListView);
		mListView.addHeaderView(headerView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                    int position, long id) {
            	DMStatusShowActivity.show(DMUserTimelineActivity.this, mAdapter.getStaList().get(position - 1));
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)){
                            loadMore();
                        }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });
		//加载更多微博
		//this.loadMore();
	}

	/*
	 * 加载更多微博
	 */
	public void loadMore() {
		StatusRequestListener listener = new StatusRequestListener();
		if (mAdapter.getStaList() != null && mAdapter.getStaList().size() != 0) {
			max_id = mAdapter.getStaList().get(mAdapter.getStaList().size() - 1).getId();
			statusesAPI.userTimeline(mUser.getId(), 0, max_id + 1, 20, 1, false, FEATURE.ALL, false, listener);
		} 
		else {
			statusesAPI.userTimeline(mUser.getId(), 0, 0, 20, 1, false, FEATURE.ALL, false, listener);
		}
	}
	

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    imageLoader.displayImage(mUser.getProfile_image_url(), utAvatar, options, animateFirstListener);
                    utScreenName.setText(mUser.getScreen_name());
                    mLocation.setText(mUser.getLocation());
                    utFriends.setText(mUser.getFriends_count() + "");
                    utFollowers.setText(mUser.getFollowers_count() + "");
                    utDetail.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DMUserDetailActivity.show(DMUserTimelineActivity.this, mUser);
                        }
                    });
                    loadMore();
                    break;
                default:
                    break;
            }
		};
	};
	public class StatusRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			DMStatusResult sr = gson.fromJson(arg0, new TypeToken<DMStatusResult>() {}.getType());
			LinkedList<DMStatus> statusList = sr.getStatuses();
			LinkedList<DMStatus> resultList = mAdapter.getStaList();
			if (resultList != null) {
				resultList.addAll(statusList);
			} else {
				resultList = statusList;
			}
			mAdapter.setStaList(resultList);
//			max_id = Long.parseLong(sr.getNext_cursor());
			total_number = Long.parseLong(sr.getTotal_number());
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub
			
		}
	}

    public class UserRequestListener implements RequestListener{

        @Override
        public void onComplete(String s) {
            mUser = DMGsonUtil.gson2User(s);
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
        }

        @Override
        public void onIOException(IOException e) {
            e.printStackTrace();
            System.out.println();
        }

        @Override
        public void onError(WeiboException e) {
            e.printStackTrace();
            System.out.println();
        }
    }
}
