package com.domee.activity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.*;
import com.domee.R;
import com.domee.adapter.DMFStatusAdapter;
import com.domee.interFace.DMGroupInterface;
import com.domee.interFace.DMRefreshInterface;
import com.domee.manager.DMAccountsManager;
import com.domee.manager.DMUIManager;
import com.domee.model.DMLists;
import com.domee.model.DMListsResult;
import com.domee.model.DMStatus;
import com.domee.model.DMStatusResult;
import com.domee.utils.DMGsonUtil;
import com.domee.utils.DMSyncImgLoader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.weibo.api.FriendShipAPI;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.widget.AbsListView.OnScrollListener;

public class DMFriendsTimelineActivity extends BaseListActivity implements DMRefreshInterface, DMGroupInterface {

	private static DMFStatusAdapter adapter = null;
	private static PullToRefreshListView mPullToRefreshListView;
	private long since_id = 0;
	private long max_id = 0;
    private long total_number = 0;
    //list_footer
    private LinearLayout mListFooter;
    private TextView mMore;

	private int temp = 0;
    private long exitTime = 0;
    private LinkedList<DMLists> mLists;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再次点击返回键退出应用程序！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ExitDialog(this).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_friend_timeline);

		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		adapter = new DMFStatusAdapter(this, getListView(), imageLoader, options, animateFirstListener);
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

        //绑定OnScrollListener监听器
//		getListView().setOnScrollListener(this);
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
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        }));
		DMUIManager.getInstance().setFriendsTimelineActivity(this);
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
                case 1:

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

//		if (adapter.getStatusList() != null) {
//			since_id = adapter.getStatusList().get(0).getId();
//		}
        DMAccountsManager.curAccount.getGroupType();
        if (DMAccountsManager.curAccount.getGroupType().equals("-1")) {
            statusesAPI.friendsTimeline(since_id, 0, 20, 1, false, FEATURE.ALL, false,
                    new FriendsTimelineRequestListener(true));
        } else {
            FriendShipAPI friendShipAPI = new FriendShipAPI(DMAccountsManager.curAccessToken());
            friendShipAPI.timeline(DMAccountsManager.curGroupType(), since_id, 0, 20, 1, false, FEATURE.ALL, false,
                    new FriendsTimelineRequestListener(true));
        }
	}

	/*
	 * 加载更多
	 */
	public void loadMore() {

		FriendsTimelineRequestListener listener = new FriendsTimelineRequestListener(false);
//		if (statusList != null) {
//			max_id = statusList.get(statusList.size() - 1).getId();
//		}

        if (DMAccountsManager.curGroupType().equals("-1")) {
            statusesAPI.friendsTimeline(0, max_id - 1, 20, 1, false, FEATURE.ALL, false, listener);
        } else {
            FriendShipAPI friendShipAPI = new FriendShipAPI(DMAccountsManager.curAccessToken());
            friendShipAPI.timeline(DMAccountsManager.curGroupType(), since_id, 0, 20, 1, false, FEATURE.ALL, false,
                    listener);
        }
	}

    @Override
    public void refresh() {
        mPullToRefreshListView.startRefreshing();
        loadNew();
//        TimerTask task = new TimerTask(){
//            public void run(){
//                //execute the task
//
//            }
//        };
//        long delay = 100000;
//        Timer timer = new Timer();
//        timer.schedule(task, delay);
    }

    @Override
    public void group() {
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
                mLists = lr.getLists();
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
			DMStatusResult sr = DMGsonUtil.gson2Status(arg0);
			System.out.println("load到的 ======= " + sr.getStatuses().size());
			max_id = Long.parseLong(sr.getNext_cursor());
            since_id = Long.parseLong(sr.getPrevious_cursor());
            total_number = Long.parseLong(sr.getTotal_number());
			LinkedList<DMStatus> statusList = sr.getStatuses();
			LinkedList<DMStatus> resultList = adapter.getStatusList();
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

//	/*
//	 * 实现滑动监听器
//	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
//	 */
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		// TODO Auto-generated method stub
//		switch (scrollState){
//		 	case OnScrollListener.SCROLL_STATE_IDLE:
//	            if (view.getLastVisiblePosition() == (view.getCount() - 1)){
//	            	loadMore(); //当滚到最后一行且停止滚动时，执行加载
//	            }
//                break;
//            default:
//                break;
//        }
//	}

    /**
     * 提示退出系统
     * @param context
     * @return
     */
    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("系统信息");
        builder.setMessage("确定要退出程序吗?");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }

}