package com.domee.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.domee.R;
import com.domee.adapter.DMStatusAdapter;
import com.domee.manager.DMAccountsManager;
import com.domee.model.DMStatus;
import com.domee.model.DMStatusResult;
import com.domee.utils.DMConstants;
import com.domee.utils.DMGsonUtil;
import com.weibo.api.SearchAPI;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import java.io.IOException;
import java.util.LinkedList;

public class DMTrendActivity extends BaseActivity {

    private static final Uri TREND_URI = Uri.parse(DMConstants.TRENDS_SCHEMA);
    private static final String TAG = "DMTrendActivity";

    //title_user_timeline
    private ImageButton tbBack;
    public TextView tbTitle;
    private String trend;
    private ListView mListView;
    private DMStatusAdapter mAdapter;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };
    public void show(Activity mActivity, String trend) {
        Intent intent = new Intent(mActivity, DMTrendActivity.class);
        intent.putExtra("trend", trend);
        mActivity.startActivityForResult(intent, 1);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        setTheme(R.style.CustomTheme);
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.ac_trend);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        //title_bar控件处理
        tbBack = (ImageButton)findViewById(R.id.tbBack);
        tbTitle = (TextView)findViewById(R.id.tbTitle);
        tbBack.setOnClickListener(new BtnListener());

        extractTrendFromUri();
        tbTitle.setText(trend);

        mListView = (ListView) findViewById(R.id.t_listView);
        mAdapter = new DMStatusAdapter(this, imageLoader, options, animateFirstListener);
        mListView.setAdapter(mAdapter);
        this.loadMore();

	}
    private void extractTrendFromUri() {
        Uri uri = getIntent().getData();
//        Uri uri = new Uri.Builder().scheme("domee://trend/?trendname=#玩转微博#");
//        && PROFILE_URI.getScheme().equals(uri.getScheme())
        System.out.println(uri.toString());
        if (uri != null) {
            trend = uri.getQueryParameter(DMConstants.PARAM_TREND);
//            if (trend != null && trend.indexOf("#") != -1) {
//                trend = trend.substring(1, trend.length() - 1);
//            }
            Log.d(TAG, "mScreenName from url: " + trend);
        }

//        if (uri != null && PROFILE_URI.getScheme().equals(uri.getScheme())) {
//            mScreenName = uri.getQueryParameter(DMConstants.PARAM_SCREEN_NAME);
//            if (mScreenName != null && mScreenName.indexOf("@") != -1) {
//                mScreenName = mScreenName.substring(1);
//            }
//            Log.d(TAG, "mScreenName from url: "+ mScreenName);
//        }
    }

    public void loadMore() {
        SearchAPI searchAPI = new SearchAPI(DMAccountsManager.curAccessToken());
        searchAPI.topics(trend, 20, 1, new RequestListener() {
            @Override
            public void onComplete(String s) {
                DMStatusResult sr = DMGsonUtil.gson2Topic(s);
                LinkedList<DMStatus> statusList = sr.getStatuses();
                LinkedList<DMStatus> resultList = mAdapter.getStatusList();
                if (resultList != null) {
                    resultList.addAll(statusList);
                } else {
                    resultList = statusList;
                }
                mAdapter.setStatusList(resultList);

                Message msg = handler.obtainMessage();
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
        });
    }

}
