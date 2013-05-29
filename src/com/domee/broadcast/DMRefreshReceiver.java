package com.domee.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.domee.manager.DMUIManager;

/**
 * Created by duyuan on 13-5-27.
 */
public class DMRefreshReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DMUIManager.getInstance().getFriendsTimelineActivity().loadNew();
    }
}
