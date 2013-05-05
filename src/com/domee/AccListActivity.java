package com.domee;

import java.io.IOException;
import java.util.List;

import com.domee.manager.AccountsManager;
import com.domee.model.Account;
import com.domee.model.User;
import com.domee.net.UserRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.TrendsAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AccListActivity extends ListActivity {

	private List<Account> accounts;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		accounts =  AccountsManager.getAccountList();
		MyAdapter adapter = new MyAdapter(this);
	    setListAdapter(adapter);
	}
	
    public final class ViewHolder{
    	
        public TextView itemUserName;
        public ImageButton imgBtn;
    }
     
    public class MyAdapter extends BaseAdapter{
 
        private LayoutInflater mInflater;
         
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return accounts.size();
        }
 
        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }
 
        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             
            ViewHolder holder = null;
            if (convertView == null) {
                 
                holder = new ViewHolder();  
                convertView = mInflater.inflate(R.layout.item_acc, null);
                
                holder.itemUserName = (TextView)convertView.findViewById(R.id.itemUserName);
                holder.imgBtn = (ImageButton) convertView.findViewById(R.id.imgBtn);
                convertView.setTag(holder);
                 
            }else {
                 
                holder = (ViewHolder)convertView.getTag();
            }
             
            Account account = accounts.get(position);
            holder.itemUserName.setText(account.getUser().getScreen_name());
            holder.imgBtn.setImageResource(R.drawable.ic_launcher);
            holder.imgBtn.setFocusable(false);
            holder.imgBtn.setFocusableInTouchMode(false);
            return convertView;
        }
        
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
//		Toast.makeText(AccListActivity.this, "You click: " + position, Toast.LENGTH_SHORT).show(); 
		Intent intent = new Intent();
		Account account = accounts.get(position);
		Oauth2AccessToken accessToken = new Oauth2AccessToken(account.getmAccessToken(), account.getmExpiresTime());
		UsersAPI usersAPI = new UsersAPI(accessToken);
		
		UserRequestListener url = new UserRequestListener();
		usersAPI.show(account.getUser().getScreen_name(), url);
		
		super.onListItemClick(l, v, position, id);
	}
    
	class UserRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			User user = gson.fromJson(arg0, User.class);
			
		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
    
}
