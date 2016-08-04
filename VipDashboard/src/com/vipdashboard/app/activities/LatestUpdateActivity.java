package com.vipdashboard.app.activities;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.LiveAlarm;
import com.vipdashboard.app.entities.LiveAlarms;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveAlarmManager;
import com.vipdashboard.app.manager.LiveAlarmManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class LatestUpdateActivity extends Activity implements
		IAsynchronousTask, LocationListener {
	private GoogleMap map;
	private LatLng Location;	
	DownloadableAsyncTask downloadAsync;
	TextView tvLatestUpdateText,tvLatestUpdateUserTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latestupdate);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
		tvLatestUpdateText=(TextView)findViewById(R.id.tvLatestUpdateText);
		tvLatestUpdateUserTitle=(TextView)findViewById(R.id.tvLatestUpdateUserTitle);
		initializeControl();

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
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);	
	}

	private void initializeControl() {

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.maplatestupdate)).getMap();

		getLetestUpdateData();
	}

	private void getLetestUpdateData() {
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}

	public void ShowData() {
		

	}

	@Override
	public void showProgressLoader() {

	}

	@Override
	public void hideProgressLoader() {

	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveAlarmManager alarmManager = new LiveAlarmManager();
		return alarmManager.GetLatestUpdate();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			LiveAlarms liveAlarms = (LiveAlarms) data;
			if(liveAlarms!=null && liveAlarms.liveAlarmList.size()>0){
				LiveAlarm liveAlarm; 
				double defaultLatitude=0,defaultLongitude=0,latitude=0,longitude=0;				
				Geocoder geocoder = new Geocoder(this, Locale.getDefault());
				List<Address> addresses;
				String addressText="";				
				for(int i=0;i<liveAlarms.liveAlarmList.size()-1;i++){
					liveAlarm=liveAlarms.liveAlarmList.get(i);
					if(liveAlarm.SiteInformation!=null){
						try {
							latitude=liveAlarm.SiteInformation.Latitude;
							longitude=liveAlarm.SiteInformation.Longitude;
							addresses = geocoder.getFromLocation(latitude, longitude, 1);							
							if (addresses != null && addresses.size() > 0) {			                
				                Address address = addresses.get(0);			                
				                for (int  lineIndex=0 ;lineIndex<address.getMaxAddressLineIndex();lineIndex++) {
				                	addressText=addressText+address.getAddressLine(lineIndex)+", ";
								}
				                addressText=addressText+address.getLocality()+", "+address.getCountryName();			               
				            }
							Location = new LatLng(latitude, longitude);	
							map.addMarker(new MarkerOptions().position(Location).title(	addressText));
							if(i==0){
								defaultLatitude=latitude;
								defaultLongitude=longitude;								
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
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
				LiveAlarm lastAlarm= liveAlarms.liveAlarmList.get(liveAlarms.liveAlarmList.size()-1);
				tvLatestUpdateText.setText(lastAlarm.AlarmText);
				
				try {
					tvLatestUpdateUserTitle.setText("Welcome "+CommonValues.getInstance().LoginUser.FirstName+", Latest update at "+ CommonTask.convertJsonDateToMessageTime(lastAlarm.EventTime) );
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				
				/*if(i==0){
					defaultLatitude=liveAlarm.SiteInformation.Latitude;
					defaultLongitude=liveAlarm.SiteInformation.Longitude;
				}
				//LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
				//android.location.Location loc= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				
				if(loc!=null){
					Location = new LatLng(loc.getLatitude(), loc.getLongitude());
					defaultLatitude=Location.latitude;
					defaultLongitude=Location.longitude;					
					Geocoder geocoder =
			                new Geocoder(this, Locale.getDefault());
					String addressText="";
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
				
				LiveAlarm lastAlarm= liveAlarms.liveAlarmList.get(liveAlarms.liveAlarmList.size()-1);
				
				//tvLatestUpdateText.setText(lastAlarm.AlarmText);
				 */
				
				
			}
		}

	}

	@Override
	public void onLocationChanged(android.location.Location arg0) {
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
