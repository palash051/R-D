package com.leadership.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.leadership.app.R;
import com.leadership.app.utils.CommonConstraints;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonURL;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LeadershipFinanceActivity extends Activity implements
		OnClickListener {

	AQuery aq;
	ImageView ivOperatorIcon, ivFinance,ivOperatorCompare,ivOperatorNetworkKpi,ivOperatorSWMI,ivOperatorSummary;
	ProgressDialog progress;
	RelativeLayout rlSUBSCRIBER, rlREVENUE, rlEBIDTA, rlARPU;
	WebView wv;

	LinearLayout llFinanceSubtab;

	TextView tvARPUVoice, tvARPUData, tvSubsMBB, tvSubsDataTraffic,
			tvSubsDataTrafficPriceGB,ivOperatorIconText;

	private final String FINANCESUBSCRIBER = "SUBSCRIBER";
	private final String FINANCEREVENUE = "REVENUE";
	private final String FINANCEEBIDTA = "EBIDATA";
	private final String FINANCEARPU = "ARPU";

	//private String selectedType = "";
	//DownloadableAsyncTask downloadableAsyncTask;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leadership_finance);
		Initialization();
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// selectedType = "ARPU";
		// showGraph();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		/*AQuery aq = new AQuery(ivOperatorIcon);
		ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
		imgOptions.ratio = 0;
		imgOptions.targetWidth = 200;
		aq.id(ivOperatorIcon)
				.progress(progress)
				.image(CommonURL.getInstance().getImageServer+ CommonValues.getInstance().SelectedCompany.CompanyLogo,
						imgOptions);*/
		((ImageView) findViewById(R.id.ivFinanceHeaderIcon)).setBackgroundResource(R.drawable.financial_info);
		if(CommonValues.getInstance().LoginUser.UserMode==3){
			ivOperatorIconText.setVisibility(View.VISIBLE);
			ivOperatorIcon.setVisibility(View.GONE);
			ivOperatorIconText.setText(CommonValues.getInstance().SelectedCompany.CompanyName);
		}else{
			ivOperatorIconText.setVisibility(View.GONE);
			ivOperatorIcon.setVisibility(View.VISIBLE);
			ivOperatorIcon.setBackgroundResource(CommonValues.getInstance().SelectedCompany.CompanyLogoId);
		}
		rlARPU.performClick();
		/*
		 * rlARPU.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected
		 * )); if (downloadableAsyncTask != null)
		 * downloadableAsyncTask.cancel(true); downloadableAsyncTask = new
		 * DownloadableAsyncTask(this); downloadableAsyncTask.execute();
		 */
		
	}

	private void Initialization() {
		ivOperatorIcon = (ImageView) findViewById(R.id.ivOperatorIcon);
		ivFinance = (ImageView) findViewById(R.id.ivFinance);
		ivOperatorCompare= (ImageView) findViewById(R.id.ivOperatorCompare);
		ivOperatorNetworkKpi= (ImageView) findViewById(R.id.ivOperatorNetworkKpi);
		ivOperatorSWMI= (ImageView) findViewById(R.id.ivOperatorSWMI);
		ivOperatorSummary= (ImageView) findViewById(R.id.ivOperatorSummary);
		
		rlSUBSCRIBER = (RelativeLayout) findViewById(R.id.rlSUBSCRIBER);
		rlREVENUE = (RelativeLayout) findViewById(R.id.rlREVENUE);
		rlEBIDTA = (RelativeLayout) findViewById(R.id.rlEBIDTA);
		rlARPU = (RelativeLayout) findViewById(R.id.rlARPU);

		llFinanceSubtab = (LinearLayout) findViewById(R.id.llFinanceSubtab);

		tvARPUVoice = (TextView) findViewById(R.id.tvARPUVoice);
		tvARPUData = (TextView) findViewById(R.id.tvARPUData);
		tvSubsMBB = (TextView) findViewById(R.id.tvSubsMBB);
		tvSubsDataTraffic = (TextView) findViewById(R.id.tvSubsDataTraffic);
		tvSubsDataTrafficPriceGB = (TextView) findViewById(R.id.tvSubsDataTrafficPriceGB);
		ivOperatorIconText= (TextView) findViewById(R.id.ivOperatorIconText);

		tvARPUVoice.setOnClickListener(this);
		tvARPUData.setOnClickListener(this);
		tvSubsMBB.setOnClickListener(this);
		tvSubsDataTraffic.setOnClickListener(this);
		tvSubsDataTrafficPriceGB.setOnClickListener(this);

		rlSUBSCRIBER.setOnClickListener(this);
		rlREVENUE.setOnClickListener(this);
		rlEBIDTA.setOnClickListener(this);
		rlARPU.setOnClickListener(this);
		ivOperatorNetworkKpi.setOnClickListener(this);
		ivOperatorSWMI.setOnClickListener(this);
		ivOperatorSummary.setOnClickListener(this);
		ivOperatorCompare.setOnClickListener(this);	
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.rlSUBSCRIBER) {
			
			/*if (downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();*/
			rlREVENUE.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlARPU.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlEBIDTA.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlSUBSCRIBER.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			
			llFinanceSubtab.setVisibility(View.VISIBLE);
			
			tvARPUVoice.setVisibility(View.GONE);
			tvARPUData.setVisibility(View.GONE);
			tvSubsMBB.setVisibility(View.VISIBLE);
			tvSubsDataTraffic.setVisibility(View.VISIBLE);
			tvSubsDataTrafficPriceGB.setVisibility(View.VISIBLE);
			
			tvSubsDataTraffic.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvSubsDataTrafficPriceGB.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvSubsMBB.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));

			//tvSubsMBB.performClick();
			CommonValues.getInstance().SelectedGraphItem="Subscribers";
			showGraph(CommonValues.getInstance().SelectedGraphItem);

		} else if (view.getId() == R.id.rlREVENUE) {
			
			/*if (downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();*/
			rlSUBSCRIBER.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlARPU.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlEBIDTA.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlREVENUE.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));

			llFinanceSubtab.setVisibility(View.GONE);
			CommonValues.getInstance().SelectedGraphItem="Revenue";
			showGraph(CommonValues.getInstance().SelectedGraphItem);

		} else if (view.getId() == R.id.rlEBIDTA) {
			
			/*if (downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();*/
			rlSUBSCRIBER.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlREVENUE.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlARPU.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlEBIDTA.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));

			llFinanceSubtab.setVisibility(View.GONE);
			CommonValues.getInstance().SelectedGraphItem="EBIDTA";
			showGraph(CommonValues.getInstance().SelectedGraphItem);

		} else if (view.getId() == R.id.rlARPU) {
			rlSUBSCRIBER.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlREVENUE.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlEBIDTA.setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			rlARPU.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			
			tvARPUVoice.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvARPUData.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			
			llFinanceSubtab.setVisibility(View.VISIBLE);
			
			tvARPUVoice.setVisibility(View.VISIBLE);
			tvARPUData.setVisibility(View.VISIBLE);
			tvSubsMBB.setVisibility(View.GONE);
			tvSubsDataTraffic.setVisibility(View.GONE);
			tvSubsDataTrafficPriceGB.setVisibility(View.GONE);
			//tvARPUVoice.performClick();
			/*if (downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();*/
			
			showGraph(CommonValues.getInstance().SelectedGraphItem="ARPU");
		}

		else if (view.getId() == R.id.tvARPUVoice) {
			tvARPUVoice.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tvARPUData.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			
			showGraph(CommonValues.getInstance().SelectedGraphItem="Voice ARPU");
		}
		else if (view.getId() == R.id.tvARPUData) {
			tvARPUVoice.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvARPUData.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			showGraph(CommonValues.getInstance().SelectedGraphItem="Data ARPU");
		}
		else if (view.getId() == R.id.tvSubsMBB) {
			tvSubsDataTraffic.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvSubsDataTrafficPriceGB.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvSubsMBB.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			showGraph(CommonValues.getInstance().SelectedGraphItem="MBB Subscribers");
		}
		else if (view.getId() == R.id.tvSubsDataTraffic) {
			tvSubsDataTraffic.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tvSubsDataTrafficPriceGB.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvSubsMBB.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem="Data Traffic");
		}
		else if (view.getId() == R.id.tvSubsDataTrafficPriceGB) {
			tvSubsDataTraffic.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			tvSubsDataTrafficPriceGB.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tvSubsMBB.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph(CommonValues.getInstance().SelectedGraphItem="Price per");
		}else if (view.getId() == R.id.ivOperatorCompare) {
			LeadershipOperatorCompareActivity.ReportType="Finance";
			Intent intent = new Intent(this, LeadershipOperatorCompareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.ivOperatorNetworkKpi){
			Intent intent = new Intent(this, LeadershipNetworkKPIVoiceDataActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.ivOperatorSWMI){
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
		wv = (WebView) findViewById(R.id.dwvSpeedometer);
		wv.getSettings().setBuiltInZoomControls(true);

		String URL = "http://120.146.188.232:9050/FinancialKPI.aspx?reqd="
				+ CommonValues.getInstance().SelectedCompany.CompanyID + ","
				+ type;

		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(CommonTask.getScale(this));
		wv.loadUrl(URL);
	}

	/*@Override
	public void showProgressLoader() {
		progress = ProgressDialog.show(this, "", "Please wait", true);
		progress.setCancelable(false);
		progress.setIcon(null);

	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();

	}

	@Override
	public Object doBackgroundPorcess() {
		INetworkKPIManager manager = new NetworkKPIManager();

		FinancialDatas financialDatas = manager
				.GetAllFinancialDataByType(selectedType);
		if (financialDatas.FinancialDataList != null
				&& financialDatas.FinancialDataList.size() > 0) {
			String chxl = "chxl=0:|";
			String q1 = "", q2 = "", q3 = "", q4 = "";
			int maxVal = 0;
			for (FinancialData financialData : financialDatas.FinancialDataList) {
				chxl = chxl + financialData.Year + "|";
				q1 = q1 + financialData.Value + ",";
				if (financialData.Value > maxVal) {
					maxVal = (int) financialData.Value;
				}
			}
			if (!q1.isEmpty()) {
				q1 = q1.substring(0, q1.length() - 1);
			}
			if (!q2.isEmpty()) {
				q2 = q2.substring(0, q2.length() - 1);
			}
			if (!q3.isEmpty()) {
				q3 = q3.substring(0, q3.length() - 1);
			}
			if (!q4.isEmpty()) {
				q4 = q4.substring(0, q4.length() - 1);
			}
			if (!chxl.isEmpty()) {
				chxl = chxl.substring(0, chxl.length() - 1);
			}
			String chd = "chd=t:", chm = "", chco = "";
			chm = "chm=N,000000,-1,10";
			chco = "chco=407FCA";
			if (!q1.isEmpty()) {
				chd = chd + q1;
			}
			if (!q2.isEmpty()) {
				if (!chd.isEmpty()) {
					chd = chd + "|" + q2;
					chm = chm + "|N,000000,-1,10";
					chco = chco + ",CE413E";
				} else {
					chd = chd + q2;
				}
			}
			if (!q3.isEmpty()) {
				if (!chd.isEmpty()) {
					chd = chd + "|" + q3;
					chm = chm + "|N,000000,-1,10";
					chco = chco + ",82A33B";
				} else {
					chd = chd + q3;
				}
			}
			if (!q4.isEmpty()) {
				if (!chd.isEmpty()) {
					chm = chm + "|N,000000,-1,10";
					chco = chco + ",715099";
					chd = chd + "|" + q4;
				} else {
					chd = chd + q4;
				}
			}

			maxVal = (int) (Math.round((maxVal + 10) / 10) * 10);

			String chds = "chds=0," + maxVal;
			String chxr = "";
			if (maxVal < 100)
				chxr = "chxr=1,0," + maxVal + ",20";
			else if (maxVal < 200)
				chxr = "chxr=1,0," + maxVal + ",40";
			else if (maxVal < 500)
				chxr = "chxr=1,0," + maxVal + ",100";
			else if (maxVal < 1000)
				chxr = "chxr=1,0," + maxVal + ",200";
			else if (maxVal < 2000)
				chxr = "chxr=1,0," + maxVal + ",400";
			else if (maxVal < 4000)
				chxr = "chxr=1,0," + maxVal + ",800";
			else if (maxVal > 4000)
				chxr = "chxr=1,0," + maxVal + ",1000";

			String url = CommonURL.getInstance().GoogleBarChartUrl;// +
																	// "chdl=1|2";
			url = url + "" + chds + "&" + chxr + "&" + chm + "&" + chco;
			url = url + "&" + chxl + "&" + chd;
			url = url.replace(" ", "_");
			return CommonTask.getBitmapImage(url);
			
			 * GraphActivity.setUrl(url); Intent intent = new Intent(this,
			 * GraphActivity.class);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			 * startActivity(intent);
			 

			// "chds=0,70&chxr=1,0,70,5&chm=N,FF0000,0,-1,10|N,0000FF,1,-1,10&chdl=1|2&chco=407FCA,CC403D&chxl=0:|0~50|50~70|70~85|85~95|95%3E&chd=t:20,30,60,38,50|50,39,55,30,60";

		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			ivFinance.setImageBitmap((Bitmap) data);
		} else {
			ivFinance.setImageBitmap(null);
		}

	}*/

}
