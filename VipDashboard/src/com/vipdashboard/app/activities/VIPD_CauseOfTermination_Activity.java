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
import com.vipdashboard.app.entities.VIPDCDRDataPercentage;
import com.vipdashboard.app.entities.VIPDCDRDataPercentageRoot;
import com.vipdashboard.app.entities.VIPDPMKPIHourlyDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;
import com.vipdashboard.app.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SearchView.OnQueryTextListener;



public class VIPD_CauseOfTermination_Activity extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask, OnItemClickListener, OnQueryTextListener {
	TextView tvInstallationDate,tvDashboardMessage;
	DownloadableAsyncTask downloadableAsyncTask;

	ImageView ivStatisticsChartOverview;
	
	
	int NormalClaring = 0, WrongNumber = 0, InvalidFormatNumber = 0,
			UserBusy = 0, CallRejected = 0, NormalUnspecified = 0,
			ResourceUnavailable = 0;
	

	ArrayList<PhoneCallInformation> phoneCallSummeryListByTotal,phoneCallSummeryListNyNumber;
	

	private static final int DOWNLOAD_TOTAL_CALL_SUMMERY = 0;
	int DOWNLOAD_TYPE = DOWNLOAD_TOTAL_CALL_SUMMERY;
	
	AllCallsAdapter adapter;
	Date date;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.vipd_subscriber_termination_info);
		initialization();
		
	}
	private void initialization() {
		
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();	
		tvDashboardMessage=(TextView) findViewById(R.id.tvDashboardMessage);		
		ivStatisticsChartOverview=(ImageView) findViewById(R.id.ivCauseofteminationPieChart);
		tvDashboardMessage.append(""+CommonTask.getCurrentDateTimeAsString());
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
		getTerminationInfo(CommonConstraints.INFO_TYPE_ALL);
		downloadableAsyncTask = new DownloadableAsyncTask(VIPD_CauseOfTermination_Activity.this);
		downloadableAsyncTask.execute();
	}
	
	private void getTerminationInfo(int infoTypeAll) {
		// TODO Auto-generated method stub
		
	}
	/*public static long getAppLastUpdateTime(Context context){
		
	    try {
	    if(Build.VERSION.SDK_INT>8Build.VERSION_CODES.FROYO ){	    	
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
	    }*/

	private void arrangeOverviewTab() {
		DOWNLOAD_TYPE = DOWNLOAD_TOTAL_CALL_SUMMERY;				
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		arrangeAllTab();
	}

	@Override
	public Object doBackgroundPorcess() 
	{
		
		try {
			
			IStatisticsReportManager manager=new StatisticsReportManager();
			Object objCauseoftermination= manager.getCauseofTermination("100");
			
			//String url=CommonURL.getInstance().GoogleLineChart;
			
			VIPDCDRDataPercentageRoot obj=(VIPDCDRDataPercentageRoot)objCauseoftermination;
			
			if(obj!=null )
			{	
				
				NormalClaring=(int) obj.CDRData.normalRelease;
				WrongNumber=(int) obj.CDRData.partialRecord;
				InvalidFormatNumber=(int) obj.CDRData.partialRecordCallReestablishment;
				UserBusy=(int) obj.CDRData.unsuccessfulCallAttempt;
				CallRejected=(int) obj.CDRData.stableCallAbnormalTermination;
				NormalUnspecified=(int) obj.CDRData.cAMELInitCallRelease;
				ResourceUnavailable=(int) obj.CDRData.cAMELCPHCallConfigurationChange;
			}
			
			
			
			
			
			int Total=NormalClaring+WrongNumber+InvalidFormatNumber+UserBusy+CallRejected+NormalUnspecified+ResourceUnavailable;
			
			//int Total=50+65+30+90+10+60+54;
			
			String urlRqs3DPie = String.format(
					CommonURL.getInstance().GoogleChartServiceUsageForCauseOfTermination,
					URLEncoder.encode("Normal Clearing", CommonConstraints.EncodingCode),
					URLEncoder.encode("Wrong Number", CommonConstraints.EncodingCode),
					URLEncoder.encode("Invalid format number", CommonConstraints.EncodingCode),
					URLEncoder.encode("User Busy", CommonConstraints.EncodingCode),
					URLEncoder.encode("Call Rejected", CommonConstraints.EncodingCode),
					URLEncoder.encode("Normal Unspecified", CommonConstraints.EncodingCode),
					URLEncoder.encode("Resource unavailable", CommonConstraints.EncodingCode),			
					
					(NormalClaring*100)/Total, (WrongNumber*100)/Total,(InvalidFormatNumber*100)/Total,(UserBusy*100)/Total,(CallRejected*100)/Total,
					(NormalUnspecified*100)/Total,(ResourceUnavailable*100)/Total,
					
					(NormalClaring*100)/Total, (WrongNumber*100)/Total,(InvalidFormatNumber*100)/Total,(UserBusy*100)/Total,(CallRejected*100)/Total,
					(NormalUnspecified*100)/Total,(ResourceUnavailable*100)/Total
					
					
					/*(50*100)/Total, (50*100)/Total,(65*100)/Total,(30*100)/Total,(90*100)/Total,(10*100)/Total,(60*100)/Total,
					(50*100)/Total,(50*100)/Total,(65*100)/Total,(30*100)/Total,(90*100)/Total,(10*100)/Total,(60*100)/Total*/
					
					
					
					
					/*50,60,90,20,50,10,30,50,
					50,60,90,20,50,10,30,50*/
					);
			return JSONfunctions.LoadChart(urlRqs3DPie);
			
	} catch (Exception e) {
		String ss = e.getMessage();
		ss = ss + e.getMessage();
	}
	return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			
			
			ivStatisticsChartOverview.setImageBitmap((Bitmap) data);
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	

}

