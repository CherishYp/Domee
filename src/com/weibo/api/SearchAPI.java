package com.weibo.api;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

/**
 * Created by duyuan on 13-5-25.
 */
public class SearchAPI extends WeiboAPI {

    private static final String SERVER_URL_PRIX = API_SERVER + "/search";

    public SearchAPI(Oauth2AccessToken oauth2AccessToken) {
        super(oauth2AccessToken);
    }

    public void topics(String q, int count, int page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.add("q", q);
        params.add("count", count);
        params.add("page", page);
        request(SERVER_URL_PRIX + "/topics.json", params, HTTPMETHOD_GET,
                listener);
    }


}
