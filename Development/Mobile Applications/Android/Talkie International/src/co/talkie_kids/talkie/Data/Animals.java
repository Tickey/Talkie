package co.talkie_kids.talkie.data;

import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.data.model.TalkieObject;

public class Animals implements TalkieObject {

	public final static String ANIMALS_KEY = "animals";

	private final static int[] ANIMALS_DRAWABLE_RESOURCES = {/*
															 * R.drawable.elephant
															 * , R.drawable.lion
															 * ,
															 * R.drawable.penguin
															 * ,
															 * R.drawable.bear,
															 * R
															 * .drawable.giraffe
															 * ,
															 * R.drawable.hippo,
															 * R
															 * .drawable.monkey,
															 * R
															 * .drawable.turtle,
															 * R.drawable.zebra
															 */};

	private static int[] ANIMALS_RAW_SOUND_RESOURCES = { R.raw.elephant,
			R.raw.lion, R.raw.penguin, R.raw.bear, R.raw.giraffe, R.raw.hypo,
			R.raw.monkey, R.raw.turtle, R.raw.zebra };

	private static String[] ANIMALS_WORDS = { "elephant", "lion", "penguin",
			"bear", "giraffe", "hippo", "monkey", "turtle", "zebra" };

	public int getDrawable(int index) {
		return ANIMALS_DRAWABLE_RESOURCES[index];
	}

	public int getCount() {
		return ANIMALS_DRAWABLE_RESOURCES.length;
	}

	public int getSound(int index) {
		return ANIMALS_RAW_SOUND_RESOURCES[index];
	}

	public String getWord(int index) {
		return ANIMALS_WORDS[index];
	}

	@Override
	public String getKey() {
		return ANIMALS_KEY;
	}
}