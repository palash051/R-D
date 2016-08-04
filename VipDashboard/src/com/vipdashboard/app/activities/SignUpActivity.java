package com.vipdashboard.app.activities;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.TempTableUserRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;


public class SignUpActivity extends Activity implements OnClickListener, IAsynchronousTask{
	

	EditText name,email,phone;
	Button send;
	DownloadableAsyncTask downloadAsync;
	ProgressBar pbSignUp;
	ProgressDialog progressDialog;
	boolean isCallFromLogin;
	boolean isSignUPButtonPressed = false;
	boolean isCallFromEmailValidation = false;
	boolean isCallFromCompany = true;
	private Random random;
	private String randomNuber;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_new_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
		randomNuber = "";
		generateRamdomNumber();
	}
	
	private void generateRamdomNumber() {
		random = new Random();
		
		for(int i=0;i<4;i++){
			randomNuber += String.valueOf(random.nextInt(9));
		}
	}
	
	

	private void Initialization() {
		name = (EditText) findViewById(R.id.etUserName);
		email = (EditText) findViewById(R.id.etUserCompanyEmail);
		phone = (EditText) findViewById(R.id.etUserPhoneNumber);
		send = (Button) findViewById(R.id.bSignUp);
		pbSignUp = (ProgressBar) findViewById(R.id.pbSignUp);
		
		TextView tvTermsCondotion = (TextView) findViewById(R.id.tvTermsAndCondition);
		CommonTask.makeLinkedTextview(this, tvTermsCondotion, tvTermsCondotion.getText().toString());
		tvTermsCondotion.setOnClickListener(this);
		send.setOnClickListener(this);
		isCallFromLogin = CommonValues.getInstance().isCallFromLoginActivity;
		isSignUPButtonPressed = false;
	}
	
	private void saveUserInformation() {
		if(downloadAsync != null){
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}
	
	@Override
	public void showProgressLoader() {
		//if(isCallFromLogin)
		//pbSignUp.setVisibility(View.VISIBLE);
		/*progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		//if(isCallFromLogin)
		//progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.UserSignUp(name.getText().toString().trim(), email.getText().toString().trim(), phone.getText().toString().trim(),randomNuber);
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(isSignUPButtonPressed){
				isSignUPButtonPressed = false;
				TempTableUserRoot tempUserTables = (TempTableUserRoot) data;
				if(tempUserTables.tempTableUser.UserID == null){
					Toast.makeText(this, "Password Already Send!", Toast.LENGTH_LONG).show();
					onBackPressed();
				}else{
					try{
						android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
					    smsManager.sendTextMessage(phone.getText().toString(), null, randomNuber + " - your code to login into Care.", null, null);
					    Toast.makeText(this, "A password has been send in your phone number.", Toast.LENGTH_LONG).show();
						/*onBackPressed();*/
					    LoginActivationActivity.mobileNo=phone.getText().toString().trim();
						Intent intent = new Intent(this, LoginActivationActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					}catch (Exception e) {
						e.printStackTrace();
					}	
				}
				
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.bSignUp){
			isSignUPButtonPressed = true;
			CheckValidation();
		}else if(id == R.id.tvTermsAndCondition){
			popUpTermsAndCondition();
		}
	}

	private void popUpTermsAndCondition() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.terms_and_condition);
		dialog.setCancelable(false);
		Button bTermsAndConditionclose = (Button) dialog.findViewById(R.id.bTermsAndCondition);
		bTermsAndConditionclose.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void CheckValidation() {
		if(!name.getText().toString().equals("")){
			if(!email.getText().toString().trim().equals("") && 
					android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
				if(!phone.getText().toString().equals("")){
					showConfirmation();
				}else{
					phone.setError("Enter your Phone Number!");
				}
			}else{
				email.setError("Enter Company Email Address!");
			}
		}else{
			name.setError("Enter your name!");
		}
	}
	
	private void showConfirmation(){
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.phone_number_confirmation);
		//dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		TextView phoneNumber = (TextView) dialog.findViewById(R.id.tvPhoneNumber);
		TextView tvEditNumber = (TextView) dialog.findViewById(R.id.tvEditNumber);
		TextView tvYes = (TextView) dialog.findViewById(R.id.tvYes);
		
		phoneNumber.setText(phone.getText().toString());
		
		tvEditNumber.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		tvYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveUserInformation();
			}
		});
		
		dialog.show();
	}
}
