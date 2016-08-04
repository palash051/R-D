package com.khareeflive.app.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.LatestUpdateTask;
import com.khareeflive.app.asynchronoustask.LoginAuthenticationTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.base.KhareefLiveService;
import com.khareeflive.app.entities.ArrayOfSiteDownGoogleMap;
import com.khareeflive.app.entities.LatestUpdate;
import com.khareeflive.app.entities.SiteDownGoogleMap;
import com.khareeflive.app.manager.LatestUpdateManager;
import com.khareeflive.app.utils.CommonConstraints;
import com.khareeflive.app.utils.CommonValues;

public class LatestUpdateActivity extends Activity {

	private LatLng Location;

	private TextView currenttime, currentnews;
	private String msg, currenttimetext;
	LatestUpdateTask lstupdatetask;
	ProgressBar pbLatestUpdate;
	private GoogleMap map;
	private ArrayOfSiteDownGoogleMap googlemapcollection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latestupdate);
		initializeControl();

	}

	private void initializeControl() {

		currenttime = (TextView) findViewById(R.id.edcurrenttime);
		currentnews = (TextView) findViewById(R.id.ednews);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		pbLatestUpdate = (ProgressBar) findViewById(R.id.pblatestupdate);

		if (lstupdatetask != null) {
			lstupdatetask.cancel(true);
		}
		lstupdatetask = new LatestUpdateTask(this);
		lstupdatetask.execute();
	}

	public boolean GetDate() {
		try {
			LatestUpdateManager lstupdatenews = new LatestUpdateManager();
			msg = lstupdatenews.GetLatestNews().Msg;
			currenttimetext = lstupdatenews.GetLatestNews().UploadDate;

			googlemapcollection = new ArrayOfSiteDownGoogleMap();
			googlemapcollection = lstupdatenews.GetGoogleMapData();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public void showData() {
		currentnews.setText(msg);
		currenttime.setText(currenttimetext);

		if (googlemapcollection != null
				&& googlemapcollection.siteDownGoogleMap.size() > 0) {
			for (int i = 0; i < googlemapcollection.siteDownGoogleMap.size(); i++) {
				Location = new LatLng(
						googlemapcollection.siteDownGoogleMap.get(i).Lat,
						googlemapcollection.siteDownGoogleMap.get(i).Lang);
				map.addMarker(new MarkerOptions().position(Location).title(
						googlemapcollection.siteDownGoogleMap.get(i).Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapcircle)));

			}

			LatLng Defaultlocation = new LatLng(
					googlemapcollection.siteDownGoogleMap.get(0).Lat,
					googlemapcollection.siteDownGoogleMap.get(0).Lang);
			
/*			CameraUpdate update = CameraUpdateFactory
					.newLatLng(Defaultlocation);
			map.animateCamera(update);*/
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation, 15));

			map.animateCamera(CameraUpdateFactory.zoomIn());

			map.animateCamera(CameraUpdateFactory.zoomTo(10));

			CameraPosition cameraPosition = new CameraPosition.Builder()
			    .target(Defaultlocation)     
			    .zoom(10)                   
			    .bearing(90)                
			    .tilt(30)                   
			    .build();                  
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			
		}
		else
		{
			LatLng Defaultsalalahlocation = new LatLng(
					CommonConstraints.salalah_Lat,
					CommonConstraints.salalah_Lang);
			map.addMarker(new MarkerOptions().position(Defaultsalalahlocation)
					.title("Salalah").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapcircle)));
/*			CameraUpdate salalahupdate = CameraUpdateFactory
					.newLatLng(Defaultsalalahlocation);
			map.animateCamera(salalahupdate);*/
			
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultsalalahlocation, 15));

			map.animateCamera(CameraUpdateFactory.zoomIn());

			map.animateCamera(CameraUpdateFactory.zoomTo(10));

			CameraPosition cameraPosition = new CameraPosition.Builder()
			    .target(Defaultsalalahlocation)     
			    .zoom(12)                   
			    .bearing(90)                
			    .tilt(30)                   
			    .build();                  
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		CommonValues.getInstance().lastWarMessageTime=sdf.format(date);
	}

	public void showLoader() {
		pbLatestUpdate.setVisibility(View.VISIBLE);
	}

	public void hideLoader() {
		pbLatestUpdate.setVisibility(View.GONE);
	}
	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		stopService(new Intent(this, KhareefLiveService.class));
		super.onDestroy();
	}
}
