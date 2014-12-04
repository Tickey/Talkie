package co.talkie_kids.talkie.data.model;

import com.google.gson.annotations.SerializedName;

public class WordLanguageReference {

	@SerializedName("word_id")
	public int wordId;

	@SerializedName("word")
	public String word;

	@SerializedName("raw")
	public String audioResourceUrl;

	@SerializedName("language_id")
	public int languageId;

}
