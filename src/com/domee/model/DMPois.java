package com.domee.model;

import java.io.Serializable;

/**
 * Created by duyuan on 13-5-23.
 */
public class DMPois implements Serializable {

    private static final long serialVersionUID = 1L;

    private String poiid;       //"poiid": "B2094654D069A6F4419C",
    private String title;       //"title": "三个贵州人(中关村店)",
    private String address;      // "address": "北四环西路58号理想国际大厦202-205",
    private String lon;             //"lon": "116.30999",
    private String lat;             //"lat": "39.98435",
    private int category;           //"category": "83",
    private String city;            //"city": "0010",
    private String province;        //"province": null,
    private String country;         //"country": null,
    private String url;             //"url": "",
    private String phone;           //"phone": "010-82607678",
    private String postcode;        //"postcode": "100000",
    private long weibo_id;          //"weibo_id": "0",
    private String categorys;        //"categorys": "64 69 83",
    private String category_name;        //"category_name": "云贵菜",
    private String icon;                //"icon": "http://u1.sinaimg.cn/upload/2012/03/23/1/xysh.png",
    private int checkin_num;                //"checkin_num": 0,
    private int checkin_user_num;           //"checkin_user_num": "0",
    private int tip_num;                //"tip_num": 0,
    private int photo_num;              //"photo_num": 0,
    private int todo_num;               //"todo_num": 0,
    private String distance;            //"distance": 70


    public String getPoiid() {
        return poiid;
    }

    public void setPoiid(String poiid) {
        this.poiid = poiid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public long getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(long weibo_id) {
        this.weibo_id = weibo_id;
    }

    public String getCategorys() {
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCheckin_num() {
        return checkin_num;
    }

    public void setCheckin_num(int checkin_num) {
        this.checkin_num = checkin_num;
    }

    public int getCheckin_user_num() {
        return checkin_user_num;
    }

    public void setCheckin_user_num(int checkin_user_num) {
        this.checkin_user_num = checkin_user_num;
    }

    public int getTip_num() {
        return tip_num;
    }

    public void setTip_num(int tip_num) {
        this.tip_num = tip_num;
    }

    public int getPhoto_num() {
        return photo_num;
    }

    public void setPhoto_num(int photo_num) {
        this.photo_num = photo_num;
    }

    public int getTodo_num() {
        return todo_num;
    }

    public void setTodo_num(int todo_num) {
        this.todo_num = todo_num;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
