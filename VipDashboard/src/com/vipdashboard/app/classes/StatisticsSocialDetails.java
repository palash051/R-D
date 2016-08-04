package com.vipdashboard.app.classes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.ExperinceLiveActivity;
import com.vipdashboard.app.activities.FB_ShareActivity;
import com.vipdashboard.app.activities.StatisticsActivity;
import com.vipdashboard.app.adapter.AllSMSAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.PhoneCallInformation;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class StatisticsSocialDetails implements OnClickListener, LocationListener, IAsynchronousTask {

	private Context context;
	int incommingCallCount;
	int outgoingCallCount;
	int missedCallCount;
	int totalCallCount;
	int dropCallCount;
	int incommingCallDuaration;
	int outgoingCallDuaration;
	int totalCallDuaration;
	NumberFormat formatter = new DecimalFormat("00");
	TextView tvAll, tvToday, tvThisWeek, tvThisMonth, outgoingCallTime,
			incomingCallTime;
	ProgressBar outgoingProgress, incomingProgress;
	ArrayList<PhoneCallInformation> callInformation;
	ImageView StatisticsSocailChartOverview, callDetails, postDetails,ivStatisticsSocialPerson;
	Dialog dialog;
	PhoneCallInformation phoneCallInformation;
	RelativeLayout rlchart;
	TextView headertext;
	TextView tvStatisticsSocialChart,tvStatisticsSocialMap,tvChatRecord;
	ScrollView svDetails, svMap;
	TextView tvName;
	TextView date;
	GoogleMap map;
	LinearLayout llMap;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	boolean isCallFromChart = true, isCallFromMap = false;
	
	public StatisticsSocialDetails(Context _context) {
		this.context = _context;
	}

	public void showDetailsInformation(
			PhoneCallInformation _phoneCallInformation) {
		
		if(dialog == null){
			dialog = new Dialog(this.context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(true);
			dialog.setContentView(R.layout.statistics_social_list_info);
			
			headertext = (TextView) dialog.findViewById(R.id.tvStatisticsDetailsTitle);
			
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
			map = ((SupportMapFragment) ((StatisticsActivity)context).getSupportFragmentManager().findFragmentById(
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
		phoneCallInformation = _phoneCallInformation;
		String name = CommonTask.getContentName(this.context,
				phoneCallInformation.Number);
		if (name != "") {
			headertext.setText("Call Details between me and "+name);
			tvName.setText(name);
		} else {
			headertext.setText("Call Details between me and "+phoneCallInformation.Number);
			tvName.setText(phoneCallInformation.Number);
		}
		
		int photoId=CommonTask.getContentPhotoId(context, phoneCallInformation.Number);
		if(photoId>0){
			ivStatisticsSocialPerson.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, photoId));
		}else{
			ivStatisticsSocialPerson.setImageDrawable(context.getResources().getDrawable(R.drawable.user_icon));
		}
		
		String dateTime = (String) DateUtils.getRelativeTimeSpanString(
				phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);
		date.setText(dateTime);

		arrengeBasicTab();
		dialog.show();
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

	private void arrengeThisMonthTab() {
		tvAll.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvToday.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisWeek.setBackgroundColor(this.context.getResources().getColor(
				R.color.value_text));
		tvThisMonth.setBackgroundColor(this.context.getResources().getColor(
				R.color.header_text));
		showCallDetails(CommonConstraints.INFO_TYPE_MONTH,
				phoneCallInformation.Number);
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
		showCallDetails(CommonConstraints.INFO_TYPE_WEEK,
				phoneCallInformation.Number);
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
		showCallDetails(CommonConstraints.INFO_TYPE_TODAY,
				phoneCallInformation.Number);
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
		showCallDetails(CommonConstraints.INFO_TYPE_ALL,
				phoneCallInformation.Number);

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
		} else if (v.getId() == R.id.ivStatisticsSocialMenu_one) {
			if(callInformation.size()>0)
				showNumberDetails(callInformation.get(0).Number);
			
		} else if (v.getId() == R.id.ivStatisticsSocialMenu_second) {
			if(isCallFromChart)
				CallHistory();
			else
				mapImageShapShot();
				//CallHistory();
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
			LoadMap(phoneCallInformation);
		}
	}

	private void LoadMap(PhoneCallInformation callinformation) {
		
		MyNetDatabase database = new MyNetDatabase(context);
		database.open();
		ArrayList<PhoneCallInformation> callinforamtoins = database.getCallInformation(callinformation.Number);
		database.close();
		
		String addressText = "";
		double defaultLatitude = 0, defaultLongitude = 0;
		LocationManager locationManager = (LocationManager) ((StatisticsActivity)context).getSystemService(context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc != null) {
			LatLng Location = new LatLng(loc.getLatitude(),
					loc.getLongitude());
			defaultLatitude = Location.latitude;
			defaultLongitude = Location.longitude;
			double latitude = defaultLatitude, longitude = defaultLongitude;
			Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			for (int rowIndex = 0; rowIndex < callinforamtoins.size(); rowIndex++) {
				try {
					if (callinforamtoins.get(rowIndex).Latitude > 0
							&& callinforamtoins.get(rowIndex).Longitude > 0) {
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
							map.addMarker(new MarkerOptions().position(
									Location).title(addressText));
							latitude = callinforamtoins.get(rowIndex).Latitude;
							longitude = callinforamtoins.get(rowIndex).Latitude;
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

	private void showNumberDetails(
			String Callernumber) {
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
		
		String dateTime = (String) DateUtils.getRelativeTimeSpanString(
				callInformationDetails
				.get(0).CallTime.getTime(), new Date().getTime(), 0);
		date.setText(dateTime);

		ListView lvStatisticsCallDetails = (ListView) numberDetailsdialog
				.findViewById(R.id.lvStatisticsCallDetails);

		AllSMSAdapter adapter = new AllSMSAdapter(this.context,
				R.layout.all_sms_item_layout, callInformationDetails);
		lvStatisticsCallDetails.setAdapter(adapter);
		numberDetailsdialog.show();
	}

	protected void postCallDetails(final String number,
			final int incommingCallCount2, final int outgoingCallCount2,
			final int missedCallCount2) {
		Dialog postdialog = new Dialog(this.context);
		postdialog.setTitle("Details");
		postdialog.setCancelable(true);
		postdialog.setContentView(R.layout.statistics_social_call_detals_post);
		final ImageView postImage = (ImageView) postdialog
				.findViewById(R.id.ivStatisticsSocailChartOverview);
		Button postinfacebook = (Button) postdialog
				.findViewById(R.id.bStatisticsSocialFacebookPost);
		Button sendMMS = (Button) postdialog
				.findViewById(R.id.bStatisticsSocialSendMMS);
		Button sendEmail = (Button) postdialog
				.findViewById(R.id.bStatisticsSocialSendEmail);
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
							incommingCallCount2, outgoingCallCount2,
							missedCallCount2, incommingCallCount2,
							outgoingCallCount2, missedCallCount2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return JSONfunctions.LoadChart(urlRqs3DPie);
			}

			protected void onPostExecute(Object data) {
				postImage.setImageBitmap((Bitmap) data);
				saveImage((Bitmap) data);
			}
		}.execute();

		postinfacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// PostStatusInFacebook();
			}
		});
		sendMMS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.putExtra("sms_body",
						"Hi,\nCheck this summary of our phone conversation :)");
				i.putExtra("address", number);
				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(
						Environment.getExternalStorageDirectory()
								+ "/MyNet/CallDetails.jpg")));
				i.setType("image/jpeg");
				context.startActivity(Intent.createChooser(i, "Send"));
			}
		});
		sendEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"MyNet");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"Hi,\nCheck this summary of our phone conversation :)");
				emailIntent.putExtra(Intent.EXTRA_STREAM, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory()
								+ "/MyNet/CallDetails.jpg")));
				emailIntent.setType("plain/text");
				context.startActivity(Intent.createChooser(emailIntent, "Send"));
			}
		});
		dialog.show();
	}

	private void saveImage(Bitmap bitmap) {
		String external_path = Environment.getExternalStorageDirectory()
				.getPath() + "/MyNet/";
		String filePath = String.format("%s.jpg", "CallDetails");
		File cduFileDir = new File(external_path);
		if (!cduFileDir.exists())
			cduFileDir.mkdir();
		File pictureFile = new File(cduFileDir, filePath);
		if (pictureFile.exists())
			pictureFile.delete();
		try {
			FileOutputStream out = new FileOutputStream(pictureFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void CallHistory() {
		try{
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
		progressDialog.setMessage("Please Wait...");
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
			String path = "";
			path += CommonURL.getInstance().getImageServer
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
}
