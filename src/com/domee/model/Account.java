package com.domee.model;

import java.io.Serializable;

import android.os.Bundle;

import com.weibo.sdk.android.Oauth2AccessToken;

public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String loginId;
	private String loginPwd;

	private String mAccessToken = "";
	private String mRefreshToken = "";
	private String mExpiresTime = "";
	
	private User user;

	public static Account accountWithOAuthBundle(Bundle values) {
//      Bundle[{uid=1774230022, remind_in=2569820, userName=周远远, expires_in=2569820, 
//		refresh_token=2.00QRUEwBRYT4DBd703277a37Ub8HDB, access_token=2.00QRUEwBRYT4DBe684381c5cKLtvxD}]
		User user = new User();
		user.setId(Long.parseLong(values.getString("uid")));
		user.setScreen_name(values.getString("userName"));
	
		Account account = new Account();
		account.setUser(user);
		account.setLoginId(values.getString("uid"));
		account.setmRefreshToken(values.getString("refresh_token"));
		account.setmAccessToken(values.getString("access_token"));
		account.setmExpiresTime(values.getString("expires_in"));
		return account;
	}
	
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getmAccessToken() {
		return mAccessToken;
	}

	public void setmAccessToken(String mAccessToken) {
		this.mAccessToken = mAccessToken;
	}

	public String getmRefreshToken() {
		return mRefreshToken;
	}

	public void setmRefreshToken(String mRefreshToken) {
		this.mRefreshToken = mRefreshToken;
	}

	public String getmExpiresTime() {
		return mExpiresTime;
	}

	public void setmExpiresTime(String mExpiresTime) {
		this.mExpiresTime = mExpiresTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "loginid===" + loginId + "mAccessToken====" + mAccessToken;
	}
	
}
