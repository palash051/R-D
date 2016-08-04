package com.khareeflive.app.activities;

import com.khareeflive.app.R;
import com.khareeflive.app.base.KhareefLiveApplication;

import android.app.Activity;
import android.os.Bundle;

public class NetworkActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);
		
	}
	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}
}
