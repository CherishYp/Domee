package com.domee.activity;

import android.app.Activity;

/**
 * Created by duyuan on 13-5-24.
 */
public interface DMSelectedListener {
    public void selected(Activity activity, Object obj);
    public void cancelSelected(Activity activity);
}
