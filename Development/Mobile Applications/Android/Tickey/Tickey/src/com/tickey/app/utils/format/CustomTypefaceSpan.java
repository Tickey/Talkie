package com.tickey.app.utils.format;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {

	private final Typeface newType;
	private float mTextSize = 0f;
	private int mTextColor = 0;

	public CustomTypefaceSpan(String family, Typeface type) {
		super(family);
		newType = type;
	}

	public void setTextSize(float size) {
		mTextSize = size;
	}

	public void setTextColor(int color) {
		mTextColor = color;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		applyCustomTypeFace(ds, newType);
	}

	@Override
	public void updateMeasureState(TextPaint paint) {
		applyCustomTypeFace(paint, newType);
	}

	private void applyCustomTypeFace(Paint paint, Typeface tf) {

		paint.setTypeface(tf);

		if (mTextSize > 0) {
			paint.setTextSize(mTextSize);
		}

		if (mTextColor != 0) {
			paint.setColor(mTextColor);
		}
	}
}