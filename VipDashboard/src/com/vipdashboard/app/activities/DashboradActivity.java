package com.vipdashboard.app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.MasterDataConstants;

public class DashboradActivity extends MainActionbarBase implements OnClickListener, IAsynchronousTask{

	RelativeLayout rlDashboardAcquisition,rlDashboardRetention,rlDashboardEfficiency,rlDashboardTraffic,rlDashboardMTR,rlDashboardSelfService;
	TextView tvDashboardMessage,tvDashboardAcquisitionValue,tvDashboardRetentionValue,tvDashboardEfficiencyValue,tvDashboardTrafficValue,tvDashboardMTRValue,tvDashboardSelfServiceValue;
	
	TextView tvDashboardFirstTitle,tvDashboardSecondTitle,tvDashboardThirdTitle,tvDashboardForthTitle,tvDashboardFifthTitle,tvDashboardSixthTitle;
	
	GaugeView gvDashboardAcquisition,gvDashboardRetention,gvDashboardEfficiency,gvDashboardTraffic,gvDashboardMTR,gvDashboardSelfService;
	RelativeLayout rlDashboardReportManagement, rlDashboardReportTechnical, rlDashboardTroubleTicket;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressBar;
	
	
	int overviewID= MasterDataConstants.NetworkPerformace.PLMN;
	TextView tvDeshboardReportTitle;
	TextView txtExperienceView;
	
	TextView tvPLMN,tvRegion,tvPlatinumCluster,tvSPISummary;
	
	LinearLayout llDashboardTab;
	
	boolean isVisibleRequired=false; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState= getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		
		 if(savedInstanceState!=null && savedInstanceState.containsKey("isVisibleRequired"))
		 {
			 isVisibleRequired=savedInstanceState.getBoolean("isVisibleRequired");
		 }
		 
		initialization();
		
	}
	
	private void BuildTitle() {
		
		 if(overviewID>0)
		    {
		    	tvDeshboardReportTitle = (TextView) findViewById(R.id.tvDeshboardReportTitle);
		    	txtExperienceView = (TextView) findViewById(R.id.txtExperienceView);
		    	
		    	switch(overviewID)
		    	{
		    	case MasterDataConstants.NetworkPerformace.PLMN:
			    	tvDeshboardReportTitle.setText("PLMN");
			    	txtExperienceView .setText("PLMN view");
			    	tvPLMN.setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
			    	tvRegion.setBackgroundResource(Color.TRANSPARENT);
			    	tvPlatinumCluster.setBackgroundResource(Color.TRANSPARENT);
			    	tvSPISummary.setBackgroundResource(Color.TRANSPARENT);
		    		break;
		    	case MasterDataConstants.NetworkPerformace.REGION:
		    		tvDeshboardReportTitle.setText("REGION");
			    	txtExperienceView .setText("Region view");
			    	tvRegion.setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
			    	tvPLMN.setBackgroundResource(Color.TRANSPARENT);
			    	tvPlatinumCluster.setBackgroundResource(Color.TRANSPARENT);
			    	tvSPISummary.setBackgroundResource(Color.TRANSPARENT);
		    		break;
		    	case MasterDataConstants.NetworkPerformace.PLATINUM_CLUSTER:
		    		tvDeshboardReportTitle.setText("PLATINUM CLUSTER");
			    	txtExperienceView .setText("Platinum Cluster view");
			    	tvPlatinumCluster.setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
			    	tvRegion.setBackgroundResource(Color.TRANSPARENT);
			    	tvPLMN.setBackgroundResource(Color.TRANSPARENT);
			    	tvSPISummary.setBackgroundResource(Color.TRANSPARENT);
		    		break;
		    	case MasterDataConstants.NetworkPerformace.SPI_SUMMARY:
		    		tvDeshboardReportTitle.setText("SPI Summary");
			    	txtExperienceView .setText("SPI Summary view");
			    	tvSPISummary.setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
			    	tvRegion.setBackgroundResource(Color.TRANSPARENT);
			    	tvPLMN.setBackgroundResource(Color.TRANSPARENT);
			    	tvPlatinumCluster.setBackgroundResource(Color.TRANSPARENT);
		    		break;
		    	}
		    }
	}
	

	private void initialization() {
		
		rlDashboardReportManagement = (RelativeLayout) findViewById(R.id.rlDashboardReportManagement);
		rlDashboardReportTechnical = (RelativeLayout) findViewById(R.id.rlDashboardReportTechnical);
		rlDashboardTroubleTicket = (RelativeLayout) findViewById(R.id.rlDashboardTroubleTicket);
		
		rlDashboardAcquisition= (RelativeLayout) findViewById(R.id.rlDashboardAcquisition);
		rlDashboardRetention= (RelativeLayout) findViewById(R.id.rlDashboardRetention);
		rlDashboardEfficiency= (RelativeLayout) findViewById(R.id.rlDashboardEfficiency);
		rlDashboardTraffic= (RelativeLayout) findViewById(R.id.rlDashboardTraffic);
		rlDashboardMTR= (RelativeLayout) findViewById(R.id.rlDashboardMTR);
		rlDashboardSelfService= (RelativeLayout) findViewById(R.id.rlDashboardSelfService);
		
		gvDashboardAcquisition=(GaugeView)findViewById(R.id.gvDashboardAcquisition);		
		gvDashboardRetention=(GaugeView)findViewById(R.id.gvDashboardRetention);
		gvDashboardEfficiency=(GaugeView)findViewById(R.id.gvDashboardEfficiency);
		gvDashboardTraffic=(GaugeView)findViewById(R.id.gvDashboardTraffic);
		gvDashboardMTR=(GaugeView)findViewById(R.id.gvDashboardMTR);
		gvDashboardSelfService=(GaugeView)findViewById(R.id.gvDashboardSelfService);
		
		tvDashboardMessage= (TextView) findViewById(R.id.tvDashboardMessage);
		tvDashboardAcquisitionValue=(TextView)findViewById(R.id.tvDashboardAcquisitionValue);	
		tvDashboardRetentionValue=(TextView)findViewById(R.id.tvDashboardRetentionValue);	
		tvDashboardEfficiencyValue=(TextView)findViewById(R.id.tvDashboardEfficiencyValue);	
		tvDashboardTrafficValue=(TextView)findViewById(R.id.tvDashboardTrafficValue);	
		tvDashboardMTRValue=(TextView)findViewById(R.id.tvDashboardMTRValue);	
		tvDashboardSelfServiceValue=(TextView)findViewById(R.id.tvDashboardSelfServiceValue);
		
		
		tvDashboardFirstTitle= (TextView) findViewById(R.id.tvDashboardFirstTitle);
		tvDashboardSecondTitle=(TextView)findViewById(R.id.tvDashboardSecondTitle);	
		tvDashboardThirdTitle=(TextView)findViewById(R.id.tvDashboardThirdTitle);	
		tvDashboardForthTitle=(TextView)findViewById(R.id.tvDashboardForthTitle);	
		tvDashboardFifthTitle=(TextView)findViewById(R.id.tvDashboardFifthTitle);	
		tvDashboardSixthTitle=(TextView)findViewById(R.id.tvDashboardSixthTitle);
		
		tvPLMN=(TextView)findViewById(R.id.tvPLMN);
		tvRegion=(TextView)findViewById(R.id.tvRegion);
		tvPlatinumCluster=(TextView)findViewById(R.id.tvPlatinumCluster);
		tvSPISummary=(TextView)findViewById(R.id.tvSPISummary);
		llDashboardTab= (LinearLayout) findViewById(R.id.llDashboardTab);
		
		rlDashboardReportManagement.setOnClickListener(this);
		rlDashboardReportTechnical.setOnClickListener(this);
		rlDashboardTroubleTicket.setOnClickListener(this);
		
		rlDashboardAcquisition.setOnClickListener(this);
		rlDashboardRetention.setOnClickListener(this);
		rlDashboardEfficiency.setOnClickListener(this);
		rlDashboardTraffic.setOnClickListener(this);
		rlDashboardMTR.setOnClickListener(this);
		rlDashboardSelfService.setOnClickListener(this);
		
		
		tvPLMN.setOnClickListener(this);
		tvRegion.setOnClickListener(this);
		tvPlatinumCluster.setOnClickListener(this);
		tvSPISummary.setOnClickListener(this);
		
		if(isVisibleRequired)
		{	
			overviewID=MasterDataConstants.NetworkPerformace.SPI_SUMMARY;
		
		}
		
		BuildTitle();
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
	  setGaugeViewDefaultData();
	  tvDashboardMessage.setText("Update as of "+CommonTask.getCurrentDateTimeAsString());
	  LoadInformation();
	 }

	private void setGaugeViewDefaultData() {
		gvDashboardAcquisition.setTargetValue(0);
		tvDashboardAcquisitionValue.setText("0");
		
		gvDashboardRetention.setTargetValue(0);
		tvDashboardRetentionValue.setText("0");
		
		gvDashboardEfficiency.setTargetValue(0);
		tvDashboardEfficiencyValue.setText("0");
		
		gvDashboardTraffic.setTargetValue(0);
		tvDashboardTrafficValue.setText("0");
		
		gvDashboardMTR.setTargetValue(0);
		tvDashboardMTRValue.setText("0");
		
		gvDashboardSelfService.setTargetValue(0);
		tvDashboardSelfServiceValue.setText("0");
		
		BuildVisibility();
	}
	
	private void BuildVisibility() {
		if (overviewID== MasterDataConstants.NetworkPerformace.SPI_SUMMARY)
		{
			rlDashboardMTR.setVisibility(View.GONE);
			rlDashboardSelfService.setVisibility(View.GONE);
			llDashboardTab.setVisibility(View.GONE);
		}
		else
		{
			rlDashboardMTR.setVisibility(View.VISIBLE);
			rlDashboardSelfService.setVisibility(View.VISIBLE);
			llDashboardTab.setVisibility(View.VISIBLE);
		}
		
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		
		if(id == R.id.rlDashboardAcquisition){
			Intent intent = new Intent(this,VIPDNetworkPerformanceLinechart.class);
			intent.putExtra("overviewID", overviewID);
			intent.putExtra("overviewLineChartText",tvDashboardFirstTitle.getText()+" historical drill down");
			intent.putExtra("overviewTagID", rlDashboardAcquisition.getTag().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.rlDashboardRetention){
			Intent intent = new Intent(this,VIPDNetworkPerformanceLinechart.class);
			intent.putExtra("overviewID", overviewID);
			intent.putExtra("overviewLineChartText",tvDashboardSecondTitle.getText()+ " historical drill down");
			intent.putExtra("overviewTagID", rlDashboardRetention.getTag().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.rlDashboardEfficiency){
			Intent intent = new Intent(this,VIPDNetworkPerformanceLinechart.class);
			intent.putExtra("overviewID", overviewID);
			intent.putExtra("overviewLineChartText",tvDashboardThirdTitle.getText()+  " historical drill down");
			intent.putExtra("overviewTagID", rlDashboardEfficiency.getTag().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.rlDashboardTraffic){
			Intent intent = new Intent(this,VIPDNetworkPerformanceLinechart.class);
			intent.putExtra("overviewID", overviewID);
			intent.putExtra("overviewLineChartText", tvDashboardForthTitle.getText()+ " historical drill down");
			intent.putExtra("overviewTagID", rlDashboardTraffic.getTag().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.rlDashboardMTR){
			Intent intent = new Intent(this,VIPDNetworkPerformanceLinechart.class);
			intent.putExtra("overviewID", overviewID);
			intent.putExtra("overviewLineChartText", tvDashboardFifthTitle.getText()+ " historical drill down");
			intent.putExtra("overviewTagID", rlDashboardMTR.getTag().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.rlDashboardSelfService){
			Intent intent = new Intent(this,VIPDNetworkPerformanceLinechart.class);
			intent.putExtra("overviewID", overviewID);
			intent.putExtra("overviewLineChartText", tvDashboardSixthTitle.getText()+ " historical drill down");
			intent.putExtra("overviewTagID", rlDashboardSelfService.getTag().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(id == R.id.tvPLMN){
		    overviewID=MasterDataConstants.NetworkPerformace.PLMN;
			LoadInformation();
			BuildTitle();
			BuildVisibility();//here
		}
		else if(id == R.id.tvRegion){
			overviewID=MasterDataConstants.NetworkPerformace.REGION;
			LoadInformation();
			BuildTitle();
			BuildVisibility();
		}
		else if(id == R.id.tvPlatinumCluster){
			overviewID=MasterDataConstants.NetworkPerformace.PLATINUM_CLUSTER;
			LoadInformation();
			BuildTitle();
			BuildVisibility();
		}
		else if(id == R.id.tvSPISummary){
			overviewID=MasterDataConstants.NetworkPerformace.SPI_SUMMARY;
			LoadInformation();
			BuildTitle();
			BuildVisibility();
		}
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
		//progressBar = ProgressDialog.show(this, "", "Please wait....", true);
		//progressBar.setIcon(null);
	}

	@Override
	public void hideProgressLoader() {
		//progressBar.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IStatisticsReportManager manager=new StatisticsReportManager();
		return manager.getPMKPIDatas(overviewID);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			PMKPIDatas pMKPIDatas=(PMKPIDatas)data;
			DashboradDetailsActivity.pMKPIDatas = pMKPIDatas;
			if(pMKPIDatas!=null &&  pMKPIDatas.PMKPIDatas.size()>0){
				for(int i=0;i<pMKPIDatas.PMKPIDatas.size();i++){
					switch(i)
					{
					case 0:
						gvDashboardAcquisition.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
						tvDashboardFirstTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardAcquisitionValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						rlDashboardAcquisition.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
					case 1:
						gvDashboardRetention.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
						tvDashboardSecondTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardRetentionValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						rlDashboardRetention.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
					case 2:
						gvDashboardEfficiency.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);
						tvDashboardThirdTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardEfficiencyValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
						rlDashboardEfficiency.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
					case 3:
						gvDashboardTraffic.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(3).KPIValue);
						tvDashboardForthTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardTrafficValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(3).KPIValue));
						rlDashboardTraffic.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
						
					case 4:
						gvDashboardMTR.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(4).KPIValue);
						tvDashboardFifthTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardMTRValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(4).KPIValue));
						rlDashboardMTR.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
						
					case 5:
						gvDashboardSelfService.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(5).KPIValue);
						tvDashboardSixthTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardSelfServiceValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(5).KPIValue));
						rlDashboardSelfService.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
						
					}
				}
			}
		}
	}

}
