package com.tickey.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.model.GraphUser;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.tickey.app.utils.format.StringFormat;
import com.tickey.app.utils.server.ServerActionListener;
import com.tickey.app.utils.server.ServerHandler;

public class LoginActivity extends BaseActivity {

	private static final String TAG = LoginActivity.class.getSimpleName();

	private UiLifecycleHelper uiHelper;

	private static final String HTTP_BODY_PARAM_KEY_PASSWORD = "password";
	private static final String HTTP_BODY_PARAM_KEY_EMAIL = "email";
	public static final String HTTP_BODY_PARAM_KEY_USER_ID = "user_id";
	EditText mETEmail;
	EditText mETPassword;
	Button mBTSignIn;
	Button mBTSignUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_login);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		mETEmail = (EditText) findViewById(R.id.etEmail);
		mETPassword = (EditText) findViewById(R.id.etPassword);
		mBTSignIn = (Button) findViewById(R.id.btSignIn);
		mBTSignUp = (Button) findViewById(R.id.btRegister);

		Typeface halvetica = Typeface.createFromAsset(this.getAssets(),
				"HelveticaNeueDeskUI.ttc");

		mBTSignIn.setOnClickListener(mOnSignInClickListener);
		mBTSignUp.setOnClickListener(mOnSignUpClickListener);

		mETEmail.setTypeface(halvetica);
		mETPassword.setTypeface(halvetica);
		mBTSignIn.setTypeface(halvetica);
		mBTSignUp.setTypeface(halvetica);

		// mETPassword

		mETPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					login();
				}
				return false;
			}
		});

		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			Request.newMeRequest(session, mFaceBookMeCallback).executeAsync();
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");

			Request.newMeRequest(session, mFaceBookMeCallback).executeAsync();
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	private GraphUserCallback mFaceBookMeCallback = new GraphUserCallback() {

		boolean mIsLoggedIn = false;

		@Override
		public void onCompleted(GraphUser user, Response response) {
			if (user != null && !mIsLoggedIn) {
				mIsLoggedIn = true;
				loggedIn(user.getId());
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

	private OnClickListener mOnSignInClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			login();
		}
	};

	protected void login() {
		String email = mETEmail.getText().toString();
		String password = mETPassword.getText().toString();

		if (!password.isEmpty() && !email.isEmpty()) {
			try {
				HashMap<String, String> params = new HashMap<String, String>();

				params.put(HTTP_BODY_PARAM_KEY_EMAIL, email);
				params.put(HTTP_BODY_PARAM_KEY_PASSWORD,
						StringFormat.SHA1(password));

				new ServerHandler(ServerHandler.METHOD_POST_KEY, params,
						getResources().getString(R.string.url_authenticate),
						mSaveActionListener, true, getApplicationContext());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(), "all fields are required",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void loggedIn(String userId) {
		finish();

		Intent intent = new Intent(getApplicationContext(),
				SearchTicketsActivity.class);

		intent.putExtra(HTTP_BODY_PARAM_KEY_USER_ID, userId);

		startActivity(intent);
	}

	ServerActionListener mSaveActionListener = new ServerActionListener() {

		private ProgressDialog mLoading;

		@Override
		public void preExecuteAction() {
			mLoading = ProgressDialog.show(LoginActivity.this, "",
					getString(R.string.loading), true);

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mETPassword.getWindowToken(), 0);

		}

		@Override
		public void postAction(boolean isSuccessful, Object json) {

			mLoading.dismiss();

			if (isSuccessful) {
				if (json != null) {
					JSONObject jsonObject = (JSONObject) json;
					Log.v(TAG, "JSON: " + jsonObject.toString());
					try {
						if (jsonObject.getBoolean("do_exist")) {

							int userId = jsonObject
									.getInt(HTTP_BODY_PARAM_KEY_USER_ID);
							loggedIn(String.valueOf(userId));

							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			Toast.makeText(getApplicationContext(), "authentication failed",
					Toast.LENGTH_SHORT).show();
		}
	};

	private OnClickListener mOnSignUpClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			startActivity(new Intent(getApplicationContext(),
					RegisterActivity.class));

		}
	};

}