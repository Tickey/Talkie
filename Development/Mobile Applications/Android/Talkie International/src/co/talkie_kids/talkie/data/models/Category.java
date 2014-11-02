package co.talkie_kids.talkie.data.models;

import com.google.gson.annotations.SerializedName;

public class Category {

	@SerializedName("id")
	public int id;

	@SerializedName("unavailable_drawable")
	public String inactiveImageName;

	@SerializedName("opened_drawable")
	public String viewedImageName;

	@SerializedName("closed_drawable")
	public String activeImageName;

}
