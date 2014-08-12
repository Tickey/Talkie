package net.servoper.widgets;

import net.servoper.android.factory.ScaleParams;
import net.servoper.android.util.ScaleViewUtils;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
public class NewButtons extends Button{

	private ScaleParams mScaleParams;
	
	public NewButtons(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public NewButtons(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public NewButtons(Context context, AttributeSet attrs, ScaleParams scaleParams) {
		super(context, attrs);
		mScaleParams = scaleParams;
		scaleTextSizeIfNeeded(attrs, scaleParams);
	}
	
	public NewButtons(Context context) {
		super(context);
	}
	
	private void scaleTextSizeIfNeeded(AttributeSet attrs, ScaleParams scaleParams)
    {

        if(attrs.getAttributeBooleanValue(scaleParams.getNamespace(), "scalableText", true))
        {
            setTextSize(ScaleViewUtils.getScaleTextSize(getTextSize() , scaleParams));
        }
    }
	
	 public void setTextSizeScalable(float size)
	 {
	        setTextSize(size);
	        setTextSize(ScaleViewUtils.getScaleTextSize(getTextSize() ,mScaleParams));
	  }
}
