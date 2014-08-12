package net.servoper.widgets;

import net.servoper.android.factory.ScaleParams;
import net.servoper.android.util.ScaleViewUtils;


import android.content.Context;
import android.util.AttributeSet;


public class NewRelativeLayouts extends android.widget.RelativeLayout {

	protected ScaleParams mScaleParams;
	
	public NewRelativeLayouts(Context context) {
		super(context);
	}

	public NewRelativeLayouts(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NewRelativeLayouts(Context context, AttributeSet attrs, ScaleParams scaleParams) {
		super(context, attrs);
		mScaleParams = scaleParams;
	}
	
	public NewRelativeLayouts(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
    	    
//    	 if(null != attrs && attrs.getAttributeBooleanValue(Constants.getNameSpace(), "scalable", true))
//         {
//    		 LayoutParams params = super.generateLayoutParams(attrs);
//    		 return ScaleViewUtils.getScaleRelativeLayoutParams(attrs , params);
//         }
//    	return super.generateLayoutParams(attrs);
    	
      	LayoutParams params = super.generateLayoutParams(attrs);
      		return ScaleViewUtils.getScaleRelativeLayoutParams(attrs , params, mScaleParams);
    }
}
