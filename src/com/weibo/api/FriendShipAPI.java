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

    public FriendsShipAPI(Oauth2AccessToken oauth2AccessToken) {
        super(oauth2AccessToken);
    }

    public void groups(RequestListener listener) {

        request(SERVER_URL_PRIX + "/groups.json", null, HTTPMETHOD_GET,
                listener);
    }
}
