package com.vipdashboard.app.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.CustomMode;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_AddMode extends MainActionbarBase implements OnClickListener{
	
	EditText etMessageText;
	RelativeLayout rlBrightness,rlTimeout,rlProgressbarValue,rlCancle, rlOk;
	TextView tvBrightnessValue,tvTimeoutValue,tvBrightnessPercentage;
	Switch Data, Wifi, bluetooth, automaticSync, silence, vibration;
	int brightnessValue, timeoutValue;
	String dataConncetionvalue, WifiValue, bluetoothValue, automaticSyncValue, silenceValue, vibrationValue;
	
	ImageView ivfifteenSecondActive, ivfifteenSecondDeactive, ivthirtySecondActive, ivthirtySecondDeactive,
	  ivoneMinutsActive,
	  ivoneMinutsDeactive,
	  ivtwoMinutsActive,
	  ivtwoMinutsDeactive,
	  ivtenMinutsActive ,
	  ivtenMinutsDeactive,
	  ivthirtyMinutsActive,
	  ivthirtyMinutsDeactive;
	
	CheckBox ckChooseAutoMode;
	SeekBar skBrightnessBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_add_more);		
		Initalization();	
		SwitchValyeGet();
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
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		super.onResume();
		setInitvalue();
		setEditTextValue();
	}

	private void Initalization() {
		etMessageText = (EditText) findViewById(R.id.etMessageText);
		rlBrightness = (RelativeLayout) findViewById(R.id.rlBrightness);
		rlTimeout = (RelativeLayout) findViewById(R.id.rlTimeout);
		tvBrightnessValue = (TextView) findViewById(R.id.tvBrightnessValue);
		tvTimeoutValue = (TextView) findViewById(R.id.tvTimeoutValue);
		Data = (Switch) findViewById(R.id.Data);
		Wifi = (Switch) findViewById(R.id.Wifi);
		bluetooth = (Switch) findViewById(R.id.bluetooth);
		automaticSync = (Switch) findViewById(R.id.automaticSync);
		silence = (Switch) findViewById(R.id.silence);
		vibration = (Switch) findViewById(R.id.vibration);
		rlCancle = (RelativeLayout) findViewById(R.id.rlCancle);
		rlOk = (RelativeLayout) findViewById(R.id.rlOk);
		
		rlBrightness.setOnClickListener(this);
		rlTimeout.setOnClickListener(this);
		rlCancle.setOnClickListener(this);
		rlOk.setOnClickListener(this);
	}
	
	private void setEditTextValue() {
		MyNetDatabase database = new MyNetDatabase(this);
		try{
			database.open();
			int count = database.getTotalCountOfCustomMode();
			etMessageText.setText("customized mode"+(count+1));
		}catch (Exception e) {
			e.printStackTrace();
			database.close();
		}finally{
			database.close();
		}
	}
	
	private void SwitchValyeGet() {
		Data.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					dataConncetionvalue = "ON";
				}else{
					dataConncetionvalue = "OFF";
				}
			}
		});
		
		Wifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					WifiValue = "ON";
				} else {
					WifiValue = "OFF";
				}
			}
		});
		
		bluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					bluetoothValue = "ON";
				} else {
					bluetoothValue = "OFF";
				}
			}
		});
		
		automaticSync.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					automaticSyncValue = "ON";
				} else {
					automaticSyncValue = "OFF";
				}
			}
		});
		
		silence.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					silenceValue = "ON";
				} else {
					silenceValue = "OFF";
				}
			}
		});
		
		vibration.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					vibrationValue = "ON";
				} else {
					vibrationValue = "OFF";
				}
			}
		});
	}
	
	private void setInitvalue() {
		brightnessValue = 255;
		timeoutValue = 15000;
		dataConncetionvalue = "ON";
		WifiValue = "ON";
		bluetoothValue = "ON";
		automaticSyncValue = "ON";
		silenceValue = "ON";
		vibrationValue = "ON";
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.rlBrightness){
			setBrightnessValue();
		}else if(view.getId() == R.id.rlTimeout){
			setTimeoutValue();
		}else if(view.getId() == R.id.rlCancle){
			onBackPressed();
		}else if(view.getId() == R.id.rlOk){
			addCustomModeIntoDataBase();
		}
	}
	
	private void addCustomModeIntoDataBase() {
		CustomMode customMode = new CustomMode();
		customMode.ModeName = etMessageText.getText().toString();
		customMode.Brightness = (int) (Integer.parseInt(tvBrightnessValue.getText().toString().split("%")[0])*2.55);
		customMode.TimeOut = timeoutValue;
		//
		if(Data.isChecked()){
			customMode.Data = 1;
		}else{
			customMode.Data = 0;
		}
		//
		if(Wifi.isChecked()){
			customMode.Wifi = 1;
		}else{
			customMode.Wifi = 0;
		}
		//
		if(bluetooth.isChecked()){
			customMode.Bluetooth = 1;
		}else{
			customMode.Bluetooth = 0;
		}
		//
		if(automaticSync.isChecked()){
			customMode.AutomaticSync = 1;
		}else{
			customMode.AutomaticSync = 0;
		}
		//
		if(silence.isChecked()){
			customMode.Silence = 1;
		}else{
			customMode.Silence = 0;
		}
		//
		if(vibration.isChecked()){
			customMode.Vibration = 1;
		}else{
			customMode.Vibration = 0;
		}
		//
		MyNetDatabase database = new MyNetDatabase(this);
		try{
			database.open();
			long _id = database.CreateCustomMode(customMode);
			if(_id > 0)
				Toast.makeText(this, "Add new custom mode", Toast.LENGTH_SHORT).show();			
		}catch (Exception e) {
			e.printStackTrace();
			database.close();
		}finally{
			database.close();
		}
		onBackPressed();
	}

	private void setTimeoutValue() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.choose_timeout);
		dialog.setCancelable(false);
		
		RelativeLayout ivfifteenSecondMode = (RelativeLayout) dialog.findViewById(R.id.ivfifteenSecondMode);
		RelativeLayout ivthirtySecondMode = (RelativeLayout) dialog.findViewById(R.id.ivthirtySecondMode);
		RelativeLayout ivoneMinutsMode = (RelativeLayout) dialog.findViewById(R.id.ivoneMinutsMode);
		RelativeLayout ivtwoMinutsMode = (RelativeLayout) dialog.findViewById(R.id.ivtwoMinutsMode);
		RelativeLayout ivtenMinutsMode = (RelativeLayout) dialog.findViewById(R.id.ivtenMinutsMode);
		RelativeLayout ivthirtyMinutsMode = (RelativeLayout) dialog.findViewById(R.id.ivthirtyMinutsMode);
		RelativeLayout rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);
		
		ivfifteenSecondActive = (ImageView) dialog.findViewById(R.id.ivfifteenSecondActive);
		ivfifteenSecondDeactive = (ImageView) dialog.findViewById(R.id.ivfifteenSecondDeactive);
		ivthirtySecondActive = (ImageView) dialog.findViewById(R.id.ivthirtySecondActive);
		ivthirtySecondDeactive = (ImageView) dialog.findViewById(R.id.ivthirtySecondDeactive);
		ivoneMinutsActive = (ImageView) dialog.findViewById(R.id.ivoneMinutsActive);
		ivoneMinutsDeactive = (ImageView) dialog.findViewById(R.id.ivoneMinutsDeactive);
		ivtwoMinutsActive = (ImageView) dialog.findViewById(R.id.ivtwoMinutsActive);
		ivtwoMinutsDeactive = (ImageView) dialog.findViewById(R.id.ivtwoMinutsDeactive);
		ivtenMinutsActive = (ImageView) dialog.findViewById(R.id.ivtenMinutsActive);
		ivtenMinutsDeactive = (ImageView) dialog.findViewById(R.id.ivtenMinutsDeactive);
		ivthirtyMinutsActive = (ImageView) dialog.findViewById(R.id.ivthirtyMinutsActive);
		ivthirtyMinutsDeactive = (ImageView) dialog.findViewById(R.id.ivthirtyMinutsDeactive);
		
		initTimeoutValue(tvTimeoutValue.getText());
		
		rlCancle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		
		ivfifteenSecondMode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				timeoutValue = 15000;
				tvTimeoutValue.setText("15s");
				
				initTimeoutValue(tvTimeoutValue.getText());
				
				dialog.dismiss();
			}
		});
		
		ivthirtySecondMode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				timeoutValue = 30000;
				tvTimeoutValue.setText("30s");
				
				initTimeoutValue(tvTimeoutValue.getText());
				
				dialog.dismiss();
			}
		});
		
		ivoneMinutsMode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				timeoutValue = 60000;
				tvTimeoutValue.setText("1m");
				
				initTimeoutValue(tvTimeoutValue.getText());
				
				dialog.dismiss();
			}
		});
		
		ivtwoMinutsMode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				timeoutValue = 120000;
				tvTimeoutValue.setText("2mins");
				
				initTimeoutValue(tvTimeoutValue.getText());
				
				dialog.dismiss();
			}
		});
		
		ivtenMinutsMode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				timeoutValue = 600000;
				tvTimeoutValue.setText("10mins");
				
				initTimeoutValue(tvTimeoutValue.getText());
				
				dialog.dismiss();
			}
		});
		
		ivthirtyMinutsMode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				timeoutValue = 1800000;
				tvTimeoutValue.setText("30mins");
				
				initTimeoutValue(tvTimeoutValue.getText());
				
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}

	private void initTimeoutValue(CharSequence text) {
		if(text.equals("15s")){
			ivfifteenSecondActive.setVisibility(View.VISIBLE);
			ivfifteenSecondDeactive.setVisibility(View.GONE);
			
			ivthirtySecondActive.setVisibility(View.GONE);
			ivthirtySecondDeactive.setVisibility(View.VISIBLE);
			
			ivoneMinutsActive.setVisibility(View.GONE);
			ivoneMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtwoMinutsActive.setVisibility(View.GONE);
			ivtwoMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtenMinutsActive.setVisibility(View.GONE);
			ivtenMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivthirtyMinutsActive.setVisibility(View.GONE);
			ivthirtyMinutsDeactive.setVisibility(View.VISIBLE);
		}else if(text.equals("30s")){
			ivfifteenSecondActive.setVisibility(View.GONE);
			ivfifteenSecondDeactive.setVisibility(View.VISIBLE);
			
			ivthirtySecondActive.setVisibility(View.VISIBLE);
			ivthirtySecondDeactive.setVisibility(View.GONE);
			
			ivoneMinutsActive.setVisibility(View.GONE);
			ivoneMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtwoMinutsActive.setVisibility(View.GONE);
			ivtwoMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtenMinutsActive.setVisibility(View.GONE);
			ivtenMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivthirtyMinutsActive.setVisibility(View.GONE);
			ivthirtyMinutsDeactive.setVisibility(View.VISIBLE);
		}else if(text.equals("1m")){
			ivfifteenSecondActive.setVisibility(View.GONE);
			ivfifteenSecondDeactive.setVisibility(View.VISIBLE);
			
			ivthirtySecondActive.setVisibility(View.GONE);
			ivthirtySecondDeactive.setVisibility(View.VISIBLE);
			
			ivoneMinutsActive.setVisibility(View.VISIBLE);
			ivoneMinutsDeactive.setVisibility(View.GONE);
			
			ivtwoMinutsActive.setVisibility(View.GONE);
			ivtwoMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtenMinutsActive.setVisibility(View.GONE);
			ivtenMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivthirtyMinutsActive.setVisibility(View.GONE);
			ivthirtyMinutsDeactive.setVisibility(View.VISIBLE);
		}else if(text.equals("2mins")){
			ivfifteenSecondActive.setVisibility(View.GONE);
			ivfifteenSecondDeactive.setVisibility(View.VISIBLE);
			
			ivthirtySecondActive.setVisibility(View.GONE);
			ivthirtySecondDeactive.setVisibility(View.VISIBLE);
			
			ivoneMinutsActive.setVisibility(View.GONE);
			ivoneMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtwoMinutsActive.setVisibility(View.VISIBLE);
			ivtwoMinutsDeactive.setVisibility(View.GONE);
			
			ivtenMinutsActive.setVisibility(View.GONE);
			ivtenMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivthirtyMinutsActive.setVisibility(View.GONE);
			ivthirtyMinutsDeactive.setVisibility(View.VISIBLE);
		}else if(text.equals("10mins")){
			ivfifteenSecondActive.setVisibility(View.GONE);
			ivfifteenSecondDeactive.setVisibility(View.VISIBLE);
			
			ivthirtySecondActive.setVisibility(View.GONE);
			ivthirtySecondDeactive.setVisibility(View.VISIBLE);
			
			ivoneMinutsActive.setVisibility(View.GONE);
			ivoneMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtwoMinutsActive.setVisibility(View.GONE);
			ivtwoMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtenMinutsActive.setVisibility(View.VISIBLE);
			ivtenMinutsDeactive.setVisibility(View.GONE);
			
			ivthirtyMinutsActive.setVisibility(View.GONE);
			ivthirtyMinutsDeactive.setVisibility(View.VISIBLE);
		}else if(text.equals("30mins")){
			ivfifteenSecondActive.setVisibility(View.GONE);
			ivfifteenSecondDeactive.setVisibility(View.VISIBLE);
			
			ivthirtySecondActive.setVisibility(View.GONE);
			ivthirtySecondDeactive.setVisibility(View.VISIBLE);
			
			ivoneMinutsActive.setVisibility(View.GONE);
			ivoneMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtwoMinutsActive.setVisibility(View.GONE);
			ivtwoMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivtenMinutsActive.setVisibility(View.GONE);
			ivtenMinutsDeactive.setVisibility(View.VISIBLE);
			
			ivthirtyMinutsActive.setVisibility(View.VISIBLE);
			ivthirtyMinutsDeactive.setVisibility(View.GONE);
		}
	}

	private void setBrightnessValue() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.choose_brightness);
		dialog.setCancelable(false);
		
		ckChooseAutoMode = (CheckBox) dialog.findViewById(R.id.ckChooseAutoMode);
		tvBrightnessPercentage = (TextView) dialog.findViewById(R.id.tvBrightnessPercentage);
		skBrightnessBar = (SeekBar) dialog.findViewById(R.id.skBrightnessBar);
		RelativeLayout rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);
		RelativeLayout rlOk = (RelativeLayout) dialog.findViewById(R.id.rlOk);
		rlProgressbarValue = (RelativeLayout) dialog.findViewById(R.id.rlProgressbarValue);
		
		setInitValue(tvBrightnessValue.getText().toString());
		
		if(ckChooseAutoMode.isChecked()){
			rlProgressbarValue.setVisibility(View.GONE);
			skBrightnessBar.setVisibility(View.GONE);
		}else{
			skBrightnessBar.setVisibility(View.VISIBLE);
			rlProgressbarValue.setVisibility(View.VISIBLE);
		}
		
		ckChooseAutoMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					rlProgressbarValue.setVisibility(View.GONE);
					skBrightnessBar.setVisibility(View.GONE);
				}else{
					rlProgressbarValue.setVisibility(View.VISIBLE);
					skBrightnessBar.setVisibility(View.VISIBLE);
				}
			}
		});
		
		skBrightnessBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int value = (int) (progress*2.55);
				tvBrightnessPercentage.setText(((int)(value/2.55))+"%");
			}
		});
		
		rlCancle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		
		rlOk.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				if(ckChooseAutoMode.isChecked()){
					tvBrightnessValue.setText("auto");
				}else{
					tvBrightnessValue.setText(tvBrightnessPercentage.getText().toString());
				}				
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}

	private void setInitValue(String text) {
		if(text.equals("auto")){
			ckChooseAutoMode.setChecked(true);
			rlProgressbarValue.setVisibility(View.GONE);
			skBrightnessBar.setVisibility(View.GONE);
		}else{
			int value = (int) (Integer.parseInt(text.split("%")[0])*2.55);
			skBrightnessBar.setMax(100);
			skBrightnessBar.setProgress(((int)(value/2.55)));
			tvBrightnessPercentage.setText(text);
		}
	}
	
}
