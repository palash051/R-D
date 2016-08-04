package com.vipdashboard.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;

public class UsersProfileActivity extends MainActionbarBase implements IAsynchronousTask{
	
	TextView fullName, accountName, emailAddress, phoneNumber, departmentName, designationName;
	ImageView userImage;
	DownloadableAsyncTask downloadAsync;
	ProgressBar pbUserProfile;
	public static UserGroupUnion selectedUserGroupUnion;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
		initialization();
		LoadUserProfile();
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		backTohome();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
	}

	
	
	private void initialization() {
		userImage = (ImageView) findViewById(R.id.image);
		fullName = (TextView) findViewById(R.id.fullname);
		accountName = (TextView) findViewById(R.id.accountName);
		emailAddress = (TextView) findViewById(R.id.tvEmailName);
		phoneNumber = (TextView) findViewById(R.id.tvphoneNumber);
		departmentName = (TextView) findViewById(R.id.tvDeprtmentName);
		designationName = (TextView) findViewById(R.id.tvDesignationName);
		pbUserProfile = (ProgressBar) findViewById(R.id.pbUserProfile);
	}
	
	private void LoadUserProfile() {
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}
	
	@Override
	public void showProgressLoader() {
		pbUserProfile.setVisibility(View.VISIBLE);
	}
	@Override
	public void hideProgressLoader() {
		pbUserProfile.setVisibility(View.GONE);
	}
	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.getUser(String.valueOf(selectedUserGroupUnion.ID));
	}
	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			UserRoot userroot = (UserRoot) data;
			try{
				fullName.setText(userroot.user.FullName);
				accountName.setText(userroot.user.UserID);
				emailAddress.setText(userroot.user.Email);
				phoneNumber.setText(userroot.user.Mobile);
				departmentName.setText(userroot.user.Department);
				designationName.setText(userroot.user.Designation);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
	}

}
