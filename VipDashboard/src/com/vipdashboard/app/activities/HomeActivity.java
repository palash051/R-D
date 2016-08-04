package com.vipdashboard.app.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.vipdashboard.app.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.Country;
import com.vipdashboard.app.entities.FacebokPerson;
import com.vipdashboard.app.entities.FacebookQualificationExperience;
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

public class HomeActivity extends MainActionbarBase implements
		 OnClickListener, OnTouchListener, IAsynchronousTask, LocationListener {

	RelativeLayout rlPHomePageMyDashBoard, rlMyDevice, rlCompanyName,
			rlPHomepageReportProblem,rlPHomePageLounge,
			rlUtilities;
	int LoginUserNumber = 0;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	
	LinearLayout llAnimation;
	Animation animation_down, animation_up;
	
	String OperatorName="", MCC="", MNC="", OperatorCountryISO="";
	

	ProgressBar pbHome;
	
	ImageView facebook;
	
	TelephonyManager telMgr;
	// private SensorManager sensorMgr;
	// boolean accelSupported;
	private long lastUpdate = -1;
	private float x, y, z;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 800;
	
	int fb_no = 0;

	TextView tvCompanyName, tvCompanyCountry, tvOperatorName;
	
	ImageView ivHomePageLounge,ivUtilities,ivHomepageTechnicalReports,ivHomepageUsagesHistry,ivHomePageMyDashBoard,ivNetworkLatest;
	TextView tvHomeAppbaseTitle,tvUtilities,tvHomePageMyDashBoard,tvMyDevice,tvPHomepageReportProblem;
	
	
	String processingMessage="Configuring settings...";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);

		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		}
		telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == telMgr.getSimState()) {

			showMessage("Mobile SIM card is not installed.\nPlease install it.");
		}else{

			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			if (!locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				showMessage("Your Location service is not available.\nPlease enable first.");
			} else {	
				// setContentView(R.layout.homepage);
				setContentView(R.layout.vipd_homepage);
				// mSupportActionBar.setDisplayHomeAsUpEnabled(true);
	
				initialization();
				
				ArrayList<Country> countryList=CommonTask.getCoutryList();
				String userCountryCode = telMgr.getSimCountryIso();
				if(countryList.size()>0 && !userCountryCode.isEmpty())
				{
					Country country=null;
					for(int i=0; i<countryList.size();i++)
					{
						country=countryList.get(i);
					   if(country.CountryCodeIso.toLowerCase().contains(userCountryCode.toLowerCase()))
					   {
						   CommonValues.getInstance().SelectedCountry = country;				   
						   break;
					   }
					}
				}
				
				if (savedInstanceState != null
						&& savedInstanceState.containsKey("LoginUserNumber")) {
					LoginUserNumber = savedInstanceState.getInt("LoginUserNumber",
							0);
					if (LoginUserNumber > 0) {
						//startService(new Intent(this, MyNetService.class));
						//processingMessage="Initializing Settings...";
						
						if (downloadableAsyncTask != null) {
							downloadableAsyncTask.cancel(true);
						}
						downloadableAsyncTask = new DownloadableAsyncTask(
								HomeActivity.this);
						downloadableAsyncTask.execute();
					}
				} else {
					//startService(new Intent(this, CareIMService.class));
					startService(new Intent(this, MyNetService.class));
					
					tvCompanyCountry.setText(MyNetService.currentCountryName);					
					tvCompanyCountry.setText(MyNetService.currentCountryName);
				}
	
				// startService(new Intent(this, MyNetService.class));
	
			}
		}
	}

	private void initialization() {
		/************************ New design *********************/
		rlPHomePageMyDashBoard = (RelativeLayout) findViewById(R.id.rlPHomePageMyDashBoard);
		rlMyDevice = (RelativeLayout) findViewById(R.id.rlMyDevice);
		rlCompanyName = (RelativeLayout) findViewById(R.id.rlCompanyName);
		rlPHomepageReportProblem = (RelativeLayout) findViewById(R.id.rlPHomepageReportProblem);
		tvOperatorName = (TextView) findViewById(R.id.tvOperatorName);
		rlPHomePageLounge= (RelativeLayout) findViewById(R.id.rlPHomePageLounge);
		rlUtilities = (RelativeLayout) findViewById(R.id.rlUtilities);
		
		tvOperatorName = (TextView) findViewById(R.id.tvOperatorName);
   		ivHomePageMyDashBoard= (ImageView) findViewById(R.id.ivHomePageMyDashBoard);
		
		llAnimation = (LinearLayout) findViewById(R.id.llAnimation);
		
		animation_down = AnimationUtils.loadAnimation(this, R.drawable.slide_down);
		animation_up = AnimationUtils.loadAnimation(this, R.drawable.slide_up);

		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		
		ivHomePageLounge = (ImageView) findViewById(R.id.ivHomePageLounge);
		ivUtilities= (ImageView) findViewById(R.id.ivUtilities);
		ivHomepageTechnicalReports= (ImageView) findViewById(R.id.ivHomepageTechnicalReports);
		ivHomepageUsagesHistry= (ImageView) findViewById(R.id.ivHomepageUsagesHistry);
		ivNetworkLatest= (ImageView) findViewById(R.id.ivNetworkLatest);
		
		tvHomeAppbaseTitle = (TextView) findViewById(R.id.tvHomeAppbaseTitle);
		tvUtilities= (TextView) findViewById(R.id.tvUtilities);
		tvHomePageMyDashBoard= (TextView) findViewById(R.id.tvHomePageMyDashBoard);
		tvMyDevice= (TextView) findViewById(R.id.tvMyDevice);
		tvPHomepageReportProblem= (TextView) findViewById(R.id.tvPHomepageReportProblem);
		facebook = (ImageView) findViewById(R.id.facebook_button);
		
		rlPHomePageMyDashBoard.setOnClickListener(this);
		rlMyDevice.setOnClickListener(this);
		rlCompanyName.setOnClickListener(this);
		rlPHomepageReportProblem.setOnClickListener(this);
		rlPHomePageLounge.setOnClickListener(this);
		rlUtilities.setOnClickListener(this);
		facebook.setOnClickListener(this);

		
		rlPHomePageMyDashBoard.setOnTouchListener(this);
		rlMyDevice.setOnTouchListener(this);
		rlCompanyName.setOnTouchListener(this);
		rlPHomepageReportProblem.setOnTouchListener(this);
		rlPHomePageLounge.setOnTouchListener(this);
		rlUtilities.setOnTouchListener(this);
		 

		/***********************************************/
		pbHome = (ProgressBar) findViewById(R.id.pbHome);

	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();

		super.onPause();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		String networkOperator = tMgr.getNetworkOperator();
		
		if (networkOperator != null) {
			
			
			
			MCC = String.valueOf(Integer.parseInt(networkOperator.substring(0, 3)));
			MNC = String.valueOf(Integer.parseInt(networkOperator.substring(3)));
			
			
			/*MCC = networkOperator.substring(0, 3);
			MNC = networkOperator.substring(3);*/
		}
		
		OperatorName = tMgr.getNetworkOperatorName().toString();
		OperatorCountryISO = tMgr.getSimCountryIso().toString();
		
		
		//llAnimation.startAnimation(animation_down);
		if(tvCompanyName==null)
			return;
		tvCompanyName.setText(telMgr.isNetworkRoaming()==true?telMgr.getNetworkOperatorName().toString()+"(Roaming)":telMgr.getNetworkOperatorName().toString());
		tvOperatorName.setText(telMgr.getNetworkOperatorName().toString().toUpperCase()
				+ " CARE");
		Locale l = new Locale("", telMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		setBackgroundColor();

	}

	private void setBackgroundColor() {
		findViewById(R.id.rlPHomePageLounge).setBackgroundColor(Color.parseColor("#2182F9"));
			 findViewById(R.id.rlUtilities).setBackgroundColor(Color.parseColor("#000099"));
			 findViewById(R.id.rlPHomePageMyDashBoard).setBackgroundColor(Color.parseColor("#FF33CC"));
			 findViewById(R.id.rlMyDevice).setBackgroundColor(Color.parseColor("#FF9900"));
			 findViewById(R.id.rlCompanyName).setBackgroundColor(Color.parseColor("#00B050"));
			 findViewById(R.id.rlPHomepageReportProblem).setBackgroundColor(Color.parseColor("#CC3300"));
			 
			 final float scale = getResources().getDisplayMetrics().density;
		   		
			 
			ivHomePageLounge.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		   	ivHomePageLounge.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
		   	tvHomeAppbaseTitle.setTextSize(CommonValues.COMMON_TEXT_SIZE);
		   	
	   		ivUtilities.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		ivUtilities.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		tvUtilities.setTextSize(CommonValues.COMMON_TEXT_SIZE);
	   		
	   		ivHomePageMyDashBoard.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		ivHomePageMyDashBoard.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		tvHomePageMyDashBoard.setTextSize(CommonValues.COMMON_TEXT_SIZE);
	   		
	   		ivNetworkLatest.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		ivNetworkLatest.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		tvMyDevice.setTextSize(CommonValues.COMMON_TEXT_SIZE);
	   		
	   		ivHomepageTechnicalReports.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		ivHomepageTechnicalReports.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		tvOperatorName.setTextSize(CommonValues.COMMON_TEXT_SIZE);
	   		
	   		ivHomepageUsagesHistry.getLayoutParams().height = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		ivHomepageUsagesHistry.getLayoutParams().width = (int) (CommonValues.COMMON_PICTURE_HEIGHT_WIDTH  * scale + 0.5f);
	   		tvPHomepageReportProblem.setTextSize(CommonValues.COMMON_TEXT_SIZE);
			 
			 
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlPHomePageMyDashBoard) {
			Intent intent = new Intent(this, VIPD_MyDashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);

		} else if (view.getId() == R.id.rlMyDevice) {
			Intent intent = new Intent(this, VIPD_MyDeviceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);

		} else if (view.getId() == R.id.rlCompanyName) {
			Intent intent = new Intent(this, ProblemTrackingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);

		} else if (view.getId() == R.id.rlPHomepageReportProblem) {
			Intent intent = new Intent(this, AssistanceReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
		else if (view.getId() == R.id.rlPHomePageLounge) {
			if (!CommonTask.isOnline(this)) {
				if (!isFinishing()) 
				{
				Toast.makeText(
						this,
						"No Internet Connection.\nPlease enable your connection first",
						Toast.LENGTH_SHORT).show();
				return;
				}
			} 
			
		/*	if(CommonValues.getInstance().ConatactUserList==null)
			{
				Toast.makeText(
						this,
						"Lounge is not prepare yet, Please wait a while or you can make manual sync to make it manually.",
						Toast.LENGTH_SHORT).show();
				return;
			}*/
			
			if(CommonValues.getInstance().LoginUser.MDUserStatusID==CommonConstraints.WAITING_FOR_APPROVAL)
			{
				
			}
			
			
			//Intent intent = new Intent(this, ExperinceLiveFeedActivity.class);
			Intent intent = new Intent(this, VIPD_CheckupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if (view.getId() == R.id.rlUtilities) {
			Intent intent = new Intent(this, UtilityActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
		else if(view.getId() == R.id.facebook_button){
			FaceBookLoginActivity.IsCalledFromProfile=true;
	    	Intent intent = new Intent(this, FaceBookLoginActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
	    }
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		//progressDialog.setIndeterminate(true);
		//progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progrress_bar));
		progressDialog.setMessage(processingMessage);
		//progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//progressDialog.requestWindowFeature(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		
		IOperatorManager operatorManager= new OperatorManager();
		Object licenseInfo = operatorManager.getOperatorLicenseByCombinedID(OperatorName, MCC, MNC, OperatorCountryISO);
		
		if(licenseInfo!=null)
		{
			OperatorLicense operatorLicense=(OperatorLicense)licenseInfo;
			
			if(operatorLicense!=null)
			{
				 CommonValues.IsOpeatorLicensed=operatorLicense.IsActivateLicensed;
				 CommonValues.OperatorHelpDeskMobileNo=operatorLicense.HelpDeskMobileNo;
				 CommonValues.OperatorHelpDeskEmail=operatorLicense.HelpDeskEmailAddress;
				 CommonValues.OpeatorLicensedID=operatorLicense.OperatorLicenseID;
			}
		}	
		
		return userManager.GetUserByID(LoginUserNumber);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			CommonValues.getInstance().LoginUser = (User) data;
			MyNetDatabase db=new MyNetDatabase(this);
			CommonValues.getInstance().ConatactUserList=db.GetUserHashMap();
			//startService(new Intent(this, CareIMService.class));
			startService(new Intent(this, MyNetService.class));
			
			/*if(CommonValues.getInstance().LoginUser.userSettingList.size()>0)
			{
				CommonConstraints.IsRecordVoiceMemo=CommonValues.getInstance().LoginUser.userSettingList.get(0).IsRecordVoiceMemo;
				CommonConstraints.IsPromotTextMemo=CommonValues.getInstance().LoginUser.userSettingList.get(0).IsPromptTextMemo;
			}*/
			
			if(CommonValues.getInstance().LoginUser.Facebook_Person != null && CommonValues.getInstance().LoginUser.Facebook_Person.FB_UserID!=null){
				FacebokPerson facebookPerson = new FacebokPerson();
				
				facebookPerson.FB_UserID=CommonValues.getInstance().LoginUser.Facebook_Person.FB_UserID;
				facebookPerson.FB_UserName=CommonValues.getInstance().LoginUser.Facebook_Person.FB_UserName;
				facebookPerson.Name=CommonValues.getInstance().LoginUser.Facebook_Person.Name;
				facebookPerson.PrimaryEmail=CommonValues.getInstance().LoginUser.Facebook_Person.PrimaryEmail;
				facebookPerson.PP_Path=CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path;
				facebookPerson.Gender=CommonValues.getInstance().LoginUser.Facebook_Person.Gender;
				facebookPerson.Relationship_Status=CommonValues.getInstance().LoginUser.Facebook_Person.Relationship_Status;
				facebookPerson.DateOfBirth=CommonValues.getInstance().LoginUser.Facebook_Person.DateOfBirth;
				facebookPerson.Religion=CommonValues.getInstance().LoginUser.Facebook_Person.Religion;
				facebookPerson.Professional_Skills=CommonValues.getInstance().LoginUser.Facebook_Person.Professional_Skills;
				facebookPerson.About=CommonValues.getInstance().LoginUser.Facebook_Person.About;
				facebookPerson.Pages=CommonValues.getInstance().LoginUser.Facebook_Person.Pages;
				facebookPerson.Groups=CommonValues.getInstance().LoginUser.Facebook_Person.Groups;
				facebookPerson.Apps	=CommonValues.getInstance().LoginUser.Facebook_Person.Apps;
				
				FacebokPerson _facebookPerson = db.getFacebokPerson();
				if(_facebookPerson.FBNo <= 0)
					fb_no = db.CreateFacebookPerson(facebookPerson);
				db.close();
				
				if(CommonValues.getInstance().LoginUser.Facebook_Person.FacebookQualificationExperience.size()>0){
					db.open();
					db.deleteFacebook_Qualification_Experience();
					db.close();
					
					for(int i=0;i<CommonValues.getInstance().LoginUser.Facebook_Person.FacebookQualificationExperience.size();i++){
						FacebookQualificationExperience facebookQualificationExperience = new FacebookQualificationExperience();
						facebookQualificationExperience.QualificationExperience = CommonValues.getInstance().LoginUser.Facebook_Person.FacebookQualificationExperience.get(i).QualificationExperience;
						facebookQualificationExperience.Position=CommonValues.getInstance().LoginUser.Facebook_Person.FacebookQualificationExperience.get(i).Position;
						facebookQualificationExperience.Duration_To = CommonValues.getInstance().LoginUser.Facebook_Person.FacebookQualificationExperience.get(i).Duration_To;
						facebookQualificationExperience.Duration_From=CommonValues.getInstance().LoginUser.Facebook_Person.FacebookQualificationExperience.get(i).Duration_From;
						facebookQualificationExperience.QualificationExperienceType=CommonValues.getInstance().LoginUser.Facebook_Person.FacebookQualificationExperience.get(i).QualificationExperienceType;
						facebookQualificationExperience.FBNo=fb_no;
					
						db.open();
						db.CreateFacebook_Qualification_Experience(facebookQualificationExperience);
						db.close();
					}
				}
			}
			
			if(MyNetService.currentCountryName.isEmpty()||MyNetService.currentCountryName=="")
			{
				getLocationAddress();
			}
			tvCompanyCountry.setText(MyNetService.currentCountryName);
		} else {
			if (!CommonTask.isOnline(this)) {
				if (!isFinishing()) 
				CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
			}
		}
	}
	
	private void getLocationAddress() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		String addressText = "";
		if (loc != null) {

			Geocoder geocoder = new Geocoder(this, Locale.getDefault());

			try {
				List<Address> addresses = geocoder.getFromLocation(
						loc.getLatitude(), loc.getLongitude(), 1);
				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					for (int lineIndex = 0; lineIndex < address
							.getMaxAddressLineIndex(); lineIndex++) {
						addressText = addressText
								+ address.getAddressLine(lineIndex) + ", ";
					}
					addressText = addressText + address.getLocality() + ", "
							+ address.getCountryName();
					
					MyNetService.currentCountryName=address.getCountryName();
				}
				

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	
	
	private void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(HomeActivity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						startActivity(new Intent(
								android.provider.Settings.ACTION_SETTINGS));
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	  @Override 
	  public boolean onTouch(View view, MotionEvent arg1)
	  {   
		   findViewById(view.getId()).setBackgroundColor(Color.parseColor("#FFC000"));
		   
		   final float scale = getResources().getDisplayMetrics().density;
		   
		   switch(view.getId())
		   { 
			   	case R.id.rlPHomePageLounge:
			   		ivHomePageLounge.getLayoutParams().height = (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		ivHomePageLounge.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		tvHomeAppbaseTitle.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
				   break;
			   	case R.id.rlUtilities:
			   		ivUtilities.getLayoutParams().height = (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		ivUtilities.getLayoutParams().width = (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		tvUtilities.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
			   	case R.id.rlPHomePageMyDashBoard:
			   		ivHomePageMyDashBoard.getLayoutParams().height =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		ivHomePageMyDashBoard.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		tvHomePageMyDashBoard.setTextSize(13);
					   break;
			   	case R.id.rlMyDevice:
			   		ivNetworkLatest.getLayoutParams().height = (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		ivNetworkLatest.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		tvMyDevice.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
			   	case R.id.rlCompanyName:
			   		ivHomepageTechnicalReports.getLayoutParams().height = (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		ivHomepageTechnicalReports.getLayoutParams().width =  (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		tvOperatorName.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
			   	case R.id.rlPHomepageReportProblem:
			   		ivHomepageUsagesHistry.getLayoutParams().height = (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH * scale + 0.5f);
			   		ivHomepageUsagesHistry.getLayoutParams().width = (int) (CommonValues.COMMON_HOVER_PICTURE_HEIGHT_WIDTH* scale + 0.5f);
			   		tvPHomepageReportProblem.setTextSize(CommonValues.COMMON_HOVER_TEXT_SIZE);
					   break;
		   }
		   return false; 
	  }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
