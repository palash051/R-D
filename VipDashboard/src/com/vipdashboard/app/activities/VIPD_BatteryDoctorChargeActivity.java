package com.vipdashboard.app.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.customcontrols.CircularProgressBar;
import com.vipdashboard.app.customcontrols.CircularProgressBar.ProgressAnimationListener;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_BatteryDoctorChargeActivity extends MainActionbarBase implements OnClickListener{
	CircularProgressBar circularProgressBar;
	boolean isFirst, isAlwaysProgressCheck;
	int progress,level,status;
	TextView tvChargingState;
	RelativeLayout rlSave,rlMode,rlRank;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_charge);
		Initalization();
		registerBatteryLevelReceiver();
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
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	private void Initalization(){
		circularProgressBar = (CircularProgressBar) findViewById(R.id.circularprogressbar);
		tvChargingState = (TextView)findViewById(R.id.tvChargingState);
		rlSave = (RelativeLayout) findViewById(R.id.rlSave);
		rlMode = (RelativeLayout) findViewById(R.id.rlMode);
		rlRank= (RelativeLayout) findViewById(R.id.rlRank);
		
		rlSave.setOnClickListener(this);
		rlMode.setOnClickListener(this);
		rlRank.setOnClickListener(this);
	}
	
	private void registerBatteryLevelReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);         
		registerReceiver(battery_receiver, filter);
	}
	
	private BroadcastReceiver battery_receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isPresent = intent.getBooleanExtra("present", false);
             int scale = intent.getIntExtra("scale", -1);
             int rawlevel = intent.getIntExtra("level", -1);
             status = intent.getIntExtra("status", 0);
             level = 0;
            
             if(isPresent)
             {
            	 if (rawlevel >= 0 && scale > 0) {
                     level = (rawlevel * 100) / scale;
                     if(level < 100){
                    	 circularProgressBar.animateProgressTo(0, level, new ProgressAnimationListener() {
							
							@Override
							public void onAnimationStart() {
								
							}
							
							@Override
							public void onAnimationProgress(int progress) {
								circularProgressBar.setTitle(progress + "%");
								
							}
							
							@Override
							public void onAnimationFinish() {
			                    circularProgressBar.setSubTitle(getBatteryState(status));
			                    circularProgressBar.setProgress(level);
							}
						});
                     }else {
	                     circularProgressBar.setTitle(level + "%");
	                     circularProgressBar.setSubTitle(getBatteryState(status));
	                     circularProgressBar.setProgress(level);
                     }
                 }
             }
		}
	};
	
	private String getBatteryState(int status) {
		String strStatus = "";
		//status.setBackgroundColor(Color.parseColor("#FF9900"));
		switch (status) {
		case BatteryManager.BATTERY_STATUS_UNKNOWN:
			strStatus = "Unknown Charged";
			break;
		case BatteryManager.BATTERY_STATUS_CHARGING:
			strStatus = "Charged Plugged";
			break;
		case BatteryManager.BATTERY_STATUS_DISCHARGING:
			strStatus = "Charged Unplugged";
			break;
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
			strStatus = "Not Charging";
			break;
		case BatteryManager.BATTERY_STATUS_FULL:
			strStatus = "Charged Completed";
			break;
		}

		return strStatus;
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.rlSave){
			Intent intent = new Intent(this, VIPD_BatteryDoctor.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlMode){
			Intent intent = new Intent(this, VIPD_BatteryDoctorMoreActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if (view.getId() == R.id.rlRank) {
			Intent intentBatteryUsage = new Intent(
					Intent.ACTION_POWER_USAGE_SUMMARY);
			startActivity(intentBatteryUsage);
		} 
	}
	 
}
