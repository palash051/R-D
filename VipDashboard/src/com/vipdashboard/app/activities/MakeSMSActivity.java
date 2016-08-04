package com.vipdashboard.app.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.MakeSMSAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MakeSMSActivity extends MainActionbarBase implements LocationListener,
		IAsynchronousTask, OnClickListener {
	private static Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	GoogleMap map;
	ListView list;
	EditText messageFields;
	Button bSendMessage;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	public static String Number;
	ArrayList<Marker> markers;
	private final int DOWNLOAD_SMS = 0;
	private final int DOWNLOAD_ALL_SMS = 1;
	private final int SEND_SMS = 2;
	int selectedBackGroundPreocess = DOWNLOAD_SMS;
	private boolean isDownloadRunning = false;
	boolean isSendingOrAllRunning = false;
	MakeSMSAdapter adapter;
	String messageText;
	int rowCountSMS = 0;
	Bitmap ownImage, callerImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_a_sms);
		init();
		
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
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
		Bundle savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("SMS_NUMBER")) {
			Number = savedInstanceState.getString("SMS_NUMBER");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{	if (!isFinishing()) 
		{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
		}
		selectedBackGroundPreocess = DOWNLOAD_ALL_SMS;		
		initThread();
		LoadInformation();
		initmap();
	}

	private void initmap() {
		new getSMSSenderImage().execute();
	}

	private void initThread() {
		recieveMessageRunnable = new Runnable() {
			public void run() {
				if (!isDownloadRunning && !isSendingOrAllRunning) {
					isDownloadRunning = true;
					LoadInformation();
				}
				recieveMessageHandler
						.postDelayed(recieveMessageRunnable, 10000);
			}
		};
		recieveMessageHandler.postDelayed(recieveMessageRunnable, 10000);
	}

	private void init() {
		recieveMessageHandler = new Handler();
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapMakeSMS))
				.getMap();
		list = (ListView) findViewById(R.id.lvSMSList);
		messageFields = (EditText) findViewById(R.id.etMessageText);
		bSendMessage = (Button) findViewById(R.id.bSendMessage);
		bSendMessage.setOnClickListener(this);
	}

	public void initmap(double latitude, double longitude) {
		String addressText = "";
		markers = new ArrayList<Marker>();
		Bitmap defaultBitmap, defaultBitmapUser;
		double defaultLatitude = 0, defaultLongitude = 0;
		double firstPersonLatitude = 0, firstPersonLongitude = 0,secondPersonLatitude = 0, secondPersonLongitude = 0;
		map.clear();
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc != null) {
			LatLng Location1 = new LatLng(loc.getLatitude(), loc.getLongitude());
			defaultLatitude = Location1.latitude;
			defaultLongitude = Location1.longitude;
			//
			firstPersonLatitude=defaultLatitude;
			firstPersonLongitude=defaultLongitude;
			//
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());

			try {
				List<Address> addresses = geocoder.getFromLocation(
						defaultLatitude, defaultLongitude, 1);
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
				if(ownImage != null){
					defaultBitmapUser = ownImage;
				}else {
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
				
				markers.add(map.addMarker(new MarkerOptions().position(Location1)
						.title(addressText)
						.icon(BitmapDescriptorFactory.fromBitmap(bmp))));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		addressText = "";
		LatLng Location2 = new LatLng(latitude, longitude);
		defaultLatitude = latitude;
		defaultLongitude = longitude;
		//
		secondPersonLatitude=defaultLatitude;
		secondPersonLongitude=defaultLongitude;
		//
		
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(
					defaultLatitude, defaultLongitude, 1);
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
			if(callerImage != null){
				defaultBitmapUser = callerImage;
			}else {
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
			markers.add(map.addMarker(new MarkerOptions().position(Location2)
					.title(addressText)
					.icon(BitmapDescriptorFactory.fromBitmap(bmp))));
		} catch (IOException e) {

			e.printStackTrace();
		}
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		if(markers.size() > 0){
			for (Marker m : markers) {
			    b.include(m.getPosition());
			}
			LatLngBounds bounds = b.build();
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 35,35,5);
			map.animateCamera(cu);
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

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
	}

	@Override
	public void hideProgressLoader() {
	}

	@Override
	public Object doBackgroundPorcess() {
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		ArrayList<PhoneSMSInformation> smsInformation = null;
		if(selectedBackGroundPreocess == DOWNLOAD_ALL_SMS){
			smsInformation =  database.getSMSInformation(Number);
			database.close();
			return smsInformation;
		} else if(selectedBackGroundPreocess == DOWNLOAD_SMS){
			smsInformation =  database.getSMSInformation(Number);
			database.close();
			return smsInformation;
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			ArrayList<PhoneSMSInformation> sms = (ArrayList<PhoneSMSInformation>) data;
			if(selectedBackGroundPreocess == DOWNLOAD_ALL_SMS){				
				if(sms != null && sms.size()>0){
					adapter = new MakeSMSAdapter(this, R.layout.message_layout, sms);
					list.setAdapter(adapter);
					if(adapter.getCount()>0){
						list.setSelection(adapter.getCount() - 1);					
					}
					rowCountSMS = adapter.getCount();
				}
				selectedBackGroundPreocess = DOWNLOAD_SMS;
				isDownloadRunning=false;
				isSendingOrAllRunning=false;
			}else if(selectedBackGroundPreocess == DOWNLOAD_SMS){
				if(sms != null && sms.size()>0){
					if(sms.size() > rowCountSMS){
						adapter.addItem(sms.get(sms.size()-1));
						adapter.notifyDataSetChanged();
						list.setSelection(adapter.getCount()-1);
					}
					rowCountSMS = sms.size();
					selectedBackGroundPreocess = DOWNLOAD_SMS;
					isDownloadRunning=false;
				}else if(!isSendingOrAllRunning){
					selectedBackGroundPreocess = DOWNLOAD_SMS;
					isDownloadRunning=false;
				}
			}
		}
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
	public void onClick(View view) {
		if(view.getId() == R.id.bSendMessage){
			if(!messageFields.getText().toString().equals("")){
				messageText = messageFields.getText().toString();
				
				try{
					android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
				    smsManager.sendTextMessage(Number, null, messageFields.getText().toString(), null, null);
				    
				    PhoneSMSInformation phoneSMSInformation=new PhoneSMSInformation();
			        phoneSMSInformation.PhoneId=MyNetService.phoneBasicInformation.PhoneId;
			        phoneSMSInformation.SMSType=2;
			        phoneSMSInformation.Number=Number;
			        phoneSMSInformation.SMSBody=messageText;	        
			        phoneSMSInformation.Latitude=MyNetService.currentLocation.getLatitude();
			        phoneSMSInformation.Longitude=MyNetService.currentLocation.getLongitude();
			        phoneSMSInformation.SMSTime=new Date();
			        phoneSMSInformation.LocationName=MyNetService.currentLocationName;
			        TelephonyManager tm = (TelephonyManager) this
							.getSystemService(Context.TELEPHONY_SERVICE);
					GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
					
			        phoneSMSInformation.LAC=String.valueOf(location.getLac() % 0xffff);
			        phoneSMSInformation.CellID=String.valueOf(location.getCid() % 0xffff);
			        MyNetDatabase mynetDatabase = new MyNetDatabase(this);
					mynetDatabase.open();
					mynetDatabase.createPhoneSMSInformation(phoneSMSInformation);			
					mynetDatabase.close();
					
					/*if(adapter == null){
						adapter = new MakeSMSAdapter(this, R.layout.message_layout, new ArrayList<PhoneSMSInformation>());
						adapter.addItem(phoneSMSInformation);
						adapter.notifyDataSetChanged();
						list.setSelection(adapter.getCount() - 1);
					}*/
					
					selectedBackGroundPreocess = DOWNLOAD_ALL_SMS;
				    messageFields.setText("");
					isDownloadRunning=true;
					isSendingOrAllRunning=true;
				    LoadInformation();
				}catch(Exception e){
					e.printStackTrace();
				}				
			}else{
				Toast.makeText(this, "Empty message. Cannot send!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public class getSMSSenderImage extends AsyncTask<Object, Object, Object>{
		private String withoutPlusNumber;
		@Override
		protected Object doInBackground(Object... params) {
			if (CommonValues.getInstance().LoginUser.Facebook_Person != null) {
				try {
					URL url = new URL(
							CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					ownImage = myBitmap;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			withoutPlusNumber = Number;
			if (Number.contains("+")) {
				withoutPlusNumber = Number.replace("+", "P");
			}
			IUserManager userManager = new UserManager();
			UserGroupUnion userGroupUnion = userManager
					.GertUserCurrentlocation(withoutPlusNumber);
			try {
				if (userGroupUnion != null) {
					URL url = new URL(userGroupUnion.ProfileImagePath);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					callerImage = myBitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return userGroupUnion;
		}
		
		@Override
		protected void onPostExecute(Object data) {
			if(data != null){
				UserGroupUnion userGroupUnion = (UserGroupUnion) data;
				if(userGroupUnion.Latitude > 0.0 && userGroupUnion.Longitude > 0.0){
					initmap(userGroupUnion.Latitude, userGroupUnion.Longitude);
				}
			}
		}
		
	}
}
