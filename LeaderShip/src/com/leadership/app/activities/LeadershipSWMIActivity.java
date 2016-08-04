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

public class LeadershipSWMIActivity extends Activity implements	OnClickListener {
	
	ImageView ivOperatorIcon,ivOperatorCompare,ivOperatorNetworkKpi,ivOperatorFinance,ivOperatorSWMI,ivOperatorSummary;	
	
	WebView wv;	

	TextView tv2GRAN,tv3GRAN,tvMSC,tvMGW,tvHLR,tvSGSN,tvGGSN,tvMW,tvAverage,ivOperatorIconText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swmi);
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
		CommonValues.getInstance().SelectedGraphItem="SWMI";
		LeadershipCompareDetailsActivity.selectedSWMIId=0;
		((ImageView) findViewById(R.id.ivFinanceHeaderIcon)).setBackgroundResource(R.drawable.swmi);
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
		ivOperatorIconText= (TextView) findViewById(R.id.ivOperatorIconText);
		
		ivOperatorFinance= (ImageView) findViewById(R.id.ivOperatorFinance);
		ivOperatorSWMI= (ImageView) findViewById(R.id.ivOperatorSWMI);
		ivOperatorSummary= (ImageView) findViewById(R.id.ivOperatorSummary);
		ivOperatorSWMI.setVisibility(View.GONE);
		ivOperatorFinance.setVisibility(View.VISIBLE);
		
		tv2GRAN= (TextView) findViewById(R.id.tv2GRAN);
		tv3GRAN= (TextView) findViewById(R.id.tv3GRAN);
		tvMSC= (TextView) findViewById(R.id.tvMSC);
		tvMGW= (TextView) findViewById(R.id.tvMGW);
		tvHLR= (TextView) findViewById(R.id.tvHLR);
		tvSGSN= (TextView) findViewById(R.id.tvSGSN);
		tvGGSN= (TextView) findViewById(R.id.tvGGSN);
		tvMW= (TextView) findViewById(R.id.tvMW);
		tvAverage= (TextView) findViewById(R.id.tvAverage);
		
		tv2GRAN.setOnClickListener(this);
		tv3GRAN.setOnClickListener(this);
		tvMSC.setOnClickListener(this);
		tvMGW.setOnClickListener(this);
		tvHLR.setOnClickListener(this);
		tvSGSN.setOnClickListener(this);
		tvGGSN.setOnClickListener(this);
		tvMW.setOnClickListener(this);
		tvAverage.setOnClickListener(this);		
		ivOperatorNetworkKpi.setOnClickListener(this);		
		ivOperatorCompare.setOnClickListener(this);			
		ivOperatorFinance.setOnClickListener(this);		
		ivOperatorSummary.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int vid=view.getId();
		/*CommonValues.getInstance().SelectedGraphItem="SWMI";
		LeadershipCompareDetailsActivity.selectedSWMIId=0;*/
		 if (vid == R.id.ivOperatorCompare) {
			LeadershipOperatorCompareActivity.ReportType="SWMI";			
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
		else if(vid == R.id.ivOperatorSummary){
			AppbaseReportActivity.isCompare=false;
			Intent intent = new Intent(this,LeadershipSummaryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(vid == R.id.tv2GRAN)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - 2G RAN";
			LeadershipCompareDetailsActivity.selectedSWMIId=1;
			showGraph(1);
		}else if(vid == R.id.tv3GRAN)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - 3G RAN";
			LeadershipCompareDetailsActivity.selectedSWMIId=2;
			showGraph(2);
		}else if(vid == R.id.tvMSC)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - MSC";
			LeadershipCompareDetailsActivity.selectedSWMIId=3;
			showGraph(3);
		}else if(vid == R.id.tvMGW)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - MGW";
			LeadershipCompareDetailsActivity.selectedSWMIId=4;
			showGraph(4);
		}else if(vid == R.id.tvHLR)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - HLR";
			LeadershipCompareDetailsActivity.selectedSWMIId=5;
			showGraph(5);
		}else if(vid == R.id.tvSGSN)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - SGSN";
			LeadershipCompareDetailsActivity.selectedSWMIId=6;
			showGraph(6);
		}else if(vid == R.id.tvGGSN)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - GGSN";
			LeadershipCompareDetailsActivity.selectedSWMIId=7;
			showGraph(7);
		}else if(vid == R.id.tvMW)	{
			CommonValues.getInstance().SelectedGraphItem="SWMI - MW";
			LeadershipCompareDetailsActivity.selectedSWMIId=8;
			showGraph(8);
		}else if(vid == R.id.tvAverage)	{
			showGraph(9);
		}
	}
	
	private void arrangeSelectedView(int selectedId){
		tv2GRAN.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tv3GRAN.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvMSC.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvMGW.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvHLR.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvSGSN.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvGGSN.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvMW.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		tvAverage.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
		if(selectedId==1){
			tv2GRAN.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==2){
			tv3GRAN.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==3){
			tvMSC.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==4){
			tvMGW.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==5){
			tvHLR.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==6){
			tvSGSN.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==7){
			tvGGSN.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==8){
			tvMW.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}else if(selectedId==9){
			tvAverage.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
		}
	}

	private void showGraph(int type) {
		arrangeSelectedView(type);
		wv = (WebView) findViewById(R.id.dwvSWMI);
		wv.getSettings().setBuiltInZoomControls(true);
		String URL = "http://120.146.188.232:9050/SWMICharts.aspx?reqd="
				+ CommonValues.getInstance().SelectedCompany.CompanyID;
		if(type>0)
			URL=URL+","+ type;
		if(type==9)
			URL="http://120.146.188.232:9050/SWMIAverageReport.aspx";
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(CommonTask.getScale(this));
		wv.loadUrl(URL);
	}
}
