package com.vipdashboard.app.classes;

import java.awt.font.NumericShaper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.ExperinceLiveActivity;
import com.vipdashboard.app.activities.FB_ShareActivity;
import com.vipdashboard.app.activities.MakeCallActivity;
import com.vipdashboard.app.adapter.AllSMSAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.UserLocationActivityInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CallHistory implements OnClickListener, LocationListener, IAsynchronousTask {
	
	Context context;
	private String Number;
	private long callTime;
	private Dialog dialog;
	private TextView headertext;
	private TextView tvStatisticsHeaderDetailsTitle;
	private TextView tvName;
	private TextView date;
	private TextView outgoingCallTime;
	private ProgressBar outgoingProgress;
	private TextView incomingCallTime;
	private ProgressBar incomingProgress;
	private ImageView StatisticsSocailChartOverview;
	private TextView tvAll;
	private TextView tvToday;
	private TextView tvThisWeek;
	private TextView tvThisMonth;
	private TextView tvStatisticsSocialChart;
	private TextView tvStatisticsSocialMap;
	private ScrollView svDetails;
	private ScrollView svMap;
	private ImageView callDetails;
	private ImageView postDetails;
	private GoogleMap map;
	private TextView tvChatRecord;
	private LinearLayout llMap;
	private RelativeLayout rlchart;
	private ImageView ivStatisticsSocialPerson;
	private PhoneCallInformation phoneCallInformation;
	private PhoneSMSInformation phoneSMSInformation;
	private ContactList contactList;
	private int incommingCallCount;
	private int outgoingCallCount;
	private int missedCallCount;
	private int totalCallCount;
	private int dropCallCount;
	private int incommingCallDuaration;
	private int outgoingCallDuaration;
	private int totalCallDuaration;
	private ArrayList<PhoneCallInformation> callInformation;
	private boolean isCallFromChart;
	private boolean isCallFromMap;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	Bitmap ownImage=null;
	private boolean isCallFromContactList,isCallFromCallLog,isCallFromSMS; 
	
	public CallHistory(Context _context){
		context = _context;
	}
	
	public void showDetailsInformation(String Cname, String Cnumber, boolean contactList,boolean calllog, boolean sms) {
		if(dialog == null){
			dialog = new Dialog(this.context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(true);
			dialog.setContentView(R.layout.statistics_social_list_info);
			
			headertext = (TextView) dialog.findViewById(R.id.tvStatisticsDetailsTitle);
			tvStatisticsHeaderDetailsTitle = (TextView) dialog.findViewById(R.id.tvStatisticsHeaderDetailsTitle);
			tvName = (TextView) dialog
					.findViewById(R.id.tvStatisticsSocialMobileNumber);
			date = (TextView) dialog
					.findViewById(R.id.tvStatisticsSocialMobileNumberDate);
			outgoingCallTime = (TextView) dialog
					.findViewById(R.id.tvOutgoingCallValue);
			outgoingProgress = (ProgressBar) dialog
					.findViewById(R.id.progressOutgoingCalls);
			incomingCallTime = (TextView) dialog
					.findViewById(R.id.tvIncoingCallValue);
			incomingProgress = (ProgressBar) dialog
					.findViewById(R.id.progressIncomingCalls);
			StatisticsSocailChartOverview = (ImageView) dialog
					.findViewById(R.id.ivStatisticsSocailChartOverview);
			tvAll = (TextView) dialog.findViewById(R.id.tvExperinceFilterAll);
			tvToday = (TextView) dialog.findViewById(R.id.tvExperinceFilterToday);
			tvThisWeek = (TextView) dialog
					.findViewById(R.id.tvExperinceFilterThisWeek);
			tvThisMonth = (TextView) dialog
					.findViewById(R.id.tvExperinceFilterThisMonth);
			tvStatisticsSocialChart = (TextView) dialog.findViewById(R.id.tvStatisticsSocialChart);
			tvStatisticsSocialMap = (TextView) dialog.findViewById(R.id.tvStatisticsSocialMap);
			svDetails = (ScrollView) dialog.findViewById(R.id.svdetails);
			svMap = (ScrollView) dialog.findViewById(R.id.svMap);
			callDetails = (ImageView) dialog
					.findViewById(R.id.ivStatisticsSocialMenu_one);
			postDetails = (ImageView) dialog
					.findViewById(R.id.ivStatisticsSocialMenu_second);
			map = ((SupportMapFragment) ((MakeCallActivity)context).getSupportFragmentManager().findFragmentById(
					R.id.mapCaller)).getMap();
			
			
			tvChatRecord = (TextView) dialog.findViewById(R.id.tvChatRecord);
			llMap = (LinearLayout) dialog.findViewById(R.id.llMap);
			rlchart = (RelativeLayout) dialog.findViewById(R.id.rlchart);
			
			tvAll.setOnClickListener(this);
			tvToday.setOnClickListener(this);
			tvThisWeek.setOnClickListener(this);
			tvThisMonth.setOnClickListener(this);
			callDetails.setOnClickListener(this);
			postDetails.setOnClickListener(this);
			tvStatisticsSocialChart.setOnClickListener(this);
			tvStatisticsSocialMap.setOnClickListener(this);
		}
		ivStatisticsSocialPerson=(ImageView) dialog
				.findViewById(R.id.ivStatisticsSocialPerson);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
 /*		contactName = name;
		contactNumber = number;*/
		isCallFromContactList = contactList;
		isCallFromCallLog = calllog;
		isCallFromSMS = sms;
		String dateTime="";
		
		if(isCallFromCallLog){
			Number = Cnumber;
			//callTime = ((PhoneCallInformation) object).CallTime.getTime();
			MyNetDatabase db = new MyNetDatabase(context);
			db.open();
			PhoneCallInformation phoneCallInformation = db.getLatestPhoneCallInfo(Number);
			db.close();
			//lastCallDuration = ((PhoneCallInformation) object).CallTime.getTime();
			dateTime = (String) DateUtils.getRelativeTimeSpanString(
					phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);
			date.setText(dateTime);
		}else if(isCallFromSMS){
			Number = Cnumber;
			//callTime = ((PhoneSMSInformation) object).SMSTime.getTime();
			MyNetDatabase db = new MyNetDatabase(context);
			db.open();
			PhoneSMSInformation phoneSMSInformation = db.getLatestPhoneSMSInfo(Number);
			db.close();
			//lastCallDuration = ((PhoneCallInformation) object).CallTime.getTime();
			if(phoneSMSInformation != null){
				dateTime = (String) DateUtils.getRelativeTimeSpanString(
						phoneSMSInformation.SMSTime.getTime(), new Date().getTime(), 0);
			}
			date.setText(dateTime);
		}else if(isCallFromContactList){
			
			Number = Cnumber;
			//callTime = new Date().getTime();
			MyNetDatabase db = new MyNetDatabase(context);
			db.open();
			PhoneCallInformation phoneCallInformation = db.getLatestPhoneCallInfo(Number);
			db.close();
			//lastCallDuration = ((PhoneCallInformation) object).CallTime.getTime();
			if(phoneCallInformation != null){
				dateTime = (String) DateUtils.getRelativeTimeSpanString(
						phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);
			}
			date.setText(dateTime);
		}
		String name = CommonTask.getContentName(context,
				Number);
		if (name != "") {
			headertext.setText("Call Details between me and "+name);
			//tvStatisticsHeaderDetailsTitle.setText("Call Details between me and "+name);
			tvName.setText(name);
		} else {
			if(Number != null){
				headertext.setText("Call Details between me and "+Number);
				//tvStatisticsHeaderDetailsTitle.setText("Call Details between me and "+Number);
				tvName.setText(Number);
			}else{
				headertext.setText("Call Details between me and");
				//tvStatisticsHeaderDetailsTitle.setText("Call Details between me and");
				tvName.setText("");
			}
		}
		
		int photoId=CommonTask.getContentPhotoId(context, Number);
		if(photoId>0){
			ivStatisticsSocialPerson.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, photoId));
		}else{
			ivStatisticsSocialPerson.setImageDrawable(context.getResources().getDrawable(R.drawable.user_icon));
		}
		
		/*String dateTime = (String) DateUtils.getRelativeTimeSpanString(
				callTime, new Date().getTime(), 0);*/
		
		isCallFromChart = true;
		arrengeBasicTab();
		dialog.show();
	}

	private void arrengeThisMonthTab() {
		tvAll.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvToday.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisWeek.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisMonth.setBackgroundColor(this.context.getResources().getColor(
				R.color.header_text));
		if(Number != null){
			if(isCallFromChart)
				showCallDetails(CommonConstraints.INFO_TYPE_MONTH,
					Number);
			else
				LoadMap(CommonConstraints.INFO_TYPE_MONTH,
					Number);
		}
		
	}

	private void arrengeThisWeekTab() {
		tvAll.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvToday.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisWeek.setBackgroundColor(this.context.getResources().getColor(
				R.color.header_text));
		tvThisMonth.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		if(Number != null){
			if(isCallFromChart)
				showCallDetails(CommonConstraints.INFO_TYPE_WEEK,
					Number);
			else
				LoadMap(CommonConstraints.INFO_TYPE_WEEK,
					Number);
		}
	}

	private void arrengeToadayTab() {
		tvAll.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvToday.setBackgroundColor(this.context.getResources().getColor(
				R.color.header_text));
		tvThisWeek.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisMonth.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		if(Number != null){
			if(isCallFromChart)
				showCallDetails(CommonConstraints.INFO_TYPE_TODAY,
					Number);
			else
				LoadMap(CommonConstraints.INFO_TYPE_TODAY,
					Number);
		}
	}

	private void arrengeBasicTab() {
		tvAll.setBackgroundColor(this.context.getResources().getColor(
				R.color.header_text));
		tvToday.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisWeek.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisMonth.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		if(Number != null){
			if(isCallFromChart)
				showCallDetails(CommonConstraints.INFO_TYPE_ALL,
					Number);
			else
				LoadMap(CommonConstraints.INFO_TYPE_ALL,
					Number);
		}

	}

	private void showCallDetails(int type, String number) {
		incommingCallCount = 0;
		outgoingCallCount = 0;
		missedCallCount = 0;
		totalCallCount = 0;
		dropCallCount = 0;
		incommingCallDuaration = 0;
		outgoingCallDuaration = 0;
		totalCallDuaration = 0;
		NumberFormat formatter = new DecimalFormat("00");
		if (type == CommonConstraints.INFO_TYPE_ALL) {
			MyNetDatabase database = new MyNetDatabase(this.context);
			database.open();
			callInformation = database.getTotalCallInforByNumber(type, number);
			database.close();
		} else if (type == CommonConstraints.INFO_TYPE_TODAY) {
			MyNetDatabase database = new MyNetDatabase(this.context);
			database.open();
			callInformation = database.getTotalCallInforByNumber(type, number);
			database.close();
		} else if (type == CommonConstraints.INFO_TYPE_WEEK) {
			MyNetDatabase database = new MyNetDatabase(this.context);
			database.open();
			callInformation = database.getTotalCallInforByNumber(type, number);
			database.close();
		} else if (type == CommonConstraints.INFO_TYPE_MONTH) {
			MyNetDatabase database = new MyNetDatabase(this.context);
			database.open();
			callInformation = database.getTotalCallInforByNumber(type, number);
			database.close();
		}

		if (callInformation != null && callInformation.size() > 0) {
			for (int i = 0; i < callInformation.size(); i++) {
				if (callInformation.get(i).CallType == CallLog.Calls.INCOMING_TYPE) {
					incommingCallDuaration += callInformation.get(i).DurationInSec;
					totalCallCount = totalCallCount
							+ callInformation.get(i).DurationInSec;
					incommingCallCount = callInformation.get(i).CallCount;
				} else if (callInformation.get(i).CallType == CallLog.Calls.OUTGOING_TYPE) {
					outgoingCallDuaration += callInformation.get(i).DurationInSec;
					totalCallCount = totalCallCount
							+ callInformation.get(i).DurationInSec;
					outgoingCallCount = callInformation.get(i).CallCount;
				} else if (callInformation.get(i).CallType == CallLog.Calls.MISSED_TYPE) {
					missedCallCount = callInformation.get(i).CallCount;
				}
			}
		}

		int hour = 0;
		int min = 0;
		int sec = 0;
		String durationText = "";

		sec = outgoingCallDuaration % 60;
		min = outgoingCallDuaration / 60;
		if (min > 59) {
			hour = min / 60;
			min = min % 60;
		}

		durationText = formatter.format(hour) + ":" + formatter.format(min)
				+ ":" + formatter.format(sec);

		outgoingCallTime.setText(durationText);
		outgoingProgress.setMax(totalCallCount);
		outgoingProgress.setProgress(outgoingCallDuaration);

		hour = 0;
		min = 0;
		sec = 0;
		durationText = "";
		sec = incommingCallDuaration % 60;
		min = incommingCallDuaration / 60;
		if (min > 59) {
			hour = min / 60;
			min = min % 60;
		}

		durationText = formatter.format(hour) + ":" + formatter.format(min)
				+ ":" + formatter.format(sec);
		incomingCallTime.setText(durationText);
		incomingProgress.setMax(totalCallCount);
		incomingProgress.setProgress(incommingCallDuaration);
		
		if(incommingCallCount > 0 || outgoingCallCount>0 || missedCallCount>0){
			StatisticsSocailChartOverview.setVisibility(View.VISIBLE);
			tvChatRecord.setVisibility(View.GONE);
			new AsyncTask<Void, Void, Object>() {
				String urlRqs3DPie;
				@Override
				protected Object doInBackground(Void... params) {
					try {
						urlRqs3DPie = String.format(
								CommonURL.getInstance().GoogleChartTotalCalls,
								URLEncoder.encode("Outgoing",
										CommonConstraints.EncodingCode), URLEncoder
										.encode("Incoming",
												CommonConstraints.EncodingCode),
								URLEncoder.encode("Missed",
										CommonConstraints.EncodingCode),
								incommingCallCount, outgoingCallCount,
								missedCallCount, incommingCallCount,
								outgoingCallCount, missedCallCount);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return JSONfunctions.LoadChart(urlRqs3DPie);
				}

				protected void onPostExecute(Object data) {
					
					StatisticsSocailChartOverview.setImageBitmap((Bitmap) data);
				}
			}.execute();
		}else{
			StatisticsSocailChartOverview.setVisibility(View.GONE);
			tvChatRecord.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvExperinceFilterAll) {
				arrengeBasicTab();
		} else if (v.getId() == R.id.tvExperinceFilterToday) {
				arrengeToadayTab();			
		} else if (v.getId() == R.id.tvExperinceFilterThisWeek) {
				arrengeThisWeekTab();			
		} else if (v.getId() == R.id.tvExperinceFilterThisMonth) {
				arrengeThisMonthTab();		
		}else if (v.getId() == R.id.ivStatisticsSocialMenu_one) {
			if(Number != null){
				if(callInformation.size()>0)
					showNumberDetails(callInformation.get(0).Number);
			}
			
		} else if (v.getId() == R.id.ivStatisticsSocialMenu_second) {
			if(isCallFromChart)
				callHistory();
			else
				mapImageShapShot();
		}else if(v.getId() == R.id.tvStatisticsSocialChart){
			svDetails.setVisibility(ScrollView.VISIBLE);
			svMap.setVisibility(ScrollView.GONE);
			
			tvStatisticsSocialChart.setBackgroundColor(this.context.getResources().getColor(
					R.color.header_text));
			tvStatisticsSocialMap.setBackgroundColor(this.context.getResources().getColor(
					R.color.value_text));
			isCallFromChart = true;
			isCallFromMap = false;
		}else if(v.getId() == R.id.tvStatisticsSocialMap){
			svDetails.setVisibility(ScrollView.GONE);
			svMap.setVisibility(ScrollView.VISIBLE);
			tvStatisticsSocialChart.setBackgroundColor(this.context.getResources().getColor(
					R.color.value_text));
			tvStatisticsSocialMap.setBackgroundColor(this.context.getResources().getColor(
					R.color.header_text));
			isCallFromMap = true;
			isCallFromChart = false;
			if(Number != null)
				LoadMap(CommonConstraints.INFO_TYPE_ALL,
					Number);
		}
	}

	private void LoadMap(int Type, String number) {
		boolean IsNeedtoCallSelf=true;
		MyNetDatabase database = new MyNetDatabase(context);
		database.open();
		ArrayList<UserLocationActivityInformation> userLocationActivityInformationList = database.getTotalCallAndSMSInforByNumber(Type, number);
		database.close();
		String addressText = "";
		double defaultLatitude = 0, defaultLongitude = 0;
		LocationManager locationManager = (LocationManager) ((MakeCallActivity)context).getSystemService(context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc != null) {
			
			for (int rowIndex = 0; rowIndex < userLocationActivityInformationList.size(); rowIndex++) {
				try {
					if (userLocationActivityInformationList.get(rowIndex).Latitude > 0
							&& userLocationActivityInformationList.get(rowIndex).Longitude > 0) {
						IsNeedtoCallSelf=false;
						LatLng Location = new LatLng(userLocationActivityInformationList.get(rowIndex).Latitude,
								userLocationActivityInformationList.get(rowIndex).Longitude);
						defaultLatitude = userLocationActivityInformationList.get(rowIndex).Latitude;
						defaultLongitude = userLocationActivityInformationList.get(rowIndex).Longitude;
						double latitude = defaultLatitude, longitude = defaultLongitude;
						Geocoder geocoder = new Geocoder(context, Locale.getDefault());
						List<Address> addresses = geocoder.getFromLocation(
								userLocationActivityInformationList.get(rowIndex).Latitude , 
								userLocationActivityInformationList.get(rowIndex).Longitude, 1);
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
							if(userLocationActivityInformationList.get(rowIndex).ActivityTypeID == 1){
								//call
								Bitmap customPin = BitmapFactory.decodeResource(context.getResources(),
										R.drawable.google_custom_pic_call);
								Bitmap bmp = Bitmap.createBitmap(customPin.getWidth(),
										customPin.getHeight(), customPin.getConfig());
								Canvas canvas = new Canvas(bmp);
								canvas.drawBitmap(customPin, new Matrix(), null);
								map.addMarker(new MarkerOptions().position(
										Location).title(addressText)
										.icon(BitmapDescriptorFactory.fromBitmap(bmp)));
							}else if(userLocationActivityInformationList.get(rowIndex).ActivityTypeID == 2){
								//sms
								Bitmap customPin = BitmapFactory.decodeResource(context.getResources(),
										R.drawable.google_custom_pic_sms);
								Bitmap bmp = Bitmap.createBitmap(customPin.getWidth(),
										customPin.getHeight(), customPin.getConfig());
								Canvas canvas = new Canvas(bmp);
								canvas.drawBitmap(customPin, new Matrix(), null);
								
								map.addMarker(new MarkerOptions().position(
										Location).title(addressText)
										.icon(BitmapDescriptorFactory.fromBitmap(bmp)));
							}
							latitude = userLocationActivityInformationList.get(rowIndex).Latitude;
							longitude = userLocationActivityInformationList.get(rowIndex).Latitude;
							Location = new LatLng(latitude, longitude);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng Defaultlocation = new LatLng(defaultLatitude,
				defaultLongitude);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation,
				14.0f));

		map.animateCamera(CameraUpdateFactory.zoomIn());

		map.animateCamera(CameraUpdateFactory.zoomTo(10));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(Defaultlocation).zoom(15).bearing(90).tilt(30)
				.build();

		map.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		
		if(IsNeedtoCallSelf)
		{
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
			
			initializeMap();
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
						 context.getResources(), R.drawable.user_icon);
				}

				defaultBitmap = BitmapFactory.decodeResource(context.getResources(),
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


	private void mapImageShapShot() {
		GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap shapshot) {
				try {
					llMap.setDrawingCacheEnabled(true);
					Bitmap backBitmap = llMap.getDrawingCache();
					Bitmap bmOverlay = Bitmap.createBitmap(
							backBitmap.getWidth(), backBitmap.getHeight(),
							backBitmap.getConfig());
					Canvas canvas = new Canvas(bmOverlay);
					canvas.drawBitmap(shapshot, new Matrix(), null);
					canvas.drawBitmap(backBitmap, 0, 0, null);

					String external_path = Environment
							.getExternalStorageDirectory().getPath()
							+ "/MyNet/";
					String filePath = String
							.format(CommonValues.getInstance().LoginUser.UserNumber
									+ ".jpg");
					File cduFileDir = new File(external_path);
					if (!cduFileDir.exists())
						cduFileDir.mkdir();
					File pictureFile = new File(cduFileDir, filePath);
					ExperinceLiveActivity.filename = pictureFile.getName();

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					ExperinceLiveActivity.selectedFile = stream.toByteArray();
					LoadInformation();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		map.snapshot(snapshotReadyCallback);
	}

	private void callHistory() {
		try{
			//tvStatisticsHeaderDetailsTitle.setVisibility(TextView.VISIBLE);
			tvStatisticsHeaderDetailsTitle.setText(headertext.getText());
			Thread.sleep(1000);
			Bitmap bmOverlay = null;
			rlchart.setDrawingCacheEnabled(true);
			bmOverlay = rlchart.getDrawingCache();
			//llMap.setDrawingCacheEnabled(true);			
			//bmOverlay = llMap.getDrawingCache();
			String external_path = Environment
					.getExternalStorageDirectory().getPath()
					+ "/MyNet/";
			String filePath = String.format(CommonValues.getInstance().LoginUser.UserNumber+".jpg");
			File cduFileDir = new File(external_path);
			if (!cduFileDir.exists())
				cduFileDir.mkdir();
			File pictureFile = new File(cduFileDir, filePath);
			ExperinceLiveActivity.filename = pictureFile.getName();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			ExperinceLiveActivity.selectedFile = stream.toByteArray();
			LoadInformation();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showNumberDetails(final String Callernumber) {
		Dialog numberDetailsdialog = new Dialog(this.context);
		numberDetailsdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		numberDetailsdialog.setCancelable(true);
		numberDetailsdialog.setContentView(R.layout.statistics_social_call_details);
		numberDetailsdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		TextView rlHeader = (TextView) numberDetailsdialog.findViewById(R.id.tvStatisticsDetailsTitle);
		TextView number = (TextView) numberDetailsdialog
				.findViewById(R.id.tvStatisticsSocialMobileNumber);
		TextView date = (TextView) numberDetailsdialog
				.findViewById(R.id.tvStatisticsSocialMobileNumberDate);
		TextView tvCallDemoDialer = (TextView) numberDetailsdialog
				.findViewById(R.id.tvCallDemoDialer);
		
		ivStatisticsSocialPerson= (ImageView) numberDetailsdialog
				.findViewById(R.id.ivStatisticsSocialPerson);
		
		MyNetDatabase database = new MyNetDatabase(context);
		database.open();
		ArrayList<PhoneCallInformation> callInformationDetails = database.getCallInformation(Callernumber);
		database.close();
		
		String name = CommonTask.getContentName(this.context,
				callInformationDetails.get(0).Number);
		if (name != "") {
			rlHeader.setText("Call Details between me and "+name);
			number.setText(name);
		} else {
			rlHeader.setText("Call Details between me and "+callInformationDetails.get(0).Number);
			number.setText(callInformationDetails.get(0).Number);
		}
		
		int phoneId=CommonTask.getContentPhotoId(context, callInformationDetails.get(0).Number);
		if(phoneId>0){
			ivStatisticsSocialPerson.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, phoneId));
		}else{
			ivStatisticsSocialPerson.setImageDrawable(context.getResources().getDrawable(R.drawable.user_icon));
		}
		
		MyNetDatabase db = new MyNetDatabase(context);
		db.open();
		PhoneCallInformation phoneCallInformation = db.getLatestPhoneCallInfo(Number);
		db.close();
		//lastCallDuration = ((PhoneCallInformation) object).CallTime.getTime();
		String dateTime = (String) DateUtils.getRelativeTimeSpanString(
				phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);/*
		tvtime.setText(dateTime);
		
		String dateTime = (String) DateUtils.getRelativeTimeSpanString(
				callInformationDetails
				.get(0).CallTime.getTime(), new Date().getTime(), 0);*/
		date.setText(dateTime);

		ListView lvStatisticsCallDetails = (ListView) numberDetailsdialog
				.findViewById(R.id.lvStatisticsCallDetails);

		AllSMSAdapter adapter = new AllSMSAdapter(this.context,
				R.layout.all_sms_item_layout, callInformationDetails);
		lvStatisticsCallDetails.setAdapter(adapter);
		
		tvCallDemoDialer.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent calltomanager = new Intent(Intent.ACTION_CALL);
				calltomanager.setData(Uri.parse("tel:" + Callernumber));
				context.startActivity(calltomanager);
			}
		});
		
		numberDetailsdialog.show();
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Processing...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.SetFBPostPicture(
				ExperinceLiveActivity.selectedFile,
				CommonValues.getInstance().LoginUser.UserNumber);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			tvStatisticsHeaderDetailsTitle.setText("");
			String path = "";
			path += CommonURL.getInstance().getCareImageServer
					+ String.valueOf(data);
			ExperinceLiveActivity.filename = "";
			ExperinceLiveActivity.selectedFile = null;

			Intent intent = new Intent(context, FB_ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("PIC_LINK", path);
			intent.putExtra("DESCRIPTION", "Call History");
			context.startActivity(intent);
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

}
