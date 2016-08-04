package com.leadership.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.leadership.app.R;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonURL;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class LeadershipNetworkKPIDownloadActivity extends Activity implements OnClickListener {
	WebView wv;
	TextView tvUplink_Speed,tvLatency,ivOperatorIconText;
	ImageView ivOperatorCompare,ivOperatorNetworkKpi,ivOperatorIcon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leadership_network_kpi_downloadspeed);		
		wv=(WebView)findViewById(R.id.wvIndividual);
		
		tvUplink_Speed=(TextView)findViewById(R.id.tvUplink_Speed);
		tvLatency=(TextView)findViewById(R.id.tvLatency);
		ivOperatorIconText= (TextView) findViewById(R.id.ivOperatorIconText);
		tvUplink_Speed.setOnClickListener(this);
		
		tvLatency.setOnClickListener(this);
		
		ivOperatorCompare= (ImageView) findViewById(R.id.ivOperatorCompare);
		ivOperatorNetworkKpi= (ImageView) findViewById(R.id.ivOperatorNetworkKpi);
		ivOperatorCompare.setOnClickListener(this);
		ivOperatorNetworkKpi.setOnClickListener(this);
		ivOperatorIcon= (ImageView) findViewById(R.id.ivOperatorIcon);
	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		tvUplink_Speed.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
		tvLatency.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
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
		
		showGraph("Down Link Speed");
		 
		 
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.tvUplink_Speed){
			tvUplink_Speed.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tvLatency.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph("Uplink Speed");
		}else if(id==R.id.tvLatency){
			tvLatency.setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
			tvUplink_Speed.setBackgroundColor(getResources().getColor(R.color.sub_tab_bg_normal));
			showGraph("Latency");
		}else if (id == R.id.ivOperatorCompare) {
			LeadershipOperatorCompareActivity.ReportType="Network KPI";
			CommonValues.getInstance().SelectedGraphItem="Speed";
			Intent intent = new Intent(this, LeadershipOperatorCompareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.ivOperatorNetworkKpi){
			Intent intent = new Intent(this, LeadershipFinanceActivity.class);
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
