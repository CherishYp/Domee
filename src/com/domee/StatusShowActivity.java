package com.domee;

import java.io.IOException;
import java.util.LinkedList;

import com.domee.model.Comment;
import com.domee.model.CommentResult;
import com.domee.model.Status;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.AUTHOR_FILTER;
import com.weibo.sdk.android.net.RequestListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StatusShowActivity extends BaseActivity {

	private Status status = null;
	private ImageView ssAvatar;
	private TextView ssScreenName; 
	private TextView ssContent;
	private ImageView ssImgView;
	private TextView ssSource;
	private TextView ssRepost;
	private TextView ssComment;
	
	private View ssReStatus;
	private TextView ssReContent;
	private ImageView ssReImgView;
	private TextView ssReSource;
	private TextView ssReRepost;
	private TextView ssReComment;
	
	private ListView ssComListView;
	private LinkedList<Comment> comList;
	private ViewHolder holder;
	private CommentAdapter adapter;
	public final class ViewHolder {
		private ImageView ssComAvatar;
		private TextView ssComScreenName;
		private TextView ssComCreateAt;
		private TextView ssComText;
		private TextView ssComSource;
		private TextView ssComRepost;
		private TextView ssComComment;
	}
	
	private long since_id = 0;
	private long max_id = 0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
		};
	};
	private LinearLayout popLayout = null;
	private Button button = null;
	private long total_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_statusshow);
		
		Intent intent = getIntent();
		status = (Status) intent.getSerializableExtra("status");
		//给listView新加一个view显示更多
		popLayout = new LinearLayout(StatusShowActivity.this);
		button = new Button(StatusShowActivity.this);
		button.setText("加载更多");
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadMore();
			}
		});
		popLayout.addView(button);

		View headerView = LayoutInflater.from(this).inflate(R.layout.header_statusshow, null);
		ssAvatar = (ImageView) headerView.findViewById(R.id.ssAvatar);
		ssScreenName = (TextView) headerView.findViewById(R.id.ssScreenName);
		ssContent = (TextView) headerView.findViewById(R.id.ssContent);
		ssImgView =(ImageView) headerView.findViewById(R.id.ssImgView);
		ssSource = (TextView) headerView.findViewById(R.id.ssSource);
		ssRepost = (TextView) headerView.findViewById(R.id.ssRepost);
		ssComment = (TextView) headerView.findViewById(R.id.ssComment);
		
		imageLoader.displayImage(status.getUser().getProfile_image_url(), ssAvatar, options, animateFirstListener);
		
		//判断是否带图片微博
		if (status.getThumbnail_pic() != null && !status.getThumbnail_pic().equals("")) {
			imageLoader.displayImage(status.getThumbnail_pic(), ssImgView, options, animateFirstListener);
		} else {
			ssImgView.setVisibility(View.GONE);
		}
		
		ssReStatus = (View) headerView.findViewById(R.id.ssReStatus);
		ssReContent = (TextView) headerView.findViewById(R.id.ssReContent);
		ssReImgView = (ImageView) headerView.findViewById(R.id.ssReImgView);
		ssReSource = (TextView) headerView.findViewById(R.id.ssReSource);
		ssReRepost = (TextView) headerView.findViewById(R.id.ssReRepost);
		ssReComment = (TextView) headerView.findViewById(R.id.ssReComment);
		
		ssScreenName.setText(status.getUser().getScreen_name());
		ssContent.setText(status.getText());
		ssSource.setText(Html.fromHtml(status.getSource()));
		ssRepost.setText("转发" + status.getReposts_count() + "");
		ssComment.setText("评论" + status.getComments_count() + "");
		
		//判断是否转发
		if (status.getRetweeted_status() != null) {
			if (status.getRetweeted_status().getUser() != null) {
				
				ssReContent.setText(status.getRetweeted_status().getUser().getScreen_name() + ":" + status.getRetweeted_status().getText());
				ssReSource.setText("10：25");
				ssReRepost.setText("转发" + status.getRetweeted_status().getReposts_count() + "");
				ssReComment.setText("评论" + status.getRetweeted_status().getComments_count() + "");
				//判断转发是否带图片
				if (status.getRetweeted_status().getThumbnail_pic() != null && !status.getRetweeted_status().getThumbnail_pic().equals("")) {
					imageLoader.displayImage(status.getRetweeted_status().getThumbnail_pic(), ssReImgView, options, animateFirstListener);
				} else {
					ssImgView.setVisibility(View.GONE);
				}
			} else {
				ssReContent.setText(status.getRetweeted_status().getText());
			}
		} else {
			ssReStatus.setVisibility(View.GONE);
		}
		
		ssComListView = (ListView) findViewById(R.id.ssComListView);
		ssComListView.addHeaderView(headerView);
		adapter = new CommentAdapter(this);
		ssComListView.setAdapter(adapter);
		//加载评论
		this.loadMore();
	}
	
	//adapter
	class CommentAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		
		public CommentAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (comList == null) {
				return 0;
			}
			return comList.size() + 1;
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

			if (convertView == null || convertView == popLayout) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_statusshow, null);
				holder.ssComAvatar = (ImageView) convertView.findViewById(R.id.ssComAvatar);
				holder.ssComScreenName = (TextView) convertView.findViewById(R.id.ssComScreenName);
				holder.ssComCreateAt = (TextView) convertView.findViewById(R.id.ssComCreateAt);
				holder.ssComText = (TextView) convertView.findViewById(R.id.ssComText);
				holder.ssComSource = (TextView) convertView.findViewById(R.id.ssComSource);
				holder.ssComRepost = (TextView) convertView.findViewById(R.id.ssComRepost);
				holder.ssComComment = (TextView) convertView.findViewById(R.id.ssComComment);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (comList.size() == position) {
				if (total_number <= 20) {
					button.setVisibility(View.GONE);
				}
				return popLayout;
			}
			Comment comment = comList.get(position);
			imageLoader.displayImage(comment.getUser().getProfile_image_url(), holder.ssComAvatar, options, animateFirstListener);
			holder.ssComScreenName.setText(comment.getUser().getScreen_name());
			holder.ssComCreateAt.setText("12:20");
			holder.ssComText.setText(comment.getText());
			holder.ssComSource.setText(Html.fromHtml(comment.getSource()));
			holder.ssComRepost.setText("转发");
			holder.ssComComment.setText("评论");
			return convertView;
		}
	}
	
	//加载更多评论
	public void loadMore() {
		CommentRequestListener listener = new CommentRequestListener();
		if (comList != null && comList.size() != 0) {
			max_id = comList.get(comList.size() - 1).getId();
			commentsAPI.show(status.getId(), max_id +1, 0 , 20, 1, AUTHOR_FILTER.ALL, listener);
		} 
		else {
			
			commentsAPI.show(status.getId(), 0, 0, 20, 1, AUTHOR_FILTER.ALL, listener);
		}
	}
	
	public class CommentRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			CommentResult cr = gson.fromJson(arg0, new TypeToken<CommentResult>(){}.getType());
			
			if (comList != null) {
				comList.addAll(cr.getComments());
			} else {
				comList = cr.getComments();
			}
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
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
