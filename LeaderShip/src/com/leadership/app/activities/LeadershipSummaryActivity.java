package com.leadership.app.activities;

import com.leadership.app.R;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class LeadershipSummaryActivity extends Activity implements
		OnClickListener {

	ImageView ivOperatorIcon,ivOperatorCompare,ivOperatorNetworkKpi,ivOperatorFinance,ivOperatorSWMI,ivOperatorSummary;	
	
	WebView wv;	

	TextView tvTechnical,tvFinancial,tvSWGap,ivOperatorIconText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary);
		Initialization();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		((ImageView) findViewById(R.id.ivFinanceHeaderIcon)).setBackgroundResource(R.drawable.summary_icon);
		if(CommonValues.getInstance().LoginUser.UserMode==3){
			ivOperatorIconText.setVisibility(View.VISIBLE);
			ivOperatorIcon.setVisibility(View.GONE);
			ivOperatorIconText.setText(CommonValues.getInstance().SelectedCompany.CompanyName);
		}else{
			ivOperatorIconText.setVisibility(View.GONE);
			ivOperatorIcon.setVisibility(View.VISIBLE);
			ivOperatorIcon.setBackgroundResource(CommonValues.getInstance().SelectedCompany.CompanyLogoId);
		}
		showGraph(0);
	}

	private void Initialization() {
		ivOperatorIcon = (ImageView) findViewById(R.id.ivOperatorIcon);		
		ivOperatorCompare= (ImageView) findViewById(R.id.ivOperatorCompare);
		ivOperatorNetworkKpi= (ImageView) findViewById(R.id.ivOperatorNetworkKpi);
		
		ivOperatorFinance= (ImageView) findViewById(R.id.ivOperatorFinance);
		ivOperatorSWMI= (ImageView) findViewById(R.id.ivOperatorSWMI);
		ivOperatorSummary= (ImageView) findViewById(R.id.ivOperatorSummary);
		ivOperatorIconText= (TextView) findViewById(R.id.ivOperatorIconText);
		ivOperatorSummary.setVisibility(View.GONE);
		ivOperatorFinance.setVisibility(View.VISIBLE);	
		
		tvTechnical= (TextView) findViewById(R.id.tvTechnical);
		tvFinancial= (TextView) findViewById(R.id.tvFinancial);
		tvSWGap= (TextView) findViewById(R.id.tvSWGap);	
		
		tvTechnical.setOnClickListener(this);
		tvFinancial.setOnClickListener(this);
		tvSWGap.setOnClickListener(this);
		
		ivOperatorNetworkKpi.setOnClickListener(this);		
		ivOperatorCompare.setOnClickListener(this);			
		ivOperatorFinance.setOnClickListener(this);		
		ivOperatorSWMI.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int vid=view.getId();
		 if (vid == R.id.ivOperatorCompare) {
			LeadershipOperatorCompareActivity.ReportType="Finance";
			Intent intent = new Intent(this, LeadershipOperatorCompareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(vid == R.id.ivOperatorNetworkKpi){
			Intent intent = new Intent(this, LeadershipNetworkKPIVoiceDataActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(vid == R.id.ivOperatorFinance){
			AppbaseReportActivity.isCompare=false;
			Intent intent = new Intent(this,LeadershipFinanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(vid == R.id.ivOperatorSWMI){
			AppbaseReportActivity.isCompare=false;
			Intent intent = new Intent(this,LeadershipSWMIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);			
		}else if(vid == R.id.tvTechnical)	{
			showGraph(1);
		}else if(vid == R.id.tvFinancial)	{
			showGraph(2);
		}else if(vid == R.id.tvSWGap)	{
			showGraph(3);
		}
	}
	
	private void arrangeSelectedView(int selectedId){
		tvTechnical.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvFinancial.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvSWGap.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		
		if(selectedId==1){
			tvTechnical.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==2){
			tvFinancial.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==3){
			tvSWGap.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}
	}

	private void showGraph(int type) {
		arrangeSelectedView(type);
		wv = (WebView) findViewById(R.id.dwvSummary);
		wv.getSettings().setBuiltInZoomControls(true);
		String URL="";
		if(CommonValues.getInstance().LoginUser.UserMode!=3)
			URL = "http://120.146.188.232:9050/FinancialSummary.aspx?reqd="
				+ CommonValues.getInstance().SelectedCompany.CompanyID;
		else
			URL = "http://120.146.188.232:9050/OpeFinancialSummary.aspx?reqd="
					+ CommonValues.getInstance().SelectedCompany.CompanyID;
		if(type==1){
			URL = "http://120.146.188.232:9050/TechnicalSummary.aspx?reqd="
					+ CommonValues.getInstance().SelectedCompany.CompanyID;
		}else if(type==2){
			if(CommonValues.getInstance().LoginUser.UserMode!=3)
				URL = "http://120.146.188.232:9050/FinancialSummary.aspx?reqd="
					+ CommonValues.getInstance().SelectedCompany.CompanyID;
			else
				URL = "http://120.146.188.232:9050/OpeFinancialSummary.aspx?reqd="
						+ CommonValues.getInstance().SelectedCompany.CompanyID;
		}
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(CommonTask.getScale(this));
		wv.loadUrl(URL);
	}

}
