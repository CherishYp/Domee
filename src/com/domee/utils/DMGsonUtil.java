package com.domee.utils;

import com.domee.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DMGsonUtil {

	private static GsonBuilder builder = new GsonBuilder();
	private static Gson gson = builder.create();
	
	public static DMStatusResult gson2Status(String jsonStr) {
		DMStatusResult sr = gson.fromJson(jsonStr, new TypeToken<DMStatusResult>(){}.getType());
		return sr;
	}
	public static CommentResult gson2Comment(String jsonStr) {
		CommentResult cr = gson.fromJson(jsonStr, new TypeToken<CommentResult>() {}.getType());
		return cr;
	}
	
	public static FriendsResult gson2Friends(String jsonStr) {
		FriendsResult fr = gson.fromJson(jsonStr, new TypeToken<FriendsResult>() {}.getType());
		return fr;
	}

    public static User gson2User(String jsonStr) {
        User user = gson.fromJson(jsonStr, new TypeToken<User>(){}.getType());
        return user;
    }

    public static DMNearBy gson2Pois(String jsonStr) {
        DMNearBy nb = gson.fromJson(jsonStr, new TypeToken<DMNearBy>(){}.getType());
        return nb;
    }

    public static DMStatusResult gson2Topic(String jsonStr) {
        DMStatusResult sr = gson.fromJson(jsonStr, new TypeToken<DMStatusResult>(){}.getType());
        return sr;
    }
}
