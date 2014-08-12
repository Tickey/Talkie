package net.servoper.widgets;

import net.servoper.android.factory.ScaleParams;
import net.servoper.android.util.ScaleViewUtils;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class NewLinearLayouts extends android.widget.LinearLayout {

	protected ScaleParams mScaleParams;
	
	public NewLinearLayouts(Context context) {
		super(context);
	}

	public NewLinearLayouts(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NewLinearLayouts(Context context, AttributeSet attrs, ScaleParams scaleParams) {
		super(context, attrs);
		mScaleParams = scaleParams;
	}
	
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		LinearLayout.LayoutParams params = super.generateLayoutParams(attrs);
		return ScaleViewUtils.getScaleLinearLayoutParams(attrs, params, mScaleParams);
	}
}
