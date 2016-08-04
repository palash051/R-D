package com.khareeflive.app.activities;

import com.khareeflive.app.R;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.utils.CommonURL;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class OfferActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offers);
		initializeControl();
		
	}
	
	private void initializeControl()
	{
		WebView myWebView = (WebView) findViewById(R.id.webviewoffer);
		myWebView.loadUrl(CommonURL.Offerurl);
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
