package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Random;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CountryAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.MyNetGooglePlusActivity;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.entities.Country;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity implements OnClickListener,
		IAsynchronousTask, ConnectionCallbacks, OnConnectionFailedListener,
		PlusClient.OnPeopleLoadedListener, OnItemSelectedListener {
	public EditText etUserName, etUserPassword;

	Button bLogin;
	TextView signUP, bSignInDemouser,tvCountryCode,tvalreadyPassword;
	ProgressBar pbLogin;
	DownloadableAsyncTask downloadableAsyncTask;
	public static boolean isStateSignUp;
	ProgressDialog progressDialog;

	private String userId, userPassword;
	boolean logoutStatus, isCallFromLoginStatus;
	ImageView googlePlus, linkedIn, facebook,ivNext,ivFlag;
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	int facebookCount = 0;
	public static boolean Login, isCallFromLogout = false;
	
	String userCountryCode="";

	Spinner spCountryCode;
	private Random random;
	private String randomNuber;
	ArrayList<Report> CountryCode;
	String mNumber;
	
	//Country country;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gray_login);	
		
		initializeControls();
		isCallFromLogout = false;

		/*mPlusClient = new PlusClient.Builder(this, this, this)
				.setActions("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity")
				.setScopes(Scopes.PLUS_LOGIN).build();*/
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
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
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		userId = "";
		userPassword = "";
		randomNuber = "";
		mNumber = "";
		Login = true;
		MyProfileOldActivity.myprofileFacebookLogin = false;
		
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		etUserName.setText(tm.getLine1Number());		
		userCountryCode = tm.getSimCountryIso();
		
		processLogoutUserStatus();
		generateRamdomNumber();
		setSpinnerValue();
	}
	
	private void trimCountryCode() {
		
		if(etUserName.getText().toString()!="" && tvCountryCode.getText().toString()!="")
		{
			String processedUserMobileNumber=etUserName.getText().toString().trim().replace(tvCountryCode.getText(), "");
			etUserName.setText(processedUserMobileNumber);
		}
	}
	
	private String trimCountryCode(String mobileNumber) {
		
		String processedUserMobileNumber="";
		
		if(tvCountryCode.getText().toString()!="")
		{
			processedUserMobileNumber=etUserName.getText().toString().trim().replace(tvCountryCode.getText(), "");
			
		}
		
		return processedUserMobileNumber;
	}

	private void setSpinnerValue() {
		//String[] recourseList=this.getResources().getStringArray(R.array.CountryCodes);
		 ArrayList<Country> countryList=CommonTask.getCoutryList();
		spCountryCode.setAdapter(new CountryAdapter(this, R.layout.country_layout,countryList  ));
		
		//Selected Spinner Item with Current Country
		
		if(countryList.size()>0 && !userCountryCode.isEmpty())
		{
			for(int i=0; i<countryList.size();i++)
			{
			   boolean isFound=countryList.get(i).CountryCodeIso.toLowerCase().contains(userCountryCode.toLowerCase());
			   
			   if(isFound)
			   {
				   spCountryCode.setSelection(i);
				   CommonValues.getInstance().SelectedCountry = (Country) spCountryCode.getSelectedItem();				   
				   tvCountryCode.setText(CommonValues.getInstance().SelectedCountry.CountryPhoneCode);
				   
				   trimCountryCode();
				   
				   AQuery aq = new AQuery(ivFlag);		
				
					ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions; 		
					imgOptions.targetWidth=100;
					imgOptions.ratio=0;
					imgOptions.round = 0;
					aq.id(ivFlag).image(CommonValues.getInstance().SelectedCountry.CountryFlagUrl, imgOptions);
				   break;
			   }
			}
		}

	}
	
	

	private void processLogoutUserStatus() {

		logoutStatus = isCallFromLogout;
		if (logoutStatus) {
			if (downloadableAsyncTask != null) {
				downloadableAsyncTask.cancel(true);
			}
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();
		}

	}

	/*@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}*/

	/*
	 * @Override protected void onDestroy() { //stopService(new Intent(this,
	 * MyNetService.class)); super.onDestroy(); }
	 */
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * super.onCreateOptionsMenu(menu); com.actionbarsherlock.view.MenuInflater
	 * menuInflater = getSupportMenuInflater();
	 * menuInflater.inflate(R.menu.menulogin, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { if
	 * (item.getItemId() == android.R.id.home) { super.onBackPressed(); }if
	 * (item.getItemId() == R.id.menu_loging) { Intent intent = new
	 * Intent(this,SettingsActivity.class);
	 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	 * startActivity(intent); }
	 * 
	 * return true; }
	 */

	private void initializeControls() {
		tvalreadyPassword = (TextView)findViewById(R.id.tvalreadyPassword);
		ivNext = (ImageView) findViewById(R.id.ivNext);
		ivFlag= (ImageView) findViewById(R.id.ivFlag);
		etUserName = (EditText) findViewById(R.id.etUserName);
		//etUserPassword = (EditText) findViewById(R.id.etUserPassword);
		//pbLogin = (ProgressBar) findViewById(R.id.pbLogin);
		//bLogin = (Button) findViewById(R.id.bLogin);
		//signUP = (TextView) findViewById(R.id.bSignUP);
		//bSignInDemouser = (TextView) findViewById(R.id.bSignInDemouser);
		tvCountryCode= (TextView) findViewById(R.id.tvCountryCode);
		spCountryCode= (Spinner) findViewById(R.id.spCountryCode);
		
		spCountryCode.setOnItemSelectedListener(this);
		/*CommonTask.makeLinkedTextview(this, signUP, "SIGNUP");
		CommonTask.makeLinkedTextview(this, bSignInDemouser,
				"LOGIN AS A DEMO USER");*/

		/*googlePlus = (ImageView) findViewById(R.id.google_plus);
		linkedIn = (ImageView) findViewById(R.id.linkedIn);
		facebook = (ImageView) findViewById(R.id.facebook_button);*/

		/*bLogin.setOnClickListener(this);
		signUP.setOnClickListener(this);
		bSignInDemouser.setOnClickListener(this);
		googlePlus.setOnClickListener(this);
		linkedIn.setOnClickListener(this);
		facebook.setOnClickListener(this);*/
		
		/*CountryCode = new ArrayList<Report>();

		CountryCode.add(new Report("Bangladesh +880",1));
		CountryCode.add(new Report("UAE +971",2));
		CountryCode.add(new Report("India +91",3));
		 
		  
		ReportAdapter adapter_Link = new ReportAdapter(this, R.layout.lavel_item_layout, CountryCode);
		  spCountryCode.setAdapter(adapter_Link);*/
		
		tvalreadyPassword.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		  
		   
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
			return;
			}
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			return;
			}
		}
		if (id == R.id.ivNext) {
			/*LoginActivationActivity.mobileNo=etUserName.getText().toString().trim();
			Intent intent = new Intent(this, LoginActivationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);*/
			String object = tvCountryCode.getText().toString();//spCountryCode.getSelectedItem().toString();
			if(object.equals("")){
				Toast.makeText(this, "Please select your country code.", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!etUserName.getText().toString().trim().isEmpty()){
				mNumber=etUserName.getText().toString().trim().replace("+", "");
				
				if(mNumber.length()>2 && mNumber.substring(0, 2).toString().equals("00")){
					mNumber = mNumber.substring(2);
				}else if(mNumber.substring(0, 1).toString().equals("0")){
					mNumber = mNumber.substring(1);
				}
				
				String countryCode = object.toString().split(",")[0];
				countryCode=countryCode.replace("+", "");
				
				mNumber=mNumber.replace(countryCode, "");
				if(mNumber.substring(0, 1).toString().equals("0")){
					mNumber = mNumber.substring(1);
				}
				mNumber = countryCode + mNumber;	
				
				CommonValues.getInstance().isCallFromLoginActivity = true;
				isStateSignUp = false;
				userId = mNumber;//etUserName.getText().toString().trim();
				//userPassword = etUserPassword.getText().toString();
				if (downloadableAsyncTask != null) {
					downloadableAsyncTask.cancel(true);
				}
				downloadableAsyncTask = new DownloadableAsyncTask(this);
				downloadableAsyncTask.execute();
			}else{
				etUserName.setError("Please enter your mobile number.");
			}
			
			
		}else if(id == R.id.tvalreadyPassword){
			Intent intent = new Intent(LoginActivity.this, LoginWithPasswordActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		/*else if (id == R.id.bSignUP) {
			isStateSignUp = true;
			Intent intent = new Intent(this, SignUpActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (id == R.id.bSignInDemouser) {
			CommonValues.getInstance().isCallFromLoginActivity = true;
			isStateSignUp = false;
			userId = "demo@oss-net.com";
			userPassword = "demo123";
			if (downloadableAsyncTask != null) {
				downloadableAsyncTask.cancel(true);
			}
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();
			
			 * Intent intent = new Intent(this, NewSelectionFragment.class);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			 * startActivity(intent);
			 
		} */
		/*else if (id == R.id.linkedIn) {

		} else if (id == R.id.google_plus) {
			// mPlusClient.connect();
			// pbLogin.setVisibility(View.VISIBLE);
		} else if (id == R.id.facebook_button) {
			Intent intent = new Intent(this, FaceBookLoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}*/
	}

	@Override
	public void showProgressLoader() {
		/*if (!logoutStatus){
			progressDialog = new ProgressDialog(this);
			progressDialog.setCancelable(true);
			progressDialog.setMessage("Please Wait...");
			progressDialog.show();
		}*/
	}

	@Override
	public void hideProgressLoader() {
		/*if (!logoutStatus){
			progressDialog.dismiss();
		}*/
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		if (!logoutStatus){
			//return userManager.getUserByMobileNumber(etUserName.getText().toString().trim());			
			return userManager.UserSignUp(mNumber, mNumber, mNumber,randomNuber);
			//return userManager.getUserLoginAuthentication(LoginActivity.this,userId, userPassword);
		}
		else
			return userManager.setLogoutUserOnlineStatus();
	}
	private void generateRamdomNumber() {
		random = new Random();
		
		for(int i=0;i<4;i++){
			randomNuber += String.valueOf(random.nextInt(9));
		}
	}
	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (!logoutStatus) {
				/*CommonValues.getInstance().LoginUser = (User) data;
				CommonTask
						.savePreferences(
								this,
								CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY,
								String.valueOf(CommonValues.getInstance().LoginUser.UserNumber)
						
						 * + "|" + CommonValues.getInstance().LoginUser.UserID +
						 * "|" + CommonValues.getInstance().LoginUser.Password +
						 * "|" + CommonValues.getInstance().LoginUser.FirstName
						 );
				Intent intent = new Intent(this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);*/
				//User user=(User)data;
				/*String DELIVERED = "SMS_DELIVERED";
				PendingIntent deliveredPI = PendingIntent.getBroadcast(LoginActivity.this, 0, new Intent(DELIVERED), 0);
				
				registerReceiver(new BroadcastReceiver() {
		            @Override
		            public void onReceive(Context arg0, Intent arg1) {
		                switch (getResultCode()) {
		                case Activity.RESULT_OK:		                	
		    				Intent intent = new Intent(LoginActivity.this, LoginActivationActivity.class);
		    				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    				startActivity(intent);
		    				progressDialog.dismiss();
		                    break;
		                case Activity.RESULT_CANCELED:
		                    break;	
		                }
		            }
		        }, new IntentFilter(DELIVERED));*/
				
				android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
			    smsManager.sendTextMessage("+"+ mNumber.trim(), null, randomNuber + " - your code to login into Care.", null, null);
			    
			    LoginActivationActivity.displayedMobileNo=trimCountryCode(mNumber);
			    LoginActivationActivity.mobileNo=mNumber;
				LoginActivationActivity.password=randomNuber;
				LoginActivationActivity.country_code = tvCountryCode.getText().toString();
				
				
				Intent intent = new Intent(LoginActivity.this, LoginActivationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			} else {
				etUserPassword.setText("");
				User user = (User) data;
				if (user.UserOnlineAvailableStatusID == CommonConstraints.OFFLINE) {
					CommonTask
							.savePreferences(
									this,
									CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY,
									"");
					CommonValues.getInstance().isCallFromLoginActivity = false;
					// bundle = null;
				}

			}
		} else {
			// CommonValues.getInstance().isCallFromLoginActivity = false;
			etUserName.setError("Please enter valid mobile number");
			// etUserPassword.setError("Wrong User Password !!!");
			if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		}
		userId = "";
		userPassword = "";
		logoutStatus = false;
		isCallFromLogout = false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 9000:
			if (requestCode == REQUEST_CODE_RESOLVE_ERR
					&& resultCode == RESULT_OK) {
				mConnectionResult = null;
				mPlusClient.connect();
			}
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener
	 * #onPeopleLoaded(com.google.android.gms.common.ConnectionResult,
	 * com.google.android.gms.plus.model.people.PersonBuffer, java.lang.String)
	 */

	@Override
	public void onPeopleLoaded(ConnectionResult arg0, PersonBuffer arg1,
			String arg2) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (pbLogin.isShown()) {
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this,
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}
		mConnectionResult = result;
	}

	@Override
	public void onConnected(Bundle arg0) {
		pbLogin.setVisibility(View.GONE);
		Intent intent = new Intent(this, MyNetGooglePlusActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public void onItemSelected(AdapterView<?> pv, View v, int position,long location) {
		
		tvCountryCode.setText(v.getTag().toString());
		
		trimCountryCode();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {	
		
	}
}
