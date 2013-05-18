package com.domee.dialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.domee.DMFriendsTimelineActivity;
import com.domee.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class DMAlertImageDialog {

	private Context context; 
	private String imagePath; 
	private Dialog dialog; 
	private Bitmap bitmap;
	
	public DMAlertImageDialog(Context context, String imagePath) {
		super();
		this.context = context;
		this.imagePath = imagePath;
	} 
	private ImageView imageView;
	private ProgressBar bar;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// 当数据回来时候影藏bar 显示原图[html]                 
			if(msg.what == 1 && bitmap != null){ 
				imageView.setImageBitmap(bitmap); 
	            bar.setVisibility(View.GONE); 
			} 
		} 
	}; 
	
	public void show() {
		create();
		dialog.show();
	}
	
	/**
	   * 
	   * <code>create</code>
	   * @description: TODO(创建一个bitmap 当本地有则去本地存储，否则去服务器上下载 
	   * @since   2011-12-19    yourname
	   */
	private void create(){
		System.out.println(imagePath);
		if (imagePath != null) {
//			try {
////				bitmap = ImageFactory.getFileBitmap(imagePath, 300, 300, context);
//			} catch (POAException e) {
//				e.printStackTrace();
//			}
			
			new Thread(downloadRun).start();
            
		}
		dialog = new Dialog(context);
		// 设置影藏dialog的标题栏，太难看了
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.dialog_image);
		initImageView();
	}
	  
	/** 
	   * 下载线程 
	   */  
	Runnable downloadRun = new Runnable(){  
		@Override  
		public void run() {  
		    // TODO Auto-generated method stub  
			try {
				URL url = new URL(imagePath);
				URLConnection conn = url.openConnection();     
				conn.connect(); 
				InputStream is = conn.getInputStream();     
				BitmapFactory.Options opts = new BitmapFactory.Options();
//				//首先取得屏幕对象  
//                Display display = ((Activity) context).getWindowManager().getDefaultDisplay();  
//                //获取屏幕的宽和高  
//                int dw = display.getWidth();  
//                int dh = display.getHeight();  
//				Rect rect = new Rect(0, 0, dw, dh);
//				bitmap = BitmapFactory.decodeStream(is, rect, opts);
				bitmap = BitmapFactory.decodeStream(is);
				handler.sendEmptyMessage(1);
				is.close();     
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}  
	};  
   /**
    * 
    * <code>initImageView</code>
    * @description: TODO(初始化弹出对话框显示的图片) 
    * @since   2011-12-19    yourname
    */
    private void initImageView(){
    	imageView = (ImageView) dialog.findViewById(R.id.image);
    	bar = (ProgressBar) dialog.findViewById(R.id.pb);
		
    	if(bitmap != null){
    		imageView.setImageBitmap(bitmap);
    	}else{
    		bar.setVisibility(View.VISIBLE);
    	}
    	imageView.setOnClickListener(new ImageView.OnClickListener() {
			
			public void onClick(View arg0) {
				if(dialog != null){
					dialog.dismiss();	
				}							
			}
		});
    }
}
