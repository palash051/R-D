package com.vipdashboard.app.activities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activity.assistance.AssistanceMainActivity;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.base.OnSwipeTouchListener;
import com.vipdashboard.app.classes.MyNetFacebookActivity;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.FacebookPersons;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.fragments.CollaborationMainFragment;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.manager.FacebookManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class NetworkSelfCareMyExperienceActivity extends Activity implements
		OnClickListener, LocationListener, IAsynchronousTask, OnDateChangeListener {
	
	
	private static final int EXCELLENT_LEVEL = 75;
    private static final int GOOD_LEVEL = 50;
    private static final int MODERATE_LEVEL = 25;
    private static final int WEAK_LEVEL = 0;

    private static final int INFO_SERVICE_STATE_INDEX = 0;
    private static final int INFO_CELL_LOCATION_INDEX = 1;
    private static final int INFO_CALL_STATE_INDEX = 2;
    private static final int INFO_CONNECTION_STATE_INDEX = 3;
    private static final int INFO_SIGNAL_LEVEL_INDEX = 4;
    private static final int INFO_SIGNAL_LEVEL_INFO_INDEX = 5;
    private static final int INFO_DATA_DIRECTION_INDEX = 6;
    private static final int INFO_DEVICE_INFO_INDEX = 7;
    
    
    
    private static final int[] info_ids= {
        R.id.serviceState_info,
        R.id.cellLocation_info,
        R.id.callState_info,
        R.id.connectionState_info,
        R.id.signalLevel,
        R.id.signalLevelInfo,
        R.id.dataDirection,
        R.id.device_info
    };
    
    

	RelativeLayout rlMyExperienceSignalStrenght, rlMyExperienceTotalCalls,
			rlMyExperienceSMS, rlMyExperienceSetupSuccess,
			rlMyExperienceDropCalls, rlMyExperienceDataSpeed;
	TextView tvMyExperienceTotalCalls, tvMyExperienceTotalCallsCalled,
			tvMyExperienceTotalCallsReceived,tvMyExperienceTotalCallsMissed,
			tvMyExperienceSignalStrenghtValue, tvMyExperienceSMSSent,
			tvMyExperienceSMSReceived,tvMyExperienceSMSFailed, tvMyExperienceSMS,
			tvMyExperienceSetupSuccessValue, tvMyExperienceDropCallsValue,
			tvMyExperienceDataSpeedDownload, tvMyExperienceDataSpeedUpload
			;

	TextView tvUserExperinceStart, tvUserExperinceAssistance,tvUserExperinceHistory;
	
	TextView tvExperinceFilterHour,tvExperinceFilterToday,tvExperinceFilterYesterday,tvExperinceFilterWeek;
	
	TextView edphonenumber;
	
	TextView tvMaxSignalStrength, tvMinSignalStrength, tvAvgSignalStrength,
			tvMaxCallDuration, tvMinCallDuration, tvAvgCallDuration,
			tvMaxLatency, tvMinLatency, tvAvgLatency;
	
	
	CalendarView cvNetworkUsageHistory;
	 TextView tvDateTimeViewHeader;
	 long pressedDate; 
	
	 TextView tvNetworkUsageHistoryCallReceived, tvNetworkUsageHistoryCallMade, tvNetworkUsageHistoryCallDroped, tvNetworkUsageHistoryCallSetupFail, tvNetworkUsageHistorySMSSent, tvNetworkUsageHistorySMSReceived,
	 			tvNetworkUsageHistoryMaxStrength, tvNetworkUsageHistoryMinStrength, tvNetworkUsageHistoryAvgStrength, tvNetworkUsageHistoryMaxDuration, tvNetworkUsageHistoryMinDuration, tvNetworkUsageHistoryAvgDuration,
	 			tvNetworkUsageHistoryMaxLatency, tvNetworkUsageHistoryMinLatency, tvNetworkUsageHistoryAvgLatency, tvNetworkUsageHistoryWIFIData, tvNetworkUsageHistoryWIFIDownloadAvgSpeed;
	 TextView tvExperinceCollaboration,tvExperinceDialer,tvExperinceSMS,tvExperinceMail,tvExperinceMemo;

	private GoogleMap map;

	GaugeView gvMyExperienceSignalStrenght, gvMyExperienceTotalCalls,
			gvMyExperienceSMS, gvMyExperienceSetupSuccess,
			gvMyExperienceDropCalls, gvMyExperienceDataSpeed;

	DownloadableAsyncTask downloadableAsyncTask;

	ImageView ivNetworkServiceUsage,ivNetworkMobileUsageSignal,ivNetworkMobileUsageCall;

	ViewFlipper vfNetwork;
	
	ProgressBar pbNetworkMain;

	private static final int DOWNLOAD_MAP = 0;
	private static final int DOWNLOAD_SERVICE_USAGES = 1;
	private static final int DOWNLOAD_MOBILE_USAGES = 2;

	int downloadType = DOWNLOAD_MAP;
	
	int incommingCallCount = 0, outgoingCallCount = 0, missedCallCount = 0, totalCallCount = 0,dropCallCount = 0,incommingSMSCount = 0, outgoingSMSCount = 0, missedSMSCount = 0, totalSMSCount = 0,dataCount=0;
	
	ArrayList<PhoneSignalStrenght>phoneSignalStrenghtList=null;
	ArrayList<PhoneCallInformation>phoneCallInformationList=null;
	ArrayList<Object> networkUsageHistoryList =null;
	
	TextView welcomeText;
	
	Bitmap profilePicture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_main);		
		initilization();
		/*if (CommonValues.getInstance().LoginUser != null)
			((TextView) findViewById(R.id.tvSelfcareMyExperinceUserTitle))
					.setText("Welcome: "
							+ CommonValues.getInstance().LoginUser.FirstName);*/		
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		if(tMgr.getLine1Number() != null){
			edphonenumber.setText(tMgr.getLine1Number().toString());
		}
		vfNetwork = (ViewFlipper) findViewById(R.id.vfNetwork);
		if(savedInstanceState != null && savedInstanceState.containsKey("MY_OPERATOR")){
			vfNetwork.setDisplayedChild(savedInstanceState.getInt("MY_OPERATOR"));
		}else{
			vfNetwork.setDisplayedChild(0);
		}
		
		registerSwipeListener();
		downloadType = DOWNLOAD_MAP;
		runDownloadable();
	}

	private void registerSwipeListener() {
		vfNetwork.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				arrangeRightSwipe();
			}
			public void onSwipeLeft() {
				arrangeSwipeLeft();
			}
		});

		rlMyExperienceSignalStrenght
				.setOnTouchListener(new OnSwipeTouchListener(this) {
					public void onSwipeRight() {
						arrangeRightSwipe();
					}
					public void onSwipeLeft() {
						arrangeSwipeLeft();
					}
				});
		rlMyExperienceTotalCalls.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				arrangeSwipeLeft();
			}
		});
		rlMyExperienceSMS.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				arrangeSwipeLeft();
			}
		});
		rlMyExperienceSetupSuccess.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		rlMyExperienceDropCalls.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		rlMyExperienceDataSpeed.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		ScrollView sv= (ScrollView)findViewById(R.id.rlNetworkPhoneStatus);
		sv.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		
		sv= (ScrollView)findViewById(R.id.rlNetworkSettings);
		sv.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		
		LinearLayout ll = (LinearLayout)findViewById(R.id.llNetworkMobileUsage);
		ll.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				arrangeSwipeLeft();
			}
		});
		
		sv= (ScrollView)findViewById(R.id.svNetworkUsageHistory);
		sv.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				arrangeSwipeLeft();
			}
		});
	}

	private void arrangeRightSwipe() {
		if (vfNetwork.getDisplayedChild() != 0) {
			vfNetwork.showPrevious();
		}else{
			Intent intent = new Intent(this, StatisticsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	private void arrangeSwipeLeft() {
		if (vfNetwork.getDisplayedChild() == 0) {
			downloadType = DOWNLOAD_MOBILE_USAGES;			
			if (downloadableAsyncTask != null) {
				downloadableAsyncTask.cancel(true);
			}
			downloadableAsyncTask = new DownloadableAsyncTask(
					NetworkSelfCareMyExperienceActivity.this);
			downloadableAsyncTask.execute();
		}else if (vfNetwork.getDisplayedChild() == 1) {
			downloadType = DOWNLOAD_SERVICE_USAGES;			
			if (downloadableAsyncTask != null) {
				downloadableAsyncTask.cancel(true);
			}
			downloadableAsyncTask = new DownloadableAsyncTask(
					NetworkSelfCareMyExperienceActivity.this);
			downloadableAsyncTask.execute();
		}else if(vfNetwork.getDisplayedChild() == 2){
			String value = DateUtils.formatDateTime(this, Calendar.getInstance().getTimeInMillis(), 
					DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
			pressedDate = Calendar.getInstance().getTimeInMillis();
			tvDateTimeViewHeader.setText(value + " day average");
			LoadInformation();
		}else if(vfNetwork.getDisplayedChild() == 3){
			 startSignalLevelListener();
		        displayTelephonyInfo();
		}
		
		if (vfNetwork.getDisplayedChild() != vfNetwork.getChildCount() - 1) {
			vfNetwork.showNext();
		}else{
			Intent intent = new Intent(this, AssistanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		StopListener();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		startSignalLevelListener();
		downloadType = DOWNLOAD_MAP;
		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.containsKey("MY_OPERATOR")){
			vfNetwork.setDisplayedChild(bundle.getInt("MY_OPERATOR"));
		}else{
			vfNetwork.setDisplayedChild(0);
		}
		
		arrangeTodayTab();
		setSpedometer(CommonConstraints.INFO_TYPE_TODAY);		
		super.onResume();
	}	

	private void runDownloadable() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	private void setSpedometer(int type) {
		try{
		incommingCallCount = 0;
		outgoingCallCount = 0;
		missedCallCount = 0;
		totalCallCount = 0;
		dropCallCount = 0;
		incommingSMSCount = 0;
		outgoingSMSCount = 0;
		missedSMSCount = 0;
		totalSMSCount = 0;
		dataCount=0;
		MyNetDatabase myNetDatabase = new MyNetDatabase(this);
		int signal = 0;
		myNetDatabase.open();
		signal = (int) (myNetDatabase.getAgvSignalStrenght(type) * 100) / 31;
		
		ArrayList<PhoneCallInformation> phoneCallList = myNetDatabase.getTotalCallInfo(type);
		ArrayList<PhoneSMSInformation> phoneSmsList = myNetDatabase.getTotalSMSInfo(type);
		PhoneDataInformation phoneDataInformation = myNetDatabase.getAgvDownLoadUploadSpeed(type);
		
		dataCount = myNetDatabase.getTotalDataCount();
		
		tvMaxSignalStrength.setText(String.valueOf(myNetDatabase.getMaxSignalStrenght()));
		tvMinSignalStrength.setText(String.valueOf(myNetDatabase.getminSignalStrenght()));
		tvAvgSignalStrength.setText(String.valueOf(myNetDatabase.getAgvSignalStrenght(0)));
		int hour=0,min=0,sec=0,duration=0;
		duration=myNetDatabase.getMaxCallDuration();
		sec = duration % 60;
		min =duration / 60;
		if (min > 59) {
			hour = min / 60;
			min = min % 60;
		}
		NumberFormat formatter = new DecimalFormat("00");
		String durationText = formatter.format(hour) + ":"
				+ formatter.format(min) + ":" + formatter.format(sec);
		
		tvMaxCallDuration.setText(durationText);
		
		hour=0;min=0;sec=0;duration=0;
		duration=myNetDatabase.getMinCallDuration();
		sec = duration % 60;
		min =duration / 60;
		if (min > 59) {
			hour = min / 60;
			min = min % 60;
		}
		formatter = new DecimalFormat("00");
		durationText = formatter.format(hour) + ":"
				+ formatter.format(min) + ":" + formatter.format(sec);
		tvMinCallDuration.setText(durationText);
		
		hour=0;min=0;sec=0;duration=0;
		duration=myNetDatabase.getAvgCallDuration();
		sec = duration % 60;
		min =duration / 60;
		if (min > 59) {
			hour = min / 60;
			min = min % 60;
		}
		formatter = new DecimalFormat("00");
		durationText = formatter.format(hour) + ":"
				+ formatter.format(min) + ":" + formatter.format(sec);
		
		tvAvgCallDuration.setText(durationText);
		
		phoneSignalStrenghtList=myNetDatabase.getSignalStrenghtList();
		phoneCallInformationList=myNetDatabase.getCallInfoList();
		
		String value = DateUtils.formatDateTime(
				NetworkSelfCareMyExperienceActivity.this, Calendar.getInstance().getTimeInMillis(), 
				DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
		pressedDate = Calendar.getInstance().getTimeInMillis();
		tvDateTimeViewHeader.setText(value + " day average");		
		networkUsageHistoryList = myNetDatabase.getUsersHistry(pressedDate);

		myNetDatabase.close();

		for (PhoneCallInformation phoneCallInformation : phoneCallList) {
			if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE) {
				incommingCallCount = phoneCallInformation.CallCount;
			} else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE) {
				outgoingCallCount = phoneCallInformation.CallCount;
			} else {
				missedCallCount = phoneCallInformation.CallCount;
			}
			totalCallCount = totalCallCount + phoneCallInformation.CallCount;
		}

		gvMyExperienceSignalStrenght.setTargetValue(signal);
		if (signal > 75)
			tvMyExperienceSignalStrenghtValue.setText("Excellent");
		else if (signal > 50)
			tvMyExperienceSignalStrenghtValue.setText("Good");
		else if (signal > 25)
			tvMyExperienceSignalStrenghtValue.setText("Moderate");
		else
			tvMyExperienceSignalStrenghtValue.setText("Weak");

		int  totalInPar = 0, successCallinPar = 0, dropCallinPar = 0;

		if (totalCallCount > 0) {
			totalInPar = ((incommingCallCount + missedCallCount) * 100) / totalCallCount;

			successCallinPar = ((totalCallCount - dropCallCount) * 100) / totalCallCount;

			dropCallinPar = (dropCallCount * 100) / totalCallCount;
		}

		gvMyExperienceTotalCalls.setTargetValue(totalInPar);

		gvMyExperienceSetupSuccess.setTargetValue(successCallinPar);

		gvMyExperienceDropCalls.setTargetValue(dropCallinPar);

		tvMyExperienceTotalCalls.setText("Total Calls: " + totalCallCount);

		tvMyExperienceTotalCallsCalled.setText(String.valueOf( outgoingCallCount ));
		
		tvMyExperienceTotalCallsMissed.setText(String.valueOf(missedCallCount));

		tvMyExperienceTotalCallsReceived.setText(String.valueOf(incommingCallCount));

		tvMyExperienceSetupSuccessValue.setText(successCallinPar + "%");
		tvMyExperienceDropCallsValue.setText(dropCallinPar + "%");
		
		
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
		totalInPar=0;
		if (totalSMSCount > 0) {
			totalInPar = (incommingSMSCount * 100) / totalSMSCount;
		}
		gvMyExperienceSMS.setTargetValue(totalInPar);

		tvMyExperienceSMS.setText("SMS: " + totalSMSCount);

		tvMyExperienceSMSSent.setText(String.valueOf(outgoingSMSCount));
		tvMyExperienceSMSFailed.setText(String.valueOf(missedSMSCount));
		tvMyExperienceSMSReceived.setText(String.valueOf( incommingSMSCount));

		if (phoneDataInformation != null
				&& phoneDataInformation.DownLoadSpeed > 0) {
			int currentSpeedInPar = (MyNetService.currentDownloadSpeedInKbPS * 100)
					/ phoneDataInformation.DownLoadSpeed;
			gvMyExperienceDataSpeed.setTargetValue(currentSpeedInPar);
			tvMyExperienceDataSpeedDownload
					.setText("Download: "
							+ (phoneDataInformation.DownLoadSpeed > 8 ? phoneDataInformation.DownLoadSpeed
									/ 8 + "KBPS"
									: phoneDataInformation.DownLoadSpeed
											+ "kbps"));
			tvMyExperienceDataSpeedUpload
					.setText("Upload: "
							+ (phoneDataInformation.UpLoadSpeed > 8 ? phoneDataInformation.UpLoadSpeed
									/ 8 + "KBPS"
									: phoneDataInformation.UpLoadSpeed + "kbps"));
		} else {
			gvMyExperienceDataSpeed.setTargetValue(0);
			tvMyExperienceDataSpeedDownload.setText("Download: 0kbps");
			tvMyExperienceDataSpeedUpload.setText("Upload: 0kbps");
		}
		LoadInformation();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void initilization() {

		// Tab
		tvUserExperinceStart = (TextView) findViewById(R.id.tvUserExperinceStart);
		tvUserExperinceAssistance = (TextView) findViewById(R.id.tvUserExperinceAssistance);
		tvUserExperinceHistory = (TextView) findViewById(R.id.tvUserExperinceHistory);
		
		
		tvUserExperinceStart.setOnClickListener(this);
		tvUserExperinceAssistance.setOnClickListener(this);
		tvUserExperinceHistory.setOnClickListener(this);
		
		//welcomeText = (TextView) findViewById(R.id.tvSelfcareMyExperinceUserTitle);
		
		tvExperinceFilterHour= (TextView) findViewById(R.id.tvExperinceFilterHour);
		tvExperinceFilterToday= (TextView) findViewById(R.id.tvExperinceFilterToday);
		tvExperinceFilterYesterday= (TextView) findViewById(R.id.tvExperinceFilterYesterday);
		tvExperinceFilterWeek= (TextView) findViewById(R.id.tvExperinceFilterWeek);
		
		tvExperinceFilterHour.setOnClickListener(this);
		tvExperinceFilterToday.setOnClickListener(this);
		tvExperinceFilterYesterday.setOnClickListener(this);
		tvExperinceFilterWeek.setOnClickListener(this);
		
		tvMaxSignalStrength = (TextView) findViewById(R.id.tvSignalStrenghtmaxstrengthvalue); 
		tvMinSignalStrength = (TextView) findViewById(R.id.tvSignalStrenghtminstrengthvalue);
		tvAvgSignalStrength = (TextView) findViewById(R.id.tvSignalStrenghtaveragestrengthvalue);
		
		tvMaxCallDuration = (TextView) findViewById(R.id.tvmaxdurationvalue); 
		tvMinCallDuration = (TextView) findViewById(R.id.tvmindurationvalue);
		tvAvgCallDuration = (TextView) findViewById(R.id.tvaveragedurationvalue);
		
		tvExperinceCollaboration= (TextView) findViewById(R.id.tvExperinceCollaboration);
		tvExperinceDialer= (TextView) findViewById(R.id.tvExperinceDialer);
		tvExperinceSMS= (TextView) findViewById(R.id.tvExperinceSMS);
		tvExperinceMail= (TextView) findViewById(R.id.tvExperinceMail);
		tvExperinceMemo= (TextView) findViewById(R.id.tvExperinceMemo);
		
		tvExperinceCollaboration.setOnClickListener(this);
		tvExperinceDialer.setOnClickListener(this);
		tvExperinceSMS.setOnClickListener(this);
		tvExperinceMail.setOnClickListener(this);
		tvExperinceMemo.setOnClickListener(this);
		
		
		
		//tvMaxLatency = (TextView) findViewById(R.id.tvmaxlatencyValue);
		//tvMinLatency = (TextView) findViewById(R.id.tvminlatencyvalue);
		//tvAvgLatency = (TextView) findViewById(R.id.tvaveragelatencyvalue);
		
		edphonenumber = (TextView) findViewById(R.id.edphonenumber);
		
		pbNetworkMain=(ProgressBar) findViewById(R.id.pbNetworkMain);

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapSelfcareMyExperince)).getMap();

		rlMyExperienceSignalStrenght = (RelativeLayout) findViewById(R.id.rlMyExperienceSignalStrenght);
		rlMyExperienceTotalCalls = (RelativeLayout) findViewById(R.id.rlMyExperienceTotalCalls);
		rlMyExperienceSMS = (RelativeLayout) findViewById(R.id.rlMyExperienceSMS);
		rlMyExperienceSetupSuccess = (RelativeLayout) findViewById(R.id.rlMyExperienceSetupSuccess);
		rlMyExperienceDropCalls = (RelativeLayout) findViewById(R.id.rlMyExperienceDropCalls);
		rlMyExperienceDataSpeed = (RelativeLayout) findViewById(R.id.rlMyExperienceDataSpeed);
		

		gvMyExperienceSignalStrenght = (GaugeView) findViewById(R.id.gvMyExperienceSignalStrenght);
		gvMyExperienceTotalCalls = (GaugeView) findViewById(R.id.gvMyExperienceTotalCalls);
		gvMyExperienceSMS = (GaugeView) findViewById(R.id.gvMyExperienceSMS);
		gvMyExperienceSetupSuccess = (GaugeView) findViewById(R.id.gvMyExperienceSetupSuccess);
		gvMyExperienceDropCalls = (GaugeView) findViewById(R.id.gvMyExperienceDropCalls);
		gvMyExperienceDataSpeed = (GaugeView) findViewById(R.id.gvMyExperienceDataSpeed);

		rlMyExperienceSignalStrenght.setOnClickListener(this);
		rlMyExperienceTotalCalls.setOnClickListener(this);
		rlMyExperienceSMS.setOnClickListener(this);
		rlMyExperienceSetupSuccess.setOnClickListener(this);
		rlMyExperienceDropCalls.setOnClickListener(this);
		rlMyExperienceDataSpeed.setOnClickListener(this);

		tvMyExperienceTotalCallsCalled = (TextView) findViewById(R.id.tvMyExperienceTotalCallsCalled);
		tvMyExperienceTotalCallsReceived = (TextView) findViewById(R.id.tvMyExperienceTotalCallsReceived);
		tvMyExperienceTotalCalls = (TextView) findViewById(R.id.tvMyExperienceTotalCalls);
		tvMyExperienceSignalStrenghtValue = (TextView) findViewById(R.id.tvMyExperienceSignalStrenghtValue);
		tvMyExperienceSMS = (TextView) findViewById(R.id.tvMyExperienceSMS);
		tvMyExperienceSMSSent = (TextView) findViewById(R.id.tvMyExperienceSMSSent);
		tvMyExperienceSMSReceived = (TextView) findViewById(R.id.tvMyExperienceSMSReceived);
		tvMyExperienceSetupSuccessValue = (TextView) findViewById(R.id.tvMyExperienceSetupSuccessValue);
		tvMyExperienceDropCallsValue = (TextView) findViewById(R.id.tvMyExperienceDropCallsValue);
		tvMyExperienceDataSpeedDownload = (TextView) findViewById(R.id.tvMyExperienceDataSpeedDownload);
		tvMyExperienceDataSpeedUpload = (TextView) findViewById(R.id.tvMyExperienceDataSpeedUpload);
		tvMyExperienceTotalCallsMissed= (TextView) findViewById(R.id.tvMyExperienceTotalCallsMissed);
		tvMyExperienceSMSFailed= (TextView) findViewById(R.id.tvMyExperienceSMSFailed);

		ivNetworkServiceUsage = (ImageView) findViewById(R.id.ivNetworkServiceUsage);
		ivNetworkMobileUsageSignal= (ImageView) findViewById(R.id.ivNetworkMobileUsageSignal);
		
		ivNetworkMobileUsageCall= (ImageView) findViewById(R.id.ivNetworkMobileUsageCall);
		
		
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
		cvNetworkUsageHistory.setOnDateChangeListener(this);
	}

	private void initializeMap() {
		try{
		
		Bitmap defaultBitmap, defaultBitmapUser;

		double defaultLatitude = 0, defaultLongitude = 0;

		if (MyNetService.currentLocation != null) {
			LatLng Location = new LatLng(
					MyNetService.currentLocation.getLatitude(),
					MyNetService.currentLocation.getLongitude());
			defaultLatitude = Location.latitude;
			defaultLongitude = Location.longitude;
			if(profilePicture != null){
				defaultBitmapUser = profilePicture;
			}else{
				defaultBitmapUser = BitmapFactory.decodeResource(getResources(),
						R.drawable.user_icon);
			}
			defaultBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.google_custom_pin);
			
			defaultBitmapUser = Bitmap.createScaledBitmap(defaultBitmapUser, defaultBitmap.getWidth() - 12,
					defaultBitmap.getHeight() - 40, true);
			Bitmap bmp = Bitmap.createBitmap(defaultBitmap.getWidth(), defaultBitmap.getHeight(),
					defaultBitmap.getConfig());
			Canvas canvas = new Canvas(bmp);
			canvas.drawBitmap(defaultBitmap, new Matrix(), null);
			canvas.drawBitmap(defaultBitmapUser, 8, 10, null);
			map.addMarker(new MarkerOptions().position(Location).title(CommonValues.getInstance().LoginUser.FullName()
							+ ", Mobile:"
							+ MyNetService.phoneBasicInformation.MobileNo)
					.icon(BitmapDescriptorFactory.fromBitmap(bmp)));

		}

		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);

		map.moveCamera(CameraUpdateFactory
				.newLatLngZoom(Defaultlocation, 14.0f));

		map.animateCamera(CameraUpdateFactory.zoomIn());

		map.animateCamera(CameraUpdateFactory.zoomTo(10));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(Defaultlocation).zoom(17).bearing(90).tilt(30).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlMyExperience) {
			Intent intent = new Intent(this,
					NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlMyExperienceSignalStrenght) {
			Intent intent = new Intent(this,
					PhoneSignalStrengthDetailsActivity.class);
			intent.putExtra("MyExperienceSignalStrength", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlMyExperienceTotalCalls) {
			Intent intent = new Intent(this, PhoneCallDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlMyExperienceSMS) {
			Intent intent = new Intent(this,
					PhoneSignalStrengthDetailsActivity.class);
			intent.putExtra("MyExperienceSMS", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlMyExperienceSetupSuccess) {
			Intent intent = new Intent(this, DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlMyExperienceDropCalls) {
			Intent intent = new Intent(this, DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			
		} else if (view.getId() == R.id.rlMyExperienceDataSpeed) {
			Intent intent = new Intent(this,
					PhoneSignalStrengthDetailsActivity.class);
			intent.putExtra("MyExperienceDataSpeed", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);			
		} else if (view.getId() == R.id.rlMyExperience) {
			Intent intent = new Intent(this, DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.tvUserExperinceStart) {
			Intent intent = new Intent(this,
					NetworkSelfcareMyExperinceShowMoreActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.tvUserExperinceAssistance) {
			Intent intent = new Intent(this, AssistanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} /*else if (view.getId() == R.id.tvUserExperinceHistory) {
			Intent intent = new Intent(this, StatisticsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} */ else if (view.getId() == R.id.tvExperinceFilterHour) {
			arrangeLastHourTab();
			setSpedometer(CommonConstraints.INFO_TYPE_LASTHOUR);
		} else if (view.getId() == R.id.tvExperinceFilterToday) {
			arrangeTodayTab();
			setSpedometer(CommonConstraints.INFO_TYPE_TODAY);
		} else if (view.getId() == R.id.tvExperinceFilterYesterday) {
			arrangeYesterdayTab();
			setSpedometer(CommonConstraints.INFO_TYPE_YESTERDAY);
		} else if (view.getId() == R.id.tvExperinceFilterWeek) {
			arrangeWeekTab();
			setSpedometer(CommonConstraints.INFO_TYPE_WEEK);
		}else if (view.getId() == R.id.tvExperinceCollaboration) {
			Intent intent = new Intent(this,
					CollaborationMainFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (view.getId() == R.id.tvExperinceDialer) {
			Intent intent = new Intent(this,MyNetAllCallDetails.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent); 
		}else if (view.getId() == R.id.tvExperinceSMS) {
			/*Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setType("vnd.android-dir/mms-sms"); 
			startActivity(intent); */
			Intent intent = new Intent(this,
					MyNetSMSSenderActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (view.getId() == R.id.tvExperinceMail) {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MyNet");
			emailIntent.setType("message/rfc822");
			startActivity(Intent.createChooser(emailIntent,"Send"));
		}else if (view.getId() == R.id.tvExperinceMemo) {
			Intent intent = new Intent(this,
					CallMemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	
	private void arrangeLastHourTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));	
	}
	
	private void arrangeTodayTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));	
	}
	
	private void arrangeYesterdayTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));	
	}
	private void arrangeWeekTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.header_text));	
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void showProgressLoader() {
		pbNetworkMain.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pbNetworkMain.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		if(downloadType == DOWNLOAD_MAP){
			IFacebookManager facebookManager = new FacebookManager();
			FacebookPersons facebookPersons = null;
			facebookPersons = facebookManager.GetFacebookProfilePicture();
			if(facebookPersons != null){
				try{
					URL url = new URL(facebookPersons.facebokPerson.PP_Path);
					profilePicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					welcomeText.setText(facebookPersons.facebokPerson.Name);
				}catch (Exception e) {
					e.printStackTrace();
				}			
			}
			return facebookPersons;
		}
		else if (downloadType == DOWNLOAD_MOBILE_USAGES) {
			ArrayList<Bitmap> imageList=null;
			int less50=0,_50to70=0,_70to_85=0,_85to95=0,greter95=0,perc=0,total=0;
			
			if(phoneSignalStrenghtList.size()>0){
				for (PhoneSignalStrenght phoneSignalStrenght : phoneSignalStrenghtList) {
					perc=(int) (phoneSignalStrenght.SignalLevel * 100) / 31;
					if(perc<50){
						less50++;
					}else if(perc<70){
						_50to70++;
					}else if(perc<85){
						_70to_85++;
					}else if(perc<95){
						_85to95++;
					}else {
						greter95++;
					}
					total++;
				}
				
				String url=CommonURL.getInstance().GoogleBarChartUrl;				
				url=url+"chxl=0:|0~50|50~70|70~85|85~95|95>";
				url=url+"&chd=t:"+(int)(less50*100)/total+","+(int)(_50to70*100)/total+","+(int)(_70to_85*100)/total+","+(int)(_85to95*100)/total+","+(int)(greter95*100)/total;
				Bitmap signalSt=JSONfunctions.LoadChart(url);
				imageList=new ArrayList<Bitmap>();
				imageList.add(signalSt);	
			}
			
			if(phoneCallInformationList.size()>0){
				total=0;
				less50=0;_50to70=0;_70to_85=0;_85to95=0;greter95=0;
				for (PhoneCallInformation phoneSignalStrenght : phoneCallInformationList) {
					if(phoneSignalStrenght.DurationInSec<1)
						continue;
					if(phoneSignalStrenght.DurationInSec<=60){
						less50++;
					}else if(phoneSignalStrenght.DurationInSec<=120){
						_50to70++;
					}else if(phoneSignalStrenght.DurationInSec<=180){
						_70to_85++;
					}else if(phoneSignalStrenght.DurationInSec<=300){
						_85to95++;
					}else {
						greter95++;
					}
					total++;
				}
				
				String url=CommonURL.getInstance().GoogleBarChartUrl;
				
				url=url+"chxl=0:|0~60|60~120|120~180|180~300|300>";
				url=url+"&chd=t:"+(int) Math.round((less50*100)/total)+","+(int)Math.round((_50to70*100)/total)+","+(int)Math.round((_70to_85*100)/total)+","+(int)Math.round((_85to95*100)/total)+","+(int)Math.round((greter95*100)/total);
				Bitmap signalSt=JSONfunctions.LoadChart(url);				
				imageList.add(signalSt);
			}
			return imageList;
			
		}else if (downloadType == DOWNLOAD_SERVICE_USAGES) {
			try {
				incommingCallCount=0;missedSMSCount=0;outgoingCallCount=0;totalSMSCount=0;dataCount=0;
				MyNetDatabase myNetDatabase = new MyNetDatabase(this);			
				myNetDatabase.open();
				ArrayList<PhoneCallInformation> phoneCallList = myNetDatabase.getTotalCallInfo(0);
				ArrayList<PhoneSMSInformation> phoneSmsList = myNetDatabase.getTotalSMSInfo(0);
				dataCount = myNetDatabase.getTotalDataCount();
				myNetDatabase.close();
				
				for (PhoneCallInformation phoneCallInformation : phoneCallList) {
					if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE) {
						incommingCallCount = phoneCallInformation.CallCount;
					} else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE) {
						outgoingCallCount = phoneCallInformation.CallCount;
					} else {
						missedCallCount = phoneCallInformation.CallCount;
					}
					totalCallCount = totalCallCount + phoneCallInformation.CallCount;
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
				
				ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
				
				
				String urlRqs3DPie = String.format(
						CommonURL.getInstance().GoogleChartServiceUsage,
						URLEncoder.encode("Calls received("+String.valueOf(incommingCallCount+missedSMSCount)+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("Calls made("+String.valueOf(outgoingCallCount)+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("Calls dropped(0)", CommonConstraints.EncodingCode),
						URLEncoder.encode("Calls setup failure(0)", CommonConstraints.EncodingCode),
						URLEncoder.encode("Messages("+String.valueOf(totalSMSCount)+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("Data Connections("+String.valueOf(dataCount)+")", CommonConstraints.EncodingCode),
						URLEncoder.encode("Active apps("+String.valueOf(am.getRunningAppProcesses().size())+")", CommonConstraints.EncodingCode),
						incommingCallCount+missedSMSCount,outgoingCallCount,0,0,totalSMSCount,dataCount, am.getRunningAppProcesses().size(),
						incommingCallCount+missedSMSCount,outgoingCallCount,0,0,totalSMSCount,dataCount,am.getRunningAppProcesses().size()
						);

				return JSONfunctions.LoadChart(urlRqs3DPie);
			} catch (Exception e) {
				String ss=e.getMessage();
				ss=ss+e.getMessage();
			}
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (downloadType == DOWNLOAD_SERVICE_USAGES) {			
				ivNetworkServiceUsage.setImageBitmap((Bitmap) data);				
			}else if (downloadType == DOWNLOAD_MOBILE_USAGES) {	
				
				ivNetworkMobileUsageSignal.setImageBitmap(((ArrayList<Bitmap>) data).get(0));
				ivNetworkMobileUsageSignal.setScaleType(ImageView.ScaleType.FIT_XY);				
				ivNetworkMobileUsageCall.setImageBitmap(((ArrayList<Bitmap>) data).get(1));
				ivNetworkMobileUsageCall.setScaleType(ImageView.ScaleType.FIT_XY);
			}else if(downloadType == DOWNLOAD_MAP){
				initializeMap();
			}
		}else{
			initializeMap();	
		}
	}

	
	
	
	private void setTextViewText(int id,String text) {
        ((TextView)findViewById(id)).setText(text);
    }	
	
	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
		String value = DateUtils.formatDateTime(
				NetworkSelfCareMyExperienceActivity.this, cal.getTimeInMillis(), 
				DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
		pressedDate = cal.getTimeInMillis();
		tvDateTimeViewHeader.setText(value + " day average");
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		networkUsageHistoryList = database.getUsersHistry(pressedDate);
		database.close();
		LoadInformation();
	}
	public void LoadInformation(){
		
		if(networkUsageHistoryList.size()>0){
			ArrayList<PhoneCallInformation>phoneCallList= (ArrayList<PhoneCallInformation>) networkUsageHistoryList.get(0);
			ArrayList<PhoneSMSInformation>phoneSmsList= (ArrayList<PhoneSMSInformation>) networkUsageHistoryList.get(1);
			int callReceived=0, callMade=0, callDroped=0,CallSetupfail=0, SMSSent=0, SMSReceived=0, MaxStrength=0, MinStrength=0, AvgStrength=0,
					MaxDuration=0, MinDuration=0, AvgDuration=0, MaxLatency=0, MinLatency=0, AvgLatency=0;
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
			MyNetDatabase database = new MyNetDatabase(this);
			database.open();
			MaxStrength = database.getMaxSignalStrength(pressedDate);
			MinStrength = database.getMinSignalStrength(pressedDate);
			AvgStrength = database.getAgvSignalStrenght(pressedDate);
			
			MaxDuration = database.getMaxCallDuration(pressedDate);
			MinDuration = database.getMinCallDuration(pressedDate);
			AvgDuration = database.getAvgCallDuration(pressedDate);
			database.close();
			
			tvNetworkUsageHistoryCallReceived.setText(String.valueOf(callReceived));
			tvNetworkUsageHistoryCallMade.setText(String.valueOf(callMade));
			tvNetworkUsageHistoryCallDroped.setText(String.valueOf(0));
			tvNetworkUsageHistoryCallSetupFail.setText(String.valueOf(0));
			
			tvNetworkUsageHistorySMSSent.setText(String.valueOf(SMSSent));
			tvNetworkUsageHistorySMSReceived.setText(String.valueOf(SMSReceived));
			
			tvNetworkUsageHistoryMaxStrength.setText(String.valueOf(MaxStrength));
			tvNetworkUsageHistoryMinStrength.setText(String.valueOf(MinStrength));
			tvNetworkUsageHistoryAvgStrength.setText(String.valueOf(AvgStrength));
			
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
    
    /*
     * 
     * */
    private void startSignalLevelListener() {
	
    	
    	TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        
		int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTH | 
                                 PhoneStateListener.LISTEN_DATA_ACTIVITY | 
                                 PhoneStateListener.LISTEN_CELL_LOCATION |
                                 PhoneStateListener.LISTEN_CALL_STATE |
                                 PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                                 PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                                 PhoneStateListener.LISTEN_SERVICE_STATE;
        
        tm.listen(phoneListener, events);
        
	}
    
    
    /*
     * De-register the telephony events 
     *  
     * */
    private void StopListener() {
    	TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    	tm.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
    }
    
    
    /*
     * Display the telephony related information
     * */
    private void displayTelephonyInfo() {
	
    	
    	//access to the telephony services
    	TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    	
    	//Get the IMEI code
        String deviceid = tm.getDeviceId();
        //Get  the phone number string for line 1, for example, the MSISDN for a GSM phone
        String phonenumber = tm.getLine1Number();
        //Get  the software version number for the device, for example, the IMEI/SV for GSM phones
        String softwareversion = tm.getDeviceSoftwareVersion();
        //Get  the alphabetic name of current registered operator. 
        String operatorname = tm.getNetworkOperatorName();
        //Get  the ISO country code equivalent for the SIM provider's country code.
        String simcountrycode = tm.getSimCountryIso();
        //Get  the Service Provider Name (SPN). 
        String simoperator = tm.getSimOperatorName();
        //Get  the serial number of the SIM, if applicable. Return null if it is unavailable. 
        String simserialno = tm.getSimSerialNumber();
        //Get  the unique subscriber ID, for example, the IMSI for a GSM phone
        String subscriberid = tm.getSubscriberId();
        //Get the type indicating the radio technology (network type) currently in use on the device for data transmission.
        //EDGE,GPRS,UMTS  etc
        String networktype =PhoneBasicInformation.getNetworkTypeString(tm.getNetworkType());
        //indicating the device phone type. This indicates the type of radio used to transmit voice calls
        //GSM,CDMA etc
        String phonetype = getPhoneTypeString(tm.getPhoneType());
        List<NeighboringCellInfo> nn=tm.getNeighboringCellInfo();
        
        
        String deviceinfo = "";
        
        deviceinfo += ("Device ID: " + deviceid + "\n");
        deviceinfo += ("Phone Number: " + phonenumber + "\n");
        deviceinfo += ("Software Version: " + softwareversion + "\n");
        deviceinfo += ("Operator Name: " + operatorname + "\n");
        deviceinfo += ("SIM Country Code: " + simcountrycode + "\n");
        deviceinfo += ("SIM Operator: " + simoperator + "\n");
        deviceinfo += ("SIM Serial No.: " + simserialno + "\n");
        deviceinfo += ("Subscriber ID: " + subscriberid + "\n");
        deviceinfo += ("Network Type: " + networktype + "\n");
        deviceinfo += ("Phone Type: " + phonetype + "\n");
        if(nn.size()>0)
        	deviceinfo += ("Neighboringcell: " + nn.get(0).toString() + "\n");
        
        
        setTextViewText(info_ids[INFO_DEVICE_INFO_INDEX],deviceinfo);
        
	}
    
    private String getLocationAddress(){
    	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		String addressText="";
		if(loc!=null){
			
							
			Geocoder geocoder =
	                new Geocoder(this, Locale.getDefault());
			
			try {
				List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
				if (addresses != null && addresses.size() > 0) {			                
	                Address address = addresses.get(0);			                
	                for (int  lineIndex=0 ;lineIndex<address.getMaxAddressLineIndex();lineIndex++) {
	                	addressText=addressText+address.getAddressLine(lineIndex)+", ";
					}
	                addressText=addressText+address.getLocality()+", "+address.getCountryName();			               
	            } 
				
				
			}catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return addressText;
    }
    
    
    private void setDataDirection(int id, int direction){
        int resid = getDataDirectionRes(direction);
        //((TextView)findViewById(id)).setCompoundDrawables(null, null,getResources().getDrawable(resid), null);
        ((ImageView)findViewById(id)).setImageResource(resid);
    }

	private int getDataDirectionRes(int direction){
	        int resid = R.drawable.nodata;
	        
	        switch(direction)
	        {
	                case TelephonyManager.DATA_ACTIVITY_IN:    		resid = R.drawable.indata;break;
	                case TelephonyManager.DATA_ACTIVITY_OUT:        resid = R.drawable.outdata; break;
	                case TelephonyManager.DATA_ACTIVITY_INOUT:      resid = R.drawable.bidata; break;
	                case TelephonyManager.DATA_ACTIVITY_NONE:       resid = R.drawable.nodata; break;
	                default: 
	                	resid = R.drawable.nodata; break;
	        }
	        
	        
	        
	        return resid;
	}

    private void setSignalLevel(int id,int infoid,int level){
    	
        int progress = (int) ((((float)level)/31.0) * 100);
        
        String signalLevelString = getSignalLevelString(progress);
        
        //set the status 
        ((ProgressBar)findViewById(id)).setProgress(progress);
        
        //set the status string
        ((TextView)findViewById(infoid)).setText(signalLevelString);
        
        Log.i("signalLevel ","" + progress);
    }

    
    private String getSignalLevelString(int level) {
    	
        String signalLevelString = "Weak";
        
        if(level > EXCELLENT_LEVEL)             signalLevelString = "Excellent";
        else if(level > GOOD_LEVEL)             signalLevelString = "Good";
        else if(level > MODERATE_LEVEL) 		signalLevelString = "Moderate";
        else if(level > WEAK_LEVEL)             signalLevelString = "Weak";
        
        return signalLevelString;
    }
    
	private String getPhoneTypeString(int type){
	        String typeString = "Unknown";
	        
	        switch(type)
	        {
	                case TelephonyManager.PHONE_TYPE_GSM:   typeString = "GSM"; break;
	                case TelephonyManager.PHONE_TYPE_NONE:  typeString = "UNKNOWN"; break;
	                default: 
	                	typeString = "UNKNOWN"; break;
	        }
	        
	        return typeString;
	}


	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phonestatus, menu);
        return true;
    }*/
	
	
	private final PhoneStateListener phoneListener = new  PhoneStateListener(){
		
		/*
		 * call fwding
		 * */
		public void onCallForwardingIndicatorChanged(boolean cfi){
			
			super.onCallForwardingIndicatorChanged(cfi);
		}
		
		/*
		 * Call State Changed 
		 * */
		public void onCallStateChanged(int state, String incomingNumber) {
			
			String phoneState = "UNKNOWN";
			
			switch(state){
			
			case TelephonyManager.CALL_STATE_IDLE : 	
				phoneState = "IDLE";
					break;
			case TelephonyManager.CALL_STATE_RINGING :  
				phoneState = "Ringing (" + incomingNumber + ") "; 
					break;
			case TelephonyManager.CALL_STATE_OFFHOOK : 
				phoneState = "Offhook";
					break;
														
			}
			
			setTextViewText(info_ids[INFO_CALL_STATE_INDEX], phoneState);
			super.onCallStateChanged(state, incomingNumber);
			
		}
		
		
		/*
		 * Cell location changed event handler
		 * */
		public void onCellLocationChanged(CellLocation location) {
			
			String strLocation = location.toString()+"\n"+getLocationAddress();			
			
			setTextViewText(info_ids[INFO_CELL_LOCATION_INDEX], strLocation);
			
			super.onCellLocationChanged(location);
		}
		
		
		/*
		 * Cellphone data connection status 
		 * */
		public void onDataConnectionStateChanged(int state) {
		
			String phoneState = "UNKNOWN";
			
			switch(state){
			
			case TelephonyManager.DATA_CONNECTED : 	
				phoneState = "Connected";
					break;
			case TelephonyManager.DATA_CONNECTING :  
				phoneState = "Connecting.."; 
					break;
			case TelephonyManager.DATA_DISCONNECTED : 
				phoneState = "Disconnected";
					break;
			case TelephonyManager.DATA_SUSPENDED : 
				phoneState = "Suspended";
					break;				
			}
			
			setTextViewText(info_ids[INFO_CONNECTION_STATE_INDEX], phoneState);
			
			super.onDataConnectionStateChanged(state);
		}
		
		
		/*
		 * Data activity handler
		 * */
		public void onDataActivity(int direction) {
			
			String strDirection = "NONE";
			
			switch(direction){
			
			case TelephonyManager.DATA_ACTIVITY_IN :
				strDirection = "IN";
				break;
			case TelephonyManager.DATA_ACTIVITY_INOUT:
				strDirection = "IN-OUT";
				break;
			case TelephonyManager.DATA_ACTIVITY_DORMANT:
				strDirection = "Dormant";
				break;
			case TelephonyManager.DATA_ACTIVITY_NONE:
				strDirection="NONE";
				break;
			case TelephonyManager.DATA_ACTIVITY_OUT:
				strDirection="OUT";
				break;
			
			}
			
			setDataDirection(info_ids[INFO_DATA_DIRECTION_INDEX],direction);
			
			super.onDataActivity(direction);
		}
		
		
		/*
		 * Cellphone Service status 
		 * */
		public void onServiceStateChanged(ServiceState serviceState) {
			
			String strServiceState = "NONE";
			
			
			switch(serviceState.getState()){
			
			case ServiceState.STATE_EMERGENCY_ONLY:
				strServiceState = "Emergency";
				break;
				
			case ServiceState.STATE_IN_SERVICE:
				strServiceState = "In Service";
				break;
			case ServiceState.STATE_OUT_OF_SERVICE:
				strServiceState = "Out of Service";
				break;
			case ServiceState.STATE_POWER_OFF:
				strServiceState = "Power off";
				break;
			}
			
			setTextViewText(info_ids[INFO_SERVICE_STATE_INDEX], strServiceState);
			
			super.onServiceStateChanged(serviceState);
			
		}
		
		/*
		 * 
		 * */
		public void onSignalStrengthChanged(int asu) {
			
			setSignalLevel(info_ids[INFO_SIGNAL_LEVEL_INDEX], info_ids[INFO_SIGNAL_LEVEL_INFO_INDEX],asu);
			
			super.onSignalStrengthChanged(asu);
		}
		
	};	
	
	
	
}
