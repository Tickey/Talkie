package net.servoper.android.util;

import net.servoper.android.factory.ScaleParams;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class ScaleViewUtils {
	
	
	// --- scale view with LayoutInflator
	
	public static LinearLayout.LayoutParams getScaleLinearLayoutParams(AttributeSet attrs , LinearLayout.LayoutParams params , ScaleParams scaleParams) {
		boolean shouldScale = false;
		float scaleFactor = 1;
		String namespace = "";
		if(scaleParams != null) {
			shouldScale = scaleParams.isNeedToScale();
			scaleFactor = scaleParams.getScale();
			namespace = scaleParams.getNamespace();
		}
		
	    if(null != attrs && attrs.getAttributeBooleanValue(namespace, "scalable", true)) {
    	    if(shouldScale) {
                params.leftMargin *= scaleFactor;
                params.rightMargin *= scaleFactor;
                params.bottomMargin *= scaleFactor;
                params.topMargin *= scaleFactor;
    	        getScaleWidthAndHeight(attrs, params, namespace, scaleFactor);
            }
        }
	    return params;
	}	
	
	public static RelativeLayout.LayoutParams getScaleRelativeLayoutParams(AttributeSet attrs , RelativeLayout.LayoutParams params, ScaleParams scaleParams) {
		boolean shouldScale = false;
		float scaleFactor = 1;
		String namespace = "";
		if(scaleParams != null) {
			shouldScale = scaleParams.isNeedToScale();
			scaleFactor = scaleParams.getScale();
			namespace = scaleParams.getNamespace();
		}
		
	    if(null != attrs && attrs.getAttributeBooleanValue(namespace, "scalable", true)) {
    	    if(shouldScale) {
                params.leftMargin *= scaleFactor;
                params.rightMargin *= scaleFactor;
                params.bottomMargin *= scaleFactor;
                params.topMargin *= scaleFactor;
    	        getScaleWidthAndHeight(attrs, params, namespace, scaleFactor);
            }
        }
	    return params;
	}
	
	public static FrameLayout.LayoutParams getScaleFrameLayoutParams(AttributeSet attrs , FrameLayout.LayoutParams params, ScaleParams scaleParams)
    {
		boolean shouldScale = false;
		float scaleFactor = 1;
		String namespace = "";
		if(scaleParams != null) {
			shouldScale = scaleParams.isNeedToScale();
			scaleFactor = scaleParams.getScale();
			namespace = scaleParams.getNamespace();
		}
		
	    if(null != attrs && attrs.getAttributeBooleanValue(namespace, "scalable", true)) {
    	    if(shouldScale) {
                params.leftMargin *= scaleFactor;
                params.rightMargin *= scaleFactor;
                params.bottomMargin *= scaleFactor;
                params.topMargin *= scaleFactor;
    	        getScaleWidthAndHeight(attrs, params, namespace, scaleFactor);
            }
        }
	    return params;
    }
	
	//-- give view with scale padding parameters
	
	public static View getViewWithScalePaddingParams(View v , AttributeSet attrs, ScaleParams scaleParams) {
		boolean shouldScale = false;
		float scaleFactor = 1;
		String namespace = "";
		if(scaleParams != null) {
			shouldScale = scaleParams.isNeedToScale();
			scaleFactor = scaleParams.getScale();
			namespace = scaleParams.getNamespace();
		}
		
	    if(null != attrs && attrs.getAttributeBooleanValue(namespace, "scalable", true)) {
    	    if(shouldScale && v != null) {
    	    	int left = v.getPaddingLeft();
                left *= scaleFactor;
                int right = v.getPaddingRight();
                right *= scaleFactor;
                int bottom = v.getPaddingBottom();
                bottom *= scaleFactor;
                int top = v.getPaddingTop();
                top *= scaleFactor;
                v.setPadding(left, top, right, bottom);
            }
        }
        return v;
    }
       
	// -- scaling width and height 
	
	public static LayoutParams getScaleWidthAndHeight(AttributeSet attrs , LayoutParams params, String namespace, final float scaleFactor) {
		if(null != attrs && attrs.getAttributeBooleanValue(namespace, "scalable", true)) {
	        if(params.width != LayoutParams.FILL_PARENT && params.width != LayoutParams.WRAP_CONTENT
	                && params.width != 0 && params.width != 1   ) {
	            params.width *= scaleFactor;
	        }
	        
	        if(params.height != LayoutParams.FILL_PARENT && params.height != LayoutParams.WRAP_CONTENT 
	                && params.height != 0 && params.height != 1 ) {
	            params.height *= scaleFactor;
	        }
        }
        return params;
	}
	
	// --- scaling container
	
	public static void  scaleContainer(View v , ScaleParams scaleParams) {
		float scaleFactor = 1;
		String namespace = "";
		if(scaleParams != null) {
			scaleFactor = scaleParams.getScale();
			namespace = scaleParams.getNamespace();
		}
		if(null != v ) {
			if(v instanceof LinearLayout) 
				((LinearLayout)v).setLayoutParams(getScaleWidthAndHeight(null, ((LinearLayout) v).getLayoutParams(), namespace, scaleFactor));

			if(v instanceof RelativeLayout)
				((RelativeLayout)v).setLayoutParams(getScaleWidthAndHeight(null, ((RelativeLayout)v).getLayoutParams(), namespace, scaleFactor ));
			
			if(v instanceof FrameLayout)
				((FrameLayout)v).setLayoutParams(getScaleWidthAndHeight(null, ((FrameLayout)v).getLayoutParams(), namespace, scaleFactor));
		}
	}

	// --- scaling view
	
	public static void scaleView(View v, ScaleParams scaleParams)
	{
		float scaleFactor = 1;
		String namespace = "";
		if(scaleParams != null) {
			scaleFactor = scaleParams.getScale();
			namespace = scaleParams.getNamespace();
		}
		if(null != v.getLayoutParams()) {
			v.setLayoutParams(getScaleWidthAndHeight(null, v.getLayoutParams(), namespace, scaleFactor));
		}
		getViewWithScalePaddingParams(v, null , scaleParams);
	}
	
	public static void scaleView(View v , ViewGroup.LayoutParams params , ScaleParams scaleParams)
	{
		float scaleFactor = 1;
		String namespace = "";
		if(scaleParams != null) {
			scaleFactor = scaleParams.getScale();
			namespace = scaleParams.getNamespace();
		}
		if(null != params) {
			v.setLayoutParams(getScaleWidthAndHeight(null, params, namespace, scaleFactor));
		}
		getViewWithScalePaddingParams(v, null, scaleParams);
	}
	
	public static void scaleView(View v , int width , int height,  ScaleParams scaleParams)
	{
		
		if(width > 0 && height > 0) {
			float scaleFactor = 1;
			String namespace = "";
			if(scaleParams != null) {
				scaleFactor = scaleParams.getScale();
				namespace = scaleParams.getNamespace();
			}
			v.setLayoutParams(getScaleWidthAndHeight(null, new ViewGroup.LayoutParams(width, height), namespace, scaleFactor));
		}
		getViewWithScalePaddingParams(v, null, scaleParams);
	}	
	
	/** TextSize scaling methods */
	
	public static float getScaleTextSize(float size , ScaleParams scaleParams)
	{
		float result = size;
		if(scaleParams != null) {
		    float temp = size;
		    //if need we scale text . When device scale factor is not real
		    if( scaleParams.isNeedToScale()) {
		        temp /= scaleParams.getDeviceScaleFactor();
		        float textScale = ( scaleParams.getRealScaleFactor()) / 
		                (float) scaleParams.getDeviceScaleFactor();  
	            temp *= textScale;
	            return (int)temp;
	        }
		    result = temp / scaleParams.getDeviceScaleFactor();
		}
	    return (int) result;
	}
	
	// scale text size on current TextView with default text size
	
	public static void scaleTextSize(TextView tv, ScaleParams scaleParams)
	{
		float deviceScaleFactor = 1; 
		float realScaleFactor = 1;

		if(scaleParams != null) {
			deviceScaleFactor = scaleParams.getDeviceScaleFactor();
			realScaleFactor = scaleParams.getRealScaleFactor();
		}
				
		if(null != tv)
		{		
			scaleTextSize(tv , tv.getTextSize(),deviceScaleFactor,realScaleFactor);
		}
	}
	
	// scale text size on current TextView with custom text size
	
	public static void scaleTextSize(TextView tv , float size , float deviceScaleFactor, float realScaleFactor)
	{
		if(null != tv)
		{
			tv.setTextSize(scaleText(size, deviceScaleFactor , realScaleFactor));
		}
	}
	
	private static float scaleText(float size , float deviceScaleFactor, float realScaleFactor)
	{
        size /= deviceScaleFactor;
        float textScale = realScaleFactor / deviceScaleFactor;  
        size *= textScale;
        return (int) size;
	}
	
	/**
	 * in HTML must add this meta tag < meta name="viewport" content="width=value"/>
	 * value is equal to the width of html design
	 * @param webView , that must scale
	 * @param baseWidth , is optional. If baseWidth = 0 , then will get 320 px like base width  
	 */
	public static void scaleWebView(WebView webView, ScaleParams params) {
		if(params != null) {
			int wvScaleFactor =(int)((params.getDeviceScreenWidth() / params.getBaseWidth()) * 100);   
			webView.setInitialScale(wvScaleFactor);
		}
	}
}