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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.tickey.app.utils.format.StringFormat;
import com.tickey.app.utils.server.ServerActionListener;
import com.tickey.app.utils.server.ServerHandler;

public class RegisterActivity extends BaseActivity {

	private static final String TAG = LoginActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private static final String HTTP_BODY_PARAM_KEY_USER_ID = "user_id";
		private static final String HTTP_BODY_PARAM_KEY_PASSWORD = "password";
		private static final String HTTP_BODY_PARAM_KEY_EMAIL = "email";
		private static final String HTTP_BODY_PARAM_KEY_PIN = "first_name";
		private static final String HTTP_BODY_PARAM_FIRST_NAME = "last_name";
		private static final String HTTP_BODY_PARAM_LAST_NAME = "pin";
		EditText mETEmail;
		EditText mETPassword;
		EditText mETFirstName;
		EditText mETLastName;
		EditText mETPIN;
		EditText mETConfirmPassword;
		Button mBTSignUp;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);

			mETEmail = (EditText) rootView.findViewById(R.id.etEmail);
			mETPassword = (EditText) rootView.findViewById(R.id.etPassword);
			mETPIN = (EditText) rootView.findViewById(R.id.etPIN);
			mETConfirmPassword = (EditText) rootView
					.findViewById(R.id.etConfirmPassword);
			mETFirstName = (EditText) rootView.findViewById(R.id.etFirstName);
			mETLastName = (EditText) rootView.findViewById(R.id.etLastName);
			mBTSignUp = (Button) rootView.findViewById(R.id.btRegister);

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			Typeface halvetica = Typeface.createFromAsset(getActivity()
					.getAssets(), "HelveticaNeueDeskUI.ttc");
			mETEmail.setTypeface(halvetica);
			mETPassword.setTypeface(halvetica);
			mETConfirmPassword.setTypeface(halvetica);
			mETPIN.setTypeface(halvetica);
			mETFirstName.setTypeface(halvetica);
			mETLastName.setTypeface(halvetica);
			mBTSignUp.setTypeface(halvetica);

			mETConfirmPassword
					.setOnEditorActionListener(new OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId,
								KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE) {
								register();
							}
							return false;
						}
					});

			mBTSignUp.setOnClickListener(mOnRegisterClickListener);
		}

		private OnClickListener mOnRegisterClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				register();
			}
		};

		protected void register() {
			String email = mETEmail.getText().toString();
			String password = mETPassword.getText().toString();
			String confirmPassword = mETConfirmPassword.getText().toString();
			String firstName = mETFirstName.getText().toString();
			String lastName = mETLastName.getText().toString();
			String pin = mETPIN.getText().toString();

			if (!password.isEmpty() && !email.isEmpty()
					&& !confirmPassword.isEmpty() && !firstName.isEmpty()
					&& !lastName.isEmpty() && !pin.isEmpty()) {

				if (password.equals(confirmPassword)) {

					try {
						HashMap<String, String> params = new HashMap<String, String>();

						params.put(HTTP_BODY_PARAM_KEY_EMAIL, email);
						params.put(HTTP_BODY_PARAM_KEY_PASSWORD,
								StringFormat.SHA1(password));
						params.put(HTTP_BODY_PARAM_KEY_PIN, pin);
						params.put(HTTP_BODY_PARAM_FIRST_NAME, firstName);
						params.put(HTTP_BODY_PARAM_LAST_NAME, lastName);

						new ServerHandler(ServerHandler.METHOD_POST_KEY,
								params, getResources().getString(
										R.string.url_register),
								mSaveActionListener, true, getActivity()
										.getApplicationContext());
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"passwords should match", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"all fields are required", Toast.LENGTH_SHORT).show();
			}
		}

		ServerActionListener mSaveActionListener = new ServerActionListener() {

			private ProgressDialog mLoading;

			@Override
			public void preExecuteAction() {
				mLoading = ProgressDialog.show(getActivity(), "",
						getString(R.string.loading), true);

				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
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
							int userId = jsonObject
									.getInt(HTTP_BODY_PARAM_KEY_USER_ID);

							if (userId != 0) {
								getActivity().finish();

								Intent intent = new Intent(getActivity()
										.getApplicationContext(),
										SearchTicketsActivity.class);

								intent.putExtra(HTTP_BODY_PARAM_KEY_USER_ID,
										userId);

								getActivity().startActivity(intent);

								return;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				Toast.makeText(getActivity().getApplicationContext(),
						"registration failed", Toast.LENGTH_SHORT).show();
			}
		};
	}

}