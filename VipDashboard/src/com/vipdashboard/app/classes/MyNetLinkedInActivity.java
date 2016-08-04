package com.vipdashboard.app.classes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Connections;
import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.PhoneNumber;
import com.google.code.linkedinapi.schema.Position;
import com.google.code.linkedinapi.schema.Skill;
import com.google.code.linkedinapi.schema.VisibilityType;
import com.vipdashboard.app.activities.AssistanceActivity;
import com.vipdashboard.app.activities.MyProfileOldActivity;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.manager.FacebookManager;
import com.vipdashboard.app.utils.CommonConstraints;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MyNetLinkedInActivity extends Activity implements IAsynchronousTask {
	static final String CONSUMER_KEY = "753ngn83uzsj0i";
	static final String CONSUMER_SECRET = "ZMMhvssyjlFrhR1Z";

	static final String APP_NAME = "MyNet";
	static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	static final String OAUTH_CALLBACK_HOST = "litestcalback";
	static final String OAUTH_CALLBACK_URL = String.format("%s://%s",
			OAUTH_CALLBACK_SCHEME, OAUTH_CALLBACK_HOST);
	static final String OAUTH_QUERY_TOKEN = "oauth_token";
	static final String OAUTH_QUERY_VERIFIER = "oauth_verifier";
	static final String OAUTH_QUERY_PROBLEM = "oauth_problem";

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
			.getInstance().createLinkedInOAuthService(CONSUMER_KEY,
					CONSUMER_SECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(CONSUMER_KEY, CONSUMER_SECRET);

	static final String OAUTH_PREF = "LIKEDIN_OAUTH";
	static final String PREF_TOKEN = "305290cc-8e7d-4927-b9d0-c2e35bd95a87";
	static final String PREF_TOKENSECRET = "305290cc-8e7d-4927-b9d0-c2e35bd95a87";
	static final String PREF_REQTOKENSECRET = "135b23b6-02fe-4014-97ba-5a3c98506f7c";
	
	private String userID;
	private String name;
	private String pp_path;
	private String gender;
	private String rel_status;
	private String dob;
	private String pref_skill;
	private String phone_number;
	private String email;
	private String zip_code;
	private String country;
	private String webSite;
	private String feedText;
	private String feedTime;
	private String education;
	private String qualification;
	private String friendsID;
	StringBuilder stringBuilder;
	String postMessage;
	boolean isCallFromPostMessage;
	long milliseconds;
	String message;
	DownloadableAsyncTask downloadableAsyncTask;
	TextView textView = null;
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		textView = new TextView(this);
		setContentView(textView);
		userID = name = pp_path = gender=rel_status=dob=pref_skill = message = qualification =education="";
		phone_number = email = zip_code = country = webSite = feedText = feedTime=friendsID="";
		if(savedInstanceState != null && savedInstanceState.containsKey("POST")){
			isCallFromPostMessage = true;
			postMessage = savedInstanceState.getString("POST").toString();
		}
		
		final SharedPreferences pref = getSharedPreferences(CommonConstraints.OAUTH_PREF,
				MODE_PRIVATE);
		clearTokens();
		final String token = pref.getString(PREF_TOKEN, null);
		final String tokenSecret = pref.getString(PREF_TOKENSECRET, null);
		if (token == null || tokenSecret == null) {
			startAutheniticate();
		} else {
			showCurrentUser(new LinkedInAccessToken(token, tokenSecret));
		}
	}

	private void showCurrentUser(LinkedInAccessToken linkedInAccessToken) {
		final LinkedInApiClient client = factory
				.createLinkedInApiClient(linkedInAccessToken);

		new AsyncTask<Void, Void, Object>() {
			
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(MyNetLinkedInActivity.this);
				progressDialog.show();
			}

			@Override
			protected Object doInBackground(Void... params) {
				ArrayList<Object> complexData = new ArrayList<Object>();
				try {
					final Set<ProfileField> connectionFields = EnumSet.of(
							ProfileField.ID, ProfileField.MAIN_ADDRESS,
							ProfileField.PHONE_NUMBERS,
							ProfileField.LOCATION,
							ProfileField.DATE_OF_BIRTH,
							ProfileField.IM_ACCOUNTS,
							ProfileField.LOCATION_COUNTRY,
							ProfileField.LOCATION_NAME,
							ProfileField.FIRST_NAME,
							ProfileField.LAST_NAME, ProfileField.HEADLINE,
							ProfileField.INDUSTRY,
							ProfileField.CURRENT_STATUS,
							ProfileField.CURRENT_STATUS_TIMESTAMP,
							ProfileField.API_STANDARD_PROFILE_REQUEST,
							ProfileField.EDUCATIONS,
							ProfileField.PUBLIC_PROFILE_URL,
							ProfileField.POSITIONS, ProfileField.LOCATION,
							ProfileField.PICTURE_URL, ProfileField.SKILLS);
					
					if (isCallFromPostMessage) {
						client.postShare(
								"Call Drop Information!",
								"MyNet",
								postMessage.toString(),
								"http://www.oss-net.com/",
								"http://oss-net.com/wp-content/uploads/2013/12/Oss-Net-Logo-3033.png",
								VisibilityType.ANYONE);
						final Person person = client
								.getProfileForCurrentUser(connectionFields);
						complexData.add(person);
					} else {
						final Person person = client
								.getProfileForCurrentUser(connectionFields);
						complexData.add(person);
						final Connections p = client
								.getConnectionsForCurrentUser(connectionFields);
						complexData.add(p);
					}
					
				} catch (LinkedInApiClientException ex) {
					//Toast.makeText(MyNetLinkedInActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
					return null;
				}
				return complexData;
			}

			@Override
			protected void onPostExecute(Object result) {
				progressDialog.dismiss();
				if(result != null){
					if(isCallFromPostMessage){
						Toast.makeText(MyNetLinkedInActivity.this, "Post Status", Toast.LENGTH_SHORT).show();
						isCallFromPostMessage = false;
						Intent intent = new Intent(MyNetLinkedInActivity.this, AssistanceActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}else{
						ArrayList<Object> data = (ArrayList<Object>) result;
						Person person = (Person) data.get(0);
						Connections connections = (Connections) data.get(1);
						getLoginPersonInformation(person,connections);
						LoadInformation();
					}
				}
				else{
					clearTokens();
					Toast.makeText(
							MyNetLinkedInActivity.this,
							"Appliaction down due LinkedInApiClientException: "
									+ " Authokens cleared - try run application again.",
							Toast.LENGTH_LONG).show();
					finish();
				}				
			}
		}.execute();
	}

	

	private void startAutheniticate() {
		new AsyncTask<Void, Void, LinkedInRequestToken>() {
			
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(MyNetLinkedInActivity.this);
				progressDialog.show();
			}

			@Override
			protected LinkedInRequestToken doInBackground(Void... params) {
				return oAuthService.getOAuthRequestToken(OAUTH_CALLBACK_URL);
			}

			@Override
			protected void onPostExecute(LinkedInRequestToken liToken) {
				final String uri = liToken.getAuthorizationUrl();
				getSharedPreferences(OAUTH_PREF, MODE_PRIVATE)
						.edit()
						.putString(PREF_REQTOKENSECRET,
								liToken.getTokenSecret()).commit();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				startActivity(i);
				progressDialog.dismiss();
			}
		}.execute();
	}
	
	private void clearTokens() {
		getSharedPreferences(OAUTH_PREF, MODE_PRIVATE).edit()
		.remove(PREF_TOKEN).remove(PREF_TOKENSECRET)
		.remove(PREF_REQTOKENSECRET).commit();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		finishAuthenticate(intent.getData());
	}

	private void finishAuthenticate(final Uri uri) {
		if (uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
			final String problem = uri.getQueryParameter(OAUTH_QUERY_PROBLEM);
			if (problem == null) {

				new AsyncTask<Void, Void, LinkedInAccessToken>() {
					
					protected void onPreExecute() {
						progressDialog = new ProgressDialog(MyNetLinkedInActivity.this);
						progressDialog.show();
					}

					@Override
					protected LinkedInAccessToken doInBackground(Void... params) {
						final SharedPreferences pref = getSharedPreferences(
								OAUTH_PREF, MODE_PRIVATE);
						final LinkedInAccessToken accessToken = oAuthService
								.getOAuthAccessToken(
										new LinkedInRequestToken(
												uri.getQueryParameter(OAUTH_QUERY_TOKEN),
												pref.getString(
														PREF_REQTOKENSECRET,
														null)),
										uri.getQueryParameter(OAUTH_QUERY_VERIFIER));
						pref.edit()
								.putString(PREF_TOKEN, accessToken.getToken())
								.putString(PREF_TOKENSECRET,
										accessToken.getTokenSecret())
								.remove(PREF_REQTOKENSECRET).commit();
						return accessToken;
					}

					@Override
					protected void onPostExecute(LinkedInAccessToken accessToken) {
						showCurrentUser(accessToken);
						progressDialog.dismiss();
					}
				}.execute();

			} else {
				Toast.makeText(this,
						"Appliaction down due OAuth problem: " + problem,
						Toast.LENGTH_LONG).show();
				finish();
			}

		}
	}
	
	protected void getLoginPersonInformation(Person person, Connections connections) {
		try{
			if(person != null){
				if(!person.getId().toString().equals("")){
					userID = person.getId().toString();
					message += userID + "\r\n";
				}
				if(!person.getFirstName().toString().equals("") && !person.getLastName().toString().equals("")){
					name = person.getFirstName().toString() + " " + person.getLastName().toString();
					message += name + "\r\n";
				}
				if(!person.getPictureUrl().toString().equals("")){
					pp_path = person.getPictureUrl().toString();
					message += pp_path + "\r\n";
				}
				if(person.getDateOfBirth() != null){
					if(person.getDateOfBirth().getMonth() != null){
						dob += person.getDateOfBirth().getMonth().toString() + "/";
					}else{
						dob += "01/";
					}
					if(person.getDateOfBirth().getDay() != null){
						dob += person.getDateOfBirth().getDay().toString() + "/";
					}else{
						dob += "01/";
					}					
					if(person.getDateOfBirth().getYear() != null){
						dob += person.getDateOfBirth().getYear().toString();
					}else{
						dob += "1970";
					}
					message += dob + "\r\n";
					SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
					Date d = f.parse(dob);
					milliseconds = d.getTime();
				}else{
					milliseconds = new Date().getTime();
				}
				if(person.getSkills() != null){
					List<Skill> skill =  person.getSkills().getSkillList();
					for(int i=0;i<skill.size();i++){
						if(i==0){
							pref_skill += skill.get(i).getSkill().getName().toString();
							
						}else{
							pref_skill +=" ," + skill.get(i).getSkill().getName().toString();
							
						}						
					}
					message += pref_skill + "\r\n";
				}
				if(person.getPhoneNumbers() != null){
					List<PhoneNumber> number = person.getPhoneNumbers().getPhoneNumberList();
					for(int i=0;i<number.size();i++){
						if(i==0){
							phone_number += number.get(i).getPhoneNumber().toString();
						}else{
							phone_number += ","+number.get(i).getPhoneNumber().toString();
						}
					}
					message += phone_number + "\r\n";
				}
				if(person.getLocation() != null){
					if(person.getLocation().getPostalCode() != null){
						zip_code += person.getLocation().getPostalCode().toString();
						message += zip_code+"\r\n";
					}
					if(person.getLocation().getName() != null){
						country += person.getLocation().getName().toString();
						message += country + "\r\n";
					}
				}
				if(person.getCurrentStatus() != null){
					if(person.getCurrentStatus() != null){
						feedText += person.getCurrentStatus().toString();
						message += feedText + "\r\n";
					}
				}
				if(person.getCurrentStatusTimestamp() != null){
					if(person.getCurrentStatusTimestamp() > 0){
						feedTime += String.valueOf(person.getCurrentStatusTimestamp());
						message += feedTime + "\r\n";
					}
				}
				if(person.getEducations() != null){
					List<Education> educationList = person.getEducations().getEducationList();
					for(int i=0;i<educationList.size();i++){
						if(i==0){
							if(educationList.get(i).getSchoolName() != null){
								education += educationList.get(i).getSchoolName().toString();
							}else{
								education += "";
							}
							if(educationList.get(i).getDegree() != null){
								education += ","+educationList.get(i).getDegree().toString();
							}else{
								education += ",";
							}
							if(educationList.get(i).getStartDate() != null){
								String date = "01/01/"+educationList.get(i).getStartDate().getYear().toString();
								SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
								Date d = f.parse(date);
								education += ","+String.valueOf(d.getTime());
							}else{
								education += ",";
							}
							if(educationList.get(i).getEndDate() != null){
								String date = "01/01/"+educationList.get(i).getEndDate().getYear().toString();
								SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
								Date d = f.parse(date);
								education += ","+String.valueOf(d.getTime());
							}else{
								education += ",";
							}
							education += ",Education";
						}else{
							education += "|";
							if(educationList.get(i).getSchoolName() != null){
								education += educationList.get(i).getSchoolName().toString();
							}else{
								education += "";
							}
							if(educationList.get(i).getDegree() != null){
								education += ","+educationList.get(i).getDegree().toString();
							}else{
								education += ",";
							}
							if(educationList.get(i).getStartDate() != null){
								String date = "01/01/"+educationList.get(i).getStartDate().getYear().toString();
								SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
								Date d = f.parse(date);
								education += ","+String.valueOf(d.getTime());
							}else{
								education += ",";
							}
							if(educationList.get(i).getEndDate() != null){
								String date = "01/01/"+educationList.get(i).getStartDate().getYear().toString();
								SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
								Date d = f.parse(date);
								education += ","+String.valueOf(d.getTime());
							}else{
								education += ",";
							}
							education += ",Education";
						}
					}
				}
				if(person.getPositions() != null){
					List<Position> position = person.getPositions().getPositionList();
					for(int i=0;i<position.size();i++){
						if(i==0){
							if(position.get(i).getCompany().getName() != null){
								qualification += position.get(i).getCompany().getName().toString();
							}else{
								qualification += "";
							}
							if(position.get(i).getTitle() != null){
								qualification += ","+position.get(i).getTitle().toString();
							}else{
								qualification += ",";
							}
							if(position.get(i).isIsCurrent()){
								if(position.get(i).getStartDate() != null){
									String date = position.get(i).getStartDate().getMonth()+"/01/"+position.get(i).getStartDate().getYear();
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									qualification += ","+String.valueOf(d.getTime());
								}else{
									qualification += ",";
								}
								qualification += ",";
							}else{
								if(position.get(i).getStartDate() != null){
									String date = position.get(i).getStartDate().getMonth()+"/01/"+position.get(i).getStartDate().getYear();
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									qualification += ","+String.valueOf(d.getTime());
								}else{
									qualification += ",";
								}
								if(position.get(i).getEndDate() != null){
									String date = position.get(i).getEndDate().getMonth()+"/01/"+position.get(i).getEndDate().getYear();
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									qualification += ","+String.valueOf(d.getTime());
								}else{
									qualification += ",";
								}
							}
							qualification += ",Profession";
						}else{
							qualification += "|";
							if(position.get(i).getCompany().getName() != null){
								qualification += position.get(i).getCompany().getName().toString();
							}else{
								qualification += "";
							}
							if(position.get(i).getTitle() != null){
								qualification += ","+position.get(i).getTitle().toString();
							}else{
								qualification += ",";
							}
							if(position.get(i).isIsCurrent()){
								if(position.get(i).getStartDate() != null){
									String date = position.get(i).getStartDate().getMonth()+"/01/"+position.get(i).getStartDate().getYear();
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									qualification += ","+String.valueOf(d.getTime());
								}else{
									qualification += ",";
								}
								qualification += ",";
							}else{
								if(position.get(i).getStartDate() != null){
									String date = position.get(i).getStartDate().getMonth()+"/01/"+position.get(i).getStartDate().getYear();
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									qualification += ","+String.valueOf(d.getTime());
								}else{
									qualification += ",";
								}
								if(position.get(i).getEndDate() != null){
									String date = position.get(i).getEndDate().getMonth()+"/01/"+position.get(i).getEndDate().getYear();
									SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy");
									Date d = f.parse(date);
									qualification += ","+String.valueOf(d.getTime());
								}else{
									qualification += ",";
								}
							}
							qualification += ",Profession";
						}
						
					}
					if(education.length()>0){
						qualification += "|" + education;
					}
					message += qualification + "\r\n";
				}
				if(connections != null){
					List<Person> userPersons = connections.getPersonList();
					for(int i=0;i<userPersons.size();i++){
						if(i==0){
							friendsID += userPersons.get(i).getId().toString();
						}else{
							friendsID += ","+userPersons.get(i).getId().toString();
						}
					}
					message += friendsID + "\r\n";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void popUpInformation() {
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("LinkedIn Public Information").setMessage(message)
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
							Toast.makeText(MyNetLinkedInActivity.this, "Information not store", Toast.LENGTH_LONG).show();
							dialog.cancel();
							Intent intent = new Intent(MyNetLinkedInActivity.this, MyProfileOldActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void LoadInformation(){
		if(downloadableAsyncTask != null){
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		//Log.e("enter", "doInBackground");
		IFacebookManager facebookManager = new FacebookManager();
		return facebookManager.setLinkedInInformation(userID, name, pp_path, gender, rel_status, String.valueOf(milliseconds),pref_skill,phone_number,email,
		zip_code,country,webSite, feedText,feedTime,friendsID,qualification);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){			
			Toast.makeText(MyNetLinkedInActivity.this, "LinkedIn Sync Successfully", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MyNetLinkedInActivity.this, MyProfileOldActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else{
			Toast.makeText(MyNetLinkedInActivity.this, "LinkedIn Sync UnSuccessful", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MyNetLinkedInActivity.this, MyProfileOldActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
