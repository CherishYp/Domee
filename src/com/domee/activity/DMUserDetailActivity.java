package com.domee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.domee.R;
import com.domee.manager.DMAccountsManager;
import com.domee.model.User;

/**
 * Created by duyuan on 13-5-21.
 */
public class DMUserDetailActivity extends BaseActivity {

    private ImageView mAvatar;
    private TextView mDetail;
    private TextView mScreenName;
    private TextView mVerified;
    private TextView mBlogUrl;
    private TextView mDescription;

    //title_user_timeline
    private ImageButton tbBack;
    public TextView tbTitle;

    private User mUser;

    public static void show(Activity activity, User user) {
        Intent intent = new Intent(activity, DMUserDetailActivity.class);
        intent.putExtra("user", user);
        activity.startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.CustomTheme);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.ac_user_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        mUser = (User) getIntent().getSerializableExtra("user");
        //title_bar控件处理
        tbBack = (ImageButton)findViewById(R.id.tbBack);
        tbTitle = (TextView)findViewById(R.id.tbTitle);
        tbBack.setOnClickListener(new BtnListener());
        if (mUser.getScreen_name().equals(DMAccountsManager.getCurUser().getScreen_name())) {
            tbTitle.setText("自己");
        } else {
            tbTitle.setText("个人资料");
        }


        mAvatar = (ImageView) findViewById(R.id.ud_avatar);
        mDetail = (TextView) findViewById(R.id.ud_detail);
        mScreenName = (TextView) findViewById(R.id.ud_screen_name);
        mVerified = (TextView) findViewById(R.id.ud_verified);
        mBlogUrl = (TextView) findViewById(R.id.ud_blog_url);
        mDescription = (TextView) findViewById(R.id.ud_description);

        imageLoader.displayImage(mUser.getProfile_image_url(), mAvatar, options, animateFirstListener);
        String gender = "";
        if (mUser.getGender().equals("m"))
            gender = "男";
        else if (mUser.getGender().equals("f"))
            gender = "女";
        else gender = "未知";
        mDetail.setText(mUser.getScreen_name() + "\n" + mUser.getLocation() + " " + gender);
        mScreenName.setText(mUser.getScreen_name());
        mVerified.setText(mUser.getVerified_reason());
        mBlogUrl.setText(mUser.getUrl());
        mDescription.setText(mUser.getDescription());
    }
}
