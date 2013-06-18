package com.domee.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import com.domee.R;
import com.domee.activity.DMUserTimelineActivity;
import com.domee.manager.DMAccountsManager;
import com.domee.manager.DMUIManager;

import android.content.Intent;
import android.text.style.*;
import android.view.View;
import android.widget.TextView;
import com.domee.parser.DMWeiboContentParser;
import com.domee.utils.DMConstants;
import com.domee.utils.DMDateUtils;

public class DMStatus implements Serializable {

	private static final long serialVersionUID = 1L;
    private static final String TAG = "DMStatus";

	private String created_at;				//	string  微博创建时间
	private long id;						//	int64	微博ID
	private long mid;						//	int64	微博MID
	private String idstr;					//	string	字符串型的微博ID
	private String text;					//	string	微博信息内容
	private String source;					//	string	微博来源
	private boolean favorited;				//	boolean	是否已收藏，true：是，false：否
	private boolean truncated;				//	boolean	是否被截断，true：是，false：否
	private String in_reply_to_status_id;	//	string	（暂未支持）回复ID
	private String in_reply_to_user_id;		//	string	（暂未支持）回复人UID
	private String in_reply_to_screen_name;	//	string	（暂未支持）回复人昵称
	private String thumbnail_pic;			//	string	缩略图片地址，没有时不返回此字段
	private String bmiddle_pic;				//	string	中等尺寸图片地址，没有时不返回此字段
	private String original_pic;			//	string	原始图片地址，没有时不返回此字段
	private Geo geo;						//	object	地理信息字段 详细
	private User user;						//	object	微博作者的用户信息字段 详细
	private DMStatus retweeted_status;		//	object	被转发的原微博信息字段，当该微博为转发微博时返回 详细
	private int reposts_count;				//	int	转发数
	private int comments_count;				//	int	评论数
	private int attitudes_count;			//	int	表态数
	private int mlevel;						//	int	暂未支持
	private Visible visible;				//	object	微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
    private String htmlText;
	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public String getCreated_at() {
        String tempStr = DMDateUtils.changeToDate(this.created_at);
        return tempStr;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
        int start = this.source.indexOf(">");
        int end = this.source.lastIndexOf("<");
        char startChar = '>';
        char endChar = '<';
        String result = "";
        for (int i = 0; i < this.source.length(); i++) {
            if (startChar == this.source.charAt(i)) {
                result = this.source.substring(i + 1);
                break;
            }
        }
        for (int i = 0; i < result.length(); i++) {
            if (endChar == result.charAt(i)) {
                result = result.substring(0, i);
            }
        }
		return result;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public String getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(String in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public String getIn_reply_to_user_id() {
		return in_reply_to_user_id;
	}

	public void setIn_reply_to_user_id(String in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DMStatus getRetweeted_status() {
		return retweeted_status;
	}

	public void setRetweeted_status(DMStatus retweeted_status) {
		this.retweeted_status = retweeted_status;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public int getAttitudes_count() {
		return attitudes_count;
	}

	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}

	public int getMlevel() {
		return mlevel;
	}

	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
	}

	public Visible getVisible() {
		return visible;
	}

	public void setVisible(Visible visible) {
		this.visible = visible;
	}


    public void extract2Link(TextView v) {
        v.setAutoLinkMask(0);

        Pattern mentionsPattern = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        String mentionsScheme = String.format("%s/?%s=", DMConstants.MENTIONS_SCHEMA, DMConstants.PARAM_SCREEN_NAME);
        Linkify.addLinks(v, mentionsPattern, mentionsScheme, new Linkify.MatchFilter() {
                    @Override
                    public boolean acceptMatch(CharSequence s, int start, int end) {
                        System.out.println(s);
                        SpannableString spanStr = new SpannableString(s);
                        spanStr.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        return s.charAt(end - 1) != '.';
                    }
                }, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        Log.d(TAG, match.group());
                        System.out.println(match.group());
                        return match.group().replaceAll("@", "");
                    }
                }
        );

        Pattern trendsPattern = Pattern.compile("#(\\w+?)#");
        String trendsScheme = String.format("%s/?%s=", DMConstants.TRENDS_SCHEMA, DMConstants.PARAM_TREND);
        Linkify.addLinks(v, trendsPattern, trendsScheme, new Linkify.MatchFilter() {
                    @Override
                    public boolean acceptMatch(CharSequence s, int start, int end) {
                        System.out.println(s);
                        return s.charAt(end - 1) != '.';
                    }
                }, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        Log.d(TAG, match.group());
                        return match.group().replaceAll("#","");
                    }
                }
        );

        Pattern linkedPattern = Pattern.compile("[hH][tT][tT][pP][sS]?:\\/\\/[a-zA-Z0-9\\-\\.\\?%&\\=\\/]*");
        String linkedScheme = String.format("%s", DMConstants.LINKED_SCHEMA);
        Linkify.addLinks(v, linkedPattern, null, null, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                Log.d(TAG, match.group());
                return match.group();
            }
        });
    }

    public void change2Link(TextView mTextView) {
        mTextView.setAutoLinkMask(0);
        String plaintText = this.text;
        Pattern mentionsPattern = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        Matcher matcher = mentionsPattern.matcher(plaintText);

        while (matcher.find()) {
            String metionsText = matcher.group();
            plaintText = plaintText.replaceAll(metionsText, "<a href='domee://profile/?screenname=" + metionsText.replaceAll("@", "") + "' style='color:red'>" + metionsText + "</a>");
        }

        Pattern trendsPattern = Pattern.compile("#(\\w+?)#");
        Matcher trendMatcher = trendsPattern.matcher(plaintText);
        while (trendMatcher.find()) {
            String trendText = trendMatcher.group();
            plaintText = plaintText.replaceAll(trendText, "<a href='domee://trend/?trendname=" + trendText.replaceAll("#", "") + "'>" + trendText + "</a>");
        }

        Pattern linkedPattern = Pattern.compile("[hH][tT][tT][pP][sS]?:\\/\\/[a-zA-Z0-9\\-\\.\\?%&\\=\\/]*");
        Matcher linkedMatcher = linkedPattern.matcher(plaintText);
        while (linkedMatcher.find()) {
            String linkedText = linkedMatcher.group();
            plaintText = plaintText.replaceAll(linkedText, "<a href='" + linkedText + "' style='color:black'>" + linkedText + "</a>");
        }

        mTextView.setText(Html.fromHtml(plaintText));
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id=======>" + id + "==========" +  "text=======>" + text + "/n";
	}


	private static class MyURLSpan extends ClickableSpan {
		   private String mUrl;
		   MyURLSpan(String url) {
		    mUrl = url;
		   }
		   
			@Override
			public void updateDrawState(TextPaint ds) {
			    ds.setColor(ds.linkColor);
			    ds.setUnderlineText(false); //去掉下划线
			}

		 
		   @Override
		   public void onClick(View widget) {
		    System.out.println(mUrl);
			if(mUrl.indexOf("domme://profile/")!=-1){
				 String username = mUrl.replaceFirst("domme://profile/", "");
                 Intent theIntent = new Intent(DMUIManager.getInstance().getMainActivity().getBaseContext(), DMUserTimelineActivity.class);
                 theIntent.putExtra("user", DMAccountsManager.getCurUser());
                 DMUIManager.getInstance().getMainActivity().startActivity(theIntent);
			   }
		     }
		   
		   } 
}
