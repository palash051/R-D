package com.vipdashboard.app.activities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.base.OnSwipeTouchListener;
import com.vipdashboard.app.classes.MyNetFacebookActivity;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.FacebookFriendses;
import com.vipdashboard.app.entities.FacebookPersons;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.manager.FacebookManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class NetworkSelfcareMyExperinceShowMoreActivity extends Activity
		implements LocationListener, OnItemClickListener, OnClickListener, IAsynchronousTask {
	/*TextView tvSelfcareMyExperinceFriendsNearBy,
			tvSelfcareMyExperinceMyBadExperiences,
			tvSelfcareMyExperinceCallHistory,
			tvSelfcareMyExperinceFamilyMembers,
			tvSelfcareMyExperinceSocialUpdatesMap,
			tvSelfcareMyExperinceChatCustomerCare;*/
	TextView tvShowMeMore,tvAddFriendsAndFamilyNmber, tvImportFriendsAndFamily;
	TextView tvUserExperinceExperience,tvUserExperinceAssistance,tvUserExperinceHistory;
	private GoogleMap map;
	LinearLayout socialMedia;
	GridView gridview;
	GaugeView gvDataSpeed, gvSignal, gvBTSDistance;
	ArrayList<Report> report;
	ReportAdapter adapter;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	private Bitmap profilePicture;
	TextView welcometext;
	int count = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfcare_myexperince_show_more);
		initialization();
		
		/*if (CommonValues.getInstance().LoginUser != null)
			((TextView) findViewById(R.id.tvSelfcareShomoreUserTitle))
					.setText("Welcome: "
							+ CommonValues.getInstance().LoginUser.FirstName);*/	
		report = new ArrayList<Report>();
		report.add(new Report("Experience", 1));
		report.add(new Report("Call History", 2));
		report.add(new Report("Nearby", 3));
		report.add(new Report("Net Doctor", 4));
		report.add(new Report("My Operator", 5));
		report.add(new Report("More", 6));
		
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout, report);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
	}
	
	@Override
	 protected void onPause() {
	  MyNetApplication.activityPaused();
	  super.onPause();
	 }

	 @Override
	 protected void onResume() {
	  MyNetApplication.activityResumed();
	  LoadInformation();
	  SetGaugeInitialValue();	
	  //initializeMap();
	  super.onResume();
	 }

	private void LoadInformation() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	private void SetGaugeInitialValue() {
		MyNetDatabase myNetDatabase = new MyNetDatabase(this);
		myNetDatabase.open();
		PhoneDataInformation phoneDataInformation = myNetDatabase.getAgvDownLoadUploadSpeed(0);
		myNetDatabase.close();
		if(phoneDataInformation!=null && phoneDataInformation.DownLoadSpeed>0){
			int currentSpeedInPar=(MyNetService.currentDownloadSpeedInKbPS*100)/phoneDataInformation.DownLoadSpeed;
			gvDataSpeed.setTargetValue(currentSpeedInPar);
		}else{
			gvDataSpeed.setTargetValue(0);
		}
		myNetDatabase.open();
		int signal = (int) (myNetDatabase.getAgvSignalStrenght() * 100) / 31;
		gvSignal.setTargetValue(signal);
		myNetDatabase.close();
		
		gvBTSDistance.setTargetValue(0);
	}

	private void initialization() {
		/*tvSelfcareMyExperinceFriendsNearBy = (TextView) findViewById(R.id.tvSelfcareMyExperinceFriendsNearBy);
		tvSelfcareMyExperinceMyBadExperiences = (TextView) findViewById(R.id.tvSelfcareMyExperinceMyBadExperiences);
		tvSelfcareMyExperinceCallHistory = (TextView) findViewById(R.id.tvSelfcareMyExperinceCallHistory);
		tvSelfcareMyExperinceFamilyMembers = (TextView) findViewById(R.id.tvSelfcareMyExperinceFamilyMembers);
		tvSelfcareMyExperinceSocialUpdatesMap = (TextView) findViewById(R.id.tvSelfcareMyExperinceSocialUpdatesMap);
		tvSelfcareMyExperinceChatCustomerCare = (TextView) findViewById(R.id.tvSelfcareMyExperinceChatCustomerCare);
		CommonTask.makeLinkedTextview(this, tvSelfcareMyExperinceFriendsNearBy, "Friends near-by");
		CommonTask.makeLinkedTextview(this, tvSelfcareMyExperinceMyBadExperiences, "My bad experiences");
		CommonTask.makeLinkedTextview(this, tvSelfcareMyExperinceCallHistory, "Call history");
		CommonTask.makeLinkedTextview(this, tvSelfcareMyExperinceFamilyMembers, "Where are my family members?");
		CommonTask.makeLinkedTextview(this, tvSelfcareMyExperinceSocialUpdatesMap, "Social updates in map");
		CommonTask.makeLinkedTextview(this, tvSelfcareMyExperinceChatCustomerCare, "Chat with Customer Care");
		tvSelfcareMyExperinceFriendsNearBy.setOnClickListener(this);
		tvSelfcareMyExperinceMyBadExperiences.setOnClickListener(this);
		tvSelfcareMyExperinceCallHistory.setOnClickListener(this);
		tvSelfcareMyExperinceFamilyMembers.setOnClickListener(this);
		tvSelfcareMyExperinceSocialUpdatesMap.setOnClickListener(this);
		tvSelfcareMyExperinceChatCustomerCare.setOnClickListener(this);*/
		
		//Tab
		welcometext = (TextView) findViewById(R.id.tvSelfcareShomoreUserTitle);
		
		tvUserExperinceExperience= (TextView) findViewById(R.id.tvUserExperinceExperience);
		tvUserExperinceAssistance= (TextView) findViewById(R.id.tvUserExperinceAssistance);
		tvUserExperinceHistory= (TextView) findViewById(R.id.tvUserExperinceHistory);
		
		tvUserExperinceExperience.setOnClickListener(this);
		tvUserExperinceAssistance.setOnClickListener(this);
		tvUserExperinceHistory.setOnClickListener(this);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapSelfcareMyExperince)).getMap();
		
		
		socialMedia = (LinearLayout) findViewById(R.id.llSocialMedia);
		progressBar = (ProgressBar) findViewById(R.id.pbSelfCareShowMeMore);
		gridview = (GridView) findViewById(R.id.gridMyExperience);
		gvDataSpeed = (GaugeView) findViewById(R.id.ivMyExperienceDataSpeed);
		gvSignal = (GaugeView) findViewById(R.id.ivMyExperienceSignalStrength);
		gvBTSDistance = (GaugeView) findViewById(R.id.ivMyExperienceBTSDistance);
		tvShowMeMore = (TextView) findViewById(R.id.tvShowMore);
		CommonTask.makeLinkedTextview(this, tvShowMeMore, "More");
		
		
		
		RelativeLayout rlmyexperiencemain=(RelativeLayout)findViewById(R.id.rlmyexperiencemain);
		
		rlmyexperiencemain.setOnTouchListener(new OnSwipeTouchListener(
				this) {		
			public void onSwipeLeft() {
				Intent intent = new Intent(NetworkSelfcareMyExperinceShowMoreActivity.this,StatisticsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});
		
		
	}

	private void initializeMap() {
		count++;
		if(count == 1){
			String addressText="";		
			Bitmap defaultBitmap, defaultBitmapUser;
			double defaultLatitude=0,defaultLongitude=0;
			
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			android.location.Location loc= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			
			if(loc!=null){
				LatLng Location = new LatLng(loc.getLatitude(), loc.getLongitude());
				defaultLatitude=Location.latitude;
				defaultLongitude=Location.longitude;					
				Geocoder geocoder =
		                new Geocoder(this, Locale.getDefault());
				
				try {
					List<Address> addresses = geocoder.getFromLocation(defaultLatitude, defaultLongitude, 1);
					if (addresses != null && addresses.size() > 0) {			                
		                Address address = addresses.get(0);			                
		                for (int  lineIndex=0 ;lineIndex<address.getMaxAddressLineIndex();lineIndex++) {
		                	addressText=addressText+address.getAddressLine(lineIndex)+", ";
						}
		                addressText=addressText+address.getLocality()+", "+address.getCountryName();			               
		            }
					
					if(profilePicture != null){
						defaultBitmapUser = profilePicture;
					}else{
						defaultBitmapUser = BitmapFactory.decodeResource(getResources(),
								R.drawable.user_icon);
					}
					defaultBitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.google_custom_pin);
					
					defaultBitmapUser = Bitmap.createScaledBitmap(defaultBitmapUser, defaultBitmap.getWidth() - 12,
							defaultBitmap.getHeight() - 40, true);
					Bitmap bmp = Bitmap.createBitmap(defaultBitmap.getWidth(), defaultBitmap.getHeight(),
							defaultBitmap.getConfig());
					Canvas canvas = new Canvas(bmp);
					canvas.drawBitmap(defaultBitmap, new Matrix(), null);
					canvas.drawBitmap(defaultBitmapUser, 8, 10, null);
					map.addMarker(new MarkerOptions().position(Location).title(	addressText)
							.icon(BitmapDescriptorFactory.fromBitmap(bmp)));
				}catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			
			LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation, 14.0f));

			map.animateCamera(CameraUpdateFactory.zoomIn());

			map.animateCamera(CameraUpdateFactory.zoomTo(10));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(Defaultlocation).zoom(17).bearing(90).tilt(30).build();

			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}

	/*@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvSelfcareMyExperinceFriendsNearBy) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if (v.getId() == R.id.tvSelfcareMyExperinceMyBadExperiences) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if (v.getId() == R.id.tvSelfcareMyExperinceCallHistory) {
			Intent intent = new Intent(this,
					NetworkSelfCareCallHistryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (v.getId() == R.id.tvSelfcareMyExperinceFamilyMembers) {
			Intent intent = new Intent(this,
					NetworkSelfCareFamilycallActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (v.getId() == R.id.tvSelfcareMyExperinceSocialUpdatesMap) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if (v.getId() == R.id.tvSelfcareMyExperinceChatCustomerCare) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}

	}*/

	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		int id = Integer.parseInt(String.valueOf(view.getTag()));
		if(id == 1){
			Intent intent = new Intent(this,NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == 2){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == 3){
			Intent intent = new Intent(this,
					DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == 4){
			Intent intent = new Intent(this,
					AssistanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == 5){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == 6){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.tvUserExperinceExperience){
			Intent intent = new Intent(this,NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(v.getId()==R.id.tvUserExperinceAssistance){
			Intent intent = new Intent(this,StatisticsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(v.getId()==R.id.tvUserExperinceHistory){
			Intent intent = new Intent(this,StatisticsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}

	@Override
	public void showProgressLoader() {
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IFacebookManager facebookManager = new FacebookManager();
		FacebookPersons facebookPersons = null;
		facebookPersons = facebookManager.GetFacebookProfilePicture();
		if(facebookPersons != null){
			try{
				URL url = new URL(facebookPersons.facebokPerson.PP_Path);
				profilePicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				welcometext.setText(facebookPersons.facebokPerson.Name);
			}catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return facebookPersons;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			initializeMap();
		}else{
			initializeMap();
		}
	}
}
