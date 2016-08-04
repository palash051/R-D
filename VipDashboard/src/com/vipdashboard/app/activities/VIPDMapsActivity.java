package com.vipdashboard.app.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

import android.app.Activity;
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
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VIPDMapsActivity extends Activity implements IAsynchronousTask,OnClickListener,
		LocationListener {
	private GoogleMap map;
	private String signalLevel, displaycid, networktype, operatorname;
	int lac, cid, psc;
	
	//ProgressBar progressRx;
	
	TextView tvVIPDMaps,tvVIPNetwork,tvVIPServices,tvApplication,tvUsage,tvlarlangDetails;
	
	TextView tvlatlang, tvNetworkCIDRxType, tvOperatorName, 
				tvPhoneType,tvMCC,tvMNC,tvDataConnectionType,
				tvRNC,tvUMTS,tvPSC,tvSpeedTest;
	DownloadableAsyncTask downloadableAsyncTask;

	private static final int DOWNLOAD_MAP = 0;
	private static final int DOWNLOAD_SERVICE_USAGES = 1;
	private static final int DOWNLOAD_MOBILE_USAGES = 2;

	int downloadType = DOWNLOAD_MAP;

	private static final int EXCELLENT_LEVEL = 75;
	private static final int GOOD_LEVEL = 50;
	private static final int MODERATE_LEVEL = 25;
	private static final int WEAK_LEVEL = 0;
	
	LinearLayout llmyexperience;
	boolean isVisibleRequired=false; 
	
	RelativeLayout rlLatestUpdateHeader;

	private static final int INFO_SIGNAL_LEVEL_INFO_INDEX = 0;
	/*
	 * private static final int INFO_SERVICE_STATE_INDEX = 5; private static
	 * final int INFO_CELL_LOCATION_INDEX = 1; private static final int
	 * INFO_CALL_STATE_INDEX = 2; private static final int
	 * INFO_CONNECTION_STATE_INDEX = 3; private static final int
	 * INFO_SIGNAL_LEVEL_INDEX = 4; private static final int
	 * INFO_DATA_DIRECTION_INDEX = 6; private static final int
	 * INFO_DEVICE_INFO_INDEX = 7;
	 */

	private static final int[] info_ids = { R.id.tvNetworkCIDRxType

	};
	Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState= getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_maps); 
		
		

		 if(savedInstanceState!=null && savedInstanceState.containsKey("isVisibleRequired"))
		 {
			 isVisibleRequired=savedInstanceState.getBoolean("isVisibleRequired");
		 }

		initilization();
		getLocationAddress();
		downloadType = DOWNLOAD_MAP;
		
		//startService(new Intent(this, MyNetService.class)); // Demo code. No
															// need to put here
		runDownloadable();

	}
	
	

	private void initilization() {
		
		rlLatestUpdateHeader = (RelativeLayout) findViewById(R.id.rlLatestUpdateHeader);
		tvlarlangDetails = (TextView) findViewById(R.id.tvlarlangDetails);
		tvlatlang = (TextView) findViewById(R.id.tvlatlang);
		tvNetworkCIDRxType = (TextView) findViewById(R.id.tvNetworkCIDRxType);
		
		tvOperatorName = (TextView) findViewById(R.id.tvOperatorName);
		tvPhoneType = (TextView) findViewById(R.id.tvPhoneType);
		tvMCC = (TextView) findViewById(R.id.tvMCC);
		tvMNC = (TextView) findViewById(R.id.tvMNC);
		tvDataConnectionType= (TextView) findViewById(R.id.tvDataConnectionType);
		
		//progressRx = (ProgressBar) findViewById(R.id.progressRx);
		
		tvRNC= (TextView) findViewById(R.id.tvRNC);
		tvUMTS= (TextView) findViewById(R.id.tvUMTS);
		tvPSC=(TextView) findViewById(R.id.tvPSC);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapVip))
				.getMap();
		
		
		tvVIPDMaps = (TextView) findViewById(R.id.tvVIPDMaps);
		tvVIPNetwork = (TextView) findViewById(R.id.tvVIPNetwork);
		
		tvVIPServices = (TextView) findViewById(R.id.tvVIPServices); 
		tvApplication = (TextView) findViewById(R.id.tvApplication);
		tvUsage = (TextView) findViewById(R.id.tvUsage);
		tvSpeedTest= (TextView) findViewById(R.id.tvSpeedTest);
		
		tvVIPDMaps.setOnClickListener(this);
		tvVIPNetwork.setOnClickListener(this);
		tvVIPServices.setOnClickListener(this);
		tvApplication.setOnClickListener(this);
		tvSpeedTest.setOnClickListener(this);
		
		tvUsage.setOnClickListener(this);
		
		
		llmyexperience= (LinearLayout) findViewById(R.id.llmyexperience);
		
		if(isVisibleRequired)
		{
			llmyexperience.setVisibility(View.GONE);
		}
		
		// runDownloadable();
	}

	private void runDownloadable() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	private void initializeMap() {
		try {
			Bitmap defaultBitmap, defaultBitmapUser = null;
			map.clear();
			double defaultLatitude = 0, defaultLongitude = 0;

			if (MyNetService.currentLocation != null) {
				LatLng Location = new LatLng(
						MyNetService.currentLocation.getLatitude(),
						MyNetService.currentLocation.getLongitude());
				defaultLatitude = Location.latitude;
				defaultLongitude = Location.longitude;
				
				if(bitmap != null){
					defaultBitmapUser = bitmap;
				}else{
					defaultBitmapUser = BitmapFactory.decodeResource(
							getResources(), R.drawable.user_icon);
				}

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
					.target(Defaultlocation).zoom(17).bearing(90).tilt(30)
					.build();

			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			String currentlatlang ="Location : "+ " Latitude:" + String.valueOf(new DecimalFormat("##.####").format(defaultLatitude)) + ","
					+ "Longitude: " + String.valueOf(new DecimalFormat("##.####").format(defaultLongitude));
			//tvlatlang.setText(currentlatlang);
			//tvlatlang
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void displayTelephonyInfo() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		networktype = PhoneBasicInformation.getNetworkTypeString(tm
				.getNetworkType());
		GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
		/* String IMEI = tm.getDeviceId(); */
		lac = location.getLac()% 0xffff;
		cid = location.getCid()% 0xffff;
		psc= location.getPsc()% 0xffff;
		operatorname = tm.getNetworkOperatorName();
	}

	private void displayOperatorInfo() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String operatorname = tm.getNetworkOperatorName();
		String phonetype = getPhoneTypeString(tm.getPhoneType());
		String dataconnectiontype = PhoneBasicInformation.getNetworkTypeString(tm.getNetworkType());
		
		String networkOperator = tm.getNetworkOperator();

		if (networkOperator != null) {
			int mcc = Integer.parseInt(networkOperator.substring(0, 3));
			int mnc = Integer.parseInt(networkOperator.substring(3));
			//tvMCC.setText(String.valueOf(mcc));
			//tvMNC.setText(String.valueOf(mnc));
		}
		tvDataConnectionType.setText(dataconnectiontype);
		tvOperatorName.setText(operatorname);
		tvPhoneType.setText(phonetype);
	}

	private void displayServingCellInfo() {
		tvRNC.setText(String.valueOf(cid));
		tvUMTS.setText(String.valueOf(lac));
		//tvPSC.setText(String.valueOf(psc));
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
		default:
			typeString = "UNKNOWN";
			break;
		}

		return typeString;
	}

	private void setSignalLevel(int infoid, int level) {

		int signal = -113 + (level * 2);
		String signalLevel = String.valueOf(signal);
		/*displaycid = "Signal level:" + "Rx " + signalLevel + "  "
				+ "dBm";*/
		//tvNetworkCIDRxType.append("Rx "+ signalLevel + "  "+ "dBm");
		
		displaycid = networktype + " " + "RNC-CID/LAC:" + String.valueOf(cid)
				+ "/" + String.valueOf(lac) + " " + "Rx " + signalLevel + "  "
				+ "dBm";
		int currentsignal=((100+signal)*100)/(100-51);
		//progressRx.setMax(100);
		//progressRx.setProgress(currentsignal);
		//((TextView) findViewById(infoid)).setText(displaycid);
	}

	private String getSignalLevelString(int level) {

		String signalLevelString = "Weak";

		if (level > EXCELLENT_LEVEL)
			signalLevelString = "Excellent";
		else if (level > GOOD_LEVEL)
			signalLevelString = "Good";
		else if (level > MODERATE_LEVEL)
			signalLevelString = "Moderate";
		else if (level > WEAK_LEVEL)
			signalLevelString = "Weak";

		return signalLevelString;
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
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			try{
				URL url = new URL(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        Bitmap myBitmap = BitmapFactory.decodeStream(input);
		        bitmap = myBitmap;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		initializeMap();
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
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
			{	if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
		startSignalLevelListener();
		displayTelephonyInfo();
		displayOperatorInfo();
		displayServingCellInfo();
		downloadType = DOWNLOAD_MAP;

		super.onResume();
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
		else if(view.getId()==R.id.tvSpeedTest){
			Intent intent = new Intent(this,VIPD_SpeedTestActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
		
	}

private void getLocationAddress() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		String addressText = "";
		if (loc != null) {

			Geocoder geocoder = new Geocoder(this, Locale.getDefault());

			try {
				List<Address> addresses = geocoder.getFromLocation(
						loc.getLatitude(), loc.getLongitude(), 1);
				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					for (int lineIndex = 0; lineIndex < address
							.getMaxAddressLineIndex(); lineIndex++) {
						addressText = addressText
								+ address.getAddressLine(lineIndex) + ", ";
					}
					addressText = addressText + address.getLocality() + ", "
							+ address.getCountryName();
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		tvlarlangDetails.setText("Latitude: "+String.valueOf(new DecimalFormat("##.####").format(loc.getLatitude()))+", Longitude: "+ String.valueOf(new DecimalFormat("##.####").format(loc.getLongitude())));
		tvlarlangDetails.append("\n"+addressText.replace("null", ""));
		//return addressText;
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

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
