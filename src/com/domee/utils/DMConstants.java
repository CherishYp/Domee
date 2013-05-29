package com.domee.utils;

import java.util.regex.Pattern;

public class DMConstants{

	//******新浪微博*******//
	public static final String MENTIONS_SCHEMA ="domee://profile";
	public static final String TRENDS_SCHEMA ="domee://trend";
    public static final String LINKED_SCHEMA = "content://";
	public static final String PARAM_SCREEN_NAME ="screenname";
    public static final String PARAM_TREND ="trendname";

    //*******发送微博广播********//
	public static final String SEND_STATUS_ACTION = "send_status";
    public static final String REFRESH_ACTION = "refresh";

    //*******发送微博和转发微博标记********//
    public static final int FLAG_REPOST = 0;
    public static final int FLAG_COMMENT = 1;
}
