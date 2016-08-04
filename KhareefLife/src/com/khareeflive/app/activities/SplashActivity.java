package com.khareeflive.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
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
import com.khareeflive.app.asynchronoustask.SplashTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.base.KhareefLiveService;
import com.khareeflive.app.entities.ArrayOfSiteDownGoogleMap;
import com.khareeflive.app.manager.LatestUpdateManager;
import com.khareeflive.app.utils.CommonConstraints;

public class SplashActivity extends Activity implements OnGestureListener {

	private LatLng Location;

	private TextView currenttime, currentnews,swipetext;
	private String msg, currenttimetext;
	SplashTask splashtask;
	ProgressBar pbLatestUpdate;
	private GoogleMap map;
	private ArrayOfSiteDownGoogleMap googlemapcollection;
	GestureDetector gestureScanner;
	private TableLayout splashlayout;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		gestureScanner = new GestureDetector(this,this);
		initializeControl();

	}
	
	
	
	
	@Override
	protected void onDestroy() {
		stopService(new Intent(this, KhareefLiveService.class));
		super.onDestroy();
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
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureScanner.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
	
		Intent intent = new Intent(this, MenuActivity.class);

		startActivity(intent);
		
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	private void initializeControl() {
		currenttime = (TextView) findViewById(R.id.edcurrenttimesplash);
		currentnews = (TextView) findViewById(R.id.ednewssplash);
		swipetext=(TextView) findViewById(R.id.tvswipe);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapsplash)).getMap();
		
		splashlayout=(TableLayout)findViewById(R.id.tl_slide);
		pbLatestUpdate = (ProgressBar) findViewById(R.id.pblatestupdate);

		if (splashtask != null) {
			splashtask.cancel(true);
		}
		splashtask = new SplashTask(this);
		splashtask.execute();
		
		findViewById(R.id.tl_slide).setOnTouchListener(new View.OnTouchListener() { 
            @Override
           public boolean onTouch(View v, MotionEvent event){
                return gestureScanner.onTouchEvent(event);
           }
  });
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
		swipetext.setText(CommonConstraints.swipe_text);
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

	public void showLoader() {
		pbLatestUpdate.setVisibility(View.VISIBLE);
	}

	public void hideLoader() {
		pbLatestUpdate.setVisibility(View.GONE);
	}
}