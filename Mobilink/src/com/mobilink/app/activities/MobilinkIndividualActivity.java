package com.mobilink.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.mobilink.app.R;
import com.mobilink.app.asynchronoustask.DownloadableAsyncTask;
import com.mobilink.app.entities.NetworkKPI;
import com.mobilink.app.entities.NetworkKPIs;
import com.mobilink.app.interfaces.IAsynchronousTask;
import com.mobilink.app.interfaces.INetworkKPIManager;
import com.mobilink.app.manager.NetworkKPIManager;
import com.mobilink.app.utils.CommonConstraints;

import com.mobilink.app.utils.CommonValues;

import com.mobilink.app.utils.CommonURL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MobilinkIndividualActivity extends Activity implements
		OnClickListener{

	RelativeLayout rlFinance, rlNetworkKPI, rlActivities;
	TextView tvLatestUpdate, tvHeaderName;
	ImageView ivOperatorIcon;
	AQuery aq;
	ProgressDialog progress;
	
	DownloadableAsyncTask downloadableAsyncTask ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobilink_individual_operator);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Initialization();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void Initialization() {

		rlFinance = (RelativeLayout) findViewById(R.id.rlFinance);
		rlNetworkKPI = (RelativeLayout) findViewById(R.id.rlNetworkKPI);
		rlActivities = (RelativeLayout) findViewById(R.id.rlActivities);

		tvLatestUpdate = (TextView) findViewById(R.id.tvLatestUpdate);
		tvHeaderName = (TextView) findViewById(R.id.tvHeaderName);
		ivOperatorIcon = (ImageView) findViewById(R.id.ivOperatorIcon);

		rlFinance.setOnClickListener(this);
		rlNetworkKPI.setOnClickListener(this);
		rlActivities.setOnClickListener(this);
		tvLatestUpdate.setOnClickListener(this);
		

		tvHeaderName.setText(CommonValues.getInstance().SelectedCompany.CompanyName.toUpperCase());
		aq = new AQuery(ivOperatorIcon);
		ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
		imgOptions.ratio=0;
		imgOptions.targetWidth=200;
		aq.id(ivOperatorIcon).image(CommonURL.getInstance().getImageServer+CommonValues.getInstance().SelectedCompany.CompanyLogo, imgOptions);


	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.rlFinance) {

			/*if(downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();*/

			Intent intent=new Intent(this,MobilinkFinanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);


		} else if (view.getId() == R.id.rlNetworkKPI) {
			Intent intent = new Intent(this, MobilinkNetworkKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlActivities) {			
			Intent intent=new Intent(this,MobilinkActivity.class);

			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);

		} else if (view.getId() == R.id.tvLatestUpdate) {

		}

	}	
}
