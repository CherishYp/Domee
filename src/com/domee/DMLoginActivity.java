package com.domee;

import java.text.SimpleDateFormat;

import com.domee.manager.DMAccountsManager;
import com.domee.model.Account;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DMLoginActivity extends Activity {

	private static final String CONSUMER_KEY = "966056985"; // 替换为开发者的appkey，例如"1646212860";
	private static final String REDIRECT_URL = "http://www.sina.com";
	public static final String TAG = "sinasdk";
	public static Oauth2AccessToken accessToken;
	public SsoHandler ssoHandler;

	private Button lOauth;
	private Button lSSo;
	private Weibo weibo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_login);
		
		lOauth = (Button) findViewById(R.id.lOauth);
		lSSo = (Button) findViewById(R.id.lSso);
		
		weibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		try {
			Class sso = Class.forName("com.weibo.sdk.android.sso.SsoHandler");
			lSSo.setVisibility(View.VISIBLE);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "com.weibo.sdk.android.sso.SsoHandler not found....");
			e.printStackTrace();
		}
		lSSo.setOnClickListener(new SsoBtnListener());
	}
	
	/*
	 * sso认证 监听器
	 */
	class SsoBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			ssoHandler = new SsoHandler(DMLoginActivity.this, weibo);
			ssoHandler.authorize(new AuthDialogListener());
		}
	}
	
	public class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			
			String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            DMLoginActivity.accessToken = new Oauth2AccessToken(token, expires_in);
            if (DMLoginActivity.accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                        .format(new java.util.Date(DMLoginActivity.accessToken
                                .getExpiresTime()));
                Toast.makeText(DMLoginActivity.this, "认证成功", Toast.LENGTH_SHORT)
                        .show();
                Account account = Account.accountWithOAuthBundle(values);
                DMAccountsManager.createAccount(account);
//                Intent intent = new Intent(DMLoginActivity.this, DMFriendsTimelineActivity.class);
                Intent intent = new Intent(DMLoginActivity.this, MainActivity.class);
                DMLoginActivity.this.startActivity(intent);
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
}
