package co.talkie_kids.talkie.DataModels;

import com.google.gson.annotations.SerializedName;

public class Word {

	@SerializedName("english_word")
	public String engishWord;

	@SerializedName("bulgarian_word")
	public String bulgarianWord;
	
	@SerializedName("english_sound")
	public String englishRawName;
	
	@SerializedName("bulgarian_sound")
	public String bulgarianRawName;

	@SerializedName("drawable")
	public String drawable;

	@SerializedName("bulgarian")
	public WordProgress bulgarianProgress;

	@SerializedName("english")
	public WordProgress englishProgress;
}
