package com.domee.parser;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duyuan on 13-6-17.
 * 解析微博内容的类
 *
 */
public class DMWeiboContentParser {
    SpannableString mSpannableString;
    private static DMWeiboContentParser mWeiboContentParser;
    public static DMWeiboContentParser getInstance(){
        if(mWeiboContentParser == null){
            mWeiboContentParser = new DMWeiboContentParser();
        }
        return mWeiboContentParser;
    }
    private final static String VOICE_TAG = "##";
    private final static String SINA_HTTP = "http://t\\.cn/\\w+";
    /**
     * 微博内容中的at正则表达式
     */
    private final Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
    /**
     * 微博内容中的#话题#正则表达式
     */
    private final Pattern TAG_PATTERN = Pattern.compile("#([^\\#|.]+)#");
    /**
     * 微博内容中的at正则表达式
     */
    private final Pattern VOICE_PATTERN = Pattern.compile(VOICE_TAG+SINA_HTTP);
    /**
     * 微博内容中的url正则表达式
     */
    private final Pattern URL_PATTERN = Pattern.compile(SINA_HTTP);
    public SpannableString parse(String input) {
        SpannableString mSpannableString = new SpannableString(input);
        Matcher mAtMatcher = AT_PATTERN.matcher(input);
        while(mAtMatcher.find()){
            String atNameMatch = mAtMatcher.group();
            String subAtNameMatch = atNameMatch.substring(1, atNameMatch.length());
            mSpannableString.setSpan(new WeiboAtClickSpan(subAtNameMatch), mAtMatcher.start(), mAtMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        Matcher mTopicMatcher = TAG_PATTERN.matcher(input);
        while(mTopicMatcher.find()){
            String tagNameMatch = mTopicMatcher.group();
            String subTagNameMatch = tagNameMatch.substring(1, tagNameMatch.length()-1);
            mSpannableString.setSpan(new WeiboTagClickSpan(subTagNameMatch), mTopicMatcher.start(), mTopicMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        Matcher mVoiceMatcher = VOICE_PATTERN.matcher(input);
        while(mVoiceMatcher.find()){
            String voiceNameMatch = mVoiceMatcher.group();
            String subVoiceNameMatch = voiceNameMatch.substring(VOICE_TAG.length(), voiceNameMatch.length());
            mSpannableString.setSpan(new BackgroundColorSpan(Color.RED), mVoiceMatcher.start()+VOICE_TAG.length(), mVoiceMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(new WeiboVoiceClickSpan(subVoiceNameMatch), mVoiceMatcher.start()+VOICE_TAG.length(), mVoiceMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            Matcher mUrlMatcher = URL_PATTERN.matcher(input);
            while(mUrlMatcher.find()){
                String urlNameMatch = mUrlMatcher.group();
                mSpannableString.setSpan(new WeiboUrlClickSpan(urlNameMatch), mUrlMatcher.start(), mUrlMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return mSpannableString;
        }
    }
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
}
