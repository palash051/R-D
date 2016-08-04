package com.vipdashboard.app.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.MyOrganizaitonMemberAdapter;
import com.vipdashboard.app.adapter.MyProfileOnlineStatusAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.MyNetFacebookActivity;
import com.vipdashboard.app.classes.MyNetGooglePlusActivity;
import com.vipdashboard.app.classes.MyNetLinkedInActivity;
import com.vipdashboard.app.classes.UserStatus;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.FacebookManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class MyProfileOldActivity extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask, OnItemSelectedListener,
		ConnectionCallbacks, OnConnectionFailedListener, PlusClient.OnPeopleLoadedListener
		{

	TextView titleText;
	ImageView linkedIN,google_plus,facebook;
	//LoginButton facebook;
	
	ImageView profile_picture_view;
	Intent intent;
	
	ListView lvProfileMemberOf;
	
	Spinner spinnerUserStatus;

	RelativeLayout rlProfileBasicInformation, rlProfileMyOrganization,
			rlProfileMYNETexperts;

	ScrollView svProfileBasicinformation, svProfileMyOrganization;

	TextView tvProfileBasicInformation, tvProfileMyOrganization,uploadpicture,
			tvProfileMYNETexperts,tvNameText,tvPhoneNumberText,tvGenderText,tvDateofBirthText,tvWorkingForText,tvPositionText,tvManagersemailText,tvCompanyTypeText,tvUnitText,tvJobTypeText,tvJobLocationText;
	TextView tvMyOrganizatrionCompantType, tvMyOrganizationUnit, tvMyOrganizationJobType, tvMyOrganizationJobLocation;
	TextView tvMyProfileEditButton, tvMyOrganizationEditButton;
	TextView tvAddNewMember;
	boolean isCallFromMyProfile, isCallFromMyOrganization, isCallFromUserOnlineStatusSpinner, isCallFromUserStatus, isCallFromGooglePlus;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	MyProfileOnlineStatusAdapter adapter;
	ArrayList<UserStatus> userStatus;
	int userOnlinestatusID;	
	MyOrganizaitonMemberAdapter myOrganizaitonMemberAdapter;
	String Urivalue;
	
	private String userID;
	private String name;
	private String pp_path;
	private String gender;
	private String dob;
	private String rel_status;
	private String pref_skill;
	private String religion;
	private String otherName;
	StringBuilder stringBuilder;
	long milliseconds;
	String message;
	
	private static final String TAG = "MyNetGooglePlusActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    public static boolean myprofileFacebookLogin;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//FacebookFragment(savedInstanceState);
		setContentView(R.layout.myprofile);
		Initialization();		
		mPlusClient = new PlusClient.Builder(this, this, this)
        .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
        .setScopes(Scopes.PLUS_LOGIN)
        .build();
	}
	
	/*private void FacebookFragment(Bundle savedInstanceState){
		if (savedInstanceState == null) {
	    	facebookFragment = new MyNetFacebookFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(R.id.FragmentContainer, facebookFragment)
	        .commit();
	    } else {
	    	facebookFragment = (MyNetFacebookFragment) getSupportFragmentManager()
	        .findFragmentById(R.id.FragmentContainer);
	    }
	}*/

	private void UserOnlineAvailablestatus(long userOnlineAvailableStatusID) {
		int id = (int) userOnlineAvailableStatusID;
		switch (id) {
		case 1:
			userStatus = new ArrayList<UserStatus>();
			userStatus.add(new UserStatus(R.drawable.user_status_online, "Online", 1));
			userStatus.add(new UserStatus(R.drawable.user_status_away, "Away", 2));
			userStatus.add(new UserStatus(R.drawable.user_status_busy, "Do Not Disturb", 3));
			userStatus.add(new UserStatus(R.drawable.user_status_offline, "Invisible", 4));
			adapter = new MyProfileOnlineStatusAdapter(this, R.layout.my_profile_user_online_status, userStatus);
			spinnerUserStatus.setAdapter(adapter);
			break;
		case 2:
			userStatus = new ArrayList<UserStatus>();
			userStatus.add(new UserStatus(R.drawable.user_status_away, "Away", 2));
			userStatus.add(new UserStatus(R.drawable.user_status_online, "Online", 1));			
			userStatus.add(new UserStatus(R.drawable.user_status_busy, "Do Not Disturb", 3));
			userStatus.add(new UserStatus(R.drawable.user_status_offline, "Invisible", 4));
			adapter = new MyProfileOnlineStatusAdapter(this, R.layout.my_profile_user_online_status, userStatus);
			spinnerUserStatus.setAdapter(adapter);
			break;
		case 3:
			userStatus = new ArrayList<UserStatus>();
			userStatus.add(new UserStatus(R.drawable.user_status_busy, "Do Not Disturb", 3));
			userStatus.add(new UserStatus(R.drawable.user_status_away, "Away", 2));
			userStatus.add(new UserStatus(R.drawable.user_status_online, "Online", 1));				
			userStatus.add(new UserStatus(R.drawable.user_status_offline, "Invisible", 4));
			adapter = new MyProfileOnlineStatusAdapter(this, R.layout.my_profile_user_online_status, userStatus);
			spinnerUserStatus.setAdapter(adapter);
			break;
		case 4:
		case 5:
			userStatus = new ArrayList<UserStatus>();
			userStatus.add(new UserStatus(R.drawable.user_status_offline, "Invisible", 4));
			userStatus.add(new UserStatus(R.drawable.user_status_busy, "Do Not Disturb", 3));
			userStatus.add(new UserStatus(R.drawable.user_status_away, "Away", 2));
			userStatus.add(new UserStatus(R.drawable.user_status_online, "Online", 1));				
			adapter = new MyProfileOnlineStatusAdapter(this, R.layout.my_profile_user_online_status, userStatus);
			spinnerUserStatus.setAdapter(adapter);
			break;

		default:
			break;
		} 
	}

	private void Initialization() {
		titleText = (TextView) findViewById(R.id.tvUserEmailAddrsss);
		facebook = (ImageView) findViewById(R.id.facebook_button);
		google_plus = (ImageView) findViewById(R.id.google_plus);
		//findViewById(R.id.sign_in_button).setOnClickListener(this);
		linkedIN = (ImageView) findViewById(R.id.linkedIn);
		spinnerUserStatus = (Spinner) findViewById(R.id.spUserStatus);
		
		progressBar = (ProgressBar) findViewById(R.id.pbMyPofile);

		rlProfileBasicInformation = (RelativeLayout) findViewById(R.id.rlProfileBasicInformation);
		rlProfileMyOrganization = (RelativeLayout) findViewById(R.id.rlProfileMyOrganization);
		rlProfileMYNETexperts = (RelativeLayout) findViewById(R.id.rlProfileMYNETexperts);

		svProfileBasicinformation = (ScrollView) findViewById(R.id.svProfileBasicinformation);
		svProfileMyOrganization = (ScrollView) findViewById(R.id.svProfileMyOrganization);

		tvProfileBasicInformation = (TextView) findViewById(R.id.tvProfileBasicInformation);
		tvProfileMyOrganization = (TextView) findViewById(R.id.tvProfileMyOrganization);
		tvProfileMYNETexperts = (TextView) findViewById(R.id.tvProfileMYNETexperts);

		tvNameText = (TextView) findViewById(R.id.tvNameText);
		profile_picture_view = (ImageView) findViewById(R.id.ivProfilePicture);
		uploadpicture = (TextView) findViewById(R.id.uploadpicture);
		CommonTask.makeLinkedTextview(this, uploadpicture, uploadpicture.getText().toString());
		tvPhoneNumberText = (TextView) findViewById(R.id.tvPhoneNumberText);
		tvGenderText = (TextView) findViewById(R.id.tvGenderText);
		tvDateofBirthText = (TextView) findViewById(R.id.tvDateofBirthText);
		tvWorkingForText = (TextView) findViewById(R.id.tvWorkingForText);
		tvPositionText = (TextView) findViewById(R.id.tvPositionText);
		tvManagersemailText = (TextView) findViewById(R.id.tvManagersemailText);
		tvMyProfileEditButton = (TextView) findViewById(R.id.tvBasicInformaitonEditButton);
		
		tvMyOrganizatrionCompantType = (TextView) findViewById(R.id.tvCompanyTypeText);
		tvMyOrganizationUnit = (TextView) findViewById(R.id.tvUnitText);
		tvMyOrganizationJobType = (TextView) findViewById(R.id.tvJobTypeText);
		tvMyOrganizationJobLocation = (TextView) findViewById(R.id.tvJobLocationText);
		tvAddNewMember = (TextView) findViewById(R.id.tvAddMember);
		lvProfileMemberOf = (ListView) findViewById(R.id.lvProfileMemberOf);

		rlProfileBasicInformation.setOnClickListener(this);
		rlProfileMyOrganization.setOnClickListener(this);
		rlProfileMYNETexperts.setOnClickListener(this);
		CommonTask.makeLinkedTextview(this, tvMyProfileEditButton, tvMyProfileEditButton.getText().toString());
		CommonTask.makeLinkedTextview(this, tvAddNewMember, tvAddNewMember.getText().toString());        
		
		
		tvMyProfileEditButton.setOnClickListener(this);
		spinnerUserStatus.setOnItemSelectedListener(this);
		tvAddNewMember.setOnClickListener(this);
		google_plus.setOnClickListener(this);
		linkedIN.setOnClickListener(this);
		facebook.setOnClickListener(this);
		uploadpicture.setOnClickListener(this);		
		//  lfacebook.setOnClickListener(this);
		
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
		arrangeBasicInformation();
		titleText.setText(CommonValues.getInstance().LoginUser.UserID);
		isCallFromMyProfile = false;
		isCallFromUserStatus = true;
		isCallFromMyOrganization = false;
		isCallFromUserOnlineStatusSpinner = false;
		isCallFromGooglePlus = false;
		myprofileFacebookLogin = true;
		LoginActivity.Login = false;
		tvNameText.setText("");
		tvPhoneNumberText.setText("");
		tvGenderText.setText("");
		tvDateofBirthText.setText("");
		tvWorkingForText.setText("");
		tvPositionText.setText("");
		tvManagersemailText.setText("");
		LoadInformation();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_uploadphoto, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.takePhoto:
			ToakePhotoFromMobile();
			break;
		case R.id.takePhotofromGalary:
			getGalaryPhoto();
			break;
		default:
			break;
		}
		return true;
	}

	private void ToakePhotoFromMobile() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(CheckExternalStorages()){
			String external_path = Environment.getExternalStorageDirectory().getPath() + "/MyNet/";
			String filePath = String.format("%d.jpg",System.currentTimeMillis());
			File cduFileDir = new File(external_path);
			if (!cduFileDir.exists())
				cduFileDir.mkdir();
			File pictureFile = new File(cduFileDir, filePath);
			Uri otuputFile = Uri.fromFile(pictureFile);
			Urivalue = otuputFile.getPath().toString();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, otuputFile);
			startActivityForResult(intent, 100);
		}	
		else{
			Toast.makeText(MyProfileOldActivity.this, "SDCard Unmounted!", Toast.LENGTH_LONG).show();
		}
	}

	private boolean CheckExternalStorages() {
		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlProfileBasicInformation) {
			arrangeBasicInformation();
			isCallFromMyProfile = true;
			LoadInformation();
		}
		else if(v.getId() == R.id.uploadpicture){
			registerForContextMenu(uploadpicture);
			openContextMenu(uploadpicture);
		}
		else if (v.getId() == R.id.rlProfileMyOrganization) {
			isCallFromMyProfile = false;
			isCallFromUserStatus = false;
			isCallFromMyOrganization = true;
			isCallFromUserOnlineStatusSpinner = false;
			LoadInformation();
			tvProfileBasicInformation.setBackgroundColor(getResources()
					.getColor(R.color.value_text));
			tvProfileMyOrganization.setBackgroundColor(getResources().getColor(
					R.color.header_text));
			tvProfileMYNETexperts.setBackgroundColor(getResources().getColor(
					R.color.value_text));

			svProfileBasicinformation.setVisibility(ScrollView.GONE);
			svProfileMyOrganization.setVisibility(ScrollView.VISIBLE);
			isCallFromMyOrganization = true;
			LoadInformation();
		} else if (v.getId() == R.id.rlProfileMYNETexperts) {
			tvProfileBasicInformation.setBackgroundColor(getResources()
					.getColor(R.color.value_text));
			tvProfileMyOrganization.setBackgroundColor(getResources().getColor(
					R.color.value_text));
			tvProfileMYNETexperts.setBackgroundColor(getResources().getColor(
					R.color.header_text));

			svProfileBasicinformation.setVisibility(ScrollView.GONE);
			svProfileMyOrganization.setVisibility(ScrollView.GONE);
			Intent intent = new Intent(this, DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if(v.getId() == R.id.tvBasicInformaitonEditButton){
			Intent intent = new Intent(this, MyProfileEditActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("USER_NAME", tvNameText.getText().toString());
			intent.putExtra("USER_PHONE", tvPhoneNumberText.getText().toString());
			intent.putExtra("USER_GENDER", tvGenderText.getText().toString());
			intent.putExtra("USER_DATE_OF_BIRTH", tvDateofBirthText.getText().toString());
			intent.putExtra("USER_WORKING_USER", tvWorkingForText.getText().toString());
			intent.putExtra("USER_POSITION", tvPositionText.getText().toString());
			intent.putExtra("USER_MANAGER_EMAIL", tvManagersemailText.getText().toString());
			startActivity(intent);
		}else if(v.getId() == R.id.tvAddMember){
			Intent intent = new Intent(this, AddMoreMemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(v.getId() == R.id.linkedIn){
			Intent intent = new Intent(this, MyNetLinkedInActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (v.getId() == R.id.google_plus) {
			//mPlusClient.connect();
			//progressBar.setVisibility(View.VISIBLE);
	    }else if(v.getId() == R.id.facebook_button){
	    	Intent intent = new Intent(this, FaceBookLoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
	    }
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (progressBar.isShown()) {
	        if (result.hasResolution()) {
	          try {
	        	  result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
	           } catch (SendIntentException e) {
	              mPlusClient.connect();
	           }
	        }
	      }
	      mConnectionResult = result;
	}
	@Override
	public void onConnected(Bundle arg0) {
		progressBar.setVisibility(View.GONE);
		Intent intent = new Intent(this, MyNetGooglePlusActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	@Override
	public void onDisconnected() {
		
	}

	private void getGalaryPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
	    intent.setType("image/*");
	    intent.putExtra("return-data", true);
	    startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		//super.onActivityResult(requestCode, responseCode, data);
		switch (requestCode) {
		case 9000:
			if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
	            mConnectionResult = null;
	            mPlusClient.connect();
	        }
			break;
        case 1:
            if(requestCode == 1 && data != null && data.getData() != null){
                Uri _uri = data.getData();
                if (_uri != null) {
                    Cursor cursor = getContentResolver().query(_uri, new String[] {android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    cursor.moveToFirst();
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String imageFilePath = cursor.getString(column_index).toString();
                    if(imageFilePath != null){
                    	File photos= new File(imageFilePath);
                        Bitmap b = CommonTask.decodeImage(photos);
                        b = Bitmap.createScaledBitmap(b,100, 100, true);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            			MyNetFacebookActivity.picByteValue = stream.toByteArray();
                        profile_picture_view.setImageBitmap(b);
                    }                    
                    cursor.close();
                }
            }
            super.onActivityResult(requestCode, responseCode, data);
            break;
        case 100:
        	if(requestCode == 100){
        		int width = profile_picture_view.getWidth();
				int height = profile_picture_view.getHeight();
				
				BitmapFactory.Options factory = new Options();
				factory.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(Urivalue, factory);
				int imagewidth = factory.outWidth;
				int imageheight = factory.outHeight;
				int scalfactor = Math.min(imagewidth/width, imageheight/height);
				factory.inJustDecodeBounds = false;
				factory.inSampleSize = scalfactor;
				factory.inPurgeable = true;
				Bitmap bitmap = BitmapFactory.decodeFile(Urivalue, factory);
				if(bitmap != null){
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    			MyNetFacebookActivity.picByteValue = stream.toByteArray();
					profile_picture_view.setImageBitmap(bitmap);
				}
    		}
        	super.onActivityResult(requestCode, responseCode, data);
        	break;
        }
	}

	private void arrangeBasicInformation() {
		tvProfileBasicInformation.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvProfileMyOrganization.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvProfileMYNETexperts.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		svProfileBasicinformation.setVisibility(ScrollView.VISIBLE);
		svProfileMyOrganization.setVisibility(ScrollView.GONE);
		
		tvNameText.setText(CommonValues.getInstance().LoginUser.FullName());
	}

	private void LoadInformation() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		if(isCallFromMyProfile || isCallFromUserStatus || isCallFromMyOrganization){
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideProgressLoader() {
		if(isCallFromMyProfile || isCallFromUserStatus || isCallFromMyOrganization){
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		IFacebookManager facebookManager = new FacebookManager();
		if(isCallFromMyProfile){
			return userManager.GetUserByID(CommonValues.getInstance().LoginUser.UserNumber);
		}else if(isCallFromUserOnlineStatusSpinner){
			return userManager.SetUserOnlineAvailbleStatus(String.valueOf(userOnlinestatusID));
		}else if(isCallFromUserStatus){
			return userManager.GetUserByID(CommonValues.getInstance().LoginUser.UserNumber);
		}else if(isCallFromMyOrganization){
			return userManager.MyOrganizationInformation();
		}else if(isCallFromGooglePlus){
			return facebookManager.setGPInformation(userID, name, pp_path, gender, rel_status, String.valueOf(milliseconds), religion, pref_skill, otherName);
		}else{
			return null;
		}
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){ 
			if(isCallFromMyProfile){
				try{
					User user = (User) data;
					if(!user.FirstName.toString().equals("")){
						tvNameText.setText(user.Name);
					}
					if(!user.Mobile.toString().equals("")){
						tvPhoneNumberText.setText(user.Mobile);
					}
					if(user.Gender != null){
						tvGenderText.setText(user.Gender);
					}
					if(user.DateOfBirth != null){
						tvDateofBirthText.setText(CommonTask.convertJsonDateToDateofBirth(user.DateOfBirth));
						MyProfileEditActivity.DateTime = String.valueOf(CommonTask.convertJsonDateToLong(user.DateOfBirth));
					}
					if(user.Company.Name != null){
						tvWorkingForText.setText(user.Company.Name);
					}
					if(user.Designation != null){
						tvPositionText.setText(user.Designation);
					}
					if(user.ManagerEmailAddress != null){
						tvManagersemailText.setText(user.ManagerEmailAddress);
					}
					isCallFromMyProfile = false;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}else if(isCallFromUserStatus){
				User user = (User) data;
				isCallFromUserStatus = false;
				isCallFromMyProfile = true;
				if(user.UserOnlineAvailableStatusID > 0){
					userOnlinestatusID = (int) user.UserOnlineAvailableStatusID;
					UserOnlineAvailablestatus(user.UserOnlineAvailableStatusID);
					LoadInformation();
				}
			}
			else if(isCallFromUserOnlineStatusSpinner){
				isCallFromUserOnlineStatusSpinner = false;
				isCallFromUserStatus = false;
				isCallFromMyProfile = false;
			}else if(isCallFromMyOrganization){
				ArrayList<Object> complesData = (ArrayList<Object>) data;				
				isCallFromMyOrganization = false;
				UserGroupUnions userGroupUnions = (UserGroupUnions) complesData.get(0);
				User user = (User) complesData.get(1);
				tvMyOrganizatrionCompantType.setText(user.Company.CompanyType.CompanyTypeName);
				tvMyOrganizationUnit.setText(user.Company.CompanyDivisions.size()>0?user.Company.CompanyDivisions.get(0).Division.DivisionName:"");
				tvMyOrganizationJobType.setText(user.JobType);
				tvMyOrganizationJobLocation.setText(user.Company.Address);
				if(userGroupUnions.userGroupUnionList != null){
					myOrganizaitonMemberAdapter = new MyOrganizaitonMemberAdapter(this, R.layout.memberof_item_layout, 
							new ArrayList<UserGroupUnion>(userGroupUnions.userGroupUnionList));
					lvProfileMemberOf.setAdapter(myOrganizaitonMemberAdapter);
				}
			}
			/*else if(isCallFromGooglePlus){
				isCallFromGooglePlus = false;
				Toast.makeText(this, "Google Plus Sync Successfully", Toast.LENGTH_LONG).show();
			}*/
			
		}else{
			/*if(isCallFromGooglePlus){
				isCallFromGooglePlus = false;
				Toast.makeText(this, "Google Plus Sync UnSuccessfully", Toast.LENGTH_LONG).show();
			}*/
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		userOnlinestatusID = (Integer) view.getTag();
		if(isCallFromUserOnlineStatusSpinner){
			LoadInformation();
		}
		isCallFromUserOnlineStatusSpinner = true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onPeopleLoaded(ConnectionResult arg0, PersonBuffer arg1,
			String arg2) {
		// TODO Auto-generated method stub
		
	}

	

	
}
