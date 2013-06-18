package com.domee.model;

import java.io.Serializable;

/**
 * Created by duyuan on 13-6-16.
 */
public class DMLists implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;    // "id": 221012250012006340,
    private String idstr;   //       "idstr": "221012250012006330",
    private String name;    //      "name": "同学",
//    private String mode;    //       "mode": "private",
//    private int visible;    //        "visible": 0,
//    private int like_count; //        "like_count": 0,
//    private int member_count;   //        "member_count": 41,
//    private String description; //        "description": "",
//    private         "tags": [],
//    private String profile_image_url;   //        "profile_image_url": "http://tp3.sinaimg.cn/2476776294/50/5619144918/1",
//    private User user;
//    private String created_at;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
