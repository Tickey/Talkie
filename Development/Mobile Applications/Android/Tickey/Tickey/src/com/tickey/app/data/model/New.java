package com.tickey.app.data.model;

import com.google.gson.annotations.SerializedName;

public class New {

	@SerializedName("shortMessage")
	public String shortMessage;

	@SerializedName("username")
	public String username;

	@SerializedName("ownerAvatarUrl")
	public String ownerAvatarUrl;

	@SerializedName("imageUrl")
	public String postImageUrl;

	@SerializedName("votesCount")
	public int likes;

	@SerializedName("_id")
	public String id;
}
