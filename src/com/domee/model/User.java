package com.domee.model;

import java.io.Serializable;

//{"id":1774230022,"idstr":"1774230022","screen_name":"周远远","name":"周远远","province":"11","city":"1000",
//"location":"北京","description":"","url":"","profile_image_url":"http://tp3.sinaimg.cn/1774230022/50/5629274977/1",
//"profile_url":"u/1774230022","domain":"","weihao":"","gender":"m","followers_count":207,"friends_count":194,"statuses_count":681,
//"favourites_count":75,"created_at":"Fri Jul 30 14:00:37 +0800 2010","following":false,"allow_all_act_msg":false,"geo_enabled":true,
//"verified":false,"verified_type":-1,"remark":"",
//"status":{"created_at":"Fri Apr 26 09:37:49 +0800 2013","id":3571268693395472,
//"mid":"3571268693395472","idstr":"3571268693395472","text":" 真他妈难产啊","source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">新浪微博</a>",
//"favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],
//"geo":null,"reposts_count":0,"comments_count":0,"attitudes_count":0,"mlevel":0,"visible":{"type":0,"list_id":0}},

//"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/1774230022/180/5629274977/1","verified_reason":"",
//"follow_me":false,"online_status":0,"bi_followers_count":78,"lang":"zh-cn","star":0,"mbtype":2,"mbrank":1,"block_word":0}

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;					//	int64	用户UID
	private String idstr;				//	string	字符串型的用户UID
	private String screen_name;			//	string	用户昵称
	private String name;				//	string	友好显示名称
	private int province;				//	int	用户所在省级ID
	private int city;					//	int	用户所在城市ID
	private String location;			//	string	用户所在地
	private String description;			//	string	用户个人描述
	private String url;					//	string	用户博客地址
	private String profile_image_url;	//	string	用户头像地址，50×50像素
	private String profile_url;			//	string	用户的微博统一URL地址
	private String domain;				//	string	用户的个性化域名
	private String weihao;				//	string	用户的微号
	private String gender;				//	string	性别，m：男、f：女、n：未知
	private int followers_count;		//	int	粉丝数
	private int friends_count;			//	int	关注数
	private int statuses_count;			//	int	微博数
	private int favourites_count;		//	int	收藏数
	private String created_at;			//	string	用户创建（注册）时间
	private boolean following;			//	boolean	暂未支持
	private boolean allow_all_act_msg;	//	boolean	是否允许所有人给我发私信，true：是，false：否
	private boolean geo_enabled;		//	boolean	是否允许标识用户的地理位置，true：是，false：否
	private boolean verified;			//	boolean	是否是微博认证用户，即加V用户，true：是，false：否
	private int verified_type;			//	int	暂未支持
	private String remark;				//	string	用户备注信息，只有在查询用户关系时才返回此字段
	private Status status;				//	object	用户的最近一条微博信息字段 详细
	private boolean allow_all_comment;	//	boolean	是否允许所有人对我的微博进行评论，true：是，false：否
	private String avatar_large;		//	string	用户大头像地址
	private String verified_reason;		//	string	认证原因
	private boolean follow_me;			//	boolean	该用户是否关注当前登录用户，true：是，false：否
	private int online_status;			//	int	用户的在线状态，0：不在线、1：在线
	private int bi_followers_count;		//	int	用户的互粉数
	private String lang;				//	string	用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getProfile_url() {
		return profile_url;
	}

	public void setProfile_url(String profile_url) {
		this.profile_url = profile_url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public int getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isAllow_all_act_msg() {
		return allow_all_act_msg;
	}

	public void setAllow_all_act_msg(boolean allow_all_act_msg) {
		this.allow_all_act_msg = allow_all_act_msg;
	}

	public boolean isGeo_enabled() {
		return geo_enabled;
	}

	public void setGeo_enabled(boolean geo_enabled) {
		this.geo_enabled = geo_enabled;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerified_type() {
		return verified_type;
	}

	public void setVerified_type(int verified_type) {
		this.verified_type = verified_type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isAllow_all_comment() {
		return allow_all_comment;
	}

	public void setAllow_all_comment(boolean allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public boolean isFollow_me() {
		return follow_me;
	}

	public void setFollow_me(boolean follow_me) {
		this.follow_me = follow_me;
	}

	public int getOnline_status() {
		return online_status;
	}

	public void setOnline_status(int online_status) {
		this.online_status = online_status;
	}

	public int getBi_followers_count() {
		return bi_followers_count;
	}

	public void setBi_followers_count(int bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "screen_name=======>" + screen_name;
	}
}
