package com.tickey.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.Session;

public class FeedActivity extends BaseActivity {

	private RecyclerView mRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feed);

		setToolbar();

		initializeViews();
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

	private void initializeViews() {
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				logout();
			}
		});
		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
	}

	private void logout() {
		finish();

		Session session = Session.getActiveSession();

		if (session != null) {
			session.closeAndClearTokenInformation();
		}

		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(intent);
	}
}
