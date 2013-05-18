package com.domee.broadcast;

import com.domee.manager.DMUIManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

public class DMSendStatusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("SEND_STATUS_ACTION");
		DMUIManager.getInstance().getFriendsTimelineActivity().loadNew();
		Toast toast = new Toast(context);
		toast = Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.show();
	}
}
