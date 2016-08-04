package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.RunningAppListAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorRunningAppsActivity extends MainActionbarBase
		implements OnClickListener, OnItemClickListener {

	ListView listRunningApplication;
	TextView ivClearNow;
	ImageView ivcwhitelist;

	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfo;
	private ArrayList<ApplicationInfo> systemApps;

	private RunningAppListAdapter adapter;
	
	public static ArrayList<String> selectedApplicationList; 

	public static int totalRunningApps = 0, runningApps = 0,
			selectedListviewItem = 0;

	ImageView ivRunningAppsOn, ivRunningAppsOff;

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
		selectedApplicationList=new ArrayList<String>();
		totalRunningApps = runningApps = selectedListviewItem = 0;
		Initialization();
		ivClearNow.setBackgroundColor(Color.parseColor("#FF0000"));
		
	}

	private void SetAdapter() {
		adapter = new RunningAppListAdapter(this,
				R.layout.battery_doctor_ruuning_apps_list, systemApps);
		listRunningApplication.setAdapter(adapter);
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
				
				CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
						info.processName, PackageManager.GET_META_DATA));

				ApplicationInfo ai = pm.getApplicationInfo(info.processName,
						totalRunningApps);

				if (!String.valueOf(info.processName).equals(
						CommonValues.APPLICATION_PACKAGE_NAME)) {
					if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {

						systemApps.add(ai);
						runningApps++;
					}
				}

				totalRunningApps++;
			} catch (Exception e) {
				// Name Not FOund Exception
			}
		}
		selectedListviewItem = runningApps;
		//ivClearNow.setText("Clean Now(" + runningApps + ")");

	}

	private void Initialization() {
		listRunningApplication = (ListView) findViewById(R.id.listRunningApplication);
		ivClearNow = (TextView) findViewById(R.id.ivClearNow);
		ivcwhitelist=(ImageView)findViewById(R.id.ivcwhitelist);

		packageManager = this.getPackageManager();
		systemApps = new ArrayList<ApplicationInfo>();

		getAllRunningApps();
		SetAdapter();

		ivClearNow.setOnClickListener(this);
		ivcwhitelist.setOnClickListener(this);
		listRunningApplication.setOnItemClickListener(this);
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
				if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
					continue;
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ivClearNow) {
			if(selectedListviewItem>0)
			{
				KillAllRunningApps();
				totalRunningApps = runningApps = 0;
				packageManager = this.getPackageManager();
				systemApps = new ArrayList<ApplicationInfo>();
	
				getAllRunningApps();
				SetAdapter();
				
				Intent intent = new Intent(this, VIPDBatteryDoctorTaptoDiagnoseActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(this, VIPDBatteryDoctorTaptoDiagnoseActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		}
		
		if(v.getId() == R.id.ivcwhitelist){
			
			Intent intent = new Intent(VIPDBatteryDoctorRunningAppsActivity.this, VIPDBatteryDoctorInstalledAppsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			VIPDBatteryDoctorInstalledAppsActivity.IsComingFromRunningApps=true;
			/*intent.putExtra("ISCOMINGFROMRUNNINGAPPS", true);*/
			startActivity(intent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int item, long arg3) {
		Context context = view.getContext();
		TextView tvRunningApps= (TextView) view.findViewById(R.id.tvRunningApps);
		
		ivRunningAppsOn = ((ImageView) view.findViewById(R.id.ivRunningAppsOn));
		ivRunningAppsOff = ((ImageView) view 
				.findViewById(R.id.ivRunningAppsOff));

		if (ivRunningAppsOff.getVisibility() == View.VISIBLE) {
			ivRunningAppsOff.setVisibility(View.GONE);
			ivRunningAppsOn.setVisibility(View.VISIBLE);
			selectedListviewItem++;
			
			selectedApplicationList.remove(String.valueOf(tvRunningApps.getText()));
			
		} else {
			ivRunningAppsOff.setVisibility(View.VISIBLE);
			ivRunningAppsOn.setVisibility(View.GONE);
			selectedListviewItem--;
			
			selectedApplicationList.add(String.valueOf(tvRunningApps.getText()));
			
		}

		ivClearNow.setText("Clean Now(" + selectedListviewItem + ")");
		ivClearNow.setBackgroundColor(Color.parseColor("#FF0000"));
		
		if(selectedListviewItem==0)
		{
			ivClearNow.setText("Back");
			ivClearNow.setBackgroundColor(Color.parseColor("#10B608"));
		}

		// String listItemId = textViewItem.getTag().toString();
	}

}
