package com.vipdashboard.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class FacebookSyncPreviewActivity extends Activity implements OnClickListener {
	
	ImageView ivSyncWithOutFacebook, ivSyncWithFacebook,ivProfilePicture;
	private String prefUserPass;
	private AQuery aq;
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_sync_preview);
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
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
		if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		LoadImage();
	}

	private void Initialization() {
		ivSyncWithOutFacebook = (ImageView) findViewById(R.id.ivSyncWithOutFacebook);
		ivSyncWithFacebook = (ImageView) findViewById(R.id.ivSyncWithFacebook);
		ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
		
		aq = new AQuery(this);
		
		imageLoader = new ImageLoader(this);		
		imgOptions = CommonValues.getInstance().defaultImageOptions; 		
		imgOptions.targetWidth=100;
		imgOptions.ratio=0;//AQuery.RATIO_PRESERVE;
		imgOptions.round = 8;
		
		ivSyncWithOutFacebook.setOnClickListener(this);
		ivSyncWithFacebook.setOnClickListener(this);
	}
	
	private void LoadImage(){
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			aq.id(ivProfilePicture).image(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, imgOptions);
		}
		else
		{
			int photoId=CommonTask.getContentPhotoId(this, CommonValues.getInstance().LoginUser.Mobile);
			if(photoId>0){
				ivProfilePicture.setImageBitmap(CommonTask.fetchContactImageThumbnail(this,photoId));
			}
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivSyncWithOutFacebook){
			prefUserPass=CommonTask.getPreferences(this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
			Intent intent = new Intent(this,
					HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
			startActivity(intent);
		}else if(view.getId() == R.id.ivSyncWithFacebook){
			Intent intent = new Intent(this,FaceBookLoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
}
