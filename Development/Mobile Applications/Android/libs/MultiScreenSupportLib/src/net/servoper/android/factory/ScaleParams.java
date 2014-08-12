package net.servoper.android.factory;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScaleParams {

	private int mDeviceScreenWidth;
	private float mBaseWidth;
	private float mDeviceScaleFactor ;
	private float mRealScaleFactor ;
	private float mScale;
	
	private float mWebViewBaseWidth;
	
	private String mAppPackageName;
	private String mNamespace;

	private boolean mIsNeedToScale;
	
	public ScaleParams(final Context context, final int baseWidth, final int baseWebViewContentWidth) {
		if(context != null) {
			DisplayMetrics metrics = new DisplayMetrics();
	        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();       
	        display.getMetrics(metrics);
	        setDeviceScreenWidth(metrics.widthPixels);
	        
	        if(baseWidth < 240) 
	        	setBaseWidth(240);
	        else
	        	setBaseWidth(baseWidth);
	        
	        setAppPackageName(context.getPackageName());
	        setDeviceScaleFactor(metrics.scaledDensity);
			
			setNamespace("http://schemas.android.com/apk/res/" + getAppPackageName());
			setRealScaleFactor(getDeviceScreenWidth() / getBaseWidth());
			setWebViewBaseWidth(baseWebViewContentWidth);
			setScale(getRealScaleFactor() / getDeviceScaleFactor());
			if( getRealScaleFactor() != getDeviceScaleFactor())
				setIsNeedToScale(true);
			else
				setIsNeedToScale(false);
		}
	}


	public int getDeviceScreenWidth() {
		return mDeviceScreenWidth;
	}

	protected void setDeviceScreenWidth(int mDeviceScreenWidth) {
		this.mDeviceScreenWidth = mDeviceScreenWidth;
	}


	public float getBaseWidth() {
		return mBaseWidth;
	}

	protected void setBaseWidth(float mBaseWidth) {
		this.mBaseWidth = mBaseWidth;
	}

	public float getDeviceScaleFactor() {
		return mDeviceScaleFactor;
	}

	protected void setDeviceScaleFactor(float mDeviceScaleFactor) {
		this.mDeviceScaleFactor = mDeviceScaleFactor;
	}

	public float getRealScaleFactor() {
		return mRealScaleFactor;
	}

	protected void setRealScaleFactor(float mRealScaleFactor) {
		this.mRealScaleFactor = mRealScaleFactor;
	}

	public float getWebViewBaseWidth() {
		return mWebViewBaseWidth;
	}

	protected void setWebViewBaseWidth(float mWebViewBaseWidth) {
		this.mWebViewBaseWidth = mWebViewBaseWidth;
	}

	public String getAppPackageName() {
		return mAppPackageName;
	}

	protected void setAppPackageName(String mAppPackageName) {
		this.mAppPackageName = mAppPackageName;
	}

	public String getNamespace() {
		return mNamespace;
	}

	protected void setNamespace(String mNamespace) {
		this.mNamespace = mNamespace;
	}

	public float getScale() {
		return mScale;
	}

	protected void setScale(float mScale) {
		this.mScale = mScale;
	}

	public boolean isNeedToScale() {
		return mIsNeedToScale;
	}

	protected void setIsNeedToScale(boolean mIsNeedToScale) {
		this.mIsNeedToScale = mIsNeedToScale;
	};
}
