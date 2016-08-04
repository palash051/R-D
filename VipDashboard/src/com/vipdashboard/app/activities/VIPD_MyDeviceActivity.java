package com.vipdashboard.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_MyDeviceActivity extends MainActionbarBase implements OnClickListener {
	
	TextView tvCheckedText;
	RelativeLayout rlBoostYourPhone, rlBatteryDoctor, rlBatteryDoctorDetails, rlCleanMaster, rlAppManager, rlAndroidSettings;
	Animation slide_up, slide_down;
	
	RelativeLayout rlHeaderLogo;
	
	TextView tvCompanyName, tvCompanyCountry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydevices);
		if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
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
		super.onResume();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
			if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
		
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
	}
	
	public void Initialization(){
		//tvCheckedText = (TextView) findViewById(R.id.tvCheckedText);
		
		
		rlBoostYourPhone = (RelativeLayout) findViewById(R.id.rlBoostYourPhone);
		rlBatteryDoctor = (RelativeLayout) findViewById(R.id.rlBatteryDoctor);
		rlCleanMaster = (RelativeLayout) findViewById(R.id.rlCleanMaster);
		rlAppManager = (RelativeLayout) findViewById(R.id.rlAppManager);
		//rlAndroidSettings = (RelativeLayout) findViewById(R.id.rlAndroidSettings);
		rlHeaderLogo= (RelativeLayout) findViewById(R.id.rlHeaderLogo);
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		//slide_up = AnimationUtils.loadAnimation(this, R.drawable.slide_up);
		//slide_down = AnimationUtils.loadAnimation(this, R.drawable.slide_down);
		
		//tvCheckedText.setOnClickListener(this);
		
		rlBoostYourPhone.setOnClickListener(this);
		rlBatteryDoctor.setOnClickListener(this);
		rlCleanMaster.setOnClickListener(this);
		rlAppManager.setOnClickListener(this);
		//rlAndroidSettings.setOnClickListener(this);
		rlHeaderLogo.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.rlHeaderLogo){
			Intent intent = new Intent(this, VIPD_CheckupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlBoostYourPhone){
			
		}else if(view.getId() == R.id.rlBatteryDoctor){
			Intent intent = new Intent(this, VIPD_BatteryDoctor.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);			
		}else if(view.getId() == R.id.rlCleanMaster){
			Intent intent = new Intent(this, VIPD_CleanMaster.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);	
		}else if(view.getId() == R.id.rlAppManager){
			Intent intent = new Intent(this,
					VIPD_CleanMasterAppManagerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			
		}
	}
	

}
