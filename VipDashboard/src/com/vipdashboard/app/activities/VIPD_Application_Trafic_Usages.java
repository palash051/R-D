package com.vipdashboard.app.activities;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.vipdashboard.app.adapter.AllCallsAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

//import com.vipdashboard.R;

import com.vipdashboard.app.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SearchView.OnQueryTextListener;

public class VIPD_Application_Trafic_Usages extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask, OnItemClickListener,
		OnQueryTextListener {
	HashMap<Integer, Long> mMap = null;

	TextView tvInstallationDate, Chart;
	DownloadableAsyncTask downloadableAsyncTask;

	ImageView ivStatisticsChartOverview;

	ArrayList<PhoneCallInformation> phoneCallSummeryListByTotal,
			phoneCallSummeryListNyNumber;

	private static final int DOWNLOAD_TOTAL_CALL_SUMMERY = 0;
	int DOWNLOAD_TYPE = DOWNLOAD_TOTAL_CALL_SUMMERY;

	TextView tvVIPDMaps, tvVIPNetwork, tvVIPServices, tvApplication, tvUsage,tvSpeedTest;

	AllCallsAdapter adapter;
	Date date;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.vipd_application_trafic_usages);
		
		
		
		initialization();
		tvInstallationDate.append("" + CommonTask.getCurrentDateTimeAsString());

	}

	private void initialization() {

		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		tvInstallationDate = (TextView) findViewById(R.id.tvTrafficUsages);
		ivStatisticsChartOverview = (ImageView) findViewById(R.id.ivDataUsages);

		tvVIPDMaps = (TextView) findViewById(R.id.tvVIPDMaps);
		tvVIPNetwork = (TextView) findViewById(R.id.tvVIPNetwork);

		tvVIPServices = (TextView) findViewById(R.id.tvVIPServices);
		tvApplication = (TextView) findViewById(R.id.tvApplication);
		tvUsage = (TextView) findViewById(R.id.tvUsage);
		tvSpeedTest  = (TextView) findViewById(R.id.tvSpeedTest );

		tvVIPDMaps.setOnClickListener(this);
		tvVIPNetwork.setOnClickListener(this);
		tvVIPServices.setOnClickListener(this);
		tvApplication.setOnClickListener(this);
		tvUsage.setOnClickListener(this);
		tvSpeedTest.setOnClickListener(this);

		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
			if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
		arrangeOverviewTab();
		super.onResume();
	}

	private void arrangeAllTab() {
		downloadableAsyncTask = new DownloadableAsyncTask(
				VIPD_Application_Trafic_Usages.this);
		downloadableAsyncTask.execute();
	}

	public static long getAppLastUpdateTime(Context context) {

		try {
			if (Build.VERSION.SDK_INT > 8/* Build.VERSION_CODES.FROYO */) {
				long installed = context.getPackageManager().getPackageInfo(
						"com.mynet.app.name", 0).firstInstallTime;
				return installed;
			} else {
				ApplicationInfo appInfo = context.getPackageManager()
						.getApplicationInfo(context.getPackageName(), 0);
				String sAppFile = appInfo.sourceDir;
				return new File(sAppFile).lastModified();
			}
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	private void arrangeOverviewTab() {
		DOWNLOAD_TYPE = DOWNLOAD_TOTAL_CALL_SUMMERY;
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		arrangeAllTab();
	}

	@Override
	public Object doBackgroundPorcess() {

		// Toast.makeText(this, "here", Toast.LENGTH_LONG).show();
		ApplicationInfo applicationInfo = null;
		PackageManager pm = getApplicationContext().getPackageManager();

		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> runningProcesses = manager
				.getRunningAppProcesses();

		List<String> Applicationlist = new ArrayList<String>();
		List<String> ApplicationHaveData  = new ArrayList<String>();
		
		for(int j=0;j<7;j++){
			ApplicationHaveData.add("null");
		}
		
		Collections.sort(runningProcesses,
				new Comparator<RunningAppProcessInfo>() {
					@Override
					public int compare(RunningAppProcessInfo s1,
							RunningAppProcessInfo s2) {

						long send1 = TrafficStats.getUidTxBytes(s1.uid);
						long recived1 = TrafficStats.getUidRxBytes(s1.uid);

						long val1 = (send1 + recived1) / 1024;

						long send2 = TrafficStats.getUidTxBytes(s2.uid);
						long recived2 = TrafficStats.getUidRxBytes(s2.uid);

						long val2 = (send2 + recived2) / 1024;

						if (val1 > val2)
							return -1;
						if (val2 > val1)
							return 1;
						return 0;
					}
				});
		
		/*int i=0,CurrentProcessuid=0,PreviousProcessuid=0;
		double CurrentdataSize=0,PreviousdataSize=0;
		for (RunningAppProcessInfo runningAppProcessInfo : runningProcesses) {
			try {
				applicationInfo = pm.getApplicationInfo(
						pm.getNameForUid(runningAppProcessInfo.uid), 0);
			} catch (final NameNotFoundException e) {
			}
			if(applicationInfo!=null){
				CurrentdataSize=TrafficStats.getUidTxBytes(runningProcesses.get(i).uid) 
								+ TrafficStats.getUidRxBytes(runningProcesses.get(i).uid);
				
				CurrentProcessuid=runningProcesses.get(i).uid;
				if(CurrentdataSize/1024>0){
					if(CurrentProcessuid==PreviousProcessuid){
						PreviousdataSize+=CurrentdataSize;
					}
					else {
						ApplicationHaveData.add((String) pm.getApplicationLabel(applicationInfo));
						PreviousdataSize=CurrentProcessuid;
					}
				}
				
				i++;
			}
			
		}*/
		
		
		
		

		for (RunningAppProcessInfo runningAppProcessInfo : runningProcesses) {
			try {
				applicationInfo = pm.getApplicationInfo(
						pm.getNameForUid(runningAppProcessInfo.uid), 0);
			} catch (final NameNotFoundException e) {
			}
			if(applicationInfo!=null){
				
				Applicationlist.add((String) pm.getApplicationLabel(applicationInfo));
			}
			//final String title =(String)((applicationInfo != null) ? pm.getApplicationLabel(applicationInfo) : "???");
		}
		/*
		 * try { applicationInfo =
		 * pm.getApplicationInfo(pm.getNameForUid(runningProcesses.get(0).uid),
		 * 0); } catch (final NameNotFoundException e) {} final String title =
		 * (String)((applicationInfo != null) ?
		 * pm.getApplicationLabel(applicationInfo) : "???");
		 */

		try {
			String urlRqs3DPie = String
					.format(CommonURL.getInstance().GoogleChartServiceUsageNew,
							URLEncoder.encode(
									""
											+ Applicationlist.get(0)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(0).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(0).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),
							URLEncoder.encode(
									""
											+ Applicationlist.get(1)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(1).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(1).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),
							URLEncoder.encode(
									""
											+ Applicationlist.get(2)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(2).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(2).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),
							URLEncoder.encode(
									""
											+ Applicationlist.get(3)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(3).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(3).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),
							URLEncoder.encode(
									""
											+ Applicationlist.get(4)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(4).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(4).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),
							URLEncoder.encode(
									""
											+ Applicationlist.get(5)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(5).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(5).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),
							URLEncoder.encode(
									""
											+ Applicationlist.get(6)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(6).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(6).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),
							URLEncoder.encode(
									""
											+ Applicationlist.get(7)
											+ "     \t"
											+ String.valueOf((TrafficStats
													.getUidTxBytes(runningProcesses
															.get(7).uid) + TrafficStats
													.getUidRxBytes(runningProcesses
															.get(7).uid)) / 1024)
											+ "( KB)",
									CommonConstraints.EncodingCode),

							runningProcesses.get(0).uid,
							runningProcesses.get(1).uid,
							runningProcesses.get(2).uid,
							runningProcesses.get(3).uid,
							runningProcesses.get(4).uid,
							runningProcesses.get(5).uid,
							runningProcesses.get(6).uid,
							runningProcesses.get(7).uid,
							runningProcesses.get(0).uid,
							runningProcesses.get(1).uid,
							runningProcesses.get(2).uid,
							runningProcesses.get(3).uid,
							runningProcesses.get(4).uid,
							runningProcesses.get(5).uid,
							runningProcesses.get(6).uid,
							runningProcesses.get(7).uid,
							(TrafficStats.getUidTxBytes(runningProcesses.get(0).uid) + TrafficStats
									.getUidRxBytes(runningProcesses.get(1).uid)) / 1024,
							(TrafficStats.getUidTxBytes(runningProcesses.get(2).uid) + TrafficStats
									.getUidRxBytes(runningProcesses.get(3).uid)) / 1024,
							(TrafficStats.getUidTxBytes(runningProcesses.get(4).uid) + TrafficStats
									.getUidRxBytes(runningProcesses.get(5).uid)) / 1024,
							(TrafficStats.getUidTxBytes(runningProcesses.get(6).uid) + TrafficStats
									.getUidRxBytes(runningProcesses.get(7).uid)) / 1024);

			return JSONfunctions.LoadChart(urlRqs3DPie);

		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + e.getMessage();
		}
		return null;
	}

	private ApplicationInfo getPackageName(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			ivStatisticsChartOverview.setImageBitmap((Bitmap) data);
		}
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{	if (!isFinishing()) 
			{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showProgressLoader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideProgressLoader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();

		if (view.getId() == R.id.tvVIPDMaps) {
			Intent intent = new Intent(this, VIPDMapsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.tvVIPNetwork) {
			Intent intent = new Intent(this, VIPDNetworkUsageviewActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.tvVIPServices) {
			Intent intent = new Intent(this, VIPD_ServiceUsages.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.tvApplication) {
			Intent intent = new Intent(this,
					VIPD_Application_Trafic_Usages.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.tvUsage) {
			Intent intent = new Intent(this, VIPDMobileUsageActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(view.getId() == R.id.tvSpeedTest){
			   Intent intent = new Intent(this,VIPD_SpeedTestActivity.class);
			   intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			   startActivity(intent);
			  }
		
	}

}