package com.vipdashboard.app.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.GetStartedAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;

public class GetStarted extends MainActionbarBase implements OnContactSelectedListener, TabListener{
	
	ActionBar actionBar;
	
	ViewPager viewPager;
	String[] tab = {"1 "," 2","3 "," 4"," 5"};
	int[] image = {R.drawable.call_log, R.drawable.message,R.drawable.contact_number};
	GetStartedAdapter adapter;
	
	public static boolean IsNeedtoskipLogin=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.getstarted);
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
	
	@Override
	public void onBackPressed() {
		if(!IsNeedtoskipLogin)
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(GetStarted.this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
			
	}

	private void Initalization() {
		viewPager = (ViewPager) findViewById(R.id.getstartedpager);
		actionBar = getActionBar();
		
		
		
		adapter = new GetStartedAdapter(getSupportFragmentManager(),IsNeedtoskipLogin);
		viewPager.setAdapter(adapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		if(!IsNeedtoskipLogin)
		{
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.hide();
		}
		
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
//		tab.hide(mFragment);
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		
	}
}
