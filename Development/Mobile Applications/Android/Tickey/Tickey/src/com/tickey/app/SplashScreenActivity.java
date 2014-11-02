package com.tickey.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class SplashScreenActivity extends BaseActivity {
	// used to know if the back button was pressed in the splash screen activity
	// and avoid opening the next activity
	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 2000; // 2 seconds
	private Intent mNextIntent;
	private long mSplashScreenStartedAt;
	private UiLifecycleHelper uiHelper;
	private boolean mIsStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSplashScreenStartedAt = System.currentTimeMillis();

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splashscreen);

		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			Request.newMeRequest(session, mFaceBookMeCallback).executeAsync();
		} else {
			notLoggedIn();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private GraphUserCallback mFaceBookMeCallback = new GraphUserCallback() {

		@Override
		public void onCompleted(GraphUser user, Response response) {
			if (user != null) {
				loggedIn(user.getId());
			} else {
				notLoggedIn();
			}
		}
	};

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (session.isOpened()) {
			Request.newMeRequest(session, mFaceBookMeCallback).executeAsync();
		} else {
			notLoggedIn();
		}
	}

	private void loggedIn(String userId) {

		mNextIntent = new Intent(getApplicationContext(),
				SearchTicketsActivity.class);

		mNextIntent.putExtra(LoginActivity.HTTP_BODY_PARAM_KEY_USER_ID, userId);
		splashScreen();
	}

	private void notLoggedIn() {

		mNextIntent = new Intent(getApplicationContext(), LoginActivity.class);

		splashScreen();

	}

	private void splashScreen() {

		long splashScreenFinishedAt = System.currentTimeMillis()
				- mSplashScreenStartedAt;

		if (splashScreenFinishedAt < SPLASH_DURATION) {

			long timeToEnd = SPLASH_DURATION - splashScreenFinishedAt;

			Handler handler = new Handler();
			// run a thread after 2 seconds to start the LevelChooseActivity
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {

					// make sure we close the splash screen so the user won't
					// come
					// back when it presses back key

					finish();

					if (!mIsBackButtonPressed && !mIsStarted) {
						finish();
						mIsStarted = true;
						startActivity(mNextIntent);
					}

				}

			}, timeToEnd); // time in milliseconds (1 second = 1000
							// milliseconds) until the run() method will
							// be
							// called
		} else {
			finish();
			startActivity(mNextIntent);
		}
	}

	@Override
	public void onBackPressed() {

		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}

}
