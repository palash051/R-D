package com.vipdashboard.app.activities;

import java.util.Locale;

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

public class TerminateAcivity extends MainActionbarBase implements OnClickListener, IAsynchronousTask {

	ImageView ivContinue, ivOperatorFlag;

	TextView tvCompanyName, tvCompanyCountry,tvFooter;
	EditText etSelectOperatorName, etSelectYourName, etEmail,
			etNumberofTerminate;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terminate);
		
		Iitialization();
	}

	private void Iitialization() {

		ivContinue = (ImageView) findViewById(R.id.ivContinue);
		ivOperatorFlag = (ImageView) findViewById(R.id.ivOperatorFlag);
		
		tvFooter =  (TextView) findViewById(R.id.tvFooter);

		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);

		etSelectOperatorName = (EditText) findViewById(R.id.etSelectOperatorName);
		etSelectYourName = (EditText) findViewById(R.id.etSelectYourName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etNumberofTerminate = (EditText) findViewById(R.id.etNumberofTerminate);
		
		ivContinue.setOnClickListener(this);

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
		ValuesInitialization();
	}

	private void ValuesInitialization() {
		
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString()); 
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		tvFooter.setText("We don’t have service contract with your operator "+tMgr.getNetworkOperatorName().toString()+". Still we will submit your request to the operator through email");
		
		if (!CommonValues.getInstance().SelectedCountry.CountryFlagUrl
				.isEmpty()) {
			AQuery aq = new AQuery(ivOperatorFlag);
			ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
			imgOptions.targetWidth = 100;
			imgOptions.ratio = 0;
			imgOptions.round = 0;
			aq.id(ivOperatorFlag).image(
					CommonValues.getInstance().SelectedCountry.CountryFlagUrl,
					imgOptions);
		}
		
		etSelectOperatorName.setText(tMgr.getNetworkOperatorName());
		//etSelectYourName.setText(CommonValues.getInstance().LoginUser.FullName);
		//etEmail.setText(CommonValues.getInstance().LoginUser.Email);
		//etNumberofTerminate.setText(CommonValues.getInstance().LoginUser.Mobile);
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
				etSelectYourName.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name);
			if(CommonValues.getInstance().LoginUser.Facebook_Person.PrimaryEmail != null)
				etEmail.setText(CommonValues.getInstance().LoginUser.Facebook_Person.PrimaryEmail);
		}else{
			MyNetDatabase database = new MyNetDatabase(this);
			database.open();
			FacebokPerson facebokPerson = database.getFacebokPerson();
			database.close();
			if(facebokPerson != null){
				if(facebokPerson.Name != null)
					etSelectYourName.setText(facebokPerson.Name);
				if(facebokPerson.PrimaryEmail != null)
					etEmail.setText(facebokPerson.PrimaryEmail);
			}
		}

	}
	
	private boolean ValidationInput()
	{
		boolean isValid=true;
		
		if(etSelectYourName.getText().toString().isEmpty())
		{
			Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
	
		if(etEmail.getText().toString().isEmpty())
		{
			Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
		
		if(etNumberofTerminate.getText().toString().isEmpty())
		{
			Toast.makeText(this, "Enter number to be terminated", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
		
		return isValid;
	}

	@Override
	public void onClick(View view) {		
		if(view.getId()==R.id.ivContinue){
				if(ValidationInput())
				LoadInformation();
		}		
	}

	private void LoadInformation() {
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
		return operatorManager.SetOperatorUnSubscribe(etNumberofTerminate.getText().toString(), etSelectYourName.getText().toString(), etEmail.getText().toString(), "0", "0", "0");
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			/*Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Mumtaz Care Terminate");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					"Operator : " + etSelectOperatorName.getText().toString() + "\n"
					+ "Name : " + etSelectYourName.getText().toString() + "\n"
					+ "Email : " + etEmail.getText().toString() + "\n"
					+ "Number of Terminate : " + etNumberofTerminate.getText().toString());
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, CommonValues.getInstance().LoginUser.Company.Email);
			
			emailIntent.setType("plain/text");
			startActivity(Intent.createChooser(emailIntent, "Send"));*/
			Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
	}

}
