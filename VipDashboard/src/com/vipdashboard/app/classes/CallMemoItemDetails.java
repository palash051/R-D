package com.vipdashboard.app.classes;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.google.android.gms.maps.model.LatLng;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.PlayVoiceMemoActivity;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CallMemoItemDetails implements OnClickListener, LocationListener{
	
	public Context context;
	Dialog dialog;
	TextView tvName, tvDate, tvText, tvLocation;
	ImageView ivPlayVoiceMemo,ivPersonImage;
	PhoneCallInformation phoneCallInforamtion;
	private AQuery aq;
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	
	public CallMemoItemDetails(Context _context) {
		context = _context;
	}
	
	public void showCallMemoItemDetails(PhoneCallInformation CallInforamtion){
		if(dialog == null){
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.call_memo_item_details);
			dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			
			tvName = (TextView) dialog.findViewById(R.id.tvPersonNumber);
			ivPersonImage = (ImageView) dialog.findViewById(R.id.ivPersonImage);
			tvDate = (TextView) dialog.findViewById(R.id.tvCallTime);
			tvText = (TextView) dialog.findViewById(R.id.tvTextMemo);
			tvLocation = (TextView) dialog.findViewById(R.id.tvCallerLocation);
			ivPlayVoiceMemo = (ImageView) dialog.findViewById(R.id.ivPlayCallMemo);
			
			ivPlayVoiceMemo.setOnClickListener(this);
		}		
		phoneCallInforamtion = CallInforamtion;
		if(phoneCallInforamtion.VoiceRecordPath == null){
			ivPlayVoiceMemo.setVisibility(ImageView.GONE);
		}else{
			ivPlayVoiceMemo.setVisibility(ImageView.VISIBLE);
		}
		tvName.setText("");
		tvDate.setText("");
		tvText.setText("");
		tvLocation.setText("");
		LoadImage();
		LoadInforamtion();
	}
	
	private void LoadImage() {
		aq=new AQuery(context);
		imgOptions = CommonValues.getInstance().defaultImageOptions; 		
		imgOptions.targetWidth=100;
		imgOptions.ratio=0;//AQuery.RATIO_PRESERVE;
		imgOptions.round = 8;
		
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			aq.id(ivPersonImage).image(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, imgOptions);
		}
		else if(CommonValues.getInstance().FB_Profile_Picture_Path != ""){
			aq.id(ivPersonImage).image(CommonValues.getInstance().FB_Profile_Picture_Path, imgOptions);
		}
		else {
			int photoId=CommonTask.getContentPhotoId(this.context, CommonValues.getInstance().LoginUser.Mobile);
			if(photoId>0){
				ivPersonImage.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
			}
		}
	}

	private void LoadInforamtion() {
		String name = CommonTask.getContentName(context, phoneCallInforamtion.Number);
		if(name != ""){
			tvName.setText(name);
		}else{
			tvName.setText(phoneCallInforamtion.Number);
		}
		
		String date = (String)DateUtils.getRelativeTimeSpanString(phoneCallInforamtion.CallTime.getTime(), new Date().getTime(), 0);
		tvDate.setText(date);
		
		tvText.setText(phoneCallInforamtion.TextCallMemo);
		
		if(phoneCallInforamtion.Latitude>0.0 && phoneCallInforamtion.Longitude>0.0){
			String addressText = "";
			LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			android.location.Location loc = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				LatLng Location = new LatLng(phoneCallInforamtion.Latitude,
						phoneCallInforamtion.Longitude);
				double latitude = Location.latitude, longitude = Location.longitude;
				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
				try{
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
						tvLocation.setText(addressText);
						Location = new LatLng(latitude, longitude);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		dialog.show();
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivPlayCallMemo){
			try{
				if(phoneCallInforamtion.VoiceRecordPath != null){
					/*VoiceCallPlayer callPlayer = new VoiceCallPlayer(context);
					callPlayer.PlayVoiceCallRecorder(phoneCallInforamtion);*/
					PlayVoiceMemoActivity.phoneCallInformation = phoneCallInforamtion;
					Intent intent = new Intent(context, PlayVoiceMemoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					context.startActivity(intent);
				}else{
					Toast.makeText(context, "No Voice Record", Toast.LENGTH_SHORT).show();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
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

}
