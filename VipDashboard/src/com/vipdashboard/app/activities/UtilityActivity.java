package com.vipdashboard.app.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.UtilityAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;

public class UtilityActivity extends MainActionbarBase implements OnContactSelectedListener, TabListener{
	ActionBar actionBar;
	ViewPager viewPager;
	String[] tab = {"Call Log","SMS","Contact"};
	int[] image = {R.drawable.call_log, R.drawable.message,R.drawable.contact_number};
	UtilityAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.utility_activity);
		Initalization();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	private void Initalization() {
		viewPager = (ViewPager) findViewById(R.id.Utilitypager);
		actionBar = getActionBar();
		adapter = new UtilityAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i=0; i<tab.length;i++) {
			actionBar.addTab(actionBar.newTab().setText(tab[i]).setTabListener(this));
		}
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
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
	public void onContactNameSelected(long contactId) {
		
	}

	@Override
	public void onContactNumberSelected(String contactNumber, String contactName) {
		
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		
	}
}
