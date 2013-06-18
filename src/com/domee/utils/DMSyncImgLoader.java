package com.domee.utils;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by duyuan on 13-6-8.
 */
public class DMSyncImgLoader {

    private Object lock = new Object();
    private boolean mAllowLoad = true;
    private boolean firstLoad = true;
    private int mStartLoadLimit = 0;
    private int mStopLoadLimit = 0;
    private ImageLoader imageLoader ;
    final Handler handler = new Handler();

    private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

    public DMSyncImgLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public interface OnImageLoadListener {
        public void onImageLoad(Integer t, Drawable drawable);
        public void onError(Integer t);
    }
    public void setLoadLimit(int startLoadLimit,int stopLoadLimit){
        if(startLoadLimit > stopLoadLimit){
            return;
        }
        mStartLoadLimit = startLoadLimit;
        mStopLoadLimit = stopLoadLimit;
    }

    public void restore(){
        mAllowLoad = true;
        firstLoad = true;
    }

    public void lock(){
        mAllowLoad = false;
        firstLoad = false;
    }

    public void unlock(){
        mAllowLoad = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void loadImage(int t, String imageUrl, ImageView imageView) {
//        final OnImageLoadListener mListener = listener;
        final String mImageUrl = imageUrl;
        final ImageView mImageView = imageView;
        final Integer mt = t;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!mAllowLoad){
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                if(mAllowLoad && firstLoad){
                    imageLoader.displayImage(mImageUrl, mImageView);
                }
                if(mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit){
//                    loadImage(mImageUrl, mt, mListener);
                    imageLoader.displayImage(mImageUrl, mImageView);
                }
            }
        }).start();
    }



}
