package com.vipdashboard.app.activities;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Session.StatusCallback;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.base.OnSwipeTouchListener;
import com.vipdashboard.app.classes.HelthCheckRoutine;
import com.vipdashboard.app.classes.MyNetLinkedInActivity;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.VIPDCustomerIssueRoot;
//import com.vipdashboard.app.entities.VIPDCustomerIssueRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;


public class AssistanceActivity extends Activity implements
IAsynchronousTask,	OnClickListener, OnKeyListener, OnItemClickListener {
	RelativeLayout rlnetworkDoctor,  rlaskaFriend, rlcontactUs, rlliveSupport, rlSelfService, rlNetworkdoctorRelativeLayout, rlLivesupportRelativeLayout,
	rlAskafriendRelativeLayout;
	TextView tvnetworkDoctor, tvReport, tvaskaFriend, tvcontactUs, tvliveSupport, tvSelfService;
	ImageView imageView;
	TextView tvPerformance, tvInternet, tvMakeCall, tvCallDrop,
			tvConnectToInternet, tvGettingDisconnected, tvInternetSpeed,
			tvWrongBilling, tvPerformancePoor;
	EditText etMessageBox, etSubject;
	TextView tvSendEmail, tvCallCustomerCare;
	TextView tvBadExperienceHistry;
	EditText etMessageBoxContact, email;
	ImageView linkedIN, googlePlus;
	LoginButton facebook;
	
	LinearLayout llVoicetoText;
	
	Button bHealthCheckRoutines;
	TextView tvVoicetoText;
	private static final int REQUEST_CODE = 1234;
	
	
	EditText etMessageBoxLiveSupport;
	ViewFlipper vfAssistance;
	ListView listview;
	ProgressDialog progressDialog;
	Button self_service_1, self_service_account_info, self_service_2, bTellMeMore, bHelthCheckRouting;
	ReportAdapter adapter;
	
	int lac, cid, psc;
	
	private String signalLevel, displaycid, networktype, operatorname;
	
	TextView tvUserExperinceStart,tvUserExperinceExperience,tvUserExperinceHistory;
	public static boolean isBackFromReport=false;
	
	boolean isSubmitted=false;
	
	TextView tvlatlang, tvNetworkCIDRxType,tvNavigationUrl,text,tvServingCell;
	
	TextView tvOperatorName, 
	tvPhoneType,tvMCC,tvMNC,tvDataConnectionType,
	tvRNC,tvUMTS,tvPSC;
	
	DownloadableAsyncTask downloadableAsyncTask;

	
	private GoogleMap map;
	
	private static final int INFO_SIGNAL_LEVEL_INFO_INDEX = 0;
	
	private static final int[] info_ids = { R.id.tvNetworkCIDRxType

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipdnetworkassistance);
		
		vfAssistance = (ViewFlipper) findViewById(R.id.vfAssistance);
		Initialization();
		//registerSwipeListener();
		
		runDownloadable();
	}
	
	private void registerSwipeListener(){
		vfAssistance.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}
			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		rlNetworkdoctorRelativeLayout
		.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}
			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		rlLivesupportRelativeLayout
		.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}
			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
		rlAskafriendRelativeLayout
		.setOnTouchListener(new OnSwipeTouchListener(
				this) {
			public void onSwipeRight() {
				//arrangeRightSwipe();
			}

			public void onSwipeLeft() {
				//arrangeSwipeLeft();
			}
		});
	}
	
	private void arrangeRightSwipe(){
		if (vfAssistance.getDisplayedChild() != 0) {
			if(vfAssistance.getDisplayedChild() == 2){
				arrageAskFrendTab();
			}else if(vfAssistance.getDisplayedChild() == 1){
				arrangeNetworkDoctorTab();
			}
			vfAssistance.showPrevious();
		}else{
			Intent intent = new Intent(this, NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	
	private void arrangeSwipeLeft(){
		if(vfAssistance.getDisplayedChild() == 0){
			arrageAskFrendTab();
		}else if(vfAssistance.getDisplayedChild() == 1){
			arrangeLiveSupportTab();
		}
		if(vfAssistance.getDisplayedChild() != vfAssistance.getChildCount() - 1){
			vfAssistance.showNext();
		}else{
			Intent intent = new Intent(this, NetworkSelfcareMyExperinceShowMoreActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	
	private void displayServingCellInfo() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		String networkOperator = tm.getNetworkOperator();

		if (networkOperator != null) {
			int mcc = Integer.parseInt(networkOperator.substring(0, 3));
			int mnc = Integer.parseInt(networkOperator.substring(3));
			tvServingCell.setText(String.valueOf(mcc));
			tvServingCell.append("-"+String.valueOf(String.valueOf(mnc)));
		}
		
		networktype = PhoneBasicInformation.getNetworkTypeString(tm.getNetworkType());
		GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
		tvServingCell.append("-"+String.valueOf(location.getLac() % 0xffff));
		tvServingCell.append("-"+String.valueOf(location.getCid() % 0xffff));
		
		
		//tvServingCell.append("  PSC:"+String.valueOf(location.getPsc()));
		
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
		startSignalLevelListener();
		displayServingCellInfo();
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

	
	private void setSignalLevel(int infoid, int level) {

		int signal = -113 + (level * 2);
		String signalLevel = String.valueOf(signal);
		/*displaycid = networktype + " " + "RNC-CID/LAC:" + String.valueOf(cid)
				+ "/" + String.valueOf(lac) + " " + "Rx " + signalLevel + "  "
				+ "dBm";*/

		displaycid = networkQualityString(signal)+ "("+ signalLevel + "  "
				+ "dBm)";
		
		int currentsignal=((100+signal)*100)/(100-51);
		
		((TextView) findViewById(infoid)).setText(displaycid);
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

	
	private void initializeMap() {
		try {
			String addressText="";
			Bitmap defaultBitmap, defaultBitmapUser;

			double defaultLatitude = 0, defaultLongitude = 0;

			if (MyNetService.currentLocation != null) {
				LatLng Location = new LatLng(
						MyNetService.currentLocation.getLatitude(),
						MyNetService.currentLocation.getLongitude());
				defaultLatitude = Location.latitude;
				defaultLongitude = Location.longitude;

				defaultBitmapUser = BitmapFactory.decodeResource(
						getResources(), R.drawable.user_icon);

				defaultBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.google_custom_pin);

				defaultBitmapUser = Bitmap.createScaledBitmap(
						defaultBitmapUser, defaultBitmap.getWidth() - 12,
						defaultBitmap.getHeight() - 40, true);
				Bitmap bmp = Bitmap.createBitmap(defaultBitmap.getWidth(),
						defaultBitmap.getHeight(), defaultBitmap.getConfig());
				Canvas canvas = new Canvas(bmp);
				canvas.drawBitmap(defaultBitmap, new Matrix(), null);
				canvas.drawBitmap(defaultBitmapUser, 8, 10, null);
				Geocoder geocoder =
		                new Geocoder(this, Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(Location.latitude, Location.longitude, 1);
				
				if (addresses != null && addresses.size() > 0) {			                
	                Address address = addresses.get(0);			                
	                for (int  lineIndex=0 ;lineIndex<address.getMaxAddressLineIndex();lineIndex++) {
	                	addressText=addressText+address.getAddressLine(lineIndex)+", ";
					}
	                addressText=addressText+address.getLocality()+", "+address.getCountryName();
	            }
				map.addMarker(new MarkerOptions().position(Location).title("")
						.icon(BitmapDescriptorFactory.fromBitmap(bmp)));

			}

			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			LatLng Defaultlocation = new LatLng(defaultLatitude,
					defaultLongitude);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation,
					14.0f));

			map.animateCamera(CameraUpdateFactory.zoomIn());

			map.animateCamera(CameraUpdateFactory.zoomTo(10));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(Defaultlocation).zoom(17).bearing(90).tilt(30).build();
			
			
			tvlatlang.setText("Latitude: "+String.valueOf(new DecimalFormat("##.####").format(defaultLatitude))+", Longitude: "+ String.valueOf(new DecimalFormat("##.####").format(defaultLongitude)));
			
			tvlatlang.append("\n"+addressText.replace("null", ""));
			
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			/*String currentlatlang =tvlatlang.getText()+ "Latitude:" + defaultLatitude + ", "
					+ "Longitude:" + defaultLongitude;
			tvlatlang.setText(currentlatlang);*/
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void Initialization() {

		//imageView = (ImageView) findViewById(R.id.image);
		text = (TextView) findViewById(R.id.text);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapVip))
				.getMap();
		
		
		tvlatlang=(TextView) findViewById(R.id.tvlatlang);
		tvNetworkCIDRxType= (TextView) findViewById(R.id.tvNetworkCIDRxType);
		tvNavigationUrl= (TextView) findViewById(R.id.tvNavigationUrl);
		bTellMeMore= (Button) findViewById(R.id.bTellMeMore);
		
		tvVoicetoText=(TextView) findViewById(R.id.tvVoicetoText);
		bHealthCheckRoutines=(Button) findViewById(R.id.bHealthCheckRoutines);
		bHealthCheckRoutines.setOnClickListener(this);
		
		tvServingCell=(TextView) findViewById(R.id.tvServingCell);
		
		llVoicetoText=(LinearLayout) findViewById(R.id.llVoicetoText);
		/*tvMCC = (TextView) findViewById(R.id.tvMCC);*/
		/*tvMNC = (TextView) findViewById(R.id.tvMNC);*/
		
		/*tvUMTS= (TextView) findViewById(R.id.tvUMTS);
		tvRNC= (TextView) findViewById(R.id.tvRNC);
		tvPSC= (TextView) findViewById(R.id.tvPSC);*/
		
		tvNavigationUrl.setOnClickListener(this);
		bTellMeMore.setOnClickListener(this);
		
		CommonTask.makeLinkedTextview(this, tvNavigationUrl, "Tap here for more");
	}
	
	private void runDownloadable() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
			
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void onClick(View view) {
		

		if(view.getId() == R.id.bHealthCheckRoutines){
			
			StartVoiceCommand();
		}
		if(view.getId() == R.id.bTellMeMore){
			Intent intent = new Intent(this, AssistanceReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(view.getId() == R.id.tvNavigationUrl)
		{
			Intent intent = new Intent(this, VIPDQuickLinkActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	
	private void StartVoiceCommand() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speek something....");
    startActivityForResult(intent, REQUEST_CODE);
		
	}
	
	/*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            
            if (matches.contains("close")) {
                finish();
           }
            
            tvVoicetoText.setText(matches.get(0));
           
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
	

	private void arrangeLiveSupportTab() {
		tvnetworkDoctor.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		/*tvnetworkDoctor.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		tvaskaFriend.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		/*tvcontactUs.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		tvliveSupport.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		/*tvSelfService.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		// vfAssistance.setDisplayedChild(3);
	}	

	private void arrageAskFrendTab() {
		tvnetworkDoctor.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		/*tvnetworkDoctor.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		tvaskaFriend.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		/*tvcontactUs.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		tvliveSupport.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		/*tvSelfService.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		// vfAssistance.setDisplayedChild(1);
	}

	private void arrangeNetworkDoctorTab() {
		tvnetworkDoctor.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		/*tvnetworkDoctor.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		tvaskaFriend.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		/*tvcontactUs.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		tvliveSupport.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		/*tvSelfService.setBackgroundColor(getResources().getColor(
				R.color.value_text));*/
		// vfAssistance.setDisplayedChild(0);
	}

	@Override
	public boolean onKey(View arg0, int code, KeyEvent keyevent) {
		if(keyevent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
			String message = etMessageBoxContact.getText().toString();
			String to = email.getText().toString().trim();
			if(android.util.Patterns.EMAIL_ADDRESS.matcher(to).matches()){
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				String aEmailList[] = {to};
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Network Problem");
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
				startActivity(emailIntent);
			}
			
		}
		return true;
	}
	
	
	private void PostStatusInFacebook() {
		try{
			com.facebook.Session session = com.facebook.Session.getActiveSession();
			if(session != null && session.isOpened()){
				com.facebook.Session.openActiveSession(this, false, new StatusCallback() {					
					@Override
					public void call(final com.facebook.Session session, SessionState state,
							Exception exception) {
						if(session.isOpened()){
							Request.newMeRequest(session, new GraphUserCallback() {								
								@Override
								public void onCompleted(GraphUser user, Response response) {
									publishFeedDialog();
								}
							}).executeAsync();
						}
					}
				});
			}else{
				try{
					com.facebook.Session.openActiveSession(this, true, new com.facebook.Session.StatusCallback() {
							@Override
							public void call(final com.facebook.Session session, SessionState state, Exception exception) {
								if(session.isOpened()){									
									Request.newMeRequest(session, new GraphUserCallback() {								
										@Override
										public void onCompleted(GraphUser user, Response response) {
											publishFeedDialog();
										}
									}).executeAsync();
								}
						}
					});
					}catch (Exception e) {
						Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
						onBackPressed();
					}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void searchMatchPage(String voiceCommand) {
		
		 if(voiceCommand!="")
		    {
		    	if(voiceCommand.equals("map")||
		    			voiceCommand.equals("maps")
		    			||voiceCommand.equals("network latest"))
		    	{
		    		Intent intent = new Intent(this,VIPDMapsActivity.class);
	    			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    			startActivity(intent);
		    	}
		    	else if(voiceCommand.equals("network")||
		    			voiceCommand.equals("Network usage"))
		    	{
		    		Intent objxxx = new Intent(this, VIPDNetworkUsageviewActivity.class);
		    		objxxx.setFlags(objxxx.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    			startActivity(objxxx);
		    	}
	    		else if(voiceCommand.equals("services")||
	    				voiceCommand.equals("services usage"))
		    	{
		    		Intent objThree = new Intent(this, VIPD_ServiceUsages.class);
		    		objThree.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(objThree);
		    	}
	    		else if(voiceCommand.equals("mobile")||
	    				voiceCommand.equals("mobile usage"))
		    	{
		    		Intent objMobile = new Intent(this, VIPDMobileUsageActivity.class);
		    		objMobile.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(objMobile);
		    	}
	    		else if(voiceCommand.equals("application")||
	    				voiceCommand.equals("application usage"))
		    	{
		    		Intent objApp = new Intent(this, VIPD_Application_Trafic_Usages.class);
		    		objApp.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(objApp);
		    	}
		    	
	    		else if(voiceCommand.equals("subscriber performance")||
	    				voiceCommand.equals("SPI")||
	    				voiceCommand.equals("Subscriber Performance Indicator"))
		    	{
			    	Intent objSPI= new Intent(this, VIPDSubscriberPerformanceIndicatorActivity.class);
			    	objSPI.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    			startActivity(objSPI);
		    	}
		    	
	    		else if(voiceCommand.equals("network performance")||
	    				voiceCommand.equals("technical report")||
	    				voiceCommand.equals("technical"))
		    	{
		    		Intent objDashboard = new Intent(this, DashboradActivity.class);
		    		objDashboard.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(objDashboard);
		    	}
		    	
	    		else if(voiceCommand.equals("report problem")||
	    				voiceCommand.equals("problem report")||voiceCommand.equals("ticket")||voiceCommand.equals("tickets"))
		    	{
		    		Intent objDashboard = new Intent(this, AssistanceReportActivity.class);
		    		objDashboard.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(objDashboard);
		    	}
	    		else if(voiceCommand.equals("test"))
		    	{
		    		Intent objDashboard = new Intent(this, VIPD_SpeedTestActivity.class);
		    		objDashboard.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(objDashboard);
		    	}
	    		else if(voiceCommand.equals("problem")||
	    				voiceCommand.equals("tracking")||voiceCommand.equals("problem tracking"))
		    	{
		    		Intent objDashboard = new Intent(this, VIPD_ProblemTrackingActivity.class);
		    		objDashboard.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(objDashboard);
		    	}
				
	    		else
	    		{	
	    			
	    			Toast.makeText(AssistanceActivity.this,"    "+voiceCommand+
                            "\nNo relevant page found",Toast.LENGTH_LONG).show();
	    			
	    			
	    			/*llVoicetoText.setVisibility(TextView.VISIBLE);
	                tvVoicetoText.setText(voiceCommand+" (No relevant page found)");*/
					// Voice Text Will Come
	    			// No Rel
	    		}
		    }
	}
	
	public String networkQualityString(int rxvalue) {
		
		String processedString="";
		
		 if(rxvalue<=-51 && rxvalue>=-75)
		    {
			 processedString="Excellent";
		    }
		 else if(rxvalue<=-76 && rxvalue>=-85)
		    {
			 processedString="Very good";
		    }
		 else if(rxvalue<=-86 && rxvalue>=-95)
		    {
			 processedString="Good";
		    }
		 else if(rxvalue<=-96 && rxvalue>=-105)
		    {
			 processedString="Poor";
		    }
		 else if(rxvalue<=-106 && rxvalue>=-113)
		    {
			 processedString="Extremely poor";
		    }
		 else if(rxvalue<-113)
		    {
			 processedString="Weak";
		    }
		 
		 
		 return processedString;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            
            if (matches.contains("close")) {
                finish();
           }
            
            //Voice command code here
            
            searchMatchPage(matches.get(0));
            
           /* llVoicetoText.setVisibility(TextView.VISIBLE);
            tvVoicetoText.setText(matches.get(0));*/
            super.onActivityResult(requestCode, resultCode, data);
           
        }
        /*else {
		super.onActivityResult(requestCode, resultCode, data);
		com.facebook.Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}*/}
	
	
	private void publishFeedDialog() {
		String message = etMessageBoxContact.getText().toString();		
		Bundle postParams = new Bundle();
        postParams.putString("name", "MyNet Apps");
        postParams.putString("caption", "Network Problem");
        postParams.putString("description", message.toString());
        postParams.putString("link", "www.oss-net.com");
 
	    WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,com.facebook.Session.getActiveSession(),postParams))
	        .setOnCompleteListener(new OnCompleteListener() {
	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(AssistanceActivity.this,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(AssistanceActivity.this, 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } 
	                else if (error instanceof FacebookOperationCanceledException) {
	                    Toast.makeText(AssistanceActivity.this, 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } 
	                else {
	                    Toast.makeText(AssistanceActivity.this, 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }
	        })
	        .build();
	    feedDialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		int ID = Integer.parseInt(String.valueOf(view.getTag()));
		if(ID ==1){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 2){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 3){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 4){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 5){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 6){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 7){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 8){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 9){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
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
	public Object doBackgroundPorcess() {
		
		
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		
			initializeMap();
			
		
	}
}
