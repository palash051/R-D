package com.vipdashboard.app.activities;

import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.MasterDataConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OffersActivity  extends MainActionbarBase{
	
	WebView wv;
	TextView tvPrivacy;
	TextView tvCompanyName, tvCompanyCountry,tvText;
	int OverviewActionID=0;
	
	ImageView ivImage;
	
	RelativeLayout rlHomePageMyDashBoard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedInstanceState= getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offers);
		
		 if(savedInstanceState!=null && savedInstanceState.containsKey("OverviewActionID"))
		 {
			 OverviewActionID =savedInstanceState.getInt("OverviewActionID");
		 }
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
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
		{	if (!isFinishing()) 
		{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
		}
		Initialization();
		
		if(!CommonTask.isMyServiceRunning(this))
			startService(new Intent(this, MyNetService.class));
	}

	private void Initialization() {
//		wv = (WebView) findViewById(R.id.dwvSpeedometer);
		tvPrivacy = (TextView) findViewById(R.id.tvPrivacy);
		ivImage = (ImageView) findViewById(R.id.ivImage);
		tvText= (TextView) findViewById(R.id.tvText);
		rlHomePageMyDashBoard= (RelativeLayout) findViewById(R.id.rlHomePageMyDashBoard);
		
		
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//tvCompanyName.setText(tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());	
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		
		tvPrivacy.setText("We don’t have service contract with your operator "+tMgr.getNetworkOperatorName().toString()+". Please browse through the web site for latest offers");
		
		switch(OverviewActionID)
		{
			case MasterDataConstants.CommonWebLink.OFFERS:
				ivImage.setImageResource(R.drawable.offer);
				tvText.setText("Offers");
				rlHomePageMyDashBoard.setBackgroundColor(Color.parseColor("#FF33CC"));
				break;
			case MasterDataConstants.CommonWebLink.SUPPORT:
				ivImage.setImageResource(R.drawable.support);
				tvText.setText("Support");
				rlHomePageMyDashBoard.setBackgroundColor(Color.parseColor("#CC3300"));
				break;
			case MasterDataConstants.CommonWebLink.PACKAGES_AND_BILLING:
				ivImage.setImageResource(R.drawable.package_billing);
				rlHomePageMyDashBoard.setBackgroundColor(Color.parseColor("#00B050"));
				tvText.setText("Billing");
				break;
		}
		
		//showWebView();
	}

	//Old code will need to implement later on...
	
	/*private void showWebView() {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		
		switch(OverviewActionID)
		{
			case MasterDataConstants.CommonWebLink.OFFERS:
				wv.loadUrl(CommonValues.getInstance().LoginUser.Company.Offers); 
				break;
			case MasterDataConstants.CommonWebLink.SUPPORT:
				wv.loadUrl(CommonValues.getInstance().LoginUser.Company.SupportAddress); 
				break;
			case MasterDataConstants.CommonWebLink.PACKAGES_AND_BILLING:
				wv.loadUrl(CommonValues.getInstance().LoginUser.Company.WebAdd); 
				break;
		}
		
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			          view.loadUrl(url);
			          return true;
			}});
	}
*/
}
