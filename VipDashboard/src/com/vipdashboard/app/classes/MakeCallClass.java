package com.vipdashboard.app.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.activities.MyNetAllCallDetails;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.MakeSMSActivity;
import com.vipdashboard.app.adapter.MyProfileOnlineStatusAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.PhoneCallReceiver;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MakeCallClass implements LocationListener, OnClickListener, IAsynchronousTask  {
	
	private Context context;
	Dialog dialog;
	ImageView ivOnlineStatus,ivCallerImage;
	
	TextView tvName, tvNumber;
	RelativeLayout rlCall, rlSMS;
	GoogleMap map;
	String name, number;
	PhoneCallReceiver receiver = null;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	ArrayList<UserStatus> userStatus;
	MyProfileOnlineStatusAdapter adapter;
	CheckBox ckIncludeVoiceMemo;
	public MakeCallClass(Context _context) {
		this.context = _context;		
	}
	
	public void getCallInformation(String Number){
		if(dialog == null){
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.make_a_call);
			
			tvName = (TextView)dialog. findViewById(R.id.tvCallerName);
			tvNumber = (TextView)dialog. findViewById(R.id.tvCallerNumber);
			rlCall = (RelativeLayout) dialog.findViewById(R.id.rlOk);
			rlSMS = (RelativeLayout)dialog. findViewById(R.id.rlCancel);
			ivOnlineStatus = (ImageView)dialog.findViewById(R.id.ivmakeCall);
			
			map = ((MapFragment) ((MyNetAllCallDetails)context).getFragmentManager().findFragmentById(R.id.mapCall)).getMap();
			
			ckIncludeVoiceMemo = (CheckBox)dialog. findViewById(R.id.ckIncludeVoiceMemo);

			rlCall.setOnClickListener(this);
			rlSMS.setOnClickListener(this);
		}
		ivCallerImage= (ImageView)dialog.findViewById(R.id.ivCallerImage);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		this.number=Number;
		this.name = CommonTask.getContentName(context, number);
		
		if (name != null) {
			tvName.setText(name);
		}
		tvNumber.setText(number);
		
		int photoId=CommonTask.getContentPhotoId(this.context, number);
		
		if(photoId>0){
			ivCallerImage.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
		}else{
			ivCallerImage.setImageDrawable(context.getResources().getDrawable(R.drawable.user_icon));
		}
		
		LoadInformation();
		initmap();
		
	}

	private void LoadInformation() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Processing...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
		
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.GetUserByMobileNumber(number);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			User user = (User) data;
			if(user.UserOnlineAvailableStatusID>0){
				setUserStatus(user.UserOnlineAvailableStatusID);
			}
		}
		dialog.show();
	}

	private void setUserStatus(long userOnlineAvailableStatusID) {
		switch ((int)userOnlineAvailableStatusID) {
		case CommonConstraints.ONLINE:
			ivOnlineStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.user_status_online));
			break;
		case CommonConstraints.AWAY:
			ivOnlineStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.user_status_away));
			break;
		case CommonConstraints.DO_NOT_DISTURB:
			ivOnlineStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.user_status_busy));
			break;
		case CommonConstraints.INVISIBLE:
			ivOnlineStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.user_status_offline));
			break;
		case CommonConstraints.OFFLINE:
			ivOnlineStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.user_status_offline));
			break;
		case CommonConstraints.PHONEOFF:
			ivOnlineStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.user_status_offline));
			break;
		case CommonConstraints.BUSY:
			ivOnlineStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.user_status_busy));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlOk) {
			Intent calltomanager = new Intent(Intent.ACTION_CALL);
			calltomanager.setData(Uri.parse("tel:" + number));
			context.startActivity(calltomanager);
			if(ckIncludeVoiceMemo.isChecked()){
				CommonConstraints.ISPRESSED_INCLUCE_VOICE_CALL = 1;
			}			

		} else if (view.getId() == R.id.rlCancel) {
			Intent intent = new Intent(context, MakeSMSActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("SMS_NUMBER", number);
			context.startActivity(intent);
		}
	}
	
	
	private void initmap() {
		MyNetDatabase database = new MyNetDatabase(context);
		database.open();
		ArrayList<PhoneCallInformation> callinforamtoins = database.getCallInformation(number);
		database.close();
		
		String addressText = "";
		double defaultLatitude = 0, defaultLongitude = 0;
		LocationManager locationManager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0,  this);
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
