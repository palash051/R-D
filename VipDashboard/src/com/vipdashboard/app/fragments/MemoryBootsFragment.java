package com.vipdashboard.app.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.ProgressAnimationListener;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CleanMasterMemoryBoostListAdapter;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MemoryBootsFragment extends Fragment implements OnClickListener {

	private ArrayList<ApplicationInfo> systemApps;

	private CleanMasterMemoryBoostListAdapter adapter;

	public static int totalRunningApps = 0, runningApps = 0;

	ListView lvRunningApplication;

	TextView ivMemoryBoots, tvRAMPercentage;

	Context context;

	SeekArc seekArcRam;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.clean_master_memory_boost, container, false);
		lvRunningApplication = (ListView) root
				.findViewById(R.id.lvRunningApplication);
		ivMemoryBoots = (TextView) root.findViewById(R.id.ivMemoryBoots);
		tvRAMPercentage = (TextView) root.findViewById(R.id.tvRAMPercentage);
		seekArcRam = (SeekArc) root.findViewById(R.id.seekArcRam);

		ivMemoryBoots.setOnClickListener(this);

		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		if (CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION) {
			CommonTask.DryConnectivityMessage(context,
					CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
		}
		super.onResume();
		totalRunningApps = runningApps = 0;
		systemApps = new ArrayList<ApplicationInfo>();
		InitalizationOfSeekArc();
		getTotalRAMUse();
		getAllRunningApps();
		SetAdapter();
	}

	private void InitalizationOfSeekArc() {
		seekArcRam.setRotation(180);
		seekArcRam.setArcWidth(15);
		seekArcRam.setStartAngle(45);
		seekArcRam.setSweepAngle(270);
		seekArcRam.setTouchInSide(false);
		seekArcRam.setProgressWidth(30);
	}

	private void SetAdapter() {
		adapter = new CleanMasterMemoryBoostListAdapter(getActivity(),
				R.layout.vipd_appmanager_uninstall_item, systemApps);
		lvRunningApplication.setAdapter(adapter);
	}

	private void getTotalRAMUse() {
		ActivityManager activityManager = (ActivityManager) getActivity()
				.getSystemService(context.ACTIVITY_SERVICE);
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

	private void KillAllRunningApps() {
		try {
			List<ApplicationInfo> packages;
			PackageManager pm;
			pm = getActivity().getPackageManager();
			// get a list of installed apps.
			packages = pm.getInstalledApplications(0);

			ActivityManager mActivityManager = (ActivityManager) getActivity()
					.getSystemService(context.ACTIVITY_SERVICE);

			for (ApplicationInfo packageInfo : packages) {
				/*
				 * if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
				 * continue;
				 */
				if (packageInfo.packageName
						.equals(CommonValues.APPLICATION_PACKAGE_NAME))
					continue;
				mActivityManager
						.killBackgroundProcesses(packageInfo.packageName);
			}
		} catch (Exception e) {
			// Name Not FOund Exception
		}
	}

	private void getAllRunningApps() {
		ActivityManager am = (ActivityManager) getActivity().getSystemService(
				context.ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = getActivity().getPackageManager();

		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				/*
				 * CharSequence c =
				 * pm.getApplicationLabel(pm.getApplicationInfo(
				 * info.processName, PackageManager.GET_META_DATA));
				 */

				ApplicationInfo ai = pm.getApplicationInfo(info.processName,
						totalRunningApps);

				if (!String.valueOf(info.processName).equals(
						CommonValues.APPLICATION_PACKAGE_NAME)) {
					/*
					 * if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
					 */
					systemApps.add(ai);
					// }
				}
				totalRunningApps++;
			} catch (Exception e) {
				// Name Not FOund Exception
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ivMemoryBoots) {
			KillAllRunningApps();
			totalRunningApps = runningApps = 0;
			// packageManager = this.getPackageManager();
			systemApps = new ArrayList<ApplicationInfo>();

			Toast.makeText(context, "Memory cleaned successfully",
					Toast.LENGTH_SHORT).show();
		}
	}

}
