package com.vipdashboard.app.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_BatteryDoctorYourModeActivity extends MainActionbarBase implements OnClickListener{
	TextView tvBrightnessValue, tvTimeoutValue, tvDataValue, tvWifiValue, tvBluetoothValue, 
			tvAutomaicSyncValue, tvSilenceValue, tvVibrationValue;
	RelativeLayout rlBrightness, rlTimeout, rlData, rlWifi, rlBluetooth, 
					rlAutomaticSync, rlSilence,rlVibration;
	Button bBack;
	boolean isCallFromSilenceMode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_your_mode);
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
		Bundle saveInstance = getIntent().getExtras();
		if(saveInstance != null && saveInstance.containsKey("SILENCEMODE")){
			isCallFromSilenceMode = saveInstance.getBoolean("SILENCEMODE");
		}
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		getSavingModeValue();
	}
	
	private void Initialization() {
		tvBrightnessValue = (TextView) findViewById(R.id.tvBrightnessValue);
		tvTimeoutValue = (TextView) findViewById(R.id.tvTimeoutValue);
		tvDataValue = (TextView) findViewById(R.id.tvDataValue);
		tvWifiValue = (TextView) findViewById(R.id.tvWifiValue);
		tvBluetoothValue =  (TextView) findViewById(R.id.tvBluetoothValue);
		tvAutomaicSyncValue = (TextView) findViewById(R.id.tvAutomaicSyncValue);
		tvSilenceValue = (TextView) findViewById(R.id.tvSilenceValue);
		tvVibrationValue = (TextView) findViewById(R.id.tvVibrationValue);
		
		rlBrightness = (RelativeLayout) findViewById(R.id.rlBrightness);
		rlTimeout =  (RelativeLayout) findViewById(R.id.rlTimeout);
		rlData =  (RelativeLayout) findViewById(R.id.rlData);
		rlWifi =  (RelativeLayout) findViewById(R.id.rlWifi);
		rlBluetooth =  (RelativeLayout) findViewById(R.id.rlBluetooth);
		rlAutomaticSync =  (RelativeLayout) findViewById(R.id.rlAutomaticSync);
		rlSilence =  (RelativeLayout) findViewById(R.id.rlSilence);
		rlVibration =  (RelativeLayout) findViewById(R.id.rlVibration);
		
		bBack = (Button)findViewById(R.id.bBack);
		bBack.setOnClickListener(this);
	}
	
	private void getSavingModeValue() {
		try{
			if(isCallFromSilenceMode){
				rlBrightness.setVisibility(View.GONE);
				rlTimeout.setVisibility(View.GONE);
				rlData.setVisibility(View.GONE);
				rlWifi.setVisibility(View.GONE);
				rlBluetooth.setVisibility(View.GONE);
				rlAutomaticSync.setVisibility(View.GONE);
				
				AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				if(am.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
					tvSilenceValue.setText("On");
				}else{
					tvSilenceValue.setText("OFF");
				}
				
				if(am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
					tvVibrationValue.setText("On");
				}else{
					tvVibrationValue.setText("OFF");
				}
			}else{
				rlBrightness.setVisibility(View.VISIBLE);
				rlTimeout.setVisibility(View.VISIBLE);
				rlData.setVisibility(View.VISIBLE);
				rlWifi.setVisibility(View.VISIBLE);
				rlBluetooth.setVisibility(View.VISIBLE);
				rlAutomaticSync.setVisibility(View.VISIBLE);
				
				ConnectivityManager cm = null;
				boolean isConnected;
				
				int brightness = (int) ((android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS))/2.55);
				tvBrightnessValue.setText(""+brightness+"%");
				
				float sleeptime = (android.provider.Settings.System.getFloat(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT))/60000;			
				tvTimeoutValue.setText(""+sleeptime+"min");
				
				cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				isConnected = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();			
				if(isConnected){
					tvDataValue.setText("On");
				}else{
					tvDataValue.setText("OFF");
				}
				
				isConnected = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();			
				if(isConnected){
					tvWifiValue.setText("On");
				}else{
					tvWifiValue.setText("OFF");
				}
				
				BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter(); 	
				if(adapter != null) {
				    if(adapter.getState() == BluetoothAdapter.STATE_ON) {
				    	tvBluetoothValue.setText("On");
				    } else if (adapter.getState() == BluetoothAdapter.STATE_OFF){
				    	tvBluetoothValue.setText("OFF");
				    } 
				}
				
				if(ContentResolver.getMasterSyncAutomatically())
					tvAutomaicSyncValue.setText("On");
				else
					tvAutomaicSyncValue.setText("OFF");
				
				AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				if(am.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
					tvSilenceValue.setText("On");
				}else{
					tvSilenceValue.setText("OFF");
				}
				
				if(am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
					tvVibrationValue.setText("On");
				}else{
					tvVibrationValue.setText("OFF");
				}
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.bBack){
			onBackPressed();
		}
	}	
}
