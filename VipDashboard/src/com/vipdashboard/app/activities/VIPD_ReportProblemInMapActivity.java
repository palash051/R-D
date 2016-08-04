package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.ReportProblemAndBadExperienceRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_ReportProblemInMapActivity extends Activity implements IAsynchronousTask{

	private GoogleMap map;
	ProgressDialog progressDialog;
	LatLng Location = null;
	
	ArrayList<ReportProblemAndBadExperience> reportProblemAndBadExperienceList;
	DownloadableAsyncTask downloadableAsyncTask;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_reportprobleminmap);
		
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapProblemInMap)).getMap();
	}
	
	@Override
	protected void onResume() {
		 LoadInformation();
		super.onResume();
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
	}

	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}
	private void initializeMap() {
		try {
			map.clear();
			
			double defaultLatitude = 0, defaultLongitude = 0;
			Boolean setDefaultValue=false;
			//ReportProblemAndBadExperience reportProblemAndBadExperience = null;

			for (final ReportProblemAndBadExperience reportProblemAndBadExperience : reportProblemAndBadExperienceList) {
				Location = new LatLng(reportProblemAndBadExperience.Latitude, reportProblemAndBadExperience.Longitude);
				final String dateTime = (String) DateUtils.getRelativeTimeSpanString(
						CommonTask.convertDatetoLong(reportProblemAndBadExperience.ProblemTime), new Date().getTime(), 0);
				String markerValue = reportProblemAndBadExperience.Problem + "\n" + 
						reportProblemAndBadExperience.Comment + "\n" + 
						reportProblemAndBadExperience.LatestFeedBack + "\n" + dateTime;
				map.addMarker(new MarkerOptions().position(Location).title(markerValue));
				
				//set value in map
				
				/*map.setInfoWindowAdapter(new InfoWindowAdapter() {
					
					@Override
					public View getInfoWindow(Marker marker) {
						return null;
					}
					
					@Override
					public View getInfoContents(Marker marker) {
						View view = getLayoutInflater().inflate(R.layout.report_problem_and_bad_experience_marker, null);
						TextView tvProblemType = (TextView) view.findViewById(R.id.tvProblemName);
						TextView tvComments = (TextView) view.findViewById(R.id.tvComments);
						TextView tvLatestFeedBack = (TextView) view.findViewById(R.id.tvLatestFeedback);
						TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
						
						tvProblemType.setText(reportProblemAndBadExperience.Problem);
						tvComments.setText(reportProblemAndBadExperience.Comment);
						tvLatestFeedBack.setText(reportProblemAndBadExperience.LatestFeedBack);
						tvDate.setText(dateTime);
						return view;
					}
				});
				
				//if possiable then do otherwise block
				map.setOnMarkerClickListener(new OnMarkerClickListener() {
					
					@Override
					public boolean onMarkerClick(Marker marker) {
						MarkerOptions markerOptions = new MarkerOptions();
						markerOptions.position(Location);
						map.animateCamera(CameraUpdateFactory
								.newLatLng(Location));
						Marker ma = map.addMarker(markerOptions);
						ma.showInfoWindow();
						
						return true;
					}
				});*/
				
				if(!setDefaultValue){
					defaultLatitude=reportProblemAndBadExperience.Latitude;
					defaultLongitude=reportProblemAndBadExperience.Longitude;
					setDefaultValue=true;
				}
			}
			

			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			LatLng Defaultlocation = new LatLng(defaultLatitude,defaultLongitude);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation,14.0f));

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
	public void showProgressLoader() {
		
		/*progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		//progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
		return statisticsReportManager.GetAllReportProblemAndBadExperience(CommonValues.getInstance().LoginUser.Mobile);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		
		if(data!=null){
			ReportProblemAndBadExperienceRoot reportProblemAndBadExperienceRoot=(ReportProblemAndBadExperienceRoot)data;
			if(reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList!=null && reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList.size()>0){
				reportProblemAndBadExperienceList=new ArrayList<ReportProblemAndBadExperience>( reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList);
				initializeMap();
			}
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}

}
