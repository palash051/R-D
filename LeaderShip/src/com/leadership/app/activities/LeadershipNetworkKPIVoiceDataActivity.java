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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LeadershipNetworkKPIVoiceDataActivity extends Activity implements
		OnClickListener {

	RelativeLayout rlVoice_happiness, rlData_happiness;
	LinearLayout llVoice, llData;
	TextView tvDown_Link_Speed;
	WebView wv;

	TextView tv3G_call_Setup_success_rate, tv3G_Drop_Rate,
			tv2G_call_Setup_success_rate, tv2G_Drop_Rate, tv3G_on_2G,
			tvEDGE_performance, tvGB_per_Subscriber,tvDataCityWiseComparision,ivOperatorIconText;
	
	ImageView ivOperatorCompare,ivOperatorNetworkKpi,ivOperatorIcon,ivDown_Link_Speed,ivDataCoverage,ivAppCoverage,ivOperatorSWMI,ivOperatorSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leadership_network_kpi_voice);
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
		rlVoice_happiness.performClick();
		
		/*AQuery aq = new AQuery(ivOperatorIcon);
		ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
		imgOptions.ratio = 0;
		imgOptions.targetWidth = 200;
		aq.id(ivOperatorIcon).image(CommonURL.getInstance().getImageServer+ CommonValues.getInstance().SelectedCompany.CompanyLogo,
						imgOptions);*/
		if(CommonValues.getInstance().LoginUser.UserMode==3){
			ivOperatorIconText.setVisibility(View.VISIBLE);
			ivOperatorIcon.setVisibility(View.GONE);
			ivOperatorIconText.setText(CommonValues.getInstance().SelectedCompany.CompanyName);
		}else{
			ivOperatorIconText.setVisibility(View.GONE);
			ivOperatorIcon.setVisibility(View.VISIBLE);
			ivOperatorIcon.setBackgroundResource(CommonValues.getInstance().SelectedCompany.CompanyLogoId);
		}
	}

	private void Initialization() {
		rlVoice_happiness = (RelativeLayout) findViewById(R.id.rlVoice_happiness);
		rlData_happiness = (RelativeLayout) findViewById(R.id.rlData_happiness);

		llVoice = (LinearLayout) findViewById(R.id.llVoice);
		llData = (LinearLayout) findViewById(R.id.llData);
		
		ivOperatorIconText= (TextView) findViewById(R.id.ivOperatorIconText);

		tvDown_Link_Speed = (TextView) findViewById(R.id.tvDown_Link_Speed);

		rlVoice_happiness.setOnClickListener(this);
		rlData_happiness.setOnClickListener(this);
		tvDown_Link_Speed.setOnClickListener(this);

		tv3G_call_Setup_success_rate = (TextView) findViewById(R.id.tv3G_call_Setup_success_rate);
		tv3G_Drop_Rate = (TextView) findViewById(R.id.tv3G_Drop_Rate);
		tv2G_call_Setup_success_rate = (TextView) findViewById(R.id.tv2G_call_Setup_success_rate);
		tv2G_Drop_Rate = (TextView) findViewById(R.id.tv2G_Drop_Rate);
		tv3G_on_2G = (TextView) findViewById(R.id.tv3G_on_2G);
		tvEDGE_performance = (TextView) findViewById(R.id.tvEDGE_performance);
		tvGB_per_Subscriber = (TextView) findViewById(R.id.tvGB_per_Subscriber);
		tvDataCityWiseComparision= (TextView) findViewById(R.id.tvDataCityWiseComparision);
		
		ivOperatorCompare= (ImageView) findViewById(R.id.ivOperatorCompare);
		ivOperatorNetworkKpi= (ImageView) findViewById(R.id.ivOperatorNetworkKpi);
		ivOperatorIcon= (ImageView) findViewById(R.id.ivOperatorIcon);
		ivDown_Link_Speed= (ImageView) findViewById(R.id.ivDown_Link_Speed);
		ivDataCoverage= (ImageView) findViewById(R.id.ivDataCoverage);
		ivAppCoverage= (ImageView) findViewById(R.id.ivAppCoverage);
		
		ivOperatorSWMI= (ImageView) findViewById(R.id.ivOperatorSWMI);
		ivOperatorSummary= (ImageView) findViewById(R.id.ivOperatorSummary);
		
		tv3G_call_Setup_success_rate.setOnClickListener(this);
		ivOperatorCompare.setOnClickListener(this);
		ivOperatorNetworkKpi.setOnClickListener(this);
		ivOperatorSWMI.setOnClickListener(this);
		ivOperatorSummary.setOnClickListener(this);
		tv3G_Drop_Rate.setOnClickListener(this);
		tv2G_call_Setup_success_rate.setOnClickListener(this);
		tv2G_Drop_Rate.setOnClickListener(this);
		tv3G_on_2G.setOnClickListener(this);
		tvEDGE_performance.setOnClickListener(this);
		tvGB_per_Subscriber.setOnClickListener(this);
		tvDataCityWiseComparision.setOnClickListener(this);
		
		ivDown_Link_Speed.setOnClickListener(this);
		ivDataCoverage.setOnClickListener(this);
		ivAppCoverage.setOnClickListener(this);
		
		

		wv = (WebView) findViewById(R.id.wvIndividual);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlVoice_happiness) {
			rlVoice_happiness.setBackgroundColor(getResources().getColor(
					R.color.tab_bg_selected));
			rlData_happiness.setBackgroundColor(getResources().getColor(
					R.color.tab_bg_normal));
			llVoice.setVisibility(LinearLayout.VISIBLE);
			llData.setVisibility(LinearLayout.GONE);
			
			tv3G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			
			showGraph(CommonValues.getInstance().SelectedGraphItem = "Voice Happiness index");
		} else if (view.getId() == R.id.rlData_happiness) {
			rlData_happiness.setBackgroundColor(getResources().getColor(
					R.color.tab_bg_selected));
			rlVoice_happiness.setBackgroundColor(getResources().getColor(
					R.color.tab_bg_normal));
			llVoice.setVisibility(LinearLayout.GONE);
			llData.setVisibility(LinearLayout.VISIBLE);
			
			tvDown_Link_Speed.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_on_2G.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvEDGE_performance.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvGB_per_Subscriber.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "Data Happiness index");
		}  else if (view.getId() == R.id.tv3G_call_Setup_success_rate) {
			tv3G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tv3G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "3G call Setup success rate");
		} else if (view.getId() == R.id.tv3G_Drop_Rate) {
			tv3G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tv2G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "3G Drop Rate");
		} else if (view.getId() == R.id.tv2G_call_Setup_success_rate) {
			tv3G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tv2G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "2G call Setup success rate");
		} else if (view.getId() == R.id.tv2G_Drop_Rate) {
			tv3G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_call_Setup_success_rate.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv2G_Drop_Rate.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "2G Drop Rate");
		}else if (view.getId() == R.id.tvDown_Link_Speed) {
			tvDown_Link_Speed.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tv3G_on_2G.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvEDGE_performance.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvGB_per_Subscriber.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			Intent intent = new Intent(this,
					LeadershipNetworkKPIDownloadActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (view.getId() == R.id.tv3G_on_2G) {
			tvDown_Link_Speed.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_on_2G.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tvEDGE_performance.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvGB_per_Subscriber.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "3G on 2G");
		} else if (view.getId() == R.id.tvEDGE_performance) {
			tvDown_Link_Speed.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_on_2G.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvEDGE_performance.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tvGB_per_Subscriber.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "EDGE performance");
		} else if (view.getId() == R.id.tvGB_per_Subscriber) {
			tvDown_Link_Speed.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tv3G_on_2G.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvEDGE_performance.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvGB_per_Subscriber.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			showGraph(CommonValues.getInstance().SelectedGraphItem = "GB per Subscriber");
		}else if (view.getId() == R.id.ivOperatorCompare) {
			LeadershipOperatorCompareActivity.ReportType="Network KPI";
			Intent intent = new Intent(this, LeadershipOperatorCompareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.ivOperatorNetworkKpi){
			Intent intent = new Intent(this, LeadershipFinanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.tvDataCityWiseComparision){
			Intent intent = new Intent(this, CityListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.ivDown_Link_Speed){
			Intent intent = new Intent(this,LeadershipNetworkKPIDownloadActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.ivDataCoverage){
			showGraph(CommonValues.getInstance().SelectedGraphItem = "3G on 2G");
		}else if(view.getId() == R.id.ivAppCoverage){
			AppbaseReportActivity.isCompare=false;
			Intent intent = new Intent(this,AppbaseReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(view.getId() == R.id.ivOperatorSWMI){
			AppbaseReportActivity.isCompare=false;
			Intent intent = new Intent(this,LeadershipSWMIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(view.getId() == R.id.ivOperatorSummary){
			AppbaseReportActivity.isCompare=false;
			Intent intent = new Intent(this,LeadershipSummaryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	private void showGraph(String type) {
		wv.getSettings().setBuiltInZoomControls(true);

		String URL = "http://120.146.188.232:9050/TechnicalKPI.aspx?reqd="
				+ CommonValues.getInstance().SelectedCompany.CompanyID + ","
				+ type;

		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(CommonTask.getScale(this));
		wv.loadUrl(URL);
	}
}
