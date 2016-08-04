package com.khareeflive.app.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.khareeflive.app.asynchronoustask.SiteDownTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.ArrayOfSiteDownGoogleMap;
import com.khareeflive.app.manager.LatestUpdateManager;
import com.khareeflive.app.utils.CommonConstraints;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SiteDownActivity extends Activity {

	private TextView currenttime, currentnews;
	private SiteDownTask sitedown;
	private String msg, currenttimetext;
	private GoogleMap map;
	private ArrayOfSiteDownGoogleMap googlemapcollection;
	private LatLng Location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sitedown);
		initializeControl();

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

	private void initializeControl() {
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapsitedown)).getMap();
		/*
		 * Date d = new Date(); SimpleDateFormat sdf = new
		 * SimpleDateFormat("hh:mm aa"); String timeformat =sdf.format(d);
		 * currenttime.setText(timeformat);
		 */

		if (sitedown != null) {
			sitedown.cancel(true);
		}
		sitedown = new SiteDownTask(this);
		sitedown.execute();
	}

	public boolean GetDate() {
		try {
			LatestUpdateManager lstupdatenews = new LatestUpdateManager();

			googlemapcollection = new ArrayOfSiteDownGoogleMap();
			googlemapcollection = lstupdatenews.GetGoogleMapData();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public void showData() {
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
	}
}
