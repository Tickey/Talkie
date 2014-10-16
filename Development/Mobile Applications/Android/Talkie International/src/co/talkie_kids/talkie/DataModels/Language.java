package co.talkie_kids.talkie.DataModels;

import com.google.gson.annotations.SerializedName;

public class Language {

	@SerializedName("id")
	public int id;

	@SerializedName("name")
	public String name;

	@SerializedName("flag_drawable")
	public String languageFlagImageName;

}
