package com.vipdashboard.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;

public class DashboradServiceActivity extends MainActionbarBase implements
		OnClickListener,IAsynchronousTask {

	RelativeLayout rlDashboardServiceVoice, rlDashboardServiceData,
			rlDashboardServiceSMS, rlDashboardServiceCorporate,rlDashboardReportManagement,rlDashboardReportTechnical;
	/*ImageView bDashboardServiceValue1, bDashboardServiceValue2,
			bDashboardServiceValue3, bDashboardServiceValue4;*/

	TextView tvDashboardServiceValue1Title, tvDashboardServiceValue2Title,
			tvDashboardServiceValue3Title, tvDashboardServiceValue1Value,
			tvDashboardServiceValue2Value, tvDashboardServiceValue3Value,
			tvDashboardServiceSubTitle;
	
	GaugeView gvDeshboardServiceVoice, gvDeshboradServiceData, gvDeshboardServiceSMS, 
			gvDeshboardServiceCorporate, bDashboardServiceValue1, bDashboardServiceValue2,
			bDashboardServiceValue3;
	TextView tvDeshboardServiceVoiceValue, tvDeshboardDataValue, tvDeshboardServiceSMSValue,
			tvDeshboardServiceCorporateValue;
	
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	
	public static PMKPIDatas pMKPIDatas;

	public static int selectedTabIndex = 0;
	
	boolean isCallFromDashboardServiceVoice,isCallFromDashboardServiceData,
	isCallFromDashboardServiceSMS, isCallFromDashboardServiceCorporate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_service);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		initialization();
	}

	private void initialization() {		
		progressBar = (ProgressBar) findViewById(R.id.pbDeshboardService);
		
		rlDashboardServiceVoice = (RelativeLayout) findViewById(R.id.rlDashboardServiceVoice);
		rlDashboardServiceData = (RelativeLayout) findViewById(R.id.rlDashboardServiceData);
		rlDashboardServiceSMS = (RelativeLayout) findViewById(R.id.rlDashboardServiceSMS);
		rlDashboardServiceCorporate = (RelativeLayout) findViewById(R.id.rlDashboardServiceCorporate);
		rlDashboardReportManagement = (RelativeLayout) findViewById(R.id.rlDashboardServiceReportManagement);
		rlDashboardReportTechnical = (RelativeLayout) findViewById(R.id.rlDashboardServiceReportTechnical);

		bDashboardServiceValue1 = (GaugeView) findViewById(R.id.bDashboardServiceValue1);
		bDashboardServiceValue2 = (GaugeView) findViewById(R.id.bDashboardServiceValue2);
		bDashboardServiceValue3 = (GaugeView) findViewById(R.id.bDashboardServiceValue3);
		
		gvDeshboardServiceVoice = (GaugeView) findViewById(R.id.bDashboardServiceVoice);
		gvDeshboradServiceData = (GaugeView) findViewById(R.id.bDashboardServiceData);
		gvDeshboardServiceSMS = (GaugeView) findViewById(R.id.bDashboardServiceSMS);
		gvDeshboardServiceCorporate = (GaugeView) findViewById(R.id.bDashboardServiceCorporate);
		
		tvDeshboardServiceVoiceValue = (TextView) findViewById(R.id.tvDashboardServiceVoiceValue);
		tvDeshboardDataValue = (TextView) findViewById(R.id.tvDashboardServiceDataValue);
		tvDeshboardServiceSMSValue = (TextView) findViewById(R.id.tvDashboardServiceSMSValue);
		tvDeshboardServiceCorporateValue = (TextView) findViewById(R.id.tvDashboardServiceCorporateValue);

		tvDashboardServiceValue1Title = (TextView) findViewById(R.id.tvDashboardServiceValue1Title);
		tvDashboardServiceValue2Title = (TextView) findViewById(R.id.tvDashboardServiceValue2Title);
		tvDashboardServiceValue3Title = (TextView) findViewById(R.id.tvDashboardServiceValue3Title);

		tvDashboardServiceValue1Value = (TextView) findViewById(R.id.tvDashboardServiceValue1Value);
		tvDashboardServiceValue2Value = (TextView) findViewById(R.id.tvDashboardServiceValue2Value);
		tvDashboardServiceValue3Value = (TextView) findViewById(R.id.tvDashboardServiceValue3Value);

		tvDashboardServiceSubTitle = (TextView) findViewById(R.id.tvDashboardServiceSubTitle);

		rlDashboardServiceVoice.setOnClickListener(this);
		rlDashboardServiceData.setOnClickListener(this);
		rlDashboardServiceSMS.setOnClickListener(this);
		rlDashboardServiceCorporate.setOnClickListener(this);
		
		rlDashboardReportManagement.setOnClickListener(this);
		rlDashboardReportTechnical.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlDashboardServiceVoice) {
			arrangeVoiceData();
		} else if (v.getId() == R.id.rlDashboardServiceData) {
			arrangeDataData();
		} else if (v.getId() == R.id.rlDashboardServiceSMS) {
			arrangeSMSData();
		} else if (v.getId() == R.id.rlDashboardServiceCorporate) {
			arrangeCorporateData();
		}else if (v.getId() == R.id.rlDashboardServiceReportManagement) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (v.getId() == R.id.rlDashboardServiceReportTechnical) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}

	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		isCallFromDashboardServiceVoice = isCallFromDashboardServiceData = isCallFromDashboardServiceSMS = isCallFromDashboardServiceCorporate = false;
		setGaugeViewDefaultData();
		arrangeTab();
		
	}

	private void setGaugeViewDefaultData() {
		if(pMKPIDatas!=null &&  pMKPIDatas.PMKPIDatas.size()>0){
			gvDeshboardServiceVoice.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(0).KPIValue);
			tvDeshboardServiceVoiceValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(0).KPIValue));
			
			gvDeshboradServiceData.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(1).KPIValue);
			tvDeshboardDataValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(1).KPIValue));
			
			gvDeshboardServiceSMS.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);
			tvDeshboardServiceSMSValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
			
			gvDeshboardServiceCorporate.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(3).KPIValue);
			tvDeshboardServiceCorporateValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(3).KPIValue));
		}else{
			gvDeshboardServiceVoice.setTargetValue(0);
			tvDeshboardServiceVoiceValue.setText("0");
			
			gvDeshboradServiceData.setTargetValue(0);
			tvDeshboardDataValue.setText("0");
			
			gvDeshboardServiceSMS.setTargetValue(0);
			tvDeshboardServiceSMSValue.setText("0");
			
			gvDeshboardServiceCorporate.setTargetValue(0);
			tvDeshboardServiceCorporateValue.setText("0");
		}
	}

	private void arrangeTab() {
		if (selectedTabIndex == 0){
			arrangeVoiceData();	
		}
		else if (selectedTabIndex == 1){
			arrangeDataData();
		}
		else if (selectedTabIndex == 2){
			arrangeSMSData();
		}
		else if (selectedTabIndex == 3){
			arrangeCorporateData();
		}
	}

	private void arrangeVoiceData() {
		rlDashboardServiceVoice.setBackgroundColor(getResources()
				.getColor(R.color.header_text));
		rlDashboardServiceData.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceSMS.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceCorporate.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));

		tvDashboardServiceSubTitle.setText("Voice");

		//bDashboardServiceValue1.setImageResource(R.drawable.speedo_100);
		tvDashboardServiceValue1Title.setText("Accessibility");
		//tvDashboardServiceValue1Value.setText("100");

		//bDashboardServiceValue2.setImageResource(R.drawable.speedo_91);
		tvDashboardServiceValue2Title.setText("Retainability");
		//tvDashboardServiceValue2Value.setText("91");

		//bDashboardServiceValue3.setImageResource(R.drawable.speedo_96);
		tvDashboardServiceValue3Title.setText("Integrity");
		//tvDashboardServiceValue3Value.setText("96");	
		isCallFromDashboardServiceVoice = true;
		LoadInformation();
	}

	private void arrangeDataData() {
		rlDashboardServiceVoice.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceData.setBackgroundColor(getResources()
				.getColor(R.color.header_text));
		rlDashboardServiceSMS.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceCorporate.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));

		tvDashboardServiceSubTitle.setText("Data");

		//bDashboardServiceValue1.setImageResource(R.drawable.speedo_67);
		tvDashboardServiceValue1Title.setText("Accessibility");
		//tvDashboardServiceValue1Value.setText("67");

		//bDashboardServiceValue2.setImageResource(R.drawable.speedo_88);
		tvDashboardServiceValue2Title.setText("Retainability");
		//tvDashboardServiceValue2Value.setText("88");

		//bDashboardServiceValue3.setImageResource(R.drawable.speedo_80);
		tvDashboardServiceValue3Title.setText("Integrity");
		//tvDashboardServiceValue3Value.setText("80");
		isCallFromDashboardServiceData = true;
		LoadInformation();
	}

	private void arrangeSMSData() {
		rlDashboardServiceVoice.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceData.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceSMS.setBackgroundColor(getResources()
				.getColor(R.color.header_text));
		rlDashboardServiceCorporate.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));

		tvDashboardServiceSubTitle.setText("SMS");

		//bDashboardServiceValue1.setImageResource(R.drawable.speedo_77);
		tvDashboardServiceValue1Title.setText("Accessibility");
		//tvDashboardServiceValue1Value.setText("77");

		//bDashboardServiceValue2.setImageResource(R.drawable.speedo_68);
		tvDashboardServiceValue2Title.setText("Retainability");
		//tvDashboardServiceValue2Value.setText("68");

		//bDashboardServiceValue3.setImageResource(R.drawable.speedo_90);
		tvDashboardServiceValue3Title.setText("Integrity");
		//tvDashboardServiceValue3Value.setText("90");
		isCallFromDashboardServiceSMS = true;
		LoadInformation();
	}
	
	private void arrangeCorporateData() {
		rlDashboardServiceVoice.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceData.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceSMS.setBackgroundColor(getResources()
				.getColor(R.color.home_item_bg));
		rlDashboardServiceCorporate.setBackgroundColor(getResources()
				.getColor(R.color.header_text));

		tvDashboardServiceSubTitle.setText("Corporate");

		//bDashboardServiceValue1.setImageResource(R.drawable.speedo_99);
		tvDashboardServiceValue1Title.setText("Accessibility");
		//tvDashboardServiceValue1Value.setText("99");

		//bDashboardServiceValue2.setImageResource(R.drawable.speedo_55);
		tvDashboardServiceValue2Title.setText("Retainability");
		//tvDashboardServiceValue2Value.setText("55");

		//bDashboardServiceValue3.setImageResource(R.drawable.speedo_95);
		tvDashboardServiceValue3Title.setText("Integrity");
		//tvDashboardServiceValue3Value.setText("95");
		isCallFromDashboardServiceCorporate = true;
		LoadInformation();
	}
	
	private void LoadInformation() {
		if(downloadableAsyncTask != null){
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		//progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//progressBar.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IStatisticsReportManager manager=new StatisticsReportManager();
		return manager.getPMKPIDatas(2);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			PMKPIDatas pMKPIDatas=(PMKPIDatas)data;
			if(pMKPIDatas != null && pMKPIDatas.PMKPIDatas.size()>0){
				if(isCallFromDashboardServiceVoice){
					isCallFromDashboardServiceVoice = false;
					for(int i=0;i<pMKPIDatas.PMKPIDatas.size();i++){
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Voice Accessibility")){
							bDashboardServiceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Voice Retainability")){
							bDashboardServiceValue2.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Voice Integrity")){
							bDashboardServiceValue3.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
					}
				}else if(isCallFromDashboardServiceData){
					isCallFromDashboardServiceData = false;
					for(int i=0;i<pMKPIDatas.PMKPIDatas.size();i++){
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Data Accessibility")){
							bDashboardServiceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Data Retainability")){
							bDashboardServiceValue2.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Data Integrity")){
							bDashboardServiceValue3.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
					}
				}else if(isCallFromDashboardServiceSMS){
					isCallFromDashboardServiceSMS = false;
					for(int i=0;i<pMKPIDatas.PMKPIDatas.size();i++){
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("SMS Accessibility")){
							bDashboardServiceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("SMS Retainability")){
							bDashboardServiceValue2.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("SMS Integrity")){
							bDashboardServiceValue3.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
					}
				}else if(isCallFromDashboardServiceCorporate){
					isCallFromDashboardServiceCorporate = false;
					for(int i=0;i<pMKPIDatas.PMKPIDatas.size();i++){
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Corporate Accessibility")){
							bDashboardServiceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Corporate Retainability")){
							bDashboardServiceValue2.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Corporate Integrity")){
							bDashboardServiceValue3.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardServiceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
					}
				}				
			}
		}
	}
}
