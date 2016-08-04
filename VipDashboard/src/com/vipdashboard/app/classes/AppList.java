package com.vipdashboard.app.classes;

import java.util.ArrayList;
import java.util.List;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.AssistanceReportDetalisActivity;
import com.vipdashboard.app.adapter.AppListAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AppList implements OnItemClickListener {
	
	private Context context;
	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfo;
	private AppListAdapter adapter;
	public static String AppName= "";
	Dialog dialog;
	public AppList(Context _context) {
		this.context = _context;
		packageManager = this.context.getPackageManager();
	}
	
	public void showAppList(){
		dialog = new Dialog(this.context);
		dialog.setTitle("Apps List");
		dialog.setContentView(R.layout.health_check_routines);
		ListView healthCheckList = (ListView) dialog.findViewById(R.id.lvHelthCheckRoutines);
		
		applicationInfo = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
		adapter = new AppListAdapter(this.context,
                R.layout.app_list_item_layout, applicationInfo);
		healthCheckList.setAdapter(adapter);
		healthCheckList.setOnItemClickListener(this);
		
		dialog.show();
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

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		ApplicationInfo applicationInfo =  (ApplicationInfo) view.getTag();
		AppName = (String) applicationInfo.loadLabel(packageManager);
		
		if(AssistanceReportDetalisActivity.tvchooseApplication != null)
			AssistanceReportDetalisActivity.tvchooseApplication.setText(AppName);
		
		dialog.dismiss();
	}

}