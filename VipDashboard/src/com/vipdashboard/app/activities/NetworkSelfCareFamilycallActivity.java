package com.vipdashboard.app.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MyNetApplication;

public class NetworkSelfCareFamilycallActivity extends Activity implements
		LocationListener {

	RelativeLayout ralsalfcare_MyExperience, rlsalfcare_MyProfile,
			rlsalfcare_MyApp, rlsalfcare_Offers;

	private GoogleMap map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfcare_callhistry);

		// initilization();

		initializeMap();
		
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

	private void initilization() {

		ralsalfcare_MyExperience = (RelativeLayout) findViewById(R.id.rlMyExperience);
		rlsalfcare_MyProfile = (RelativeLayout) findViewById(R.id.rlSelfcare_Myprofile);
		rlsalfcare_MyApp = (RelativeLayout) findViewById(R.id.rlSelfcare_Myapps);
		rlsalfcare_Offers = (RelativeLayout) findViewById(R.id.rlSelfcare_Myoffers);

		/*
		 * rlsalfcare_product.setOnClickListener(this);
		 * rlsalfcare_Account.setOnClickListener(this);
		 * rlsalfcare_SelfService.setOnClickListener(this);
		 * rlsalfcare_AboutUs.setOnClickListener(this);
		 * ralsalfcare_MyExperience.setOnClickListener(this);
		 * rlsalfcare_MyProfile.setOnClickListener(this);
		 * rlsalfcare_MyApp.setOnClickListener(this);
		 * rlsalfcare_Offers.setOnClickListener(this);
		 */
	}

	private void initializeMap() {

		String addressText = "";
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapSelfcarecallhistry)).getMap();
		double defaultLatitude = 0, defaultLongitude = 0;

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (loc != null) {

			
			Bitmap bmp1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.google_custom_pin);
			Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),
					R.drawable.user_icon);
			bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth()-12,bmp1.getHeight()- 55, true);			
			Bitmap bmp = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(),
					bmp1.getConfig());
			Canvas canvas = new Canvas(bmp);			
			Paint color = new Paint();
			color.setTextSize(22);
			color.setColor(Color.BLACK);
			color.setTextAlign(Paint.Align.CENTER);		
			canvas.drawBitmap(bmp1, new Matrix(), null);
			canvas.drawBitmap(bmp2, 8, 10, null);
			canvas.drawText("User", canvas.getWidth() / 2, bmp1.getHeight()- 25 , color);

			LatLng Location = new LatLng(loc.getLatitude(), loc.getLongitude());
			defaultLatitude = Location.latitude;
			defaultLongitude = Location.longitude;
			double latitude = defaultLatitude, longitude = defaultLongitude;
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			for (int index = 0; index < 5; index++) {
				try {
					List<Address> addresses = geocoder.getFromLocation(
							latitude, longitude, 1);
					if (addresses != null && addresses.size() > 0) {
						Address address = addresses.get(0);
						for (int lineIndex = 0; lineIndex < address
								.getMaxAddressLineIndex(); lineIndex++) {
							addressText = addressText
									+ address.getAddressLine(lineIndex) + ", ";
						}
						addressText = addressText + address.getLocality()
								+ ", " + address.getCountryName();
						map.addMarker(new MarkerOptions().position(Location)
								.title(addressText)
								.icon(BitmapDescriptorFactory.fromBitmap(bmp)));
						latitude = latitude + 0.004;
						longitude = longitude + 0.005;
						Location = new LatLng(latitude, longitude);
					}

				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);

		map.moveCamera(CameraUpdateFactory
				.newLatLngZoom(Defaultlocation, 14.0f));

		map.animateCamera(CameraUpdateFactory.zoomIn());

		map.animateCamera(CameraUpdateFactory.zoomTo(10));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(Defaultlocation).zoom(14).bearing(90).tilt(30).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

	}

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

}
