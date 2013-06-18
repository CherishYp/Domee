package com.weibo.api;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

/**
 * Created by duyuan on 13-6-16.
 */
public class FriendShipAPI extends WeiboAPI {

    private static final String SERVER_URL_PRIX = API_SERVER + "/friendships";

    public FriendShipAPI(Oauth2AccessToken oauth2AccessToken) {
        super(oauth2AccessToken);
    }

    public void groups(RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        request(SERVER_URL_PRIX + "/groups.json", params, HTTPMETHOD_GET,
                listener);
    }

    public void timeline(String list_id, long since_id, long max_id, int count, int page,
                          boolean base_app, FEATURE feature, boolean trim_user, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.add("list_id", list_id);
        params.add("since_id", since_id);
        params.add("max_id", max_id);
        params.add("count", count);
        params.add("page", page);
        if (base_app) {
            params.add("base_app", 1);
        } else {
            params.add("base_app", 0);
        }
        params.add("feature", feature.ordinal());
        if (trim_user) {
            params.add("trim_user", 1);
        } else {
            params.add("trim_user", 0);
        }

        request(SERVER_URL_PRIX + "/groups/timeline.json", params, HTTPMETHOD_GET,
                listener);
    }
}
