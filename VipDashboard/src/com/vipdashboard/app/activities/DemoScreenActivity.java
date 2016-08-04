package com.vipdashboard.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;

public class DemoScreenActivity extends MainActionbarBase {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_screen_layout);
		
	}

	@Override
	 protected void onPause() {
	  MyNetApplication.activityPaused();
	  super.onPause();
	 }

	 @Override
	 protected void onResume() {
		 super.onResume();
	  MyNetApplication.activityResumed();
	  
	 }

}
