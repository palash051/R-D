package com.khareeflive.app.base;
import com.khareeflive.app.utils.CommonURL;
import com.khareeflive.app.utils.CommonValues;

import android.app.Application;
import android.content.Intent;




public class KhareefLiveApplication extends Application{
	@Override
	public void onCreate() {		
		super.onCreate();		
		initializeCommonInstance();		
		
	}
	private void initializeCommonInstance() {
		CommonURL.initializeInstance();
		CommonValues.initializeInstance();
	}
	
	public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void activityResumed() {
		activityVisible = true;
	}

	public static void activityPaused() {
		activityVisible = false;
	}

	private static boolean activityVisible;
	
}
