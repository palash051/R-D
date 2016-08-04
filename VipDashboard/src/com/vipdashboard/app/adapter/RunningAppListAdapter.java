package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.VIPDBatteryDoctorInstalledAppsActivity;
import com.vipdashboard.app.activities.VIPDBatteryDoctorRunningAppsActivity;
import com.vipdashboard.app.activities.VIPDBatteryDoctorWhiteListActivity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RunningAppListAdapter extends ArrayAdapter<ApplicationInfo> {

	private Context context;
	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfoList;
	private ApplicationInfo applicationInfo;

	ImageView ivRunningAppsOn, ivRunningAppsOff;
	RelativeLayout rlRunningApps;

	public RunningAppListAdapter(Context _context, int textViewResourceId,
			ArrayList<ApplicationInfo> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		applicationInfoList = _objects;
		packageManager = context.getPackageManager();
	}

	@Override
	public int getCount() {
		return ((null != applicationInfoList) ? applicationInfoList.size() : 0);
	}

	@Override
	public ApplicationInfo getItem(int position) {
		return ((null != applicationInfoList) ? applicationInfoList
				.get(position) : null);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		applicationInfo = applicationInfoList.get(position);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(
				R.layout.battery_doctor_ruuning_apps_list, null);
		if (null != applicationInfo) {
			TextView tvRunningApps = (TextView) view
					.findViewById(R.id.tvRunningApps);

			ImageView ivRunningApps = (ImageView) view
					.findViewById(R.id.ivRunningApps);
			ivRunningAppsOn = (ImageView) view
					.findViewById(R.id.ivRunningAppsOn);
			ivRunningAppsOff = (ImageView) view
					.findViewById(R.id.ivRunningAppsOff);
			rlRunningApps = (RelativeLayout) view
					.findViewById(R.id.rlRunningApps);

			tvRunningApps.setText(applicationInfo.loadLabel(packageManager));
			ivRunningApps.setImageDrawable(applicationInfo
					.loadIcon(packageManager));
			
			if(VIPDBatteryDoctorRunningAppsActivity.selectedApplicationList!=null)
			{
				for(int count=0;count<VIPDBatteryDoctorRunningAppsActivity.selectedApplicationList.size();count++)
				{
					String currentAppName=String.valueOf(VIPDBatteryDoctorRunningAppsActivity.selectedApplicationList.get(count));
					if(String.valueOf(applicationInfo.loadLabel(packageManager)).equals(currentAppName))
					{
						ivRunningAppsOn.setVisibility(View.GONE);
						ivRunningAppsOff.setVisibility(View.VISIBLE);
					}
				}
			}
			
			if(VIPDBatteryDoctorInstalledAppsActivity.selectedInstalledApplicationList!=null)
			{
				for(int count=0;count<VIPDBatteryDoctorInstalledAppsActivity.selectedInstalledApplicationList.size();count++)
				{
					String currentAppName=String.valueOf(VIPDBatteryDoctorInstalledAppsActivity.selectedInstalledApplicationList.get(count));
					
					if(String.valueOf(applicationInfo.loadLabel(packageManager)).equals(currentAppName))
					{
						ivRunningAppsOn.setVisibility(View.GONE);
						ivRunningAppsOff.setVisibility(View.VISIBLE);
					}
				}
			}

			// rlRunningApps.setOnClickListener(this);

			view.setTag(applicationInfo);

		}
		return view;
	}

	/*
	 * @Override public void onClick(View v) { if (v.getId() ==
	 * R.id.rlRunningApps) { if (ivRunningAppsOff.getVisibility() ==
	 * View.VISIBLE) { ivRunningAppsOff.setVisibility(View.GONE);
	 * ivRunningAppsOn.setVisibility(View.VISIBLE); } else {
	 * ivRunningAppsOff.setVisibility(View.VISIBLE);
	 * ivRunningAppsOn.setVisibility(View.GONE); } }
	 * 
	 * }
	 */

}
