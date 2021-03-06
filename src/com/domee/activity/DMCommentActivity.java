package com.domee.activity;

import java.io.IOException;
import java.util.LinkedList;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.domee.R;
import com.domee.adapter.DMCommentAdapter;
import com.domee.interFace.DMRefreshInterface;
import com.domee.model.DMComment;
import com.domee.model.CommentResult;
import com.domee.utils.DMGsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.AUTHOR_FILTER;
import com.weibo.sdk.android.api.WeiboAPI.SRC_FILTER;
import com.weibo.sdk.android.net.RequestListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class DMCommentActivity extends BaseListActivity implements DMRefreshInterface {
	
	private static DMCommentAdapter adapter = null;
	private static PullToRefreshListView mPullToRefreshListView;
	private long since_id;
	private long max_id;
    //list_footer
    private LinearLayout mListFooter;
    private TextView mMore;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_comment);
		
		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		adapter = new DMCommentAdapter(this, imageLoader, options, animateFirstListener);
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

        mListFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_footer, null);
        getListView().addFooterView(mListFooter);
        mMore = (TextView) mListFooter.findViewById(R.id.f_more);
        getListView().setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true, new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                            loadMore(); //当滚到最后一行且停止滚动时，执行加载
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int i, int i2, int i3) {

            }
        }));
	}
	
	public void loadNew() {
//		if (adapter.getComList() != null) {
//			since_id = adapter.getComList().get(0).getId();
//		}
		commentsAPI.toME(since_id, 0, 20, 1, AUTHOR_FILTER.ALL, SRC_FILTER.ALL, new ComToMERequestListener());
	}
	public void loadMore() {
		commentsAPI.toME(0, max_id, 20, 1, AUTHOR_FILTER.ALL, SRC_FILTER.ALL, new ComToMERequestListener());
	}

    @Override
    public void refresh() {
        loadNew();
    }

    private class ComToMERequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			Log.w("DMCommentActivity", arg0);
			System.out.println(arg0);
			CommentResult cr = DMGsonUtil.gson2Comment(arg0);
			max_id = Long.parseLong(cr.getNext_cursor());
			since_id = Long.parseLong(cr.getPrevious_cursor());
            LinkedList<DMComment> resList = adapter.getComList();
            LinkedList<DMComment> comList = cr.getComments();
            if (resList != null) {
                resList.addAll(comList);
            }else {
                resList = comList;
            }
			adapter.setComList(resList);
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
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		// TODO Auto-generated method stub
//		switch (scrollState){
//	 	case OnScrollListener.SCROLL_STATE_IDLE:
//            if (view.getLastVisiblePosition() == (view.getCount() - 1)){
//            	loadMore();
//            }
//		}
//	}
	
	//点击list
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

//        mAddLayout.setVisibility(mAddLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
	}
}
