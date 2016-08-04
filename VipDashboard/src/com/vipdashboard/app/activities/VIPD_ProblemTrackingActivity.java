package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportProblemAndBadExperienceAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.ReportProblemAndBadExperienceRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class VIPD_ProblemTrackingActivity extends MainActionbarBase implements IAsynchronousTask, OnItemClickListener, OnQueryTextListener {
	
	ListView lvProblamTracking;
	SearchView svProblemTracking;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	ReportProblemAndBadExperienceAdapter reportProblemAndBadExperienceAdapter;
	ArrayList<ReportProblemAndBadExperience> reportProblemAndBadExperienceList;
	TextView tvCollaborationMainTitle,tvMainSubTitle,tvNotAvailableInfo;
	public static int selectedMenu;
	private GoogleMap map;
	LinearLayout llmap;
	LatLng Location = null;
	ImageView ivLogo;
	String mobileNo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_problemtracking);
		
		
		
		
		lvProblamTracking = (ListView) findViewById(R.id.lvProblemTrackingList);
		svProblemTracking = (SearchView) findViewById(R.id.svProblemTracking);
		tvCollaborationMainTitle = (TextView) findViewById(R.id.tvMainTitle);
		tvMainSubTitle= (TextView) findViewById(R.id.tvMainSubTitle);
		tvNotAvailableInfo= (TextView) findViewById(R.id.tvNotAvailableInfo);
		
		lvProblamTracking.setOnItemClickListener(this);
		llmap = (LinearLayout) findViewById(R.id.llmap);
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapProblemInMap)).getMap();
		ivLogo = (ImageView) findViewById(R.id.ivLogo);
		
		AutoCompleteTextView search_text = (AutoCompleteTextView) svProblemTracking.findViewById(svProblemTracking.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.WHITE);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
		
		svProblemTracking.setOnQueryTextListener(this);		
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
		arrengeMenu();
		LoadInformation();
	}
	
	private void arrengeMenu(){
		
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvCollaborationMainTitle.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		tvMainSubTitle.setText(MyNetService.currentCountryName);
		//tvMainSubTitle.setText(l.getDisplayCountry());
		
		
		
		if(selectedMenu == 2){
			//tvCollaborationMainTitle.setText("Problem Tracking");
			ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.problem_tracking));
			llmap.setVisibility(LinearLayout.VISIBLE);
		}else{
			//tvCollaborationMainTitle.setText("Bad Experiences");
			
			llmap.setVisibility(LinearLayout.VISIBLE);
			ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.bad_experience));
		}
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		/*IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
		return statisticsReportManager.GetAllReportProblemAndBadExperience(CommonValues.getInstance().LoginUser.Mobile);*/
		ReportProblemAndBadExperienceRoot reportProblemAndBadExperienceRoot=new ReportProblemAndBadExperienceRoot();
		MyNetDatabase db=new MyNetDatabase(this);
		db.open();
		ArrayList<ReportProblemAndBadExperience> rdpeList=db.getReportProblemAndBadExperience();
		db.close();
		reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList=rdpeList;
		return reportProblemAndBadExperienceRoot;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			tvNotAvailableInfo.setVisibility(View.GONE);
			ReportProblemAndBadExperienceRoot reportProblemAndBadExperienceRoot=(ReportProblemAndBadExperienceRoot)data;
			reportProblemAndBadExperienceAdapter = new ReportProblemAndBadExperienceAdapter(this, R.layout.reportproblemandbadexperience_item,
					new ArrayList<ReportProblemAndBadExperience>( reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList));
			lvProblamTracking.setAdapter(reportProblemAndBadExperienceAdapter);
			reportProblemAndBadExperienceList=new ArrayList<ReportProblemAndBadExperience>( reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList);
			if(lvProblamTracking.getCount()<=0)
			{
				tvNotAvailableInfo.setVisibility(View.VISIBLE);
			}
			LoadMap();
		}
		else
		{
			tvNotAvailableInfo.setVisibility(View.VISIBLE);
		}
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		//progressDialog.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
		ReportProblemAndBadExperience reportProblemAndBadExperience = (ReportProblemAndBadExperience) view.getTag();
		if(reportProblemAndBadExperience != null){
			if(selectedMenu==1){
				Intent intent = new Intent(this,VIPD_ProblemTrackingDetails.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				VIPD_ProblemTrackingDetails.reportProblemAndBadExperience = reportProblemAndBadExperience;
				VIPD_ProblemTrackingDetails.selectedList = 1;
				startActivity(intent);
			}else{
				Intent intent = new Intent(this,VIPD_ProblemTrackingDetails.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				VIPD_ProblemTrackingDetails.reportProblemAndBadExperience = reportProblemAndBadExperience;
				VIPD_ProblemTrackingDetails.selectedList = 2;
				startActivity(intent);
			}
		}
	}

	@Override
	public boolean onQueryTextChange(String value) {
		reportProblemAndBadExperienceAdapter.Filter(value);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
	
	private void LoadMap(){
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
}
