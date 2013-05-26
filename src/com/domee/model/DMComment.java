package com.domee.model;

import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
import com.domee.utils.DMConstants;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comment implements Serializable {

	private static final long serialVersionUID = 1L;
    private static final String TAG = "DMComment";

	private String created_at;		// 	string	评论创建时间
	private long id;				//	int64	评论的ID
	private String text;			//	string	评论的内容
	private String source;			//	string	评论的来源
	private User user;				//	object	评论作者的用户信息字段 详细
	private String mid;				//	string	评论的MID
	private String idstr;			//	string	字符串型的评论ID
	private DMStatus status;			//	object	评论的微博信息字段 详细
	private Comment reply_comment;	//	object	评论来源评论，当本评论属于对另一评论的回复时返回此字段

	public String getCreated_at() {
		return created_at;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public DMStatus getStatus() {
		return status;
	}

	public void setStatus(DMStatus status) {
		this.status = status;
	}

	public Comment getReply_comment() {
		return reply_comment;
	}

	public void setReply_comment(Comment reply_comment) {
		this.reply_comment = reply_comment;
	}

    public void extract2Link(TextView v) {
        v.setAutoLinkMask(0);

        Pattern mentionsPattern = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        String mentionsScheme = String.format("%s/?%s=", DMConstants.MENTIONS_SCHEMA, DMConstants.PARAM_SCREEN_NAME);
        Linkify.addLinks(v, mentionsPattern, mentionsScheme, new Linkify.MatchFilter() {

                    @Override
                    public boolean acceptMatch(CharSequence s, int start, int end) {
                        System.out.println(s);
                        return s.charAt(end - 1) != '.';
                    }
                }, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        Log.d(TAG, match.group());
                        System.out.println(match.group());
                        return match.group();
                    }
                }
        );

        Pattern trendsPattern = Pattern.compile("#(\\w+?)#");
        String trendsScheme = String.format("%s/?%s=", DMConstants.TRENDS_SCHEMA, DMConstants.PARAM_TREND);
        Linkify.addLinks(v, trendsPattern, trendsScheme, null, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                Log.d(TAG, match.group());
                return match.group();
            }
        });

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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "screen_name===========>" + getUser().getScreen_name() + "=======text=========>" + getText();
	}
}
