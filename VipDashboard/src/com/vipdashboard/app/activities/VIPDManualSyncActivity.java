package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadPhoneinfoAsyncTask;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.asynchronoustask.ProblemTrackingIntegrationAsyncTask;
import com.vipdashboard.app.base.CallInformationReceiver;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.customcontrols.ProgressDialogView;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IDownloadPhoneinfoAsyncTask;
import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.PhoneInformationManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.TrafficSnapshot;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VIPDManualSyncActivity extends MainActionbarBase implements
		IAsynchronousTask, LocationListener, IDownloadPhoneinfoAsyncTask,
		OnClickListener {
	TextView tvCompanyName, tvCompanyCountry;

	DownloadableAsyncTask downloadableTask;
	DownloadPhoneinfoAsyncTask downloadPhoneinfoAsyncTask;

	Intent intent;
	PendingIntent pendingIntent;
	int notificationCount = 0, alarmCount = 0, collaborationCount = 0,
			intServiceState = 0;
	public static int phoneState = 0, phoneCallId = 0, lastSmsId = 0;

	long previousTime = 0, priviousDownloadData = 0, currentDownloadData = 0,
			priviousUploadData = 0, currentUploadData = 0,
			lastPhoneInfoSyncTime = 0;

	public static PhoneBasicInformation phoneBasicInformation;

	public static android.location.Location currentLocation;
	public static int currentSignalStrenght = 31, previousSignal = 0;
	public static int currentDownloadSpeedInKbPS = 0,
			currentUploadSpeedInKbPS = 0;

	CallInformationReceiver receiver = null;

	private static int phoneId = 0;
	
	ArrayList<PhoneCallInformation> callList = new ArrayList<PhoneCallInformation>();
	ArrayList<PhoneSMSInformation> smsList = new ArrayList<PhoneSMSInformation>();
	ArrayList<PhoneSignalStrenght> signalStrengthList = new ArrayList<PhoneSignalStrenght>();
	PhoneDataInformation dataInfo = null;
	PhoneSignalStrenght signalInfo = null;

	TextView tvOk, tvCancel;
	
	DownloadableAsyncTask downloadAsync;
	ProgressDialog progress;
	ProgressDialogView progressDialogView;
	int signal;
	
	ImageView ivShareLocationOn,
	ivShareLocationOff,
	ivSeeCallHistryOn,
	ivSeeCallHistryOff,
	ivMyDeviceOn,
	ivMyDeviceOff,
	ivAnyPromotionOn,
	ivAnyPromotionOff,
	ivNotifyIncidentsOn,
	ivNotifyIncidentsOff,
	ivCallAndSMSSyncOn,
	ivCallAndSMSSyncOff,
	ivAppAndDataOn,
	ivAppAndDataOff;

	private static final int INFO_SIGNAL_LEVEL_INFO_INDEX = 0;

	private static final int[] info_ids = { R.id.tvNetworkCIDRxType

	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedInstanceState = getIntent().getExtras();
		setContentView(R.layout.vipd_manual_sync_popup);
		
		
		
		ivShareLocationOn= (ImageView) findViewById(R.id.ivShareLocationOn);
		ivShareLocationOff= (ImageView) findViewById(R.id.ivShareLocationOff);
		ivSeeCallHistryOn= (ImageView) findViewById(R.id.ivSeeCallHistryOn);
		ivSeeCallHistryOff= (ImageView) findViewById(R.id.ivSeeCallHistryOff);
		ivMyDeviceOn= (ImageView) findViewById(R.id.ivMyDeviceOn);
		ivMyDeviceOff= (ImageView) findViewById(R.id.ivMyDeviceOff);
		ivAnyPromotionOn= (ImageView) findViewById(R.id.ivAnyPromotionOn);
		ivAnyPromotionOff= (ImageView) findViewById(R.id.ivAnyPromotionOff);
		ivNotifyIncidentsOn= (ImageView) findViewById(R.id.ivNotifyIncidentsOn);
		ivNotifyIncidentsOff= (ImageView) findViewById(R.id.ivNotifyIncidentsOff);
		ivCallAndSMSSyncOn= (ImageView) findViewById(R.id.ivCallAndSMSSyncOn);
		ivCallAndSMSSyncOff= (ImageView) findViewById(R.id.ivCallAndSMSSyncOff);
		ivAppAndDataOn= (ImageView) findViewById(R.id.ivAppAndDataOn);
		ivAppAndDataOff= (ImageView) findViewById(R.id.ivAppAndDataOff);
		
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
	
		tvOk = (TextView) findViewById(R.id.tvOk);
		tvOk.setOnClickListener(this);
		ivShareLocationOn.setOnClickListener(this);
		ivShareLocationOff.setOnClickListener(this);
		ivSeeCallHistryOn.setOnClickListener(this);
		ivSeeCallHistryOff.setOnClickListener(this);
		ivMyDeviceOn.setOnClickListener(this);
		ivMyDeviceOff.setOnClickListener(this);
		ivAnyPromotionOn.setOnClickListener(this);
		ivAnyPromotionOff.setOnClickListener(this);
		ivNotifyIncidentsOn.setOnClickListener(this);
		ivNotifyIncidentsOff.setOnClickListener(this);
		ivCallAndSMSSyncOn.setOnClickListener(this);
		ivCallAndSMSSyncOff.setOnClickListener(this);
		ivAppAndDataOn.setOnClickListener(this);
		ivAppAndDataOff.setOnClickListener(this);
		
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
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
		callList = new ArrayList<PhoneCallInformation>();
		smsList = new ArrayList<PhoneSMSInformation>();
		signalStrengthList = new ArrayList<PhoneSignalStrenght>();
		
		
		if(!CommonTask.isMyServiceRunning(this))
			startService(new Intent(this, MyNetService.class));
		tvCompanyName.setText(tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		startSignalLevelListener();
		
		/*signalStrengthList=new 	ArrayList<PhoneSignalStrenght>();
		callList = new ArrayList<PhoneCallInformation>();
		smsList = new ArrayList<PhoneSMSInformation>();*/
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyNetApplication.activityPaused();
	}

	private void startSignalLevelListener() {

		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTH
				| PhoneStateListener.LISTEN_DATA_ACTIVITY
				| PhoneStateListener.LISTEN_CELL_LOCATION
				| PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
				| PhoneStateListener.LISTEN_SERVICE_STATE;

		tm.listen(phoneListener, events);

	}

	private final PhoneStateListener phoneListener = new PhoneStateListener() {
		/*
		 * call fwding
		 */
		public void onCallForwardingIndicatorChanged(boolean cfi) {

			super.onCallForwardingIndicatorChanged(cfi);
		}

		/*
		 * Call State Changed
		 */
		public void onCallStateChanged(int state, String incomingNumber) {

		}

		/*
		 * Cell location changed event handler
		 */
		public void onCellLocationChanged(CellLocation location) {

		}

		/*
		 * Cellphone data connection status
		 */
		public void onDataConnectionStateChanged(int state) {

		}

		/*
		 * Data activity handler
		 */
		public void onDataActivity(int direction) {

		}

		/*
		 * Cellphone Service status
		 */
		public void onServiceStateChanged(ServiceState serviceState) {

		}

		/*
	 * 
	 * */
		public void onSignalStrengthChanged(int asu) {

			setSignalLevel(info_ids[INFO_SIGNAL_LEVEL_INFO_INDEX], asu);

			super.onSignalStrengthChanged(asu);
		}

	};

	private void setSignalLevel(int infoid, int level) {

		//signal = -113 + (level * 2);
		signal = level;
	}

	@Override
	public void onClick(View view) {
		
		switch(view.getId())
		{
			case R.id.tvOk:
				
				if(ivShareLocationOn.getVisibility()==View.GONE&&
						ivSeeCallHistryOn.getVisibility()==View.GONE&&
						ivMyDeviceOn.getVisibility()==View.GONE&&
						ivAnyPromotionOn.getVisibility()==View.GONE&&
						ivNotifyIncidentsOn.getVisibility()==View.GONE&&
						ivCallAndSMSSyncOn.getVisibility()==View.GONE&&
						ivAppAndDataOn.getVisibility()==View.GONE){
					Toast.makeText(this, "Please select at least one item to sync.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					LoadInformation();
				}
			break;
			
			case R.id.ivShareLocationOn:
				ivShareLocationOff.setVisibility(View.VISIBLE);
				ivShareLocationOn.setVisibility(View.GONE);
			break;
			case R.id.ivShareLocationOff:
				ivShareLocationOff.setVisibility(View.GONE);
				ivShareLocationOn.setVisibility(View.VISIBLE);
				break;
			case R.id.ivSeeCallHistryOn:
				ivSeeCallHistryOff.setVisibility(View.VISIBLE);
				ivSeeCallHistryOn.setVisibility(View.GONE);
				break;
			case R.id.ivSeeCallHistryOff:
				ivSeeCallHistryOff.setVisibility(View.GONE);
				ivSeeCallHistryOn.setVisibility(View.VISIBLE);
				break;
			case R.id.ivMyDeviceOn:
				ivMyDeviceOff.setVisibility(View.VISIBLE);
				ivMyDeviceOn.setVisibility(View.GONE);
				break;
			case R.id.ivMyDeviceOff:
				ivMyDeviceOff.setVisibility(View.GONE);
				ivMyDeviceOn.setVisibility(View.VISIBLE);
				break;
			case R.id.ivAnyPromotionOn:
				ivAnyPromotionOff.setVisibility(View.VISIBLE);
				ivAnyPromotionOn.setVisibility(View.GONE);
				break;
			case R.id.ivAnyPromotionOff:
				ivAnyPromotionOff.setVisibility(View.GONE);
				ivAnyPromotionOn.setVisibility(View.VISIBLE);
				break;
			case R.id.ivNotifyIncidentsOn:
				ivNotifyIncidentsOff.setVisibility(View.VISIBLE);
				ivNotifyIncidentsOn.setVisibility(View.GONE);
				break;
			case R.id.ivNotifyIncidentsOff:
				ivNotifyIncidentsOff.setVisibility(View.GONE);
				ivNotifyIncidentsOn.setVisibility(View.VISIBLE);
				break;
			case R.id.ivCallAndSMSSyncOn:
				ivCallAndSMSSyncOff.setVisibility(View.VISIBLE);
				ivCallAndSMSSyncOn.setVisibility(View.GONE);
				break;
			case R.id.ivCallAndSMSSyncOff:
				ivCallAndSMSSyncOff.setVisibility(View.GONE);
				ivCallAndSMSSyncOn.setVisibility(View.VISIBLE);
				break;
			case R.id.ivAppAndDataOn:
				ivAppAndDataOff.setVisibility(View.VISIBLE);
				ivAppAndDataOn.setVisibility(View.GONE);
				break;
			case R.id.ivAppAndDataOff:
				ivAppAndDataOff.setVisibility(View.GONE);
				ivAppAndDataOn.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void LoadInformation() {
		if(MyNetService.phoneId>0){
			if (downloadAsync != null) {
				downloadAsync.cancel(true);
			}
			downloadAsync = new DownloadableAsyncTask(this);
			downloadAsync.execute();
		}
		else
		{
			Toast.makeText(this, "Manual Sync is not ready yet, Please wait a while",Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public Object doBackgroundTask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processPostDataDownload(Object data) {
		// TODO Auto-generated method stub

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

	@Override
	public void showProgressLoader() {
		/*progressDialogView = new ProgressDialogView(this, R.drawable.progress_loader);
		progressDialogView.show();*/
		progress = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(false);
		//progress.setIndeterminate(true);
		//progress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progrress_bar));
		progress.setMessage("Synchronizing...");
		progress.show();
	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();
		//progressDialogView.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IPhoneInformationService oPhoneInformationService = new PhoneInformationManager();		
		PhoneSignalStrenght phoneSignalStrenght = new PhoneSignalStrenght();
		phoneSignalStrenght.PhoneId = MyNetService.phoneId;
		phoneSignalStrenght.SignalLevel = signal;
		phoneSignalStrenght.Longitude = MyNetService.currentLocation.getLongitude();
		phoneSignalStrenght.Latitude = MyNetService.currentLocation.getLatitude();
		phoneSignalStrenght.LocationName=MyNetService.currentLocationName; 
		phoneSignalStrenght.Time = new Date(System.currentTimeMillis());
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
		phoneSignalStrenght.CellID = String.valueOf(location.getCid() % 0xffff);
		phoneSignalStrenght.LAC = String.valueOf(location.getLac() % 0xffff);
		
		signalStrengthList=new 	ArrayList<PhoneSignalStrenght>();
		
		signalStrengthList.add(phoneSignalStrenght);
		
		MyNetDatabase mynetDatabase = new MyNetDatabase(this);
		mynetDatabase.open();

		if (ivCallAndSMSSyncOn.getVisibility()==View.VISIBLE) {
		
			smsList = mynetDatabase.getSMSInfoListForSync();
			callList = mynetDatabase.getCallInfoListForSync();
		}
		
		mynetDatabase.close();
		
		if(ivAppAndDataOn.getVisibility()==View.VISIBLE)
		{
			//Data Sync
			oPhoneInformationService.SetDataSpeedInfo(VIPDManualSyncActivity.this,false);
			
			//Application Sync
			oPhoneInformationService.processPhoneAppsData(this);
			
			//IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
			//statisticsReportManager.GetAllReportProblemAndBadExperience(CommonValues.getInstance().LoginUser.Mobile);
		}
		
		return oPhoneInformationService.SetPhoneBasicInfo(this,
				MyNetService.phoneId, callList, smsList, null,
				signalStrengthList);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		// TODO Auto-generated method stub
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		 else	showSuccesfullDialog();

		
		new ProblemTrackingIntegrationAsyncTask(this).execute(); 

	}

	private void showSuccesfullDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		MyNetDatabase mynetDatabase = new MyNetDatabase(this);
		mynetDatabase.open();
		mynetDatabase.updateDataSyncInfo(callList, smsList, signalStrengthList);
		mynetDatabase.close();
	    dialog.setTitle("Synchronized!!!");
	    dialog.setMessage("Thank you!\n Data Synchronized Successfully.");
	    dialog.setCancelable(false);
	    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int buttonId) {
	        	dialog.dismiss();
	        }
	    });   
	    dialog.show();
		
	}

}
