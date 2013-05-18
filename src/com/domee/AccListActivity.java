package com.domee;

import java.util.List;

import com.domee.manager.DMAccountsManager;
import com.domee.model.Account;

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

public class AccListActivity extends ListActivity {

	private List<Account> accounts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		accounts =  DMAccountsManager.getAccountList();
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
		intent.setClass(this, DMFriendsTimelineActivity.class);
		startActivity(intent);
		super.onListItemClick(l, v, position, id);
	}
}
