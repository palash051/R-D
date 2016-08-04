package com.vipdashboard.app.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
/*import com.vipdashboard.app.classes.AppList;*/
import com.vipdashboard.app.classes.AppList;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class AssistanceReportDetalisActivity extends MainActionbarBase
		implements OnClickListener, IAsynchronousTask, LocationListener {
	boolean isCallFromCall, isCallFromData, isCallFromText, isCallFromDevice,
			isCallFromApps;
	RelativeLayout rlCallLayout, rlDataLayout, rlTextLayout, rlDeviceLayout,
			rlAppLayout;
	RelativeLayout rlCallblocked, rlDropedCall, rlPoorVoice, rlMissedCall,
			rlGoodPerfCall, rlSlowNetData, rlCannotConnData, rlDropedConnData,
			rlGoodPerfData, rlFailedText, rlSlowDelText, rlMissedIncText,
			rlGoodPerfText, rlDevCrashDevice, rlHangDevice, rlSlowDevice,
			rlBatteryDevice, rlGoodPrefText, rlCrashApp, rlHangApp, rlSlowApp,
			rlBugApp, rlBettHogApp, rlGoodPerfApp;
	DownloadableAsyncTask downloadableAsyncTask;
	TextView tvShowTime;
	ProgressDialog progressDialog;
	String mobileNo, latitude, longitude, RxLevel, deviceType, brand, problem,
			problemTime, status, comment, problemType, LocationName, MCC, MNC,
			LAC, CID, IMSI, IMEI, SIMID, category, subCategory;
	Dialog dialog;
	Calendar myCalender;
	public static String WhenDidItOccured="";
	public static String problemHeader = "";
	public static TextView tvchooseApplication;
	long problemDate;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.assistance_report_details_layout);
		Initalization();
		if (savedInstanceState != null
				&& (savedInstanceState.containsKey("CALL_KEY")
						|| savedInstanceState.containsKey("DATA_KEY")
						|| savedInstanceState.containsKey("TEXT_KEY")
						|| savedInstanceState.containsKey("DEVICE_KEY") || savedInstanceState
							.containsKey("APPS_KEY"))) {
			isCallFromCall = savedInstanceState.containsKey("CALL_KEY");
			isCallFromData = savedInstanceState.containsKey("DATA_KEY");
			isCallFromText = savedInstanceState.containsKey("TEXT_KEY");
			isCallFromDevice = savedInstanceState.containsKey("DEVICE_KEY");
			isCallFromApps = savedInstanceState.containsKey("APPS_KEY");
		}
		if (isCallFromCall) {
			CallReport();
		} else if (isCallFromData) {
			DataReport();
		} else if (isCallFromText) {
			TextReport();
		} else if (isCallFromDevice) {
			DeviceReport();
		} else if (isCallFromApps) {
			AppReport();
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
		TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == telMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		initializationInformation();
		// InitialArrangement();

	}

	private void initializationInformation() {
		mobileNo=latitude=longitude=RxLevel=deviceType=brand=problem=problemTime=status=comment=problemType="";
		LocationName=MCC=MNC=LAC=CID=IMSI=IMEI=SIMID = "";
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		/*if(tMgr.getLine1Number() != null){
			mobileNo = tMgr.getLine1Number().toString();
		}*/
		mobileNo = CommonValues.getInstance().LoginUser.Mobile;
		latitude = String.valueOf(MyNetService.currentLocation.getLatitude());
		longitude = String.valueOf(MyNetService.currentLocation.getLongitude());
		RxLevel = String.valueOf(MyNetService.currentSignalStrenght==99?0:MyNetService.currentSignalStrenght);
		deviceType = Build.MANUFACTURER;
		brand = Build.BOARD;
		status = "New";
		problemType = "TRUE";
		getLocatoin(Double.parseDouble(latitude), Double.parseDouble(longitude));
		getNetworkType(tMgr.getNetworkOperator());
		//String networktype = PhoneBasicInformation.getNetworkTypeString(tMgr.getNetworkType());
		GsmCellLocation location = (GsmCellLocation) tMgr.getCellLocation();
		LAC = String.valueOf(location.getLac() % 0xffff);
		CID = String.valueOf(location.getCid() % 0xffff);
		IMSI = tMgr.getSimSerialNumber();
		//IMSI = android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMSI);
		IMEI = tMgr.getDeviceId();
		SIMID = tMgr.getSimSerialNumber();
	}
	
	private void getLocatoin(double _latitude, double _longitude){
		String addressText = "";
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc != null) {
			LatLng Location = new LatLng(_latitude,
					_longitude);
			double latitude = Location.latitude, longitude = Location.longitude;
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			try{
				List<Address> addresses = geocoder.getFromLocation(
						latitude, longitude, 1);
				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					for (int lineIndex = 0; lineIndex < address
							.getMaxAddressLineIndex(); lineIndex++) {
						addressText = addressText
								+ address.getAddressLine(lineIndex)
								+ ", ";
					}
					addressText = addressText
							+ address.getLocality() + ", "
							+ address.getCountryName();
					LocationName = addressText;
					Location = new LatLng(latitude, longitude);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void getNetworkType(String type) {

		if (type != null) {
			MCC = String.valueOf(Integer.parseInt(type.substring(0, 3)));
			MNC = String.valueOf(Integer.parseInt(type.substring(3)));
		}
	}

	private String getPhoneTypeString(int type) {
		String typeString = "Unknown";

		switch (type) {
		case TelephonyManager.PHONE_TYPE_GSM:
			typeString = "GSM";
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			typeString = "UNKNOWN";
			break;
		case TelephonyManager.PHONE_TYPE_CDMA:
			typeString = "CDMA";
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			typeString = "SIP";
			break;
		default:
			typeString = "UNKNOWN";
			break;
		}

		return typeString;
	}

	private void Initalization() {
		myCalender = Calendar.getInstance();

		rlCallLayout = (RelativeLayout) findViewById(R.id.rlAssistanceReportCallLayout);
		rlDataLayout = (RelativeLayout) findViewById(R.id.rlAssistanceReportDataLayout);
		rlTextLayout = (RelativeLayout) findViewById(R.id.rlAssistanceReportTextLayout);
		rlDeviceLayout = (RelativeLayout) findViewById(R.id.rlAssistanceReportDeviceLayout);
		rlAppLayout = (RelativeLayout) findViewById(R.id.rlAssistanceReportAppsLayout);

		rlCallblocked = (RelativeLayout) findViewById(R.id.rlblockedCallButton);
		rlDropedCall = (RelativeLayout) findViewById(R.id.rlDropedCallButton);
		rlPoorVoice = (RelativeLayout) findViewById(R.id.rlPoorVoiceQuilityButton);
		rlMissedCall = (RelativeLayout) findViewById(R.id.rlMissedCallButton);
		rlGoodPerfCall = (RelativeLayout) findViewById(R.id.rlGoodPerformanceButton);

		rlSlowNetData = (RelativeLayout) findViewById(R.id.rlSlowNetworkButton);
		rlCannotConnData = (RelativeLayout) findViewById(R.id.rlCannotConnectionButton);
		rlDropedConnData = (RelativeLayout) findViewById(R.id.rlDropedConnectionButton);
		rlGoodPerfData = (RelativeLayout) findViewById(R.id.rlGoodPerformanceButtonData);

		rlFailedText = (RelativeLayout) findViewById(R.id.rlFailedToSendButton);
		rlSlowDelText = (RelativeLayout) findViewById(R.id.rlSlowDelevaryButton);
		rlMissedIncText = (RelativeLayout) findViewById(R.id.rlMissedIncomingTextButton);
		rlGoodPerfText = (RelativeLayout) findViewById(R.id.rlGoodPerformanceButtontext);

		rlDevCrashDevice = (RelativeLayout) findViewById(R.id.rlDeviceCrashButton);
		rlHangDevice = (RelativeLayout) findViewById(R.id.rlDevicehangButton);
		rlSlowDevice = (RelativeLayout) findViewById(R.id.rlDeviceslowButton);
		rlBatteryDevice = (RelativeLayout) findViewById(R.id.rlDevicePoorPerformaceButton);
		rlGoodPrefText = (RelativeLayout) findViewById(R.id.rlDeviceGoodPerformanceButton);

		rlCrashApp = (RelativeLayout) findViewById(R.id.rlAppCrashButton);
		rlHangApp = (RelativeLayout) findViewById(R.id.rlAppHangButton);
		rlSlowApp = (RelativeLayout) findViewById(R.id.rlAppSlowButton);
		rlBugApp = (RelativeLayout) findViewById(R.id.rlAppBugButton);
		rlBettHogApp = (RelativeLayout) findViewById(R.id.rlAppBatteryHogButton);
		rlGoodPerfApp = (RelativeLayout) findViewById(R.id.rlAppGoodPerformanceButton);

		rlCallblocked.setOnClickListener(this);
		rlDropedCall.setOnClickListener(this);
		rlPoorVoice.setOnClickListener(this);
		rlMissedCall.setOnClickListener(this);
		rlGoodPerfCall.setOnClickListener(this);

		rlSlowNetData.setOnClickListener(this);
		rlCannotConnData.setOnClickListener(this);
		rlDropedConnData.setOnClickListener(this);
		rlGoodPerfData.setOnClickListener(this);

		rlFailedText.setOnClickListener(this);
		rlSlowDelText.setOnClickListener(this);
		rlMissedIncText.setOnClickListener(this);
		rlGoodPerfText.setOnClickListener(this);

		rlDevCrashDevice.setOnClickListener(this);
		rlHangDevice.setOnClickListener(this);
		rlSlowDevice.setOnClickListener(this);
		rlBatteryDevice.setOnClickListener(this);
		rlGoodPrefText.setOnClickListener(this);

		rlCrashApp.setOnClickListener(this);
		rlHangApp.setOnClickListener(this);
		rlSlowApp.setOnClickListener(this);
		rlBugApp.setOnClickListener(this);
		rlBettHogApp.setOnClickListener(this);
		rlGoodPerfApp.setOnClickListener(this);
	}

	private void AppReport() {
		// rlMainLayout.setVisibility(RelativeLayout.GONE);
		rlCallLayout.setVisibility(RelativeLayout.GONE);
		rlDataLayout.setVisibility(RelativeLayout.GONE);
		rlTextLayout.setVisibility(RelativeLayout.GONE);
		rlDeviceLayout.setVisibility(RelativeLayout.GONE);
		rlAppLayout.setVisibility(RelativeLayout.VISIBLE);
	}

	private void DeviceReport() {
		// rlMainLayout.setVisibility(RelativeLayout.GONE);
		rlCallLayout.setVisibility(RelativeLayout.GONE);
		rlDataLayout.setVisibility(RelativeLayout.GONE);
		rlTextLayout.setVisibility(RelativeLayout.GONE);
		rlDeviceLayout.setVisibility(RelativeLayout.VISIBLE);
		rlAppLayout.setVisibility(RelativeLayout.GONE);
	}

	private void TextReport() {
		// rlMainLayout.setVisibility(RelativeLayout.GONE);
		rlCallLayout.setVisibility(RelativeLayout.GONE);
		rlDataLayout.setVisibility(RelativeLayout.GONE);
		rlTextLayout.setVisibility(RelativeLayout.VISIBLE);
		rlDeviceLayout.setVisibility(RelativeLayout.GONE);
		rlAppLayout.setVisibility(RelativeLayout.GONE);
	}

	private void DataReport() {
		// rlMainLayout.setVisibility(RelativeLayout.GONE);
		rlCallLayout.setVisibility(RelativeLayout.GONE);
		rlDataLayout.setVisibility(RelativeLayout.VISIBLE);
		rlTextLayout.setVisibility(RelativeLayout.GONE);
		rlDeviceLayout.setVisibility(RelativeLayout.GONE);
		rlAppLayout.setVisibility(RelativeLayout.GONE);
	}

	private void CallReport() {
		// rlMainLayout.setVisibility(RelativeLayout.GONE);
		rlCallLayout.setVisibility(RelativeLayout.VISIBLE);
		rlDataLayout.setVisibility(RelativeLayout.GONE);
		rlTextLayout.setVisibility(RelativeLayout.GONE);
		rlDeviceLayout.setVisibility(RelativeLayout.GONE);
		rlAppLayout.setVisibility(RelativeLayout.GONE);
	}

	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.rlblockedCallButton)
		{
			showDialog("Blocked Call");
		}
		
		else if (view.getId() == R.id.rlDropedCallButton)
		{
			showDialog("Dropped Call");
		}
		
		else if (view.getId() == R.id.rlPoorVoiceQuilityButton) {
			showDialog("Text");
		} else if (view.getId() == R.id.rlMissedCallButton) {
			showDialog("Missed Incoming Call");
		} else if (view.getId() == R.id.rlGoodPerformanceButton) {
			showDialog("Good Performance");
		} else if (view.getId() == R.id.rlSlowNetworkButton) {
			showDialog("Slow Performance");
		} else if (view.getId() == R.id.rlCannotConnectionButton) {
			showDialog("Can not Connect");
		} else if (view.getId() == R.id.rlDropedConnectionButton) {
			showDialog("Dropped Connection");
		} else if (view.getId() == R.id.rlGoodPerformanceButtonData) {
			showDialog("Good Performance");
		} else if (view.getId() == R.id.rlFailedToSendButton) {
			showDialog("Failed To Send");
		} else if (view.getId() == R.id.rlSlowDelevaryButton) {
			showDialog("Slow Delivery");
		} else if (view.getId() == R.id.rlMissedIncomingTextButton) {
			showDialog("Missed Incoming Call");
		} else if (view.getId() == R.id.rlGoodPerformanceButtontext) {
			showDialog("Good Performance");
		} else if (view.getId() == R.id.rlDeviceCrashButton) {
			showDialog("Device Crash");
		} else if (view.getId() == R.id.rlDevicehangButton) {
			showDialog("Device Hang");
		} else if (view.getId() == R.id.rlDeviceslowButton) {
			showDialog("Device Slow");
		} else if (view.getId() == R.id.rlDevicePoorPerformaceButton) {
			showDialog("Poor Battery");
		} else if (view.getId() == R.id.rlDeviceGoodPerformanceButton) {
			showDialog("Good Performance");
		} else if (view.getId() == R.id.rlAppCrashButton) {
			showDialog("App Crash");
		} else if (view.getId() == R.id.rlAppHangButton) {
			showDialog("App Hang");
		} else if (view.getId() == R.id.rlAppSlowButton) {
			showDialog("App Slow");
		} else if (view.getId() == R.id.rlAppBugButton) {
			showDialog("App Bug");
		} else if (view.getId() == R.id.rlAppBatteryHogButton) {
			showDialog("App Battery Hog");
		} else if (view.getId() == R.id.rlAppGoodPerformanceButton) {
			showDialog("Good Performance");
		}
	}

	private void showDialog(String title) {
		dialog = new Dialog(this);
		dialog.setTitle(title);
		dialog.setContentView(R.layout.assistance_send_report);
		problem = title;
		category = subCategory = problemTime = "";
		ImageView image = (ImageView) dialog
				.findViewById(R.id.ivAssistanceSendReportImage);
		TextView titleText = (TextView) dialog
				.findViewById(R.id.tvAssistanceSendReportHeaderText);
		final RelativeLayout rlStationary = (RelativeLayout) dialog
				.findViewById(R.id.rlStationary);
		final RelativeLayout rlMoveing = (RelativeLayout) dialog
				.findViewById(R.id.rlMoving);
		final RelativeLayout rlNow = (RelativeLayout) dialog
				.findViewById(R.id.rlNow);
		final RelativeLayout rlLastHour = (RelativeLayout) dialog
				.findViewById(R.id.rlLastHour);
		final RelativeLayout rlPick = (RelativeLayout) dialog
				.findViewById(R.id.rlPick);
		final EditText etNote = (EditText) dialog.findViewById(R.id.etNote);
		RelativeLayout rlChoolseApplication = (RelativeLayout) dialog
				.findViewById(R.id.rlWhichApplication);
		
		final RelativeLayout rlStationaryHome = (RelativeLayout) dialog.findViewById(R.id.rlStationaryHome);
		final RelativeLayout rlStationaryOutdoors  = (RelativeLayout) dialog.findViewById(R.id.rlStationaryOutdoors);
		final RelativeLayout rlStationaryBuilding = (RelativeLayout) dialog.findViewById(R.id.rlStationaryBuilding);
		final RelativeLayout rlStationaryOther = (RelativeLayout) dialog.findViewById(R.id.rlStationaryOther);
		
		final RelativeLayout rlMovingWalk  = (RelativeLayout) dialog.findViewById(R.id.rlMovingWalk);
		final RelativeLayout rlMovingAuto = (RelativeLayout) dialog.findViewById(R.id.rlMovingAuto);
		final RelativeLayout rlMovingOther = (RelativeLayout) dialog.findViewById(R.id.rlMovingOther);
		
		final TextView tvStationary = (TextView) dialog.findViewById(R.id.tvStationary);
		final TextView tvMoving = (TextView) dialog.findViewById(R.id.tvMoving);
		
		final TextView tvStationaryHome = (TextView) dialog.findViewById(R.id.tvStationaryHome);
		final TextView tvStationaryOutdoors = (TextView) dialog.findViewById(R.id.tvStationaryOutdoors);
		final TextView tvStationaryBuilding = (TextView) dialog.findViewById(R.id.tvStationaryBuilding);
		final TextView tvStationaryOther = (TextView) dialog.findViewById(R.id.tvStationaryOther);
		
		final TextView tvMovingWalk = (TextView) dialog.findViewById(R.id.tvMovingWalk);
		final TextView tvMovingAuto = (TextView) dialog.findViewById(R.id.tvMovingAuto);
		final TextView tvMovingOther = (TextView) dialog.findViewById(R.id.tvMovingOther);
		
		tvShowTime = (TextView)dialog.findViewById(R.id.tvShowTime);
 		
		tvchooseApplication = (TextView) dialog.findViewById(R.id.tvchooseApplication);
		
		
		Button bSendReport = (Button) dialog.findViewById(R.id.bSendReport);
		final LinearLayout stationaryMenu = (LinearLayout) dialog
				.findViewById(R.id.llAssistanceSendReportStationarySubButton);
		final LinearLayout movingMenu = (LinearLayout) dialog
				.findViewById(R.id.llAssistanceSendReportMovingSubButton);
		if (title.equals("Blocked Call")) {
			image.setBackgroundResource(R.drawable.blockcall);
			titleText.setText(title);
		} else if (title.equals("Dropped Call")) {
			image.setBackgroundResource(R.drawable.dropcall);
			titleText.setText(title);
		} else if (title.equals("Text")) {
			image.setBackgroundResource(R.drawable.poorvoice);
			titleText.setText(title);
		} else if (title.equals("Missed Incoming Call")) {
			image.setBackgroundResource(R.drawable.missedincomingcall);
			titleText.setText(title);
		} else if (title.equals("Good Performance")) {
			image.setBackgroundResource(R.drawable.goodperformance);
			titleText.setText(title);
		} else if (title.equals("Slow Performance")) {
			image.setBackgroundResource(R.drawable.slownetwork_data);
			titleText.setText(title);
		} else if (title.equals("Can not Connect")) {
			image.setBackgroundResource(R.drawable.cannotconnect);
			titleText.setText(title);
		} else if (title.equals("Dropped Connection")) {
			image.setBackgroundResource(R.drawable.dropconnection);
			titleText.setText(title);
		} else if (title.equals("Good Performance")) {
			image.setBackgroundResource(R.drawable.goodperformance_data);
			titleText.setText(title);
		} else if (title.equals("Failed To Send")) {
			image.setBackgroundResource(R.drawable.textfailed);
			titleText.setText(title);
		} else if (title.equals("Slow Delivery")) {
			image.setBackgroundResource(R.drawable.textslowdelivery);
			titleText.setText(title);
		} else if (title.equals("Missed Incoming Call")) {
			image.setBackgroundResource(R.drawable.textmissed);
			titleText.setText(title);
		} else if (title.equals("Good Performance")) {
			image.setBackgroundResource(R.drawable.goodperformance_text);
			titleText.setText(title);
		} else if (title.equals("Good Performance")) {
			image.setBackgroundResource(R.drawable.goodperformance_text);
			titleText.setText(title);
		} else if (title.equals("Device Crash")) {
			image.setBackgroundResource(R.drawable.appcrash_device);
			titleText.setText(title);
		} else if (title.equals("Device Hang")) {
			image.setBackgroundResource(R.drawable.devicehang);
			titleText.setText(title);
		} else if (title.equals("Device Slow")) {
			image.setBackgroundResource(R.drawable.deviceslow);
			titleText.setText(title);
		} else if (title.equals("Poor Battery")) {
			image.setBackgroundResource(R.drawable.poorquality);
			titleText.setText(title);
		} else if (title.equals("Good Performance")) {
			image.setBackgroundResource(R.drawable.goodperformance_device);
			titleText.setText(title);
		} else if (title.equals("App Crash")) {
			rlChoolseApplication.setVisibility(RelativeLayout.VISIBLE);
			image.setBackgroundResource(R.drawable.appcrash);
			titleText.setText(title);
		} else if (title.equals("App Hang")) {
			rlChoolseApplication.setVisibility(RelativeLayout.VISIBLE);
			image.setBackgroundResource(R.drawable.apphung);
			titleText.setText(title);
		} else if (title.equals("App Slow")) {
			rlChoolseApplication.setVisibility(RelativeLayout.VISIBLE);
			image.setBackgroundResource(R.drawable.appslow);
			titleText.setText(title);
		} else if (title.equals("App Bug")) {
			rlChoolseApplication.setVisibility(RelativeLayout.VISIBLE);
			image.setBackgroundResource(R.drawable.appbug);
			titleText.setText(title);
		} else if (title.equals("App Battery Hog")) {
			rlChoolseApplication.setVisibility(RelativeLayout.VISIBLE);
			image.setBackgroundResource(R.drawable.appbuttary);
			titleText.setText(title);
		} else if (title.equals("Good Performance")) {
			rlChoolseApplication.setVisibility(RelativeLayout.VISIBLE);
			image.setBackgroundResource(R.drawable.goodperformance_app);
			titleText.setText(title);
		}
		
		//catagory
		rlStationary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rlStationary.setBackgroundColor(Color.rgb(200, 200, 200));
				rlMoveing.setBackgroundColor(Color.rgb(255, 255, 255));
				stationaryMenu.setVisibility(LinearLayout.VISIBLE);
				movingMenu.setVisibility(LinearLayout.GONE);
				category = tvStationary.getText().toString();
			}
		});
		rlMoveing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rlMoveing.setBackgroundColor(Color.rgb(200, 200, 200));
				rlStationary.setBackgroundColor(Color.rgb(255, 255, 255));
				stationaryMenu.setVisibility(LinearLayout.GONE);
				movingMenu.setVisibility(LinearLayout.VISIBLE);
				category = tvMoving.getText().toString();
			}
		});
		
		//choose application
		rlChoolseApplication.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AppList appList = new AppList(
						AssistanceReportDetalisActivity.this);

				appList.showAppList();
			}
		});
		
		// sub catagory-stationary
		rlStationaryHome.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				rlStationaryHome.setBackgroundColor(Color.rgb(200, 200, 200));
				rlStationaryOutdoors.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryBuilding.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryOther.setBackgroundColor(Color.rgb(255, 255, 255));
				subCategory = tvStationaryHome.getText().toString();
			}
		});
		rlStationaryOutdoors.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				rlStationaryOutdoors.setBackgroundColor(Color.rgb(200, 200, 200));
				rlStationaryHome.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryBuilding.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryOther.setBackgroundColor(Color.rgb(255, 255, 255));
				subCategory = tvStationaryOutdoors.getText().toString();
			}
		});
		rlStationaryBuilding.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				rlStationaryBuilding.setBackgroundColor(Color.rgb(200, 200, 200));
				rlStationaryHome.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryOutdoors.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryOther.setBackgroundColor(Color.rgb(255, 255, 255));
				subCategory = tvStationaryBuilding.getText().toString();
			}
		});
		
		rlStationaryOther.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				rlStationaryOther.setBackgroundColor(Color.rgb(200, 200, 200));
				rlStationaryOutdoors.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryHome.setBackgroundColor(Color.rgb(255, 255, 255));
				rlStationaryBuilding.setBackgroundColor(Color.rgb(255, 255, 255));
				subCategory = tvStationaryOther.getText().toString();
			}
		});
		
		//sub-catagory-moving
		
		rlMovingWalk.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				rlMovingWalk.setBackgroundColor(Color.rgb(200, 200, 200));
				rlMovingAuto.setBackgroundColor(Color.rgb(255, 255, 255));
				rlMovingOther.setBackgroundColor(Color.rgb(255, 255, 255));
				
				subCategory = tvMovingWalk.getText().toString();
			}
		});
		
		rlMovingAuto.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				rlMovingAuto.setBackgroundColor(Color.rgb(200, 200, 200));
				rlMovingWalk.setBackgroundColor(Color.rgb(255, 255, 255));
				rlMovingOther.setBackgroundColor(Color.rgb(255, 255, 255));
				subCategory = tvMovingAuto.getText().toString();
			}
		});
		
		rlMovingOther.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				rlMovingOther.setBackgroundColor(Color.rgb(200, 200, 200));
				rlMovingAuto.setBackgroundColor(Color.rgb(255, 255, 255));
				rlMovingWalk.setBackgroundColor(Color.rgb(255, 255, 255));
				subCategory = tvMovingOther.getText().toString();
			}
		});
		

		rlNow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WhenDidItOccured="Now";
				rlNow.setBackgroundColor(Color.rgb(200, 200, 200));
				rlLastHour.setBackgroundColor(Color.rgb(255, 255, 255));
				rlPick.setBackgroundColor(Color.rgb(255, 255, 255));
				//problemTime = String.valueOf(System.currentTimeMillis());
				 Calendar lCDateTime = Calendar.getInstance();
			      problemTime = String.valueOf(lCDateTime.getTimeInMillis());
			      problemDate=lCDateTime.getTimeInMillis();
			      String dateTime = (String) DateUtils.getRelativeTimeSpanString(Long.parseLong(problemTime), new Date().getTime(), 0);
			      tvShowTime.setText(dateTime);
				
			}
		});

		rlLastHour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WhenDidItOccured="LastHour";
				rlLastHour.setBackgroundColor(Color.rgb(200, 200, 200));
				rlNow.setBackgroundColor(Color.rgb(255, 255, 255));
				rlPick.setBackgroundColor(Color.rgb(255, 255, 255));
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, -1);
				problemTime = String.valueOf(cal.getTimeInMillis());
				 problemDate=cal.getTimeInMillis();
				String dateTime = (String) DateUtils.getRelativeTimeSpanString(
						Long.parseLong(problemTime), new Date().getTime(), 0);
		      tvShowTime.setText(dateTime);
			}
		});

		rlPick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WhenDidItOccured="Pick";
				rlPick.setBackgroundColor(Color.rgb(200, 200, 200));
				rlLastHour.setBackgroundColor(Color.rgb(255, 255, 255));
				rlNow.setBackgroundColor(Color.rgb(255, 255, 255));
				pickTime();
			}
		});

		bSendReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!ValidationInput())
					return;
				if (etNote.getText().equals(""))
					comment = "";
				else
					comment = etNote.getText().toString();
				
				ReportProblemAndBadExperience rpbe = new ReportProblemAndBadExperience();
				rpbe.Latitude=MyNetService.currentLocation.getLatitude();
				rpbe.Longitude=MyNetService.currentLocation.getLongitude();
				rpbe.LocationName=LocationName;
				rpbe.RxLevel=RxLevel;
				rpbe.Problem=problem;
				rpbe.ProblemTime=problemTime;
				rpbe.ReportTime=String.valueOf(System.currentTimeMillis());
				rpbe.Status=status;
				rpbe.Comment=comment;
				rpbe.ProblemType=problemType.equals("TRUE")?true:false;
				rpbe.Failed=0;
				rpbe.ProblemDetailCategory=category;
				rpbe.ProblemDetailSubCategory=subCategory;
				rpbe.Remarks=WhenDidItOccured;
				rpbe.Extra1="";
				rpbe.Extra2="";
				rpbe.problemHeader=problemHeader;	
				
				MyNetDatabase db=new MyNetDatabase(AssistanceReportDetalisActivity.this);
				db.open();
				db.CreateReportProblemAndBadExperience(rpbe);
				db.close();					
				
				LoadInformation();

				Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
		});
		dialog.show();
	}

	private void pickTime() {
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
				dateofbirth, myCalender.get(Calendar.YEAR),
				myCalender.get(Calendar.MONTH),
				myCalender.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		datePickerDialog.setTitle("Date");
		datePickerDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.cancel();
			}
		});
		datePickerDialog.show();
	}

	public DatePickerDialog.OnDateSetListener dateofbirth = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			Calendar cal = new GregorianCalendar(years, monthOfYear, dayOfMonth);
			problemTime = String.valueOf(cal.getTimeInMillis());
			problemDate=cal.getTimeInMillis();
			String dateTime = (String) DateUtils.getRelativeTimeSpanString(
					Long.parseLong(problemTime), new Date().getTime(), 0);
			tvShowTime.setText(dateTime);
		}
	};
	
	private boolean ValidationInput()
	{
		boolean isValid=true;
		

		if(category.isEmpty())
		{
			Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
		
		else if(subCategory.isEmpty())
		{
			Toast.makeText(this, "Please select sub-category", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}	
	
		else if(problemDate <= 0)
		{
			Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
		
		if(tvchooseApplication.getVisibility()==View.VISIBLE)
		{	
			if(tvchooseApplication.getText().equals("Choose an Application") )
			{
				if(problem =="App Crash"||problem =="App Hang"||problem =="App Slow"||problem =="App Bug"||problem =="App Battery Hog" )
				{	
				Toast.makeText(this, "Please choose an application", Toast.LENGTH_SHORT).show();	
				isValid=false;
				return isValid;	
				}
			}

		}
		
		return isValid;
	}


	private void LoadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		/*progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Submitting to operator...");
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		//progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		
		IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
		return statisticsReportManager.SetReportProblemAndBadExperience(mobileNo, latitude, longitude, RxLevel, deviceType, brand, problem, problemTime, status, comment, 
				problemType, "", "", LocationName, MCC, MNC, LAC, CID, IMSI, "0", IMEI, SIMID, category, subCategory,WhenDidItOccured,problemHeader);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		/*if (data != null) {
			AlertDialog.Builder builder = new Builder(
					AssistanceReportDetalisActivity.this);
			builder.setTitle("Report Sent");
			builder.setCancelable(false);
			builder.setMessage("Thank you!\nYou have submitted your problem to your operator. You may check the problem update in Dashboard > Problem Tracking any time!");
			builder.setPositiveButton("Dismiss",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface builder, int which) {
							builder.dismiss();
							dialog.dismiss();
							
						}
					});
			builder.show();
			
			Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		else{
			Toast.makeText(getApplicationContext(), "Please Select options...", Toast.LENGTH_SHORT).show();
			}*/
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
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
