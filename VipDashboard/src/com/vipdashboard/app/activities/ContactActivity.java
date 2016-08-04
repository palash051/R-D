package com.vipdashboard.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.OperatorLicense;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnionEntityHolder;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IOperatorManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.OperatorManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactActivity extends MainActionbarBase implements
		OnClickListener,IAsynchronousTask {

	DownloadableAsyncTask downloadableAsyncTask;
	public static UserGroupUnion selectUserGroupUnion;
	ImageView ivProfilePicture,ivFavoriteNormal,ivFavoriteSelected;

	TextView tvUserName, tvMessageToContact, tvMobile, tvOnlineStatus, tvEmail,
			tvClearConversion;
	
	ProgressDialog progressDialog;
	

	private AQuery aq;
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	FrameLayout frameLayout;
	public static int searchFilterFlag = 0;

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactview);

		Initialization();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		ChangeUserInfo();
		ChangeUserImage();
		
		LoadInforamtion();
	}
	
	private void LoadInforamtion() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	private void ChangeUserInfo() {
		tvUserName.setText(selectUserGroupUnion.Name);
		tvMessageToContact.setText("Message " +selectUserGroupUnion.Name);
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + selectUserGroupUnion.Name.substring(3) +"%'", null, null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			tvUserName.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			
			tvMessageToContact.setText("Message " +cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			tvMobile.setText("Mobile :"+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		}
		
		setUserStatus(selectUserGroupUnion.userOnlinestatus);
	}
	
	private void ChangeUserImage() {
		if(selectUserGroupUnion.Type.equals("U")){
			ContentResolver cr = getContentResolver();
			Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + selectUserGroupUnion.Name.substring(3) +"%'", null, null);
			
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				Bitmap bit=CommonTask.fetchContactImageThumbnail(this,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
				if(bit!=null)
					ivProfilePicture.setImageBitmap(bit);
				else{
					ivProfilePicture.setImageResource(R.drawable.user_icon);
				}
			}else{
				ivProfilePicture.setImageResource(R.drawable.user_icon);
			}
			cursor.close();
		}else{
			ivProfilePicture.setImageResource(R.drawable.user_group);
		}
	}

	private void Initialization() {
		ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvMessageToContact = (TextView) findViewById(R.id.tvMessageToContact);
		tvMobile = (TextView) findViewById(R.id.tvMobile);
		tvOnlineStatus = (TextView) findViewById(R.id.tvOnlineStatus);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvClearConversion = (TextView) findViewById(R.id.tvClearConversion);
		
		ivFavoriteNormal= (ImageView) findViewById(R.id.ivFavoriteNormal);
		ivFavoriteSelected= (ImageView) findViewById(R.id.ivFavoriteSelected);

		tvUserName.setOnClickListener(this);
		tvMessageToContact.setOnClickListener(this);
		tvClearConversion.setOnClickListener(this);
		
	}

	private void setUserStatus(long userOnlineAvailableStatusID) {
		switch ((int) userOnlineAvailableStatusID) {
		case 1:
			tvOnlineStatus.setText("Status : Online");
			break;
		case 2:
			tvOnlineStatus.setText("Status : Away");
			break;
		case 3:
			tvOnlineStatus.setText("Status : Do Not Disturb");
			break;
		case 4:
			tvOnlineStatus.setText("Status : Invisible");
			break;
		case 5:
			tvOnlineStatus.setText("Status : Offline");
			break;
		case 6:
			tvOnlineStatus.setText("Status : Phoneoff");
			break;
		case 7:
			tvOnlineStatus.setText("Status : Busy");
			break;
		case 8:
			tvOnlineStatus.setText("Status : Available");
			break;
		case 9:
			tvOnlineStatus.setText("Status : At Office");
			break;
		case 10:
			tvOnlineStatus.setText("Status : In a Meeting");
			break;
		case 11:
			tvOnlineStatus.setText("Status : Sleeping");
			break;
		case 12:
			tvOnlineStatus.setText("Status : Emergency call only");
			break;
		case 13:
			tvOnlineStatus.setText("Status : Can't talk, Care only");
			break;
		case 14:
			tvOnlineStatus.setText("Status : Low battery");
			break;
		default:
			tvOnlineStatus.setText("");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvMessageToContact||v.getId() == R.id.tvUserName) {
			
			CollaborationDiscussionActivity.selectedUserGroupUnion = selectUserGroupUnion;
			Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);

		} else if (v.getId() == R.id.tvClearConversion) {

		}
	}
	
	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.GetMyFavouriteUsersEmail(String.valueOf(selectUserGroupUnion.ID));
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			UserGroupUnionEntityHolder userGroupUnionEntityHolder =(UserGroupUnionEntityHolder)data;
			
			if(userGroupUnionEntityHolder.userGroupUnion!=null)
			{
				if(userGroupUnionEntityHolder.userGroupUnion.isFavourite)
				{
					ivFavoriteSelected.setVisibility(View.VISIBLE);
					ivFavoriteNormal.setVisibility(View.GONE);
				}
				else
				{
					ivFavoriteSelected.setVisibility(View.GONE);
					ivFavoriteNormal.setVisibility(View.VISIBLE);
				}
				
				tvEmail.setText("Email : "+(userGroupUnionEntityHolder.userGroupUnion.Email!=null ? userGroupUnionEntityHolder.userGroupUnion.Email : "N/A"));
				
				if(tvOnlineStatus.getText().equals(""))
				{
					setUserStatus(userGroupUnionEntityHolder.userGroupUnion.userOnlinestatus);
				}
			}
		}
	}

	@Override
	public void showProgressLoader() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideProgressLoader() {
		// TODO Auto-generated method stub
		
	}
}
