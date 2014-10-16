package com.tickey.app.utils.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.tickey.app.parcelable.InputStreamToJSON;

public class ServerHandler extends Activity {

	public static final String METHOD_POST_KEY = "POST";
	public static final String METHOD_GET_KEY = "GET";
	private String mHttpMethod;
	/** The url. */
	private String mUrl;

	/** The http request params. */
	private HashMap<String, String> mHttpRequestParams;

	private ServerActionListener mServerActionListener;
	private boolean mIsResponseRequested = false;
	public Context mContext;

	public ServerHandler(String httpMethod, HashMap<String, String> httpParams,
			String url, ServerActionListener serverActionListener,
			boolean isResponseRequested, Context context) {
		mServerActionListener = serverActionListener;
		this.mHttpRequestParams = httpParams;
		this.mUrl = url;
		this.mHttpMethod = httpMethod;
		this.mIsResponseRequested = isResponseRequested;
		this.mContext = context;
		new ConnectionTask().execute();
	}

	/**
	 * The Class ConnectionTask.
	 */
	class ConnectionTask extends AsyncTask<String, String, Object> {

		/** is there response. */
		private boolean mIsRequestOk;

		/**
		 * Instantiates a new connection task.
		 */
		public ConnectionTask() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/** set to false for beginning */
			mIsRequestOk = false;

			if (mServerActionListener != null)
				mServerActionListener.preExecuteAction();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(String... args) {
			HttpRequestHelper httpRequester;

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			/** if there are parameters add them to the list of paramseters */
			if (mHttpRequestParams != null) {
				for (Entry<String, String> entry : mHttpRequestParams
						.entrySet())
					params.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
			}

			httpRequester = new HttpRequestHelper(mUrl, mHttpMethod, params,
					mContext);

			Object json;

			if (mIsResponseRequested) {
				/** run the request */
				InputStream is = httpRequester
						.getHttpRequestResult(mIsResponseRequested);
				if (is != null) {
					InputStreamToJSON isToJSON = new InputStreamToJSON(is);

					Boolean isJSONObject = isToJSON.isJSONObject();

					if (isJSONObject != null) {
						if (isJSONObject == true) {
							json = new JSONObject();
							json = isToJSON.getJSONObjectFromInputStream();
						} else {
							json = new JSONArray();
							json = isToJSON.getJSONArrayFromInputStream();
						}
					} else {
						json = null;
					}

					if (json != null) {
						mIsRequestOk = true;
					} else {
						mIsRequestOk = false;
					}
				} else {
					mIsRequestOk = false;
					json = null;
				}
			} else {
				if (httpRequester.getHttpRequestResult(mIsResponseRequested) == null) {
					mIsRequestOk = false;
				} else {
					mIsRequestOk = true;
				}

				json = null;
			}

			return json;
		}

		/**
		 * After completing background task Dismiss the progress dialog *.
		 * 
		 * @param file_url
		 *            the file_url
		 */
		protected void onPostExecute(final Object json) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (mServerActionListener != null)
						mServerActionListener.postAction(mIsRequestOk, json);
				}
			});
		}
	}
}