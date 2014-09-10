package co.talkie_kids.talkie.Activities;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.DataModels.UpdateResponse;
import co.talkie_kids.talkie.DataModels.Word;
import co.talkie_kids.talkie.Network.Utilities.ConnectionCheck;
import co.talkie_kids.talkie.Network.Utilities.ServerActionListener;
import co.talkie_kids.talkie.Network.Utilities.ServerHandler;
import co.talkie_kids.talkie.utilities.DeviceSpecifications;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class SplashScreenActivity extends BaseTalkieActivity {
	
	private static final int DELAY_TO_START_NEXT_ACTIVITY = 1000;

	private static final String KEY_IS_UPDATE_NEEDED = "is_update_needed";

	protected static final String TAG = SplashScreenActivity.class.getSimpleName();
	
	private ProgressBar mDownloadProgressbar;
	private TextView mActionLabel;

	private ImageLoader mImageLoader;

	protected int mDownloadedImages = 0;
	protected int mFailedImagesCount = 0;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ImageLoaderConfiguration config =
        		new ImageLoaderConfiguration.Builder(getApplicationContext())
            .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);
        
        setContentView(R.layout.splash_screen);
        
        mActionLabel = (TextView) findViewById(android.R.id.text1);
        mDownloadProgressbar = (ProgressBar)
        		findViewById(android.R.id.progress);
        
        mDownloadProgressbar.setVisibility(View.INVISIBLE);

        mActionLabel.setText(R.string.checking_internet);
        
        if( ConnectionCheck.isOnline(getApplicationContext()) ) {
	        mActionLabel.setText(R.string.checking_for_updates);
	
	        String screenSizeValue = DeviceSpecifications.getScreenSize(getResources());
	        
	        String densityDpiValue = DeviceSpecifications.getDpiDensity(getResources());
	        
	        String deviceId = DeviceSpecifications.getDeviceId(getApplication());
	        
	        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	        
	        params.add( new BasicNameValuePair("screen_size", "normal") );
	        params.add( new BasicNameValuePair("density", "ldpi") );
			params.add( new BasicNameValuePair("device_id", deviceId) );
			
			new ServerHandler(ServerHandler.POST, params,
	        		getString(R.string.url_get_resources),
	        		mGetRetailersServerActionListener, true);
        } else {
			mActionLabel.setText(getString(R.string.no_internet));
		}
	}
	
	private ServerActionListener mGetRetailersServerActionListener =
			new ServerActionListener() {
				
				@Override
				public void preExecuteAction() {
				}
				
				@Override
				public void postAction(boolean isSuccessful, Object json) {
					if( isSuccessful ) {
						isSuccessful = false;
						JSONObject jsonObject = (JSONObject) json;
						
						if( jsonObject.has(KEY_IS_UPDATE_NEEDED) ) {
							try {
								if( jsonObject.getBoolean(KEY_IS_UPDATE_NEEDED) ) {
									mActionLabel.setText(
											getString(R.string
													.update_resources_fetched));
									
									isSuccessful = true;
									
									updateResourcesFetched(jsonObject);
								} else {
									continueToApp(getString(R.string.no_update));
								}
							} catch (JSONException e) {
								continueToApp(getString(R.string.unknown_update));
								e.printStackTrace();
							}
						} else {
							continueToApp(getString(R.string.unknown_update));
						}
						
					} else {
						continueToApp(getString(R.string
								.cant_connect_to_server));
					}

					Log.v(TAG, "isSuccessful: " + Boolean.toString(isSuccessful));
				}
			};

	private int mWordsStartedLoading = 0;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void updateProgress(int progress) {
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
		    ObjectAnimator animation = ObjectAnimator.ofInt(mDownloadProgressbar,
		    		"progress", progress); 
		    animation.setDuration(500);
		    animation.setInterpolator(new DecelerateInterpolator());
		    animation.start();
		} else {
			mDownloadProgressbar.setProgress(progress);
		}
	}

	protected void updateResourcesFetched(JSONObject jsonObject) {
		mActionLabel.setText(getString(R.string.downloading_resources));
        
        mDownloadProgressbar.setVisibility(View.VISIBLE);
        
		Type responseType = new TypeToken<UpdateResponse>() {
		        }.getType();
		final UpdateResponse update = new Gson()
				.fromJson(jsonObject.toString(), responseType);
		
		mDownloadProgressbar.setMax(update.words.size());
		
		Log.v(TAG, "MAX: " + mDownloadProgressbar.getMax());
		
		String resourcesBasePath = update.imageResourcesPathURL;
		
		for( Word word : update.words) {
			String imageUrl = resourcesBasePath + word.imageName;
			mWordsStartedLoading++;
			//Log.v(TAG, imageUrl);
			
			mImageLoader.loadImage( imageUrl,
					new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri,
			    		View view, Bitmap loadedImage) {
					
			    	updateProgress(++mDownloadedImages);
	    			
	    			Log.v(TAG, "mFailedImagesCount: " + mFailedImagesCount);
	    			Log.v(TAG, "mDownloadedImages: " + mDownloadedImages);
	    			Log.v(TAG, "mDownloadedImages: " + update.words.size());
	    			Log.v(TAG, "mWordsStartedLoading: " + mWordsStartedLoading);
			    	
			    	if(mDownloadedImages + mFailedImagesCount ==
			    			update.words.size() ) {
				    	if( mDownloadProgressbar.getMax() == mDownloadedImages ) {
							continueToApp(getString(R.string.update_completed));
				    	} else {
							continueToApp(getString(R.string
									.downloading_resources_faled));
						}
			    	}
			    	
			    }
			    
			    @Override
	    		public void onLoadingFailed(String imageUri, View view,
	    				FailReason failReason) {
	    			super.onLoadingFailed(imageUri, view, failReason);
	    			
	    			mFailedImagesCount++;
	    			
	    			Log.v(TAG, "mFailedImagesCount: " + mFailedImagesCount);
	    			Log.v(TAG, "mDownloadedImages: " + mDownloadedImages);
	    			Log.v(TAG, "mDownloadedImages: " + update.words.size());
	    			Log.v(TAG, "mWordsStartedLoading: " + mWordsStartedLoading);
	    			
			    	if(mDownloadedImages + mFailedImagesCount ==
			    			update.words.size() ) {
						continueToApp(getString(R.string
								.downloading_resources_faled));
			    	}
	    		}
			});
		}
	}

	protected void continueToApp(String message) {
		mActionLabel.setText(message);
		
		new Handler().postDelayed( new Runnable() {
			
			@Override
			public void run() {
				
				startActivity( new Intent(getApplicationContext(),
						CategoryChooseActivity.class));
				
				finish();
			}
		}, DELAY_TO_START_NEXT_ACTIVITY);
	}

}
