package com.vipdashboard.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.FacebokPerson;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IOperatorManager;
import com.vipdashboard.app.manager.OperatorManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class OperatorMailActivity extends MainActionbarBase implements OnClickListener, IAsynchronousTask {
	
	ImageView ivCountryFlag,ivContinue;
	TextView tvCompanyName, tvCompanyCountry,tvFooter;
	TextView tvSelectCountryName;
	EditText etYourName, etEmail, etExistingnumber,etMailBody,etSubject;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	String imageFilePath="";
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operator_mail);
		
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
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		
		SetValue();
	}

	private void Initialization() {
		ivCountryFlag = (ImageView) findViewById(R.id.ivCountryFlag);
		ivContinue = (ImageView) findViewById(R.id.ivContinue);
		tvSelectCountryName = (TextView) findViewById(R.id.tvSelectCountryName);
		etYourName = (EditText) findViewById(R.id.etYourName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etExistingnumber = (EditText) findViewById(R.id.etExistingnumber);
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		tvFooter = (TextView) findViewById(R.id.tvFooter);
		etSubject= (EditText) findViewById(R.id.etSubject);
		etMailBody= (EditText) findViewById(R.id.etMailBody);
		
		ivContinue.setOnClickListener(this);
		
	}
	
	private void SetValue(){
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvSelectCountryName.setText(tMgr.getNetworkOperatorName());
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
				etYourName.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name);
		}else{
			MyNetDatabase database = new MyNetDatabase(this);
			database.open();
			FacebokPerson facebokPerson = database.getFacebokPerson();
			database.close();
			if(facebokPerson != null){
				if(facebokPerson.Name != null)
					etYourName.setText(facebokPerson.Name);
			}
		}
		
		if(CommonValues.OperatorHelpDeskEmail!="Not Available")
		etEmail.setText(CommonValues.OperatorHelpDeskEmail);
		etExistingnumber.setText(CommonValues.getInstance().LoginUser.Mobile!=null?CommonValues.getInstance().LoginUser.Mobile:"");
		
		if (!CommonValues.getInstance().SelectedCountry.CountryFlagUrl
				.isEmpty()) {
			AQuery aq = new AQuery(ivCountryFlag);
			ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
			imgOptions.targetWidth = 100;
			imgOptions.ratio = 0;
			imgOptions.round = 0;
			aq.id(ivCountryFlag).image(
					CommonValues.getInstance().SelectedCountry.CountryFlagUrl,
					imgOptions);
		}
		
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		tvFooter.setText("We don’t have service contract with your operator "+tMgr.getNetworkOperatorName().toString()+".  Still we will submit your request to the operator through email");
		
	}
	
	private boolean ValidationInput()
	{
		boolean isValid=true;
		
		/*if(etYourName.getText().toString().isEmpty())
		{
			Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}*/
	
		if(etEmail.getText().toString().isEmpty())
		{
			Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
		
	/*	else if(etExistingnumber.getText().toString().isEmpty())
		{
			Toast.makeText(this, "Enter your mobile no", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}*/
		
		else if(etMailBody.getText().toString().isEmpty())
		{
			Toast.makeText(this, "Did you forgot to write message?", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
		
		return isValid;
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivContinue){
			if(ValidationInput())
			{
				LoadInformation();
			}
		}
		
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Submitting to operator...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IOperatorManager operatorManager = new OperatorManager();
		return operatorManager.setOperatorMail(String.valueOf(MyNetService.phoneId),CommonValues.OperatorHelpDeskEmail, ""
				, "conio.innovation@gmail.com", String.valueOf(etSubject.getText()),String.valueOf(etMailBody.getText()), "", ""
				, String.valueOf(CommonValues.getInstance().LoginUser.UserNumber),String.valueOf(CommonValues.OpeatorLicensedID), String.valueOf(etYourName.getText()), String.valueOf(etExistingnumber.getText()));
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		
	}

}
