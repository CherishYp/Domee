package com.domee.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domee.activity.DMBigImgShowActivity;
import com.domee.activity.DMUserTimelineActivity;
import com.domee.R;
import com.domee.dialog.DMAlertImageDialog;
import com.domee.model.DMStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class DMNAStatusAdapter extends BaseAdapter {

	private DMUserTimelineActivity activity;
	private LayoutInflater mInflater;
	private ImageLoader imageLoader ;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private LinkedList<DMStatus> staList;
	
	//给listView加的一个view
	private LinearLayout utLinearLayout;
	
	public final class ViewHolder {
		private View utStatus;
		private TextView utCreatedAt;
		private TextView utText;
		private ImageView utImgView;
		private View utReStatus;
		private TextView utReText;
		private ImageView utReImgView;
		private TextView utReComment;
		private TextView utReRepost;
		private TextView utSource;
		private TextView utComment;
		private TextView utRepost;
	}
	
	public DMNAStatusAdapter(Context activity, 
			ImageLoader imageLoader, DisplayImageOptions options, ImageLoadingListener listener) {
		this.activity = (DMUserTimelineActivity) activity;
		this.mInflater = LayoutInflater.from(activity);
		this.imageLoader = imageLoader;
		this.options = options;
		this.animateFirstListener = listener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (staList == null) {
			return 0;
		}
		return staList.size();
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
		if (convertView == null || convertView == utLinearLayout) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_user_timeline, null);
			holder.utStatus = (View) convertView.findViewById(R.id.utStatus);
			holder.utCreatedAt = (TextView) convertView.findViewById(R.id.utCreatedAt);
			holder.utText = (TextView) convertView.findViewById(R.id.utText);
			holder.utImgView = (ImageView) convertView.findViewById(R.id.utImgView);
			holder.utReStatus = (View) convertView.findViewById(R.id.utReStatus);
			holder.utReText = (TextView) convertView.findViewById(R.id.utReText);
			holder.utReImgView = (ImageView) convertView.findViewById(R.id.utReImgView);
			holder.utReComment = (TextView) convertView.findViewById(R.id.utReComment);
			holder.utReRepost = (TextView) convertView.findViewById(R.id.utReRepost);
			holder.utSource = (TextView) convertView.findViewById(R.id.utSource);
			holder.utComment = (TextView) convertView.findViewById(R.id.utComment);
			holder.utRepost = (TextView) convertView.findViewById(R.id.utRepost);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
//		if (staList.size() == position) {
//			if (this.activity.total_number <= 20) {
//				this.activity.utButton.setVisibility(View.GONE);
//			}
//			return utLinearLayout;
//		}
		
		DMStatus status = staList.get(position);
	 	holder.utCreatedAt.setText("12:20");
		holder.utText.setText(status.getText());
        status.extract2Link(holder.utText);
		//判断微博是否有图片
		if (status.getThumbnail_pic() != null && !status.getThumbnail_pic().equals("")) {
			imageLoader.displayImage(status.getThumbnail_pic(), holder.utImgView, options, animateFirstListener);
			holder.utImgView.setVisibility(View.VISIBLE);
            holder.utImgView.setOnClickListener(new BtnListener(position));
		} else {
			holder.utImgView.setVisibility(View.GONE);
		}
		//判断微博是否是转发的
		if (status.getRetweeted_status() != null) {
			if (status.getRetweeted_status().getUser() != null) {
				holder.utReText.setText("@" + status.getRetweeted_status().getUser().getScreen_name() + ":" + status.getRetweeted_status().getText());
                status.extract2Link(holder.utReText);
			}else {
				holder.utReText.setText(status.getRetweeted_status().getText());
                status.extract2Link(holder.utReText);
			}
			//判断转发的微博是否带图片
			if (status.getRetweeted_status().getThumbnail_pic() != null && !status.getRetweeted_status().getThumbnail_pic().equals("")) {
				imageLoader.displayImage(status.getRetweeted_status().getThumbnail_pic(), holder.utReImgView, options, animateFirstListener);
				holder.utReImgView.setVisibility(View.VISIBLE);
                holder.utReImgView.setOnClickListener(new BtnListener(position));
			}else {
				holder.utReImgView.setVisibility(View.GONE);
			}
			holder.utReComment.setText("评论" + status.getRetweeted_status().getComments_count() + "");
			holder.utReRepost.setText("转发" + status.getRetweeted_status().getReposts_count() + "");
		
			holder.utReStatus.setVisibility(View.VISIBLE);
		} else {
			holder.utReStatus.setVisibility(View.GONE);
		}
		holder.utSource.setText(Html.fromHtml(status.getSource()));
		holder.utComment.setText("评论" + status.getComments_count() + "");
		holder.utRepost.setText("转发" + status.getReposts_count() + "");
		
		return convertView;
	}

	public LinkedList<DMStatus> getStaList() {
		return staList;
	}

	public void setStaList(LinkedList<DMStatus> staList) {
		this.staList = staList;
	}

    public class BtnListener implements View.OnClickListener {
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
                    DMUserTimelineActivity.show(activity, staList.get(position).getUser());
                    break;
                case R.id.utImgView:
                    DMBigImgShowActivity.show(activity, staList.get(position).getOriginal_pic(), staList.get(position).getThumbnail_pic());
                    break;
                case R.id.utReImgView:
                    DMBigImgShowActivity.show(activity, staList.get(position).getRetweeted_status().getOriginal_pic(), staList.get(position).getRetweeted_status().getThumbnail_pic());
                    break;
                default:
                    break;
            }

        }
    }
}
