package com.domee;

import java.io.IOException;
import java.util.LinkedList;

import com.domee.adapter.DMNAStatusAdapter;
import com.domee.manager.DMAccountsManager;
import com.domee.model.Status;
import com.domee.model.StatusResult;
import com.domee.model.User;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class DMUserTimelineActivity extends BaseActivity{

	private User user;
	private View headerView;
	private ImageButton utAvatar;
	private TextView utScreenName;
	private TextView utCity;
	private TextView utFriends;
	private TextView utFollowers;
	private TextView utDetail;
	private UtBtnListener utBtnListener = new UtBtnListener();
	
	private ListView utListView;
	
	private DMNAStatusAdapter adapter;
	private long since_id;
	private long max_id;
	public long total_number;
	//给listView加的一个view
	private LinearLayout utLinearLayout;
	public Button utButton;
	//title_user_timeline
	private ImageButton tbBack;
	private TextView tbTitle;
	
	public static void show(Activity activity, User user) {
		Intent intent = new Intent();
		intent.setClass(activity, DMUserTimelineActivity.class);
		intent.putExtra("user", user);
		activity.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setTheme(R.style.CustomTheme);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ac_user_timeline);     
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		
        adapter = new DMNAStatusAdapter(DMUserTimelineActivity.this, imageLoader, options, animateFirstListener);
        
		utLinearLayout = new LinearLayout(this);
		utButton = new Button(this);
		utButton.setText("加载更多");
		utButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadMore();
			}
		});
		utLinearLayout.addView(utButton);

		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");
		if (user == null) {
			user = DMAccountsManager.curAccount.getUser();
		}
		//title_bar控件处理
        tbBack = (ImageButton)findViewById(R.id.tbBack);
        tbTitle = (TextView)findViewById(R.id.tbTitle);
        tbBack.setOnClickListener(utBtnListener);
        tbTitle.setText("个人主页");
		
		headerView = LayoutInflater.from(this).inflate(R.layout.header_user_timeline, null);
		utAvatar = (ImageButton) headerView.findViewById(R.id.utAvatar);
		utScreenName = (TextView) headerView.findViewById(R.id.utScreenName);
		utCity = (TextView) headerView.findViewById(R.id.utCity);
		utFriends = (TextView) headerView.findViewById(R.id.utFriends);
		utFollowers = (TextView) headerView.findViewById(R.id.utFollowers);
		
		imageLoader.displayImage(user.getProfile_image_url(), utAvatar, options, animateFirstListener);
		utScreenName.setText(user.getScreen_name());
		utCity.setText(user.getProvince() + " " + user.getCity());
		utFriends.setText(user.getFriends_count() + "");
		utFollowers.setText(user.getFollowers_count() + "");
		
		utListView = (ListView) findViewById(R.id.utListView);
		utListView.addHeaderView(headerView);
		utListView.setAdapter(adapter);
		utListView.setBackgroundColor(android.R.color.white);
		utListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                    int position, long id) {
            	DMStatusShowActivity.show(DMUserTimelineActivity.this, adapter.getStaList().get(position - 1));
            }
        });
		
		//加载更多微博
		this.loadMore();
	}
	
	/*
	 * 加载更多微博
	 */
	public void loadMore() {
		StatusRequestListener listener = new StatusRequestListener();
		if (adapter.getStaList() != null && adapter.getStaList().size() != 0) {
			max_id = adapter.getStaList().get(adapter.getStaList().size() - 1).getId();
			statusesAPI.userTimeline(user.getId(), 0, max_id + 1, 20, 1, false, FEATURE.ALL, false, listener);
		} 
		else {
			statusesAPI.userTimeline(user.getId(), 0, 0, 20, 1, false, FEATURE.ALL, false, listener);
		}
	}
	

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
		};
	};
	public class StatusRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			StatusResult sr = gson.fromJson(arg0, new TypeToken<StatusResult>() {}.getType());
			LinkedList<Status> statusList = sr.getStatuses();
			LinkedList<Status> resultList = adapter.getStaList();
			if (resultList != null) {
				resultList.addAll(statusList);
			} else {
				resultList = statusList;
			}
			adapter.setStaList(resultList);
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
	
	public class UtBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tbBack:
				finish();
				break;
//			case R.id.tutAvatar:
//				break;
			default:
				break;
			}
		}
	}
}
