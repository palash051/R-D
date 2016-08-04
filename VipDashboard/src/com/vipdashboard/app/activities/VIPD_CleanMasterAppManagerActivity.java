package com.vipdashboard.app.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CleanMaserAppManagerAdapter;
import com.vipdashboard.app.adapter.UtilityAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_CleanMasterAppManagerActivity extends MainActionbarBase implements TabListener{
	
	ViewPager appManagerViewpager;
	private ActionBar actionBar;
	private CleanMaserAppManagerAdapter adapter;
	String[] tab = {"Uninstall","Picks"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clean_master_appmanager);
		Initialization();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	private void Initialization() {
		appManagerViewpager = (ViewPager) findViewById(R.id.appManagerViewpager);
		actionBar = getActionBar();
		adapter = new CleanMaserAppManagerAdapter(getSupportFragmentManager());
		appManagerViewpager.setAdapter(adapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i=0; i<tab.length;i++) {
			actionBar.addTab(actionBar.newTab().setText(tab[i]).setTabListener(this));
		}
		appManagerViewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				 actionBar.setSelectedNavigationItem(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		appManagerViewpager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		
	}

}
