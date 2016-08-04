package com.leadership.app.activities;

import com.leadership.app.R;
import com.leadership.app.utils.CommonConstraints;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class NetworkKpiVoice extends Activity {
	
	WebView wv;
	TextView tvApplicationTopHeader;
	public static String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_kpi_voice);
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
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	public void initialization(){
		
		tvApplicationTopHeader = (TextView) findViewById(R.id.tvApplicationTopHeader);
		tvApplicationTopHeader.setText("NETWORK KPI " + type.toUpperCase());
		
		wv = (WebView)findViewById(R.id.dwvSpeedometer);
		wv.getSettings().setBuiltInZoomControls(true);
		
		String URL = "http://120.146.188.232:9050/TechnicalKPI.aspx?reqd="+CommonValues.getInstance().SelectedCompany.CompanyID+","+type;
		
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(CommonTask.getScale(this));
		wv.loadUrl(URL); 
		
	}

}
