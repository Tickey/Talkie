package co.talkie_kids.talkie.DataModels;

import com.google.gson.annotations.SerializedName;

public class Word {

	@SerializedName("word_id")
	public int id;

	@SerializedName("drawable")
	public String imageName;
	
	@SerializedName("language_id")
	public int languageId;
}
