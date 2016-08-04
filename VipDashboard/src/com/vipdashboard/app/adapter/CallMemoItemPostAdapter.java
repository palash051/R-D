package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.utils.CommonTask;

public class CallMemoItemPostAdapter extends ArrayAdapter<PhoneCallInformation> implements LocationListener{
	private Context context;
	private PhoneCallInformation callInformation;
	private ArrayList<PhoneCallInformation> callInformationList;
	
	public CallMemoItemPostAdapter(Context _context, int textViewResourceId,
			ArrayList<PhoneCallInformation> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		callInformationList = _objects;
	}
	
	@Override
	public int getCount() {
		return callInformationList.size();
	}

	public void setList(ArrayList<PhoneCallInformation> _userGroupUnionList) {

		callInformationList = _userGroupUnionList;
		notifyDataSetChanged();
	}

	@Override
	public PhoneCallInformation getItem(int position) {
		return callInformationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		callInformation = callInformationList.get(position);
		//final Holder holder;
		try{
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View feedItemView = inflater
					.inflate(R.layout.call_memo_item_post, null);
			//holder = new Holder();
			TextView name = (TextView) feedItemView.findViewById(R.id.tvName);
			TextView date = (TextView) feedItemView.findViewById(R.id.tvDate);
			TextView callMemo = (TextView) feedItemView.findViewById(R.id.tvCallMemoText);
			TextView location = (TextView) feedItemView.findViewById(R.id.tvCallMemoLocation);
			ImageView image = (ImageView) feedItemView.findViewById(R.id.ivLiveFeedImage);
			ImageView voice = (ImageView) feedItemView.findViewById(R.id.ivVoiceMemo);
			
			String personname = CommonTask.getContentName(context, callInformation.Number);
			if(personname != ""){
				name.setText(personname);
			}else{
				name.setText(callInformation.Number);
			}
			int photoId=CommonTask.getContentPhotoId(this.context, callInformation.Number);
			if(photoId>0){
				image.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
			}
			if(callInformation.CallTime.getTime() > 0){
				String dateTime = (String) DateUtils.getRelativeTimeSpanString(callInformation.CallTime.getTime(), new Date().getTime(), 0);
				date.setText(dateTime);
			}
			
			if(callInformation.TextCallMemo != null){
				if (callInformation.TextCallMemo.length() > 15) {
					String text = callInformation.TextCallMemo.substring(0, 15);
					text = text.replaceAll("\n", " ");
					callMemo.setText(text + "...");
				}else {
					callMemo.setText(callInformation.TextCallMemo);
				}
			}
			location.setText("");
			if(callInformation.Latitude>0.0 && callInformation.Longitude>0.0){
				String addressText = "";
				LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0, this);
				android.location.Location loc = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (loc != null) {
					LatLng Location = new LatLng(callInformation.Latitude,
							callInformation.Longitude);
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
							location.setText(addressText);
							Location = new LatLng(latitude, longitude);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				location.setVisibility(View.GONE);
			}
			if(callInformation.VoiceRecordPath != null)
				voice.setVisibility(View.VISIBLE);
			else
				voice.setVisibility(View.GONE);
			
			feedItemView.setTag(callInformation);
			
			return feedItemView;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
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
