package com.leadership.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.leadership.app.R;
import com.leadership.app.utils.CommonTask;

public class NetworkKpiCompare extends Activity {
	WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_kpi_compare);
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

	public void initialization() {

		wv = (WebView) findViewById(R.id.dwvSpeedometer);
		wv.getSettings().setBuiltInZoomControls(true);

		String URL = "http://120.146.188.232:9050/CompareNetworkKPI.aspx?reqd=Q4";

		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(CommonTask.getScale(this));
		wv.loadUrl(URL);

	}
}
