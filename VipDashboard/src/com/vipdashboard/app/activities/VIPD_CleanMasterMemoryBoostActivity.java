package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.ProgressAnimationListener;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CleanMasterMemoryBoostListAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_CleanMasterMemoryBoostActivity extends MainActionbarBase implements OnClickListener,OnItemClickListener {

	SeekArc seekArcRam;

	TextView tvRAMPercentage,ivMemoryBoots;

	private ArrayList<ApplicationInfo> systemApps;

	private CleanMasterMemoryBoostListAdapter adapter;

	public static int totalRunningApps = 0, runningApps = 0;

	ListView lvRunningApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clean_master_memory_boost);
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
		totalRunningApps = runningApps = 0;
		systemApps=new ArrayList<ApplicationInfo>();
		getTotalRAMUse();
		getAllRunningApps();
		SetAdapter();
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	private void Initialization() {
		seekArcRam = (SeekArc) findViewById(R.id.seekArcRam);
		tvRAMPercentage = (TextView) findViewById(R.id.tvRAMPercentage);
		lvRunningApplication = (ListView) findViewById(R.id.lvRunningApplication);
		ivMemoryBoots= (TextView) findViewById(R.id.ivMemoryBoots);
		
		ivMemoryBoots.setOnClickListener(this);
	}

	private void InitalizationOfSeekArc() {
		seekArcRam.setRotation(180);
		seekArcRam.setArcWidth(15);
		seekArcRam.setStartAngle(45);
		seekArcRam.setSweepAngle(270);
		seekArcRam.setTouchInSide(false);
		seekArcRam.setProgressWidth(15);//30
	}

	private void SetAdapter() {
		adapter = new CleanMasterMemoryBoostListAdapter(this,
				R.layout.vipd_appmanager_uninstall_item, systemApps);
		lvRunningApplication.setAdapter(adapter);
	}
	
	private void KillAllRunningApps() {
		try {
			List<ApplicationInfo> packages;
			PackageManager pm;
			pm = getPackageManager();
			// get a list of installed apps.
			packages = pm.getInstalledApplications(0);

			ActivityManager mActivityManager = (ActivityManager) this
					.getSystemService(Context.ACTIVITY_SERVICE);

			for (ApplicationInfo packageInfo : packages) {
			/*	if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
					continue;*/
				if (packageInfo.packageName
						.equals(CommonValues.APPLICATION_PACKAGE_NAME))
					continue;	
				mActivityManager
						.killBackgroundProcesses(packageInfo.packageName);
				runningApps++;
			}
		} catch (Exception e) {
			// Name Not FOund Exception
		}
	}

	private void getAllRunningApps() {
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();

		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				/*CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
						info.processName, PackageManager.GET_META_DATA));*/

				ApplicationInfo ai = pm.getApplicationInfo(info.processName,
						totalRunningApps);

				if (!String.valueOf(info.processName).equals(
						CommonValues.APPLICATION_PACKAGE_NAME)) {
					/*if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
*/
						systemApps.add(ai);
//					}
						totalRunningApps++;
				}
				
			} catch (Exception e) {
				// Name Not FOund Exception
			}
		}
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ivMemoryBoots) {
			/*if(selectedListviewItem>0)
			{*/
				KillAllRunningApps();
				
				//packageManager = this.getPackageManager();
				systemApps = new ArrayList<ApplicationInfo>();
				
				Intent intent = new Intent(this, OptimizationResultActivity.class);
				OptimizationResultActivity.calledFromInfo="MemoryBoots";
				OptimizationResultActivity.optimizationMessage="Memory Boost Successfully.\n"+totalRunningApps+" Applications closed.";
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);

				totalRunningApps = runningApps = 0;
				
				/*Toast.makeText(this, "Memory cleaned successfully", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(this,
						VIPD_CleanMaster.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);*/
	
				//getAllRunningApps();
				//SetAdapter();
			//}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
