package com.tickey.app.activity;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tickey.app.R;
import com.tickey.app.R.id;
import com.tickey.app.R.layout;
import com.tickey.app.R.string;
import com.tickey.app.data.model.ServerResponse;
import com.tickey.app.data.model.User;
import com.tickey.app.network.helper.GsonRequest;

public class LoginActivity extends BaseActivity {

	private static final String TAG = LoginActivity.class.getSimpleName();

	private UiLifecycleHelper uiHelper;

	private static final String HTTP_BODY_PARAM_KEY_PASSWORD = "password";
	private static final String HTTP_BODY_PARAM_KEY_EMAIL = "email";
	public static final String HTTP_BODY_PARAM_KEY_USER_ID = "user_id";
	private EditText mETEmail;
	private EditText mETPassword;
	private Button mBTSignIn;
	private Button mBTSignUp;
	boolean mIsLoggedIn = false;

	private RequestQueue mRequestQqueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_login);

		mRequestQqueue = Volley.newRequestQueue(getApplicationContext());

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		mETEmail = (EditText) findViewById(R.id.etEmail);
		mETPassword = (EditText) findViewById(R.id.etPassword);
		mBTSignIn = (Button) findViewById(R.id.btSignIn);
		mBTSignUp = (Button) findViewById(R.id.btRegister);
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("user_birthday"));

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
		if (session != null) {
			onSessionStateChange(session, session.getState(), null);
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

			if (session != null) {
				String fbAccessToken = session.getAccessToken();
				if (!TextUtils.isEmpty(fbAccessToken)) {
					loggedIn(fbAccessToken, true);
				}
			}
			// loggedInWithFacebook(session);

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	private void loggedInWithFacebook(Session session) {
		Map<String, String> params = new HashMap<String, String>();

		params.put(User.KEY_FB_ACCESS_TOKEN, session.getAccessToken());

		Type loginResponseType = new TypeToken<ServerResponse<User>>() {
		}.getType();

		GsonRequest<ServerResponse<User>> loginRequest = new GsonRequest<ServerResponse<User>>(
				Method.POST, getString(R.string.url_facebook_login),
				loginResponseType, params, createMyReqSuccessListener(),
				createMyReqErrorListener());

		loginRequest.mContentType = "application/json";
		mRequestQqueue.add(loginRequest);
	}

	private Listener<ServerResponse<User>> createMyReqSuccessListener() {
		Listener<ServerResponse<User>> responseListener = new Listener<ServerResponse<User>>() {

			@Override
			public void onResponse(ServerResponse<User> response) {
				Gson gson = new Gson();
				Log.v(TAG,
						"response: " + gson.toJson(response.result, User.class));

			}
		};
		return responseListener;
	}

	private ErrorListener createMyReqErrorListener() {
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.v(TAG, "Error: " + error.getMessage());
			}
		};
		return errorListener;
	}

	private GraphUserCallback mFaceBookMeCallback = new GraphUserCallback() {

		boolean mIsLoggedIn = false;

		@Override
		public void onCompleted(GraphUser user, Response response) {
			if (user != null && !mIsLoggedIn) {
				mIsLoggedIn = true;

				loggedIn(user.getId(), true);
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
		} else {
			Toast.makeText(getApplicationContext(), "all fields are required",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void loggedIn(String userId, boolean isFacebook) {
		if (!mIsLoggedIn) {
			mIsLoggedIn = true;
			finish();

			Intent intent = new Intent(getApplicationContext(),
					FeedActivity.class);

			if (isFacebook) {
				intent.putExtra(User.KEY_AUTH_TOKEN, userId);
			} else {
				intent.putExtra(HTTP_BODY_PARAM_KEY_USER_ID, userId);
			}

			startActivity(intent);
		}
	}

	private OnClickListener mOnSignUpClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			startActivity(new Intent(getApplicationContext(),
					RegisterActivity.class));

		}
	};

}