package co.talkie_kids.talkie.activities;

import java.util.Map;

import net.servoper.android.factory.LayoutInflateFactory;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.mediaplayer.MediaPlayerOptimizer;
import co.talkie_kids.talkie.resources.BasicConstants;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class BaseTalkieActivity extends Activity {
	protected LayoutInflateFactory mFactory;

	private static String gaPropertyId;
	private static final String SCREEN_LABEL = "TalkieActivity";

	// This examples assumes the use of Google Analytics campaign
	// "utm" parameters, like "utm_source"
	private static final String CAMPAIGN_SOURCE_PARAM = "utm_source";

	Tracker mTracker;

	public static MediaPlayer mMediaPlayer = null;

	@Override
	public void onCreate(Bundle savedInstance) {
		gaPropertyId = getResources().getString(R.string.ga_trackingId);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			setRequestedOrientation(BasicConstants.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		mFactory = new LayoutInflateFactory(this, 320, 320);
		super.onCreate(savedInstance);
		getLayoutInflater().setFactory(mFactory);

		mTracker = GoogleAnalytics.getInstance(this).getTracker(gaPropertyId);

		if (BaseTalkieActivity.mMediaPlayer == null) {
			InitializeMediaPlayer();
			BaseTalkieActivity.mMediaPlayer.start();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		// Set screen name on tracker so that all subsequent hits will use this
		// value.
		mTracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);

		// Get the intent that started this Activity.
		Intent intent = this.getIntent();
		Uri uri = intent.getData();

		// Send a screenview using any available campaign or referrer data.
		MapBuilder.createAppView().setAll(getReferrerMapFromUri(uri));
	}

	/*
	 * Given a URI, returns a map of campaign data that can be sent with any GA
	 * hit.
	 * 
	 * @param uri A hierarchical URI that may or may not have campaign data
	 * stored in query parameters.
	 * 
	 * @return A map that may contain campaign or referrer that may be sent with
	 * any Google Analytics hit.
	 */
	Map<String, String> getReferrerMapFromUri(Uri uri) {

		MapBuilder paramMap = new MapBuilder();

		// If no URI, return an empty Map.
		if (uri == null) {
			return paramMap.build();
		}

		// Source is the only required campaign field. No need to continue if
		// not
		// present.
		if (uri.getQueryParameter(CAMPAIGN_SOURCE_PARAM) != null) {

			// MapBuilder.setCampaignParamsFromUrl parses Google Analytics
			// campaign
			// ("UTM") parameters from a string URL into a Map that can be set
			// on
			// the Tracker.
			paramMap.setCampaignParamsFromUrl(uri.toString());

			// If no source parameter, set authority to source and medium to
			// "referral".
		} else if (uri.getAuthority() != null) {
			paramMap.set(Fields.CAMPAIGN_MEDIUM, "referral");
			paramMap.set(Fields.CAMPAIGN_SOURCE, uri.getAuthority());

		}

		return paramMap.build();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (BaseTalkieActivity.mMediaPlayer != null)
			if (BaseTalkieActivity.mMediaPlayer.isPlaying())
				BaseTalkieActivity.mMediaPlayer.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (BaseTalkieActivity.mMediaPlayer != null) {
			if (!BaseTalkieActivity.mMediaPlayer.isPlaying())
				BaseTalkieActivity.mMediaPlayer.start();
		} else {
			InitializeMediaPlayer();
			BaseTalkieActivity.mMediaPlayer.start();
		}
	}

	protected void InitializeMediaPlayer() {
		// http://dig.ccmixter.org/music_for_games?dig-lic=safe&dig-query=children#
		BaseTalkieActivity.mMediaPlayer = MediaPlayerOptimizer.create(
				BaseTalkieActivity.this, R.raw.gurdonark_innocence, false);
		BaseTalkieActivity.mMediaPlayer.setLooping(true);
		float volume = (float) (1 - (Math.log(100 - 20) / Math.log(100)));
		BaseTalkieActivity.mMediaPlayer.setVolume(volume, volume);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}
}