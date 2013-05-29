package com.domee.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.ImageButton;
import com.domee.R;
import com.domee.manager.DMAccountsManager;
import com.domee.utils.DMConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.api.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends Activity {

	protected CommentsAPI commentsAPI = new CommentsAPI(DMAccountsManager.curAccessToken());
	protected StatusesAPI statusesAPI = new StatusesAPI(DMAccountsManager.curAccessToken());
	protected TrendsAPI trendsAPI = new TrendsAPI(DMAccountsManager.curAccessToken());
	protected FriendshipsAPI friendshipsAPI = new FriendshipsAPI(DMAccountsManager.curAccessToken());
    protected UsersAPI usersAPI = new UsersAPI(DMAccountsManager.curAccessToken());
    protected PlaceAPI placeAPI = new PlaceAPI(DMAccountsManager.curAccessToken());
    protected FavoritesAPI favoritesAPI = new FavoritesAPI(DMAccountsManager.curAccessToken());

	protected GsonBuilder builder;
	protected Gson gson;

	//加载图片
	ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	protected ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
								.cacheInMemory()
								.cacheOnDisc()
								.displayer(new RoundedBitmapDisplayer(0))
								.build();
		builder = new GsonBuilder();
		gson = builder.create();

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
//
//	public static void extractMention2Link(TextView v) {
//		v.setAutoLinkMask(0);
//		Pattern mentionsPattern = Pattern.compile("@(\\w+?)(?=\\W|$)(.)");
//		String mentionsScheme = String.format("%s/?%s=", DMConstants.MENTIONS_SCHEMA, DMConstants.PARAM_UID);
//		Linkify.addLinks(v, mentionsPattern, mentionsScheme, new Linkify.MatchFilter() {
//
//			@Override
//			public boolean acceptMatch(CharSequence s, int start, int end) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		}, new Linkify.TransformFilter() {
//			@Override
//			public String transformUrl(Matcher match, String url) {
//				Log.d("BaseActivity", match.group(1));
//				return match.group(1);
//			}
//		});
//
//		Pattern trendsPattern = Pattern.compile("#(\\w+?)#");
//		String trendsScheme = String.format("%s/?%s=", DMConstants.TRENDS_SCHEMA, DMConstants.PARAM_UID);
//		Linkify.addLinks(v, trendsPattern, trendsScheme, null, new Linkify.TransformFilter() {
//			@Override
//			public String transformUrl(Matcher match, String url) {
//				Log.d("", match.group(1));
//				return match.group(1);
//			}
//		});
//
//	}

    public class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.tbBack:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
