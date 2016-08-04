package com.vipdashboard.app.activities;

import java.io.File;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IOperatorManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.OperatorManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class ProblemTrackingReviewActivity extends MainActionbarBase implements
		OnClickListener, OnRatingBarChangeListener, IAsynchronousTask {

	ImageView ivCountryFlag, ivContinue;
	RatingBar rbNetworkQuality, rbCustomerCare, rbPackageandPrice;
	EditText etRemarks,etSelectOperatorName;
	TextView tvCompanyName, tvCompanyCountry,tvSelectCountryName;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.problim_tracking_review);
		Initialization();
	}

	private void Initialization() {

		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		tvSelectCountryName= (TextView) findViewById(R.id.tvSelectCountryName);

		ivCountryFlag = (ImageView) findViewById(R.id.ivCountryFlag);
		ivContinue = (ImageView) findViewById(R.id.ivContinue);

		rbNetworkQuality = (RatingBar) findViewById(R.id.rbNetworkQuality);
		rbCustomerCare = (RatingBar) findViewById(R.id.rbCustomerCare);
		rbPackageandPrice = (RatingBar) findViewById(R.id.rbPackageandPrice);

		etRemarks = (EditText) findViewById(R.id.etRemarks);
		etSelectOperatorName= (EditText) findViewById(R.id.etSelectOperatorName);

		ivContinue.setOnClickListener(this);
		rbNetworkQuality.setOnRatingBarChangeListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		 if(!CommonTask.isMyServiceRunning(this))
				startService(new Intent(this, MyNetService.class));
		 
		InitializeValues();
	}

	private void InitializeValues() {
		
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		
		if(CommonValues.getInstance().SelectedCountry!=null && !CommonValues.getInstance().SelectedCountry.CountryFlagUrl.isEmpty()){
			   AQuery aq = new AQuery(ivCountryFlag); 
			   ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;   
			   imgOptions.targetWidth=100;
			   imgOptions.ratio=0;
			   imgOptions.round = 0;
			   aq.id(ivCountryFlag).image(CommonValues.getInstance().SelectedCountry.CountryFlagUrl, imgOptions);
			  }
	
		  tvSelectCountryName.setText(tMgr.getNetworkOperatorName());

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	public void onClick(View view) {
		
		if(view.getId()==R.id.ivContinue){
			
			LoadInformation();
			
		}

	}

	private void LoadInformation() {
	
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
		
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		
		

	}

	@Override
	public void showProgressLoader() {
		
			progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("Processing...");
			progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();

	}

	@Override
	public Object doBackgroundPorcess() {
		IOperatorManager operatorManager=new OperatorManager();
		return operatorManager.setProblemTrackingReview(CommonValues.getInstance().LoginUser.Company.CompanyID, 
				MyNetService.phoneId, (int)rbNetworkQuality.getRating(), 
				(int)rbCustomerCare.getRating()	,(int)rbPackageandPrice.getRating() , 
				 etRemarks.getText().toString(), System.currentTimeMillis());
	}

	@Override
	public void processDataAfterDownload(Object data) {
		
		if(data!=null){
			//Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
			
			/*Intent emailIntent = new Intent(
				     android.content.Intent.ACTION_SEND);
				   emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				     "Mumtaz Care Review");
				   emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				     "Subcribe Operator : " + tvSelectCountryName.getText().toString() + "\n"
				     + "Network Quality : " + (int)rbNetworkQuality.getRating() + "\n"
				     + "Customer Care : " + (int)rbCustomerCare.getRating() + "\n"
				     + "Package and Price : " + (int)rbPackageandPrice.getRating());
				   emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, CommonValues.getInstance().LoginUser.Company.Email);
			   emailIntent.setType("plain/text");
				   startActivity(Intent.createChooser(emailIntent, "Send"));*/
			
			Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
		    onBackPressed();
			
			
		}
		
	}

}
