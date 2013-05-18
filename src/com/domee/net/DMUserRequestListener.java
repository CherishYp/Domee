package com.domee.net;

import java.io.IOException;

import android.os.Bundle;

import com.domee.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class DMUserRequestListener implements RequestListener {

	@Override
	public void onComplete(String arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		User user = gson.fromJson(arg0, User.class);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		
	}

	@Override
	public void onError(WeiboException arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
	}

	@Override
	public void onIOException(IOException arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
	}

}
