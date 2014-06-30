package co.talkie_kids.talkie.utilities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class DeviceSpecifications {

	public static final String JSON_KEY_SCREEN_SIZE = "screen_size";
	
	public static final String JSON_VALUE_SCREEN_SIZE_XLARGE = "xlarge";
	public static final String JSON_VALUE_SCREEN_SIZE_LARGE = "large";
	public static final String JSON_VALUE_SCREEN_SIZE_NORMAL = "normal";
	public static final String JSON_VALUE_SCREEN_SIZE_SMALL = "small";
	public static final String JSON_VALUE_SCREEN_SIZE_UNKNOWN = "unknown";

	public static final String JSON_KEY_DENSITY_DPI = "density_dpi";

	public static final String JSON_VALUE_DENSITY_DPI_LOW = "ldpi";
	public static final String JSON_VALUE_DENSITY_DPI_MEDIUM = "mdpi";
	public static final String JSON_VALUE_DENSITY_DPI_TV = "tvdpi";
	public static final String JSON_VALUE_DENSITY_DPI_HIGH = "hdpi";
	public static final String JSON_VALUE_DENSITY_DPI_XHIGH = "xhdpi";
	public static final String JSON_VALUE_DENSITY_DPI_XXHIGH = "xxhdpi";
	public static final String JSON_VALUE_DENSITY_DPI_XXXHIGH = "xxxhdpi";
	public static final String JSON_VALUE_DENSITY_DPI_UNKNOWN = "unknown";

	public static final String JSON_KEY_DEVICE_ID = "device_id";

	public static String getDpiDensity(Resources resources) {
		DisplayMetrics metrics = resources.getDisplayMetrics();
        
        String densityDpiValue;
        
        switch (metrics.densityDpi) {
			case DisplayMetrics.DENSITY_LOW:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_LOW;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_MEDIUM;
				break;
			case DisplayMetrics.DENSITY_TV:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_TV;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_HIGH;
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_XHIGH;
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_XXHIGH;
				break;
			case DisplayMetrics.DENSITY_XXXHIGH:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_XXXHIGH;
				break;
			default:
				densityDpiValue = JSON_VALUE_DENSITY_DPI_UNKNOWN;
				break;
		}
		return densityDpiValue;
	}

	public static String getScreenSize(Resources resources) {
		int screenSize = resources.getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        
        String screenSizeValue;
        
        switch(screenSize) {
	        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
	        	screenSizeValue = JSON_VALUE_SCREEN_SIZE_XLARGE;
	            break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            	screenSizeValue = JSON_VALUE_SCREEN_SIZE_LARGE;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
            	screenSizeValue = JSON_VALUE_SCREEN_SIZE_NORMAL;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            	screenSizeValue = JSON_VALUE_SCREEN_SIZE_SMALL;
                break;
            default:
            	screenSizeValue = JSON_VALUE_SCREEN_SIZE_UNKNOWN;
        }
        
		return screenSizeValue;
	}

	public static String getDeviceId(Application application) {
		String packageName = application.getPackageName();
		
		SharedPreferences prefs = application.getApplicationContext().getSharedPreferences(
				packageName, Context.MODE_PRIVATE);
        
        String keyDeviceId = packageName + "." + JSON_KEY_DEVICE_ID;
        
        String deviceId = prefs.getString(keyDeviceId, null);
        
        if( deviceId == null) {
	        TelephonyManager telephonyManager =
	        		(TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
	        
	        deviceId = telephonyManager.getDeviceId();
	        
	        if( deviceId == null ) {
	        	deviceId = Secure.getString(application.getContentResolver(), Secure.ANDROID_ID);
	        } else {
	        	if( deviceId.length() == 0 ) {
	            	deviceId = Secure.getString(application.getContentResolver(), Secure.ANDROID_ID);
	        	}
	        }
	        
	        prefs.edit().putString(keyDeviceId, deviceId).commit();
        }
        
		return deviceId;
	}
}
