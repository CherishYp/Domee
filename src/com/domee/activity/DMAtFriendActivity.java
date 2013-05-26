package com.domee.activity;

import java.io.IOException;
import java.util.LinkedList;

import android.widget.*;
import com.domee.R;
import com.domee.adapter.DMFriendAdapter;
import com.domee.manager.DMAccountsManager;
import com.domee.model.FriendsResult;
import com.domee.model.User;
import com.domee.utils.DMGsonUtil;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;

public class DMAtFriendActivity extends BaseActivity implements OnScrollListener {

	private ListView mListView;
    private static DMUserSelectedListener dmUserSelectedListener;
	
	//title_user_timeline
	private ImageButton tbBack;
	private TextView tbTitle;
	
	private static DMFriendAdapter adapter;
	private int cursor;

	public static void show(Activity activity, DMUserSelectedListener listener) {
		Intent intent = new Intent(activity, DMAtFriendActivity.class);
		activity.startActivityForResult(intent, 0);
        dmUserSelectedListener = listener;
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
        tbBack.setOnClickListener(new UtBtnListener());
        tbTitle.setText("选择好友");
        
        adapter = new DMFriendAdapter(this, imageLoader, options, animateFirstListener);
        mListView = (ListView) findViewById(R.id.utListView);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                DMComposeActivity.show(DMAtFriendActivity.this);
                dmUserSelectedListener.userSelected(DMAtFriendActivity.this, adapter.getUserList().get(i));
            }
        });
        
        this.loadMore();
	}
	
	public void loadMore() {
		FriendsRequestListener listener = new FriendsRequestListener();
		friendshipsAPI.friends(DMAccountsManager.getCurUser().getId(), 20, cursor, false, listener);
	}
	
	public class FriendsRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			FriendsResult fr = DMGsonUtil.gson2Friends(arg0);
			
			cursor = Integer.parseInt(fr.getNext_cursor());
			LinkedList<User> userList = fr.getUsers();
			LinkedList<User> resultList = adapter.getUserList();
			if(resultList == null){
				resultList = userList;
			}else {
				resultList.addAll(userList);
			}
			adapter.setUserList(resultList);
			if (fr.getTotal_number().equals(resultList.size())) {
				Toast toast = new Toast(DMAtFriendActivity.this);
			} else {
				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
			}
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
	
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};	
	public class UtBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tbBack:
				finish();
				break;
			default:
				break;
			}
		}
	}
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




}
