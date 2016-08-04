package com.vipdashboard.app.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonTask;

public class CollaborationUserLocationActivity extends MainActionbarBase implements LocationListener {
	
	GoogleMap map;
	double lat, lang;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collaboration_user_location);
		
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			return;	
		}
		MyNetApplication.activityResumed();
		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.containsKey("LATITUDE") && bundle.containsKey("LONGITUDE")){
			lat = bundle.getDouble("LATITUDE");
			lang = bundle.getDouble("LONGITUDE");
		}
		initializeControl();
	}
	
	private void initializeControl() {

		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.maplatestupdate)).getMap();

		getLetestUpdateData();
	}

	private void getLetestUpdateData() {
		String addressText = "";
		double defaultLatitude = 0, defaultLongitude = 0;
		try {
			LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			android.location.Location loc = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			
			map.clear();
			if (loc != null) {
				LatLng Location = new LatLng(lat,
						lang);
				defaultLatitude = Location.latitude;
				defaultLongitude = Location.longitude;
				double latitude = defaultLatitude, longitude = defaultLongitude;
				Geocoder geocoder = new Geocoder(this, Locale.getDefault());
				try {
					List<Address> addresses = geocoder.getFromLocation(
							latitude, longitude, 1);
					if (addresses != null && addresses.size() > 0) {
						Address address = addresses.get(0);
						for (int lineIndex = 0; lineIndex < address
								.getMaxAddressLineIndex();

						lineIndex++) {
							addressText = addressText +

							address.getAddressLine(lineIndex) + ", ";
						}
						addressText = addressText + address.getLocality

						() + ", " + address.getCountryName

						();
						map.addMarker(new MarkerOptions().position(Location)
								.title(addressText));
						Location = new LatLng(latitude, longitude);
					}

				} catch (IOException e) {

					e.printStackTrace();
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
		
		} catch (Exception e) {
			e.printStackTrace();
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
