package com.domee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.domee.manager.AccountsManager;
import com.domee.model.Status;
import com.domee.model.StatusResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsTimelineActivity extends Activity implements OnScrollListener {

	ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options;
	
	public ArrayList<Status> statusList = null;
	public TimelineAdapter adapter = null; 
	public ListView listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_timeline);
		listView = (ListView)findViewById(R.id.listView1);
		
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
								.cacheInMemory()
								.cacheOnDisc()
								.displayer(new RoundedBitmapDisplayer(0))
								.build();

		
		adapter = new TimelineAdapter(this); 
		listView.setAdapter(adapter);
		
		this.loadNew();
	}
	
	public final class ViewHolder {

		public ImageView avatar;
		public TextView screenName;
		public TextView createdAt;
		public TextView content;
		public ImageView cImgIV;
		public TextView source;
		public TextView repost;
		public TextView comment;
	}

	
	public class TimelineAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		private LayoutInflater mInflater;

		public TimelineAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
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
				convertView = mInflater.inflate(R.layout.item_timeline, null);

				holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
				holder.screenName = (TextView) convertView.findViewById(R.id.screenName);
				holder.createdAt = (TextView) convertView.findViewById(R.id.createdAt);
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.cImgIV = (ImageView) convertView.findViewById(R.id.cImgIV);
				holder.source = (TextView) convertView.findViewById(R.id.source);
				holder.repost = (TextView) convertView.findViewById(R.id.repost);
				holder.comment = (TextView) convertView.findViewById(R.id.comment);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			Status sta = statusList.get(position);
	        
//			holder.avatar.setImageResource(R.drawable.ic_launcher);
//			holder.avatar.setFocusable(false);
//			holder.avatar.setFocusableInTouchMode(false);
			imageLoader.displayImage(sta.getUser().getProfile_image_url(), holder.avatar, options, animateFirstListener);
			
			holder.screenName.setText(sta.getUser().getScreen_name());
			holder.createdAt.setText("2013.5.6");
			holder.content.setText(sta.getText());
			imageLoader.displayImage(sta.getThumbnail_pic(), holder.cImgIV, options, animateFirstListener);
			
			holder.source.setText(Html.fromHtml(sta.getSource()));
			stripUnderlines(holder.source, sta.getSource());
			
			holder.repost.setText(sta.getReposts_count() + "");
			holder.comment.setText(sta.getComments_count() + "");
			return convertView;
		}
	}
	
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
		}
	};
	
	/*
	 * 加载新的动态
	 */
	public void loadNew() {
		StatusesAPI statusesAPI = new StatusesAPI(AccountsManager.curAccessToken());
		statusesAPI.friendsTimeline(0, 0, 50, 1, false, FEATURE.ALL, false,
				new FriendsTimelineRequestListener());
	}

	public void loadMore() {

	}

	/*
	 * RequestListener,请求返回的json数据在里面的onComplete方法获得
	 */
	class FriendsTimelineRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			StatusResult sr = gson.fromJson(arg0, new TypeToken<StatusResult>(){}.getType());
			System.out.println(sr.getStatuses().size());
			statusList = sr.getStatuses();
			
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
	
	/*
	 * 加载图片部分代码
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	//去除a标签的下划线
	private void stripUnderlines(TextView textView, String source) {
//      Spannable s = (Spannable)textView.getText();
		Spannable s = new SpannableString(Html.fromHtml(source));
      	URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class); 
      	for (URLSpan span: spans) { 
          	int start = s.getSpanStart(span); 
          	int end = s.getSpanEnd(span); 
          	s.removeSpan(span); 
          	span = new URLSpanNoUnderline(span.getURL()); 
          	s.setSpan(span, start, end, 0); 
      	} 
      	textView.setText(s); 
	} 

	private class URLSpanNoUnderline extends URLSpan { 
		public URLSpanNoUnderline(String url) { 
			super(url); 
		} 
		@Override public void updateDrawState(TextPaint ds) { 
			super.updateDrawState(ds); 
			ds.setUnderlineText(false); 
		} 
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
