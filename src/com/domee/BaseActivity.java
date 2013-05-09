package com.domee;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.domee.manager.AccountsManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.api.CommentsAPI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class BaseActivity extends Activity {

	protected CommentsAPI commentsAPI = new CommentsAPI(AccountsManager.curAccessToken());
	
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
}
