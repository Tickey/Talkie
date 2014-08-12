package net.servoper.android.factory;

import net.servoper.android.util.ScaleViewUtils;
import net.servoper.widgets.NewButtons;
import net.servoper.widgets.NewEditTexts;
import net.servoper.widgets.NewFrameLayouts;
import net.servoper.widgets.NewImageViews;
import net.servoper.widgets.NewLinearLayouts;
import net.servoper.widgets.NewRelativeLayouts;
import net.servoper.widgets.NewTextViews;
import net.servoper.widgets.NewWebViews;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater.Factory;
import android.view.View;

public class LayoutInflateFactory implements Factory {
	
	public LayoutInflateFactory(Context context, int baseWidth, int baseWebViewContentWidth) {
		mScaleParams = new ScaleParams(context, baseWidth, baseWebViewContentWidth);
	}
	protected ScaleParams mScaleParams;
	
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return ScaleViewUtils.getViewWithScalePaddingParams(pleaseCreateView(name, context, attrs) , attrs , mScaleParams);
	}

	protected View pleaseCreateView(String name, Context context,
			AttributeSet attrs) {

		if (android.widget.LinearLayout.class.getSimpleName().equals(name)) {
			return new NewLinearLayouts(context, attrs, mScaleParams);
		}

		if (android.widget.RelativeLayout.class.getSimpleName().equals(name)) {
			return new NewRelativeLayouts(context, attrs, mScaleParams);
		}

		if (android.widget.FrameLayout.class.getSimpleName().equals(name)) {
			return new NewFrameLayouts(context, attrs, mScaleParams);
		}

		if (android.widget.ImageView.class.getSimpleName().equals(name)) {
			return new NewImageViews(context, attrs);
		}

		if (android.widget.TextView.class.getSimpleName().equals(name)) {
			return new NewTextViews(context, attrs, mScaleParams);
		}

		if (android.widget.Button.class.getSimpleName().equals(name)) {
			return new NewButtons(context, attrs, mScaleParams);
		}

		if (android.widget.EditText.class.getSimpleName().equals(name)) {
			return new NewEditTexts(context, attrs, mScaleParams);
		}
		
		if(android.webkit.WebView.class.getSimpleName().equals(name)) {
			return new NewWebViews(context, attrs, mScaleParams);
		}

		return null;
	}
}
