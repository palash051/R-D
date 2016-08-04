package com.khareeflive.app.activities;

import com.khareeflive.app.R;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.utils.CommonURL;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class NewGraphActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgraph);
		initializeControl();
		
	}
	
	private void initializeControl()
	{
		WebView myWebView = (WebView) findViewById(R.id.webviewgraph);
		myWebView.getSettings().setBuiltInZoomControls(true); 
		myWebView.getSettings().setLoadWithOverviewMode(true);
		myWebView.getSettings().setUseWideViewPort(true);
		myWebView.setInitialScale(1);
		myWebView.loadUrl(CommonURL.Graphurl);
	}
	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}
}
