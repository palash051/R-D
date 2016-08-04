package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadPhoneinfoAsyncTask;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.CallInformationReceiver;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VIPDEngineeringModeActivity extends MainActionbarBase implements
		OnClickListener {
	TextView tvCompanyName, tvCompanyCountry;
	RelativeLayout rlServiceTest, rlSpeedTest;
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

	TextView tvOk, tvCancel,tvCoverageValue,
	tvServicetestValue;
	
	DownloadableAsyncTask downloadAsync;
	ProgressDialog progress;
	int signal;
	
	ImageView ivEngineeringModeOn,
	ivEngineeringModeOff,
	ivDeviceTestOn,
	ivDeviceTestOff,
	ivUrlTestOn,
	ivUrlTestOff,
	ivStreamingTestOn,
	ivStreamingTestOff,
	ivAccuracyOn,
	ivAccuracyOff,
	tvNFCOn,
	tvNFCOff;
	
	int numberPickerValue=0;
	
	String OverviewnumberPicker="";

	private static final int INFO_SIGNAL_LEVEL_INFO_INDEX = 0;

	private static final int[] info_ids = { R.id.tvNetworkCIDRxType

	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedInstanceState = getIntent().getExtras();
		setContentView(R.layout.vipdengineeringmode);
		
		
		
		ivEngineeringModeOn= (ImageView) findViewById(R.id.ivEngineeringModeOn);
		ivEngineeringModeOff= (ImageView) findViewById(R.id.ivEngineeringModeOff);
		ivDeviceTestOn= (ImageView) findViewById(R.id.ivDeviceTestOn);
		ivDeviceTestOff= (ImageView) findViewById(R.id.ivDeviceTestOff);
		ivUrlTestOn= (ImageView) findViewById(R.id.ivUrlTestOn);
		ivUrlTestOff= (ImageView) findViewById(R.id.ivUrlTestOff);
		ivStreamingTestOn= (ImageView) findViewById(R.id.ivStreamingTestOn);
		ivStreamingTestOff= (ImageView) findViewById(R.id.ivStreamingTestOff);
		ivAccuracyOn= (ImageView) findViewById(R.id.ivAccuracyOn);
		ivAccuracyOff= (ImageView) findViewById(R.id.ivAccuracyOff);
		tvNFCOn= (ImageView) findViewById(R.id.tvNFCOn);
		tvNFCOff= (ImageView) findViewById(R.id.tvNFCOff);
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		tvOk = (TextView) findViewById(R.id.tvOk);
		tvCoverageValue= (TextView) findViewById(R.id.tvCoverageValue);
		tvServicetestValue= (TextView) findViewById(R.id.tvServicetestValue);
		
		 rlServiceTest = (RelativeLayout) findViewById(R.id. rlServiceTest); 
		 rlSpeedTest = (RelativeLayout) findViewById(R.id. rlSpeedTest); 
		
		
		tvOk.setOnClickListener(this);
		ivEngineeringModeOn.setOnClickListener(this);
		ivEngineeringModeOff.setOnClickListener(this);
		ivDeviceTestOn.setOnClickListener(this);
		ivDeviceTestOff.setOnClickListener(this);
		ivUrlTestOn.setOnClickListener(this);
		ivUrlTestOff.setOnClickListener(this);
		ivStreamingTestOn.setOnClickListener(this);
		ivStreamingTestOff.setOnClickListener(this);
		ivAccuracyOn.setOnClickListener(this);
		ivAccuracyOff.setOnClickListener(this);
		tvNFCOn.setOnClickListener(this);
		tvNFCOff.setOnClickListener(this);
		tvCoverageValue.setOnClickListener(this);
		tvServicetestValue.setOnClickListener(this);
		rlServiceTest.setOnClickListener(this);
		rlSpeedTest.setOnClickListener(this);
		
		loadSavedPreferences();
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
			{	if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
		tvCompanyName.setText(tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyNetApplication.activityPaused();
	}

	@Override
	public void onClick(View view) {
		
		switch(view.getId())
		{
			case R.id.tvOk:
					boolean engineeringMode=true,isDataTest=false,isURLTest=false, isStreamingTest=false, isGPS=false,isNFC=false; 
					String coverageSeconds,serviceSeconds;
					
					if(ivEngineeringModeOff.getVisibility()==View.VISIBLE)
					{
						engineeringMode=false;
					}
					
					if(ivDeviceTestOn.getVisibility()==View.VISIBLE)
					{
						isDataTest=true;
					}
					if(ivUrlTestOn.getVisibility()==View.VISIBLE)
					{
						isURLTest=true;
					}
					if(ivStreamingTestOn.getVisibility()==View.VISIBLE)
					{
						isStreamingTest=true;
					}
					if(ivAccuracyOn.getVisibility()==View.VISIBLE)
					{
						isGPS=true;
					}
					if(tvNFCOn.getVisibility()==View.VISIBLE)
					{
						isNFC=true;
					}
					
					savePreferences(CommonValues.ENGINEERING_MODE, engineeringMode);
					savePreferences(CommonValues.CONVERAGE_SECONDS, tvCoverageValue.getText().toString());
					savePreferences(CommonValues.SERVICE_TEST_SECONDS, tvServicetestValue.getText().toString());
					savePreferences(CommonValues.IS_DATA_TEST, isDataTest);
					savePreferences(CommonValues.IS_URL_TEST, isURLTest);
					savePreferences(CommonValues.IS_STREAMING_TEST, isStreamingTest);
					savePreferences(CommonValues.IS_GPS, isGPS);
					savePreferences(CommonValues.IS_NFC, isNFC);
					
	//				MyNetService objMyNet=new MyNetService();
//					objMyNet.updateSharedPreference();
					Toast.makeText(this, "Engineering Settings Saved Successfully.", Toast.LENGTH_SHORT).show();
					
					onBackPressed();
					/*AlertDialog dialog = new AlertDialog.Builder(this).create();
				    dialog.setTitle("Engineering Mode");
				    dialog.setMessage("Information Saved Successfully.");
				    dialog.setCancelable(false);
				    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int buttonId) {
				        	dialog.dismiss();
				        }
				    });   
				    dialog.show();*/
					
					
			break;
			
			case R.id.tvCoverageValue:
				OverviewnumberPicker="CoverageValue";
				numberPickerValue=Integer.parseInt(String.valueOf(tvCoverageValue.getText()));
				showNumberPickerDialog();
				break;
			case R.id.tvServicetestValue:
				OverviewnumberPicker="ServicetestValue";
				numberPickerValue=Integer.parseInt(String.valueOf(tvServicetestValue.getText()));
				showNumberPickerDialog();
				break;
			case R.id.ivEngineeringModeOn:
				ivEngineeringModeOn.setVisibility(View.GONE);
				ivEngineeringModeOff.setVisibility(View.VISIBLE);
				break;
			case R.id.ivEngineeringModeOff:
				ivEngineeringModeOn.setVisibility(View.VISIBLE);
				ivEngineeringModeOff.setVisibility(View.GONE);
				break;
			case R.id.ivDeviceTestOn:
				ivDeviceTestOn.setVisibility(View.GONE);
				ivDeviceTestOff.setVisibility(View.VISIBLE);
				break;
			case R.id.ivDeviceTestOff:
				ivDeviceTestOn.setVisibility(View.VISIBLE);
				ivDeviceTestOff.setVisibility(View.GONE);
				break;
			case R.id.ivUrlTestOn:
				ivUrlTestOn.setVisibility(View.GONE);
				ivUrlTestOff.setVisibility(View.VISIBLE);
				break;
			case R.id.ivUrlTestOff:
				ivUrlTestOn.setVisibility(View.VISIBLE);
				ivUrlTestOff.setVisibility(View.GONE);
				
				break;
			case R.id.ivStreamingTestOn:
				ivStreamingTestOn.setVisibility(View.GONE);
				ivStreamingTestOff.setVisibility(View.VISIBLE);
				break;
			case R.id.ivStreamingTestOff:
				ivStreamingTestOn.setVisibility(View.VISIBLE);
				ivStreamingTestOff.setVisibility(View.GONE);
				break;
			case R.id.ivAccuracyOn:
				ivAccuracyOn.setVisibility(View.GONE);
				ivAccuracyOff.setVisibility(View.VISIBLE);
				break;
			case R.id.ivAccuracyOff:
				ivAccuracyOn.setVisibility(View.VISIBLE);
				ivAccuracyOff.setVisibility(View.GONE);
				break;
			case R.id.tvNFCOn:
				tvNFCOn.setVisibility(View.GONE);
				tvNFCOff.setVisibility(View.VISIBLE);
				break;
			case R.id.tvNFCOff:
				tvNFCOn.setVisibility(View.VISIBLE);
				tvNFCOff.setVisibility(View.GONE);
				break;
			case R.id.rlServiceTest:
				Intent intent = new Intent(this, VIPD_ServiceTestActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.rlSpeedTest:
				Intent _intent = new Intent(this, VIPD_SpeedTestActivity.class);
				_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(_intent);
				break;
		}
	}
	
	  public void showNumberPickerDialog()
	    {

	         final Dialog d = new Dialog(VIPDEngineeringModeActivity.this);
	         //d.requestWindowFeature(Window.FEATURE_NO_TITLE);
	         d.setTitle("NumberPicker");
	         d.setContentView(R.layout.numberpickerpopup);
	         Button b1 = (Button) d.findViewById(R.id.button1);
	         Button b2 = (Button) d.findViewById(R.id.button2);
	         final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
	         
	         int minvalue=0,maxvalue=0;
	         
	         if(OverviewnumberPicker== "CoverageValue")
	       	  {
	        	 	d.setTitle("Coverage Value");
		        	 minvalue=30;
		        	 maxvalue=60;
	       	  }
	       	  else if(OverviewnumberPicker== "ServicetestValue")
	       	  {
	       		d.setTitle("Service test Value");
	       		 minvalue=300;
	        	 maxvalue=600;
	       	  }
	         np.setMaxValue(maxvalue);
	         np.setMinValue(minvalue);
	         np.setValue(numberPickerValue);
	         np.setWrapSelectorWheel(false);
	        // np.setOnValueChangedListener(this);
	         b1.setOnClickListener(new OnClickListener()
	         {
	          @Override
	          public void onClick(View v) {
	        	  
	        	  if(OverviewnumberPicker== "CoverageValue")
	        	  {
	        		  tvCoverageValue.setText(String.valueOf(np.getValue()));
	        	  }
	        	  else if(OverviewnumberPicker== "ServicetestValue")
	        	  {
	        		  tvServicetestValue.setText(String.valueOf(np.getValue()));
	        	  }
	              d.dismiss();
	           }    
	          });
	         b2.setOnClickListener(new OnClickListener()
	         {
	          @Override
	          public void onClick(View v) {
	              d.dismiss();
	           }    
	          });
	       d.show();


	    }
	  
	  private void loadSavedPreferences() {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			
			boolean IsEngineeringMode = sharedPreferences.getBoolean(CommonValues.ENGINEERING_MODE, false);
			boolean IsDatatest = sharedPreferences.getBoolean(CommonValues.IS_DATA_TEST, true);
			boolean IsURLTest = sharedPreferences.getBoolean(CommonValues.IS_URL_TEST, true);
			boolean IsStreamingTest = sharedPreferences.getBoolean(CommonValues.IS_STREAMING_TEST, true);
			boolean IsGPS = sharedPreferences.getBoolean(CommonValues.IS_GPS, true);
			boolean IsNFC = sharedPreferences.getBoolean(CommonValues.IS_NFC, false);
			
			String ConverageSeconds = sharedPreferences.getString(CommonValues.CONVERAGE_SECONDS, "30");
			String ServicetestSeconds = sharedPreferences.getString(CommonValues.SERVICE_TEST_SECONDS, "300");
			
			int coverageValue=0,servicetestValue=0;
			coverageValue=Integer.parseInt(String.valueOf(ConverageSeconds));
			servicetestValue=Integer.parseInt(String.valueOf(ServicetestSeconds));	
			
			if(coverageValue>0)
			{
				tvCoverageValue.setText(String.valueOf(coverageValue));
			}
			
			if(servicetestValue>0)
			{
				tvServicetestValue.setText(String.valueOf(servicetestValue));
			}
			
			if(IsEngineeringMode)
			{
				ivEngineeringModeOn.setVisibility(View.VISIBLE);
				ivEngineeringModeOff.setVisibility(View.GONE);
			}
			else
			{
				ivEngineeringModeOn.setVisibility(View.GONE);
				ivEngineeringModeOff.setVisibility(View.VISIBLE);
			}
			
			if(IsDatatest)
			{
				ivDeviceTestOn.setVisibility(View.VISIBLE);
				ivDeviceTestOff.setVisibility(View.GONE);
			}
			else
			{
				ivDeviceTestOn.setVisibility(View.GONE);
				ivDeviceTestOff.setVisibility(View.VISIBLE);
			}
			
			if(IsURLTest)
			{
				ivUrlTestOn.setVisibility(View.VISIBLE);
				ivUrlTestOff.setVisibility(View.GONE);
			}
			else
			{
				ivUrlTestOn.setVisibility(View.GONE);
				ivUrlTestOff.setVisibility(View.VISIBLE);
			}
			
			if(IsStreamingTest)
			{
				ivStreamingTestOn.setVisibility(View.VISIBLE);
				ivStreamingTestOff.setVisibility(View.GONE);
			}
			else
			{
				ivStreamingTestOn.setVisibility(View.GONE);
				ivStreamingTestOff.setVisibility(View.VISIBLE);
			}
			
			
			if(IsGPS)
			{
				ivAccuracyOn.setVisibility(View.VISIBLE);
				ivAccuracyOff.setVisibility(View.GONE);
			}
			else
			{
				ivAccuracyOn.setVisibility(View.GONE);
				ivAccuracyOff.setVisibility(View.VISIBLE);
			}
			
			if(IsNFC)
			{
				tvNFCOn.setVisibility(View.VISIBLE);
				tvNFCOff.setVisibility(View.GONE);
			}
			else
			{
				tvNFCOn.setVisibility(View.GONE);
				tvNFCOff.setVisibility(View.VISIBLE);
			}	
		}

	  private void savePreferences(String key, boolean value) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, value);
			editor.commit();
		}

		private void savePreferences(String key, String value) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}
	  
	  

}
