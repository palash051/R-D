package com.vipdashboard.app.activities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.CustomMode;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_BatteryDoctorMoreActivity extends MainActionbarBase implements
		OnClickListener {

	RelativeLayout rlCharge, rlSave, rlYourMode, rlSuperSaving, rlSilentMode,
			rlCancle, rlOk, rlAddModeButton, rlCustomMode1, rlCustomMode2,
			rlCustomMode3, rlCustomMode4, rlCustomMode5, rlCustomMode6,rlRank;

	TextView tvSilenceMode, tvVibrationMode, tvSilenceModeHeader,
			tvBrightnessMode, tvTimeoutMode, tvDataMode, tvWifiMode,
			tvAutomaticSynsMode, tvBluetoothMode, tvCustomMode1Headertext,
			tvCustomMode2Headertext, tvCustomMode3Headertext,
			tvCustomMode4Headertext, tvCustomMode5Headertext,
			tvCustomMode6Headertext;

	ImageView ivModeActive, ivModeDeactive, ivSuperSavingActive,
			ivSuperSavingDeactive, ivSilentModeActive, ivSilentModeDeactive,
			ivCustomMode1Active, ivCustomMode1Deactive, ivCustomMode2Active,
			ivCustomMode2Deactive, ivCustomMode3Active, ivCustomMode3Deactive,
			ivCustomMode4Active, ivCustomMode4Deactive, ivCustomMode5Active,
			ivCustomMode5Deactive, ivCustomMode6Active, ivCustomMode6Deactive;

	public final static String YourModeKey = "YOURMODE";
	public final static String SuperSavingKey = "SUPERSAVING";
	public final static String SilenceModeKey = "SILENCEMODE";
	public final static String customMode1Key = "CUSTOMMODE1";
	public final static String customMode2Key = "CUSTOMMODE2";
	public final static String customMode3Key = "CUSTOMMODE3";
	public final static String customMode4Key = "CUSTOMMODE4";
	public final static String customMode5Key = "CUSTOMMODE5";
	public final static String customMode6Key = "CUSTOMMODE6";

	String YourModeValue, SuperSavingValue, SilenceModeValue, customMode1,
			customMode2, customMode3, customMode4, customMode5, customMode6;

	SharedPreferences.Editor putEditor;
	SharedPreferences getEditor;
	MyNetDatabase database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_more);

		Initalization();
	}

	private void Initalization() {
		rlSave = (RelativeLayout) findViewById(R.id.rlSave);
		rlCharge = (RelativeLayout) findViewById(R.id.rlCharge);
		rlRank= (RelativeLayout) findViewById(R.id.rlRank);
		rlYourMode = (RelativeLayout) findViewById(R.id.rlYourMode);
		rlSuperSaving = (RelativeLayout) findViewById(R.id.rlSuperSaving);
		rlSilentMode = (RelativeLayout) findViewById(R.id.rlSilentMode);
		rlAddModeButton = (RelativeLayout) findViewById(R.id.rlAddModeButton);
		rlCustomMode1 = (RelativeLayout) findViewById(R.id.rlCustomMode1);
		rlCustomMode2 = (RelativeLayout) findViewById(R.id.rlCustomMode2);
		rlCustomMode3 = (RelativeLayout) findViewById(R.id.rlCustomMode3);
		rlCustomMode4 = (RelativeLayout) findViewById(R.id.rlCustomMode4);
		rlCustomMode5 = (RelativeLayout) findViewById(R.id.rlCustomMode5);
		rlCustomMode6 = (RelativeLayout) findViewById(R.id.rlCustomMode6);		

		tvCustomMode1Headertext = (TextView) findViewById(R.id.tvCustomMode1Headertext);
		tvCustomMode2Headertext = (TextView) findViewById(R.id.tvCustomMode2Headertext);
		tvCustomMode3Headertext = (TextView) findViewById(R.id.tvCustomMode3Headertext);
		tvCustomMode4Headertext = (TextView) findViewById(R.id.tvCustomMode4Headertext);
		tvCustomMode5Headertext = (TextView) findViewById(R.id.tvCustomMode5Headertext);
		tvCustomMode6Headertext = (TextView) findViewById(R.id.tvCustomMode6Headertext);

		ivModeActive = (ImageView) findViewById(R.id.ivModeActive);
		ivModeDeactive = (ImageView) findViewById(R.id.ivModeDeactive);
		ivSuperSavingActive = (ImageView) findViewById(R.id.ivSuperSavingActive);
		ivSuperSavingDeactive = (ImageView) findViewById(R.id.ivSuperSavingDeactive);
		ivSilentModeActive = (ImageView) findViewById(R.id.ivSilentModeActive);
		ivSilentModeDeactive = (ImageView) findViewById(R.id.ivSilentModeDeactive);
		ivCustomMode1Active = (ImageView) findViewById(R.id.ivCustomMode1Active);
		ivCustomMode1Deactive = (ImageView) findViewById(R.id.ivCustomMode1Deactive);
		ivCustomMode2Active = (ImageView) findViewById(R.id.ivCustomMode2Active);
		ivCustomMode2Deactive = (ImageView) findViewById(R.id.ivCustomMode2Deactive);
		ivCustomMode3Active = (ImageView) findViewById(R.id.ivCustomMode3Active);
		ivCustomMode3Deactive = (ImageView) findViewById(R.id.ivCustomMode3Deactive);
		ivCustomMode4Active = (ImageView) findViewById(R.id.ivCustomMode4Active);
		ivCustomMode4Deactive = (ImageView) findViewById(R.id.ivCustomMode4Deactive);
		ivCustomMode5Active = (ImageView) findViewById(R.id.ivCustomMode5Active);
		ivCustomMode5Deactive = (ImageView) findViewById(R.id.ivCustomMode5Deactive);
		ivCustomMode6Active = (ImageView) findViewById(R.id.ivCustomMode6Active);
		ivCustomMode6Deactive = (ImageView) findViewById(R.id.ivCustomMode6Deactive);		

		rlSave.setOnClickListener(this);
		rlCharge.setOnClickListener(this);
		rlYourMode.setOnClickListener(this);
		rlSuperSaving.setOnClickListener(this);
		rlSilentMode.setOnClickListener(this);
		rlAddModeButton.setOnClickListener(this);
		rlCustomMode1.setOnClickListener(this);
		rlCustomMode2.setOnClickListener(this);
		rlCustomMode3.setOnClickListener(this);
		rlCustomMode4.setOnClickListener(this);
		rlCustomMode5.setOnClickListener(this);
		rlCustomMode6.setOnClickListener(this);
		rlRank.setOnClickListener(this);

		putEditor = getPreferences(MODE_PRIVATE).edit();
		getEditor = getPreferences(MODE_PRIVATE);
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
		setShareadPrefferencevalue();
		setCustomMode();
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	private void setShareadPrefferencevalue() {
		// get value from sharedprefference
		YourModeValue = getEditor.getString(YourModeKey, "");
		SuperSavingValue = getEditor.getString(SuperSavingKey, "");
		SilenceModeValue = getEditor.getString(SilenceModeKey, "");
		customMode1 = getEditor.getString(customMode1Key, "");
		customMode2 = getEditor.getString(customMode2Key, "");
		customMode3 = getEditor.getString(customMode3Key, "");
		customMode4 = getEditor.getString(customMode4Key, "");
		customMode5 = getEditor.getString(customMode5Key, "");
		customMode6 = getEditor.getString(customMode6Key, "");
		// set value from sharedprefference
		if (YourModeValue.isEmpty() && SuperSavingValue.isEmpty()
				&& SilenceModeValue.isEmpty() && customMode1.isEmpty()
				&& customMode2.isEmpty()) {
			// inital position all of are black so add sharedprefference value
			// as youmode
			putEditor.putString(YourModeKey, "1");
			putEditor.putString(SuperSavingKey, "");
			putEditor.putString(SilenceModeKey, "");
			putEditor.putString(customMode1Key, "");
			putEditor.putString(customMode2Key, "");
			putEditor.putString(customMode3Key, "");
			putEditor.putString(customMode4Key, "");
			putEditor.putString(customMode5Key, "");
			putEditor.putString(customMode6Key, "");
			putEditor.commit();
		} else if (!YourModeValue.isEmpty()) {
			ivModeActive.setVisibility(View.VISIBLE);
			ivModeDeactive.setVisibility(View.GONE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
			
		} else if (!SuperSavingValue.isEmpty()) {
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.VISIBLE);
			ivSuperSavingDeactive.setVisibility(View.GONE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
			
		} else if (!SilenceModeValue.isEmpty()) {
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.VISIBLE);
			ivSilentModeDeactive.setVisibility(View.GONE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
			
		}else if(!customMode1.isEmpty()){
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.VISIBLE);
			ivCustomMode1Deactive.setVisibility(View.GONE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
		}else if(!customMode2.isEmpty()){
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.VISIBLE);
			ivCustomMode2Deactive.setVisibility(View.GONE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
		}else if(!customMode3.isEmpty()){
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.VISIBLE);
			ivCustomMode3Deactive.setVisibility(View.GONE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
		}else if(!customMode4.isEmpty()){
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.VISIBLE);
			ivCustomMode4Deactive.setVisibility(View.GONE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
		}else if(!customMode5.isEmpty()){
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.VISIBLE);
			ivCustomMode5Deactive.setVisibility(View.GONE);
			
			ivCustomMode6Active.setVisibility(View.GONE);
			ivCustomMode6Deactive.setVisibility(View.VISIBLE);
		}else if(!customMode6.isEmpty()){
			ivModeActive.setVisibility(View.GONE);
			ivModeDeactive.setVisibility(View.VISIBLE);

			ivSuperSavingActive.setVisibility(View.GONE);
			ivSuperSavingDeactive.setVisibility(View.VISIBLE);

			ivSilentModeActive.setVisibility(View.GONE);
			ivSilentModeDeactive.setVisibility(View.VISIBLE);
			
			ivCustomMode1Active.setVisibility(View.GONE);
			ivCustomMode1Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode2Active.setVisibility(View.GONE);
			ivCustomMode2Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode3Active.setVisibility(View.GONE);
			ivCustomMode3Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode4Active.setVisibility(View.GONE);
			ivCustomMode4Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode5Active.setVisibility(View.GONE);
			ivCustomMode5Deactive.setVisibility(View.VISIBLE);
			
			ivCustomMode6Active.setVisibility(View.VISIBLE);
			ivCustomMode6Deactive.setVisibility(View.GONE);
		}
	}
	
	private void setCustomMode() {
		database = new MyNetDatabase(this);
		try{
			database.open();
			ArrayList<CustomMode> customModes = database.getAllCustomModeList();
			if(customModes != null && customModes.size()>0){
				customModeSetInLayout(customModes.size(),customModes);
			}
		}catch (Exception e) {
			e.printStackTrace();
			database.close();
		}finally{
			database.close();
		}
	}

	private void customModeSetInLayout(int size, ArrayList<CustomMode> customModes) {
		switch (size) {
		case 1:
			rlCustomMode1.setVisibility(View.VISIBLE);
			rlCustomMode1.setTag(customModes.get(0)._id);
			tvCustomMode1Headertext.setText(customModes.get(0).ModeName);
			break;
		case 2:
			rlCustomMode1.setVisibility(View.VISIBLE);
			rlCustomMode1.setTag(customModes.get(0)._id);
			tvCustomMode1Headertext.setText(customModes.get(0).ModeName);
			rlCustomMode2.setVisibility(View.VISIBLE);
			rlCustomMode2.setTag(customModes.get(1)._id);
			tvCustomMode2Headertext.setText(customModes.get(1).ModeName);
			break;
		case 3:
			rlCustomMode1.setVisibility(View.VISIBLE);
			rlCustomMode1.setTag(customModes.get(0)._id);
			tvCustomMode1Headertext.setText(customModes.get(0).ModeName);
			rlCustomMode2.setVisibility(View.VISIBLE);
			rlCustomMode2.setTag(customModes.get(1)._id);
			tvCustomMode2Headertext.setText(customModes.get(1).ModeName);
			rlCustomMode3.setVisibility(View.VISIBLE);
			rlCustomMode3.setTag(customModes.get(2)._id);
			tvCustomMode3Headertext.setText(customModes.get(2).ModeName);
			break;
		case 4:
			rlCustomMode1.setVisibility(View.VISIBLE);
			rlCustomMode1.setTag(customModes.get(0)._id);
			tvCustomMode1Headertext.setText(customModes.get(0).ModeName);
			rlCustomMode2.setVisibility(View.VISIBLE);
			rlCustomMode2.setTag(customModes.get(1)._id);
			tvCustomMode2Headertext.setText(customModes.get(1).ModeName);
			rlCustomMode3.setVisibility(View.VISIBLE);
			rlCustomMode3.setTag(customModes.get(2)._id);
			tvCustomMode3Headertext.setText(customModes.get(2).ModeName);
			rlCustomMode4.setVisibility(View.VISIBLE);
			rlCustomMode4.setTag(customModes.get(3)._id);
			tvCustomMode4Headertext.setText(customModes.get(3).ModeName);
			break;
		case 5:
			rlCustomMode1.setVisibility(View.VISIBLE);
			rlCustomMode1.setTag(customModes.get(0)._id);
			tvCustomMode1Headertext.setText(customModes.get(0).ModeName);
			rlCustomMode2.setVisibility(View.VISIBLE);
			rlCustomMode2.setTag(customModes.get(1)._id);
			tvCustomMode2Headertext.setText(customModes.get(1).ModeName);
			rlCustomMode3.setVisibility(View.VISIBLE);
			rlCustomMode3.setTag(customModes.get(2)._id);
			tvCustomMode3Headertext.setText(customModes.get(2).ModeName);
			rlCustomMode4.setVisibility(View.VISIBLE);
			rlCustomMode4.setTag(customModes.get(3)._id);
			tvCustomMode4Headertext.setText(customModes.get(3).ModeName);
			rlCustomMode5.setVisibility(View.VISIBLE);
			rlCustomMode5.setTag(customModes.get(4)._id);
			tvCustomMode5Headertext.setText(customModes.get(4).ModeName);
			break;
		case 6:
			rlCustomMode1.setVisibility(View.VISIBLE);
			rlCustomMode1.setTag(customModes.get(0)._id);
			tvCustomMode1Headertext.setText(customModes.get(0).ModeName);
			rlCustomMode2.setVisibility(View.VISIBLE);
			rlCustomMode2.setTag(customModes.get(1)._id);
			tvCustomMode2Headertext.setText(customModes.get(1).ModeName);
			rlCustomMode3.setVisibility(View.VISIBLE);
			rlCustomMode3.setTag(customModes.get(2)._id);
			tvCustomMode3Headertext.setText(customModes.get(2).ModeName);
			rlCustomMode4.setVisibility(View.VISIBLE);
			rlCustomMode4.setTag(customModes.get(3)._id);
			tvCustomMode4Headertext.setText(customModes.get(3).ModeName);
			rlCustomMode5.setVisibility(View.VISIBLE);
			rlCustomMode5.setTag(customModes.get(4)._id);
			tvCustomMode5Headertext.setText(customModes.get(4).ModeName);
			rlCustomMode6.setVisibility(View.VISIBLE);
			rlCustomMode6.setTag(customModes.get(5)._id);
			tvCustomMode6Headertext.setText(customModes.get(5).ModeName);
			break;
		default:
			rlCustomMode1.setVisibility(View.GONE);
			rlCustomMode2.setVisibility(View.GONE);
			rlCustomMode3.setVisibility(View.GONE);
			rlCustomMode4.setVisibility(View.GONE);
			rlCustomMode5.setVisibility(View.GONE);
			rlCustomMode6.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlSave) {
			Intent intent = new Intent(this, VIPD_BatteryDoctor.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlCharge) {
			Intent intent = new Intent(this,
					VIPD_BatteryDoctorChargeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} 
		else if (view.getId() == R.id.rlRank) {
			Intent intentBatteryUsage = new Intent(
					Intent.ACTION_POWER_USAGE_SUMMARY);
			startActivity(intentBatteryUsage);
		} 
		else if (view.getId() == R.id.rlAddModeButton) {
			database = new MyNetDatabase(this);
			try{
				database.open();
				int count = database.getTotalCountOfCustomMode();
				if(count > 5){
					Toast.makeText(this, "Max customize Mode", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = new Intent(this, VIPD_AddMode.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}catch (Exception e) {
				e.printStackTrace();
				database.close();
			}finally{
				database.close();
			}
			
		} else if (view.getId() == R.id.rlYourMode) {
			YourModeChange();
		} else if (view.getId() == R.id.rlSuperSaving) {
			SuperSavingMode();
		} else if (view.getId() == R.id.rlSilentMode) {
			SilentMode();
		}else if(view.getId() == R.id.rlCustomMode1){
			int _id = (Integer) rlCustomMode1.getTag();
			database = new MyNetDatabase(this);
			try{
				database.open();
				CustomMode customMode = database.getSpecificCustomMode(_id);
				database.close();
				setCustomMode1(customMode,_id);
			}catch (Exception e) {
				e.printStackTrace();
				database.close();
			}finally{
				database.close();
			}
		}else if(view.getId() == R.id.rlCustomMode2){
			int _id = (Integer) rlCustomMode2.getTag();
			database = new MyNetDatabase(this);
			try{
				database.open();
				CustomMode customMode = database.getSpecificCustomMode(_id);
				database.close();
				setCustomMode1(customMode,_id);
			}catch (Exception e) {
				e.printStackTrace();
				database.close();
			}finally{
				database.close();
			}
		}else if(view.getId() == R.id.rlCustomMode3){
			int _id = (Integer) rlCustomMode3.getTag();
			database = new MyNetDatabase(this);
			try{
				database.open();
				CustomMode customMode = database.getSpecificCustomMode(_id);
				database.close();
				setCustomMode1(customMode,_id);
			}catch (Exception e) {
				e.printStackTrace();
				database.close();
			}finally{
				database.close();
			}
		}else if(view.getId() == R.id.rlCustomMode4){
			int _id = (Integer) rlCustomMode4.getTag();
			database = new MyNetDatabase(this);
			try{
				database.open();
				CustomMode customMode = database.getSpecificCustomMode(_id);
				database.close();
				setCustomMode1(customMode,_id);
			}catch (Exception e) {
				e.printStackTrace();
				database.close();
			}finally{
				database.close();
			}
		}else if(view.getId() == R.id.rlCustomMode5){
			int _id = (Integer) rlCustomMode5.getTag();
			database = new MyNetDatabase(this);
			try{
				database.open();
				CustomMode customMode = database.getSpecificCustomMode(_id);
				database.close();
				setCustomMode1(customMode,_id);
			}catch (Exception e) {
				e.printStackTrace();
				database.close();
			}finally{
				database.close();
			}
		}else if(view.getId() == R.id.rlCustomMode6){
			int _id = (Integer) rlCustomMode6.getTag();
			database = new MyNetDatabase(this);
			try{
				database.open();
				CustomMode customMode = database.getSpecificCustomMode(_id);
				database.close();
				setCustomMode1(customMode,_id);
			}catch (Exception e) {
				e.printStackTrace();
				database.close();
			}finally{
				database.close();
			}
		}
	}
	
	private void setCustomMode1(final CustomMode customMode, final int _id) {
		if(!customMode1.isEmpty()){
			Intent intent = new Intent(this,
					VIPD_BatteryDoctorYourModeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else{
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.choose_supersaving);
			dialog.setCancelable(false);
			
			TextView tvHeaderTitle = (TextView)dialog.findViewById(R.id.tvHeaderTitle);
			rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);
			rlOk = (RelativeLayout) dialog.findViewById(R.id.rlOk);
			tvBrightnessMode = (TextView) dialog.findViewById(R.id.tvBrightnessMode);
			tvTimeoutMode = (TextView) dialog.findViewById(R.id.tvTimeoutMode);
			tvDataMode = (TextView) dialog.findViewById(R.id.tvDataMode);
			tvWifiMode = (TextView) dialog.findViewById(R.id.tvWifiMode);
			tvBluetoothMode = (TextView) dialog.findViewById(R.id.tvBluetoothMode);
			tvAutomaticSynsMode = (TextView) dialog.findViewById(R.id.tvAutomaticSynsMode);
			tvSilenceMode = (TextView) dialog.findViewById(R.id.tvSilenceMode);
			tvVibrationMode = (TextView) dialog.findViewById(R.id.tvVibrationMode);
			
			tvHeaderTitle.setText(customMode.ModeName);
			tvBrightnessMode.setText(((int)(customMode.Brightness/2.55))+"%");
			tvTimeoutMode.setText(customMode.TimeOut/60000+"mins");
			if(customMode.Data==1){
				tvDataMode.setText("ON");
			}else{
				tvDataMode.setText("OFF");
			}
			
			if(customMode.Wifi == 1){
				tvWifiMode.setText("ON");
			}else{
				tvWifiMode.setText("OFF");
			}
			
			if(customMode.Bluetooth == 1){
				tvBluetoothMode.setText("ON");
			}else{
				tvBluetoothMode.setText("OFF");
			}
			
			if(customMode.AutomaticSync == 1){
				tvAutomaticSynsMode.setText("ON");
			}else{
				tvAutomaticSynsMode.setText("OFF");
			}
			
			if(customMode.Silence == 1){
				tvSilenceMode.setText("ON");
			}else{
				tvSilenceMode.setText("OFF");
			}
			
			if(customMode.Vibration == 1){
				tvVibrationMode.setText("ON");
			}else{
				tvVibrationMode.setText("OFF");
			}
			
			rlCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});
			
			rlOk.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					try{
						// brightness set
						android.provider.Settings.System
								.putFloat(
										getContentResolver(),
										android.provider.Settings.System.SCREEN_BRIGHTNESS,
										customMode.Brightness);
						// timeout (sleep) mode time set
						android.provider.Settings.System
								.putInt(getContentResolver(),
										android.provider.Settings.System.SCREEN_OFF_TIMEOUT,
										customMode.TimeOut);
						// data conneciton set
						
						final ConnectivityManager conman = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
						final Class conmanClass = Class.forName(conman.getClass().getName());
						final Field connectivityManagerField = conmanClass
								.getDeclaredField("mService");
						connectivityManagerField.setAccessible(true);
						final Object connectivityManager = connectivityManagerField
								.get(conman);
						final Class connectivityManagerClass = Class
								.forName(connectivityManager.getClass()
										.getName());
						final Method setMobileDataEnabledMethod = connectivityManagerClass
								.getDeclaredMethod("setMobileDataEnabled",
										Boolean.TYPE);
						setMobileDataEnabledMethod.setAccessible(true);
						if(customMode.Data == 1){
							setMobileDataEnabledMethod.invoke(connectivityManager,
									true);
						}else{
							setMobileDataEnabledMethod.invoke(connectivityManager,
									false);
						}
						
						if(customMode.Wifi == 1){
							WifiManager wifiManager = (WifiManager) getApplicationContext()
									.getSystemService(WIFI_SERVICE);
							wifiManager.setWifiEnabled(true);
						}else{
							WifiManager wifiManager = (WifiManager) getApplicationContext()
									.getSystemService(WIFI_SERVICE);
							wifiManager.setWifiEnabled(false);
						}
						
						if(customMode.Bluetooth == 1){
							BluetoothAdapter adapter = BluetoothAdapter
									.getDefaultAdapter();
							if (adapter != null)
								adapter.enable();
						}else{
							BluetoothAdapter adapter = BluetoothAdapter
									.getDefaultAdapter();
							if (adapter != null)
								adapter.disable();
						}
						
						if(customMode.AutomaticSync == 1){
							ContentResolver.setMasterSyncAutomatically(true);
						}else{
							ContentResolver.setMasterSyncAutomatically(false);
						}
						
						if(customMode.Silence == 1){
							AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
						}else{
							AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						}
						
						if(customMode.Vibration == 1){
							AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
						}else{
							AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						}
						
						// change sharedprefference value
						putEditor.clear();
						putEditor.putString(YourModeKey, "");
						putEditor.putString(SuperSavingKey, "");
						putEditor.putString(SilenceModeKey, "");
						putEditor.putString(customMode1Key, _id==0?"1":"");
						putEditor.putString(customMode2Key, _id==1?"1":"");
						putEditor.putString(customMode3Key, _id==2?"1":"");
						putEditor.putString(customMode4Key, _id==3?"1":"");
						putEditor.putString(customMode5Key, _id==4?"1":"");
						putEditor.putString(customMode6Key, _id==5?"1":"");
						putEditor.commit();
						// change chooseing image
						setShareadPrefferencevalue();
						// dialog dismiss
						dialog.dismiss();
						
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			dialog.show();
		}
	}

	private void SilentMode() {
		if (!SilenceModeValue.isEmpty()) {
			Intent intent = new Intent(this,
					VIPD_BatteryDoctorYourModeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("SILENCEMODE", true);
			startActivity(intent);
		} else {
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.battery_doctor_silence_mode);
			dialog.setCancelable(false);

			// initalizaiton component
			tvSilenceModeHeader = (TextView) dialog
					.findViewById(R.id.tvSilenceModeHeader);
			tvSilenceMode = (TextView) dialog.findViewById(R.id.tvSilenceMode);
			tvVibrationMode = (TextView) dialog
					.findViewById(R.id.tvVibrationMode);
			rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);
			rlOk = (RelativeLayout) dialog.findViewById(R.id.rlOk);

			// event click on cancel button
			rlCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			// event click of ok button
			rlOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AudioManager audio_mngr = (AudioManager) getBaseContext()
							.getSystemService(AUDIO_SERVICE);
					audio_mngr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					audio_mngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

					tvSilenceModeHeader.setText("Changed");

					if (audio_mngr.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
						tvSilenceMode.setText("On");
					} else {
						tvSilenceMode.setText("OFF");
					}

					if (audio_mngr.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
						tvVibrationMode.setText("On");
					} else {
						tvVibrationMode.setText("OFF");
					}
					// change sharedprefference value
					putEditor.clear();
					putEditor.putString(YourModeKey, "");
					putEditor.putString(SuperSavingKey, "");
					putEditor.putString(SilenceModeKey, "1");
					putEditor.putString(customMode1Key, "");
					putEditor.putString(customMode2Key, "");
					putEditor.putString(customMode3Key, "");
					putEditor.putString(customMode4Key, "");
					putEditor.putString(customMode5Key, "");
					putEditor.putString(customMode6Key, "");
					putEditor.commit();
					// change chooseing image
					setShareadPrefferencevalue();
					// dialog dismiss
					dialog.dismiss();
				}
			});

			dialog.show();

		}
	}

	private void SuperSavingMode() {
		if (!SuperSavingValue.isEmpty()) {
			Intent intent = new Intent(this,
					VIPD_BatteryDoctorYourModeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else {
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.choose_supersaving);
			dialog.setCancelable(false);

			rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);
			rlOk = (RelativeLayout) dialog.findViewById(R.id.rlOk);
			
			rlCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});

			rlOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						// brightness set
						android.provider.Settings.System
								.putFloat(
										getContentResolver(),
										android.provider.Settings.System.SCREEN_BRIGHTNESS,
										CommonValues.getInstance().brightnessvalue_supersaving);
						// timeout (sleep) mode time set
						android.provider.Settings.System
								.putInt(getContentResolver(),
										android.provider.Settings.System.SCREEN_OFF_TIMEOUT,
										CommonValues.getInstance().timeOut_supersaving);
						// data conneciton set
						final ConnectivityManager conman = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
						final Class conmanClass = Class.forName(conman
								.getClass().getName());
						final Field connectivityManagerField = conmanClass
								.getDeclaredField("mService");
						connectivityManagerField.setAccessible(true);
						final Object connectivityManager = connectivityManagerField
								.get(conman);
						final Class connectivityManagerClass = Class
								.forName(connectivityManager.getClass()
										.getName());
						final Method setMobileDataEnabledMethod = connectivityManagerClass
								.getDeclaredMethod("setMobileDataEnabled",
										Boolean.TYPE);
						setMobileDataEnabledMethod.setAccessible(true);
						setMobileDataEnabledMethod.invoke(connectivityManager,
								false);
						// set wifi
						WifiManager wifiManager = (WifiManager) getApplicationContext()
								.getSystemService(WIFI_SERVICE);
						wifiManager.setWifiEnabled(true);
						// set bluetooth
						BluetoothAdapter adapter = BluetoothAdapter
								.getDefaultAdapter();
						if (adapter != null)
							adapter.disable();
						// set automatic sync
						ContentResolver.setMasterSyncAutomatically(false);
						// set silence mode
						AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
						am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
						// set vibration mode
						am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

						// change sharedprefference value
						putEditor.clear();
						putEditor.putString(YourModeKey, "");
						putEditor.putString(SuperSavingKey, "1");
						putEditor.putString(SilenceModeKey, "");
						putEditor.putString(customMode1Key, "");
						putEditor.putString(customMode2Key, "");
						putEditor.putString(customMode3Key, "");
						putEditor.putString(customMode4Key, "");
						putEditor.putString(customMode5Key, "");
						putEditor.putString(customMode6Key, "");
						putEditor.commit();
						// change chooseing image
						setShareadPrefferencevalue();
						// dialog dismiss
						dialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			dialog.show();
		}
	}

	private void YourModeChange() {
		if (!YourModeValue.isEmpty()) {
			Intent intent = new Intent(this,
					VIPD_BatteryDoctorYourModeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else {
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.choose_yourmode);
			dialog.setCancelable(false);

			rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);
			rlOk = (RelativeLayout) dialog.findViewById(R.id.rlOk);

			rlCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});

			rlOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						// brightness set
						android.provider.Settings.System
								.putInt(getContentResolver(),
										android.provider.Settings.System.SCREEN_BRIGHTNESS,
										CommonValues.getInstance().brightnessvalue);
						// timeout (sleep) mode time set
						android.provider.Settings.System
								.putInt(getContentResolver(),
										android.provider.Settings.System.SCREEN_OFF_TIMEOUT,
										CommonValues.getInstance().timeOut);
						// data conneciton set
						final ConnectivityManager conman = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
						final Class conmanClass = Class.forName(conman
								.getClass().getName());
						final Field connectivityManagerField = conmanClass
								.getDeclaredField("mService");
						connectivityManagerField.setAccessible(true);
						final Object connectivityManager = connectivityManagerField
								.get(conman);
						final Class connectivityManagerClass = Class
								.forName(connectivityManager.getClass()
										.getName());
						final Method setMobileDataEnabledMethod = connectivityManagerClass
								.getDeclaredMethod("setMobileDataEnabled",
										Boolean.TYPE);
						setMobileDataEnabledMethod.setAccessible(true);
						setMobileDataEnabledMethod.invoke(connectivityManager,
								true);
						// set wifi
						WifiManager wifiManager = (WifiManager) getApplicationContext()
								.getSystemService(WIFI_SERVICE);
						wifiManager.setWifiEnabled(true);
						// set bluetooth
						BluetoothAdapter adapter = BluetoothAdapter
								.getDefaultAdapter();
						if (adapter != null)
							adapter.enable();
						// set automatic sync
						ContentResolver.setMasterSyncAutomatically(true);
						// set silence mode
						AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
						am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
						// set vibration mode
						am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

						// change sharedprefference value
						putEditor.clear();
						putEditor.putString(YourModeKey, "1");
						putEditor.putString(SuperSavingKey, "");
						putEditor.putString(SilenceModeKey, "");
						putEditor.putString(customMode1Key, "");
						putEditor.putString(customMode2Key, "");
						putEditor.putString(customMode3Key, "");
						putEditor.putString(customMode4Key, "");
						putEditor.putString(customMode5Key, "");
						putEditor.putString(customMode6Key, "");
						putEditor.commit();
						// change chooseing image
						setShareadPrefferencevalue();
						// dialog dismiss
						dialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			dialog.show();
		}

	}

}
