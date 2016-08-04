package com.vipdashboard.app.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Organizations;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.manager.FacebookManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MyNetGooglePlusActivity extends Activity implements 
		ConnectionCallbacks, OnConnectionFailedListener, PlusClient.OnPeopleLoadedListener, IAsynchronousTask, OnClickListener{
    private static final String TAG = "MyNetGooglePlusActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

	
	private String userID;
	private String name;
	private String pp_path;
	private String gender;
	private String rel_status;
	private String dob;
	private String pref_skill;
	private String religion;
	private String otherName;
	private String mobileNumber;
	private String alternative_email;
	private String zipCode;
	private String website;
	private String country;
	private String feedText;
	private String feedTime;
	private String friendsID;
	private String qualification;
	StringBuilder stringBuilder;
	long milliseconds;
	DownloadableAsyncTask downloadableAsyncTask;
	String message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_plus);
		mPlusClient = new PlusClient.Builder(this, this, this)
        .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
        .setScopes(Scopes.PLUS_LOGIN)
        .build();
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		 try {
			    if (status != ConnectionResult.SUCCESS) {
			        GooglePlayServicesUtil.getErrorDialog(status, this,REQUEST_CODE_RESOLVE_ERR).show();
			    }
			} catch (Exception e) {
			    Log.e("Error: GooglePlayServiceUtil: ", "" + e);
			}
		 findViewById(R.id.sign_in_button).setOnClickListener(this);
		 mConnectionProgressDialog = new ProgressDialog(this);
	        
	}
	
	 @Override
	    protected void onStart() {
	        super.onStart();
	        mPlusClient.connect();
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        mPlusClient.disconnect();
	    }
	    

	    @Override
	    public void onConnectionFailed(ConnectionResult result) {
	      if (mConnectionProgressDialog.isShowing()) {
	        // The user clicked the sign-in button already. Start to resolve
	        // connection errors. Wait until onConnected() to dismiss the
	        // connection dialog.
	        if (result.hasResolution()) {
	          try {
	                   result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
	           } catch (SendIntentException e) {
	                   mPlusClient.connect();
	           }
	        }
	      }
	      // Save the result and resolve the connection failure upon a user click.
	      mConnectionResult = result;
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
	        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
	            mConnectionResult = null;
	            mPlusClient.connect();
	        }
	    }

	    @Override
	    public void onConnected(Bundle connectionHint) {
	        String accountName = mPlusClient.getAccountName();
	        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
	        GetInformation(mPlusClient);
	    }

	    @Override
	    public void onDisconnected() {
	        Log.d(TAG, "disconnected");
	    }
	private void LoadInformation() {
		if(downloadableAsyncTask != null){
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	private void GetInformation(PlusClient plusClient) {
		Person currentPerson = plusClient.getCurrentPerson();
		try{
			if(currentPerson.getId() != null){
				userID = currentPerson.getId().toString();
				message += userID + "\r\n";
			}
			if(currentPerson.getName() != null){
				name = currentPerson.getName().getGivenName().toString();
				message += name + "\r\n";
				otherName = currentPerson.getName().getFamilyName().toString();
				message += otherName + "\r\n";
			}
			if(currentPerson.getImage() != null){
				Person.Image img = currentPerson.getImage();
				pp_path = img.getUrl().toString();
				message += pp_path + "\r\n";
			}
			if(currentPerson.getGender() > -1){
				int value = currentPerson.getGender();
				gender = value == 0?"Male":value==1?"Female":"Other";
				message += gender + "\r\n";
			}
			if(currentPerson.getRelationshipStatus() > -1){
				int value = currentPerson.getRelationshipStatus();
				rel_status = value == 0?"Single":value==1?"Relationship":value==2?"Engaged":value==3?"Married":value==4?"Complecated":value==5?"Open Relationship":value==6?"Widow":value==7?"Domestic Relationship":"Civil Union";
				message += rel_status + "\r\n";
			}
			if(currentPerson.getBirthday() != null){
				dob = currentPerson.getBirthday().toString();
				message += dob + "\r\n";
				String[] splitvalue = dob.split("-");
				dob = "";
				dob += splitvalue[2].toString();
				dob += "/";
				dob += splitvalue[1].toString();
				dob += "/";
				dob += splitvalue[0].toString();
				SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
				Date d = f.parse(dob);
				milliseconds = d.getTime();
				if(milliseconds < 0){
					milliseconds = new Date().getTime();
				}
			}else{
				milliseconds = new Date().getTime();
			}
			if(currentPerson.getCurrentLocation() != null){
				
			}
			if(currentPerson.getOrganizations() != null){
				List<Organizations> organizations = currentPerson.getOrganizations();
				for(int i=0;i<organizations.size();i++){
					if(i==0){
						if(organizations.get(i).hasName()){
							pref_skill += organizations.get(i).getName();
						}else{
							pref_skill += "";
						}
						if(organizations.get(i).hasTitle()){
							pref_skill += "~" +  organizations.get(i).getTitle();
						}else{
							pref_skill += "~";
						}
						if(organizations.get(i).hasStartDate()){
							String startDate = "01/01/"+organizations.get(i).getStartDate();
							SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
							Date d = f.parse(startDate);
							milliseconds = d.getTime();
							pref_skill+= "~"+milliseconds;
						}else{
							pref_skill += "~";
						}
						if(organizations.get(i).hasEndDate()){
							String startDate = "01/01/"+organizations.get(i).getEndDate();
							SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
							Date d = f.parse(startDate);
							milliseconds = d.getTime();
							pref_skill+= "~"+milliseconds;
						}else{
							pref_skill += "~";
						}
						pref_skill += "~" + "Education";
					}else{
						pref_skill += "|";
						
					}
					
				}
				message += pref_skill + "\r\n";
			}
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer,
			String nextPageToken) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
	        try {
	            int count = personBuffer.getCount();
	            for (int i = 0; i < count; i++) {
	                Log.d(TAG, "Display Name: " + personBuffer.get(i).getDisplayName());
	            }
	        } finally {
	            personBuffer.close();
	        }
	    } else {
	        Log.e(TAG, "Error listing people: " + status.getErrorCode());
	    }
		
	}
	

	private void popUpInformation() {
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("G+ Information").setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							LoadInformation();
							dialog.cancel();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(MyNetGooglePlusActivity.this, "Information not store", Toast.LENGTH_LONG).show();
							dialog.cancel();
							onBackPressed();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showProgressLoader() {
		mConnectionProgressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		mConnectionProgressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IFacebookManager facebookManager = new FacebookManager();
		return facebookManager.setGPInformation(userID, name, pp_path, gender, rel_status, String.valueOf(milliseconds), religion, pref_skill, otherName);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){		
			Toast.makeText(MyNetGooglePlusActivity.this, "GooglePlus Sync Successfully", Toast.LENGTH_LONG).show();
			onBackPressed();
		}else{
			Toast.makeText(MyNetGooglePlusActivity.this, "GooglePlus Sync UnSuccessfull", Toast.LENGTH_LONG).show();
			onBackPressed();
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
            if (mConnectionResult == null) {
                mConnectionProgressDialog.show();
            } else {
                try {
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (SendIntentException e) {
                    // Try connecting again.
                    mConnectionResult = null;
                    mPlusClient.connect();
                }
            }
        }
	}
}
