package com.vipdashboard.app.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.activities.FaceBookLoginActivity;
import com.vipdashboard.app.classes.UserFriends;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.MyProfileOnlineStatusAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.asynchronoustask.LoadImageAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.UserAboutMe;
import com.vipdashboard.app.classes.UserStatus;
import com.vipdashboard.app.entities.FacebokPerson;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class MyProfileActivity extends MainActionbarBase implements
		IAsynchronousTask, OnItemSelectedListener, OnClickListener,
		LocationListener,OnContactSelectedListener {
	Spinner spinnerUserStatus;
	ArrayList<UserStatus> userStatus;
	MyProfileOnlineStatusAdapter adapter;
	public boolean isFirstCall = true;
	int userOnlinestatusID;
	DownloadableAsyncTask downloadableAsyncTask;
	public static RelativeLayout rlAbout, rlAllFriends, rlCoins, rlAssets;
	Context context;
	View vsAboutMe, vsFriends, vsCoins, vsSharInfo;
	boolean isCallFromMyProfile, isCallFromMyOrganization,
			isCallFromUserOnlineStatusSpinner, isCallFromUserStatus,
			isCallFromGooglePlus;
	public boolean isProfilePictureSelected = false,isProfilePictureChose=false;
	UserAboutMe userAboutMe;
	UserFriends userFriends;
	public static TextView tvLocation, tvUserName, tvChangePicture;
	TextView tvBirthDayInfo, tvHomtownInfo, tvRelationshipStatusInfo,
			tvLookingForInfo, tvFacebookLinksInfo, tvEducationInfo, tvWorkInfo,tvInviteFriends,tvChangePasswrod;
	TextView tvMyNetFriends, tvFBFriends, tvContacts, tvfbFriendsListNote;
	ListView lvMyNetFriends, lvFBFriends;
	ListView lvUserCareCoin;
	TextView tvBalance, tvUploadResorces,tvAboutMeBasicInfo, tvAboutMeFamily,tvFamilyFooter;
	public static ImageView ivProfilePicture;
	ListView lvUserAssetShareInfo,lvFamilyMemberList;
	RelativeLayout rlEducationTitle, rlWorkTitle;
	ProgressDialog progressDialog;
	ImageView ivUserStatus;
	public static byte[] selectedFile;
	public static String filename = "";
	public static boolean myprofileFacebookLogin;
	LoadImageAsyncTask loadImageAsyncTask;
	LinearLayout llBottomAddFriend,llFamilyMember;
	RelativeLayout rlAboutMeBasicInfo, rlAboutMeFamily;
	TextView tvBioHeader,tvLinks,tvEducation,tvWorkHeader;
	ScrollView svBasicInfo;
	ImageView facebook;
	int fb_no = 0;
	
	public final static int SEARCH_FILTER_CARE_FRIENDS = 1;
	public final static int SEARCH_FILTER_FB_FRIENDS = 2;
	public final static int SEARCH_FILTER_CONTACTS = 3;
	private final static int SEARCH_FILTER_CARE_COIN = 4;
	private final static int SEARCH_FILTER_ASSETS = 5;
	
	public static RelativeLayout rlHelpout;
	RelativeLayout rlRanks;
	RelativeLayout rlSkill;
	RelativeLayout rlCalls;
	ListView lvSkillMap;
	TextView tvSkillName;
	
	View vsMyProfileHelpout;
	
	ImageView ivFriendNormal,ivFriendSelected,ivAboutMeNormal,ivAboutMeSelected;
	
	private AQuery aq;
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	FrameLayout frameLayout;
	public static int searchFilterFlag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileview);
		context = this.getApplicationContext();
		Initialization();
		
		getLocation();
		UserAboutMeInformation();	
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		myprofileFacebookLogin = true;
		LoginActivity.Login = false;
		isFirstCall = true;
		if(!isProfilePictureChose){			
			//loadProfileImage();
			/*ImageLoader loader = new ImageLoader(this);			
			loader.DisplayImage(profImg, ivProfilePicture);*/
		}else{
			isProfilePictureChose=false;
		}
		showImage();
		ChangeUserInfo();
		

		if(FaceBookLoginActivity.IsCalledFromProfile)
		{
			UserAboutMeInformation();
			FaceBookLoginActivity.IsCalledFromProfile=false;
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}

	private void showImage() {
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			aq.id(ivProfilePicture).image(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, imgOptions);
		}
		else if(CommonValues.getInstance().FB_Profile_Picture_Path != ""){
			aq.id(ivProfilePicture).image(CommonValues.getInstance().FB_Profile_Picture_Path, imgOptions);
		}
		else
		{
			int photoId=CommonTask.getContentPhotoId(this.context, CommonValues.getInstance().LoginUser.Mobile);
			if(photoId>0){
				ivProfilePicture.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
			}
		}
		
	MyNetDatabase db = new MyNetDatabase(this);
		
		FacebokPerson facebookPersonFromDb = db.getFacebokPerson();
		
		
			if (facebookPersonFromDb.FBNo > 0)
			{
				tvUserName.setText(facebookPersonFromDb.Name);
				aq.id(ivProfilePicture).image(facebookPersonFromDb.PP_Path, imgOptions);
				//CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path=facebookPersonFromDb.PP_Path;
				//CommonValues.getInstance().LoginUser.Facebook_Person.Name=facebookPersonFromDb.Name;
			}
	}	
	
	private void Initialization() {
		// my profile
		tvChangePasswrod = (TextView) findViewById(R.id.tvChangePasswrod);
		ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvChangePicture = (TextView) findViewById(R.id.tvChangePicture);

		vsAboutMe = ((ViewStub) findViewById(R.id.vsAboutMe)).inflate();
		vsFriends = ((ViewStub) findViewById(R.id.vsFriends)).inflate();

		spinnerUserStatus = (Spinner) findViewById(R.id.spUserStatus);

		rlAbout = (RelativeLayout) findViewById(R.id.rlAbout);
		rlAllFriends = (RelativeLayout) findViewById(R.id.rlAllFriends);

		// user_about_us init
		svBasicInfo = (ScrollView)vsAboutMe.findViewById(R.id.svBasicInfo);
		llFamilyMember = (LinearLayout)vsAboutMe.findViewById(R.id.llFamilyMember);
		lvFamilyMemberList = (ListView)vsAboutMe.findViewById(R.id.lvFamilyMemberList);
		tvFamilyFooter = (TextView)vsAboutMe.findViewById(R.id.tvFamilyFooter);
		
		tvBirthDayInfo = (TextView) vsAboutMe.findViewById(R.id.tvBirthDayInfo);
		tvHomtownInfo = (TextView) vsAboutMe.findViewById(R.id.tvHomtownInfo);

		tvRelationshipStatusInfo = (TextView) vsAboutMe
				.findViewById(R.id.tvRelationshipStatusInfo);
		tvLookingForInfo = (TextView) vsAboutMe
				.findViewById(R.id.tvLookingForInfo);
		tvFacebookLinksInfo = (TextView) vsAboutMe
				.findViewById(R.id.tvFacebookLinksInfo);
		rlEducationTitle = (RelativeLayout) vsAboutMe
				.findViewById(R.id.rlEducationTitle);
		rlWorkTitle = (RelativeLayout) vsAboutMe.findViewById(R.id.rlWorkTitle);
		
		tvBioHeader=(TextView) vsAboutMe.findViewById(R.id.tvBioHeader);
		tvLinks=(TextView) vsAboutMe.findViewById(R.id.tvLinks);
		tvEducation=(TextView) vsAboutMe.findViewById(R.id.tvEducation);
		tvWorkHeader=(TextView) vsAboutMe.findViewById(R.id.tvWorkHeader);
		
		// user_friends_inti
		tvInviteFriends = (TextView) vsFriends.findViewById(R.id.tvInviteFriends);
		tvFBFriends = (TextView) vsFriends.findViewById(R.id.tvFBFriends);
		tvContacts = (TextView) vsFriends.findViewById(R.id.tvContacts);
		llBottomAddFriend = (LinearLayout) vsFriends
				.findViewById(R.id.llBottomAddFriend);

		tvfbFriendsListNote = (TextView) vsFriends
				.findViewById(R.id.tvfbFriendsListNote);
		
		lvFBFriends = (ListView) vsFriends.findViewById(R.id.lvFBFriends);
		frameLayout= (FrameLayout)vsFriends.findViewById(R.id.fragment_container);
		ivUserStatus = (ImageView) findViewById(R.id.ivUserStatus);
		ivFriendNormal= (ImageView) findViewById(R.id.ivFriendNormal);
		ivFriendSelected= (ImageView) findViewById(R.id.ivFriendSelected);

		ivAboutMeNormal= (ImageView) findViewById(R.id.ivAboutMeNormal);
		ivAboutMeSelected= (ImageView) findViewById(R.id.ivAboutMeSelected);
		
		// event call
		rlAbout.setOnClickListener(this);
		rlAllFriends.setOnClickListener(this);
		ivUserStatus.setOnClickListener(this);
		spinnerUserStatus.setOnItemSelectedListener(this);
		llBottomAddFriend.setOnClickListener(this);
		tvChangePasswrod.setOnClickListener(this);
		
		tvChangePicture.setOnClickListener(this);
		
		facebook = (ImageView) findViewById(R.id.facebook_button);
		facebook.setOnClickListener(this);
		
		aq=new AQuery(this);
		imgOptions = CommonValues.getInstance().defaultImageOptions; 		
		imgOptions.targetWidth=100;
		imgOptions.ratio=0;//AQuery.RATIO_PRESERVE;
		imgOptions.round = 8;
		
		UserOnlineAvailablestatus();
	}

	private void getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		String addressText = "";
		if (loc != null) {

			Geocoder geocoder = new Geocoder(this, Locale.getDefault());

			try {
				List<android.location.Address> addresses = geocoder
						.getFromLocation(loc.getLatitude(), loc.getLongitude(),
								1);
				if (addresses != null && addresses.size() > 0) {
					android.location.Address address = addresses.get(0);
					for (int lineIndex = 0; lineIndex < address
							.getMaxAddressLineIndex(); lineIndex++) {
						addressText = addressText
								+ address.getAddressLine(lineIndex) + ", ";
					}
					addressText = addressText + address.getLocality() + ", "
							+ address.getCountryName();
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		tvLocation.setText("" + addressText);
	}

	private void UserOnlineAvailablestatus() {

		userStatus = new ArrayList<UserStatus>();
		userStatus.add(new UserStatus(R.drawable.user_status_online, "Online",
				CommonConstraints.ONLINE));
		userStatus.add(new UserStatus(R.drawable.user_status_away, "Away",
				CommonConstraints.AWAY));
		userStatus.add(new UserStatus(R.drawable.user_status_busy,
				"Do Not Disturb", CommonConstraints.DO_NOT_DISTURB));
		userStatus.add(new UserStatus(R.drawable.user_status_offline,
				"Invisible", CommonConstraints.INVISIBLE));
		userStatus.add(new UserStatus(R.drawable.user_status_offline,
				"Off Line", CommonConstraints.OFFLINE));
		userStatus.add(new UserStatus(R.drawable.user_status_offline,
				"Phone Off", CommonConstraints.PHONEOFF));
		userStatus.add(new UserStatus(R.drawable.user_status_busy, "Busy",
				CommonConstraints.BUSY));
		adapter = new MyProfileOnlineStatusAdapter(this,
				R.layout.my_profile_user_online_status, userStatus);
		spinnerUserStatus.setAdapter(adapter);
		isFirstCall = true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		userOnlinestatusID = (Integer) view.getTag();
		isCallFromUserOnlineStatusSpinner = true;
		isCallFromMyProfile = false;
		if (!isFirstCall) {
			// LoadInformation();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlAbout) {
			ivAboutMeNormal.setVisibility(View.GONE);
			ivFriendSelected.setVisibility(View.GONE);
			ivAboutMeSelected.setVisibility(View.VISIBLE);
			ivFriendNormal.setVisibility(View.VISIBLE);
			
			UserAboutMeInformation();
		} else if (v.getId() == R.id.rlAllFriends) {
			ivAboutMeNormal.setVisibility(View.VISIBLE);
			
			ivFriendSelected.setVisibility(View.VISIBLE);
			ivAboutMeSelected.setVisibility(View.GONE);
			ivFriendNormal.setVisibility(View.GONE);

			UserFriendsInformation();
		} else if (v.getId() == R.id.ivUserStatus) {
			final Dialog nagDialog = new Dialog(this,
					android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			nagDialog.setCancelable(false);
			nagDialog.setContentView(R.layout.user_status_layout);
			ListView lvUserStatus = (ListView) nagDialog
					.findViewById(R.id.lvUserStatus);

			userStatus = new ArrayList<UserStatus>();
			userStatus.add(new UserStatus(R.drawable.user_status_online,
					"Online", CommonConstraints.ONLINE));
			userStatus.add(new UserStatus(R.drawable.user_status_away, "Away",
					CommonConstraints.AWAY));
			userStatus.add(new UserStatus(R.drawable.user_status_busy,
					"Do Not Disturb", CommonConstraints.DO_NOT_DISTURB));
			userStatus.add(new UserStatus(R.drawable.user_status_offline,
					"Invisible", CommonConstraints.INVISIBLE));
			userStatus.add(new UserStatus(R.drawable.user_status_offline,
					"Off Line", CommonConstraints.OFFLINE));
			userStatus.add(new UserStatus(R.drawable.user_status_offline,
					"Phone Off", CommonConstraints.PHONEOFF));
			userStatus.add(new UserStatus(R.drawable.user_status_busy, "Busy",
					CommonConstraints.BUSY));
			MyProfileOnlineStatusAdapter myProfileOnlineStatusAdapter = new MyProfileOnlineStatusAdapter(
					this, R.layout.my_profile_user_online_status, userStatus);
			lvUserStatus.setAdapter(myProfileOnlineStatusAdapter);
			lvUserStatus.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					userOnlinestatusID = (Integer) view.getTag();
					isCallFromUserOnlineStatusSpinner = true;
					isFirstCall = false;
					if (!isFirstCall) {
						nagDialog.dismiss();
						if (userOnlinestatusID > 0) {
							setOnlineStatusImage(userOnlinestatusID);
						}
						LoadInformation();
					}
				}
			});

			nagDialog.setOnKeyListener(new Dialog.OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int keyCode,
						KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {

						nagDialog.dismiss();
					}
					return true;
				}
			});
			nagDialog.show();
		} else if (v.getId() == R.id.tvChangePicture) {
			UploadResorce();
		} else if (v.getId() == R.id.llBottomAddFriend) {
			UserFriendsInformation();
		}
		
		else if(v.getId() == R.id.facebook_button){
			FaceBookLoginActivity.IsCalledFromProfile=true;
	    	Intent intent = new Intent(this, FaceBookLoginActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
	    }
		
	}

	private void UploadResorce() {
		isProfilePictureChose=true;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(
				Intent.createChooser(intent, "Select Profile Picture"), 2);
	}

	public void UserFriendsInformation() {
		vsAboutMe.setVisibility(View.GONE);
		vsFriends.setVisibility(View.VISIBLE);
		
		if (userFriends == null)
			userFriends = new UserFriends(this, tvMyNetFriends, tvFBFriends,
					tvContacts, tvfbFriendsListNote, lvMyNetFriends,
					lvFBFriends, frameLayout,tvInviteFriends,llBottomAddFriend);
		userFriends.showFriends();
	}

	private void UserAboutMeInformation() {
		vsAboutMe.setVisibility(View.VISIBLE);
		vsFriends.setVisibility(View.GONE);
		if(userAboutMe == null)
			userAboutMe = new UserAboutMe(this, tvBirthDayInfo, tvHomtownInfo,
				tvRelationshipStatusInfo, tvLookingForInfo,
				tvFacebookLinksInfo, tvEducationInfo, tvWorkInfo,
				rlEducationTitle, rlWorkTitle,tvBioHeader,
				tvLinks,
				tvEducation,
				tvWorkHeader, rlAboutMeBasicInfo,rlAboutMeFamily,
				tvAboutMeBasicInfo, tvAboutMeFamily,
				svBasicInfo,llFamilyMember,lvFamilyMemberList,tvFamilyFooter);
		userAboutMe.showAboutMe();
	}

	private void LoadInformation() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		if (isCallFromUserOnlineStatusSpinner) {
			Object userOnlineAvailableStatus= userManager.SetUserOnlineAvailbleStatus(String
					.valueOf(userOnlinestatusID));
			
			CommonValues.getInstance().LoginUser=userManager.GetUserByID(CommonValues.getInstance().LoginUser.UserNumber);
			
			return userOnlineAvailableStatus;
		} else if (isProfilePictureSelected) {
			return userManager.SetProfilePictureChange(selectedFile,
					CommonValues.getInstance().LoginUser.UserNumber);
		}
		
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (isCallFromUserOnlineStatusSpinner) {
				User user = (User) data;
				isCallFromUserOnlineStatusSpinner = false;
				isFirstCall = true;
				setOnlineStatusImage(user.UserOnlineAvailableStatusID);
				ChangeUserInfo();
			} else if (isProfilePictureSelected) {
				// Load profile picture here
				isProfilePictureSelected = false;
				String profImg = CommonURL.getInstance().getImageServer
						+ "DafaultImage\\defaultuser.png";
				/*if (data != null) {
					profImg = String.valueOf(data);
					if (!profImg.contains("graph.facebook.com")) {
						profImg = CommonURL.getInstance().getImageServer
								+ profImg;
					}
					profImg = profImg.replace("large", "small");

				}*/
				//ImageLoader loader = new ImageLoader(this);
				//loader.clearCache();
				//loader.DisplayImage(profImg, ivProfilePicture);

			} else {
				User user = (User) data;
				isCallFromUserStatus = false;
				isCallFromMyProfile = true;
				tvUserName.setText(user.FullName.toString().toUpperCase());
				
				tvUserName.setText(CommonTask.getContentName(context,
						user.FullName).toUpperCase());
				
				if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
					if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
						tvUserName.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name.toString().toUpperCase());
				}else{
					MyNetDatabase database = new MyNetDatabase(this);
					database.open();
					FacebokPerson facebokPerson = database.getFacebokPerson();
					database.close();
					if(facebokPerson != null && facebokPerson.Name != null){
						tvUserName.setText(facebokPerson.Name.toString().toUpperCase());
					}
				}
				
				if (user.UserOnlineAvailableStatusID > 0) {
					setOnlineStatusImage(user.UserOnlineAvailableStatusID);
				}
			}
		}
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
				if (!isFinishing()) 
				{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
		}
	}
	
	private void ChangeUserInfo()
	{
		String UserContactName= CommonTask.getContentName(context,
				CommonValues.getInstance().LoginUser.Mobile).toUpperCase();
		if(!UserContactName.equals(""))
		{
			tvUserName.setText(UserContactName);
		}
		else
		{

			tvUserName.setText(CommonValues.getInstance().LoginUser.Mobile.toString().toUpperCase());
		}
		
		
		
		
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
				tvUserName.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name.toString().toUpperCase());
		}else{
			MyNetDatabase database = new MyNetDatabase(this);
			database.open();
			FacebokPerson facebokPerson = database.getFacebokPerson();
			database.close();
			if(facebokPerson != null && facebokPerson.Name != null){
				tvUserName.setText(facebokPerson.Name.toString().toUpperCase());
			}
		}	
		
		if (CommonValues.getInstance().LoginUser.UserOnlineAvailableStatusID > 0) {
			setOnlineStatusImage(CommonValues.getInstance().LoginUser.UserOnlineAvailableStatusID);
		}
	}

	private void setOnlineStatusImage(long userOnlineAvailableStatusID) {
		switch ((int) userOnlineAvailableStatusID) {
		case 1:
			ivUserStatus.setImageDrawable(getResources().getDrawable(
					R.drawable.user_status_online));
			break;
		case 2:
			ivUserStatus.setImageDrawable(getResources().getDrawable(
					R.drawable.user_status_away));
			break;
		case 3:
			ivUserStatus.setImageDrawable(getResources().getDrawable(
					R.drawable.user_status_busy));
			break;
		case 4:
			ivUserStatus.setImageDrawable(getResources().getDrawable(
					R.drawable.user_status_offline));
			break;
		case 5:
			ivUserStatus.setImageDrawable(getResources().getDrawable(
					R.drawable.user_status_offline));
			break;
		case 6:
			ivUserStatus.setImageDrawable(getResources().getDrawable(
					R.drawable.user_status_offline));
			break;
		case 7:
			ivUserStatus.setImageDrawable(getResources().getDrawable(
					R.drawable.user_status_busy));
			break;

		default:
			break;
		}
	}

	public static byte[] convertInputStreamToByteArray(InputStream input)
			throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	public String getRealPathFromURI(Uri contentUri) {

		Cursor cursor = getContentResolver()
				.query(contentUri,
						new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
						null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void showProgressLoader() {
		/*progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		//progressDialog.dismiss();
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

	@Override
	public void onContactNameSelected(long contactId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContactNumberSelected(String contactNumber, String contactName) {
		// TODO Auto-generated method stub
		
	}
}
