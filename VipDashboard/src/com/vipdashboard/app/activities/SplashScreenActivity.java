package com.vipdashboard.app.activities;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CountryAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Country;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Spinner;


public class SplashScreenActivity extends Activity implements IAsynchronousTask{
	protected boolean _active = true;
	protected int _splashTime = 5000;
	DownloadableAsyncTask downloadableAsyncTask;
	String prefUserPass;
	
	String userPhoneNumber,userCountryCode;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		
		
		//Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(SplashScreenActivity.this));
		
		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(10);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					
				} finally {					
					//SplashScreenActivity.this.finish();
					prefUserPass=CommonTask.getPreferences(SplashScreenActivity.this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
					//prefUserPass="1";
					if(prefUserPass.equals("")){
						
						// Include Login process(START)
						
						Intent intent = new Intent(SplashScreenActivity.this,
								GetStarted.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						
						// Include Login process(END)
						
						// Eliminate Login process(START)
						
					/*	Log.i("1", "1");
						TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
						userCountryCode=tm.getSimCountryIso();
						
						userPhoneNumber=tm.getSimCountryIso()+"-"+(tm.getLine1Number().equalsIgnoreCase("")==true?tm.getSimSerialNumber() :tm.getLine1Number());
						
						ArrayList<Country> countryList=CommonTask.getCoutryList();
						String userCountryCode = tm.getSimCountryIso();
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
						
						
						if (downloadableAsyncTask != null) {
							downloadableAsyncTask.cancel(true);
						}
						downloadableAsyncTask = new DownloadableAsyncTask(SplashScreenActivity.this);
						downloadableAsyncTask.execute();*/
						
						// Eliminate Login process(END)
						
						
					}else{
						ArrayList<Country> countryList=CommonTask.getCoutryList();
						TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
						String userCountryCode = tm.getSimCountryIso();
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
						
						Intent intent = new Intent(SplashScreenActivity.this,
								HomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
						startActivity(intent);
						/*if(CommonValues.getInstance().LoginUser==null){
							if (downloadableAsyncTask != null) {
								downloadableAsyncTask.cancel(true);
							}
							downloadableAsyncTask = new DownloadableAsyncTask(SplashScreenActivity.this);
							downloadableAsyncTask.execute();
							CommonValues.getInstance().LoginUser=new User();
							CommonValues.getInstance().LoginUser.UserNumber=Integer.parseInt(prefUserPass.split("\\|")[0]);
							CommonValues.getInstance().LoginUser.UserID=prefUserPass.split("\\|")[1];
							CommonValues.getInstance().LoginUser.Password=prefUserPass.split("\\|")[2];
							CommonValues.getInstance().LoginUser.FirstName=prefUserPass.split("\\|")[3];
							
						}*/
					}
				}
			}
		};
		splashTread.start();
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
	}
	
	/*@Override
	 protected void onDestroy() {
	  stopService(new Intent(this, MyNetService.class));
	  super.onDestroy();
	 }*/

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

	@Override
	public void showProgressLoader() {
		/*progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		//progressDialog.setIndeterminate(true);
		//progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progrress_bar));
		progressDialog.setMessage(processingMessage);
		//progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//progressDialog.requestWindowFeature(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		//progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		/*IUserManager userManager = new UserManager();
		String user=prefUserPass.split("\\|")[1];
		String pass=prefUserPass.split("\\|")[2];
		return userManager.getUserLoginAuthentication(SplashScreenActivity.this, user, pass);		*/
		Log.i("2", "3");
		IUserManager userManager = new UserManager();
		return userManager.getUserLoginAuthentication(SplashScreenActivity.this,userPhoneNumber, "1234");
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			Log.i("2", "4");
			CommonValues.getInstance().LoginUser = (User) data;
			
			CommonTask.savePreferences(this,CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY,String.valueOf(CommonValues.getInstance().LoginUser.UserNumber)	);
			
			prefUserPass=CommonTask.getPreferences(this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
			
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}
}
