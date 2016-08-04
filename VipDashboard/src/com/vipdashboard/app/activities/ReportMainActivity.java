package com.vipdashboard.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;

public class ReportMainActivity extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask {
	RelativeLayout rlReportVoice, rlReportData, rlReportSMS, rlReportCorporate,
			rlReportManagementOptimization, rlReportManagementOperations,
			rlReportManagementDeployment, rlReportTechnicalOptimization,
			rlReportTechnicalOperations, rlReportTechnicalDeployment,			
			rlReportTechnicalOptimizationCSCore,
			rlReportTechnicalOptimizationPSCore,
			rlReportTechnicalOptimizationRAN,
			rlReportTechnicalOperationsVAS, rlReportTechnicalOperationsIN,
			rlReportTechnicalOperationsNOC_FOPS,			
			rlReportTechnicalDeploymentProjectStatus,
			rlReportTechnicalDeploymentCivilWork,
			rlReportTechnicalDeploymentInstallation,
			rlReportTechnicalDeploymentIntegration,
			rlReportTechnicalDeploymentTuning, rlReportTechnicalDeploymentSOA;
	
	LinearLayout rlReportTechnicalOptimizationValue,rlReportTechnicalDeploymentValue, rlReportTechnicalOperationsValue;
	
	TextView tvTechnicalOptimization,tvTechnicalOperations,tvTechnicalDeployment;
	
	TextView tvVoiceValue, tvDataValue, tvSMSValue, tvCorporateValue;
	
	GaugeView gvReportVoice, gvReportData, gvReportSMS, gvReportCorporate;
	
	DownloadableAsyncTask asyncTask;
	ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_main);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		initialization();
	}

	private void initialization() {
		progressBar = (ProgressBar) findViewById(R.id.pbTechnicalReport);
		
		rlReportVoice = (RelativeLayout) findViewById(R.id.rlReportVoice);
		rlReportData = (RelativeLayout) findViewById(R.id.rlReportData);
		rlReportSMS = (RelativeLayout) findViewById(R.id.rlReportSMS);
		rlReportCorporate = (RelativeLayout) findViewById(R.id.rlReportCorporate);
		
		gvReportVoice = (GaugeView) findViewById(R.id.bReportVoice);
		gvReportData = (GaugeView) findViewById(R.id.bReportData);
		gvReportSMS = (GaugeView) findViewById(R.id.bReportSMS);
		gvReportCorporate = (GaugeView) findViewById(R.id.bReportCorporate);
		
		tvVoiceValue = (TextView) findViewById(R.id.tvReportVoiceValue);
		tvDataValue = (TextView) findViewById(R.id.tvReportDataValue);
		tvSMSValue = (TextView) findViewById(R.id.tvReportSMSValue);
		tvCorporateValue = (TextView) findViewById(R.id.tvReportCorporateValue);
		
		rlReportManagementOptimization = (RelativeLayout) findViewById(R.id.rlReportManagementOptimization);
		rlReportManagementOperations = (RelativeLayout) findViewById(R.id.rlReportManagementOperations);
		rlReportManagementDeployment = (RelativeLayout) findViewById(R.id.rlReportManagementDeployment);
		rlReportTechnicalOptimization = (RelativeLayout) findViewById(R.id.rlReportTechnicalOptimization);
		rlReportTechnicalOperations = (RelativeLayout) findViewById(R.id.rlReportTechnicalOperations);
		rlReportTechnicalDeployment = (RelativeLayout) findViewById(R.id.rlReportTechnicalDeployment);
		
		rlReportTechnicalOptimizationCSCore = (RelativeLayout) findViewById(R.id.rlReportTechnicalOptimizationCSCore);
		rlReportTechnicalOptimizationPSCore = (RelativeLayout) findViewById(R.id.rlReportTechnicalOptimizationPSCore);
		rlReportTechnicalOptimizationRAN = (RelativeLayout) findViewById(R.id.rlReportTechnicalOptimizationRAN);
		
		rlReportTechnicalOperationsVAS = (RelativeLayout) findViewById(R.id.rlReportTechnicalOperationsVAS);
		rlReportTechnicalOperationsIN = (RelativeLayout) findViewById(R.id.rlReportTechnicalOperationsIN);
		rlReportTechnicalOperationsNOC_FOPS = (RelativeLayout) findViewById(R.id.rlReportTechnicalOperationsNOC_FOPS);
		
		rlReportTechnicalDeploymentProjectStatus = (RelativeLayout) findViewById(R.id.rlReportTechnicalDeploymentProjectStatus);
		rlReportTechnicalDeploymentCivilWork = (RelativeLayout) findViewById(R.id.rlReportTechnicalDeploymentCivilWork);
		rlReportTechnicalDeploymentInstallation = (RelativeLayout) findViewById(R.id.rlReportTechnicalDeploymentInstallation);
		rlReportTechnicalDeploymentIntegration = (RelativeLayout) findViewById(R.id.rlReportTechnicalDeploymentIntegration);
		rlReportTechnicalDeploymentTuning = (RelativeLayout) findViewById(R.id.rlReportTechnicalDeploymentTuning);
		rlReportTechnicalDeploymentSOA = (RelativeLayout) findViewById(R.id.rlReportTechnicalDeploymentSOA);

		rlReportTechnicalOptimizationValue = (LinearLayout) findViewById(R.id.rlReportTechnicalOptimizationValue);
		rlReportTechnicalOperationsValue = (LinearLayout) findViewById(R.id.rlReportTechnicalOperationsValue);
		rlReportTechnicalDeploymentValue = (LinearLayout) findViewById(R.id.rlReportTechnicalDeploymentValue);
		
		tvTechnicalOptimization= (TextView) findViewById(R.id.tvTechnicalOptimization);
		tvTechnicalOperations= (TextView) findViewById(R.id.tvTechnicalOperations);
		tvTechnicalDeployment= (TextView) findViewById(R.id.tvTechnicalDeployment);
		
		rlReportVoice.setOnClickListener(this);
		rlReportData.setOnClickListener(this);
		rlReportSMS.setOnClickListener(this);
		rlReportCorporate.setOnClickListener(this);
		rlReportManagementOptimization.setOnClickListener(this);
		rlReportManagementOperations.setOnClickListener(this);
		rlReportManagementDeployment.setOnClickListener(this);
		rlReportTechnicalOptimization.setOnClickListener(this);
		rlReportTechnicalOperations.setOnClickListener(this);
		rlReportTechnicalDeployment.setOnClickListener(this);		
		rlReportTechnicalOptimizationCSCore.setOnClickListener(this);
		rlReportTechnicalOptimizationPSCore.setOnClickListener(this);
		rlReportTechnicalOptimizationRAN.setOnClickListener(this);		
		rlReportTechnicalOperationsVAS.setOnClickListener(this);
		rlReportTechnicalOperationsIN.setOnClickListener(this);
		rlReportTechnicalOperationsNOC_FOPS.setOnClickListener(this);		
		rlReportTechnicalDeploymentProjectStatus.setOnClickListener(this);
		rlReportTechnicalDeploymentCivilWork.setOnClickListener(this);
		rlReportTechnicalDeploymentInstallation.setOnClickListener(this);
		rlReportTechnicalDeploymentIntegration.setOnClickListener(this);
		rlReportTechnicalDeploymentTuning.setOnClickListener(this);
		rlReportTechnicalDeploymentSOA.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		setGaugeViewDefaultData();
		arrangeOptimization();
		LoadInformation();
		super.onResume();
	}

	private void setGaugeViewDefaultData() {
		gvReportVoice.setTargetValue(0);
		tvVoiceValue.setText("0");
		
		gvReportData.setTargetValue(0);
		tvDataValue.setText("0");
		
		gvReportSMS.setTargetValue(0);
		tvSMSValue.setText("0");
		
		gvReportCorporate.setTargetValue(0);
		tvCorporateValue.setText("0");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlReportVoice) {
			DashboradServiceActivity.selectedTabIndex=0;
			Intent intent = new Intent(this,DashboradServiceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportData) {
			DashboradServiceActivity.selectedTabIndex=1;
			Intent intent = new Intent(this,DashboradServiceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportSMS) {
			DashboradServiceActivity.selectedTabIndex=2;
			Intent intent = new Intent(this,DashboradServiceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportCorporate) {
			DashboradServiceActivity.selectedTabIndex=3;
			Intent intent = new Intent(this,DashboradServiceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportManagementOptimization) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportManagementOperations) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportManagementDeployment) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalOptimization) {
			arrangeOptimization();			
		} else if (v.getId() == R.id.rlReportTechnicalOperations) {
			arrangeOperations();
		} else if (v.getId() == R.id.rlReportTechnicalDeployment) {
			arrangeDeploment();
		}  else if (v.getId() == R.id.rlReportTechnicalOptimizationCSCore) {
			ReportDetailsActivity.selectedReport=ReportDetailsActivity.OPTIMIZATION_CS_CORE;
			Intent intent = new Intent(this,ReportDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalOptimizationPSCore) {
			ReportDetailsActivity.selectedReport=ReportDetailsActivity.OPTIMIZATION_PS_CORE;
			Intent intent = new Intent(this,ReportDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalOptimizationRAN) {	
			ReportDetailsActivity.selectedReport=ReportDetailsActivity.OPTIMIZATION_RAN;
			Intent intent = new Intent(this,ReportDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalOperationsVAS) {
			ReportDetailsActivity.selectedReport=ReportDetailsActivity.OPERATION_VAS;
			Intent intent = new Intent(this,ReportDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalOperationsIN) {
			ReportDetailsActivity.selectedReport=ReportDetailsActivity.OPERATION_IN;
			Intent intent = new Intent(this,ReportDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalOperationsNOC_FOPS) {	
			ReportDetailsActivity.selectedReport=ReportDetailsActivity.OPERATION_NOC_FOPS;
			Intent intent = new Intent(this,ReportDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalDeploymentProjectStatus) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalDeploymentCivilWork) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalDeploymentInstallation) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalDeploymentIntegration) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalDeploymentTuning) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlReportTechnicalDeploymentSOA) {
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}

	}

	private void arrangeDeploment() {
		rlReportTechnicalOptimizationValue.setVisibility(LinearLayout.GONE); 
		rlReportTechnicalOperationsValue.setVisibility(LinearLayout.GONE); 
		rlReportTechnicalDeploymentValue.setVisibility(LinearLayout.VISIBLE); 			
		tvTechnicalOptimization.setBackgroundColor(getResources().getColor(R.color.value_text));
		tvTechnicalOperations.setBackgroundColor(getResources().getColor(R.color.value_text));
		tvTechnicalDeployment.setBackgroundColor(getResources().getColor(R.color.header_text));
	}

	private void arrangeOperations() {
		rlReportTechnicalOptimizationValue.setVisibility(LinearLayout.GONE); 
		rlReportTechnicalOperationsValue.setVisibility(LinearLayout.VISIBLE); 
		rlReportTechnicalDeploymentValue.setVisibility(LinearLayout.GONE); 			
		tvTechnicalOptimization.setBackgroundColor(getResources().getColor(R.color.value_text));
		tvTechnicalOperations.setBackgroundColor(getResources().getColor(R.color.header_text));
		tvTechnicalDeployment.setBackgroundColor(getResources().getColor(R.color.value_text));
	}

	private void arrangeOptimization() {
		rlReportTechnicalOptimizationValue.setVisibility(LinearLayout.VISIBLE); 
		rlReportTechnicalOperationsValue.setVisibility(LinearLayout.GONE); 
		rlReportTechnicalDeploymentValue.setVisibility(LinearLayout.GONE); 			
		tvTechnicalOptimization.setBackgroundColor(getResources().getColor(R.color.header_text));
		tvTechnicalOperations.setBackgroundColor(getResources().getColor(R.color.value_text));
		tvTechnicalDeployment.setBackgroundColor(getResources().getColor(R.color.value_text));
	}
	
	private void LoadInformation() {
		if(asyncTask != null){
			asyncTask.cancel(true);
		}
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
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
		if(data!=null){
			PMKPIDatas pMKPIDatas=(PMKPIDatas)data;
			DashboradServiceActivity.pMKPIDatas = pMKPIDatas;
			if(pMKPIDatas!=null &&  pMKPIDatas.PMKPIDatas.size()>0){
				gvReportVoice.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(0).KPIValue);
				tvVoiceValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(0).KPIValue));
				
				gvReportData.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(1).KPIValue);
				tvDataValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(1).KPIValue));
				
				gvReportSMS.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);
				tvSMSValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
				
				gvReportCorporate.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(3).KPIValue);
				tvCorporateValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(3).KPIValue));
			}
		}
	}	
}
