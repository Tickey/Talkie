package com.tickey.app.network.helper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonRequest<T> extends Request<T> {

	private static final String TAG = GsonRequest.class.getSimpleName();

	private final Gson mGson = new Gson();
	private final Type mType;
	private Map<String, String> mHeaders;
	private Map<String, String> mMapParams;
	private final Listener<T> mListener;
	public String mContentType;

	/**
	 * Make a GET request and return a parsed object from JSON.
	 * 
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param params
	 *            Map of request body parameters
	 * @param headers
	 *            Map of request headers
	 */
	public GsonRequest(int method, String url, Type type,
			Map<String, String> params, Map<String, String> headers,
			Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mType = type;
		this.mHeaders = headers;
		this.mMapParams = params;
		this.mListener = listener;
	}

	/**
	 * Make a GET request and return a parsed object from JSON.
	 * 
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param params
	 *            Map of request body parameters
	 */
	public GsonRequest(int method, String url, Type type,
			Map<String, String> params, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mType = type;
		this.mMapParams = params;
		this.mListener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		byte[] body = null;

		if (mMapParams != null) {
			JSONObject json = new JSONObject();

			Iterator<Entry<String, String>> it = mMapParams.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
						.next();
				try {
					json.put(pairs.getKey(), pairs.getValue());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				it.remove(); // avoids a ConcurrentModificationException
			}

			if (json != null) {
				Log.v(TAG, "json: " + json.toString());
				body = json.toString().getBytes();
			}
		}

		return body != null ? body : super.getBody();
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	public String getBodyContentType() {
		if (mContentType == null)
			return super.getBodyContentType();
		else
			return mContentType;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			Object fromJson = mGson.fromJson(json, mType);
			return Response.success((T) fromJson,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
