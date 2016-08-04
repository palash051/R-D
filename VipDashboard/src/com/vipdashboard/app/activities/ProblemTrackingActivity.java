package com.vipdashboard.app.activities;

import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadPhoneinfoAsyncTask;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.OperatorLicense;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IOperatorManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.OperatorManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.MasterDataConstants;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProblemTrackingActivity extends MainActionbarBase implements  OnClickListener,OnTouchListener{
	
	RelativeLayout rlOffers, rlSubscribe, rlPackageandBilling, rlSupport, rlReview, rlTerminate;
	TextView tvCompanyName, tvCompanyCountry,tvDucare;
	
	DownloadableAsyncTask downloadAsync;
	ProgressDialog progress;
	
	String OperatorName="", MCC="", MNC="", OperatorCountryISO="";
	
	Dialog gdialog;
	
	ImageView ivOffers,
	ivSubscribe,
	ivPackageandBilling,
	ivSupport,
	ivReview,
	ivTerminate;


	TextView tvOffers,
	tvSubscribe,
	tvPackageandBilling,
	tvSupport,
	tvReview,
	tvTerminate; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operator_care);
		Initialization();	
		//CheckedLicense();
		
		//CheckLicensed(CommonValues.IsOpeatorLicensed);
		
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
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
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		 
		super.onResume();
		
		tvDucare.setText(tMgr.getNetworkOperatorName().toString().toUpperCase()
				+ " CARE");
		
		setBackgroundColor();
	}

	private void CheckLicensed(boolean isActivateLicensed) {
		if(!isActivateLicensed){
			Dialog dialog = new Dialog(this);
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.check_liensed);
			gdialog=dialog;
			
			ImageView back = (ImageView) dialog.findViewById(R.id.ivBack);
			TextView ivDynamicLabel=(TextView) dialog.findViewById(R.id.ivDynamicLabel);
			
		
			
			
			ivDynamicLabel.setText("We don’t have service contract with your operator "+OperatorName +". Still we will send your Care requests to your operator through email.");
			
			back.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					gdialog.dismiss();
				}
			});
			
			dialog.show();
		}
		
	}
	
	

	private void Initialization() {
		//rlOffers = (RelativeLayout) findViewById(R.id.rlOffers);
		rlSubscribe = (RelativeLayout) findViewById(R.id.rlSubscribe);
		//rlPackageandBilling = (RelativeLayout) findViewById(R.id.rlPackageandBilling);
		rlSupport = (RelativeLayout) findViewById(R.id.rlSupport);
		rlReview = (RelativeLayout) findViewById(R.id.rlReview);
		rlTerminate = (RelativeLayout) findViewById(R.id.rlTerminate);
		
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		
		tvDucare = (TextView) findViewById(R.id.tvDucare);
		
		//ivOffers= (ImageView) findViewById(R.id.ivOffers);
		ivSubscribe= (ImageView) findViewById(R.id.ivSubscribe);
		//ivPackageandBilling= (ImageView) findViewById(R.id.ivPackageandBilling);
		ivSupport= (ImageView) findViewById(R.id.ivSupport);
		ivReview= (ImageView) findViewById(R.id.ivReview);
		ivTerminate= (ImageView) findViewById(R.id.ivTerminate);


		//tvOffers= (TextView) findViewById(R.id.tvOffers);
		tvSubscribe= (TextView) findViewById(R.id.tvSubscribe);
		//tvPackageandBilling= (TextView) findViewById(R.id.tvPackageandBilling);
		tvSupport= (TextView) findViewById(R.id.tvSupport);
		tvReview= (TextView) findViewById(R.id.tvReview);
		tvTerminate= (TextView) findViewById(R.id.tvTerminate);
		
		
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		
		//Raju Dutta Changed here, licensing logic as per rupom vaiya 06-Apr-2014
		
		String networkOperator = tMgr.getNetworkOperator();
		
		if (networkOperator != null) {
			
			
			
			MCC = String.valueOf(Integer.parseInt(networkOperator.substring(0, 3)));
			MNC = String.valueOf(Integer.parseInt(networkOperator.substring(3)));
			
			
			/*MCC = networkOperator.substring(0, 3);
			MNC = networkOperator.substring(3);*/
		}
		
		OperatorName = tMgr.getNetworkOperatorName().toString();
		OperatorCountryISO = tMgr.getSimCountryIso().toString();
		
		//rlOffers.setOnClickListener(this);
		rlSubscribe.setOnClickListener(this);
		//rlPackageandBilling.setOnClickListener(this);
		rlSupport.setOnClickListener(this);
		rlReview.setOnClickListener(this);
		rlTerminate.setOnClickListener(this);
		
		//rlOffers.setOnTouchListener(this);
		rlSubscribe.setOnTouchListener(this);
		//rlPackageandBilling.setOnTouchListener(this);
		rlSupport.setOnTouchListener(this);
		rlReview.setOnTouchListener(this);
		rlTerminate.setOnTouchListener(this);
		

	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.rlSupport){
			//public static String OperatorHelpDeskEmail = "Not Available";
			if(CommonValues.OperatorHelpDeskMobileNo!="Not Available")
			{
			Intent calltomanager = new Intent(Intent.ACTION_CALL);
			calltomanager.setData(Uri.parse("tel:" + CommonValues.OperatorHelpDeskMobileNo));
			startActivity(calltomanager);
			}
			else
			{
				Toast.makeText(this, "Your provider didn't set help desk number; Please contact provider.", Toast.LENGTH_LONG).show();
			}
		}
		if(CommonTask.isOnline(getApplicationContext())){
		/*if(v.getId() == R.id.rlOffers){
			Intent intent = new Intent(ProblemTrackingActivity.this,OffersActivity.class); 
			intent.putExtra("OverviewActionID", MasterDataConstants.CommonWebLink.OFFERS); 
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}*/if(v.getId() == R.id.rlSubscribe){
			Intent intent = new Intent(ProblemTrackingActivity.this,SubscriberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}/*else if(v.getId() == R.id.rlPackageandBilling){
			Intent intent = new Intent(ProblemTrackingActivity.this,OffersActivity.class);   
			intent.putExtra("OverviewActionID", MasterDataConstants.CommonWebLink.PACKAGES_AND_BILLING);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}	*/
		else if(v.getId() == R.id.rlReview){
			Intent intent = new Intent(ProblemTrackingActivity.this,ProblemTrackingReviewActivity.class);   
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			
		}else if(v.getId() == R.id.rlTerminate){
			if(CommonValues.OperatorHelpDeskMobileNo!="Not Available")
			{
				Intent intent = new Intent(ProblemTrackingActivity.this,OperatorMailActivity.class);   
				intent.putExtra("OverviewActionID", MasterDataConstants.CommonWebLink.PACKAGES_AND_BILLING);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				/*Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + CommonValues.OperatorHelpDeskEmail));
				intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request Via Care");
				intent.putExtra(Intent.EXTRA_TEXT, "This email is automatically generated by Care Solution.\nTo get details http://www.conio.org.au");
				startActivity(intent);*/
			}
			else
			{
				Toast.makeText(this, "Sorry, Your operator doesn't provide contact information.", Toast.LENGTH_LONG).show();
			}
			
		}
		}
		else{
			Toast.makeText(getApplicationContext(), "Sorry, Your operator doesn't provide contact information.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setBackgroundColor() {
		//rlOffers.setBackgroundColor(Color.parseColor("#FF33CC")); 
		rlSubscribe.setBackgroundColor(Color.parseColor("#FF9900")); 
		//rlPackageandBilling.setBackgroundColor(Color.parseColor("#00B050"));  
		rlSupport.setBackgroundColor(Color.parseColor("#CC3300"));  
		rlReview.setBackgroundColor(Color.parseColor("#666699")); 
		rlTerminate.setBackgroundColor(Color.parseColor("#6699FF"));
		
		  final float scale = getResources().getDisplayMetrics().density;

		
		
		/*ivOffers.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		ivOffers.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		tvOffers.setTextSize(CommonValues.COMMON_TEXT_SIZE);*/
   		
   		ivSubscribe.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		ivSubscribe.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		tvSubscribe.setTextSize(CommonValues.COMMON_TEXT_SIZE);
   		
   		/*ivPackageandBilling.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		ivPackageandBilling.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		tvPackageandBilling.setTextSize(CommonValues.COMMON_TEXT_SIZE);*/
   		
   		ivSupport.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		ivSupport.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
   		tvSupport.setTextSize(CommonValues.COMMON_TEXT_SIZE);
   		
   		ivReview.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivReview.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		tvReview.setTextSize(CommonValues.COMMON_TEXT_SIZE);
		
		ivTerminate.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		ivTerminate.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		tvTerminate.setTextSize(CommonValues.COMMON_TEXT_SIZE);
	}
	
	@Override public boolean onTouch(View view, MotionEvent arg1)
	  {   
		   findViewById(view.getId()).setBackgroundColor(Color.parseColor("#FFC000"));
		   
		   final float scale = getResources().getDisplayMetrics().density;
		   
		   switch(view.getId())
		   {
			   /*	case R.id.rlOffers:
			   		ivOffers.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		ivOffers.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		tvOffers.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
				   break;*/
			   	case R.id.rlSubscribe:
			   		ivSubscribe.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
			   		ivSubscribe.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
			   		tvSubscribe.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
			  /* 	case R.id.rlPackageandBilling:
			   		ivPackageandBilling.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
			   		ivPackageandBilling.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
			   		tvPackageandBilling.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;*/
			   	case R.id.rlSupport:
			   		ivSupport.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
			   		ivSupport.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
			   		tvSupport.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
				case R.id.rlReview:
					ivReview.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
					ivReview.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
					tvReview.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
				case R.id.rlTerminate:
					ivTerminate.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
					ivTerminate.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);;
					tvTerminate.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
		   }
		   return false; 
	  }

}
