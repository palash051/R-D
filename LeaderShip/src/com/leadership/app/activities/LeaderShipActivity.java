package com.leadership.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.leadership.app.R;
import com.leadership.app.asynchronoustask.DownloadableAsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;

public class LeaderShipActivity extends Activity implements OnClickListener {
	
	ImageView ivOperatorIcon;
	AQuery aq;
	ProgressDialog progress;
	TextView tvFull_scope_ms_rfp,tvMumtaz_trial,tvKhareef_preparation,tvWork_order_4_negotiation,tvWork_packages_negotiation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.leadership_activities);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		Initialization();
	}
	
	private void Initialization() {	
		
		ivOperatorIcon=(ImageView) findViewById(R.id.ivOperatorIcon);
		
		/*aq = new AQuery(ivOperatorIcon);
		ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
		imgOptions.ratio=0;
		imgOptions.targetWidth=200;
		aq.id(ivOperatorIcon).progress(progress).image(CommonURL.getInstance().getImageServer+CommonValues.getInstance().SelectedCompany.CompanyLogo, imgOptions);*/
		
		ivOperatorIcon.setBackgroundResource(CommonValues.getInstance().SelectedCompany.CompanyLogoId);
		
		
		tvFull_scope_ms_rfp = (TextView) findViewById(R.id.tvFull_scope_ms_rfp);
		tvMumtaz_trial = (TextView) findViewById(R.id.tvMumtaz_trial);
		tvKhareef_preparation = (TextView) findViewById(R.id.tvKhareef_preparation);
		tvWork_order_4_negotiation = (TextView) findViewById(R.id.tvWork_order_4_negotiation);
		tvWork_packages_negotiation = (TextView) findViewById(R.id.tvWork_packages_negotiation);
		
		tvFull_scope_ms_rfp.setOnClickListener(this);
		tvMumtaz_trial.setOnClickListener(this);
		tvKhareef_preparation.setOnClickListener(this);
		tvWork_order_4_negotiation .setOnClickListener(this);
		tvWork_packages_negotiation.setOnClickListener(this);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.tvFull_scope_ms_rfp){
			Intent intent = new Intent(this, DemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvMumtaz_trial){
			Intent intent = new Intent(this, DemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvKhareef_preparation){
			Intent intent = new Intent(this, DemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvWork_order_4_negotiation){
			Intent intent = new Intent(this, DemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvWork_packages_negotiation){
			Intent intent = new Intent(this, DemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

}
