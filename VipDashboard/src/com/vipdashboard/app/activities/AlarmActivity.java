package com.vipdashboard.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AlarmPagerAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.fragments.TTRequestDetailsFragment;
import com.vipdashboard.app.utils.CommonValues;

public class AlarmActivity extends MainActionbarBase{
	private ViewPager _mViewPager;
	public AlarmPagerAdapter _adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		setUpView();
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
	
	private void setUpView() {
		_mViewPager = (ViewPager) findViewById(R.id.vpAlarm);
		_adapter = new AlarmPagerAdapter(getApplicationContext(),
				getSupportFragmentManager());
		_mViewPager.setAdapter(_adapter);
		_mViewPager.setCurrentItem(0);	
		
	}
	
	public void startFragment(){
		Intent intent = new Intent(this, TTRequestDetailsFragment.class);
		startActivity(intent);
	}
}
