package com.domee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.domee.R;
import com.domee.adapter.DMUserDetailAdapter;
import com.domee.manager.DMAccountsManager;
import com.domee.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private DMUserDetailAdapter mAdapter;
    private ListView mListView;

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

        mAdapter = new DMUserDetailAdapter(this);

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

        imageLoader.displayImage(mUser.getProfile_image_url(), mAvatar, options, animateFirstListener);
        String gender = "";
        if (mUser.getGender().equals("m"))
            gender = "男";
        else if (mUser.getGender().equals("f"))
            gender = "女";
        else gender = "未知";
        mDetail.setText(mUser.getScreen_name() + "\n" + mUser.getLocation() + " " + gender);

        initList();
        mListView = (ListView) findViewById(R.id.ud_list_view);
        mListView.setAdapter(mAdapter);
    }

    public void initList() {
        List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("title", "昵称");
        map1.put("text", mUser.getScreen_name());
        tempList.add(map1);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("title", "认证");
        map2.put("text", mUser.getVerified_reason());
        tempList.add(map2);
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("title", "博客地址");
        map3.put("text", mUser.getUrl());
        tempList.add(map3);
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("title", "个人介绍");
        map4.put("text", mUser.getDescription());
        tempList.add(map4);
        mAdapter.setmDetailList(tempList);
    }

}
