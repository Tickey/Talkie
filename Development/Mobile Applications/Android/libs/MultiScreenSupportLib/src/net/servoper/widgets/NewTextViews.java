package net.servoper.widgets;

import net.servoper.android.factory.ScaleParams;
import net.servoper.android.util.ScaleViewUtils;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class NewTextViews extends TextView {

	protected ScaleParams mScaleParams;
		
    public NewTextViews(Context context) {
        super(context);
    }

    public NewTextViews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewTextViews(Context context, AttributeSet attrs, ScaleParams scaleParams) {
        super(context, attrs);
        mScaleParams = scaleParams;
        scaleTextSizeIfNeeded(attrs, scaleParams);
    }
    
    public NewTextViews(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void scaleTextSizeIfNeeded(AttributeSet attrs, ScaleParams scaleParams)
    {

        if(attrs.getAttributeBooleanValue(scaleParams.getNamespace(), "scalableText", true))
        {
            setTextSize(ScaleViewUtils.getScaleTextSize(getTextSize(), scaleParams));
        }
    }
    
    public void setTextSizeScalable(float size)
    {
        setTextSize(size);
        setTextSize(ScaleViewUtils.getScaleTextSize(getTextSize(), mScaleParams));
    }
}
