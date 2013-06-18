package com.domee.activity;

import android.app.Activity;
import android.os.Message;
import com.domee.model.DMStatus;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import java.io.IOException;

/**
 * Created by duyuan on 13-6-4.
 */
public class DMFavActivity extends BaseActivity {

    public void show(Activity mActivity, DMStatus mStatus) {


    }

//    public void addFav() {
//        favoritesAPI.create(status.getId(), new RequestListener() {
//            @Override
//            public void onComplete(String s) {
//                Message msg = handler.obtainMessage();
//                msg.what = 1;
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onIOException(IOException e) {
//
//            }
//
//            @Override
//            public void onError(WeiboException e) {
//
//            }
//        });
//    }
}
