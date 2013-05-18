package com.domee.utils;

import com.domee.model.CommentResult;
import com.domee.model.StatusResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {

	private static GsonBuilder builder = new GsonBuilder();
	private static Gson gson = builder.create();
	
	public static StatusResult gson2Status(String jsonStr) {
		StatusResult sr = gson.fromJson(jsonStr, new TypeToken<StatusResult>(){}.getType());
		return sr;
	}
	public static CommentResult gson2Comment(String jsonStr) {
		CommentResult cr = gson.fromJson(jsonStr, new TypeToken<CommentResult>() {}.getType());
		return cr;
	}
}
