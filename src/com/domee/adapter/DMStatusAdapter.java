package com.domee.adapter;

import java.util.LinkedList;

import com.domee.BaseListActivity;
import com.domee.DMFriendsTimelineActivity;
import com.domee.R;
import com.domee.DMUserTimelineActivity;
import com.domee.dialog.DMAlertImageDialog;
import com.domee.model.Status;
import com.domee.utils.DMConstants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.R.anim;
import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.SyncStateContract.Constants;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InlinedApi")
public class DMStatusAdapter extends BaseAdapter {
	
	private static final String TAG ="MentionsActivity";
	private static final Uri PROFILE_URI = Uri.parse(DMConstants.MENTIONS_SCHEMA);
	
	private Activity activity;
	private LayoutInflater mInflater;
	private ImageLoader imageLoader ;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private LinkedList<Status> statusList = null;
	
	public DMStatusAdapter(Activity activity, 
			ImageLoader imageLoader, DisplayImageOptions options, ImageLoadingListener listener) {
		this.activity = activity;
		this.mInflater = LayoutInflater.from(activity);
		this.imageLoader = imageLoader;
		this.options = options;
		this.animateFirstListener = listener;
	}

	public final class ViewHolder {
		private ImageButton ftAvatar;
		private TextView ftScreenName;
		private TextView ftCreatedAt;
		private TextView ftText;
		private ImageView ftImgView;
		private TextView ftSource;
		private TextView ftRepost;
		private TextView ftComment;
		
		private View ftReStatus;
		private TextView ftReText;
		private ImageView ftReImgView;
		private TextView ftReRepost;
		private TextView ftReComment;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (statusList == null) {
			return 0;
		}
		return statusList.size();
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
			convertView = mInflater.inflate(R.layout.item_status, null);

			holder.ftAvatar = (ImageButton) convertView.findViewById(R.id.ftAvatar);
			holder.ftScreenName = (TextView) convertView.findViewById(R.id.ftScreenName);
	    	holder.ftCreatedAt = (TextView) convertView.findViewById(R.id.ftCreatedAt);
			holder.ftText = (TextView) convertView.findViewById(R.id.ftText);
			holder.ftImgView = (ImageView) convertView.findViewById(R.id.ftImgView);
		    holder.ftSource = (TextView) convertView.findViewById(R.id.ftSource);
			holder.ftRepost = (TextView) convertView.findViewById(R.id.ftRepost);
			holder.ftComment = (TextView) convertView.findViewById(R.id.ftComment);
			
			holder.ftReStatus = convertView.findViewById(R.id.ftReStatus);
			holder.ftReText = (TextView) convertView.findViewById(R.id.ftReText);
			holder.ftReImgView = (ImageView) convertView.findViewById(R.id.ftReImgView);
			holder.ftReRepost = (TextView) convertView.findViewById(R.id.ftReRepost);
			holder.ftReComment = (TextView) convertView.findViewById(R.id.ftReComment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Status sta = statusList.get(position);
		imageLoader.displayImage(sta.getUser().getProfile_image_url(), holder.ftAvatar, options, animateFirstListener);
		holder.ftAvatar.setOnClickListener(new BtnListener(position));
		//判断是否带图片微博
		if(sta.getThumbnail_pic() != null && !sta.getThumbnail_pic().equals("")) {
			imageLoader.displayImage(sta.getThumbnail_pic(), holder.ftImgView, options, animateFirstListener);
			holder.ftImgView.setVisibility(View.VISIBLE);
			holder.ftImgView.setOnClickListener(new BtnListener(position));
		} else {
			holder.ftImgView.setVisibility(View.GONE);
		}
		
		holder.ftScreenName.setText(sta.getUser().getScreen_name());
		holder.ftCreatedAt.setText("2013.5.6");
		sta.setHtmlStatusText(holder.ftText);
		holder.ftText.setMovementMethod(LinkMovementMethod.getInstance());
	      
		   // holder.ftText.setText(style);
		holder.ftSource.setText(Html.fromHtml(sta.getSource()));
//		((BaseListActivity) activity).stripUnderlines(holder.ftSource, sta.getSource());
		holder.ftRepost.setText("转发" + sta.getReposts_count() + "");
		holder.ftComment.setText("评论" + sta.getComments_count() + "");
		
		//判断是否转发
		if (sta.getRetweeted_status() != null) {
			//判断微博是否存在
			if(sta.getRetweeted_status().getUser() != null){
//				sta.getRetweeted_status().setHtmlStatusText(holder.ftReText);
                holder.ftReText.setText(" @" + sta.getRetweeted_status().getUser().getScreen_name() + " :" + sta.getRetweeted_status().getText());
				holder.ftReText.setMovementMethod(LinkMovementMethod.getInstance());
			}else{
				sta.getRetweeted_status().setHtmlStatusText(holder.ftReText);
				holder.ftReText.setMovementMethod(LinkMovementMethod.getInstance());
			}
			//判断转发的微博是否有图片
			if(sta.getRetweeted_status().getThumbnail_pic() != null && !sta.getRetweeted_status().getThumbnail_pic().equals("")) {
				imageLoader.displayImage(sta.getRetweeted_status().getThumbnail_pic(), holder.ftReImgView, options, animateFirstListener);
				holder.ftReImgView.setVisibility(View.VISIBLE);
				holder.ftReImgView.setOnClickListener(new BtnListener(position));
			} else {
				holder.ftReImgView.setVisibility(View.GONE);
			}
			holder.ftReRepost.setText("转发" + sta.getRetweeted_status().getReposts_count() + "");
			holder.ftReComment.setText("评论" + sta.getRetweeted_status().getComments_count() + "");
			holder.ftReStatus.setVisibility(View.VISIBLE);
		} else {
			holder.ftReStatus.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public class BtnListener implements OnClickListener {
		private int position;
		public BtnListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DMAlertImageDialog dialog = null;
			switch (v.getId()) {
			case R.id.ftAvatar:
				DMUserTimelineActivity.show(activity, statusList.get(position).getUser());
				break;
			case R.id.ftImgView:
				dialog = new DMAlertImageDialog(activity, statusList.get(position).getOriginal_pic());
				dialog.show();
				break;
			case R.id.ftReImgView:
				dialog = new DMAlertImageDialog(activity, statusList.get(position).getRetweeted_status().getOriginal_pic());
				dialog.show();
				break;
			default:
				break;
			}
			
		}
	}
	
	public interface IBtnListener {
		
	}

	public LinkedList<Status> getStatusList() {
		return statusList;
	}

	public void setStatusList(LinkedList<Status> statusList) {
		this.statusList = statusList;
	}
	

    
}
