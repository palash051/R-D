package com.vipdashboard.app.activities;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.FB_FriendsListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.UserFriends;
import com.vipdashboard.app.entities.FB_Friend;
import com.vipdashboard.app.entities.FB_FriendsRoot;
import com.vipdashboard.app.entities.FacebokPerson;
import com.vipdashboard.app.entities.FacebookInfo;
import com.vipdashboard.app.entities.FacebookQualificationExperience;
import com.vipdashboard.app.entities.UserFriend;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.manager.FacebookManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;
import com.vipdashboard.app.utils.JSONfunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class FB_FriendsActivity extends Activity implements IAsynchronousTask {
	private static List<String> permissions;
	Session.StatusCallback statusCallback = new SessionStatusCallback();
	ProgressDialog progressBar;
	DownloadableAsyncTask asyncTask;
	FB_FriendsRoot fb_FriendsRoot;
	public static FB_FriendsListAdapter fb_FriendsListAdapter;
	JSONObject jsonObject;
	private String userID, name, pp_path, gender, rel_status, dob,religion,pref_skill,about,pages,groups,
	apps,frieldId,username,mobileNumber,alternativeEmail,zipCode,country,website,feedText,feedTime,
	qualification,education,email;
	long DurationFromMillisecond,DurationToMillisecond,DOBmilliseconds;
	FacebookInfo facebookInfos;
	Date date;
	URL img_value = null;
	private AQuery aq;
	ImageOptions imgOptions;
	ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		progressBar = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressBar.setCancelable(false);
		progressBar.setMessage("Sync...");
		/***** FB Permissions *****/
		permissions = new ArrayList<String>();
		permissions
				.add("email,user_birthday,user_groups,user_relationships,"
						+ "user_about_me,user_religion_politics,user_website,user_work_history,user_education_history");
		/***** End FB Permissions *****/

		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			session.addCallback(statusCallback);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this).setCallback(
						statusCallback).setPermissions(permissions));
			}
		}
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(FB_FriendsActivity.this)
					.setCallback(statusCallback).setPermissions(permissions));
		} else {
			Session.openActiveSession(FB_FriendsActivity.this, true,
					statusCallback);
		}

	}

	/*
	 * call back funciton for facebook
	 */
	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			processSessionStatus(session, state, exception);
		}
	}

	public void processSessionStatus(final Session session, SessionState state,
			Exception exception) {
		if (session != null && session.isOpened()) {
			if (session.getPermissions().contains("email")) {
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					@Override
					public void onCompleted(final GraphUser user,
							Response response) {
						if (user != null) {
							userID = name = pp_path = gender=rel_status=dob=religion=pref_skill=about=pages=groups=apps = qualification="";
							frieldId=username=mobileNumber=alternativeEmail=zipCode=country=website=feedText=feedTime=education="";
							email = "";
							progressBar.show();
							getInformtion(user, session);
						}
					}
				}).executeAsync();
			} else {
				session.requestNewReadPermissions(new Session.NewPermissionsRequest(
						FB_FriendsActivity.this, permissions));
			}
		}
	}

	/********** Activity Methods **********/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("FbLogin", "Result Code is - " + resultCode + "");
		Session.getActiveSession().onActivityResult(FB_FriendsActivity.this,
				requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
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
	}

	@Override
	protected void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}
	
	/*
	 * FB Personal Information
	 */
	
	private void getInformtion(GraphUser user, Session session) {
		if(user!=null){
			try{
				JSONObject jsonObject = user.getInnerJSONObject();
				Gson gson = new Gson();
				facebookInfos = gson.fromJson(JSONfunctions
						.fixEncoding(jsonObject.toString()),
						FacebookInfo.class);
				email = facebookInfos.email!=null?facebookInfos.email:"";
				userID = facebookInfos.id!=null?facebookInfos.id:"";
				username = facebookInfos.username!=null?facebookInfos.username:"";
				name = facebookInfos.name!=null?facebookInfos.name:"";
				gender = facebookInfos.gender!=null?facebookInfos.gender:"";
				rel_status = facebookInfos.relationship_status!=null?facebookInfos.relationship_status:"";
				SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
				date = f.parse(facebookInfos.birthday!=null?facebookInfos.birthday:"00/00/0000");
				DOBmilliseconds = facebookInfos.birthday!=null?date.getTime():0;
				religion = facebookInfos.religion!=null?facebookInfos.religion:"";
				about = facebookInfos.bio!=null?facebookInfos.bio:"";
				country = facebookInfos.hometown.name!=null?facebookInfos.hometown.name:"";
				for(int i=0;i<facebookInfos.educations.size();i++){
					education += facebookInfos.educations.get(i).school.name != null?facebookInfos.educations.get(i).school.name:"";
					education += facebookInfos.educations.get(i).classes.size()>0?"~"+facebookInfos.educations.get(i).classes.get(0).name:"~";
					education += "~";
					SimpleDateFormat educationYear = new SimpleDateFormat("mm/dd/yyyy");
					date = educationYear.parse(facebookInfos.educations.get(i).year != null ? "01/01/"+facebookInfos.educations.get(i).year.name:"00/00/0000");
					education += facebookInfos.educations.get(i).year != null ? "~"+date.getTime():"~";
					education += i==facebookInfos.educations.size()-1?"~Education":"~Education|";					
				}
				for (int i = 0; i < facebookInfos.works.size(); i++) {
					qualification += facebookInfos.works.get(i).employes.name != null ? facebookInfos.works.get(i).employes.name:"";
					qualification += facebookInfos.works.get(i).position.name != null ? "~" + facebookInfos.works.get(i).position.name:"~";
					SimpleDateFormat professionDate = new SimpleDateFormat("yyyy-MM-dd");
					date = professionDate.parse(facebookInfos.works.get(i).start_date != null ? facebookInfos.works.get(i).start_date : "0000-00-00");
					qualification += facebookInfos.works.get(i).start_date != null ? "~" + date.getTime() : "~";
					date = professionDate.parse(facebookInfos.works.get(i).end_date != null ? facebookInfos.works.get(i).end_date : "0000-00-00");
					qualification += facebookInfos.works.get(i).end_date != null ? "~" + date.getTime() : "~";
					qualification += i==facebookInfos.works.size()-1?"~Profession":"~Profession|";
				}
				if(education.length() > 0)
					qualification += "|" + education;
				pp_path = new URL("https://graph.facebook.com/"+userID+"/picture?type=large").toString();
				
				//add data into sqlite
				FacebokPerson facebookPerson = new FacebokPerson();
				facebookPerson.FB_UserID=userID;
				facebookPerson.FB_UserName=username;
				facebookPerson.Name=name;
				facebookPerson.PrimaryEmail=email;
				facebookPerson.PP_Path=pp_path;
				CommonValues.getInstance().FB_Profile_Picture_Path = pp_path;
				facebookPerson.Gender=gender;
				facebookPerson.Relationship_Status=rel_status;
				facebookPerson.DateOfBirth=String.valueOf(DOBmilliseconds);
				facebookPerson.Religion=religion;
				facebookPerson.Professional_Skills=null;
				facebookPerson.About=about;
				facebookPerson.Pages=country;
				facebookPerson.Groups=null;
				facebookPerson.Apps	=null;
				
				MyNetDatabase db=new MyNetDatabase(this);
				db.open();
				int fb_no = db.CreateFacebookPerson(facebookPerson);
				db.close();
				
				db.open();
				db.deleteFacebook_Qualification_Experience();
				db.close();
				
				FacebookQualificationExperience facebookQualificationExperience = new FacebookQualificationExperience();
				for(int i=0;i<facebookInfos.educations.size();i++){
					facebookQualificationExperience.QualificationExperience = facebookInfos.educations.get(i).school.name != null?facebookInfos.educations.get(i).school.name:null;
					facebookQualificationExperience.Position = facebookInfos.educations.get(i).classes.size()>0?facebookInfos.educations.get(i).classes.get(0).name:null;
					facebookQualificationExperience.Duration_From=null;
					SimpleDateFormat educationYear = new SimpleDateFormat("mm/dd/yyyy");
					date = educationYear.parse(facebookInfos.educations.get(i).year != null ? "01/01/"+facebookInfos.educations.get(i).year.name:"00/00/0000");
					facebookQualificationExperience.Duration_To = facebookInfos.educations.get(i).year != null ?""+date.getTime():"~";
					facebookQualificationExperience.QualificationExperienceType="Education";
					facebookQualificationExperience.FBNo=fb_no;
					
					db.open();
					db.CreateFacebook_Qualification_Experience(facebookQualificationExperience);
					db.close();
				}
				for (int i = 0; i < facebookInfos.works.size(); i++) {
					facebookQualificationExperience.QualificationExperience= facebookInfos.works.get(i).employes.name != null ? facebookInfos.works.get(i).employes.name:null;
					facebookQualificationExperience.Position = facebookInfos.works.get(i).position.name != null ?facebookInfos.works.get(i).position.name:null;
					SimpleDateFormat professionDate = new SimpleDateFormat("yyyy-MM-dd");
					date = professionDate.parse(facebookInfos.works.get(i).start_date != null ? facebookInfos.works.get(i).start_date : "0000-00-00");
					facebookQualificationExperience.Duration_From = facebookInfos.works.get(i).start_date != null ? "" + date.getTime() : null;
					date = professionDate.parse(facebookInfos.works.get(i).end_date != null ? facebookInfos.works.get(i).end_date : "0000-00-00");
					facebookQualificationExperience.Duration_To = facebookInfos.works.get(i).end_date != null ? "" + date.getTime() : null;
					facebookQualificationExperience.QualificationExperienceType="Profession";
					facebookQualificationExperience.FBNo=fb_no;
					
					db.open();
					db.CreateFacebook_Qualification_Experience(facebookQualificationExperience);
					db.close();
				}
				//end
				getFriedLists(user, session);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	/*
	 * Fb Friends get From Facebook Service
	 */

	private void getFriedLists(final GraphUser user, final Session session) {
		if (user != null) {
			Bundle bundle = new Bundle();
			bundle.putString("fields", "id,name,email,username");
			new Request(session, "/me/friends", bundle, HttpMethod.GET,
					new Request.Callback() {
						public void onCompleted(Response response) {
							try {
								GraphObject graphObject = response
										.getGraphObject();
								jsonObject = graphObject
										.getInnerJSONObject();
								progressBar.dismiss();
								LoadInformation();
							} catch (Exception ex) {
								Toast.makeText(FB_FriendsActivity.this,
										ex.getMessage().toString(),
										Toast.LENGTH_LONG).show();
								onBackPressed();
							}
						}
					}).executeAsync();
		}
	}

	private void LoadInformation() {
		if (asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressBar.setCancelable(false);
		progressBar.setMessage("Sync...");
		progressBar.show();
	}

	@Override
	public void hideProgressLoader() {
		progressBar.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		
		IFacebookManager facebookManager = new FacebookManager();
		return facebookManager.SetFacebookPersonInformation(userID,username,name,email,pp_path,gender,rel_status,String.valueOf(DOBmilliseconds),religion,pref_skill,about,
				country,groups,apps, mobileNumber, alternativeEmail,zipCode,country,website,feedText,"0",frieldId,qualification);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){			
			Gson gson = new Gson();
			/*fb_FriendsRoot = gson.fromJson(JSONfunctions
					.fixEncoding(jsonObject.toString()),
					FB_FriendsRoot.class);*/
			/*fb_FriendsListAdapter = new FB_FriendsListAdapter(this, R.layout.fb_friends_list_adapter, 
					new ArrayList<FB_Friend>(fb_FriendsRoot.fb_FriendList));
			UserFriends.lvFBFriends.setAdapter(fb_FriendsListAdapter);*/
			CommonConstraints.fb_friendsList = UserFriends.lvFBFriends;
			if(UserFriends.llBottomAddFriend != null)
				UserFriends.llBottomAddFriend.setVisibility(View.VISIBLE);
			MyNetDatabase database = new MyNetDatabase(this);
			database.open();
			database.updateFacebookPerson(1);
			database.close();
			ChangeName();
			//ChangePicture();
			onBackPressed();
		}
				
	}
	
	private void ChangePicture() {
		aq=new AQuery(this);
		imgOptions = CommonValues.getInstance().defaultImageOptions; 		
		imgOptions.targetWidth=100;
		imgOptions.ratio=0;//AQuery.RATIO_PRESERVE;
		imgOptions.round = 8;
		
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			aq.id(MyProfileActivity.ivProfilePicture).image(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, imgOptions);
		}
		else if(CommonValues.getInstance().FB_Profile_Picture_Path != ""){
			aq.id(MyProfileActivity.ivProfilePicture).image(CommonValues.getInstance().FB_Profile_Picture_Path.toString(), imgOptions);
		}
		else
		{
			int photoId=CommonTask.getContentPhotoId(this, CommonValues.getInstance().LoginUser.Mobile);
			if(photoId>0){
				MyProfileActivity.ivProfilePicture.setImageBitmap(CommonTask.fetchContactImageThumbnail(this,photoId));
			}
		}
	}

	private void ChangeName() {
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
				MyProfileActivity.tvUserName.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name.toString().toUpperCase());
		}else{
			MyNetDatabase database = new MyNetDatabase(this);
			database.open();
			FacebokPerson facebokPerson = database.getFacebokPerson();
			database.close();
			if(facebokPerson != null && facebokPerson.Name != null){
				MyProfileActivity.tvUserName.setText(facebokPerson.Name.toString().toUpperCase());
			}
		}
	}
	
	public static void applyFilter(String charText){
		fb_FriendsListAdapter.applyFilter(charText);
	}
}
