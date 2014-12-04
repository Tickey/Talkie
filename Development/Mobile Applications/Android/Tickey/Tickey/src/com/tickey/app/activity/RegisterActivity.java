package com.tickey.app.activity;

import java.util.Calendar;

import com.tickey.app.R;
import com.tickey.app.R.drawable;
import com.tickey.app.R.id;
import com.tickey.app.R.layout;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


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
		private static final String HTTP_BODY_PARAM_LAST_NAME = "first_name";
		private static final String HTTP_BODY_PARAM_FIRST_NAME = "last_name";
		private static final String HTTP_BODY_PARAM_BIRTHDAY = "birthday";
		private EditText mETEmail;
		private EditText mETPassword;
		private EditText mETFirstName;
		private EditText mETLastName;
		private TextView mDateTextView;
		private EditText mETConfirmPassword;
		private Button mBTSignUp;
		private Toolbar mToolbar;

		private int year;
		private int month;
		private int day;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);

			mETEmail = (EditText) rootView.findViewById(R.id.etEmail);
			mETPassword = (EditText) rootView.findViewById(R.id.etPassword);
			mDateTextView = (TextView) rootView
					.findViewById(R.id.et_birth_date);
			mETConfirmPassword = (EditText) rootView
					.findViewById(R.id.etConfirmPassword);
			mETFirstName = (EditText) rootView.findViewById(R.id.etFirstName);
			mETLastName = (EditText) rootView.findViewById(R.id.etLastName);
			mBTSignUp = (Button) rootView.findViewById(R.id.btRegister);

			mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			ActionBarActivity activity = (ActionBarActivity) getActivity();
			activity.setSupportActionBar(mToolbar);

			mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			mToolbar.setNavigationOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			});

			Typeface halvetica = Typeface.createFromAsset(getActivity()
					.getAssets(), "HelveticaNeueDeskUI.ttc");
			mETEmail.setTypeface(halvetica);
			mETPassword.setTypeface(halvetica);
			mETConfirmPassword.setTypeface(halvetica);
			mDateTextView.setTypeface(halvetica);
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
			setCurrentDateOnView();
		}

		// display current date
		public void setCurrentDateOnView() {

			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			// set current date into textview
			mDateTextView.setText(new StringBuilder()
					// Month is 0 based, just add 1
					.append(month + 1).append("-").append(day).append("-")
					.append(year).append(" "));

			// set current date into datepicker
			mDateTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new DatePickerDialog(getActivity(), datePickerListener,
							year, month, day).show();
				}
			});

		}

		private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

			// when dialog box is closed, below method will be called.
			public void onDateSet(DatePicker view, int selectedYear,
					int selectedMonth, int selectedDay) {
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;

				// set selected date into textview
				mDateTextView.setText(new StringBuilder().append(month + 1)
						.append("-").append(day).append("-").append(year)
						.append(" "));

				// set selected date into datepicker also

			}
		};

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
			String pin = mDateTextView.getText().toString();

			if (!password.isEmpty() && !email.isEmpty()
					&& !confirmPassword.isEmpty() && !firstName.isEmpty()
					&& !lastName.isEmpty() && !pin.isEmpty()) {

				if (password.equals(confirmPassword)) {

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

	}
}