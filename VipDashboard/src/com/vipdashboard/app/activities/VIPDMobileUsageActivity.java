package com.vipdashboard.app.activities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ProgressDialog;
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

public class VIPDMobileUsageActivity extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask {

	// Button management, technical, tticket;
	// RelativeLayout
	// rlDashboardAcquisition,rlDashboardRetention,rlDashboardEfficiency,rlDashboardTraffic,rlDashboardMTR,rlDashboardSelfService;
	// TextView
	// tvDashboardMessage,tvDashboardAcquisitionValue,tvDashboardRetentionValue,tvDashboardEfficiencyValue,tvDashboardTrafficValue,tvDashboardMTRValue,tvDashboardSelfServiceValue;

	// TextView
	// tvDashboardFirstTitle,tvDashboardSecondTitle,tvDashboardThirdTitle,tvDashboardForthTitle,tvDashboardFifthTitle,tvDashboardSixthTitle;

	// RelativeLayout rlDashboardReportManagement, rlDashboardReportTechnical,
	// rlDashboardTroubleTicket;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;

	// int overviewID=0;
	// String overviewLineChartText=null;
	// TextView tvDeshboardReportTitle;
	// TextView txtExperienceView;
	ImageView ivChartView;

	// ImageView
	// ivNetworkServiceUsage,ivNetworkMobileUsageSignal,ivNetworkMobileUsageCall;

	ImageView ivNetworkMobileUsageSignal, ivNetworkMobileUsageCall;

	int incommingCallCount = 0, outgoingCallCount = 0, missedCallCount = 0,
			totalCallCount = 0, dropCallCount = 0, incommingSMSCount = 0,
			outgoingSMSCount = 0, missedSMSCount = 0, totalSMSCount = 0;

	ArrayList<PhoneSignalStrenght> phoneSignalStrenghtList = null;
	ArrayList<PhoneCallInformation> phoneCallInformationList = null;
	// ArrayList<Object> networkUsageHistoryList =null;

	// TextView
	// tvExperinceFilterHour,tvExperinceFilterToday,tvExperinceFilterYesterday,tvExperinceFilterWeek;

	TextView edphonenumber;

	TextView tvMaxSignalStrength, tvMinSignalStrength, tvAvgSignalStrength,
			tvMaxCallDuration, tvMinCallDuration, tvAvgCallDuration,
			tvMaxLatency, tvMinLatency, tvAvgLatency;

	// TextView welcomeText;

	TextView tvVIPDMaps, tvVIPNetwork, tvUsage;

	// ,tvApplication,tvUsage,tvVIPServices;
	// ,tvSpeedTest;

	// Bitmap profilePicture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_mobile_usage);

		// mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		initialization();
		setInfo();
		LoadInformation();
	}

	private void initialization() {

		tvMaxSignalStrength = (TextView) findViewById(R.id.tvSignalStrenghtmaxstrengthvalue);
		tvMinSignalStrength = (TextView) findViewById(R.id.tvSignalStrenghtminstrengthvalue);
		tvAvgSignalStrength = (TextView) findViewById(R.id.tvSignalStrenghtaveragestrengthvalue);

		tvMaxCallDuration = (TextView) findViewById(R.id.tvmaxdurationvalue);
		tvMinCallDuration = (TextView) findViewById(R.id.tvmindurationvalue);
		tvAvgCallDuration = (TextView) findViewById(R.id.tvaveragedurationvalue);

		// ivNetworkServiceUsage = (ImageView)
		// findViewById(R.id.ivNetworkServiceUsage);
		ivNetworkMobileUsageSignal = (ImageView) findViewById(R.id.ivNetworkMobileUsageSignal);

		ivNetworkMobileUsageCall = (ImageView) findViewById(R.id.ivNetworkMobileUsageCall);

		tvVIPDMaps = (TextView) findViewById(R.id.tvVIPDMaps);
		tvVIPNetwork = (TextView) findViewById(R.id.tvVIPNetwork);

		// tvVIPServices = (TextView) findViewById(R.id.tvVIPServices);
		// tvApplication = (TextView) findViewById(R.id.tvApplication);
		tvUsage = (TextView) findViewById(R.id.tvUsage);
		// tvSpeedTest = (TextView) findViewById(R.id.tvSpeedTest);

		tvVIPDMaps.setOnClickListener(this);
		tvVIPNetwork.setOnClickListener(this);
		// tvVIPServices.setOnClickListener(this);
		// tvApplication.setOnClickListener(this);
		tvUsage.setOnClickListener(this);
		// tvSpeedTest.setOnClickListener(this);
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
			CommonTask.DryConnectivityMessage(this,
					"Mobile SIM card is not installed.\nPlease install it.");
		} else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask
					.DryConnectivityMessage(this,
							"No Internet Connection.\nPlease enable your connection first.");
		} else if (!CommonTask.isMyServiceRunning(this))
			startService(new Intent(this, MyNetService.class));
		else if (CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION) {
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,
					CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
			}
		}
		// tvDashboardMessage.setText("Update as of ");

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
		} else if (view.getId() == R.id.tvSpeedTest) {
			Intent intent = new Intent(this, VIPD_SpeedTestActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	private void LoadInformation() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Graph processing...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		phoneSignalStrenghtList = database.getSignalStrenghtList();
		phoneCallInformationList = database.getCallInfoList();
		database.close();
		ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
		int less50 = 0, _50to70 = 0, _70to_85 = 0, _85to95 = 0, greter95 = 0, perc = 0, total = 0;

		if (phoneSignalStrenghtList.size() > 0) {
			for (PhoneSignalStrenght phoneSignalStrenght : phoneSignalStrenghtList) {
				perc = (int) (phoneSignalStrenght.SignalLevel * 100) / 31;
				if (perc < 50) {
					less50++;
				} else if (perc < 70) {
					_50to70++;
				} else if (perc < 85) {
					_70to_85++;
				} else if (perc < 95) {
					_85to95++;
				} else {
					greter95++;
				}
				total++;
			}

			String url = CommonURL.getInstance().GoogleBarChartUrl;
			url = url + "chxl=0:|0~50|50~70|70~85|85~95|95>";
			url = url + "&chd=t:" + (int) (less50 * 100) / total + ","
					+ (int) (_50to70 * 100) / total + ","
					+ (int) (_70to_85 * 100) / total + ","
					+ (int) (_85to95 * 100) / total + ","
					+ (int) (greter95 * 100) / total;
			Bitmap signalSt = JSONfunctions.LoadChart(url);

			imageList.add(signalSt);
		}

		if (phoneCallInformationList.size() > 0) {
			total = 0;
			less50 = 0;
			_50to70 = 0;
			_70to_85 = 0;
			_85to95 = 0;
			greter95 = 0;
			for (PhoneCallInformation phoneSignalStrenght : phoneCallInformationList) {
				if (phoneSignalStrenght.DurationInSec < 1)
					continue;
				if (phoneSignalStrenght.DurationInSec <= 60) {
					less50++;
				} else if (phoneSignalStrenght.DurationInSec <= 120) {
					_50to70++;
				} else if (phoneSignalStrenght.DurationInSec <= 180) {
					_70to_85++;
				} else if (phoneSignalStrenght.DurationInSec <= 300) {
					_85to95++;
				} else {
					greter95++;
				}
				total++;
			}

			String url = CommonURL.getInstance().GoogleBarChartUrl;

			url = url + "chxl=0:|0~60|60~120|120~180|180~300|300>";
			url = url + "&chd=t:" + (int) Math.round((less50 * 100) / total)
					+ "," + (int) Math.round((_50to70 * 100) / total) + ","
					+ (int) Math.round((_70to_85 * 100) / total) + ","
					+ (int) Math.round((_85to95 * 100) / total) + ","
					+ (int) Math.round((greter95 * 100) / total);
			Bitmap signalSt = JSONfunctions.LoadChart(url);
			imageList.add(signalSt);
		}

		return imageList;

	}

	private void setInfo() {
		try {
			incommingCallCount = 0;
			outgoingCallCount = 0;
			missedCallCount = 0;
			totalCallCount = 0;
			dropCallCount = 0;
			incommingSMSCount = 0;
			outgoingSMSCount = 0;
			missedSMSCount = 0;
			totalSMSCount = 0;
			// dataCount=0;
			MyNetDatabase myNetDatabase = new MyNetDatabase(this);
			int signal = 0;
			myNetDatabase.open();
			signal = (int) (myNetDatabase
					.getAgvSignalStrenght(CommonConstraints.INFO_TYPE_TODAY) * 100) / 31;
			ArrayList<PhoneCallInformation> phoneCallList = myNetDatabase
					.getTotalCallInfo(CommonConstraints.INFO_TYPE_TODAY);
			ArrayList<PhoneSMSInformation> phoneSmsList = myNetDatabase
					.getTotalSMSInfo(CommonConstraints.INFO_TYPE_TODAY);
			// PhoneDataInformation phoneDataInformation =
			// myNetDatabase.getAgvDownLoadUploadSpeed(CommonConstraints.INFO_TYPE_TODAY);

			// dataCount = myNetDatabase.getTotalDataCount();

			int maxSignalStrength = myNetDatabase.getMaxSignalStrenght();

			if (maxSignalStrength == 99) {
				maxSignalStrength = 0;
			}

			maxSignalStrength = -113 + (2 * maxSignalStrength);

			int minSignalStrength = myNetDatabase.getminSignalStrenght();

			if (minSignalStrength == 99) {
				minSignalStrength = 0;
			}
			minSignalStrength = -113 + (2 * minSignalStrength);

			int avgSignalS = myNetDatabase.getAgvSignalStrenght(0);
			avgSignalS = -113 + (2 * avgSignalS);

			tvMaxSignalStrength.setText(String.valueOf(maxSignalStrength));
			tvMinSignalStrength.setText(String.valueOf(minSignalStrength));
			tvAvgSignalStrength.setText(String.valueOf(avgSignalS));
			int hour = 0, min = 0, sec = 0, duration = 0;
			duration = myNetDatabase.getMaxCallDuration();
			sec = duration % 60;
			min = duration / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			NumberFormat formatter = new DecimalFormat("00");
			String durationText = formatter.format(hour) + ":"
					+ formatter.format(min) + ":" + formatter.format(sec);

			tvMaxCallDuration.setText(durationText);

			hour = 0;
			min = 0;
			sec = 0;
			duration = 0;
			duration = myNetDatabase.getMinCallDuration();
			sec = duration % 60;
			min = duration / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			formatter = new DecimalFormat("00");
			durationText = formatter.format(hour) + ":" + formatter.format(min)
					+ ":" + formatter.format(sec);
			tvMinCallDuration.setText(durationText);

			hour = 0;
			min = 0;
			sec = 0;
			duration = 0;
			duration = myNetDatabase.getAvgCallDuration();
			sec = duration % 60;
			min = duration / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			formatter = new DecimalFormat("00");
			durationText = formatter.format(hour) + ":" + formatter.format(min)
					+ ":" + formatter.format(sec);

			tvAvgCallDuration.setText(durationText);

			phoneSignalStrenghtList = myNetDatabase.getSignalStrenghtList();
			phoneCallInformationList = myNetDatabase.getCallInfoList();

			/*
			 * String value = DateUtils.formatDateTime(
			 * VIPDMobileUsageActivity.this,
			 * Calendar.getInstance().getTimeInMillis(),
			 * DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
			 */

			myNetDatabase.close();

			for (PhoneCallInformation phoneCallInformation : phoneCallList) {
				if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE) {
					incommingCallCount = phoneCallInformation.CallCount;
				} else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE) {
					outgoingCallCount = phoneCallInformation.CallCount;
				} else {
					missedCallCount = phoneCallInformation.CallCount;
				}
				totalCallCount = totalCallCount
						+ phoneCallInformation.CallCount;
			}

			int totalInPar = 0, successCallinPar = 0, dropCallinPar = 0;

			if (totalCallCount > 0) {
				totalInPar = ((incommingCallCount + missedCallCount) * 100)
						/ totalCallCount;

				successCallinPar = ((totalCallCount - dropCallCount) * 100)
						/ totalCallCount;

				dropCallinPar = (dropCallCount * 100) / totalCallCount;
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
			totalInPar = 0;
			if (totalSMSCount > 0) {
				totalInPar = (incommingSMSCount * 100) / totalSMSCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			ivNetworkMobileUsageSignal
					.setImageBitmap(((ArrayList<Bitmap>) data).get(0));
			ivNetworkMobileUsageSignal.setScaleType(ImageView.ScaleType.FIT_XY);
			if (((ArrayList<Bitmap>) data).size() > 1) {
				ivNetworkMobileUsageCall
						.setImageBitmap(((ArrayList<Bitmap>) data).get(1));
				ivNetworkMobileUsageCall
						.setScaleType(ImageView.ScaleType.FIT_XY);
			}
			if (CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION) {
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,
						CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
				}
			}
		}
	}
}
