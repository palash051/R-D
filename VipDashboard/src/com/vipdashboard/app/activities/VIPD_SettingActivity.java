package com.vipdashboard.app.activities;

import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.UserSetting;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_SettingActivity extends MainActionbarBase implements OnClickListener, IAsynchronousTask {
	
	TextView tvCompanyName, tvCompanyCountry;
	ImageView ivOn, ivOff, ivSeeCallHistryOn, ivSeeCallHistryOff, ivMyDeviceOn, ivMyDeviceOff, ivAnyPromotionOn,
				ivAnyPromotionOff,ivNotifyIncidentsOn,ivNotifyIncidentsOff,ivSyncOn, ivSyncOff,
				ivSyncWifiOn, ivSyncWifiOff, ivnext;
	String ISMyLocation, ivSeeCallHistry, ivMyDevice, ivAnyPromotion, ivNotifyIncidents, 
			ivSync, ivSyncWifi;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	boolean isCallFromSetUserSettings, isCallFromGetUserSettings;
	
	ImageView ivPromptTextMemoOff,
	ivPromptTextMemoOn,ivRecordVoiceMemoOn,
	ivRecordVoiceMemoOff;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_settings);
		
		
		 ISMyLocation = ivSeeCallHistry = ivMyDevice = ivAnyPromotion = ivNotifyIncidents =  
			ivSync = ivSyncWifi = "false";
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

		
		 if(!CommonTask.isMyServiceRunning(this))
				startService(new Intent(this, MyNetService.class));
		
		
		tvCompanyName.setText(tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		
		isCallFromGetUserSettings = true;
		isCallFromSetUserSettings = false;
		SetInformation();
	}
	
	private void Initialization() {		
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		ivOn = (ImageView) findViewById(R.id.ivOn);
		ivOff = (ImageView) findViewById(R.id.ivOff);
		ivSeeCallHistryOn = (ImageView) findViewById(R.id.ivSeeCallHistryOn);
		ivSeeCallHistryOff = (ImageView) findViewById(R.id.ivSeeCallHistryOff);
		ivMyDeviceOn = (ImageView) findViewById(R.id.ivMyDeviceOn);
		ivMyDeviceOff = (ImageView) findViewById(R.id.ivMyDeviceOff);
		ivAnyPromotionOn = (ImageView) findViewById(R.id.ivAnyPromotionOn);
		ivAnyPromotionOff = (ImageView) findViewById(R.id.ivAnyPromotionOff);
		ivNotifyIncidentsOn = (ImageView) findViewById(R.id.ivNotifyIncidentsOn);
		ivNotifyIncidentsOff = (ImageView) findViewById(R.id.ivNotifyIncidentsOff);
		ivSyncOn = (ImageView) findViewById(R.id.ivSyncOn);
		ivSyncOff = (ImageView) findViewById(R.id.ivSyncOff);
		ivSyncWifiOn = (ImageView) findViewById(R.id.ivSyncWifiOn);
		ivSyncWifiOff = (ImageView) findViewById(R.id.ivSyncWifiOff);
		ivnext = (ImageView) findViewById(R.id.ivnext);
		ivPromptTextMemoOff = (ImageView) findViewById(R.id.ivPromptTextMemoOff);
		ivPromptTextMemoOn = (ImageView) findViewById(R.id.ivPromptTextMemoOn);
		ivRecordVoiceMemoOn = (ImageView) findViewById(R.id.ivRecordVoiceMemoOn);
		ivRecordVoiceMemoOff = (ImageView) findViewById(R.id.ivRecordVoiceMemoOff);
		
		ivnext.setOnClickListener(this);
		ivOn.setOnClickListener(this);
		ivOff.setOnClickListener(this);
		ivSeeCallHistryOn.setOnClickListener(this);
		ivSeeCallHistryOff.setOnClickListener(this);
		ivMyDeviceOn.setOnClickListener(this);
		ivMyDeviceOff.setOnClickListener(this);
		ivAnyPromotionOn.setOnClickListener(this);
		ivAnyPromotionOff.setOnClickListener(this);
		ivNotifyIncidentsOn.setOnClickListener(this);
		ivNotifyIncidentsOff.setOnClickListener(this);
		ivSyncOn.setOnClickListener(this);
		ivSyncOff.setOnClickListener(this);
		ivSyncWifiOn.setOnClickListener(this);
		ivSyncWifiOff.setOnClickListener(this);
		
		ivPromptTextMemoOff.setOnClickListener(this);
		ivPromptTextMemoOn.setOnClickListener(this);
		ivRecordVoiceMemoOn.setOnClickListener(this);
		ivRecordVoiceMemoOff.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivnext){
			SetInformation();
			isCallFromSetUserSettings = true;
		}else if(view.getId() == R.id.ivOn){
			ivOn.setVisibility(ImageView.GONE);
			ivOff.setVisibility(ImageView.VISIBLE);
			ISMyLocation = "false";
		}else if(view.getId() == R.id.ivOff){
			ivOn.setVisibility(ImageView.VISIBLE);
			ivOff.setVisibility(ImageView.GONE);
			ISMyLocation = "true";
		}else if(view.getId() == R.id.ivSeeCallHistryOn){
			ivSeeCallHistryOn.setVisibility(ImageView.GONE);
			ivSeeCallHistryOff.setVisibility(ImageView.VISIBLE);
			ivSeeCallHistry = "false";
		}else if(view.getId() == R.id.ivSeeCallHistryOff){
			ivSeeCallHistryOn.setVisibility(ImageView.VISIBLE);
			ivSeeCallHistryOff.setVisibility(ImageView.GONE);
			ivSeeCallHistry = "true";
		}else if(view.getId() == R.id.ivMyDeviceOn){
			ivMyDeviceOn.setVisibility(ImageView.GONE);
			ivMyDeviceOff.setVisibility(ImageView.VISIBLE);
			ivMyDevice = "false";
		}else if(view.getId() == R.id.ivMyDeviceOff){
			ivMyDeviceOn.setVisibility(ImageView.VISIBLE);
			ivMyDeviceOff.setVisibility(ImageView.GONE);
			ivMyDevice = "true";
		}else if(view.getId() == R.id.ivAnyPromotionOn){
			ivAnyPromotionOn.setVisibility(ImageView.GONE);
			ivAnyPromotionOff.setVisibility(ImageView.VISIBLE);
			ivAnyPromotion = "false";
		}else if(view.getId() == R.id.ivAnyPromotionOff){
			ivAnyPromotionOn.setVisibility(ImageView.VISIBLE);
			ivAnyPromotionOff.setVisibility(ImageView.GONE);
			ivAnyPromotion = "true";
		}else if(view.getId() == R.id.ivNotifyIncidentsOn){
			ivNotifyIncidentsOn.setVisibility(ImageView.GONE);
			ivNotifyIncidentsOff.setVisibility(ImageView.VISIBLE);
			ivNotifyIncidents = "false";
		}else if(view.getId() == R.id.ivNotifyIncidentsOff){
			ivNotifyIncidentsOn.setVisibility(ImageView.VISIBLE);
			ivNotifyIncidentsOff.setVisibility(ImageView.GONE);
			ivNotifyIncidents = "true";
		}else if(view.getId() == R.id.ivSyncOn){
			ivSyncOn.setVisibility(ImageView.GONE);
			ivSyncOff.setVisibility(ImageView.VISIBLE);
			ivSync = "false";
		}else if(view.getId() == R.id.ivSyncOff){
			ivSyncOn.setVisibility(ImageView.VISIBLE);
			ivSyncOff.setVisibility(ImageView.GONE);
			ivSync = "true";
			// This will disable wifi if exists
			if (ivSyncWifiOn.getVisibility()==View.VISIBLE)
			{
				ivSyncWifiOn.setVisibility(ImageView.GONE);
				ivSyncWifiOff.setVisibility(ImageView.VISIBLE);
				ivSyncWifi = "false";
			}
		}else if(view.getId() == R.id.ivSyncWifiOn){
			ivSyncWifiOn.setVisibility(ImageView.GONE);
			ivSyncWifiOff.setVisibility(ImageView.VISIBLE);
			ivSyncWifi = "false";
		}else if(view.getId() == R.id.ivSyncWifiOff){
			ivSyncWifiOn.setVisibility(ImageView.VISIBLE);
			ivSyncWifiOff.setVisibility(ImageView.GONE);
			ivSyncWifi = "true";
			
			// This will disable data connection if exists
			if (ivSyncOn.getVisibility()==View.VISIBLE)
			{
				ivSyncOn.setVisibility(ImageView.GONE);
				ivSyncOff.setVisibility(ImageView.VISIBLE);
				ivSync = "false";
			}
		}
		else if(view.getId() == R.id.ivPromptTextMemoOn){
			ivPromptTextMemoOn.setVisibility(ImageView.GONE);
			ivPromptTextMemoOff.setVisibility(ImageView.VISIBLE);
		}else if(view.getId() == R.id.ivPromptTextMemoOff){
			ivPromptTextMemoOn.setVisibility(ImageView.VISIBLE);
			ivPromptTextMemoOff.setVisibility(ImageView.GONE);
		}
		else if(view.getId() == R.id.ivRecordVoiceMemoOn){
			ivRecordVoiceMemoOn.setVisibility(ImageView.GONE);
			ivRecordVoiceMemoOff.setVisibility(ImageView.VISIBLE);
		}else if(view.getId() == R.id.ivRecordVoiceMemoOff){
			ivRecordVoiceMemoOn.setVisibility(ImageView.VISIBLE);
			ivRecordVoiceMemoOff.setVisibility(ImageView.GONE);
		}
		
	}

	private void SetInformation() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Processing....");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		if(isCallFromSetUserSettings){
			return userManager.setUserSettings(ISMyLocation, ivSeeCallHistry, ivMyDevice, ivAnyPromotion, ivNotifyIncidents, ivSync, ivSyncWifi,
					ivRecordVoiceMemoOn.getVisibility()==View.VISIBLE?"true":"false",ivPromptTextMemoOn.getVisibility()==View.VISIBLE?"true":"false","");
		}else{
			return userManager.GetUserSettings();
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(isCallFromSetUserSettings){
				Toast.makeText(this, "Settings changed successfully.", Toast.LENGTH_SHORT).show();
				isCallFromSetUserSettings = false;
				onBackPressed();
				CommonConstraints.IsRecordVoiceMemo=ivRecordVoiceMemoOn.getVisibility()==View.VISIBLE?true:false;
				CommonConstraints.IsPromotTextMemo=ivPromptTextMemoOn.getVisibility()==View.VISIBLE?true:false;
			}else{
				UserSetting userSetting = (UserSetting) data;
				if(userSetting != null){
					setUserSettings(userSetting);
				}
			}
		}
		else
		{
			if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		}

	}

	private void setUserSettings(UserSetting userSetting) {
		if(!userSetting.IsSharLocation){
			ivOn.setVisibility(ImageView.GONE);
			ivOff.setVisibility(ImageView.VISIBLE);
			ISMyLocation = "false";
		}
		if(userSetting.IsSharLocation){
			ivOn.setVisibility(ImageView.VISIBLE);
			ivOff.setVisibility(ImageView.GONE);
			ISMyLocation = "true";
		}
		if(!userSetting.IsSeeMyCallHistory){
			ivSeeCallHistryOn.setVisibility(ImageView.GONE);
			ivSeeCallHistryOff.setVisibility(ImageView.VISIBLE);
			ivSeeCallHistry = "false";
		}
		if(userSetting.IsSeeMyCallHistory){
			ivSeeCallHistryOn.setVisibility(ImageView.VISIBLE);
			ivSeeCallHistryOff.setVisibility(ImageView.GONE);
			ivSeeCallHistry = "true";
		}
		if(!userSetting.IsCareConnectToMyDevice){
			ivMyDeviceOn.setVisibility(ImageView.GONE);
			ivMyDeviceOff.setVisibility(ImageView.VISIBLE);
			ivMyDevice = "false";
		}
		if(userSetting.IsCareConnectToMyDevice){
			ivMyDeviceOn.setVisibility(ImageView.VISIBLE);
			ivMyDeviceOff.setVisibility(ImageView.GONE);
			ivMyDevice = "true";
		}
		if(!userSetting.IsNotifyMeAnyPromotion){
			ivAnyPromotionOn.setVisibility(ImageView.GONE);
			ivAnyPromotionOff.setVisibility(ImageView.VISIBLE);
			ivAnyPromotion = "false";
		}
		if(userSetting.IsNotifyMeAnyPromotion){
			ivAnyPromotionOn.setVisibility(ImageView.VISIBLE);
			ivAnyPromotionOff.setVisibility(ImageView.GONE);
			ivAnyPromotion = "true";
		}
		if(!userSetting.IsNotifyNetworkIncidents){
			ivNotifyIncidentsOn.setVisibility(ImageView.GONE);
			ivNotifyIncidentsOff.setVisibility(ImageView.VISIBLE);
			ivNotifyIncidents = "false";
		}
		if(userSetting.IsNotifyNetworkIncidents){
			ivNotifyIncidentsOn.setVisibility(ImageView.VISIBLE);
			ivNotifyIncidentsOff.setVisibility(ImageView.GONE);
			ivNotifyIncidents = "true";
		}
		if(!userSetting.IsSyncDataWithTraffic){
			ivSyncOn.setVisibility(ImageView.GONE);
			ivSyncOff.setVisibility(ImageView.VISIBLE);
			ivSync = "false";
		}
		if(userSetting.IsSyncDataWithTraffic){
			ivSyncOn.setVisibility(ImageView.VISIBLE);
			ivSyncOff.setVisibility(ImageView.GONE);
			ivSync = "true";
		}
		if(!userSetting.IsSyncOnlyOnWiFi){
			ivSyncWifiOn.setVisibility(ImageView.GONE);
			ivSyncWifiOff.setVisibility(ImageView.VISIBLE);
			ivSyncWifi = "false";
		}
		if(userSetting.IsSyncOnlyOnWiFi){
			ivSyncWifiOn.setVisibility(ImageView.VISIBLE);
			ivSyncWifiOff.setVisibility(ImageView.GONE);
			ivSyncWifi = "true";
		}
		if(!userSetting.IsRecordVoiceMemo){
			ivRecordVoiceMemoOn.setVisibility(ImageView.GONE);
			ivRecordVoiceMemoOff.setVisibility(ImageView.VISIBLE);
		}
		if(userSetting.IsRecordVoiceMemo){
			ivRecordVoiceMemoOn.setVisibility(ImageView.VISIBLE);
			ivRecordVoiceMemoOff.setVisibility(ImageView.GONE);
		}
		if(userSetting.IsPromptTextMemo){
			ivPromptTextMemoOn.setVisibility(ImageView.VISIBLE);
			ivPromptTextMemoOff.setVisibility(ImageView.GONE);
		}
		if(!userSetting.IsPromptTextMemo){
			ivPromptTextMemoOff.setVisibility(ImageView.VISIBLE);
			ivPromptTextMemoOn.setVisibility(ImageView.GONE);
		}

	}


}
