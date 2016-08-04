package com.leadership.app.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.leadership.app.R;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonURL;
import com.leadership.app.utils.CommonValues;

public class LeadershipCompareDetailsActivity extends Activity {
	WebView wvIndividual;

	public static String selectedUsers;
	public static String selectedOperatorNames;
	public static int selectedSWMIId=0;

	ImageView ivOperatorName;
	TextView tvDetailsHeader,ivOperatorIconText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leadership_individual_compare_details);
		wvIndividual = (WebView) findViewById(R.id.wvIndividual);
		ivOperatorName = (ImageView) findViewById(R.id.ivOperatorName);
		ivOperatorIconText= (TextView) findViewById(R.id.ivOperatorIconText);
		tvDetailsHeader= (TextView) findViewById(R.id.tvDetailsHeader);
	}

	@Override
	protected void onResume() {

		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask
					.showMessage(this,
							"Network connection error.\nPlease check your internet connection.");
			return;
		}
		if(CommonValues.getInstance().SelectedGraphItem.equals(""))
			tvDetailsHeader.setVisibility(View.GONE);
		else{
			tvDetailsHeader.setVisibility(View.VISIBLE);
			tvDetailsHeader.setText(CommonValues.getInstance().SelectedGraphItem);
		}
		selectedOperatorNames=CommonValues.getInstance().SelectedCompany.CompanyName+","+selectedOperatorNames;
		((TextView) findViewById(R.id.tvDetailsOperatorNames))
				.setText(selectedOperatorNames.length() > 50 ? selectedOperatorNames
						.substring(0, 50) + "..."
						: selectedOperatorNames);

		/*
		 * AQuery aq = new AQuery(ivOperatorName); ImageOptions imgOptions =
		 * CommonValues.getInstance().defaultImageOptions; imgOptions.ratio = 0;
		 * imgOptions.targetWidth = 200;
		 * aq.id(ivOperatorName).image(CommonURL.getInstance().getImageServer+
		 * CommonValues.getInstance().SelectedCompany.CompanyLogo, imgOptions);
		 */
		
		
		if(CommonValues.getInstance().LoginUser.UserMode==3){
			ivOperatorIconText.setVisibility(View.VISIBLE);
			ivOperatorName.setVisibility(View.GONE);
			ivOperatorIconText.setText(CommonValues.getInstance().SelectedCompany.CompanyName);
		}else{
			ivOperatorIconText.setVisibility(View.GONE);
			ivOperatorName.setVisibility(View.VISIBLE);
			ivOperatorName.setBackgroundResource(CommonValues.getInstance().SelectedCompany.CompanyLogoId);
		}
		showGraph();
	}

	private void showGraph() {

		wvIndividual.getSettings().setBuiltInZoomControls(true);
		String URL = "";
		selectedUsers = CommonValues.getInstance().SelectedCompany.CompanyID
				+ "," + selectedUsers;
		if (LeadershipOperatorCompareActivity.ReportType.equals("Finance")) {
			if(CommonValues.getInstance().LoginUser.UserMode!=3)
				URL = "http://120.146.188.232:9050/KPICompareFinance.aspx?reqd="
					+ selectedUsers + "|"
					+ CommonValues.getInstance().SelectedGraphItem;
			else
				URL = "http://120.146.188.232:9050/OpeKPICompareFinance.aspx?reqd="
						+ selectedUsers + "|"
						+ CommonValues.getInstance().SelectedGraphItem;
		}else if (LeadershipOperatorCompareActivity.ReportType.equals("SWMI")) {
			if(CommonValues.getInstance().LoginUser.UserMode!=3)
				URL = "http://120.146.188.232:9050/CompareSWMI.aspx?reqd="+ selectedUsers;
			else
				URL = "http://120.146.188.232:9050/OpeCompareSWMI.aspx?reqd="+ selectedUsers;
			if(selectedSWMIId>0){
				URL=URL+"|"+selectedSWMIId;
			}
		} else {
			if(CommonValues.getInstance().LoginUser.UserMode!=3)
				URL = "http://120.146.188.232:9050/CompareNetworkKPI.aspx?reqd="
					+ selectedUsers + "|"
					+ CommonValues.getInstance().SelectedGraphItem;
			else
				URL = "http://120.146.188.232:9050/OpeCompareNetworkKPI.aspx?reqd="
						+ selectedUsers + "|"
						+ CommonValues.getInstance().SelectedGraphItem;
		}

		wvIndividual.getSettings().setJavaScriptEnabled(true);
		wvIndividual.getSettings().setLoadWithOverviewMode(true);
		wvIndividual.getSettings().setUseWideViewPort(true);
		wvIndividual.setInitialScale(CommonTask.getScale(this));
		wvIndividual.loadUrl(URL);
	}
}
