package com.domee;

import java.io.IOException;
import java.util.LinkedList;

import com.domee.adapter.DMStatusAdapter;
import com.domee.manager.DMUIManager;
import com.domee.model.Status;
import com.domee.model.StatusResult;
import com.domee.utils.GsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.AUTHOR_FILTER;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.api.WeiboAPI.SRC_FILTER;
import com.weibo.sdk.android.api.WeiboAPI.TYPE_FILTER;
import com.weibo.sdk.android.net.RequestListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class DMAtActivity extends BaseListActivity implements OnScrollListener {
	
	private static DMStatusAdapter adapter = null;
	private static PullToRefreshListView mPullToRefreshListView;
	private long since_id = 0;
	private long max_id = 0;
	
	private int temp = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_profile);        
		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		adapter = new DMStatusAdapter(this, imageLoader, options, animateFirstListener); 
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "写微博");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			Intent intent = new Intent();
			intent.setClass(this, DMComposeActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				mPullToRefreshListView.onRefreshComplete();
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	
	/*
	 * 加载新的动态
	 */
	public void loadNew() {

		if (adapter.getStatusList() != null) {
			since_id = adapter.getStatusList().get(0).getId();
		}
		FriendsTimelineRequestListener loadMoreListener = new FriendsTimelineRequestListener(true);

		statusesAPI.mentions(since_id, 0, 20, 1, AUTHOR_FILTER.ALL, SRC_FILTER.ALL, TYPE_FILTER.ALL, false, loadMoreListener);
	}

	/*
	 * 加载更多
	 */
	public void loadMore() {
		
		FriendsTimelineRequestListener loadMoreListener = new FriendsTimelineRequestListener(false);
//		if (statusList != null) {
//			max_id = statusList.get(statusList.size() - 1).getId();
//		}
		statusesAPI.mentions(0, max_id, 20, 1, AUTHOR_FILTER.ALL, SRC_FILTER.ALL, TYPE_FILTER.ALL, false, loadMoreListener);
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
			StatusResult sr = GsonUtil.gson2Status(arg0);
			System.out.println("load到的 ======= " + sr.getStatuses().size());
			max_id = Long.parseLong(sr.getNext_cursor());
			LinkedList<Status> statusList = sr.getStatuses();
			LinkedList<Status> resultList = adapter.getStatusList();
			if(resultList == null){
				resultList = statusList;
			}
			else if (!isLoadNew) {
				resultList.addAll(statusList);
			}else{
				resultList.addAll(0, statusList);
			}
			adapter.setStatusList(resultList);
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
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		System.out.println(firstVisibleItem);
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
		intent.setClass(this, DMStatusShowActivity.class);
		intent.putExtra("status", adapter.getStatusList().get(position - 1));
		startActivityForResult(intent, 0);
	}
	
}    