package com.vipdashboard.app.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.HttpMethod;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.manager.FacebookManager;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyNetFacebookActivity extends Activity implements IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressBar;
	TextView textview;
	private String userID;
	private String name;
	private String pp_path;
	private String gender;
	private String rel_status;
	private String dob;
	private String religion;
	private String pref_skill;
	private String about;
	private String pages;
	private String groups;
	private String apps;
	private String frieldId;
	private String username;
	private String mobileNumber;
	private String alternativeEmail;
	private String zipCode;
	private String country;
	private String website;
	private String feedText;
	private String feedTime;
	private String qualification;
	private String education;
	private String email;
	long DurationFromMillisecond;
	long DurationToMillisecond;
	long DOBmilliseconds;
	String message;  
	int count=0;
	public static Bitmap Profile_picture_path = null;
	public static byte[] picByteValue;
	public static Session session;
	public static GraphUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		textview = new TextView(this);
		setContentView(textview);
		progressBar = new ProgressDialog(this);
		userID = name = pp_path = gender=rel_status=dob=religion=pref_skill=about=pages=groups=apps = qualification="";
		message=frieldId=username=mobileNumber=alternativeEmail=zipCode=country=website=feedText=feedTime=education="";
		email = "";
		getInformtion(user, session);
		
	}
	private void getFriedLists(final GraphUser user,
			final Session session) {
		if(user != null){
			Bundle bundle = new Bundle();
			bundle.putString("fields","id,name,email,username,relationship_status,bio,birthday,location,gender,religion");
			new  Request(session,"/me/friends",bundle,HttpMethod.GET,new Request.Callback() {
				public void onCompleted(Response response) {
					try{
						GraphObject graphObject = response.getGraphObject();
						JSONObject jsonObject = graphObject.getInnerJSONObject();
						JSONArray root = jsonObject.getJSONArray("data");
						if(root.length()  > 0){
							for(int i=0;i<root.length();i++){
								JSONObject rootJsonObject = root.getJSONObject(i);
								if(i==0){
									frieldId += rootJsonObject.optString("id");
									
								}else{
									frieldId += " ," + rootJsonObject.optString("id");
								}
							}
						}
						message += frieldId + "\r\n";
						LoadInformation();
					}catch (Exception ex) {
						Toast.makeText(MyNetFacebookActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
						onBackPressed();
					}
				}
			}
			).executeAsync();  
		}
	}	
	
	private void getProfilePicturePath(GraphUser user, Session session) {
		if(user != null){
			URL img_value = null;
			try{
				img_value = new URL("http://graph.facebook.com/"+user.getId().toString()+"/picture?type=large");
			}catch (Exception e) {
				e.printStackTrace();
			}
			pp_path = img_value.toString();
			message += pp_path + "\r\n";
		}
	}
	
	private void getGroups(GraphUser user,Session session) {
		if(user != null){
			Bundle bundle = new Bundle();
			bundle.putString("fields", "id,name,privacy");
			new Request(session, "/me/groups", bundle, HttpMethod.GET, new Request.Callback() {				
				@Override
				public void onCompleted(Response response) {
					try{
						String educationMessage = "";
						GraphObject graphObject = response.getGraphObject();
						JSONObject jsonObject = graphObject.getInnerJSONObject();
						JSONArray root = jsonObject.getJSONArray("data");
						if(root.length()  > 0){
							for(int i=0;i<root.length();i++){
								JSONObject rootJsonObject = root.getJSONObject(i);
								if (i == 0) {
									educationMessage += "[ "+ rootJsonObject.optString("name")
														+ " ]";
								} else {
									educationMessage += ",[ "+ rootJsonObject.optString("name")
														+ " ]";
								}		
							}
							groups = educationMessage;
							message += groups + "\r\n";
						}else{
							groups = "";
						}
						
					}catch (Exception ex) {
						Toast.makeText(MyNetFacebookActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
						onBackPressed();
					}
				}
			}).executeAsync();
		}
	}
	
	private void getInformtion(GraphUser user, Session session) {
		if(user!=null){
			Toast.makeText(MyNetFacebookActivity.this, "WelCome to " + user.getName(), Toast.LENGTH_LONG).show();
			try{
				if(user.asMap().get("email") != null){
					email += user.asMap().get("email").toString();
					message += user.asMap().get("email").toString()+"\r\n";
				}else{
					message += "Email Null"+"\r\n";
				}
				if(user.getId() != null){
					userID = user.getId().toString();
					message += userID + "\r\n";
				}
				if(user.getUsername() != null){
					username = user.getUsername().toString();
					message += username + "\r\n";
				}
				if(user.getName() != null){
					name = user.getName().toString();
					message += name + "\r\n";
				}
				if(user.asMap().get("gender") != null){
					gender=  user.asMap().get("gender").toString();
					message += gender + "\r\n";
				}
				if(user.asMap().get("relationship_status") != null){
					rel_status = user.asMap().get("relationship_status").toString();
					message += rel_status + "\r\n";
				}
				if(user.asMap().get("birthday") != null){
					dob = user.asMap().get("birthday").toString();
					message += dob + "\r\n";
					SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
					Date d = f.parse(dob);
					DOBmilliseconds = d.getTime();
				}else{
					DOBmilliseconds = new Date().getTime();
				}
				if(user.asMap().get("religion") != null){
					religion = user.asMap().get("religion").toString();
					message += religion + "\r\n";
				}
				if(user.asMap().get("bio") != null){
					about  = user.asMap().get("bio").toString();
					message += about + "\r\n";
				}
				if(user.asMap().get("website") != null){
					website = user.asMap().get("website").toString();
					message += website + "\r\n";
				}
				if(user.getLocation().getProperty("name") != null){
					country = user.getLocation().getProperty("name").toString();
					message += country +"\r\n";
				}
				
				if(user.asMap().get("education") != null){
					JSONArray root = (JSONArray) user.getProperty("education");
					for(int i=0;i<root.length();i++){
						JSONObject rootJsonObject = root.getJSONObject(i);
						if(i==0){
							if(rootJsonObject.has("school")){
								JSONObject school = rootJsonObject.getJSONObject("school");
								if(school.has("name")){
									education += school.getString("name");
								}
							}else{
								education += "";
							}
							if(rootJsonObject.has("degree")){
								JSONObject degree = rootJsonObject.getJSONObject("degree");
								if(degree.has("name")){
									education += "~" + degree.getString("name");
								}
							}else{
								education += "~";
							}
							education += "~";
							if(rootJsonObject.has("year")){
								JSONObject year = rootJsonObject.getJSONObject("year");
								if(year.has("name")){
									String date = "01/01/"+year.getString("name");
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									education += "~"+String.valueOf(d.getTime());
								}else{
									education += "~";
								}
							}else{
								education += "~";
							}
							
							education += "~Education";
						}else{
							education += "|";
							if(rootJsonObject.has("school")){
								JSONObject school = rootJsonObject.getJSONObject("school");
								if(school.has("name")){
									education += school.getString("name");
								}
							}else{
								education += "";
							}
							if(rootJsonObject.has("degree")){
								JSONObject degree = rootJsonObject.getJSONObject("degree");
								if(degree.has("name")){
									education += "~" + degree.getString("name");
								}
							}else{
								education += "~";
							}
							education += "~";
							if(rootJsonObject.has("year")){
								JSONObject year = rootJsonObject.getJSONObject("year");
								if(year.has("name")){
									String date = "01/01/"+year.getString("name");
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									education += "~"+String.valueOf(d.getTime());
								}else{
									education += "~";
								}
							}else{
								education += "~";
							}
							
							education += "~Education";
						}
					}
				}
				
				if(user.asMap().get("work") != null){
					JSONArray root = (JSONArray) user.getProperty("work");
					for(int i=0;i<root.length();i++){
						JSONObject rootJsonObject = root.getJSONObject(i);
						if(i==0){
							if(rootJsonObject.has("employer")){
								JSONObject employee = rootJsonObject.getJSONObject("employer");
								if(employee.has("name")){
									qualification += employee.getString("name");
								}
							}else{
								qualification += "";
							}
							if(rootJsonObject.has("position")){
								JSONObject position = rootJsonObject.getJSONObject("position");
								if(position.has("name")){
									qualification += "~" + position.getString("name");
								}
							}else{
								qualification += "~";
							}
							if(rootJsonObject.has("start_date")){
								String startdate = rootJsonObject.getString("start_date");
								String[] start = startdate.split("-");
								if(start.length == 3){
									startdate = "";
									startdate += start[1] + "/" + start[2] + "/" + start[0];								
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(startdate);
									DurationFromMillisecond = d.getTime();
									qualification += "~"+String.valueOf(DurationFromMillisecond);
								}else{
									qualification += "~";
								}
							}else{
								qualification += "~";
							}
							if(rootJsonObject.has("end_date")){
								String endDate = rootJsonObject.getString("end_date");
								String[] end = endDate.split("-");
								if(end.length == 3){
									endDate = "";
									endDate += end[1] + "/" + end[2] + "/" + end[0];
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(endDate);
									DurationToMillisecond = d.getTime();
									qualification += "~"+String.valueOf(DurationToMillisecond);
								}else{
									qualification += "~";
								}
							}else{
								qualification += "~";
							}
							qualification += "~Profession";
						}else{
							qualification += "|";
							if(rootJsonObject.has("employer")){
								JSONObject employee = rootJsonObject.getJSONObject("employer");
								if(employee.has("name")){
									qualification += employee.getString("name");
								}
							}else{
								qualification += "";
							}
							if(rootJsonObject.has("position")){
								JSONObject position = rootJsonObject.getJSONObject("position");
								if(position.has("name")){
									qualification += "~" + position.getString("name");
								}
							}else{
								qualification += "~";
							}
							if(rootJsonObject.has("start_date")){
								String startdate = rootJsonObject.getString("start_date");
								String[] start = startdate.split("-");
								if(start.length == 3){
									startdate = "";
									startdate += start[1] + "/" + start[2] + "/" + start[0];								
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(startdate);
									DurationFromMillisecond = d.getTime();
									qualification += "~"+String.valueOf(DurationFromMillisecond);
								}else{
									qualification += "~";
								}
							}else{
								qualification += "~";
							}
							if(rootJsonObject.has("end_date")){
								String endDate = rootJsonObject.getString("end_date");
								String[] end = endDate.split("~");
								if(end.length == 3){
									endDate = "";
									endDate += end[1] + "/" + end[2] + "/" + end[0];
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(endDate);
									DurationToMillisecond = d.getTime();
									qualification += "~"+String.valueOf(DurationToMillisecond);
								}else{
									qualification += "~";
								}
							}else{
								qualification += "~";
							}
							qualification += "~Profession";
						}													
					}
					if(education.length()>0){
						qualification += "|" + education;
					}					
					message += qualification + "\r\n";
				}else{
					pref_skill = "";
				}
				getFeed(user,session);			
		}catch(Exception ex){
			Toast.makeText(MyNetFacebookActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
			onBackPressed();
		}
	}
	}
	
	private void getFeed(final GraphUser user, final Session session) {
		if(user != null){
			try{
				new Request(session,"/me/feed",null,HttpMethod.GET,new Request.Callback() {
					public void onCompleted(Response response) {
						if(response != null){
							try{
								GraphObject graphObject = response.getGraphObject();
								JSONObject jsonObject = graphObject.getInnerJSONObject();
								JSONArray jsonArray = jsonObject.getJSONArray("data");
								if(jsonArray.length()>0){
									JSONObject rootJsonObject = jsonArray.getJSONObject(0);
									if(rootJsonObject.has("message")){
										feedText += rootJsonObject.getString("message").toString();
										message += feedText + "\r\n";
									}else if(rootJsonObject.has("story")){
										feedText += rootJsonObject.getString("story").toString();
										message += feedText +"\r\n";
									}
									if(rootJsonObject.has("created_time")){
										feedTime += rootJsonObject.getString("created_time").toString();
										String[] value = feedTime.split("T");										
										feedTime = value[0].toString();
										message += feedTime + "\r\n";
										value = feedTime.split("-");
										if(value.length == 3){
											feedTime = "";
											feedTime += value[1] + "/" + value[2] + "/" + value[0];
											SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
											Date d = f.parse(feedTime);
											DOBmilliseconds = d.getTime();
											feedTime = String.valueOf(DOBmilliseconds);
										}										
									}
								}
								
							}catch (Exception ex) {
								Toast.makeText(MyNetFacebookActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
								onBackPressed();
							}
							getGroups(user, session);
							getProfilePicturePath(user, session);
							getFriedLists(user, session);
						}
					}
				}
				).executeAsync();
			}catch (Exception ex) {
				Toast.makeText(MyNetFacebookActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
				onBackPressed();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private void popUpInformation() {
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Facebook Public Information").setMessage(message)
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
							Toast.makeText(MyNetFacebookActivity.this, "Information not store", Toast.LENGTH_LONG).show();
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

	private void LoadInformation() {
		if(downloadableAsyncTask != null){
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressBar.show();
	}

	@Override
	public void hideProgressLoader() {
		progressBar.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		Log.e("enter", "doInBackground");
		try {
			URL url = new URL(pp_path);
			Profile_picture_path = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Profile_picture_path.compress(Bitmap.CompressFormat.PNG, 100, stream);
			picByteValue = stream.toByteArray();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IFacebookManager facebookManager = new FacebookManager();
		return facebookManager.SetFacebookPersonInformation(userID,username,name,email,pp_path,gender,rel_status,String.valueOf(DOBmilliseconds),religion,pref_skill,about,
		pages,groups,apps, mobileNumber, alternativeEmail,zipCode,country,website,feedText,feedTime,frieldId,
		qualification);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			Toast.makeText(MyNetFacebookActivity.this, "Facebook Sync Successfully", Toast.LENGTH_LONG).show();
			onBackPressed();
		}else{
			Toast.makeText(MyNetFacebookActivity.this, "Facebook Sync UnSuccessfull", Toast.LENGTH_LONG).show();
			onBackPressed();
		}
	}
	
	
}
