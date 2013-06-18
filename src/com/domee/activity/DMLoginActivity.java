package com.domee.activity;

import java.text.SimpleDateFormat;

import com.domee.R;
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
    private static final String CONSUMER_KEY = "1082819259"; // 替换为开发者的appkey，例如"1646212860";
    private static final String REDIRECT_URL = "http://oauth.weico.cc";
	//private static final String CONSUMER_KEY = "1152034500"; // 替换为开发者的appkey，例如"1646212860";
	//private static final String REDIRECT_URL = "http://www.sina.com";
    private static String OAUTH_URL = "https://api.weibo.com/oauth2/authorize?&response_type=code&";
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
        lOauth.setOnClickListener(new SsoBtnListener());
	}
	
	/*
	 * sso认证 监听器
	 */
	class SsoBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
                case R.id.lSso:
                    ssoHandler = new SsoHandler(DMLoginActivity.this, weibo);
                    ssoHandler.authorize(new AuthDialogListener());
                    break;
                case R.id.lOauth:
                    weibo.authorize(DMLoginActivity.this, new AuthDialogListener());
                    break;
                default:
                    break;
            }

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
                DMLoginActivity.this.finish();
            }
		}

        @Override
        public void onError(WeiboDialogError e) {
            Log.d("Weibo-authorize", "Login failed: " + e);
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Log.d("Weibo-authorize", "Login canceled");
            Toast.makeText(getApplicationContext(), "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.d("Weibo-authorize", "Login failed: " + e);
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
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
