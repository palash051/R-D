package com.vipdashboard.app.classes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.LiveFeedCallMemoAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.utils.CommonValues;

import android.content.Context;
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

public class MyNetPostCheckIn  implements LocationListener,OnMarkerDragListener,OnMapClickListener, OnMapLongClickListener {
	Context context;
	MainActionbarBase mainActionbarBase;
	LiveFeedCallMemoAdapter callMemoAdapter;
	
	public MyNetPostCheckIn(Context _context, MainActionbarBase _mainActionbarBase) {
		context = _context;
		mainActionbarBase = _mainActionbarBase;
	}
	
	GoogleMap gMap;
	
	boolean markerClicked;
	
	public void ShowMap(GoogleMap map, View mapImage){
		gMap=map;
		String addressText = "";
		double defaultLatitude = 0, defaultLongitude = 0;
		try {
			LocationManager locationManager = (LocationManager)
			mainActionbarBase.getSystemService(context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			android.location.Location loc = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			
			map.clear();
			if (loc != null) {
				LatLng Location = new LatLng(loc.getLatitude(),
						loc.getLongitude());
				defaultLatitude = Location.latitude;
				defaultLongitude = Location.longitude;
				double latitude = defaultLatitude, longitude = defaultLongitude;
				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
						//Location = new LatLng(latitude, longitude);
					}

				} catch (IOException e) {

					e.printStackTrace();
				}
				Bitmap defaultBitmap, defaultBitmapUser = null;
				ContactUser contactUser= CommonValues.getInstance().ConatactUserList.get(CommonValues.getInstance().LoginUser.UserNumber);
				if(CommonValues.getInstance().LoginUser.Facebook_Person!=null){
					ImageSize targetSize = new ImageSize(80, 80);
					defaultBitmapUser=CommonValues.getInstance().imageLoader.loadImageSync(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, targetSize, CommonValues.getInstance().imageOptions);
				}else if(contactUser!=null && contactUser.Image!=null){
					defaultBitmapUser=contactUser.Image;
				}else{
					defaultBitmapUser = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.user_icon);
				}
				contactUser=null;
				defaultBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.google_custom_pin);

				defaultBitmapUser = Bitmap.createScaledBitmap(defaultBitmapUser, defaultBitmap.getWidth() - 12,	defaultBitmap.getHeight() - 40, true);
				Bitmap bmp = Bitmap.createBitmap(defaultBitmap.getWidth(),defaultBitmap.getHeight(), defaultBitmap.getConfig());
				Canvas canvas = new Canvas(bmp);
				canvas.drawBitmap(defaultBitmap, new Matrix(), null);
				canvas.drawBitmap(defaultBitmapUser, 8, 10, null);
				map.addMarker(new MarkerOptions().position(Location).title(addressText).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
			}

			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			LatLng Defaultlocation = new LatLng(defaultLatitude,
					defaultLongitude);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation,
					14.0f));

			map.animateCamera(CameraUpdateFactory.zoomIn());

			map.animateCamera(CameraUpdateFactory.zoomTo(10));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(Defaultlocation).zoom(15).bearing(0).tilt(30)
					.build();

			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			
			//Raju's code here
			map.setOnMarkerDragListener(this);
			map.setOnMapClickListener(this);
			map.setOnMapLongClickListener(this);
		
			
			 markerClicked = false;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	 @Override
	 public void onMarkerDrag(Marker marker) {
		 String id;
		 LatLng position;
		 id=marker.getId();
		 position=marker.getPosition();
	 }

	 @Override
	 public void onMarkerDragEnd(Marker marker) {
		 String id;
		 id= marker.getId();
	 }

	 @Override
	 public void onMarkerDragStart(Marker marker) {
		 String id;
		 id= marker.getId();
	 }

	 @Override
	 public void onMapClick(LatLng point) {
		 gMap.clear();
		 gMap.addMarker(new MarkerOptions()
	       .position(point)
	       .draggable(true));
	  
	  markerClicked = false;
	 }

	 @Override
	 public void onMapLongClick(LatLng point) {
      
      gMap.clear();
	  gMap.addMarker(new MarkerOptions()
	       .position(point)
	       .draggable(true));
	  
	  markerClicked = false;
	 }
	
}
