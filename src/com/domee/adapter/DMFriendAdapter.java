package com.domee.adapter;

import java.util.LinkedList;

import com.domee.activity.DMAtFriendActivity;
import com.domee.R;
import com.domee.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DMFriendAdapter extends BaseAdapter {

	private DMAtFriendActivity activity;
	private LayoutInflater mInflater;
	private ImageLoader imageLoader ;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private LinkedList<User> userList = null;
	
	public DMFriendAdapter(DMAtFriendActivity activity, 
			ImageLoader imageLoader, DisplayImageOptions options, ImageLoadingListener listener) {
		this.activity = activity;
		this.mInflater = LayoutInflater.from(activity);
		this.imageLoader = imageLoader;
		this.options = options;
		this.animateFirstListener = listener;
	}
	
	public final class ViewHolder {
		private ImageView afAvatar;
		private TextView afScreenName;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (userList != null) {
			return userList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_friend, null);
			holder.afAvatar = (ImageView) convertView.findViewById(R.id.af_avatar);
			holder.afScreenName = (TextView) convertView.findViewById(R.id.af_screen_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		User user = userList.get(position);
		imageLoader.displayImage(user.getProfile_image_url(), holder.afAvatar, options, animateFirstListener);
		holder.afScreenName.setText(user.getScreen_name());
		return convertView;
	}

	public LinkedList<User> getUserList() {
		return userList;
	}

	public void setUserList(LinkedList<User> userList) {
		this.userList = userList;
	}

}
