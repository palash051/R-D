package com.vipdashboard.app.activities;

import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.ProgressAnimationListener;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_CleanMaster extends MainActionbarBase implements
		OnClickListener {
	SeekArc seekArcStroages, seekArcRam;
	TextView tvStroagesPercentage, tvStroagesTotalByTotal, tvRAMPercentage;
	RelativeLayout rlAppManager,rlMemoryBoost,rlJunkFiles,rlSecurityAndPrivacy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_cleanmaster);
		Initialization();
		InitalizationOfSeekArc();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		if (CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION) {
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,
					CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
			}
		}
		super.onResume();
		getTotalStorageUse();
		getTotalRAMUse();
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	private void Initialization() {
		seekArcStroages = (SeekArc) findViewById(R.id.seekArcStroages);
		seekArcRam = (SeekArc) findViewById(R.id.seekArcRam);
		tvStroagesPercentage = (TextView) findViewById(R.id.tvStroagesPercentage);
		tvStroagesTotalByTotal = (TextView) findViewById(R.id.tvStroagesTotalByTotal);
		tvRAMPercentage = (TextView) findViewById(R.id.tvRAMPercentage);
		

		rlAppManager = (RelativeLayout) findViewById(R.id.rlAppManager);
		rlMemoryBoost= (RelativeLayout) findViewById(R.id.rlMemoryBoost);
		rlJunkFiles= (RelativeLayout) findViewById(R.id.rlJunkFiles);
		rlSecurityAndPrivacy= (RelativeLayout) findViewById(R.id.rlSecurityAndPrivacy);

		rlAppManager.setOnClickListener(this);
		rlMemoryBoost.setOnClickListener(this);
		rlJunkFiles.setOnClickListener(this);
		rlSecurityAndPrivacy.setOnClickListener(this);
	}

	private void InitalizationOfSeekArc() {
		seekArcStroages.setRotation(180);
		seekArcStroages.setArcWidth(12);//25
		seekArcStroages.setStartAngle(50);
		seekArcStroages.setSweepAngle(255);
		seekArcStroages.setTouchInSide(false);
		seekArcStroages.setProgressWidth(15);//30

		seekArcRam.setRotation(180);
		seekArcRam.setArcWidth(7);//15
		seekArcRam.setStartAngle(45);
		seekArcRam.setSweepAngle(270);
		seekArcRam.setTouchInSide(false);
		seekArcRam.setProgressWidth(15);//30
	}

	private void getTotalStorageUse() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdTotalSize = (double) statFs.getBlockCount()
				* (double) statFs.getBlockSize();
		double sdAvailSize = (double) statFs.getAvailableBlocks()
				* (double) statFs.getBlockSize();
		double sdUsedSize = sdTotalSize - sdAvailSize;

		double totalSizeInGb = Double.parseDouble(String
				.valueOf(new DecimalFormat("##.##")
						.format(sdTotalSize / 1073741824)));
		double usedSizeInGb = Double.parseDouble(String
				.valueOf(new DecimalFormat("##.##")
						.format(sdUsedSize / 1073741824)));
		;

		double percentageInSdCardUsed = Math
				.ceil(((100 * usedSizeInGb) / totalSizeInGb));

		tvStroagesTotalByTotal.setText(usedSizeInGb + " GB/" + totalSizeInGb
				+ "GB");
		seekArcStroages.amimateProcessTo(0, ((int) percentageInSdCardUsed),
				new ProgressAnimationListener() {

					@Override
					public void onAnimationStart() {

					}

					@Override
					public void onAnimationProgress(int progress) {
						seekArcStroages.setProgress(progress);
						tvStroagesPercentage.setText(progress + "%");
					}

					@Override
					public void onAnimationFinish() {

					}
				});
	}

	private void getTotalRAMUse() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		double freeRAM = (double) memoryInfo.availMem;
		double totalRAM = (double) memoryInfo.totalMem;
		double useRAM = totalRAM - freeRAM;

		int usePercentage = (int) ((100 * useRAM) / totalRAM);

		tvRAMPercentage.setText(usePercentage + "%");

		seekArcRam.amimateProcessTo(0, usePercentage,
				new ProgressAnimationListener() {

					@Override
					public void onAnimationStart() {

					}

					@Override
					public void onAnimationProgress(int progress) {
						seekArcRam.setProgress(progress);
					}

					@Override
					public void onAnimationFinish() {

					}
				});
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlAppManager) {
			Intent intent = new Intent(this, VIPD_AppsManager.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
		else if (view.getId() == R.id.rlMemoryBoost) {
			Intent intent = new Intent(this,
					VIPD_CleanMasterMemoryBoostActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if (view.getId() == R.id.rlJunkFiles) {
			Intent intent = new Intent(this,
					History.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
		else if (view.getId() == R.id.rlSecurityAndPrivacy)
		{
			Intent intent = new Intent(this, VIPD_ClearMasterSecurity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}
}
