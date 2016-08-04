package com.vipdashboard.app.activities;


import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginWithPasswordActivity extends Activity implements OnClickListener, IAsynchronousTask {
	
	EditText etMobileNumber, etPassword;
	ImageView ivNext;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	String userPhoneNumber, password,prefUserPass;
	
	String OperatorName="", MCC="", MNC="", OperatorCountryISO="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_password);
		
		Initialization();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		/*//License Issue
		
				String networkOperator = tMgr.getNetworkOperator();
				
				if (networkOperator != null) {
					
					
					
					MCC = String.valueOf(Integer.parseInt(networkOperator.substring(0, 3)));
					MNC = String.valueOf(Integer.parseInt(networkOperator.substring(3)));
					
					
					MCC = networkOperator.substring(0, 3);
					MNC = networkOperator.substring(3);
				}
				
				OperatorName = tMgr.getNetworkOperatorName().toString();
				OperatorCountryISO = tMgr.getSimCountryIso().toString();*/
				
				
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
			}
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		
		userPhoneNumber = password = "";
	}

	private void Initialization() {
		etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
		etPassword = (EditText) findViewById(R.id.etPassword);
		ivNext = (ImageView) findViewById(R.id.ivNext);
		
		ivNext.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivNext){
			if(!etMobileNumber.getText().toString().trim().isEmpty()&&!etPassword.getText().toString().trim().isEmpty())
			{
			userPhoneNumber = etMobileNumber.getText().toString().trim();
			password = etPassword.getText().toString().trim();
			LoadInforamtion();
			}
			else
			{
				if(etMobileNumber.getText().toString().trim().isEmpty())
				{
					etMobileNumber.setError("Please enter your mobile number");
					
				}
				else if(etPassword.getText().toString().trim().isEmpty())
				{
					etPassword.setError("Please enter your passcode");
				}
			}
		}
	}

	private void LoadInforamtion() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
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
		IUserManager userManager = new UserManager();
		
		return userManager.getUserLoginAuthentication(LoginWithPasswordActivity.this,userPhoneNumber, password);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			CommonValues.getInstance().LoginUser = (User) data;
			
			CommonTask.savePreferences(this,CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY,String.valueOf(CommonValues.getInstance().LoginUser.UserNumber)	);
			
			prefUserPass=CommonTask.getPreferences(this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
			
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			
			/*CommonTask.savePreferences(this,CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY,String.valueOf(CommonValues.getInstance().LoginUser.UserNumber)	);
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("LoginUserNumber", Integer.parseInt(CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY));
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);*/
			
			/*
			CommonTask.savePreferences(this,CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY,String.valueOf(CommonValues.getInstance().LoginUser.UserNumber)	);
			prefUserPass=CommonTask.getPreferences(this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);*/
			
		}else{
			etPassword.setError("Please enter your valid activation code.");
		}
		
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}
}
