package co.talkie_kids.talkie.DataModels;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Category {

	@SerializedName("is_available")
	public boolean isAvailable;
	
	@SerializedName("is_started")
	public boolean isStarted;
	
	@SerializedName("closed_drawable")
	public String closedDrawable;
	
	@SerializedName("opened_drawable")
	public String openedDrawable;
	
	@SerializedName("unavailable_drawable")
	public String unavailableDrawable;
	
	@SerializedName("words")
	public ArrayList<Word> words;
}
