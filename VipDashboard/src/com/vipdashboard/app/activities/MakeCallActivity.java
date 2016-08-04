package com.vipdashboard.app.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.classes.CallHistory;
import com.vipdashboard.app.classes.ContactList;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.UserActivityInformation;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

import android.app.Application;
import android.app.ProgressDialog;
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
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MakeCallActivity extends MainActionbarBase implements LocationListener,
		OnClickListener, IAsynchronousTask {
	
	TextView tvMyNetPostTitle, tvPersonName, tvNumber, tvdistance, tvtime, tvEvent_one, tvEvent_second, tvEvent_three;
	ImageView ivPersonImage, ivEvent_one, ivEvent_second, ivEvent_three, ivUserStatus;
	RelativeLayout rlCaller, rlsms, rlhistory;
	private String number,withoutPlusNumber;
	private String name, contactName,contactNumber;
	private long lastCallDuration;
	private GoogleMap map;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	Bitmap ownImage, callerImage;
	private CallHistory callHistory;
	public static Object object;
	//public static String contact_name, contact_number;
	public static PhoneCallInformation phoneCallInformation;
	ArrayList<Marker> markers;
	private AQuery aq;
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private boolean iscallfromContactList, iscallfromcalllog, iscallfromsms;
	int selectedCategoryEventOne,selectedCategoryEventTwo,selectedCategoryEventThree;
	Bitmap bitmap;
	
	public static boolean IsCalledAfterCalled=false;
	ImageView ivNext;
	
	TextView tvdistanceNotAvailable,
	tvdistanceInvite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_caller_details);
		init();
		if(savedInstanceState != null)
		{
			if(savedInstanceState.containsKey("CONTACT_NAME") && 
					savedInstanceState.containsKey("CONTACT_NUMBER") && 
					(savedInstanceState.containsKey("CALL_FROM_CONTACT_LIST")
							|| savedInstanceState.containsKey("CALL_FROM_CALL_LOG")
							|| savedInstanceState.containsKey("CALL_FROM_SMS"))){
				contactName = savedInstanceState.getString("CONTACT_NAME");
				contactNumber = savedInstanceState.getString("CONTACT_NUMBER");
				iscallfromContactList = savedInstanceState.getBoolean("CALL_FROM_CONTACT_LIST");
				iscallfromcalllog = savedInstanceState.getBoolean("CALL_FROM_CALL_LOG");
				iscallfromsms = savedInstanceState.getBoolean("CALL_FROM_SMS");
			}
		}
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
				
				if(ownImage != null){
					defaultBitmapUser = ownImage;
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

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();		
		super.onPause();
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
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
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		if(IsCalledAfterCalled)
			ivNext.setVisibility(View.VISIBLE);
			else
				ivNext.setVisibility(View.GONE);
		LoadObjectValue();
		LoadImage();
		LatestCommunication();
		LoadInformation();
		
		
	}

	private void LoadObjectValue() {
		
		String dateTime="";
		
		if(iscallfromcalllog){	
			number = contactNumber;
			name = contactName;
			//number = ((PhoneCallInformation) object).Number;
			//name = CommonTask.getContentName(this, number);
			//if (name != null) {
				tvPersonName.setText(contactName);
				
			//}
			tvNumber.setText(contactNumber);
			MyNetDatabase db = new MyNetDatabase(this);
			db.open();
			PhoneCallInformation phoneCallInformation = db.getLatestPhoneCallInfo(number);
			db.close();
			//lastCallDuration = ((PhoneCallInformation) object).CallTime.getTime();
			if(phoneCallInformation!=null && phoneCallInformation.CallTime!=null)
			{
				dateTime = (String) DateUtils.getRelativeTimeSpanString(
						phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);
			}
			tvtime.setText(dateTime);
		}else if(iscallfromsms){
			number = contactNumber;
			name = contactName;
			//number = ((PhoneSMSInformation) object).Number;
			//name = CommonTask.getContentName(this, number);
			//if (name != null) {
				tvPersonName.setText(contactName);
			//}
			tvNumber.setText(contactNumber);
			MyNetDatabase db = new MyNetDatabase(this);
			db.open();
			PhoneSMSInformation phoneSMSInformation = db.getLatestPhoneSMSInfo(number);
			db.close();
			//lastCallDuration = ((PhoneSMSInformation) object).SMSTime.getTime();
			
			if(phoneSMSInformation!=null && phoneSMSInformation.SMSTime!=null)
			{
			dateTime = (String) DateUtils.getRelativeTimeSpanString(
					phoneSMSInformation.SMSTime.getTime(), new Date().getTime(), 0);
			}
			
			tvtime.setText(dateTime);
		}
		else if(iscallfromContactList){
			number = contactNumber;
			name = contactName;
			tvPersonName.setText(contactName);
			tvNumber.setText(contactNumber);
			MyNetDatabase db = new MyNetDatabase(this);
			db.open();
			PhoneCallInformation phoneCallInformation = db.getLatestPhoneCallInfo(number);
			db.close();
			
			if(phoneCallInformation!=null && phoneCallInformation.CallTime!=null)
			{
				dateTime = (String) DateUtils.getRelativeTimeSpanString(
						phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);
			}
			
			tvtime.setText(dateTime);
		}
		if(name != null){
			tvMyNetPostTitle.setText("Me and " + name);
		}else{
			tvMyNetPostTitle.setText("Me and " + number);
		}
	}

	private void LoadImage() {
		
	}

	private void init() {	
		tvMyNetPostTitle = (TextView) findViewById(R.id.tvMyNetPostTitle);
		tvPersonName = (TextView) findViewById(R.id.tvPersonName);
		tvNumber = (TextView) findViewById(R.id.tvNumber);
		tvdistance = (TextView) findViewById(R.id.tvdistance);
		tvtime = (TextView) findViewById(R.id.tvtime);
		tvEvent_one = (TextView) findViewById(R.id.tvEvent_one);
		tvEvent_second = (TextView) findViewById(R.id.tvEvent_second);
		tvEvent_three =(TextView) findViewById(R.id.tvEvent_three);
		ivPersonImage = (ImageView) findViewById(R.id.ivPersonImage);
		ivEvent_one = (ImageView) findViewById(R.id.ivEvent_one);
		ivEvent_second = (ImageView) findViewById(R.id.ivEvent_second);
		ivEvent_three = (ImageView) findViewById(R.id.ivEvent_three);
		ivUserStatus = (ImageView) findViewById(R.id.ivUserStatus);		
		rlCaller = (RelativeLayout) findViewById(R.id.rlCaller);
		rlsms = (RelativeLayout) findViewById(R.id.rlsms);
		rlhistory = (RelativeLayout) findViewById(R.id.rlhistory);	
		ivNext= (ImageView) findViewById(R.id.ivNext);
		tvdistanceNotAvailable=(TextView) findViewById(R.id.tvdistanceNotAvailable);
		tvdistanceInvite=(TextView) findViewById(R.id.tvdistanceInvite);
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapCall))
				.getMap();
		
		aq=new AQuery(this);
		imgOptions = CommonValues.getInstance().defaultImageOptions; 		
		imgOptions.targetWidth=100;
		imgOptions.ratio=0;//AQuery.RATIO_PRESERVE;
		imgOptions.round = 8;
		
		rlCaller.setOnClickListener(this);
		rlsms.setOnClickListener(this);
		rlhistory.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		tvdistanceInvite.setOnClickListener(this);
	}

	private void initmap(double latitude, double longitude) {
		String addressText = "";
		markers = new ArrayList<Marker>();
		Bitmap defaultBitmap, defaultBitmapUser;
		double defaultLatitude = 0, defaultLongitude = 0;
		
		///
		double firstPersonLatitude = 0, firstPersonLongitude = 0,secondPersonLatitude = 0, secondPersonLongitude = 0;
		//
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
	public void onProviderEnabled(String arg0) {
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}

	@Override
	public void onClick(View view) {
		if(CommonTask.isOnline(getApplicationContext())){
		if(view.getId() == R.id.rlCaller){
			Intent calltomanager = new Intent(Intent.ACTION_CALL);
			calltomanager.setData(Uri.parse("tel:" + number));
			startActivity(calltomanager);	
		}
		else if(view.getId() == R.id.tvdistanceInvite)	
		{
		    Intent intent = new Intent(Intent.ACTION_VIEW);
			String smsbody = CommonValues.getInstance().LoginUser.FullName + " invited and recommended you to use Care.";
			intent.putExtra("sms_body",smsbody);
			intent.putExtra("address", contactNumber);
			intent.setType("vnd.android-dir/mms-sms"); 
			startActivity(intent);
	}
		else if(view.getId() == R.id.rlsms){
			Intent intent = new Intent(this, MakeSMSActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("SMS_NUMBER", number);
			startActivity(intent);
		}else if(view.getId() == R.id.rlhistory){
			if(callHistory == null)
				callHistory = new CallHistory(this);
			callHistory.showDetailsInformation(contactName,contactNumber,iscallfromContactList,iscallfromcalllog,iscallfromsms);
		}
		else if(view.getId() == R.id.ivNext){
			Intent intent = new Intent(Intent.ACTION_MAIN);
			  intent.addCategory(Intent.CATEGORY_HOME);
			  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			  startActivity(intent);
			}
		
		
		//
		else if(view.getId() == R.id.ivEvent_one||
				view.getId() == R.id.tvEvent_one){
			if(selectedCategoryEventOne==1)
			{
				Intent calltomanager = new Intent(Intent.ACTION_CALL);
				calltomanager.setData(Uri.parse("tel:" + number));
				startActivity(calltomanager);	
			}
			else if(selectedCategoryEventOne==2)
			{
				Intent intent = new Intent(this, MakeSMSActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("SMS_NUMBER", number);
				startActivity(intent);
			}
		}
		else if(view.getId() == R.id.ivEvent_second||
				view.getId() == R.id.tvEvent_second){
			if(selectedCategoryEventTwo==1)
			{
				Intent calltomanager = new Intent(Intent.ACTION_CALL);
				calltomanager.setData(Uri.parse("tel:" + number));
				startActivity(calltomanager);	
			}
			else if(selectedCategoryEventTwo==2)
			{
				Intent intent = new Intent(this, MakeSMSActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("SMS_NUMBER", number);
				startActivity(intent);
			}
		}
		else if(view.getId() == R.id.ivEvent_three||
				view.getId() == R.id.tvEvent_three){
			if(selectedCategoryEventThree==1)
			{
				Intent calltomanager = new Intent(Intent.ACTION_CALL);
				calltomanager.setData(Uri.parse("tel:" + number));
				startActivity(calltomanager);	
			}
			else if(selectedCategoryEventThree==2)
			{
				Intent intent = new Intent(this, MakeSMSActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("SMS_NUMBER", number);
				startActivity(intent);
			}
		}
		}
		
		
		else{
			Toast.makeText(getApplicationContext(), "NO internet connectivity.Please enable your Internet Connection", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		/*progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading Map...");
		progressDialog.setCancelable(false);
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		/*progressDialog.dismiss();*/
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
		        ownImage = myBitmap;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		withoutPlusNumber = number;
		if(number.contains("+")){
			withoutPlusNumber = number.replace("+", "P");
		}
		IUserManager userManager = new UserManager();
		UserGroupUnion userGroupUnion =  userManager.GertUserCurrentlocation(withoutPlusNumber);
		try{
			if(userGroupUnion != null){
				URL url = new URL(userGroupUnion.ProfileImagePath);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        Bitmap myBitmap = BitmapFactory.decodeStream(input);
		        callerImage = myBitmap;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return userGroupUnion;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			tvdistanceNotAvailable.setText("");
			tvdistanceInvite.setText("");
			tvdistanceNotAvailable.setVisibility(View.GONE);
			tvdistanceInvite.setVisibility(View.GONE);
			UserGroupUnion userGroupUnion = (UserGroupUnion) data;
			if(userGroupUnion.userOnlinestatus > 0)
				setUserStatus(userGroupUnion.userOnlinestatus);
			if(userGroupUnion.Latitude > 0.0 && userGroupUnion.Longitude > 0.0){
				int distence= CommonTask.distanceCalculationInMeter(MyNetService.currentLocation.getLatitude(), MyNetService.currentLocation.getLongitude(),userGroupUnion.Latitude , userGroupUnion.Longitude);
				distence=distence/1000;
				tvdistance.setText(distence<2?"within one km":String.valueOf(distence)+" km");
			}
			initmap(userGroupUnion.Latitude, userGroupUnion.Longitude);
		}
		else
		{
			/*if(CommonValues.getInstance().LoginUser.Facebook_Person != null){

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
			}*/
			
			SpannableString s=new SpannableString("Invite");
			s.setSpan(new UnderlineSpan(), 0, s.length(), 0);
			tvdistanceNotAvailable.setText("Not a Care user, ");
			tvdistanceInvite.setText(s);
			
			
			tvdistanceNotAvailable.setVisibility(View.VISIBLE);
			tvdistanceInvite.setVisibility(View.VISIBLE);
			initializeMap();
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	
		LoadCallerImage();
	}
	
	private void LoadCallerImage() {
		if(callerImage != null){
			ivPersonImage.setImageBitmap(callerImage);
		}else{
			int photoId=CommonTask.getContentPhotoId(this, number);
			if(photoId>0){
				ivPersonImage.setImageBitmap(CommonTask.fetchContactImageThumbnail(this,photoId));
			}
		}
		
	}

	private void LatestCommunication() {
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		ArrayList<UserActivityInformation> userActivityInformations = database.getUserActivityInformation(number);
		database.close();
		if(userActivityInformations != null && userActivityInformations.size()>0){
			int i=1;
			for (UserActivityInformation userActivityInformation : userActivityInformations) {
				
				switch(i)
				{
				case 1:
					if(userActivityInformation.ActivityTypeID == 1){
						//call
						ivEvent_one.setImageResource(R.drawable.big_phone);
						String dateTime = (String) DateUtils.getRelativeTimeSpanString(
						Long.parseLong(userActivityInformation.ActivityTime), new Date().getTime(), 0);
						tvEvent_one.setText(dateTime);
						selectedCategoryEventOne=1;
					}else if(userActivityInformation.ActivityTypeID == 2){
						//sms
						ivEvent_one.setImageResource(R.drawable.big_sms);
						tvEvent_one.setText(userActivityInformation.ActivityDetails.length()>30?userActivityInformation.ActivityDetails.substring(0, 30)+"...":userActivityInformation.ActivityDetails);
						selectedCategoryEventOne=2;
					}
					
					ivEvent_one.setOnClickListener(this);
					tvEvent_one.setOnClickListener(this);
					break;
				case 2:
					if(userActivityInformation.ActivityTypeID == 1){
						//call
						ivEvent_second.setImageResource(R.drawable.big_phone);
						String dateTime = (String) DateUtils.getRelativeTimeSpanString(
						Long.parseLong(userActivityInformation.ActivityTime), new Date().getTime(), 0);
						tvEvent_second.setText(dateTime);
						selectedCategoryEventTwo=1;
					}else if(userActivityInformation.ActivityTypeID == 2){
						//sms
						ivEvent_second.setImageResource(R.drawable.big_sms);
						tvEvent_second.setText(userActivityInformation.ActivityDetails.length()>30?userActivityInformation.ActivityDetails.substring(0, 30)+"...":userActivityInformation.ActivityDetails);
						selectedCategoryEventTwo=2;
					}
					ivEvent_second.setOnClickListener(this);
					tvEvent_second.setOnClickListener(this);
					break;
				case 3:
					if(userActivityInformation.ActivityTypeID == 1){
						//call
						ivEvent_three.setImageResource(R.drawable.big_phone);
						String dateTime = (String) DateUtils.getRelativeTimeSpanString(
						Long.parseLong(userActivityInformation.ActivityTime), new Date().getTime(), 0);
						tvEvent_three.setText(dateTime);
						selectedCategoryEventThree=1;
					}else if(userActivityInformation.ActivityTypeID == 2){
						//sms
						ivEvent_three.setImageResource(R.drawable.big_sms);
						tvEvent_three.setText(userActivityInformation.ActivityDetails.length()>30?userActivityInformation.ActivityDetails.substring(0, 30)+"...":userActivityInformation.ActivityDetails);
						selectedCategoryEventThree=2;	
					}
					
					ivEvent_three.setOnClickListener(this);
					tvEvent_three.setOnClickListener(this);
					break;
				}
				i++;
			}
		}
	}
	
	
	private void setUserStatus(long userOnlineAvailableStatusID) {
		switch ((int)userOnlineAvailableStatusID) {
		case CommonConstraints.ONLINE:
			ivUserStatus.setImageDrawable(this.getResources().getDrawable(R.drawable.user_status_online));
			break;
		case CommonConstraints.AWAY:
			ivUserStatus.setImageDrawable(this.getResources().getDrawable(R.drawable.user_status_away));
			break;
		case CommonConstraints.DO_NOT_DISTURB:
			ivUserStatus.setImageDrawable(this.getResources().getDrawable(R.drawable.user_status_busy));
			break;
		case CommonConstraints.INVISIBLE:
			ivUserStatus.setImageDrawable(this.getResources().getDrawable(R.drawable.user_status_offline));
			break;
		case CommonConstraints.OFFLINE:
			ivUserStatus.setImageDrawable(this.getResources().getDrawable(R.drawable.user_status_offline));
			break;
		case CommonConstraints.PHONEOFF:
			ivUserStatus.setImageDrawable(this.getResources().getDrawable(R.drawable.user_status_offline));
			break;
		case CommonConstraints.BUSY:
			ivUserStatus.setImageDrawable(this.getResources().getDrawable(R.drawable.user_status_busy));
			break;
		default:
			break;
		}
	}
	
	

}
