package com.domee.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
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
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.*;
/**
 * Created by duyuan on 13-5-20.
 */
public class DMBigImgShowActivity extends BaseActivity {

    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory().toString() + "/Domee";
    private ImageView mBigImgView;
    private ImageView mSaveImgView;
    private String originalPath;
    private String mThumbnailPath;
    private ProgressDialog mProgressDialog = null;
    private String returnMsg;
    private File originalImg;
    private Bitmap originalBitmap;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private int mode = NONE;
    private float oldDist;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();

    public static void show(Activity activity, String originalPath, String thumbnailPath) {
        Intent intent = new Intent(activity, DMBigImgShowActivity.class);
        intent.putExtra("originalPath", originalPath);
        intent.putExtra("thumbnailPath", thumbnailPath);
        activity.startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_big_image);

        originalPath = getIntent().getStringExtra("originalPath");
        mThumbnailPath = getIntent().getStringExtra("thumbnailPath");

        mBigImgView = (ImageView)findViewById(R.id.big_image_view);
        mSaveImgView = (ImageView)findViewById(R.id.save_img_view);


        imageLoader.loadImage(mThumbnailPath, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);

                mBigImgView.setImageBitmap(loadedImage);

            }
        });

//        mBigImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        mBigImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent mEvent) {
                //System.out.println( "====ACTION_TOUCH===="+mEvent.getAction());
                switch (mEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix); //把原始  Matrix对象保存起来
                        start.set(mEvent.getX(), mEvent.getY());  //设置x,y坐标
                        mode = DRAG;
                        System.out.println(mEvent.getX() + "====ACTION_DOWN====" +  mEvent.getY());
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
                        //System.out.println(mEvent.getX() + "====ACTION_POINTER_DOWN====" +  mEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //System.out.println(mEvent.getX() + "====ACTION_MOVE====" +  mEvent.getY());
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(mEvent.getX() - start.x, mEvent.getY() - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(mEvent);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                             //   System.out.println("scale ===="+scale);
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }
                 //System.out.println(mBigImgView);
                mBigImgView.setImageMatrix(matrix);
                mBigImgView.invalidate();
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

        imageLoader.displayImage(originalPath, mBigImgView, new DownloadFinishedListener());

        originalImg = imageLoader.getDiscCache().get(originalPath);
        System.out.println(originalImg);
    }

    private Runnable saveImgRunnable = new Runnable() {
        @Override
        public void run() {
            String fileName = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.length());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(originalImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            saveImg(fileName, fis);
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
            originalBitmap = loadedImage;
            if (loadedImage != null) {
                mSaveImgView.setVisibility(View.VISIBLE);
                float x = mBigImgView.getWidth();
                float y = mBigImgView.getHeight();
                float width = mBigImgView.getWidth();
                float height = mBigImgView.getHeight();
                float imageWidth = loadedImage.getWidth();
                float imageHeight = loadedImage.getHeight();
                float scaleW = width / imageWidth;

                float realHeight = imageHeight * scaleW;
                float beginY = 0;
                if (realHeight <= height){
                    beginY = (height - realHeight)/2;
                }
                matrix.setScale(scaleW, scaleW);
                matrix.postTranslate(0, beginY );

                mBigImgView.setScaleType(ImageView.ScaleType.MATRIX);
            }
        }
    }

    public void saveImg(String fileName, FileInputStream fis){
        File dirFile = new File(ALBUM_PATH);
        if(!dirFile.exists()){
            boolean flag = dirFile.mkdir();
            System.out.println(flag);
        }

        File myCaptureFile = new File(ALBUM_PATH, fileName);
        try {
            System.out.println(myCaptureFile.getAbsolutePath());
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            returnMsg = "文件下载成功";
        } catch (IOException e) {
            returnMsg = "文件下载失败";
            e.printStackTrace();
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

}
