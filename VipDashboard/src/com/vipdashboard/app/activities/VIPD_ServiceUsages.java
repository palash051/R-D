package com.vipdashboard.app.activities;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import com.vipdashboard.app.adapter.AllCallsAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
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



import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SearchView.OnQueryTextListener;



public class VIPD_ServiceUsages extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask, OnItemClickListener, OnQueryTextListener {
	
	TextView tvInstallationDate,Chart;
	DownloadableAsyncTask downloadableAsyncTask;

	ImageView ivStatisticsChartOverview;

	ArrayList<PhoneCallInformation> phoneCallSummeryListByTotal,phoneCallSummeryListNyNumber;
	

	private static final int DOWNLOAD_TOTAL_CALL_SUMMERY = 0;
	int DOWNLOAD_TYPE = DOWNLOAD_TOTAL_CALL_SUMMERY;

	int incommingCallCount = 0, outgoingCallCount = 0, missedCallCount = 0,
			totalCallCount = 0, dropCallCount = 0, incommingCallDuaration = 0,
			outgoingCallDuaration = 0, totalCallDuaration = 0;
	
	int incommingSMSCount,dataCount,outgoingSMSCount, totalSMSCount,missedSMSCount; 
		
	

	TextView tvVIPDMaps,tvVIPNetwork,tvVIPServices,tvApplication,tvUsage,tvSpeedTest;

	AllCallsAdapter adapter;
	Date date;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.vipd_serviceusages);
		
		
		initialization();
		//tvInstallationDate.append(""+new Date(getAppLastUpdateTime(this) * 1000));
		tvInstallationDate.append(""+CommonTask.getCurrentDateTimeAsString());
		
	}

	private void initialization() {
		
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		//ArrayList<PhoneCallInformation> callList = database.getTotalCallInfoByPhoneNumber();
		//database.close();
		//adapter = new AllCallsAdapter(this, R.layout.all_call_item_layout, callList);		
		tvInstallationDate=(TextView) findViewById(R.id.tvInstallationDate);		
		ivStatisticsChartOverview=(ImageView) findViewById(R.id.ivNetworkServiceUsage);
		
		tvVIPDMaps = (TextView) findViewById(R.id.tvVIPDMaps);
		tvVIPNetwork = (TextView) findViewById(R.id.tvVIPNetwork);
		
		tvVIPServices = (TextView) findViewById(R.id.tvVIPServices); 
		tvApplication = (TextView) findViewById(R.id.tvApplication);
		tvUsage = (TextView) findViewById(R.id.tvUsage);
		tvSpeedTest = (TextView) findViewById(R.id.tvSpeedTest);
		
		
		tvVIPDMaps.setOnClickListener(this);
		tvVIPNetwork.setOnClickListener(this);
		tvVIPServices.setOnClickListener(this);
		tvApplication.setOnClickListener(this);
		tvUsage.setOnClickListener(this);
		tvSpeedTest.setOnClickListener(this);
		
		
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
		getCallInfo(CommonConstraints.INFO_TYPE_ALL);
		downloadableAsyncTask = new DownloadableAsyncTask(VIPD_ServiceUsages.this);
		downloadableAsyncTask.execute();
	}
	
	public static long getAppLastUpdateTime(Context context){
		
	    try {
	    if(Build.VERSION.SDK_INT>8/*Build.VERSION_CODES.FROYO*/ ){	    	
	    	long installed = context.getPackageManager().getPackageInfo("com.mynet.app.name", 0).firstInstallTime;	    	
	    	return installed;	    	
	    }else{
	        ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
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
	public void onClick(View view) {
		int id = view.getId();
		
		if (view.getId() == R.id.tvVIPDMaps) {
			Intent intent = new Intent(this,VIPDMapsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.tvVIPNetwork) {
			Intent intent = new Intent(this,VIPDNetworkUsageviewActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvVIPServices){
			Intent intent = new Intent(this,VIPD_ServiceUsages.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(view.getId()==R.id.tvApplication){
			Intent intent = new Intent(this,VIPD_Application_Trafic_Usages.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(view.getId()==R.id.tvUsage){
			Intent intent = new Intent(this,VIPDMobileUsageActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(view.getId() == R.id.tvSpeedTest){
			   Intent intent = new Intent(this,VIPD_SpeedTestActivity.class);
			   intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			   startActivity(intent);
			  }
	}

	@Override
	public Object doBackgroundPorcess() {

		try {
			if (DOWNLOAD_TYPE == DOWNLOAD_TOTAL_CALL_SUMMERY) {
				ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
				
				int wify=CheckforWIFI();
				
				String urlRqs3DPie = String.format(
						CommonURL.getInstance().GoogleChartServiceUsageNew,
						URLEncoder.encode("Calls received("+String.valueOf(incommingCallCount+missedSMSCount)+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("Calls made("+String.valueOf(outgoingCallCount)+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("Calls dropped(0)", CommonConstraints.EncodingCode),
						URLEncoder.encode("Calls setup failure(0)", CommonConstraints.EncodingCode),
						URLEncoder.encode("Messages("+String.valueOf(totalSMSCount)+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("Data Connections("+String.valueOf(dataCount)+")", CommonConstraints.EncodingCode),						
						URLEncoder.encode("Active apps("+String.valueOf(am.getRunningAppProcesses().size())+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("WIFI("+String.valueOf(wify)+")", CommonConstraints.EncodingCode),
						
						incommingCallCount+missedSMSCount,outgoingCallCount,0,0,totalSMSCount,dataCount, am.getRunningAppProcesses().size(),wify,
						incommingCallCount+missedSMSCount,outgoingCallCount,0,0,totalSMSCount,dataCount,am.getRunningAppProcesses().size(),wify
						);

				return JSONfunctions.LoadChart(urlRqs3DPie);
			
			}
			
		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + e.getMessage();
		}
		return null;
	}
	
	

	private int CheckforWIFI() {
		WifiManager mainWifi;
		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		//Toast.makeText(this, ""+mainWifi.getScanResults().size(), Toast.LENGTH_LONG).show();
		return mainWifi.getScanResults().size();
	}

	private void getCallInfo(int type) {
		incommingCallCount = 0;
		outgoingCallCount = 0;
		missedCallCount = 0;
		totalCallCount = 0;
		dropCallCount = 0;
		incommingCallDuaration = 0;
		outgoingCallDuaration = 0;
		totalCallDuaration = 0;
		
		incommingSMSCount = 0;outgoingSMSCount=0; totalSMSCount=0;missedSMSCount=0;
		dataCount=0;
		
		

		//incommingSMSCount = phoneSMSInformation.SMSCount;
		MyNetDatabase myNetDatabase = new MyNetDatabase(this);

		myNetDatabase.open();
		phoneCallSummeryListByTotal = myNetDatabase.getTotalCallInfo(type);
		
		ArrayList<PhoneSMSInformation> phoneSmsList = myNetDatabase.getTotalSMSInfo(0);
		dataCount = myNetDatabase.getTotalDataCount();
		
		//phoneCallSummeryListNyNumber=myNetDatabase.getTotalCallInfoByPhoneNumber();
		myNetDatabase.close();

		for (PhoneCallInformation phoneCallInformation : phoneCallSummeryListByTotal) {
			
			
			if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE) {
				incommingCallCount = phoneCallInformation.CallCount;
				incommingCallDuaration = phoneCallInformation.DurationInSec;
				totalCallDuaration = totalCallDuaration
						+ incommingCallDuaration;
			} 
			
			
			else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE) {
				outgoingCallCount = phoneCallInformation.CallCount;
				outgoingCallDuaration = phoneCallInformation.DurationInSec;
				totalCallDuaration = totalCallDuaration
						+ outgoingCallDuaration;
			} 
			
			else {
				missedCallCount = phoneCallInformation.CallCount;
			}
			
			
			totalCallCount = totalCallCount
					+ phoneCallInformation.CallCount;
		}
		
		
		for (PhoneSMSInformation phoneSMSInformation : phoneSmsList) {
			if (phoneSMSInformation.SMSType == 1) {
				incommingSMSCount = phoneSMSInformation.SMSCount;
			} else if (phoneSMSInformation.SMSType == 2) {
				outgoingSMSCount = phoneSMSInformation.SMSCount;
			} else {
				missedSMSCount = phoneSMSInformation.SMSCount;
			}
			totalSMSCount = totalSMSCount + phoneSMSInformation.SMSCount;
		}
		
		
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			ivStatisticsChartOverview.setImageBitmap((Bitmap) data);//Draw piechart using DATA
			
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) {
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

}