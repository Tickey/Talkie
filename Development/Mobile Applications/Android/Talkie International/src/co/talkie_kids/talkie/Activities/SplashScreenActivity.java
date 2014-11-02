package co.talkie_kids.talkie.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.data.models.Category;
import co.talkie_kids.talkie.data.models.UpdateResponse;
import co.talkie_kids.talkie.data.models.Word;
import co.talkie_kids.talkie.data.models.WordLanguageReference;
import co.talkie_kids.talkie.network.utilities.ConnectionCheck;
import co.talkie_kids.talkie.network.utilities.ServerHandler;
import co.talkie_kids.talkie.network.utilities.ServerResponseListener;
import co.talkie_kids.talkie.resources.ImageLoader;
import co.talkie_kids.talkie.utilities.DeviceSpecifications;
import co.talkie_kids.talkie.utilities.StorageHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SplashScreenActivity extends BaseTalkieActivity {

	private static final int DELAY_TO_START_NEXT_ACTIVITY = 1000;

	private static final String KEY_IS_UPDATE_NEEDED = "is_update_needed";

	protected static final String TAG = SplashScreenActivity.class
			.getSimpleName();

	private ProgressBar mDownloadProgressbar;
	private TextView mActionLabel;

	private ImageLoader mImageLoader;

	private UpdateResponse mUpdateResponse;

	protected int mDownloadedImagesCount = 0;
	protected int mFailedImagesCount = 0;
	protected int mStartedLoadingImagesCount = 0;

	private boolean mIsFinished = false;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mImageLoader = new ImageLoader(getApplicationContext());

		setContentView(R.layout.splash_screen);

		mActionLabel = (TextView) findViewById(android.R.id.text1);
		mDownloadProgressbar = (ProgressBar) findViewById(android.R.id.progress);

		mDownloadProgressbar.setVisibility(View.INVISIBLE);

		mActionLabel.setText(R.string.checking_internet);

		if (mStartedLoadingImagesCount == 0) {

			if (ConnectionCheck.isOnline(getApplicationContext())) {
				mActionLabel.setText(R.string.checking_for_updates);

				String screenSizeValue = DeviceSpecifications
						.getScreenSize(getResources());

				String densityDpiValue = DeviceSpecifications
						.getDpiDensity(getResources());

				String deviceId = DeviceSpecifications
						.getDeviceId(getApplication());

				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("screen_size",
						screenSizeValue));
				params.add(new BasicNameValuePair("density", densityDpiValue));
				params.add(new BasicNameValuePair("device_id", deviceId));

				new ServerHandler(ServerHandler.POST, params,
						getString(R.string.url_get_resources),
						mGetRetailersServerActionListener, true);
			} else {
				mActionLabel.setText(getString(R.string.no_internet));
			}
		} else {
			updateProgress(mDownloadedImagesCount);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}

	private ServerResponseListener mGetRetailersServerActionListener = new ServerResponseListener() {

		@Override
		public void preExecuteAction() {
		}

		@Override
		public void postAction(boolean isSuccessful, Object json) {
			if (isSuccessful) {
				isSuccessful = false;
				JSONObject jsonObject = (JSONObject) json;

				if (jsonObject.has(KEY_IS_UPDATE_NEEDED)) {
					try {
						if (jsonObject.getBoolean(KEY_IS_UPDATE_NEEDED)) {
							mActionLabel
									.setText(getString(R.string.update_resources_fetched));

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
				continueToApp(getString(R.string.cant_connect_to_server));
			}

			Log.v(TAG, "isSuccessful: " + Boolean.toString(isSuccessful));
		}
	};

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void updateProgress(int progress) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ObjectAnimator animation = ObjectAnimator.ofInt(
					mDownloadProgressbar, "progress", progress);
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
		mUpdateResponse = new Gson().fromJson(jsonObject.toString(),
				responseType);

		String resourcesBasePath = mUpdateResponse.imageResourcesPathURL;

		ArrayList<String> imageUrlsToBeDownloaded = new ArrayList<String>();

		for (Word word : mUpdateResponse.words) {
			String imageUrl = resourcesBasePath + word.imageName;

			if (!imageUrlsToBeDownloaded.contains(imageUrl)) {
				imageUrlsToBeDownloaded.add(imageUrl);
			}
		}

		for (Category category : mUpdateResponse.categories) {
			String imageUrl = resourcesBasePath + category.activeImageName;

			if (!imageUrlsToBeDownloaded.contains(imageUrl)) {
				imageUrlsToBeDownloaded.add(imageUrl);
			}

			imageUrl = resourcesBasePath + category.inactiveImageName;

			if (!imageUrlsToBeDownloaded.contains(imageUrl)) {
				imageUrlsToBeDownloaded.add(imageUrl);
			}

			imageUrl = resourcesBasePath + category.viewedImageName;

			if (!imageUrlsToBeDownloaded.contains(imageUrl)) {
				imageUrlsToBeDownloaded.add(imageUrl);
			}
		}

		for (WordLanguageReference wordLangRef : mUpdateResponse.wordLanguageReference) {

			String audioResourceUrl = mUpdateResponse.audioResourcesPathURL
					+ wordLangRef.audioResourceUrl;

			if (!imageUrlsToBeDownloaded.contains(audioResourceUrl)) {
				imageUrlsToBeDownloaded.add(audioResourceUrl);
			}
		}

		mDownloadProgressbar.setMax(imageUrlsToBeDownloaded.size()
		/*
		 * + mUpdateResponse.languages.size() mUpdateResponse.words.size()
		 */);

		String fileName = imageUrlsToBeDownloaded.get(0);
		Log.v(TAG,
				"---> hashedName: " + StorageHelper.getHashedFileName(fileName)
						+ " || url" + fileName);

		for (String imageUrl : imageUrlsToBeDownloaded) {

			if (!TextUtils.isEmpty(imageUrl)) {

				mImageLoader.cacheFile(imageUrl, new ServerResponseListener() {

					@Override
					public void preExecuteAction() {
						mStartedLoadingImagesCount++;
					}

					@Override
					public void postAction(boolean isSuccessful, Object result) {

						if (isSuccessful) {
							mDownloadedImagesCount++;

							updateProgress(mDownloadedImagesCount);
						} else {
							mFailedImagesCount++;
						}

						checkIfDownloadingCompleted();
					}
				});
			} else {
				mFailedImagesCount++;
				Log.v(TAG, "empty: " + imageUrl);
			}
		}
	}

	protected void continueToApp(String message) {

		// if (!mIsFinished) {

		mIsFinished = true;

		mActionLabel.setText(message);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				startActivity(new Intent(getApplicationContext(),
						CategoryChooseActivity.class));

				finish();
			}
		}, DELAY_TO_START_NEXT_ACTIVITY);
		// }
	}

	private void checkIfDownloadingCompleted() {
		/*
		 * Log.v(TAG, "mFailedImagesCount: " + mFailedImagesCount); Log.v(TAG,
		 * "mDownloadedImagesCount: " + mDownloadedImagesCount); Log.v(TAG,
		 * "mCanceledImagesCount: " + mCanceledImagesCount); Log.v(TAG,
		 * "mStartedLoadingImagesCount: " + mStartedLoadingImagesCount);
		 * Log.v(TAG, "images count: " + mUpdateResponse.words.size());
		 */
		if (mDownloadedImagesCount + mFailedImagesCount == mDownloadProgressbar
				.getMax()) {
			if (mDownloadProgressbar.getMax() == mDownloadedImagesCount) {
				continueToApp(getString(R.string.update_completed));
			} else {
				continueToApp(getString(R.string.downloading_resources_faled));
			}
		}

	}
}
