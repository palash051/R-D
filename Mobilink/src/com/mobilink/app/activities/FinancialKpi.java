package com.mobilink.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.mobilink.app.R;

public class FinancialKpi extends Activity {
	WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalcial_kpi);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialization();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void initialization() {

		wv = (WebView) findViewById(R.id.dwvSpeedometer);
		wv.getSettings().setBuiltInZoomControls(true);

		String URL = "http://120.146.188.232:9050/FinancialKPI.aspx";

		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.loadUrl(URL);

	}
}
