package com.vipdashboard.app.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorMoreOptionsActivity extends MainActionbarBase
		implements OnClickListener {

	RelativeLayout rlScreenOffSaving, rlWhenpoweris;

	ImageView ivDisableWifiOn, ivDisableWifiOff, ivScreenOffSavingOn,
			ivScreenOffSavingOff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_more_options);
		Initialization();
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		  if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		super.onResume();
		BindInitialInfo();
	}

	private void Initialization() {
		rlScreenOffSaving = (RelativeLayout) findViewById(R.id.rlScreenOffSaving);
		rlWhenpoweris = (RelativeLayout) findViewById(R.id.rlWhenpoweris);

		ivDisableWifiOn = (ImageView) findViewById(R.id.ivDisableWifiOn);
		ivDisableWifiOff = (ImageView) findViewById(R.id.ivDisableWifiOff);

		ivScreenOffSavingOn = (ImageView) findViewById(R.id.ivScreenOffSavingOn);
		ivScreenOffSavingOff = (ImageView) findViewById(R.id.ivScreenOffSavingOff);

		rlScreenOffSaving.setOnClickListener(this);
		rlWhenpoweris.setOnClickListener(this);
	}
	
	private void BindInitialInfo() {
		if (CommonConstraints.IS_BD_MORE_OPTION_SCREEN_SAVING) {
			ivScreenOffSavingOn.setVisibility(View.VISIBLE);
			ivScreenOffSavingOff.setVisibility(View.GONE);
		} else {
			ivScreenOffSavingOn.setVisibility(View.GONE);
			ivScreenOffSavingOff.setVisibility(View.VISIBLE);
		}
		
		if(CommonConstraints.IS_BD_MORE_OPTION_DISABLE_WIFI)
		{
			ivDisableWifiOn.setVisibility(View.VISIBLE);
			ivDisableWifiOff.setVisibility(View.GONE);
		}
		else
		{
			ivDisableWifiOn.setVisibility(View.GONE);
			ivDisableWifiOff.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bBack) {
			onBackPressed();
		} else if (view.getId() == R.id.rlScreenOffSaving) {
			if(ivScreenOffSavingOn.getVisibility()==View.VISIBLE)
			{
				ivScreenOffSavingOn.setVisibility(View.GONE);
				ivScreenOffSavingOff.setVisibility(View.VISIBLE);
				CommonConstraints.IS_BD_MORE_OPTION_SCREEN_SAVING=false;
			}
			else if(ivScreenOffSavingOff.getVisibility()==View.VISIBLE)
			{
				ivScreenOffSavingOn.setVisibility(View.VISIBLE);
				ivScreenOffSavingOff.setVisibility(View.GONE);
				CommonConstraints.IS_BD_MORE_OPTION_SCREEN_SAVING=true;
			}	
		}

		else if (view.getId() == R.id.rlWhenpoweris) {
			if(ivDisableWifiOn.getVisibility()==View.VISIBLE)
			{
				ivDisableWifiOn.setVisibility(View.GONE);
				ivDisableWifiOff.setVisibility(View.VISIBLE);
				CommonConstraints.IS_BD_MORE_OPTION_DISABLE_WIFI=false;
			}
			else if(ivDisableWifiOff.getVisibility()==View.VISIBLE)
			{
				ivDisableWifiOn.setVisibility(View.VISIBLE);
				ivDisableWifiOff.setVisibility(View.GONE);
				CommonConstraints.IS_BD_MORE_OPTION_DISABLE_WIFI=true;
			}	
		}
	}
}
