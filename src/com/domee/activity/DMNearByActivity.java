package com.domee.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.domee.R;
import com.domee.adapter.DMNearByAdapter;
import com.domee.model.DMNearBy;
import com.domee.model.DMPois;
import com.domee.utils.DMGsonUtil;
import com.domee.utils.DMLocationUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by duyuan on 13-5-22.
 */
public class DMNearByActivity extends BaseListActivity {
    //title_user_timeline
    private ImageButton tbBack;
    public TextView tbTitle;
    private DMNearByAdapter mAdapter;
    private PullToRefreshListView mPullToRefreshListView;
    private DMNearBy dmNearBy;

    private static DMComposeActivity.DMPoiSelectedListener mPoiSelectedListener = null;

    private double mLat;
    private double mLon;
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };
    public static void show(Activity mActivity, DMComposeActivity.DMPoiSelectedListener listener) {
        mPoiSelectedListener = listener;
        Intent intent = new Intent(mActivity, DMNearByActivity.class);
        mActivity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.CustomTheme);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.ac_near_by);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        //title_bar控件处理
        tbBack = (ImageButton)findViewById(R.id.tbBack);
        tbTitle = (TextView)findViewById(R.id.tbTitle);
        tbBack.setOnClickListener(new BaseListActivity.BtnListener());
        tbTitle.setText("选择地点");

        DMLocationUtil mLocationUtil = new DMLocationUtil(this);
        mLat = mLocationUtil.getmLat();
        mLon = mLocationUtil.getmLon();

        mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.nearby_pull_refresh_list);
        mAdapter = new DMNearByAdapter(this);
        mPullToRefreshListView.setAdapter(mAdapter);
        loadMore();
    }

    public void loadMore(){
        placeAPI.nearbyPois(Double.toString(mLat), Double.toString(mLon), 2000, null, null, 20, 1, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                dmNearBy = DMGsonUtil.gson2Pois(s);
                LinkedList<DMPois> poiList = dmNearBy.getPois();
                LinkedList<DMPois> resList = mAdapter.getPoisList();
                if (resList != null) {
                    resList.addAll(poiList);
                }else {
                    resList = poiList;
                }
                mAdapter.setPoisList(resList);

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mPoiSelectedListener.selected(this, mAdapter.getPoisList().get(position));
    }
}
