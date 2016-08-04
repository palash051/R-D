package com.vipdashboard.app.activities;

import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VIPD_MyDashboardActivity extends MainActionbarBase implements
		OnClickListener, OnTouchListener {

	RelativeLayout rlPerformance, rlProblemTracking, rlNetworkInfo,
			rlBadExperience;

	TextView tvCompanyName, tvCompanyCountry;
	
	ImageView ivPerformance,
	ivProblemTracking,
	ivNetworkInfo,
	ivBadExperience;
	
	TextView tvProblemTracking,
	tvNetworkInfo,
	tvBadExperience,
	tvPerformance;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_dashboard_menu);

		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);

		rlPerformance = (RelativeLayout) findViewById(R.id.rlPerformance);
		rlProblemTracking = (RelativeLayout) findViewById(R.id.rlProblemTracking);
		rlNetworkInfo = (RelativeLayout) findViewById(R.id.rlNetworkInfo);
		rlBadExperience = (RelativeLayout) findViewById(R.id.rlBadExperience);
		
		ivPerformance = (ImageView) findViewById(R.id.ivPerformance);
		ivProblemTracking = (ImageView) findViewById(R.id.ivProblemTracking);
		ivNetworkInfo = (ImageView) findViewById(R.id.ivNetworkInfo);
		ivBadExperience = (ImageView) findViewById(R.id.ivBadExperience);
		
		tvProblemTracking= (TextView) findViewById(R.id.tvProblemTracking);
		tvNetworkInfo= (TextView) findViewById(R.id.tvNetworkInfo);
		tvBadExperience= (TextView) findViewById(R.id.tvBadExperience);
		tvPerformance= (TextView) findViewById(R.id.tvPerformance);

		rlPerformance.setOnClickListener(this);
		rlProblemTracking.setOnClickListener(this);
		rlNetworkInfo.setOnClickListener(this);
		rlBadExperience.setOnClickListener(this);

		rlPerformance.setOnTouchListener(this);
		rlProblemTracking.setOnTouchListener(this);
		rlNetworkInfo.setOnTouchListener(this);
		rlBadExperience.setOnTouchListener(this);
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
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
			}
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		 if(!CommonTask.isMyServiceRunning(this))
				startService(new Intent(this, MyNetService.class));
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		InitializeValues();
		setBackgroundColor();
	}

	private void InitializeValues() {
		
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
}

	private void setBackgroundColor() {
		rlPerformance.setBackgroundColor(Color.parseColor("#FF33CC"));
		rlProblemTracking.setBackgroundColor(Color.parseColor("#FF9900"));
		rlNetworkInfo.setBackgroundColor(Color.parseColor("#00B050"));
		rlBadExperience.setBackgroundColor(Color.parseColor("#CC3300"));
		
		final float scale = getResources().getDisplayMetrics().density;
		
		
		ivPerformance.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivPerformance.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		
		ivProblemTracking.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivProblemTracking.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivNetworkInfo.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivNetworkInfo.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivBadExperience.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivBadExperience.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		
		
		tvPerformance.setTextSize(CommonValues.COMMON_TEXT_SIZE);
		tvProblemTracking.setTextSize(CommonValues.COMMON_TEXT_SIZE);
		tvNetworkInfo.setTextSize(CommonValues.COMMON_TEXT_SIZE);
		tvBadExperience.setTextSize(CommonValues.COMMON_TEXT_SIZE);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlPerformance) {
			Intent intent = new Intent(this,
					ExperienceNetworkSummaryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlProblemTracking) {
			Intent intent = new Intent(this, VIPD_ProblemTrackingActivity.class);
			VIPD_ProblemTrackingActivity.selectedMenu = 2;
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlNetworkInfo) {
			Intent intent = new Intent(this, VIPDNetworkUsageviewActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlBadExperience) {
			Intent intent = new Intent(this, VIPDMapsActivity.class);
			intent.putExtra("isVisibleRequired", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	
	@Override public boolean onTouch(View view, MotionEvent arg1)
	  {   
		   findViewById(view.getId()).setBackgroundColor(Color.parseColor("#FFC000"));
		   
		   final float scale = getResources().getDisplayMetrics().density;
		   
		   switch(view.getId())
		   {
			   	case R.id.rlPerformance:
			   		ivPerformance.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					ivPerformance.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					tvPerformance.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
				   break;
			   	case R.id.rlProblemTracking:
			   		ivProblemTracking.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					ivProblemTracking.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					tvProblemTracking.setTextSize(13);
					   break;
			   	case R.id.rlNetworkInfo:
			   		ivNetworkInfo.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					ivNetworkInfo.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					tvNetworkInfo.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
			   	case R.id.rlBadExperience:
			   		ivBadExperience.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					ivBadExperience.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
					tvBadExperience.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
		   }
		   return false; 
	  }
}
