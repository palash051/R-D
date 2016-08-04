package com.vipdashboard.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorSmartSavingActivity extends MainActionbarBase
		implements OnClickListener {

	RelativeLayout rlLowPowerNotificationStatus, rlLowPowerSwitch,
			rlCPUManagement, rlMemoryWhitelist, rlMoreOptions;

	TextView tvLowPowerNotificationStatus, tvLowPowerSwitchStatus,
			tvCPUManagementStatus,tvSelectedAppInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_smart_saving);
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
		if (CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION) {
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,
					CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
			}
		}
		super.onResume();
		BindInitialInfo();
	}

	private void BindInitialInfo() {
		tvLowPowerNotificationStatus.setTextColor(Color.parseColor("#FFFFFF"));
		tvLowPowerSwitchStatus.setTextColor(Color.parseColor("#FFFFFF"));
		tvLowPowerNotificationStatus.setText("OFF");
		tvLowPowerSwitchStatus.setText("OFF");

		if (CommonConstraints.IS_BD_LOW_POWER_NOTIFIATION) {
			tvLowPowerNotificationStatus.setText("ON");
			tvLowPowerNotificationStatus.setTextColor(Color
					.parseColor("#000000"));
		}
		if (CommonConstraints.IS_BD_LOW_POWER_SWITCH_NOTIFIATION) {
			tvLowPowerSwitchStatus.setText("ON");
			tvLowPowerSwitchStatus.setTextColor(Color.parseColor("#000000"));
		}
		
		tvSelectedAppInfo.setText("Selected "+VIPDBatteryDoctorInstalledAppsActivity.selectedInstalledApplicationList.size()+" app(s)");

		// CPU Management Will come here.
		// tvCPUManagementStatus;
	}

	private void Initialization() {
		rlLowPowerNotificationStatus = (RelativeLayout) findViewById(R.id.rlLowPowerNotificationStatus);
		rlLowPowerSwitch = (RelativeLayout) findViewById(R.id.rlLowPowerSwitch);
		rlCPUManagement = (RelativeLayout) findViewById(R.id.rlCPUManagement);
		rlMemoryWhitelist = (RelativeLayout) findViewById(R.id.rlMemoryWhitelist);
		rlMoreOptions = (RelativeLayout) findViewById(R.id.rlMoreOptions);
		tvLowPowerNotificationStatus = (TextView) findViewById(R.id.tvLowPowerNotificationStatus);
		tvLowPowerSwitchStatus = (TextView) findViewById(R.id.tvLowPowerSwitchStatus);
		tvCPUManagementStatus = (TextView) findViewById(R.id.tvCPUManagementStatus);
		tvSelectedAppInfo = (TextView) findViewById(R.id.tvSelectedAppInfo);
		

		// bBack = (Button)findViewById(R.id.bBack);
		// bBack.setOnClickListener(this);
		rlLowPowerNotificationStatus.setOnClickListener(this);
		rlLowPowerSwitch.setOnClickListener(this);
		rlCPUManagement.setOnClickListener(this);
		rlMemoryWhitelist.setOnClickListener(this);
		rlMoreOptions.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bBack) {
			onBackPressed();
		} else if (view.getId() == R.id.rlLowPowerNotificationStatus) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorLowPowerNotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlLowPowerSwitch) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorLowPowerSwitchActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlCPUManagement) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorCPUManagementActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlMemoryWhitelist) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorInstalledAppsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			VIPDBatteryDoctorInstalledAppsActivity.IsComingFromRunningApps = false;
			startActivity(intent);
		} else if (view.getId() == R.id.rlMoreOptions) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorMoreOptionsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
}
