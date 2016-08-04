package com.vipdashboard.app.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.CommonConstraints;

public class BatteryAlertNotificationActivity extends Activity {

	EditText etCommandPrompt;
	Button btSendCommand;
	String output = "";

	RelativeLayout rlCancle, rlOk;

	TextView tvSilenceModeHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Initialization();
	}

	private void Initialization() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.battery_doctor_alert_mode);
		dialog.setCancelable(false);

		rlOk = (RelativeLayout) dialog.findViewById(R.id.rlOk);

		rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);

		tvSilenceModeHeader = (TextView) dialog
				.findViewById(R.id.tvSilenceModeHeader);

		tvSilenceModeHeader
				.setText("Battery is below "+CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE);

		rlOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					dialog.dismiss();	
					Intent intent = new Intent(BatteryAlertNotificationActivity.this, VIPD_BatteryDoctor.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);

					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

		rlCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				finish();
			}
		});

		dialog.show();

	}
}
