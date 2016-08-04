package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AppListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_ManageApps extends MainActionbarBase implements OnItemClickListener, IAsynchronousTask {
	
	ListView lvManageApps;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfo;
	private AppListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_app);
		Initialization();
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
		LoadInformation();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
		
	}
	
	private void Initialization(){
		lvManageApps = (ListView) findViewById(R.id.lvManageApps);
		packageManager = this.getPackageManager();
		
		lvManageApps.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
		ApplicationInfo applicationInfo =  (ApplicationInfo) view.getTag();
		
		Uri packageUri = Uri.parse("package:"+applicationInfo.packageName);
        Intent uninstallIntent =
          new Intent(Intent.ACTION_DELETE, packageUri);
        startActivity(uninstallIntent);
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Processing...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		applicationInfo = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
		return applicationInfo;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){			
			adapter = new AppListAdapter(this,
	                R.layout.app_list_item_layout, applicationInfo);
			lvManageApps.setAdapter(adapter);
		}
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
	}
	
	private ArrayList<ApplicationInfo> checkForLaunchIntent(
			List<ApplicationInfo> installedApplications) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : installedApplications) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 
        return applist;
	}
	
	

}
