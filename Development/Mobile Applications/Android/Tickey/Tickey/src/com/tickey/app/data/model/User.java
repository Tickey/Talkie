package com.tickey.app.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
	
	public static final String KEY_FB_ACCESS_TOKEN = "access_token";
	
	public static final String KEY_AUTH_TOKEN = "Authorization";
	
	@SerializedName("token")
	public String authtoken;

}
