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

public class VIPDBatteryDoctorLowPowerSwitchActivity extends MainActionbarBase
		implements OnClickListener {

	RelativeLayout rlLowPowerSwitchStatus, rlWhenpoweris, rlSwitchTo;

	ImageView ivLowPowerSwitchStatusOn, ivLowPowerSwitchStatusOff;

	RelativeLayout ivThirtyPercentageMode, ivTweentyFivePercentageMode,
			ivTweentyPercentageMode, ivFiftheenPercentageMode,
			ivTenPercentageMode, rlCancle,
			ivYourMode,ivsupersaving,ivsilent,
			rlYourMode,rlSuperSaving,rlSilentMode;

	ImageView ivTenPercentageModeActive, ivTenPercentageModeDeactive,
			ivFiftheenPercentageModeActive, ivFiftheenPercentageModeDeactive,
			ivTweentyPercentageModeActive, ivTweentyPercentageModeDeactive,
			ivTweentyFivePercentageModeActive,
			ivTweentyFivePercentageModeDeactive, ivThirtyPercentageModeActive,
			ivThirtyPercentageModeDeactive,
			ivModeActive,ivSuperSavingActive,ivSilentModeActive,
			ivModeDeactive,ivSuperSavingDeactive,ivSilentModeDeactive		
			;

	TextView tvSelectedPercentages,tvTips,tvPowerMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_low_power_switch);
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

	private void BindInitialInfo() {
		if (CommonConstraints.IS_BD_LOW_POWER_SWITCH_NOTIFIATION) {
			ivLowPowerSwitchStatusOn.setVisibility(View.VISIBLE);
			ivLowPowerSwitchStatusOff.setVisibility(View.GONE);
		} else {
			ivLowPowerSwitchStatusOn.setVisibility(View.GONE);
			ivLowPowerSwitchStatusOff.setVisibility(View.VISIBLE);
		}

		tvSelectedPercentages
				.setText(CommonConstraints.BD_LOW_POWER_SWITCH_NOTIFIATION_PERCENTAGE);
		tvTips.setText("Tip: Switch to former mode when power higher than "+ tvSelectedPercentages.getText());
	}

	private void VisibleWhenPanel() {
		if (ivLowPowerSwitchStatusOn.getVisibility() == View.VISIBLE) {
			rlWhenpoweris.setEnabled(true);
			rlSwitchTo.setEnabled(true);
		} else if (ivLowPowerSwitchStatusOff.getVisibility() == View.VISIBLE) {
			rlWhenpoweris.setEnabled(false);
			rlSwitchTo.setEnabled(false);
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

	private void Initialization() {
		rlLowPowerSwitchStatus = (RelativeLayout) findViewById(R.id.rlLowPowerSwitchStatus);
		rlWhenpoweris = (RelativeLayout) findViewById(R.id.rlWhenpoweris);
		rlSwitchTo = (RelativeLayout) findViewById(R.id.rlSwitchTo);
		ivLowPowerSwitchStatusOn = (ImageView) findViewById(R.id.ivLowPowerSwitchStatusOn);
		ivLowPowerSwitchStatusOff = (ImageView) findViewById(R.id.ivLowPowerSwitchStatusOff);
		tvSelectedPercentages = (TextView) findViewById(R.id.tvSelectedPercentages);
		tvPowerMode= (TextView) findViewById(R.id.tvPowerMode);
		tvTips = (TextView) findViewById(R.id.tvTips);

		// bBack = (Button)findViewById(R.id.bBack);
		// bBack.setOnClickListener(this);
		rlLowPowerSwitchStatus.setOnClickListener(this);
		rlWhenpoweris.setOnClickListener(this);
		rlSwitchTo.setOnClickListener(this);
	}
	
	private void SwitchSelectedMenu() {
	
		if (CommonValues.getInstance().SELECTED_SWITCH_TO.equals("Your mode")) {

			  ivModeActive.setVisibility(View.VISIBLE);
			  ivModeDeactive.setVisibility(View.GONE);
			  ivSuperSavingActive.setVisibility(View.GONE);
			  ivSuperSavingDeactive.setVisibility(View.VISIBLE);
			  ivSilentModeActive.setVisibility(View.GONE);
			  ivSilentModeDeactive.setVisibility(View.VISIBLE);
		} else if (CommonValues.getInstance().SELECTED_SWITCH_TO.equals("Super Saving")) {
			  ivModeActive.setVisibility(View.GONE);
			  ivModeDeactive.setVisibility(View.VISIBLE);
			  ivSuperSavingActive.setVisibility(View.VISIBLE);
			  ivSuperSavingDeactive.setVisibility(View.GONE);
			  ivSilentModeActive.setVisibility(View.GONE);
			  ivSilentModeDeactive.setVisibility(View.VISIBLE);
		} else if (CommonValues.getInstance().SELECTED_SWITCH_TO.equals("Silent Mode")) {
			  ivModeActive.setVisibility(View.GONE);
			  ivModeDeactive.setVisibility(View.VISIBLE);
			  ivSuperSavingActive.setVisibility(View.GONE);
			  ivSuperSavingDeactive.setVisibility(View.VISIBLE);
			  ivSilentModeActive.setVisibility(View.VISIBLE);
			  ivSilentModeDeactive.setVisibility(View.GONE);
		} 
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bBack) {
			onBackPressed();
		} else if (view.getId() == R.id.rlLowPowerSwitchStatus) {
			if (ivLowPowerSwitchStatusOn.getVisibility() == View.VISIBLE) {
				ivLowPowerSwitchStatusOn.setVisibility(View.GONE);
				ivLowPowerSwitchStatusOff.setVisibility(View.VISIBLE);
				CommonConstraints.IS_BD_LOW_POWER_SWITCH_NOTIFIATION=false;
			} else if (ivLowPowerSwitchStatusOff.getVisibility() == View.VISIBLE) {
				ivLowPowerSwitchStatusOn.setVisibility(View.VISIBLE);
				ivLowPowerSwitchStatusOff.setVisibility(View.GONE);
				CommonConstraints.IS_BD_LOW_POWER_SWITCH_NOTIFIATION=true;
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
					tvTips.setText("Tip: Switch to former mode when power higher than "+ tvSelectedPercentages.getText());
					CommonConstraints.BD_LOW_POWER_SWITCH_NOTIFIATION_PERCENTAGE = String
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
							tvTips.setText("Tip: Switch to former mode when power higher than "+ tvSelectedPercentages.getText());
							CommonConstraints.BD_LOW_POWER_SWITCH_NOTIFIATION_PERCENTAGE = String
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
					tvTips.setText("Tip: Switch to former mode when power higher than "+ tvSelectedPercentages.getText());
					CommonConstraints.BD_LOW_POWER_SWITCH_NOTIFIATION_PERCENTAGE = String
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
					tvTips.setText("Tip: Switch to former mode when power higher than "+ tvSelectedPercentages.getText());
					CommonConstraints.BD_LOW_POWER_SWITCH_NOTIFIATION_PERCENTAGE = String
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
					tvTips.setText("Tip: Switch to former mode when power higher than "+ tvSelectedPercentages.getText());
					CommonConstraints.BD_LOW_POWER_SWITCH_NOTIFIATION_PERCENTAGE = String
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

		else if (view.getId() == R.id.rlSwitchTo) {
			
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.choose_switch_saving);
			dialog.setCancelable(false);
			
		
			
			
			 rlYourMode = (RelativeLayout) dialog
					.findViewById(R.id.rlYourMode);
			 rlSuperSaving = (RelativeLayout) dialog
					.findViewById(R.id.rlSuperSaving);
			
			 rlSilentMode = (RelativeLayout) dialog
					.findViewById(R.id.rlSilentMode);
		     rlCancle = (RelativeLayout) dialog
					.findViewById(R.id.rlCancle);


			  ivModeActive = (ImageView) dialog
					.findViewById(R.id.ivModeActive);
			 
			  ivModeDeactive = (ImageView) dialog
						.findViewById(R.id.ivModeDeactive);
			 
			  ivSuperSavingActive = (ImageView) dialog
						.findViewById(R.id.ivSuperSavingActive);
			 
			  ivSuperSavingDeactive = (ImageView) dialog
						.findViewById(R.id.ivSuperSavingDeactive);
			 
			  ivSilentModeActive = (ImageView) dialog
						.findViewById(R.id.ivSilentModeActive);
			 
			  ivSilentModeDeactive = (ImageView) dialog
						.findViewById(R.id.ivSilentModeDeactive);
			  
			
				SwitchSelectedMenu();
			
			
			rlYourMode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ivModeActive.setVisibility(View.VISIBLE);
					ivModeDeactive.setVisibility(View.GONE);
					CommonValues.getInstance().SELECTED_SWITCH_TO="Your mode";
					tvPowerMode.setText(String.valueOf(CommonValues.getInstance().SELECTED_SWITCH_TO));
					
					dialog.dismiss();
				}
			});

			rlSuperSaving
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ivSuperSavingActive
									.setVisibility(View.VISIBLE);
							ivSuperSavingDeactive
									.setVisibility(View.GONE);
							
							CommonValues.getInstance().SELECTED_SWITCH_TO="Super Saving";
							
							tvPowerMode.setText(String.valueOf(CommonValues.getInstance().SELECTED_SWITCH_TO));
							
							dialog.dismiss();
						}
					});

			rlSilentMode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ivSilentModeActive.setVisibility(View.VISIBLE);
					ivSilentModeDeactive.setVisibility(View.GONE);
					
					CommonValues.getInstance().SELECTED_SWITCH_TO="Silent Mode";
					
					tvPowerMode.setText(String.valueOf(CommonValues.getInstance().SELECTED_SWITCH_TO));
					
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
