package com.tickey.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;
import com.tickey.app.utils.server.ServerActionListener;

public class SearchTicketsActivity extends BaseActivity implements
		IBeaconConsumer {

	private static final String TAG = SearchTicketsActivity.class
			.getSimpleName();

	private IBeaconManager iBeaconManager = IBeaconManager
			.getInstanceForApplication(this);
	protected Region mMonitoringRegion = new Region("myRangingUniqueId",
			null/* "10D39AE7-020E-4467-9CB2-DD36366F899D" */, null, null);

	private static final String HTTP_BODY_PARAM_KEY_UUID = "uuid";
	private static final String HTTP_BODY_PARAM_KEY_USER_ID = "user_id";
	private static final String HTTP_BODY_PARAM_KEY_LINE_NAME = "line_name";

	private static final String URL_ACTIVE_CARDS = "http://tickey.herokuapp.com/card_purches/user_active_cards/%d/%s.json";

	public static final String USER_NAME = "user_name";
	private TextView mTVName;
	private TextView mTVTicketsCount;
	private TextView mTVScanner;
	private LinearLayout mLLBuy;

	private int mUserId;

	protected boolean mIsChecking = false;

	private Button mBTBuy;

	private TextView mTVStatus;

	private int mTiketsCount = 0;

	private ImageView mIVUserAvatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_search_tickets);

		mUserId = getIntent().getIntExtra(HTTP_BODY_PARAM_KEY_USER_ID,
				mTiketsCount);

		if (!getIntent().getBooleanExtra("has_ticket", false)) {
			iBeaconManager.bind(this);
		}

		mTVName = (TextView) findViewById(R.id.tvName);
		mTVTicketsCount = (TextView) findViewById(R.id.tvTicketsCount);
		mTVScanner = (TextView) findViewById(R.id.tvScanner);
		mBTBuy = (Button) findViewById(R.id.btBuy);
		mTVStatus = (TextView) findViewById(R.id.tvYouAreInBus);
		mLLBuy = (LinearLayout) findViewById(R.id.llBuy);
		mIVUserAvatar = (ImageView) findViewById(R.id.imageView1);

		mLLBuy.setVisibility(View.GONE);

		((ImageView) findViewById(R.id.ivLogout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

						Session session = Session.getActiveSession();

						if (session != null) {
							session.closeAndClearTokenInformation();
						}

						Intent intent = new Intent(getApplicationContext(),
								LoginActivity.class);

						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						startActivity(intent);
					}
				});
		mBTBuy.setOnClickListener(mBuyClickListener);

		Typeface norwester = Typeface.createFromAsset(getAssets(),
				"norwester.ttf");

		Typeface halvetica = Typeface.createFromAsset(getAssets(),
				"HelveticaNeueDeskUI.ttc");
		/*
		 * mBTSignIn.setOnClickListener(mOnSignInClickListener);
		 * mBTSignUp.setOnClickListener(mOnSignUpClickListener);
		 * 
		 * mETEmail.setTypeface(halvetica); mETPassword.setTypeface(halvetica);
		 * mBTSignIn.setTypeface(halvetica); mBTSignUp.setTypeface(halvetica);
		 */
		mTVName.setTypeface(norwester);
		mTVScanner.setTypeface(norwester);
		mTVTicketsCount.setTypeface(halvetica);
		mTVStatus.setTypeface(norwester);

		mTVTicketsCount.setText(getString(R.string.tickets_count, 0));

		mTVScanner.setText(Html.fromHtml(getString(R.string.scanning_lines)));

		Session fbSession = Session.getActiveSession();

		Request.newMeRequest(fbSession, new GraphUserCallback() {

			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					mTVName.setText(user.getName());

					new AsyncTask<String, Void, Bitmap>() {

						@Override
						protected Bitmap doInBackground(String... params) {
							String userId = params[0];
							return getFacebookProfilePicture(userId);
						}
						
						protected void onPostExecute(Bitmap userAvatar) {
							if(userAvatar != null) {
								mIVUserAvatar.setImageBitmap(userAvatar);
							}
						};
					}.execute(user.getId());
				}
			}
		}).executeAsync();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		iBeaconManager.unBind(this);
	}

	public static Bitmap getFacebookProfilePicture(String userID) {
		Bitmap bitmap;
		try {
			URL imageURL = new URL("https://graph.facebook.com/" + userID
					+ "/picture?type=large");
			bitmap = BitmapFactory.decodeStream(imageURL.openConnection()
					.getInputStream());
		} catch (MalformedURLException e) {
			bitmap = null;
			e.printStackTrace();
		} catch (IOException e) {
			bitmap = null;
			e.printStackTrace();
		}

		return bitmap;
	}

	private OnClickListener mBuyClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent intent = new Intent(getApplicationContext(),
					ChooseAndBuyTicketActivity.class);

			intent.putExtra(HTTP_BODY_PARAM_KEY_USER_ID, mUserId);

			intent.putExtra(USER_NAME, mTVName.getText());

			startActivity(intent);
		}
	};

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (intent.getBooleanExtra("has_ticket", false)) {
			mTiketsCount++;

			mTVTicketsCount.setText(getString(R.string.tickets_count,
					mTiketsCount));

			mBTBuy.setText("check in");
			mBTBuy.setOnClickListener(new OnClickListener() {

				private ProgressDialog mLoading;

				@Override
				public void onClick(View v) {
					if (mTiketsCount > 0) {
						mLoading = ProgressDialog.show(
								SearchTicketsActivity.this, "",
								getString(R.string.loading), true);

						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								mLoading.dismiss();
								mTVStatus
										.setText(getString(R.string.checked_in));

								mTiketsCount--;

								mTVTicketsCount.setText(getString(
										R.string.tickets_count, mTiketsCount));

								mBTBuy.setText("New bus?");

								mBTBuy.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										mBTBuy.setText("buy");
										mBTBuy.setOnClickListener(mBuyClickListener);

										mTVScanner.setVisibility(View.VISIBLE);

										mLLBuy.setVisibility(View.GONE);
										mTVStatus
												.setText(getString(R.string.you_are_in_bus));
										iBeaconManager
												.bind(SearchTicketsActivity.this);

										mTVScanner.setText(Html
												.fromHtml(getString(R.string.scanning_lines)));
									}
								});
							}
						}, 500);
					} else {
						mBTBuy.setText("buy");
						mBTBuy.setOnClickListener(mBuyClickListener);
						Toast.makeText(getApplicationContext(), "no tickets",
								Toast.LENGTH_SHORT).show();
					}
				}
			});

			mTVScanner.setVisibility(View.GONE);

			mLLBuy.setVisibility(View.VISIBLE);
		}
	};

	ServerActionListener mSaveActionListener = new ServerActionListener() {

		private ProgressDialog mLoading;

		@Override
		public void preExecuteAction() {

			mIsChecking = true;
			/*
			 * mLoading = ProgressDialog.show(getApplicationContext(), "",
			 * getString(R.string.loading), true);
			 */

		}

		@Override
		public void postAction(boolean isSuccessful, Object json) {
			/*
			 * mLoading.dismiss();
			 */
			if (isSuccessful) {
				if (json != null) {
					try {
						JSONArray jsonArray = (JSONArray) json;

						if (jsonArray.length() > 0) {

							Log.v(TAG,
									"line name: "
											+ jsonArray
													.getJSONObject(0)
													.getString(
															HTTP_BODY_PARAM_KEY_LINE_NAME));

							ticketAilable(jsonArray.getJSONObject(0).getString(
									HTTP_BODY_PARAM_KEY_LINE_NAME));

							mTVScanner.setText("you have a ticket");

							mTVTicketsCount.setText(getString(
									R.string.tickets_count, 1));

						} else {
							noTicket();
						}

						return;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			mIsChecking = false;
		}
	};

	protected void checkTickets(String uuid) {

		if (!mIsChecking) {

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					noTicket();
				}
			}, 1000);

			/*
			 * HashMap<String, String> params = new HashMap<String, String>();
			 * 
			 * String url = String.format(URL_ACTIVE_CARDS, mUserId, uuid);
			 * 
			 * new ServerHandler(ServerHandler.METHOD_GET_KEY, params, url,
			 * mSaveActionListener, true, getApplicationContext());
			 */
		}
	}

	protected void ticketAilable(String string) {
		iBeaconManager.unBind(this);

		// TODO start ticket available activity
	}

	protected void noTicket() {
		iBeaconManager.unBind(this);

		mTVScanner.setVisibility(View.GONE);

		mLLBuy.setVisibility(View.VISIBLE);
	}

	@Override
	public void onIBeaconServiceConnect() {

		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(
					final Collection<IBeacon> iBeacons, Region region) {
				if (iBeacons.size() > 0) {

					runOnUiThread(new Runnable() {

						public void run() {
							mTVScanner.setText(Html
									.fromHtml(getString(R.string.check_for_tickets)));

							Log.v(TAG, "UUID: "
									+ iBeacons.iterator().next()
											.getProximityUuid());

							checkTickets(iBeacons.iterator().next()
									.getProximityUuid());

						}
					});
				}
			}
		});

		try {
			iBeaconManager.startRangingBeaconsInRegion(mMonitoringRegion);
		} catch (RemoteException e) {
		}

		iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
			@Override
			public void didEnterRegion(final Region region) {
				runOnUiThread(new Runnable() {
					public void run() {
						region.getProximityUuid();
					}
				});

				try {
					iBeaconManager
							.stopMonitoringBeaconsInRegion(mMonitoringRegion);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void didExitRegion(Region region) {
				Log.i(TAG, "didExitRegion");
			}

			@Override
			public void didDetermineStateForRegion(int state, Region region) {
			}
		});

		try {
			iBeaconManager.startMonitoringBeaconsInRegion(mMonitoringRegion);
		} catch (RemoteException e) {

		}
	}

}