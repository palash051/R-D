package com.vipdashboard.app.activities;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_ProblemTrackingDetails extends MainActionbarBase {
	
	public static ReportProblemAndBadExperience reportProblemAndBadExperience;
	public static int selectedList;
	TextView tvProblemTrackingTTNumber, tvProblemTrackingStatus, tvProblemTrackingDateAndTime,
			tvProblemTrackingProblem, tvProblemTrackingLocation, tvProblemTrackingLatestUpdate,
			tvBadExperienceDateAndTime,tvBadExperienceEvent, tvBadExperienceLocation,
			tvProblemTrackingDetalisTitle;
	RelativeLayout rlProblemTracking, rlBadExperience;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_problemtracking_details);
		
		
		
		Initialization();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MyNetApplication.activityPaused();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
			if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
		arrengeSelectedListValue();
	}

	private void Initialization() {
		
		rlProblemTracking = (RelativeLayout) findViewById(R.id.rlProblemTracking);
		rlBadExperience = (RelativeLayout) findViewById(R.id.rlBadExperience);
		
		tvProblemTrackingTTNumber = (TextView) findViewById(R.id.tvTTNumber);
		tvProblemTrackingStatus = (TextView) findViewById(R.id.tvStatus);
		tvProblemTrackingDateAndTime = (TextView) findViewById(R.id.tvDateAndTime);
		tvProblemTrackingProblem = (TextView) findViewById(R.id.tvProblem);
		tvProblemTrackingLocation = (TextView) findViewById(R.id.tvLocation);
		tvProblemTrackingLatestUpdate = (TextView) findViewById(R.id.tvLatestUpdate);
		
		tvBadExperienceDateAndTime = (TextView) findViewById(R.id.tvDateTime);
		tvBadExperienceEvent = (TextView) findViewById(R.id.tvEvent);
		tvBadExperienceLocation = (TextView) findViewById(R.id.tvLocaiton);
		
		tvProblemTrackingDetalisTitle = (TextView) findViewById(R.id.tvProblemTrackingDetalisTitle);
	}
	
	private void arrengeSelectedListValue(){
		if(selectedList == 1){
			tvProblemTrackingDetalisTitle.setText("Problem Tracking Details");
			arrengeProblemTrackingVlaue();
			rlProblemTracking.setVisibility(RelativeLayout.VISIBLE);
			rlBadExperience.setVisibility(RelativeLayout.GONE);
		}else{
			tvProblemTrackingDetalisTitle.setText("Bad Experiences Details");
			arrengeBadExperienceVlaue();
			rlBadExperience.setVisibility(RelativeLayout.VISIBLE);
			rlProblemTracking.setVisibility(RelativeLayout.GONE);
		}
	}

	private void arrengeBadExperienceVlaue() {
		try{
			/*if(!reportProblemAndBadExperience.TTNumber.equals(""))
				tvProblemTrackingTTNumber.setText(reportProblemAndBadExperience.TTNumber);
			if(!reportProblemAndBadExperience.Status.equals(""))
				tvProblemTrackingStatus.setText(reportProblemAndBadExperience.Status);*/
			if(reportProblemAndBadExperience.ProblemTime != null){
				String dateTime = (String) DateUtils.getRelativeTimeSpanString(
						CommonTask.convertDatetoLong(reportProblemAndBadExperience.ProblemTime), new Date().getTime(), 0);
				//tvBadExperienceDateAndTime.setText(CommonTask.ConvertUTCtoLocalDateTimeString(String.valueOf(reportProblemAndBadExperience.ProblemTime)));
				tvBadExperienceDateAndTime.setText(dateTime);
			}
			if(reportProblemAndBadExperience.Problem != null)
				tvBadExperienceEvent.setText(reportProblemAndBadExperience.Problem);
			/*if(!reportProblemAndBadExperience.LatestFeedBack.equals(""))
				tvProblemTrackingStatus.setText(reportProblemAndBadExperience.LatestFeedBack);*/
			//For locaiton
			if(reportProblemAndBadExperience.Latitude>0.0 && reportProblemAndBadExperience.Longitude > 0.0){
				LatLng Location = new LatLng(reportProblemAndBadExperience.Latitude, reportProblemAndBadExperience.Longitude);					
				Geocoder geocoder =
		                new Geocoder(this, Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(Location.latitude, Location.longitude, 1);
				String addressText="";
				if (addresses != null && addresses.size() > 0) {			                
	                Address address = addresses.get(0);			                
	                for (int  lineIndex=0 ;lineIndex<address.getMaxAddressLineIndex();lineIndex++) {
	                	addressText=addressText+address.getAddressLine(lineIndex)+", ";
					}
	                addressText=addressText+address.getLocality()+", "+address.getCountryName();	
	                tvBadExperienceLocation.setText(addressText);
	            }
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void arrengeProblemTrackingVlaue() {
		try{
			if(reportProblemAndBadExperience.TTNumber != null)
				tvProblemTrackingTTNumber.setText(reportProblemAndBadExperience.TTNumber);
			if(reportProblemAndBadExperience.Status != null)
				tvProblemTrackingStatus.setText(reportProblemAndBadExperience.Status);
			if(reportProblemAndBadExperience.ProblemTime != null){
				
				String dateTime = (String) DateUtils.getRelativeTimeSpanString(
						CommonTask.convertDatetoLong(reportProblemAndBadExperience.ProblemTime), new Date().getTime(), 0);
				//String dateTime =CommonTask.ConvertUTCtoLocalDateTimeString(String.valueOf(reportProblemAndBadExperience.ProblemTime));
				tvProblemTrackingDateAndTime.setText(dateTime);
			}
			if(reportProblemAndBadExperience.Problem != null)
				tvProblemTrackingProblem.setText(reportProblemAndBadExperience.Problem);
			if(reportProblemAndBadExperience.LatestFeedBack != null)
				tvProblemTrackingLatestUpdate.setText(reportProblemAndBadExperience.LatestFeedBack);
			//For locaiton
			if(reportProblemAndBadExperience.Latitude>0.0 && reportProblemAndBadExperience.Longitude > 0.0){
				LatLng Location = new LatLng(reportProblemAndBadExperience.Latitude, reportProblemAndBadExperience.Longitude);					
				Geocoder geocoder =
		                new Geocoder(this, Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(Location.latitude, Location.longitude, 1);
				String addressText="";
				if (addresses != null && addresses.size() > 0) {			                
	                Address address = addresses.get(0);			                
	                for (int  lineIndex=0 ;lineIndex<address.getMaxAddressLineIndex();lineIndex++) {
	                	addressText=addressText+address.getAddressLine(lineIndex)+", ";
					}
	                addressText=addressText+address.getLocality()+", "+address.getCountryName();	
	                tvProblemTrackingLocation.setText(addressText);
	            }
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
