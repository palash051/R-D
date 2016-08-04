package com.vipdashboard.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.entities.OperatorLicense;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IOperatorManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.OperatorManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivationActivity extends FragmentActivity implements IAsynchronousTask{
	EditText etUserActivationCode,etUserEdit;
	Button bLogin,bChangeUser;
	TextView tvLoginMessage,tvCountryCode,etUserName,etPassword;
	public static String mobileNo,password,country_code,displayedMobileNo;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	ImageView ivNext,ivFlag;
	
	String OperatorName="", MCC="", MNC="", OperatorCountryISO="",prefUserPass;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login_activation);
		
		tvCountryCode = (TextView) findViewById(R.id.tvCountryCode);
		etUserName = (TextView) findViewById(R.id.etUserName);
		etPassword = (TextView) findViewById(R.id.etPassword);
		ivNext = (ImageView) findViewById(R.id.ivNext);		
		
		etUserActivationCode=(EditText)findViewById(R.id.etUserActivationCode);
		etUserEdit=(EditText)findViewById(R.id.etUserEdit);
		
		bLogin=(Button)findViewById(R.id.bLogin);
		bChangeUser=(Button)findViewById(R.id.bChangeUser);
		etUserEdit.setVisibility(View.GONE);
		
		tvLoginMessage=(TextView)findViewById(R.id.tvLoginMessage);
		ivFlag= (ImageView) findViewById(R.id.ivFlag);
		ivNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!etPassword.getText().toString().trim().isEmpty()){
					CommonValues.getInstance().isCallFromLoginActivity = true;				
					if (downloadableAsyncTask != null) {
						downloadableAsyncTask.cancel(true);
					}
					downloadableAsyncTask = new DownloadableAsyncTask(LoginActivationActivity.this);
					downloadableAsyncTask.execute();
				}else{
					etPassword.setError("Please enter your activation code.");
				}
				
			}
		});
		
		bChangeUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivationActivity.this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onPause() {		
		super.onPause();
	}
	@Override
	protected void onResume() {		
		super.onResume();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		//License Issue
		
		/*String networkOperator = tMgr.getNetworkOperator();
		
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
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		tvLoginMessage.setText("You will receive an SMS with a code to " + mobileNo + " within a minute.");	
		tvCountryCode.setText(country_code);
		etUserName.setText(displayedMobileNo);
		if(!CommonValues.getInstance().SelectedCountry.CountryFlagUrl.isEmpty()){
			AQuery aq = new AQuery(ivFlag);	
			ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions; 		
			imgOptions.targetWidth=100;
			imgOptions.ratio=0;
			imgOptions.round = 0;
			aq.id(ivFlag).image(CommonValues.getInstance().SelectedCountry.CountryFlagUrl, imgOptions);
		}
		if(!password.isEmpty()){
			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			if(tm.getLine1Number().contains(mobileNo)){
				etPassword.setText(password);
			}
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
	}
	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Processing....");
		progressDialog.show();
	}
	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
		
	}
	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		//mobileNo = mobileNo.replace("+", "");
		
		return userManager.getUserLoginAuthentication(LoginActivationActivity.this,mobileNo, etPassword.getText().toString());
	}
	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			/*CommonValues.getInstance().LoginUser = (User) data;*/
			CommonTask.savePreferences(this,CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY,String.valueOf(CommonValues.getInstance().LoginUser.UserNumber)	);
			prefUserPass=CommonTask.getPreferences(this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			
		}else{
			etUserActivationCode.setError("Please enter your valid activation code.");
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{	if (!isFinishing()) 
		{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
		}
		
	}
	
}
