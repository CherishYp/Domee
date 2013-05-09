package com.domee;

import java.io.IOException;
import java.util.LinkedList;

import com.domee.model.Status;
import com.domee.model.StatusResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsTimelineActivity extends BaseListActivity implements OnScrollListener {

	private LinkedList<Status> statusList = null;
	private TimelineAdapter adapter = null; 
	private PullToRefreshListView mPullToRefreshListView;
	private long since_id = 0;
	private long max_id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_timeline);
		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		
		adapter = new TimelineAdapter(this); 
		mPullToRefreshListView.setAdapter(adapter);
		
		this.loadNew();
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			    loadNew();
			}
		});
		//绑定OnScrollListener监听器
		getListView().setOnScrollListener(this);
	}
	
	public final class ViewHolder {

		public ImageView ftAvatar;
		public TextView ftScreenName;
		public TextView ftCreatedAt;
		public TextView ftText;
		public ImageView ftImgView;
		public TextView ftSource;
		public TextView ftRepost;
		public TextView ftComment;
		
		public View ftReStatus;
		public TextView ftReText;
		public ImageView ftReImgView;
		public TextView ftReRepost;
		public TextView ftReComment;
	}
	
	public class TimelineAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public TimelineAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (statusList == null) {
				return 0;
			}
			return statusList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_timeline, null);

				holder.ftAvatar = (ImageView) convertView.findViewById(R.id.ftAvatar);
				holder.ftScreenName = (TextView) convertView.findViewById(R.id.ftScreenName);
		    	holder.ftCreatedAt = (TextView) convertView.findViewById(R.id.ftCreatedAt);
				holder.ftText = (TextView) convertView.findViewById(R.id.ftText);
				holder.ftImgView = (ImageView) convertView.findViewById(R.id.ftImgView);
			    holder.ftSource = (TextView) convertView.findViewById(R.id.ftSource);
				holder.ftRepost = (TextView) convertView.findViewById(R.id.ftRepost);
				holder.ftComment = (TextView) convertView.findViewById(R.id.ftComment);
				
				holder.ftReStatus = convertView.findViewById(R.id.ftReStatus);
				holder.ftReText = (TextView) convertView.findViewById(R.id.ftReText);
				holder.ftReImgView = (ImageView) convertView.findViewById(R.id.ftReImgView);
				holder.ftReRepost = (TextView) convertView.findViewById(R.id.ftReRepost);
				holder.ftReComment = (TextView) convertView.findViewById(R.id.ftReComment);
				
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			Status sta = statusList.get(position);
			imageLoader.displayImage(sta.getUser().getProfile_image_url(), holder.ftAvatar, options, animateFirstListener);
			//判断是否带图片微博
			if(sta.getThumbnail_pic() != null && !sta.getThumbnail_pic().equals("")) {
				imageLoader.displayImage(sta.getThumbnail_pic(), holder.ftImgView, options, animateFirstListener);
				holder.ftImgView.setVisibility(View.VISIBLE);
			} else {
				holder.ftImgView.setVisibility(View.GONE);
			}
			
			holder.ftScreenName.setText(sta.getUser().getScreen_name());
			holder.ftCreatedAt.setText("2013.5.6");
			holder.ftText.setText("");
			holder.ftText.setText(sta.getText());
			holder.ftSource.setText(Html.fromHtml(sta.getSource()));
			stripUnderlines(holder.ftSource, sta.getSource());
			
			holder.ftRepost.setText(sta.getReposts_count() + "");
			holder.ftComment.setText(sta.getComments_count() + "");
			
			//判断是否转发
			if (sta.getRetweeted_status() != null) {
				//判断微博是否存在
				if(sta.getRetweeted_status().getUser() != null){
					holder.ftReText.setText(sta.getRetweeted_status().getUser().getScreen_name() + ":"
							+ sta.getRetweeted_status().getText());
				}else{
					holder.ftReText.setText(sta.getRetweeted_status().getText());
				}
				//判断转发的微博是否有图片
				if(sta.getRetweeted_status().getThumbnail_pic() != null && !sta.getRetweeted_status().getThumbnail_pic().equals("")) {
					imageLoader.displayImage(sta.getRetweeted_status().getThumbnail_pic(), holder.ftReImgView, options, animateFirstListener);
					holder.ftReImgView.setVisibility(View.VISIBLE);
				} else {
					holder.ftReImgView.setVisibility(View.GONE);
				}
				holder.ftReRepost.setText("转发" + sta.getRetweeted_status().getReposts_count() + "");
				holder.ftReComment.setText("评论" + sta.getRetweeted_status().getComments_count() + "");
				holder.ftReStatus.setVisibility(View.VISIBLE);
			} else {
				holder.ftReStatus.setVisibility(View.GONE);
			}
			return convertView;
		}
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mPullToRefreshListView.onRefreshComplete();
			adapter.notifyDataSetChanged();
		}
	};
	
	/*
	 * 加载新的动态
	 */
	public void loadNew() {

		if (statusList != null) {
			since_id = statusList.get(0).getId();
		}
		statusesAPI.friendsTimeline(since_id, 0, 20, 1, false, FEATURE.ALL, false,
				new FriendsTimelineRequestListener(true));
	}

	/*
	 * 加载更多
	 */
	public void loadMore() {
		
		FriendsTimelineRequestListener loadMoreListener = new FriendsTimelineRequestListener(false);
//		if (statusList != null) {
//			max_id = statusList.get(statusList.size() - 1).getId();
//		}
		statusesAPI.friendsTimeline(0, max_id, 20, 1, false, FEATURE.ALL, false, loadMoreListener);
	}

	/*
	 * RequestListener,请求返回的json数据在里面的onComplete方法获得
	 */
	class FriendsTimelineRequestListener implements RequestListener {
		
		public boolean isLoadNew = true;
		
		public FriendsTimelineRequestListener(boolean isLoadNew) {
			super();
			this.isLoadNew = isLoadNew;
		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			StatusResult sr = gson.fromJson(arg0, new TypeToken<StatusResult>(){}.getType());
			System.out.println("load到的 ======= " + sr.getStatuses().size());
			max_id = Long.parseLong(sr.getNext_cursor());
			if(statusList == null){
				statusList = sr.getStatuses();
			}
			else if (!isLoadNew) {
				statusList.addAll(sr.getStatuses());
			}else{
				statusList.addAll(0, sr.getStatuses());
			}
			System.out.println("load之后总共 ======= " + sr.getStatuses().size());
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
	
	/*
	 * 实现滑动监听器
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (scrollState){
		 	case OnScrollListener.SCROLL_STATE_IDLE:
	            if (view.getLastVisiblePosition() == (view.getCount() - 1)){
	            	loadMore();
	            }               
		}        
	}
	
	//点击list
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent();
		intent.setClass(this, StatusShowActivity.class);
		intent.putExtra("status", statusList.get(position - 1));
		startActivity(intent);
	}
}    
	
