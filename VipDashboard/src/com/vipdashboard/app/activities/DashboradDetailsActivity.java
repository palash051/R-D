package com.vipdashboard.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.internal.aq;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;

public class DashboradDetailsActivity extends MainActionbarBase implements
		OnClickListener,IAsynchronousTask {

	RelativeLayout rlDashboardExperienceAcquisition,
			rlDashboardExperienceRetention, rlDashboardExperienceEfficiency,rlDashboardExperienceValue4,rlDashboardReportManagement,rlDashboardReportTechnical;
	GaugeView gvDashboardExperienceAcquisition,gvDashboardExperienceRetention,gvDashboardExperienceEfficiency,gvbDashboardExperienceValue1, gvbDashboardExperienceValue2,
	gvbDashboardExperienceValue3, gvbDashboardExperienceValue4;
	
	TextView tvDashboardAcquisitionValue,tvDashboardRetentionValue,tvDashboardEfficiencyValue; 

	TextView tvDashboardExperienceValue1Title,
			tvDashboardExperienceValue2Title, tvDashboardExperienceValue3Title,
			tvDashboardExperienceValue4Title,tvDashboardExperienceValue1Value,tvDashboardExperienceValue2Value,tvDashboardExperienceValue3Value,tvDashboardExperienceValue4Value,tvDashboardExperienceExperienceSubTitle;

	boolean isCallFromAcquisition, isCallFromRetention,isCallFromEfficiency;
	public static int selectedTabIndex=0;
	public static PMKPIDatas pMKPIDatas;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	int acquisition_ID=2, retantion_ID = 3, efficency_ID=4;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_experience);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		initialization();
	}

	private void initialization() {
		progressBar = (ProgressBar) findViewById(R.id.pbDashboradDetails);
		rlDashboardExperienceAcquisition = (RelativeLayout) findViewById(R.id.rlDashboardExperienceAcquisition);
		rlDashboardExperienceRetention = (RelativeLayout) findViewById(R.id.rlDashboardExperienceRetention);
		rlDashboardExperienceEfficiency = (RelativeLayout) findViewById(R.id.rlDashboardExperienceEfficiency);
		rlDashboardReportManagement = (RelativeLayout) findViewById(R.id.rlDashboardReportManagement);
		rlDashboardReportTechnical = (RelativeLayout) findViewById(R.id.rlDashboardReportTechnical);
		
		tvDashboardAcquisitionValue = (TextView) findViewById(R.id.tvDashboardExperienceAcquisitionValue);
		tvDashboardRetentionValue = (TextView) findViewById(R.id.tvDashboardExperienceRetentionValue);
		tvDashboardEfficiencyValue = (TextView) findViewById(R.id.tvDashboardExperienceEfficiencyValue);
		
		rlDashboardExperienceValue4= (RelativeLayout) findViewById(R.id.rlDashboardExperienceValue4);

		gvDashboardExperienceAcquisition= (GaugeView) findViewById(R.id.gvDashboardExperienceAcquisition);
		gvDashboardExperienceRetention= (GaugeView) findViewById(R.id.gvDashboardExperienceRetention);
		gvDashboardExperienceEfficiency= (GaugeView) findViewById(R.id.gvDashboardExperienceEfficiency);
		gvbDashboardExperienceValue1 = (GaugeView) findViewById(R.id.gvbDashboardExperienceValue1);
		gvbDashboardExperienceValue2 = (GaugeView) findViewById(R.id.gvbDashboardExperienceValue2);
		gvbDashboardExperienceValue3 = (GaugeView) findViewById(R.id.gvbDashboardExperienceValue3);
		gvbDashboardExperienceValue4 = (GaugeView) findViewById(R.id.gvbDashboardExperienceValue4);
		
		

		tvDashboardExperienceValue1Title = (TextView) findViewById(R.id.tvDashboardExperienceValue1Title);
		tvDashboardExperienceValue2Title = (TextView) findViewById(R.id.tvDashboardExperienceValue2Title);
		tvDashboardExperienceValue3Title = (TextView) findViewById(R.id.tvDashboardExperienceValue3Title);
		tvDashboardExperienceValue4Title = (TextView) findViewById(R.id.tvDashboardExperienceValue4Title);
		
		tvDashboardExperienceValue1Value= (TextView) findViewById(R.id.tvDashboardExperienceValue1Value);
		tvDashboardExperienceValue2Value= (TextView) findViewById(R.id.tvDashboardExperienceValue2Value);
		tvDashboardExperienceValue3Value= (TextView) findViewById(R.id.tvDashboardExperienceValue3Value);
		tvDashboardExperienceValue4Value= (TextView) findViewById(R.id.tvDashboardExperienceValue4Value);
		
		tvDashboardExperienceExperienceSubTitle= (TextView) findViewById(R.id.tvDashboardExperienceExperienceSubTitle);
		

		rlDashboardExperienceAcquisition.setOnClickListener(this);
		rlDashboardExperienceRetention.setOnClickListener(this);
		rlDashboardExperienceEfficiency.setOnClickListener(this);
		
		rlDashboardReportManagement.setOnClickListener(this);
		rlDashboardReportTechnical.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlDashboardExperienceAcquisition) {
			arrangeAcquisitionData();
		}else if (v.getId() == R.id.rlDashboardExperienceRetention) {
			arrangeRetentionData();
		}else if (v.getId() == R.id.rlDashboardExperienceEfficiency) {
			arrangeEfficiencyData();
		}else if (v.getId() == R.id.rlDashboardReportManagement) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (v.getId() == R.id.rlDashboardReportTechnical) {
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
		//gvDashboardExperienceAcquisition.setTargetValue(89);
		//gvDashboardExperienceRetention.setTargetValue(92);
		//gvDashboardExperienceEfficiency.setTargetValue(89);
		acquisition_ID=2; retantion_ID = 3; efficency_ID=4;
		isCallFromAcquisition = isCallFromRetention = isCallFromEfficiency = false;
		setGaugeViewDefaultData();
		arrangeTab();
		
	}

	private void setGaugeViewDefaultData() {
		if(pMKPIDatas != null && pMKPIDatas.PMKPIDatas.size()>0){				
			gvDashboardExperienceAcquisition.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(0).KPIValue);
			tvDashboardAcquisitionValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(0).KPIValue));
			gvDashboardExperienceRetention.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(1).KPIValue);
			tvDashboardRetentionValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(1).KPIValue));
			gvDashboardExperienceEfficiency.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);
			tvDashboardEfficiencyValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
		}else{
			gvDashboardExperienceAcquisition.setTargetValue(0);
			tvDashboardAcquisitionValue.setText("0");
			gvDashboardExperienceRetention.setTargetValue(0);
			tvDashboardRetentionValue.setText("0");
			gvDashboardExperienceEfficiency.setTargetValue(0);
			tvDashboardEfficiencyValue.setText("0");
		}
	}

	private void arrangeTab() {
		if(selectedTabIndex==0)
			arrangeAcquisitionData();
		else if(selectedTabIndex==1)
			arrangeRetentionData();
		else if(selectedTabIndex==2)
			arrangeEfficiencyData();
	}

	private void arrangeEfficiencyData() {
		rlDashboardExperienceAcquisition.setBackgroundColor(getResources().getColor(R.color.home_item_bg));
		rlDashboardExperienceRetention.setBackgroundColor(getResources().getColor(R.color.home_item_bg));
		rlDashboardExperienceEfficiency.setBackgroundColor(getResources().getColor(R.color.header_text));
		
		rlDashboardExperienceValue4.setVisibility(RelativeLayout.VISIBLE);
		
		tvDashboardExperienceExperienceSubTitle.setText("Efficiency");
		
		//gvbDashboardExperienceValue1.setTargetValue(97);
		tvDashboardExperienceValue1Title.setText("Cost of Acquisition");
		//tvDashboardExperienceValue1Value.setText("97");
		
		//gvbDashboardExperienceValue2.setTargetValue(91);
		tvDashboardExperienceValue2Title.setText("Cost of Retention");
		//tvDashboardExperienceValue2Value.setText("91");
		
		//gvbDashboardExperienceValue3.setTargetValue(96);
		tvDashboardExperienceValue3Title.setText("Self Service Rate");
		//tvDashboardExperienceValue3Value.setText("96");
		
		//gvbDashboardExperienceValue4.setTargetValue(93);
		tvDashboardExperienceValue4Title.setText("First Contact Resolution");
		//tvDashboardExperienceValue4Value.setText("93");
		isCallFromEfficiency = true;
		LoadInformation();
	}

	private void arrangeRetentionData() {
		rlDashboardExperienceAcquisition.setBackgroundColor(getResources().getColor(R.color.home_item_bg));
		rlDashboardExperienceRetention.setBackgroundColor(getResources().getColor(R.color.header_text));
		rlDashboardExperienceEfficiency.setBackgroundColor(getResources().getColor(R.color.home_item_bg));
		
		rlDashboardExperienceValue4.setVisibility(RelativeLayout.VISIBLE);
		
		tvDashboardExperienceExperienceSubTitle.setText("Retention");
		
		//gvbDashboardExperienceValue1.setTargetValue(90);
		tvDashboardExperienceValue1Title.setText("Service Performance");
		//tvDashboardExperienceValue1Value.setText("90");
		
		//gvbDashboardExperienceValue2.setTargetValue(98);
		tvDashboardExperienceValue2Title.setText("Mean Resolution Time");
		//tvDashboardExperienceValue2Value.setText("98");
		
		//gvbDashboardExperienceValue3.setTargetValue(99);
		tvDashboardExperienceValue3Title.setText("Customer Churn Rate");
		//tvDashboardExperienceValue3Value.setText("99");
		
		//gvbDashboardExperienceValue4.setTargetValue(92);
		tvDashboardExperienceValue4Title.setText("Net Promoter Score");
		//tvDashboardExperienceValue4Value.setText("92");
		isCallFromRetention = true;
		LoadInformation();
	}

	private void arrangeAcquisitionData() {
		rlDashboardExperienceAcquisition.setBackgroundColor(getResources().getColor(R.color.header_text));
		rlDashboardExperienceRetention.setBackgroundColor(getResources().getColor(R.color.home_item_bg));
		rlDashboardExperienceEfficiency.setBackgroundColor(getResources().getColor(R.color.home_item_bg));
		
		rlDashboardExperienceValue4.setVisibility(RelativeLayout.GONE);
		
		tvDashboardExperienceExperienceSubTitle.setText("Acquisition");
		
		//gvbDashboardExperienceValue1.setTargetValue(96);
		tvDashboardExperienceValue1Title.setText("Traffic");
		//tvDashboardExperienceValue1Value.setText("96");
		
		//gvbDashboardExperienceValue2.setTargetValue(93);
		tvDashboardExperienceValue2Title.setText("Social");
		//tvDashboardExperienceValue2Value.setText("93");
		
		//gvbDashboardExperienceValue3.setTargetValue(99);
		tvDashboardExperienceValue3Title.setText("ARPU");
		//tvDashboardExperienceValue3Value.setText("99");
		isCallFromAcquisition = true;
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
		if(isCallFromAcquisition){
			return manager.getParentPMKPIDatas(acquisition_ID);
		}else if(isCallFromRetention){
			return manager.getParentPMKPIDatas(retantion_ID);
		}else if(isCallFromEfficiency){
			return manager.getParentPMKPIDatas(efficency_ID);
		}else{
			return null;
		}
		
	}
	
	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			PMKPIDatas pMKPIData = (PMKPIDatas) data;
			if(pMKPIData != null && pMKPIData.PMKPIDatas.size() > 0){
				if(isCallFromAcquisition){
					isCallFromAcquisition = false;
					/*for(int i=0;i<pMKPIData.PMKPIDatas.size();i++){
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Traffic")){
							gvbDashboardExperienceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardExperienceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Social")){
							gvbDashboardExperienceValue2.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardExperienceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("ARPU")){
							gvbDashboardExperienceValue3.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);							
							tvDashboardExperienceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
					}*/
					if(pMKPIData.PMKPIDatas.get(0).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(0).KPIID){
						gvbDashboardExperienceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(0).KPIValue);
						tvDashboardExperienceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(0).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(1).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(1).KPIID){
						gvbDashboardExperienceValue2.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(1).KPIValue);
						tvDashboardExperienceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(1).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(2).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(2).KPIID){
						gvbDashboardExperienceValue3.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);							
						tvDashboardExperienceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
					}
				}else if(isCallFromRetention){
					isCallFromRetention = false;
					/*for(int i=0;i<pMKPIData.PMKPIDatas.size();i++){
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Service Performance\r\n")){
							gvbDashboardExperienceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardExperienceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Mean Resolution Time\r\n")){
							gvbDashboardExperienceValue2.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardExperienceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Customer Churn Rate\r\n")){
							gvbDashboardExperienceValue3.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);							
							tvDashboardExperienceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Net Promoter Score\r\n")){
							gvbDashboardExperienceValue4.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);							
							tvDashboardExperienceValue4Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
					}*/
					if(pMKPIData.PMKPIDatas.get(0).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(0).KPIID){
						gvbDashboardExperienceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(0).KPIValue);
						tvDashboardExperienceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(0).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(1).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(1).KPIID){
						gvbDashboardExperienceValue2.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(1).KPIValue);
						tvDashboardExperienceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(1).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(2).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(2).KPIID){
						gvbDashboardExperienceValue3.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);							
						tvDashboardExperienceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(3).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(3).KPIID){
						gvbDashboardExperienceValue4.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(3).KPIValue);							
						tvDashboardExperienceValue4Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(3).KPIValue));
					}
				}else if(isCallFromEfficiency){
					isCallFromEfficiency = false;
					/*for(int i=0;i<pMKPIData.PMKPIDatas.size();i++){
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Cost of Acquisition\r\n")){
							gvbDashboardExperienceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardExperienceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Cost of Retention\r\n")){
							gvbDashboardExperienceValue2.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
							tvDashboardExperienceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("Self Service Rate\r\n")){
							gvbDashboardExperienceValue3.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);							
							tvDashboardExperienceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
						if(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPI.equals("First Contact Resolution\r\n")){
							gvbDashboardExperienceValue4.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);							
							tvDashboardExperienceValue4Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						}
					}*/
					if(pMKPIData.PMKPIDatas.get(0).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(0).KPIID){
						gvbDashboardExperienceValue1.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(0).KPIValue);
						tvDashboardExperienceValue1Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(0).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(1).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(1).KPIID){
						gvbDashboardExperienceValue2.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(1).KPIValue);
						tvDashboardExperienceValue2Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(1).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(2).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(2).KPIID){
						gvbDashboardExperienceValue3.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);							
						tvDashboardExperienceValue3Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
					}
					if(pMKPIData.PMKPIDatas.get(3).PMKPI.KPIID == pMKPIData.PMKPIDatas.get(3).KPIID){
						gvbDashboardExperienceValue4.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(3).KPIValue);							
						tvDashboardExperienceValue4Value.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(3).KPIValue));
					}
				}
			}
		}
	}

}
