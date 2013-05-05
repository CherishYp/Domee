package com.domee;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.domee.manager.AccountsManager;
import com.domee.model.Account;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author duyuan
 * 
 */
public class MainActivity extends Activity {

	private static final String CONSUMER_KEY = "966056985"; // 替换为开发者的appkey，例如"1646212860";
	private static final String REDIRECT_URL = "http://www.sina.com";
	public static final String TAG = "sinasdk";
	public static Oauth2AccessToken accessToken;
	public SsoHandler ssoHandler;

	private Weibo weibo;
	private Button authBtn = null;
	private Button ssoBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_main);
		AccountsManager.initContext(MainActivity.this);
        if (AccountsManager.getCurAccount() != null) {
        	Intent intent = new Intent(this, AccListActivity.class);
//        	List<Account> accounts = AccountsManager.getAccounts();
//        	intent.putExtra("accounts", (Serializable) accounts);
        	startActivity(intent);
		}
		
		weibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		authBtn = (Button) findViewById(R.id.authBtn);
//		authBtn.setOnClickListener(new AuthBtnListener());

		ssoBtn = (Button) findViewById(R.id.ssoBtn);
		try {
			Class sso = Class.forName("com.weibo.sdk.android.sso.SsoHandler");
			ssoBtn.setVisibility(View.VISIBLE);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "com.weibo.sdk.android.sso.SsoHandler not found....");
			e.printStackTrace();
		}
		ssoBtn.setOnClickListener(new SsoBtnListener());

	}

//	/*
//	 * 使用auth认证
//	 */
//	class AuthBtnListener implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//		}
//
//	}

	/*
	 * sso认证 监听器
	 */
	class SsoBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			ssoHandler = new SsoHandler(MainActivity.this, weibo);
			ssoHandler.authorize(new AuthDialogListener());
		}
	}

	public class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			
			String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            MainActivity.accessToken = new Oauth2AccessToken(token, expires_in);
            if (MainActivity.accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                        .format(new java.util.Date(MainActivity.accessToken
                                .getExpiresTime()));
                Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT)
                        .show();
                Account account = Account.accountWithOAuthBundle(values);
                AccountsManager.createAccount(account);
            }
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(WeiboDialogError arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 下面两个注释掉的代码，仅当sdk支持sso时有效，
         */
        if (ssoHandler != null) {
        	ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
