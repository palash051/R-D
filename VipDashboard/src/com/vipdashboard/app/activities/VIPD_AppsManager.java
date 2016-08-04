package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AppListAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_AppsManager extends MainActionbarBase implements OnClickListener {
	
	TextView tvInstallApps,tvUsedSpace;
	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfo;
	private ArrayList<ApplicationInfo> systemApps;
	RelativeLayout rlManageApp, rlInstall, rlUnInstall;
	private AppListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager);
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
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		super.onResume();
		calculateInstallApps();
		calculateStroages();
	}
	
	private void Initialization(){
		tvInstallApps = (TextView)findViewById(R.id.tvInstallApps);
		tvUsedSpace = (TextView)findViewById(R.id.tvUsedSpace);
		rlManageApp = (RelativeLayout)findViewById(R.id.rlManageApp);
		rlInstall = (RelativeLayout)findViewById(R.id.rlInstall);
		rlUnInstall = (RelativeLayout)findViewById(R.id.rlUnInstall);
		packageManager = this.getPackageManager();
		systemApps = new ArrayList<ApplicationInfo>();
		
		rlManageApp.setOnClickListener(this);
		rlInstall.setOnClickListener(this);
		rlUnInstall.setOnClickListener(this);
	}
	
	private void calculateInstallApps(){
		applicationInfo = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
		tvInstallApps.setText("Installed apps: "+applicationInfo.size());
	}
	
	private ArrayList<ApplicationInfo> checkForLaunchIntent(
			List<ApplicationInfo> installedApplications) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : installedApplications) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
                if((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                	systemApps.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
        return applist;
	}
	
	private void calculateStroages(){
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
		long megAvailable = bytesAvailable / (1024 * 1024);
		//long totalAvailable = (long)stat.getFreeBlocks()*(long)stat.getBlockSize();
		//double userPercentage = (totalAvailable-bytesAvailable)/100;
		//tvUsedSpace.setText("Used space: "+userPercentage+", Remaining space: "+megAvailable+"MB");
		tvUsedSpace.setText("Remaining space: "+megAvailable+"MB");
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.rlManageApp){
			Intent intent = new Intent(this, VIPD_ManageApps.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlInstall){
			//Toast.makeText(this, "", duration)
			
		}else if(view.getId() == R.id.rlUnInstall){
			SystemApps();
		}
	}

	private void SystemApps() {
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.manage_app);		
		ListView lvManageApps = (ListView) dialog.findViewById(R.id.lvManageApps);
		adapter = new AppListAdapter(this,
                R.layout.app_list_item_layout, systemApps);
		lvManageApps.setAdapter(adapter);
		dialog.show();
	}
	

}
