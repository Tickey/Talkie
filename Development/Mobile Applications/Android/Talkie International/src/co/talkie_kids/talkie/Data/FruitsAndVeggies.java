package co.talkie_kids.talkie.Data;

import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.DataModels.TalkieObject;

public class FruitsAndVeggies implements TalkieObject {

	public final static String FRU_AND_VEGGY_KEY = "veggies"; 
	
	private static int[]  FRUITS_AND_VEGGIES_DRAWABLE_RESOURCES = { /*R.drawable.apple,
		R.drawable.banana, R.drawable.orange, R.drawable.strawberry, R.drawable.tomato,
		R.drawable.potato, R.drawable.pepper, R.drawable.kiwi, R.drawable.broccoli*/ };
	
	private static int[]  FRUITS_AND_VEGGIES_RAW_SOUND_RESOURCES = { R.raw.apple,
		R.raw.banana, R.raw.orange, R.raw.strawberry, R.raw.tomato,
		R.raw.potato, R.raw.pepper, R.raw.kiwi, R.raw.broccoli };
	
	private static String[]  FRUITS_AND_VEGGIES_WORDS = { "apple", "banana" , "orange", "strawberry", "tomato",
		"potato", "pepper", "kiwi", "broccoli" };

	public int getDrawable(int index) {
			return FRUITS_AND_VEGGIES_DRAWABLE_RESOURCES[index];
	}
	
	public int getCount() {
		return FRUITS_AND_VEGGIES_DRAWABLE_RESOURCES.length;
	}
	
	public int getSound(int index) {
			return FRUITS_AND_VEGGIES_RAW_SOUND_RESOURCES[index];
	}
	
	public String getWord(int index) {
			return FRUITS_AND_VEGGIES_WORDS[index];
	}

	@Override
	public String getKey() {
		return FRU_AND_VEGGY_KEY;
	}
}