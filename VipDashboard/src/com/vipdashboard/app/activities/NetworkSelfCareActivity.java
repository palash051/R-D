package com.vipdashboard.app.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.DailyKPI;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class NetworkSelfCareActivity extends Activity implements OnClickListener, LocationListener {
	
	RelativeLayout rlsalfcare_product, rlsalfcare_Account, rlsalfcare_SelfService, rlsalfcare_AboutUs;
	RelativeLayout ralsalfcare_MyExperience, rlsalfcare_MyProfile, rlsalfcare_MyApp, rlsalfcare_Offers;
	TextView tvSelfcareOwnlocationAddress;
	private GoogleMap map;
	private LatLng Location;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfcare_homepage);
		initialization();
		initializeMap();
		showGraph();
		
		if(CommonValues.getInstance().LoginUser!=null)
			((TextView)findViewById(R.id.tvSelfcareUserTitle)).setText("Welcome: " + CommonValues.getInstance().LoginUser.FirstName);
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);	
	}
	
	private void showGraph(){		
		GraphViewData[] total_Data = new GraphViewData[10];
		total_Data[0] = new GraphViewData(0,80 );
		total_Data[1] = new GraphViewData(1,75 );
		total_Data[2] = new GraphViewData(2,70 );
		total_Data[3] = new GraphViewData(3,90 );
		total_Data[4] = new GraphViewData(4,95 );
		total_Data[5] = new GraphViewData(5,92 );
		total_Data[6] = new GraphViewData(6,60 );
		total_Data[7] = new GraphViewData(7,56 );
		total_Data[8] = new GraphViewData(8,97 );
		total_Data[9] = new GraphViewData(9,78 );		
		
		GraphViewSeries totalData = new GraphViewSeries("RA",new GraphViewSeriesStyle(Color.GREEN, 3), total_Data);
		//GraphViewSeries ericssionData = new GraphViewSeries("Ericssion", new GraphViewSeriesStyle(Color.BLUE, 3), total_Ericssion);
		//GraphViewSeries huwaeiData = new GraphViewSeries("huwaei", new GraphViewSeriesStyle(Color.RED, 3), total_Huwaei);
		LineGraphView lineGraphView = new LineGraphView(this, "Signal Strenght");
		String[] horLevels = new String[1]; 
		horLevels[0]="Last One Hour";
		lineGraphView.setHorizontalLabels(horLevels);
		lineGraphView.addSeries(totalData);
		lineGraphView.getGraphViewStyle().setGridColor(Color.BLUE);
		lineGraphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
		lineGraphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
		lineGraphView.getGraphViewStyle().setTextSize(30);
		lineGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
		//lineGraphView.setDrawDataPoints(true);
		//lineGraphView.setDataPointsRadius(10f);
		lineGraphView.setLegendAlign(LegendAlign.MIDDLE);
		//lineGraphView.setDrawDataPoints(true);
		//lineGraphView.setDataPointsRadius(6f);
		lineGraphView.setDrawBackground(true);
		lineGraphView.setViewPort(0, 7);
		lineGraphView.setManualYAxisBounds(100, 0);
		lineGraphView.setScalable(true);
		
		LinearLayout llSelfcare_Signal_Strenght=(LinearLayout)findViewById(R.id.llSelfcare_Signal_Strenght);
		llSelfcare_Signal_Strenght.addView(lineGraphView);
	}


	private void initialization() {
		rlsalfcare_product = (RelativeLayout) findViewById(R.id.rlSelfcare_Product);
		rlsalfcare_Account = (RelativeLayout) findViewById(R.id.rlSelfcare_Account);
		rlsalfcare_SelfService = (RelativeLayout) findViewById(R.id.rlSelfcare_SelfService);
		rlsalfcare_AboutUs = (RelativeLayout) findViewById(R.id.rlSelfcare_AboutUs);
		ralsalfcare_MyExperience = (RelativeLayout) findViewById(R.id.rlMyExperience);
		rlsalfcare_MyProfile = (RelativeLayout) findViewById(R.id.rlSelfcare_Myprofile);
		rlsalfcare_MyApp = (RelativeLayout) findViewById(R.id.rlSelfcare_Myapps);
		rlsalfcare_Offers = (RelativeLayout) findViewById(R.id.rlSelfcare_Myoffers);
		
		tvSelfcareOwnlocationAddress= (TextView) findViewById(R.id.tvSelfcareOwnlocationAddress);
		
		rlsalfcare_product.setOnClickListener(this);
		rlsalfcare_Account.setOnClickListener(this);
		rlsalfcare_SelfService.setOnClickListener(this);
		rlsalfcare_AboutUs.setOnClickListener(this);
		ralsalfcare_MyExperience.setOnClickListener(this);
		rlsalfcare_MyProfile.setOnClickListener(this);
		rlsalfcare_MyApp.setOnClickListener(this);
		rlsalfcare_Offers.setOnClickListener(this);
	}


	private void initializeMap() {
		
		String addressText="";
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapSelfcareUserLocation)).getMap();
		double defaultLatitude=0,defaultLongitude=0;
		
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		if(loc!=null){
			Location = new LatLng(loc.getLatitude(), loc.getLongitude());
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
				
				map.addMarker(new MarkerOptions().position(Location).title(	addressText));
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
		
		tvSelfcareOwnlocationAddress.setText(addressText);
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
		
	}

	


	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.rlMyExperience){
			Intent intent = new Intent(this,NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlSelfcare_Product){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlSelfcare_Account){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlSelfcare_SelfService){
			Intent intent = new Intent(this,SelfcareSelfServiceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlSelfcare_AboutUs){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlSelfcare_Myprofile){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlSelfcare_Myapps){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlSelfcare_Myoffers){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
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
