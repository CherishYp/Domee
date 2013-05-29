package com.domee.adapter;

import java.util.LinkedList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.domee.activity.DMCommentActivity;
import com.domee.activity.DMUserTimelineActivity;
import com.domee.R;
import com.domee.model.DMComment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class DMCommentAdapter extends BaseAdapter {

	private DMCommentActivity activity;
	private LayoutInflater mInflater;
	private ImageLoader imageLoader ;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private LinkedList<DMComment> comList = null;
	
	public DMCommentAdapter(DMCommentActivity activity, 
			ImageLoader imageLoader, DisplayImageOptions options, ImageLoadingListener listener) {
		this.activity = activity;
		this.mInflater = LayoutInflater.from(activity);
		this.imageLoader = imageLoader;
		this.options = options;
		this.animateFirstListener = listener;
	}
	
	public final class ComHolder {
		private ImageView cAvatar;
		private TextView cScreenName;
		private TextView cCreatedAt;
		private TextView cText;
		private TextView cReText;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (comList != null) {
			return comList.size();
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
		ComHolder holder = null;
		if (convertView == null) {
			holder = new ComHolder();
			convertView = mInflater.inflate(R.layout.item_comment, null);
			holder.cAvatar = (ImageView)convertView.findViewById(R.id.cAvatar);
			holder.cScreenName = (TextView)convertView.findViewById(R.id.cScreenName);
			holder.cCreatedAt = (TextView)convertView.findViewById(R.id.cCreatedAt);
			holder.cText = (TextView)convertView.findViewById(R.id.cText);
			holder.cReText = (TextView)convertView.findViewById(R.id.cReText);
			convertView.setTag(holder);
		} else {
			holder = (ComHolder) convertView.getTag();
		}
		DMComment com = comList.get(position);
		imageLoader.displayImage(com.getUser().getProfile_image_url(), holder.cAvatar, options, animateFirstListener);
		holder.cAvatar.setOnClickListener(new AvatarListener(position));
		holder.cScreenName.setText(com.getUser().getScreen_name());
		holder.cCreatedAt.setText("09:20");
		holder.cText.setText(com.getText());
        com.extract2Link(holder.cText);
        if (com.getStatus().getUser() != null) {
            holder.cReText.setText(" @" + com.getStatus().getUser().getScreen_name() + " :" + com.getStatus().getText());
            com.getStatus().extract2Link(holder.cReText);
        } else {
            holder.cReText.setText(com.getStatus().getText());
            com.getStatus().extract2Link(holder.cReText);
        }

		return convertView;
	}
	
	public class AvatarListener implements OnClickListener {

		private int position;
		public AvatarListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(activity, DMUserTimelineActivity.class);
			intent.putExtra("user", comList.get(position).getUser());
			activity.startActivity(intent);
		}
	}

	public LinkedList<DMComment> getComList() {
		return comList;
	}

	public void setComList(LinkedList<DMComment> comList) {
		this.comList = comList;
	}
	
	
}
