package net.servoper.widgets;

import net.servoper.android.factory.ScaleParams;
import net.servoper.android.util.ScaleViewUtils;


import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class NewWebViews extends WebView {

	public NewWebViews(Context context) {
		super(context);
	}
	
	public NewWebViews(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public NewWebViews(Context context, AttributeSet attrs, ScaleParams scaleParams) {
		super(context, attrs);
		ScaleViewUtils.scaleWebView(this, scaleParams);
	}
	
	public NewWebViews(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	//TODO override loadURl and execute js to get view port width from html  
}
