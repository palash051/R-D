package com.vipdashboard.app.activities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
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

public abstract class VIPDNetworkUsageActivity extends MainActionbarBase implements OnClickListener, IAsynchronousTask{
	
	 TextView tvNetworkUsageHistoryCallReceived, tvNetworkUsageHistoryCallMade, tvNetworkUsageHistoryCallDroped, tvNetworkUsageHistoryCallSetupFail, tvNetworkUsageHistorySMSSent, tvNetworkUsageHistorySMSReceived,
		tvNetworkUsageHistoryMaxStrength, tvNetworkUsageHistoryMinStrength, tvNetworkUsageHistoryAvgStrength, tvNetworkUsageHistoryMaxDuration, tvNetworkUsageHistoryMinDuration, tvNetworkUsageHistoryAvgDuration,
		tvNetworkUsageHistoryMaxLatency, tvNetworkUsageHistoryMinLatency, tvNetworkUsageHistoryAvgLatency, tvNetworkUsageHistoryWIFIData, tvNetworkUsageHistoryWIFIDownloadAvgSpeed;
	
	RelativeLayout rlDashboardReportManagement, rlDashboardReportTechnical, rlDashboardTroubleTicket;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	
	int overviewID=0;
	String overviewLineChartText=null;
	TextView tvDeshboardReportTitle;
	TextView txtExperienceView;
	ImageView ivChartView;
	
	ImageView ivNetworkServiceUsage,ivNetworkMobileUsageSignal,ivNetworkMobileUsageCall;
	
	int incommingCallCount = 0, outgoingCallCount = 0, missedCallCount = 0, totalCallCount = 0,dropCallCount = 0,incommingSMSCount = 0, outgoingSMSCount = 0, missedSMSCount = 0, totalSMSCount = 0,dataCount=0;
	
	ArrayList<PhoneSignalStrenght>phoneSignalStrenghtList=null;
	ArrayList<PhoneCallInformation>phoneCallInformationList=null;
	ArrayList<Object> networkUsageHistoryList =null;
	
	TextView tvExperinceFilterHour,tvExperinceFilterToday,tvExperinceFilterYesterday,tvExperinceFilterWeek,tvSpeedTest;
	
	TextView edphonenumber;
	
	CalendarView cvNetworkUsageHistory;
	 TextView tvDateTimeViewHeader;
	 long pressedDate; 
	
	TextView tvMaxSignalStrength, tvMinSignalStrength, tvAvgSignalStrength,
			tvMaxCallDuration, tvMinCallDuration, tvAvgCallDuration,
			tvMaxLatency, tvMinLatency, tvAvgLatency;
	
	TextView tvVIPDMaps,tvVIPNetwork,tvVIPServices,tvApplication,tvUsage;
	
	boolean isVisibleRequired=false; 
	
	
	TextView welcomeText;
	
	Bitmap profilePicture;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedInstanceState= getIntent().getExtras();
		setContentView(R.layout.network_update_history);
		
		
		
		 if(savedInstanceState!=null && savedInstanceState.containsKey("isVisibleRequired"))
		 {
			 isVisibleRequired=savedInstanceState.getBoolean("isVisibleRequired");
		 }
	
		initialization();
	}

	private void initialization() {
		
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
		
		if(isVisibleRequired)
		{
			tvVIPDMaps.setVisibility(View.GONE);
			tvVIPNetwork.setVisibility(View.GONE);
			tvVIPServices.setVisibility(View.GONE);
			tvApplication.setVisibility(View.GONE);
			tvUsage.setVisibility(View.GONE);
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
		
	  //tvDashboardMessage.setText("Update as of ");
	  //LoadInformation();
	  
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
	public void showProgressLoader() {
		//progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//progressBar.setVisibility(View.GONE);
	}
}

