package com.vipdashboard.app.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorLowPowerNotificationActivity extends
		MainActionbarBase implements OnClickListener {

	RelativeLayout rlLowPowerNotificationStatus, rlWhenpoweris;
	TextView tvSelectedPercentages;

	ImageView ivLowPowerNotificationStatusOn, ivLowPowerNotificationStatusOff;

	RelativeLayout ivThirtyPercentageMode, ivTweentyFivePercentageMode,
			ivTweentyPercentageMode, ivFiftheenPercentageMode,
			ivTenPercentageMode, rlCancle;

	ImageView ivTenPercentageModeActive, ivTenPercentageModeDeactive,
			ivFiftheenPercentageModeActive, ivFiftheenPercentageModeDeactive,
			ivTweentyPercentageModeActive, ivTweentyPercentageModeDeactive,
			ivTweentyFivePercentageModeActive,
			ivTweentyFivePercentageModeDeactive, ivThirtyPercentageModeActive,
			ivThirtyPercentageModeDeactive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_low_power_notification);
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
		  if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		super.onResume();

		VisibleWhenPanel();
		BindInitialInfo();
	}

	private void Initialization() {
		rlLowPowerNotificationStatus = (RelativeLayout) findViewById(R.id.rlLowPowerNotificationStatus);
		rlWhenpoweris = (RelativeLayout) findViewById(R.id.rlWhenpoweris);
		ivLowPowerNotificationStatusOn = (ImageView) findViewById(R.id.ivLowPowerNotificationStatusOn);
		ivLowPowerNotificationStatusOff = (ImageView) findViewById(R.id.ivLowPowerNotificationStatusOff);
		tvSelectedPercentages = (TextView) findViewById(R.id.tvSelectedPercentages);
		// bBack = (Button)findViewById(R.id.bBack);
		// bBack.setOnClickListener(this);
		rlLowPowerNotificationStatus.setOnClickListener(this);
		rlWhenpoweris.setOnClickListener(this);
	}

	private void BindInitialInfo() {
		if (CommonConstraints.IS_BD_LOW_POWER_NOTIFIATION) {
			ivLowPowerNotificationStatusOn.setVisibility(View.VISIBLE);
			ivLowPowerNotificationStatusOff.setVisibility(View.GONE);
		} else {
			ivLowPowerNotificationStatusOn.setVisibility(View.GONE);
			ivLowPowerNotificationStatusOff.setVisibility(View.VISIBLE);
		}

		tvSelectedPercentages
				.setText(CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE);
	}

	private void VisibleWhenPanel() {
		if (ivLowPowerNotificationStatusOn.getVisibility() == View.VISIBLE) {
			rlWhenpoweris.setEnabled(true);
		} else if (ivLowPowerNotificationStatusOff.getVisibility() == View.VISIBLE) {
			rlWhenpoweris.setEnabled(false);
		}
	}

	private void ChoseSelectedMenu() {
		if (tvSelectedPercentages.getText().equals("10%")) {
			ivTenPercentageModeActive.setVisibility(View.VISIBLE);
			ivTenPercentageModeDeactive.setVisibility(View.GONE);
		} else if (tvSelectedPercentages.getText().equals("15%")) {
			ivFiftheenPercentageModeActive.setVisibility(View.VISIBLE);
			ivFiftheenPercentageModeDeactive.setVisibility(View.GONE);
		} else if (tvSelectedPercentages.getText().equals("20%")) {
			ivTweentyPercentageModeActive.setVisibility(View.VISIBLE);
			ivTweentyPercentageModeDeactive.setVisibility(View.GONE);
		} else if (tvSelectedPercentages.getText().equals("25%")) {
			ivTweentyFivePercentageModeActive.setVisibility(View.VISIBLE);
			ivTweentyFivePercentageModeDeactive.setVisibility(View.GONE);
		} else if (tvSelectedPercentages.getText().equals("30%")) {
			ivThirtyPercentageModeActive.setVisibility(View.VISIBLE);
			ivThirtyPercentageModeDeactive.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bBack) {
			onBackPressed();
		} else if (view.getId() == R.id.rlLowPowerNotificationStatus) {

			if (ivLowPowerNotificationStatusOn.getVisibility() == View.VISIBLE) {
				ivLowPowerNotificationStatusOn.setVisibility(View.GONE);
				ivLowPowerNotificationStatusOff.setVisibility(View.VISIBLE);
				CommonConstraints.IS_BD_LOW_POWER_NOTIFIATION = false;

			} else if (ivLowPowerNotificationStatusOff.getVisibility() == View.VISIBLE) {
				ivLowPowerNotificationStatusOn.setVisibility(View.VISIBLE);
				ivLowPowerNotificationStatusOff.setVisibility(View.GONE);
				CommonConstraints.IS_BD_LOW_POWER_NOTIFIATION = true;
			}

			VisibleWhenPanel();
		}

		else if (view.getId() == R.id.rlWhenpoweris) {

			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.choose_power_percentages);
			dialog.setCancelable(false);

			ivThirtyPercentageMode = (RelativeLayout) dialog
					.findViewById(R.id.ivThirtyPercentageMode);
			ivTweentyFivePercentageMode = (RelativeLayout) dialog
					.findViewById(R.id.ivTweentyFivePercentageMode);
			ivTweentyPercentageMode = (RelativeLayout) dialog
					.findViewById(R.id.ivTweentyPercentageMode);
			ivFiftheenPercentageMode = (RelativeLayout) dialog
					.findViewById(R.id.ivFiftheenPercentageMode);
			ivTenPercentageMode = (RelativeLayout) dialog
					.findViewById(R.id.ivTenPercentageMode);

			rlCancle = (RelativeLayout) dialog.findViewById(R.id.rlCancle);

			ivTenPercentageModeActive = (ImageView) dialog
					.findViewById(R.id.ivTenPercentageModeActive);
			ivTenPercentageModeDeactive = (ImageView) dialog
					.findViewById(R.id.ivTenPercentageModeDeactive);
			ivFiftheenPercentageModeActive = (ImageView) dialog
					.findViewById(R.id.ivFiftheenPercentageModeActive);
			ivFiftheenPercentageModeDeactive = (ImageView) dialog
					.findViewById(R.id.ivFiftheenPercentageModeDeactive);
			ivTweentyPercentageModeActive = (ImageView) dialog
					.findViewById(R.id.ivTweentyPercentageModeActive);
			ivTweentyPercentageModeDeactive = (ImageView) dialog
					.findViewById(R.id.ivTweentyPercentageModeDeactive);
			ivTweentyFivePercentageModeActive = (ImageView) dialog
					.findViewById(R.id.ivTweentyFivePercentageModeActive);
			ivTweentyFivePercentageModeDeactive = (ImageView) dialog
					.findViewById(R.id.ivTweentyFivePercentageModeDeactive);
			ivThirtyPercentageModeActive = (ImageView) dialog
					.findViewById(R.id.ivThirtyPercentageModeActive);
			ivThirtyPercentageModeDeactive = (ImageView) dialog
					.findViewById(R.id.ivThirtyPercentageModeDeactive);

			ChoseSelectedMenu();

			ivThirtyPercentageMode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ivThirtyPercentageModeActive.setVisibility(View.VISIBLE);
					ivThirtyPercentageModeDeactive.setVisibility(View.GONE);
					tvSelectedPercentages.setText("30%");
					CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE = String
							.valueOf(tvSelectedPercentages.getText());
					dialog.dismiss();
				}
			});

			ivTweentyFivePercentageMode
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ivTweentyFivePercentageModeActive
									.setVisibility(View.VISIBLE);
							ivTweentyFivePercentageModeDeactive
									.setVisibility(View.GONE);
							tvSelectedPercentages.setText("25%");
							CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE = String
									.valueOf(tvSelectedPercentages.getText());
							dialog.dismiss();
						}
					});

			ivTweentyPercentageMode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ivTweentyPercentageModeActive.setVisibility(View.VISIBLE);
					ivTweentyPercentageModeDeactive.setVisibility(View.GONE);
					tvSelectedPercentages.setText("20%");
					CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE = String
							.valueOf(tvSelectedPercentages.getText());
					dialog.dismiss();
				}
			});

			ivFiftheenPercentageMode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ivFiftheenPercentageModeActive.setVisibility(View.VISIBLE);
					ivFiftheenPercentageModeDeactive.setVisibility(View.GONE);
					tvSelectedPercentages.setText("15%");
					CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE = String
							.valueOf(tvSelectedPercentages.getText());
					dialog.dismiss();
				}
			});

			ivTenPercentageMode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ivTenPercentageModeActive.setVisibility(View.VISIBLE);
					ivTenPercentageModeDeactive.setVisibility(View.GONE);
					tvSelectedPercentages.setText("10%");
					CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE = String
							.valueOf(tvSelectedPercentages.getText());
					dialog.dismiss();
				}
			});

			rlCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		}
	}
}
