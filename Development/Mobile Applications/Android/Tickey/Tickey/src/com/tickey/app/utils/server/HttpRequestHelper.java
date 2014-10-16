package com.tickey.app.utils.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tickey.app.LoginActivity;

/**
 * Makes http requests with both POST and GET methods and gives InputStream
 * result or null.
 * <p>
 * NULL is returned when request timeout is left or response exception occur
 */
public class HttpRequestHelper {

	private static final String TAG = HttpRequestHelper.class.getSimpleName();

	/** The connection timeout in milliseconds. */
	private final int mConnectionTimeout = 10000;

	/** The socket timeout in milliseconds. */
	private final int mSocketTimeout = 10000;

	/** The url. */
	private String mUrl;

	/** The request method type. */
	private String mRequestMethodType;

	/** The request parameters. */
	private List<NameValuePair> mRequestParams;

	private Context mContext;

	/**
	 * Instantiates a new http request.
	 * 
	 * @param url
	 *            the url
	 * @param requestmethod
	 *            the method
	 * @param requestParams
	 *            the params
	 */
	public HttpRequestHelper(String url, String requestMethod,
			List<NameValuePair> requestParams, Context ctx) {
		this.mRequestParams = requestParams;
		this.mUrl = url;
		this.mRequestMethodType = requestMethod;
		mContext = ctx;
	}

	/**
	 * Makes http request and handles its response
	 * 
	 * @param isResponseRequested
	 * 
	 * @return the http request result
	 */
	public InputStream getHttpRequestResult(boolean isResponseRequested) {
		InputStream inputStream = null;
		/** check for request method */
		if (mRequestMethodType == "POST") {
			try {
				// readUrl(mUrl);

				Log.v(TAG, mUrl);

				HttpPost request = new HttpPost(mUrl);
				HttpParams httpParameters = new BasicHttpParams();

				HttpEntity inputEntity = new UrlEncodedFormEntity(
						mRequestParams);

				Log.v(TAG, "Params:" + mRequestParams.toString());

				/** adds content type of the entity to the request */
				/** adds the eintities to the request */
				request.setEntity(inputEntity);

				HttpConnectionParams.setConnectionTimeout(httpParameters,
						mConnectionTimeout);
				/**
				 * Set the default socket timeout (SO_TIMEOUT) in milliseconds
				 * which is the timeout for waiting for data.
				 */
				HttpConnectionParams.setSoTimeout(httpParameters,
						mSocketTimeout);
				/** create object of DefaultHttpClient adds the parameters */

				DefaultHttpClient httpClient = new DefaultHttpClient(
						httpParameters);
				/** executes the request and gets the response */
				HttpResponse response = httpClient.execute(request);
				/** gets the response status line */
				StatusLine statusLine = response.getStatusLine();
				/** gets the status code from the statis line */
				int statusCode = statusLine.getStatusCode();
				/**
				 * checks for status code.. status code needs to be 200 or
				 * HttpStatus.SC_OK
				 */
				if (statusCode == HttpStatus.SC_OK) {
					if (isResponseRequested) {
						/** get response entities */
						HttpEntity entity = response.getEntity();
						/** get inputstream of entities */
						inputStream = entity.getContent();
					} else {
						inputStream = new InputStream() {

							@Override
							public int read() throws IOException {
								return 0;
							}
						};
					}
				} else {
					Log.v("response", response.getEntity().getContent()
							.toString());
					Log.v("Status line", Integer.toString(statusCode) + ": "
							+ statusLine.getReasonPhrase());

					if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
						Intent i = new Intent(mContext, LoginActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(i);

					}

					return null;
				}
				/** convert entity response to string */
			} catch (SocketException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} else if (mRequestMethodType == "GET") {
			try {

				HttpGet request;
				HttpParams httpParameters = new BasicHttpParams();

				request = new HttpGet(mUrl);
				/** adds json application content type */
				request.addHeader("Content-Type", "application/json");
				/** add params to the entity object */

				if (mRequestParams != null) {
					Log.v(TAG, "zaza");
					HttpEntity inputEntity = new UrlEncodedFormEntity(
							mRequestParams);

					// ** adds content type of the entity to the request *//*
					request.addHeader(inputEntity.getContentType());
				}

				Log.v(TAG, mUrl);

				/**
				 * Set the timeout in milliseconds until a connection is
				 * established. The default value is zero, that means the
				 * timeout is not used.
				 */

				HttpConnectionParams.setConnectionTimeout(httpParameters,
						mConnectionTimeout);
				/**
				 * Set the default socket timeout (SO_TIMEOUT) in milliseconds
				 * which is the timeout for waiting for data.
				 */

				HttpConnectionParams.setSoTimeout(httpParameters,
						mSocketTimeout);

				/** create object of DefaultHttpClient adds the parameters */
				DefaultHttpClient httpClient = new DefaultHttpClient(
						httpParameters);
				/** adds json application content type */
				// request.addHeader("Content-Type", "application/json");

				/** executes the request and gets the response */
				HttpResponse response = httpClient.execute(request);
				/** gets the response status line */
				StatusLine statusLine = response.getStatusLine();
				/** gets the status code from the statis line */
				int statusCode = statusLine.getStatusCode();
				/**
				 * checks for status code.. status code needs to be 200 or
				 * HttpStatus.SC_OK
				 */
				if (statusCode == HttpStatus.SC_OK) {
					if (isResponseRequested) {
						/** get response entities */
						HttpEntity entity = response.getEntity();
						/** get inputstream of entities */
						inputStream = entity.getContent();
					} else {
						inputStream = new InputStream() {

							@Override
							public int read() throws IOException {
								return 0;
							}
						};
					}
				} else {
					Log.v("response", response.getEntity().getContent()
							.toString());
					Log.v("Status line", Integer.toString(statusCode) + ": "
							+ statusLine.getReasonPhrase());

					if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
						Intent i = new Intent(mContext, LoginActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// i.putExtra(LoginActivity.UNAUTHORIZED, true);
						mContext.startActivity(i);
					}

					return null;
				}
			} catch (SocketException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return inputStream;
	}
}