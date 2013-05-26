package com.domee.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.domee.R;
import com.domee.utils.DMHttpDownloader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;

/**
 * Created by duyuan on 13-5-20.
 */
public class DMBigImgShowActivity extends BaseActivity {

    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/Domee/";
    private ImageView mBigImgView;
    private ImageView mSaveImgView;
    private String originalPath;
    private ProgressDialog mProgressDialog = null;
    private String returnMsg;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private int mode = NONE;
    private float oldDist;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();

    public static void show(Activity activity, String originalPath) {
        Intent intent = new Intent(activity, DMBigImgShowActivity.class);
        intent.putExtra("originalPath", originalPath);
        activity.startActivityForResult(intent, 1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_big_image);

        mBigImgView = (ImageView)findViewById(R.id.big_image_view);
        mSaveImgView = (ImageView)findViewById(R.id.save_img_view);
        mBigImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBigImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent mEvent) {
                mBigImgView = (ImageView) view;
                switch (mEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix); //把原始  Matrix对象保存起来
                        start.set(mEvent.getX(), mEvent.getY());  //设置x,y坐标
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(mEvent);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, mEvent);  //求出手指两点的中点
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(mEvent.getX() - start.x, mEvent.getY() - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(mEvent);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }

                mBigImgView.setImageMatrix(matrix);
                return true;
            }
            //求两点距离
            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return FloatMath.sqrt(x * x + y * y);
            }

            //求两点间中点
            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }
        });

        mSaveImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog = ProgressDialog.show(DMBigImgShowActivity.this, "保存图片", "保存中...", true);
                new Thread(saveImgRunnable).start();
            }
        });
        originalPath = getIntent().getStringExtra("originalPath");
        imageLoader.displayImage(originalPath, mBigImgView, new DownloadFinishedListener());

        File file = imageLoader.getDiscCache().get(originalPath);
        System.out.println(file);
    }

    private Runnable saveImgRunnable = new Runnable() {
        @Override
        public void run() {
            int result = DMHttpDownloader.download(originalPath);
            if (result == 0) {
                returnMsg = "文件下载成功";
            } else if (result == 1){
                returnMsg = "文件已经存在";
            } else {
                returnMsg = "文件下载失败";
            }
            Message message = handler.obtainMessage();
            handler.sendMessage(message);
        }
    };
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            Toast.makeText(DMBigImgShowActivity.this, returnMsg, Toast.LENGTH_SHORT).show();
        }
    };

    public class DownloadFinishedListener extends SimpleImageLoadingListener {

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
//                mSaveImgView.setVisibility(View.VISIBLE);
            }
        }
    }



}
