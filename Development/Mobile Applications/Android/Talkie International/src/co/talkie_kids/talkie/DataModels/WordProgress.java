package co.talkie_kids.talkie.DataModels;

import com.google.gson.annotations.SerializedName;

public class WordProgress {

	@SerializedName("is_available")
	public boolean isAvailable;

	@SerializedName("is_answered")
	public boolean isAnswered;

}
