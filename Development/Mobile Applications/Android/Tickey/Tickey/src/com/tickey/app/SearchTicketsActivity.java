package com.tickey.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class SearchTicketsActivity extends BaseActivity {

	private static final String TAG = SearchTicketsActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feed);
		
		Session fbSession = Session.getActiveSession();
		
		String accessToken = fbSession.getAccessToken();
		
		Log.v(TAG, "accessToken: " + accessToken);

		Request.newMeRequest(fbSession, new GraphUserCallback() {

			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					//TODO set userName to the drawerLayout title
					String userName = user.getName();
		
					new AsyncTask<String, Void, Bitmap>() {

						@Override
						protected Bitmap doInBackground(String... params) {
							String userId = params[0];
							return getFacebookProfilePicture(userId);
						}

						protected void onPostExecute(Bitmap userAvatar) {
							if (userAvatar != null) {
								//TODO set profile picture to drawer thing
							}
						};
					}.execute(user.getId());
				}
			}
		}).executeAsync();
		
		setToolbar();
	}


	@Override
	protected void setToolbar() {
		super.setToolbar();

		DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
				R.string.navigation_drawer_close);

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerToggle.syncState();
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

	private void logout() {
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

}