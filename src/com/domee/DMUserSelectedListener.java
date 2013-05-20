package com.domee;

import android.app.Activity;
import com.domee.model.User;

/**
 * Created by duyuan on 13-5-19.
 */
public interface DMUserSelectedListener {
    public void userSelected(Activity activity, User user);
    public void userCancelSelected(Activity activity);
}
