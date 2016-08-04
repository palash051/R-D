package com.vipdashboard.app.activities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.VIPDPMKPIHourlyDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.MasterDataConstants;

public class VIPDNetworkUsageviewActivity extends MainActionbarBase implements OnDateChangeListener{

	RelativeLayout rlDashboardAcquisition,rlDashboardRetention,rlDashboardEfficiency,rlDashboardTraffic,rlDashboardMTR,rlDashboardSelfService;
	TextView tvDashboardMessage,tvDashboardAcquisitionValue,tvDashboardRetentionValue,tvDashboardEfficiencyValue,tvDashboardTrafficValue,tvDashboardMTRValue,tvDashboardSelfServiceValue;
	
	TextView tvDashboardFirstTitle,tvDashboardSecondTitle,tvDashboardThirdTitle,tvDashboardForthTitle,tvDashboardFifthTitle,tvDashboardSixthTitle;
	
	RelativeLayout rlDashboardReportManagement, rlDashboardReportTechnical, rlDashboardTroubleTicket;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	
	int overviewID=0;
	String overviewLineChartText=null;
	TextView tvDeshboardReportTitle;
	TextView txtExperienceView;
	ImageView ivChartView;
	
	boolean isVisibleRequired=false; 

	TextView tvVIPDMaps,tvVIPNetwork,tvVIPServices,tvApplication,tvUsage,tvSpeedTest;
	
	ImageView ivNetworkServiceUsage,ivNetworkMobileUsageSignal,ivNetworkMobileUsageCall;
	
	CalendarView cvNetworkUsageHistory;
	TextView tvDateTimeViewHeader;
	long pressedDateFrom,pressedDateTo;
	 
	ArrayList<Object> networkUsageHistoryList =null;
	LinearLayout llmyexperience;
	
	TextView tvNetworkUsageHistoryCallReceived, tvNetworkUsageHistoryCallMade, tvNetworkUsageHistoryCallDroped, tvNetworkUsageHistoryCallSetupFail, tvNetworkUsageHistorySMSSent, tvNetworkUsageHistorySMSReceived,
	tvNetworkUsageHistoryMaxStrength, tvNetworkUsageHistoryMinStrength, tvNetworkUsageHistoryAvgStrength, tvNetworkUsageHistoryMaxDuration, tvNetworkUsageHistoryMinDuration, tvNetworkUsageHistoryAvgDuration,
	tvNetworkUsageHistoryMaxLatency, tvNetworkUsageHistoryMinLatency, tvNetworkUsageHistoryAvgLatency, tvNetworkUsageHistoryWIFIData, tvNetworkUsageHistoryWIFIDownloadAvgSpeed;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState= getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_update_history); 
		
		initialization();
	}

	private void initialization() {
		
		cvNetworkUsageHistory = (CalendarView) findViewById(R.id.cvNetworkUsageHistory);
		tvDateTimeViewHeader = (TextView) findViewById(R.id.tvDateTimeViewHeader);
		
		tvNetworkUsageHistoryCallReceived = (TextView) findViewById(R.id.tvNetworkUsageHistoryCallReceivedValue);
		tvNetworkUsageHistoryCallMade = (TextView) findViewById(R.id.tvNetworkUsageHistoryCallMadeValue);
		tvNetworkUsageHistoryCallDroped = (TextView) findViewById(R.id.tvNetworkUsageHistoryCallDroppedValue);
		tvNetworkUsageHistoryCallSetupFail = (TextView) findViewById(R.id.tvNetworkUsageHistoryCallSetupFailurValue);
		
		tvNetworkUsageHistorySMSSent = (TextView) findViewById(R.id.tvNetworkUsageHistorySMSSentValue);
		tvNetworkUsageHistorySMSReceived = (TextView) findViewById(R.id.tvNetworkUsageHistorySMSReceivedValue);
		
		tvNetworkUsageHistoryMaxStrength = (TextView) findViewById(R.id.tvNetworkUsageHistoryMaxStrengthValue);
		tvNetworkUsageHistoryMinStrength = (TextView) findViewById(R.id.tvNetworkUsageHistoryMinStrengthValue);
		tvNetworkUsageHistoryAvgStrength = (TextView) findViewById(R.id.tvNetworkUsageHistoryAvgStrengthValue);
		
		tvNetworkUsageHistoryMaxDuration = (TextView) findViewById(R.id.tvNetworkUsageHistoryMaxDurationValue);
		tvNetworkUsageHistoryMinDuration = (TextView) findViewById(R.id.tvNetworkUsageHistoryMinDurationValue);
		tvNetworkUsageHistoryAvgDuration = (TextView) findViewById(R.id.tvNetworkUsageHistoryAvgDurationValue);
		
		tvNetworkUsageHistoryMaxLatency = (TextView) findViewById(R.id.tvNetworkUsageHistoryMaxLatencyValue);
		tvNetworkUsageHistoryMinLatency = (TextView) findViewById(R.id.tvNetworkUsageHistoryMinLatencyValue);
		tvNetworkUsageHistoryAvgLatency = (TextView) findViewById(R.id.tvNetworkUsageHistoryAvgLatencyValue);
		
		tvNetworkUsageHistoryWIFIData = (TextView) findViewById(R.id.tvNetworkUsageHistoryWIfiDataValue);
		tvNetworkUsageHistoryWIFIDownloadAvgSpeed = (TextView) findViewById(R.id.tvNetworkUsageHistoryDownloadAverageValue);
		progressBar = (ProgressBar) findViewById(R.id.pbDashBoard);		
		
		cvNetworkUsageHistory.setMaxDate(System.currentTimeMillis());
		cvNetworkUsageHistory.setOnDateChangeListener(this);
		
		llmyexperience= (LinearLayout) findViewById(R.id.llmyexperience);
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
	  getInfoFromDB();  
	  
	 }
	 
	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
		/*cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);*/
		
		/*pressedDateFrom = cal.getTimeInMillis();
		cal.set(Calendar.DAY_OF_MONTH,dayOfMonth+1);
		pressedDateTo=cal.getTimeInMillis();
		
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		networkUsageHistoryList = database.getUsersHistry(pressedDateFrom,pressedDateTo);
		database.close();*/
		getInfoFromDB(cal);
	}
	private void getInfoFromDB(){
		getInfoFromDB(Calendar.getInstance());
	}
	private void getInfoFromDB(Calendar cal) {
		String value = DateUtils.formatDateTime(
				VIPDNetworkUsageviewActivity.this, cal.getTimeInMillis(), 
				DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
		tvDateTimeViewHeader.setText(value + " day average");
		//Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		pressedDateFrom=cal.getTimeInMillis();
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
		pressedDateTo=cal.getTimeInMillis();
		LoadInformation();
	}
	
	private void LoadInformation() {
		int callReceived=0, callMade=0, callDroped=0,CallSetupfail=0, SMSSent=0, SMSReceived=0, MaxStrength=0, MinStrength=0, AvgStrength=0,
				MaxDuration=0, MinDuration=0, AvgDuration=0, MaxLatency=0, MinLatency=0, AvgLatency=0;
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		networkUsageHistoryList = database.getUsersHistry(pressedDateFrom,pressedDateTo);
		
		MaxStrength = database.getMaxSignalStrength(pressedDateFrom,pressedDateTo);
		MinStrength = database.getMinSignalStrength(pressedDateFrom,pressedDateTo);
		AvgStrength = database.getAgvSignalStrenght(pressedDateFrom,pressedDateTo);
		
		MaxDuration = database.getMaxCallDuration(pressedDateFrom,pressedDateTo);
		MinDuration = database.getMinCallDuration(pressedDateFrom,pressedDateTo);
		AvgDuration = database.getAvgCallDuration(pressedDateFrom,pressedDateTo);
		
		database.close();
		
		if(networkUsageHistoryList.size()>0){
			ArrayList<PhoneCallInformation> phoneCallList= (ArrayList<PhoneCallInformation>) networkUsageHistoryList.get(0);
			ArrayList<PhoneSMSInformation> phoneSmsList= (ArrayList<PhoneSMSInformation>) networkUsageHistoryList.get(1);
			
			for(int i=0;i<phoneCallList.size();i++){
				if(phoneCallList.get(i).CallType == CallLog.Calls.INCOMING_TYPE){
					callReceived++;
				}else if(phoneCallList.get(i).CallType == CallLog.Calls.OUTGOING_TYPE){
					callMade++;
				}
			}
			for(int i=0;i<phoneSmsList.size();i++){
				if(phoneSmsList.get(i).SMSType == 1){
					SMSReceived++;
				}else if(phoneSmsList.get(i).SMSType == 2){
					SMSSent++;
				}
			}
			tvNetworkUsageHistoryCallReceived.setText(String.valueOf(callReceived));
			tvNetworkUsageHistoryCallMade.setText(String.valueOf(callMade));
			tvNetworkUsageHistoryCallDroped.setText(String.valueOf(0));
			tvNetworkUsageHistoryCallSetupFail.setText(String.valueOf(0));
			
			tvNetworkUsageHistorySMSSent.setText(String.valueOf(SMSSent));
			tvNetworkUsageHistorySMSReceived.setText(String.valueOf(SMSReceived));
			
			MaxStrength =(MaxStrength==99?0:MaxStrength);
			MinStrength=MinStrength==99?0:MinStrength;
			AvgStrength=AvgStrength==99?0:AvgStrength;
			tvNetworkUsageHistoryMaxStrength.setText(String.valueOf(-113+(2*MaxStrength)));
			tvNetworkUsageHistoryMinStrength.setText(String.valueOf(-113+(2*MinStrength)));
			tvNetworkUsageHistoryAvgStrength.setText(String.valueOf(-113+(2*AvgStrength)));
			
			tvNetworkUsageHistoryMaxDuration.setText(String.valueOf(MaxDuration));
			tvNetworkUsageHistoryMinDuration.setText(String.valueOf(MinDuration));
			tvNetworkUsageHistoryAvgDuration.setText(String.valueOf(AvgDuration));
			
			tvNetworkUsageHistoryMaxLatency.setText(String.valueOf(0));
			tvNetworkUsageHistoryMinLatency.setText(String.valueOf(0));
			tvNetworkUsageHistoryAvgLatency.setText(String.valueOf(0));
			
			tvNetworkUsageHistoryWIFIData.setText(String.valueOf(0));
			tvNetworkUsageHistoryWIFIDownloadAvgSpeed.setText(String.valueOf(0));
		}
	}	
}


