package co.talkie_kids.talkie.data.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {

	@SerializedName("is_update_needed")
	public boolean isUpdateNeeded;

	@SerializedName("url_resources")
	public String imageResourcesPathURL;

	@SerializedName("url_audio")
	public String audioResourcesPathURL;

	@SerializedName("words")
	public ArrayList<Word> words;

	@SerializedName("categories")
	public ArrayList<Category> categories;

	@SerializedName("languages")
	public ArrayList<Language> languages;

	@SerializedName("ref_words_languages")
	public ArrayList<WordLanguageReference> wordLanguageReference;

	@SerializedName("last_updated")
	public String lastUpdatedDate;

}
