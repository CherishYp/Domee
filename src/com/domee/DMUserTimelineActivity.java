package com.domee;

import java.io.IOException;
import java.util.LinkedList;

import com.domee.R.layout;
import com.domee.manager.DMAccountsManager;
import com.domee.model.Status;
import com.domee.model.StatusResult;
import com.domee.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.AUTHOR_FILTER;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DMUserTimelineActivity extends BaseActivity {

	private User user;
	private View headerView;
	private ImageButton utAvatar;
	private TextView utScreenName;
	private TextView utCity;
	private TextView utFriends;
	private TextView utFollowers;
	private TextView utDetail;
	private UtBtnListener utBtnListener = new UtBtnListener();
	
	private LinkedList<Status> staList;
	private ListView utListView;
	private ViewHolder holder;
	public final class ViewHolder {
		private View utStatus;
		private TextView utCreatedAt;
		private TextView utText;
		private ImageView utImgView;
		private View utReStatus;
		private TextView utReText;
		private ImageView utReImgView;
		private TextView utReComment;
		private TextView utReRepost;
		private TextView utSource;
		private TextView utComment;
		private TextView utRepost;
	}
	private StatusAdapter adapter;
	private long since_id;
	private long max_id;
	private long total_number;
	//给listView加的一个view
	private LinearLayout utLinearLayout;
	private Button utButton;
	//title_user_timeline
	private ImageButton tbBack;
	private TextView tbTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setTheme(R.style.CustomTheme);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ac_user_timeline);     
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		
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
		adapter = new StatusAdapter(this);
		utListView.setAdapter(adapter);
		//加载更多微博
		this.loadMore();
	}
	
	public class StatusAdapter extends BaseAdapter {
	
		private LayoutInflater mInflater;
		public StatusAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (staList == null) {
				return 0;
			}
			return staList.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null || convertView == utLinearLayout) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_user_timeline, null);
				holder.utStatus = (View) convertView.findViewById(R.id.utStatus);
				holder.utCreatedAt = (TextView) convertView.findViewById(R.id.utCreatedAt);
				holder.utText = (TextView) convertView.findViewById(R.id.utText);
				holder.utImgView = (ImageView) convertView.findViewById(R.id.utImgView);
				holder.utReStatus = (View) convertView.findViewById(R.id.utReStatus);
				holder.utReText = (TextView) convertView.findViewById(R.id.utReText);
				holder.utReImgView = (ImageView) convertView.findViewById(R.id.utReImgView);
				holder.utReComment = (TextView) convertView.findViewById(R.id.utReComment);
				holder.utReRepost = (TextView) convertView.findViewById(R.id.utReRepost);
				holder.utSource = (TextView) convertView.findViewById(R.id.utSource);
				holder.utComment = (TextView) convertView.findViewById(R.id.utComment);
				holder.utRepost = (TextView) convertView.findViewById(R.id.utRepost);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (staList.size() == position) {
				if (total_number <= 20) {
					utButton.setVisibility(View.GONE);
				}
				return utLinearLayout;
			}
			
			Status status = staList.get(position);
		 	holder.utCreatedAt.setText("12:20");
			holder.utText.setText(status.getText());
			//判断微博是否有图片
			if (status.getThumbnail_pic() != null && !status.getThumbnail_pic().equals("")) {
				imageLoader.displayImage(status.getThumbnail_pic(), holder.utImgView, options, animateFirstListener);
				holder.utImgView.setVisibility(View.VISIBLE);
			} else {
				holder.utImgView.setVisibility(View.GONE);
			}
			//判断微博是否是转发的
			if (status.getRetweeted_status() != null) {
				if (status.getRetweeted_status().getUser() != null) {
					holder.utReText.setText(status.getRetweeted_status().getUser().getScreen_name() + ":" + status.getRetweeted_status().getText());
				}else {
					holder.utReText.setText(status.getRetweeted_status().getText());
				}
				//判断转发的微博是否带图片
				if (status.getRetweeted_status().getThumbnail_pic() != null && !status.getRetweeted_status().getThumbnail_pic().equals("")) {
					imageLoader.displayImage(status.getRetweeted_status().getThumbnail_pic(), holder.utReImgView, options, animateFirstListener);
					holder.utReImgView.setVisibility(View.VISIBLE);
				}else {
					holder.utReImgView.setVisibility(View.GONE);
				}
				holder.utReComment.setText("评论" + status.getRetweeted_status().getComments_count() + "");
				holder.utReRepost.setText("转发" + status.getRetweeted_status().getReposts_count() + "");
			
				holder.utReStatus.setVisibility(View.VISIBLE);
			} else {
				holder.utReStatus.setVisibility(View.GONE);
			}
			holder.utSource.setText(Html.fromHtml(status.getSource()));
			holder.utComment.setText("评论" + status.getComments_count() + "");
			holder.utRepost.setText("转发" + status.getReposts_count() + "");
			
			return convertView;
		}
	}
	/*
	 * 加载更多微博
	 */
	public void loadMore() {
		StatusRequestListener listener = new StatusRequestListener();
		if (staList != null && staList.size() != 0) {
			max_id = staList.get(staList.size() - 1).getId();
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
			if (staList != null) {
				staList.addAll(sr.getStatuses());
			} else {
				
				staList = sr.getStatuses();
			}
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
