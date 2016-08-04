package com.vipdashboard.app.activities;

import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Sampler.Value;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.entities.VIPDPMKPIHourlyDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;
import com.vipdashboard.app.utils.MasterDataConstants;

public class VIPDNetworkPerformanceLinechart extends MainActionbarBase implements OnClickListener, IAsynchronousTask{

	RelativeLayout rlDashboardAcquisition,rlDashboardRetention,rlDashboardEfficiency,rlDashboardTraffic,rlDashboardMTR,rlDashboardSelfService;
	TextView tvDashboardMessage,tvDashboardAcquisitionValue,tvDashboardRetentionValue,tvDashboardEfficiencyValue,tvDashboardTrafficValue,tvDashboardMTRValue,tvDashboardSelfServiceValue;
	
	TextView tvDashboardFirstTitle,tvDashboardSecondTitle,tvDashboardThirdTitle,tvDashboardForthTitle,tvDashboardFifthTitle,tvDashboardSixthTitle;
	
	RelativeLayout rlDashboardReportManagement, rlDashboardReportTechnical, rlDashboardTroubleTicket;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	
	int overviewID=0;
	String overviewTagID=null;
	String overviewLineChartText=null;
	TextView tvDeshboardReportTitle;
	TextView txtExperienceView;
	ImageView ivChartView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState= getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_networkperformance_linechart);
		
		 if(savedInstanceState!=null && savedInstanceState.containsKey("overviewID"))
		 {
			 overviewID=savedInstanceState.getInt("overviewID");
			 
			 overviewLineChartText=savedInstanceState.getString("overviewLineChartText");
			 
			 overviewTagID=savedInstanceState.getString("overviewTagID");
		 }
		 
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		initialization();
	}

	private void initialization() {
    	
		tvDashboardMessage = (TextView) findViewById(R.id.tvDashboardMessage);
		txtExperienceView = (TextView) findViewById(R.id.txtExperienceView);
		tvDeshboardReportTitle = (TextView) findViewById(R.id.tvDeshboardReportTitle);
		
		ivChartView = (ImageView) findViewById(R.id.ivChartView);
		
		progressBar = (ProgressBar) findViewById(R.id.pbDashBoard);
		
		txtExperienceView.setText(overviewLineChartText);
		
		BuildTitle();
		 
	}
	
	private void BuildTitle() {
		
		if(overviewID>0)
	    {	
	    	switch(overviewID)
	    	{
	    	case MasterDataConstants.NetworkPerformace.PLMN:
		    	tvDeshboardReportTitle.setText("PLMN");
	    		break;
	    	case MasterDataConstants.NetworkPerformace.REGION:
	    		tvDeshboardReportTitle.setText("REGION");
	    		break;
	    	case MasterDataConstants.NetworkPerformace.PLATINUM_CLUSTER:
	    		tvDeshboardReportTitle.setText("PLATINUM CLUSTER");
	    		break;
	    	case MasterDataConstants.NetworkPerformace.SPI_SUMMARY:
	    		tvDeshboardReportTitle.setText("SPI Summary");
	    		break;
	    	}
	    }
	}
	
	@Override
	 protected void onPause() {
	  MyNetApplication.activityPaused();
	  super.onPause();
	 }

	 @Override
	 protected void onResume() {
		 super.onResume();
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
	  tvDashboardMessage.setText("Update as of "+CommonTask.getCurrentDateTimeAsString());
	  LoadInformation();
	  
	 }

	@Override
	public void onClick(View view) {
		int id = view.getId();	
	}
	
	private void LoadInformation() {
		if(downloadableAsyncTask != null){
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		//progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//progressBar.setVisibility(View.GONE);
	}
	
	public Object doBackgroundPorcess() {/*
		IStatisticsReportManager manager=new StatisticsReportManager();
		Object datax = manager.getPMKPIHourlyData(Integer.parseInt(overviewTagID));	
			
		String url=CommonURL.getInstance().GoogleLineChart;
		//url=url+"chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24";
		
		//url=url+"&chd=t:20,23,45,67,90|45,33,55,65,10";
		
		VIPDPMKPIHourlyDatas pMKPIHourlyDatas=(VIPDPMKPIHourlyDatas)datax;
		
		String processedString="";
		
		if(pMKPIHourlyDatas!=null && pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size()>0)
		{	
			for(int i=0;i<pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size();i++){
				processedString+=(int) Math.round(pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.get(i).KPIValue)+",";
				
				if(i==15)
				{
					processedString = processedString.substring(0,processedString.length()-1);
					processedString+="|";
				}
			}
		}
		
		processedString = processedString.substring(0,processedString.length()-1);
			
		url=url+"&chd=t:"+processedString;
		
		return JSONfunctions.LoadChart(url);*/
		
		
		String urlRqs3DPie = String.format(
				CommonURL.getInstance().GoogleChartServiceUsageNew,
				/*URLEncoder.encode("Calls received("+String.valueOf(incommingCallCount+missedSMSCount)+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("Calls made("+String.valueOf(outgoingCallCount)+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("Calls dropped(0)", CommonConstraints.EncodingCode),
				URLEncoder.encode("Calls setup failure(0)", CommonConstraints.EncodingCode),
				URLEncoder.encode("Messages("+String.valueOf(totalSMSCount)+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("Data Connections("+String.valueOf(dataCount)+")", CommonConstraints.EncodingCode),						
				URLEncoder.encode("Active apps("+String.valueOf(am.getRunningAppProcesses().size8())+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("WIFI("+String.valueOf(wify)+")", CommonConstraints.EncodingCode),*/
				
				0,1,2,3,4,5,6,7,
				8,7,6,5,7,9,97,5
				);
		

		return JSONfunctions.LoadChart(urlRqs3DPie);
	}
	
	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null)
		{		
			
		}	
		ivChartView.setImageBitmap((Bitmap)data);
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		}
	
	}
