package com.domee.adapter;

import java.util.LinkedList;

import com.domee.*;
import com.domee.activity.DMBigImgShowActivity;
import com.domee.activity.DMFriendsTimelineActivity;
import com.domee.activity.DMUserTimelineActivity;
import com.domee.dialog.DMAlertImageDialog;
import com.domee.model.DMStatus;
import com.domee.utils.DMConstants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InlinedApi")
public class DMFStatusAdapter extends BaseAdapter {

	private static final String TAG ="MentionsActivity";
	private static final Uri PROFILE_URI = Uri.parse(DMConstants.MENTIONS_SCHEMA);

	private Activity mActivity;
	private LayoutInflater mInflater;
	private ImageLoader imageLoader ;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private LinkedList<DMStatus> statusList = null;

    public DMFStatusAdapter(Activity mActivity,
                            ImageLoader imageLoader, DisplayImageOptions options, ImageLoadingListener listener) {
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.imageLoader = imageLoader;
		this.options = options;
		this.animateFirstListener = listener;
	}

	public final static class ViewHolder {
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

        private ImageView mAdd;
        private View mAddLayout;

        private ImageView mComment;
        private ImageView mRepost;
        private ImageView mFav;
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
            holder.mAdd = (ImageView) convertView.findViewById(R.id.s_add);
            holder.mAddLayout = (View) convertView.findViewById(R.id.s_add_layout);
            holder.mComment = (ImageView) convertView.findViewById(R.id.s_comment);
            holder.mRepost = (ImageView) convertView.findViewById(R.id.s_repost);
            holder.mFav = (ImageView) convertView.findViewById(R.id.s_fav);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DMStatus sta = statusList.get(position);
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
		holder.ftCreatedAt.setText(sta.getCreated_at());
        holder.ftText.setText(sta.getText());
        sta.extract2Link(holder.ftText);

//        sta.change2Link(holder.ftText);

		   // holder.ftText.setText(style);
		holder.ftSource.setText(sta.getSource());
//		((BaseListActivity) activity).stripUnderlines(holder.ftSource, sta.getSource());
		holder.ftRepost.setText("转发" + sta.getReposts_count() + "");
		holder.ftComment.setText("评论" + sta.getComments_count() + "");
		
		//判断是否转发
		if (sta.getRetweeted_status() != null) {
			//判断微博是否存在
			if(sta.getRetweeted_status().getUser() != null){
                holder.ftReText.setText(" @" + sta.getRetweeted_status().getUser().getScreen_name() + " :" + sta.getRetweeted_status().getText());
                sta.extract2Link(holder.ftReText);
			}else{
                holder.ftReText.setText(sta.getRetweeted_status().getText());
                sta.extract2Link(holder.ftReText);
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
        final ViewHolder finalHolder = holder;
        holder.mAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finalHolder.mAddLayout.setVisibility(finalHolder.mAddLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        holder.mComment.setOnClickListener(new DMFriendsTimelineActivity.FBtnListener(position));
        holder.mRepost.setOnClickListener(new BtnListener(position, finalHolder));
        holder.mFav.setOnClickListener(new BtnListener(position, finalHolder));
		return convertView;
	}
	
	public class BtnListener implements OnClickListener {
		private int position;
        private ViewHolder mHolder;
		public BtnListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}
        public BtnListener(int position, ViewHolder holder) {
            // TODO Auto-generated constructor stub
            this.position = position;
            this.mHolder = holder;
        }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DMAlertImageDialog dialog = null;
			switch (v.getId()) {
                case R.id.ftAvatar:
                    DMUserTimelineActivity.show(mActivity, statusList.get(position).getUser());
                    break;
                case R.id.ftImgView:
                    DMBigImgShowActivity.show(mActivity, statusList.get(position).getOriginal_pic(), statusList.get(position).getThumbnail_pic());
                    break;
                case R.id.ftReImgView:
                    DMBigImgShowActivity.show(mActivity, statusList.get(position).getRetweeted_status().getOriginal_pic(), statusList.get(position).getRetweeted_status().getThumbnail_pic());
                    break;
                default:
                    break;
			}
			
		}
	}
	
	public LinkedList<DMStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(LinkedList<DMStatus> statusList) {
		this.statusList = statusList;
	}
	

    
}
