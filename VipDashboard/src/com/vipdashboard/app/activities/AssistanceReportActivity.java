package com.vipdashboard.app.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.utils.CommonTask;

public class AssistanceReportActivity extends MainActionbarBase implements OnClickListener{
	RelativeLayout rlCall,rlData,rlText,rlDevice,rlApp;
	TextView tvNetworkType;
	
	TextView tvGPSValue;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assistance_report);
		
		Initialization();
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
		TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == telMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		//InitialArrangement();
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AssistanceActivity.isBackFromReport=true;
	}
	
	private void Initialization(){	
		//rlMainLayout = (RelativeLayout) findViewById(R.id.rlAssistanceReportMainLayout);
		
		rlCall = (RelativeLayout) findViewById(R.id.rlCallButton);
		rlData = (RelativeLayout) findViewById(R.id.rlDataButton);
		rlText = (RelativeLayout) findViewById(R.id.rlTextButton);
		rlDevice = (RelativeLayout) findViewById(R.id.rlDeviceButton);
		rlApp = (RelativeLayout) findViewById(R.id.rlAppButton);
		
		tvNetworkType=(TextView) findViewById(R.id.tvNetworkType);
		tvGPSValue=(TextView) findViewById(R.id.tvGPSValue);
		
		
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(
                Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if(!wifiNetwork.isConnected()){	
			TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			tvNetworkType.setText(tm.getSimOperatorName()+" | "+PhoneBasicInformation.getNetworkTypeString(tm.getNetworkType()));
		}else{
			final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		    if (connectionInfo != null) {		      
		      tvNetworkType.setText(connectionInfo.getSSID() +" | "+ wifiNetwork.getTypeName());
		    }
			
		}
		rlCall.setOnClickListener(this);
		rlData.setOnClickListener(this);
		rlText.setOnClickListener(this);
		rlDevice.setOnClickListener(this);
		rlApp.setOnClickListener(this);
		
		displayGPSInfo();
	}
	
	private void displayGPSInfo() {
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			tvGPSValue.setText("On");
		} else {
			tvGPSValue.setText("Off");
		}
	}

	@Override
	public void onClick(View view) {
		if (!CommonTask.isOnline(this)) {
			Toast.makeText(
					this,
					"No Internet Connection.\nPlease enable your connection first",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(view.getId() == R.id.rlCallButton){
			Intent intent = new Intent(this, AssistanceReportDetalisActivity.class);
			intent.putExtra("CALL_KEY", true);
			AssistanceReportDetalisActivity.problemHeader = "Call";
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(view.getId() == R.id.rlDataButton){
			Intent intent = new Intent(this, AssistanceReportDetalisActivity.class);
			intent.putExtra("DATA_KEY", true);
			AssistanceReportDetalisActivity.problemHeader = "Data";
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(view.getId() == R.id.rlTextButton){
			Intent intent = new Intent(this, AssistanceReportDetalisActivity.class);
			intent.putExtra("TEXT_KEY", true);
			AssistanceReportDetalisActivity.problemHeader = "Text";
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(view.getId() == R.id.rlDeviceButton){
			Intent intent = new Intent(this, AssistanceReportDetalisActivity.class);
			intent.putExtra("DEVICE_KEY", true);
			AssistanceReportDetalisActivity.problemHeader = "Device";
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(view.getId() == R.id.rlAppButton){
			Intent intent = new Intent(this, AssistanceReportDetalisActivity.class);
			intent.putExtra("APPS_KEY", true);
			AssistanceReportDetalisActivity.problemHeader = "App";
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
