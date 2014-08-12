package net.servoper.widgets;

import net.servoper.android.factory.ScaleParams;
import net.servoper.android.util.ScaleViewUtils;


import android.content.Context;
import android.util.AttributeSet;


public class NewFrameLayouts extends android.widget.FrameLayout {

	protected ScaleParams mScaleParams;
	
    public NewFrameLayouts(Context context) {
        super(context);
    }

    public NewFrameLayouts(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewFrameLayouts(Context context, AttributeSet attrs, ScaleParams scaleParams) {
        super(context, attrs);
        mScaleParams = scaleParams;
    }
    
    public NewFrameLayouts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {

        LayoutParams params = super.generateLayoutParams(attrs);
        return ScaleViewUtils.getScaleFrameLayoutParams(attrs,params, mScaleParams);
    }

}
