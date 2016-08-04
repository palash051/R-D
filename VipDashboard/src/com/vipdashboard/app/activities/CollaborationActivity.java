package com.vipdashboard.app.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CollaborationPagerAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class CollaborationActivity extends MainActionbarBase {
	private ViewPager _mViewPager;
	public CollaborationPagerAdapter _adapter;
	public boolean isCallFromNotification;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collaboration);
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
		if (!CommonTask.isOnline(this)) {
			return;	
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		MyNetApplication.activityResumed();
	
	}

	

	private void setUpView() {
		_mViewPager = (ViewPager) findViewById(R.id.vpCollaboration);
		_adapter = new CollaborationPagerAdapter(getApplicationContext(),
				getSupportFragmentManager());
		_mViewPager.setAdapter(_adapter);
		_mViewPager.setCurrentItem(0);
	}

	public ViewPager getViewPager() {
		if (null == _mViewPager) {
			_mViewPager = (ViewPager) findViewById(R.id.vpCollaboration);
		}
		return _mViewPager;
	}
	
	public CollaborationPagerAdapter getPagerAdapter() {		
		return _adapter;
	}
}
