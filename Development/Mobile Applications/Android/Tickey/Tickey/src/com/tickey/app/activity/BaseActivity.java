package com.tickey.app.activity;

import com.tickey.app.R;
import com.tickey.app.R.id;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends ActionBarActivity {

	protected Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}
	
	protected void setToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if(mToolbar != null ) {
			setSupportActionBar(mToolbar);
		}
		
	}

}
