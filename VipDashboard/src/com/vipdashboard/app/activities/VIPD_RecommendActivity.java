package com.vipdashboard.app.activities;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_RecommendActivity extends MainActionbarBase implements OnClickListener{
	
	TextView tvMobileSafe, tvMobileSecurity, tvBatteryDoctor, tvCleanMaster,tvAirDroid;
	TextView tvCompanyName, tvCompanyCountry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_device);
		
		
		
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
		tvCompanyName.setText(tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
	}

	private void Initialization() {
		tvMobileSafe = (TextView) findViewById(R.id.tvMobileSafe);
		tvMobileSecurity = (TextView) findViewById(R.id.tvMobileSecurity);
		tvBatteryDoctor = (TextView) findViewById(R.id.tvBatteryDoctor);
		tvCleanMaster = (TextView) findViewById(R.id.tvCleanMaster);
		tvAirDroid = (TextView) findViewById(R.id.tvAirDroid);
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		
		tvMobileSafe.setOnClickListener(this);
		tvMobileSecurity.setOnClickListener(this);
		tvBatteryDoctor.setOnClickListener(this);
		tvCleanMaster.setOnClickListener(this);
		tvAirDroid.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.tvMobileSafe){			
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("https://play.google.com/store/apps/details?id=com.qihoo.msafe"));
				startActivity(browserIntent);
		}else if(view.getId() == R.id.tvMobileSecurity){
			
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("https://play.google.com/store/apps/details?id=com.qihoo.security"));
				startActivity(browserIntent);
		}else if(view.getId() == R.id.tvBatteryDoctor){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("https://play.google.com/store/apps/details?id=net.lepeng.batterydoctor"));
			startActivity(browserIntent);
		}else if(view.getId() == R.id.tvCleanMaster){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("https://play.google.com/store/apps/details?id=com.cleanmaster.mguard"));
			startActivity(browserIntent);
		}else if(view.getId() == R.id.tvAirDroid){
			
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("https://play.google.com/store/apps/details?id=com.sand.airdroid"));
				startActivity(browserIntent);
		}
	}

}
