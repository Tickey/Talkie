package com.tickey.app.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.Session;
import com.tickey.app.R;
import com.tickey.app.data.model.New;
import com.tickey.app.view.adapter.NewsAdapter;

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

		ArrayList<New> itemsData = new ArrayList<New>();
		
		New newItem = new New();
		newItem.likes = 0;
		newItem.id = "dsada";
		newItem.shortMessage = "one upon a time there was a little bla bla and he just dam bam and that's it";
		newItem.username = "Sir Nicolas The First";
		newItem.postImageUrl = "http://audaceradio.com/pressaudace/wp-content/uploads/2011/11/get-some-rest.jpg";
		newItem.ownerAvatarUrl = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/10429829_843049559059827_2872530395932165280_n.jpg?oh=58e539788465ee80b94b163e64477f91&oe=55020BF5&__gda__=1427078238_0b6d5f48d98e65ff5766c207d33cc5b0";
		itemsData.add( newItem);
		
		New newItem2 = new New();
		newItem2.likes = 0;
		newItem2.id = "dsada";
		newItem2.shortMessage = "one upon a time there was a little bla bla and he just dam bam and that's it";
		newItem2.username = "Sir Nicolas The First";
		newItem2.postImageUrl = "http://audaceradio.com/pressaudace/wp-content/uploads/2011/11/get-some-rest.jpg";
		newItem2.ownerAvatarUrl = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/10429829_843049559059827_2872530395932165280_n.jpg?oh=58e539788465ee80b94b163e64477f91&oe=55020BF5&__gda__=1427078238_0b6d5f48d98e65ff5766c207d33cc5b0";
		itemsData.add( newItem2);
		
		New newItem3 = new New();
		newItem3.likes = 0;
		newItem3.id = "dsada";
		newItem3.shortMessage = "one upon a time there was a little bla bla and he just dam bam and that's it";
		newItem3.username = "Sir Nicolas The First";
		newItem3.postImageUrl = "http://audaceradio.com/pressaudace/wp-content/uploads/2011/11/get-some-rest.jpg";
		newItem3.ownerAvatarUrl = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/10429829_843049559059827_2872530395932165280_n.jpg?oh=58e539788465ee80b94b163e64477f91&oe=55020BF5&__gda__=1427078238_0b6d5f48d98e65ff5766c207d33cc5b0";
		itemsData.add( newItem3);
		
		// specify an adapter (see also next example)
		mRecyclerView.setAdapter( new NewsAdapter(itemsData ));
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
