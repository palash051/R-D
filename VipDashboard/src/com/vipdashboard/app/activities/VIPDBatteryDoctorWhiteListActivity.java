package com.vipdashboard.app.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorWhiteListActivity extends MainActionbarBase{
	
	ListView listRunningApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_running_app);
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
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
	}

	

}
