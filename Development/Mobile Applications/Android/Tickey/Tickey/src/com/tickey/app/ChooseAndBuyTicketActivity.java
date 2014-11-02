package com.tickey.app;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bg.teracomm.iap.IAPInfo;
import bg.teracomm.iap.IAPInterface;
import bg.teracomm.iap.IAPService;

import com.tickey.app.utils.server.ServerActionListener;
import com.tickey.app.utils.server.ServerHandler;

public class ChooseAndBuyTicketActivity extends BaseActivity implements
		IAPInterface {

	private static final String TAG = ChooseAndBuyTicketActivity.class
			.getSimpleName();

	public final static String IAP_KEY = "test_iap_key";

	private IAPService iapService;

	public static String mTransactionId = null;

	private static final String HTTP_BODY_PARAM_KEY_USER_ID = "user_id";
	private static final String HTTP_BODY_PARAM_KEY_LINE_NAME = "line_name";

	private static final String HTTP_BODY_PARAM_KEY_TICKET_TYPE = "card_type";
	private TextView mTVName;
	private TextView mTVTicketsCount;

	private int mUserId;

	protected boolean mIsChecking = false;

	private TextView mTVSuccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_choose_and_buy);

		// create an IAPService instance
		iapService = new IAPService(this, IAP_KEY, "TICKEY");

		mUserId = getIntent().getIntExtra(HTTP_BODY_PARAM_KEY_USER_ID, 0);

		mTVName = (TextView) findViewById(R.id.tvName);
		mTVTicketsCount = (TextView) findViewById(R.id.tvTicketsCount);
		mTVSuccess = (TextView) findViewById(R.id.tvSuccess);

		((ImageView) findViewById(R.id.ivLogout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								LoginActivity.class);

						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						startActivity(intent);
					}
				});

		Typeface norwester = Typeface.createFromAsset(getAssets(),
				"norwester.ttf");

		Typeface halvetica = Typeface.createFromAsset(getAssets(),
				"HelveticaNeueDeskUI.ttc");

		mTVName.setTypeface(norwester);
		mTVSuccess.setTypeface(norwester);
		((TextView) findViewById(R.id.tv11)).setTypeface(norwester);
		((TextView) findViewById(R.id.tv21)).setTypeface(norwester);
		((TextView) findViewById(R.id.tv12)).setTypeface(norwester);
		((TextView) findViewById(R.id.tv22)).setTypeface(norwester);
		((TextView) findViewById(R.id.tv13)).setTypeface(norwester);
		((TextView) findViewById(R.id.tv23)).setTypeface(norwester);
		mTVTicketsCount.setTypeface(halvetica);

		mTVSuccess.setVisibility(View.GONE);

		((RelativeLayout) findViewById(R.id.rlSingle))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						purchace(1F, "Single Ticket");
					}
				});

		((RelativeLayout) findViewById(R.id.rlFullDay))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						purchace(4F, "1 Day Travel");
					}
				});

		((RelativeLayout) findViewById(R.id.rlAllWeek))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						purchace(8F, "1 Week Travel");
					}
				});

		mTVTicketsCount.setText(getString(R.string.tickets_count, 0));
		String userName = getIntent().getStringExtra(
				SearchTicketsActivity.USER_NAME);
		if (!TextUtils.isEmpty(userName)) {
			mTVName.setText(userName);
		}

	}

	@Override
	public void onTransactionResult(String transID, int transResult) {
		Log.i("onTransactionResult", "transID = " + transID + ", result = "
				+ transResult);
		if (transID.equals(mTransactionId)
				&& transResult == IAPInfo.TRANSACTION_RESULT_SUCCESS) {

			((LinearLayout) findViewById(R.id.llTicketTypes))
					.setVisibility(View.GONE);

			mTVSuccess.setVisibility(View.VISIBLE);

			mTVTicketsCount.setText(getString(R.string.tickets_count, 1));

			makeOrder();
		}
	}

	ServerActionListener mSaveActionListener = new ServerActionListener() {

		@Override
		public void preExecuteAction() {

		}

		@Override
		public void postAction(boolean isSuccessful, Object json) {
			if (isSuccessful) {
				if (json != null) {
					try {
						JSONObject jsonObject = (JSONObject) json;

						Log.v(TAG, "Response: " + jsonObject.toString());

						if (jsonObject.getBoolean("success")) {

							return;

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			mIsChecking = false;
		}
	};

	protected void makeOrder() {
		HashMap<String, String> params = new HashMap<String, String>();

		params.put(HTTP_BODY_PARAM_KEY_USER_ID, Integer.toString(mUserId));
		params.put(HTTP_BODY_PARAM_KEY_LINE_NAME, "StartUp Rocket");
		params.put(HTTP_BODY_PARAM_KEY_TICKET_TYPE, "single ticket");

		new ServerHandler(ServerHandler.METHOD_POST_KEY, params,
				getString(R.string.url_make_order), mSaveActionListener, true,
				getApplicationContext());
	}

	private ProgressDialog mLoading;

	protected void purchace(double price, String paymentProductName) {

		mLoading = ProgressDialog.show(this, "",
				getString(R.string.loading), true);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mLoading.dismiss();
				Toast.makeText(getApplicationContext(), "payment successfull",
						Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent(getApplicationContext(),
						SearchTicketsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);

				intent.putExtra("has_ticket", true);

				startActivity(intent);

			}
		}, 500);
		/*
		 * Transaction transaction = iapService.purchaseSinglePayment(
		 * paymentProductName, // -- the product name price, // -- the price of
		 * the product IAPInfo.CURRENCY_BGN -- the currency of the product,
		 * currently supported: CURRENCY_BGN CURRENCY_EUR CURRENCY_USD
		 * CURRENCY_MKD
		 * 
		 * ); if (transaction != null) { mTransactionId =
		 * transaction.getTransactionId(); } Log.i("onClick",
		 * "purchase requested, transaction id: " + mTransactionId);
		 */
	}
}