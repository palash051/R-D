package com.leadership.app.activities;

import com.leadership.app.R;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.ImageView;
import android.widget.TextView;

public class CityWiseReportActivity extends FragmentActivity implements
		OnClickListener {
	WebView wv;
	TextView tvCitySubHeader;
	ImageView ivNext;
	public static boolean isCompare=false;
	
	public static String selectedCityNames="";
	public static String selectedCityIds="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_wise_report);

		wv = (WebView) findViewById(R.id.wvCity);
		tvCitySubHeader = (TextView) findViewById(R.id.tvCitySubHeader);
		wv.getSettings().setBuiltInZoomControls(true);
		ivNext = (ImageView) findViewById(R.id.ivNext);
		ivNext.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		initialization();
	}

	public void initialization() {
		String URL="";
		if(!isCompare){
			ivNext.setVisibility(View.VISIBLE);
			tvCitySubHeader
					.setText(CommonValues.getInstance().SelectedCity.CityName.toUpperCase());
			
			 URL = "http://120.146.188.232:9050/ReportCity.aspx?reqd="
					+ CommonValues.getInstance().SelectedCity.CityID;
		}else{
			ivNext.setVisibility(View.GONE);
			selectedCityNames= (CommonValues.getInstance().SelectedCity.CityName+","+selectedCityNames).toUpperCase();
			selectedCityIds=CommonValues.getInstance().SelectedCity.CityID+","+selectedCityIds;
			tvCitySubHeader
			.setText(selectedCityNames.length()>30?selectedCityNames.substring(0, 30)+"...":selectedCityNames.subSequence(0, selectedCityNames.length()-1));
	
			URL = "http://120.146.188.232:9050/ReportCityCompareNew.aspx?reqd="
					+ selectedCityIds;
		}
		
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(CommonTask.getScale(this));
		
		wv.loadUrl(URL);

	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ivNext){
			if (!CommonTask.isOnline(this)) {
				CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
				return;
			}
			Intent intent = new Intent(this, CityWiseComparisionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
}
