package com.domee.parser.onclick;

/**
 * Created by duyuan on 13-6-17.
 */

import android.text.style.ClickableSpan;
import android.view.View;

/**
 *WeiboClickSpan类，WeiboAtClickSpan 等类继承自此类，在onClick(View widget, String content)方法中做调用操作
 */
public abstract class WeiboClickSpan extends ClickableSpan {
    // 点击的内容
    String mContent;

    public WeiboClickSpan(String content){
        mContent = content;
    }
    @Override
    public void onClick(View widget) {
        onClick(widget, mContent);
    }

    abstract void onClick(View widget, String content);
}
