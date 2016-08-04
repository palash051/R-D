package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.InstalledAppListAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorInstalledAppsActivity extends MainActionbarBase
		implements OnClickListener, OnItemClickListener {

	ListView listRunningApplication;

	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfo;
	private ArrayList<ApplicationInfo> systemApps;

	public static boolean IsComingFromRunningApps = false;

	public static ArrayList<String> selectedInstalledApplicationList = new ArrayList<String>();

	private InstalledAppListAdapter adapter;

	ImageView ivRunningAppsOn, ivRunningAppsOff;

	TextView ivCancel, ivSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_installed_app);

	/*	if (savedInstanceState != null
				&& savedInstanceState.containsKey("ISCOMINGFROMRUNNINGAPPS")) {
			IsComingFromRunningApps = savedInstanceState
					.getBoolean("ISCOMINGFROMRUNNINGAPPS");
		}
*/
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
		Initialization();
		calculateInstallApps();
		setAdapter();

	}

	private void setAdapter() {
		adapter = new InstalledAppListAdapter(this,
				R.layout.battery_doctor_ruuning_apps_list, systemApps);
		listRunningApplication.setAdapter(adapter);
	}

	private void calculateInstallApps() {
		applicationInfo = checkForLaunchIntent(packageManager
				.getInstalledApplications(PackageManager.GET_META_DATA));
	}

	private ArrayList<ApplicationInfo> checkForLaunchIntent(
			List<ApplicationInfo> installedApplications) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo info : installedApplications) {
			try {
				if (null != packageManager
						.getLaunchIntentForPackage(info.packageName)) {
					applist.add(info);
				}
				if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					systemApps.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return applist;
	}

	private void Initialization() {
		listRunningApplication = (ListView) findViewById(R.id.listRunningApplication);
		// ivClearNow = (TextView) findViewById(R.id.ivClearNow);

		ivCancel = (TextView) findViewById(R.id.ivCancel);
		ivSave = (TextView) findViewById(R.id.ivSave);

		packageManager = this.getPackageManager();
		systemApps = new ArrayList<ApplicationInfo>();
		listRunningApplication.setOnItemClickListener(this);
		ivCancel.setOnClickListener(this);
		ivSave.setOnClickListener(this);
		// ivClearNow.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int item, long arg3) {
		Context context = view.getContext();

		TextView tvRunningApps = (TextView) view
				.findViewById(R.id.tvRunningApps);

		ivRunningAppsOn = ((ImageView) view.findViewById(R.id.ivRunningAppsOn));
		ivRunningAppsOff = ((ImageView) view
				.findViewById(R.id.ivRunningAppsOff));

		if (ivRunningAppsOff.getVisibility() == View.VISIBLE) {
			ivRunningAppsOff.setVisibility(View.GONE);
			ivRunningAppsOn.setVisibility(View.VISIBLE);

			selectedInstalledApplicationList.add(String.valueOf(tvRunningApps
					.getText()));

		} else {
			ivRunningAppsOff.setVisibility(View.VISIBLE);
			ivRunningAppsOn.setVisibility(View.GONE);

			selectedInstalledApplicationList.remove(String
					.valueOf(tvRunningApps.getText()));
		}

		// String listItemId = textViewItem.getTag().toString();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ivSave || v.getId() == R.id.ivCancel) {
			if (!IsComingFromRunningApps) {

				Intent intent = new Intent(this,
						VIPDBatteryDoctorSmartSavingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this,
						VIPDBatteryDoctorRunningAppsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);

			}
		}
	}

	// Bundle bundle = getIntent().getExtras();

	// @Override
	// public void onClick(View v) {
	// if (v.getId() == R.id.ivSave) {
	// Intent intent = new Intent(this,
	// VIPDBatteryDoctorRunningAppsActivity.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	// startActivity(intent);
	// }
	// }

}
