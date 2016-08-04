package com.mobilink.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.mobilink.app.R;
import com.mobilink.app.utils.CommonTask;
import com.mobilink.app.utils.CommonURL;
import com.mobilink.app.utils.CommonValues;

public class MobilinkCompareDetailsActivity extends Activity {
	WebView wvIndividual;
	
	public static String selectedUsers;
	public static String selectedOperatorNames;
	
	ImageView ivOperatorName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobilink_individual_compare_details);
		wvIndividual = (WebView) findViewById(R.id.wvIndividual);
		ivOperatorName= (ImageView) findViewById(R.id.ivOperatorName);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();		
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		((TextView)findViewById(R.id.tvDetailsHeader)).setText(CommonValues.getInstance().SelectedGraphItem);
		((TextView)findViewById(R.id.tvDetailsOperatorNames)).setText(selectedOperatorNames.length()>50?selectedOperatorNames.substring(0,50)+"...":selectedOperatorNames);
		
		
		AQuery aq = new AQuery(ivOperatorName);
		ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
		imgOptions.ratio = 0;
		imgOptions.targetWidth = 200;
		aq.id(ivOperatorName).image(CommonURL.getInstance().getImageServer+ CommonValues.getInstance().SelectedCompany.CompanyLogo,
						imgOptions);
		showGraph();
	}

	private void showGraph() {
		
		wvIndividual.getSettings().setBuiltInZoomControls(true);
		String URL ="";
		selectedUsers=CommonValues.getInstance().SelectedCompany.CompanyID+","+selectedUsers;
		if(MobilinkOperatorCompareActivity.ReportType.equals("Finance")){
			URL = "http://120.146.188.232:9050/KPICompareFinance.aspx?reqd="
		    + selectedUsers + "|"
		    + CommonValues.getInstance().SelectedGraphItem;
		}else{
			URL = "http://120.146.188.232:9050/CompareNetworkKPI.aspx?reqd="
				    + selectedUsers + "|"
				    + CommonValues.getInstance().SelectedGraphItem;
		}

		  wvIndividual.getSettings().setJavaScriptEnabled(true);
		  wvIndividual.getSettings().setLoadWithOverviewMode(true);
		  wvIndividual.getSettings().setUseWideViewPort(true);
		  wvIndividual.loadUrl(URL);
		 }
}
