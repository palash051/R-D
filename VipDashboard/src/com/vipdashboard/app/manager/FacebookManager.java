package com.vipdashboard.app.manager;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Base64;


import com.vipdashboard.app.entities.AudioPlayerRoot;
import com.vipdashboard.app.entities.FacebookFriendses;
import com.vipdashboard.app.entities.FacebookPersons;
import com.vipdashboard.app.entities.GPPersons;
import com.vipdashboard.app.entities.LinkedInPersons;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserRoot;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class FacebookManager implements IFacebookManager{

	@Override
	public GPPersons setGPInformation(String userId, String name,
			String pp_path, String gender, String ralation_status, String dob,String religion,
			String education, String otherName) {
		GPPersons gpPersons = null;
		try{
			gpPersons = (GPPersons) JSONfunctions.retrieveDataFromStream(String.format(CommonURL.getInstance().SetGooglePlusInformation, 
					URLEncoder.encode(userId, CommonConstraints.EncodingCode),
					URLEncoder.encode(name, CommonConstraints.EncodingCode),
					URLEncoder.encode(pp_path, CommonConstraints.EncodingCode),
					URLEncoder.encode(gender, CommonConstraints.EncodingCode),
					URLEncoder.encode(ralation_status, CommonConstraints.EncodingCode),
					URLEncoder.encode(dob, CommonConstraints.EncodingCode),
					URLEncoder.encode(religion, CommonConstraints.EncodingCode),
					URLEncoder.encode(education, CommonConstraints.EncodingCode),
					URLEncoder.encode(otherName, CommonConstraints.EncodingCode),
					CommonValues.getInstance().LoginUser.UserNumber), GPPersons.class);
			
		}catch(Exception ex){
			
		}
		return gpPersons;
	}

	@Override
	public FacebookPersons SetFacebookPersonInformation(String userID,
			String username, String name,String email, String pp_path, String gender, String rel_status,
			String dob, String religion, String education, String about,
			String pages, String groups, String apps, String mobileNumber,
			String alternativeEmail, String zipCode, String country,
			String website, String feedText, String feedTime, String frieldId,
			String qualification) {
		FacebookPersons facebokPerson = null;
try{
			
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("fB_UserID", userID);
			jsonObject.put("fb_UserName", username);
			jsonObject.put("name", name);
			jsonObject.put("primaryEmail", email);
			jsonObject.put("pp_Path", pp_path);
			jsonObject.put("gender", gender);
			jsonObject.put("relationship_Status", rel_status);
			jsonObject.put("DateOfBirth", dob);
			jsonObject.put("religion", religion);
			jsonObject.put("professional_Skills", education);
			jsonObject.put("about", about);
			jsonObject.put("pages", pages);
			jsonObject.put("groups", groups);
			jsonObject.put("apps", apps);
			jsonObject.put("MobileNumber", mobileNumber);
			jsonObject.put("Alternate_Emails", alternativeEmail);
			jsonObject.put("ZIPCode", zipCode);
			jsonObject.put("Country", country);
			jsonObject.put("Website", website);
			jsonObject.put("FeedText", feedText);
			jsonObject.put("Feedtime", feedTime);
			jsonObject.put("friendsIDs", frieldId);
			jsonObject.put("qualificationExperiences", qualification);
			jsonObject.put("userId", CommonValues.getInstance().LoginUser.UserNumber);			
			facebokPerson =(FacebookPersons) JSONfunctions.retrieveDataFromJsonPost(CommonURL.getInstance().SetFBPersonalInformationPost,jsonObject, FacebookPersons.class);
			
		}catch(Exception ex){
		}
		return facebokPerson;
	}

	@Override
	public LinkedInPersons setLinkedInInformation(String userID, String name,
			String pp_path, String gender, String rel_status, String dob,
			String pref_skill, String phone_number, String email,
			String zip_code, String country, String webSite, String feedText,
			String feedTime, String friendsID, String qualification) {
		LinkedInPersons linkedInPerson = null;
		try{
			linkedInPerson = (LinkedInPersons) JSONfunctions.retrieveDataFromStream(String.format(CommonURL.getInstance().SetLinkedInInformation, 
					URLEncoder.encode(userID, CommonConstraints.EncodingCode),
					URLEncoder.encode(name, CommonConstraints.EncodingCode),
					URLEncoder.encode(pp_path, CommonConstraints.EncodingCode),
					URLEncoder.encode(gender, CommonConstraints.EncodingCode),
					URLEncoder.encode(rel_status, CommonConstraints.EncodingCode),
					URLEncoder.encode(dob, CommonConstraints.EncodingCode),
					URLEncoder.encode(pref_skill, CommonConstraints.EncodingCode),
					URLEncoder.encode(phone_number, CommonConstraints.EncodingCode),
					URLEncoder.encode(email, CommonConstraints.EncodingCode),
					URLEncoder.encode(zip_code, CommonConstraints.EncodingCode),
					URLEncoder.encode(country, CommonConstraints.EncodingCode),
					URLEncoder.encode(webSite, CommonConstraints.EncodingCode),
					URLEncoder.encode(feedText, CommonConstraints.EncodingCode),
					URLEncoder.encode(feedTime, CommonConstraints.EncodingCode),
					URLEncoder.encode(friendsID, CommonConstraints.EncodingCode),
					URLEncoder.encode(qualification, CommonConstraints.EncodingCode),
					CommonValues.getInstance().LoginUser.UserNumber), LinkedInPersons.class);
			
		}catch(Exception ex){
			
		}
		return linkedInPerson;
	}

	@Override
	public FacebookFriendses CheckedFriendAndFamily() {
		FacebookFriendses facebokFriendses = null;
		try{
			facebokFriendses = (FacebookFriendses) JSONfunctions.retrieveDataFromStream(
					String.format(CommonURL.getInstance().CheckFriendsAndFamily,					
					CommonValues.getInstance().LoginUser.UserNumber), FacebookFriendses.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return facebokFriendses;
	}

	@Override
	public FacebookPersons GetFacebookProfilePicture() {
		FacebookPersons facebokPerson = null;
		try{
			facebokPerson = (FacebookPersons) JSONfunctions.retrieveDataFromStream(
					String.format(CommonURL.getInstance().GetProfilePicture,					
					CommonValues.getInstance().LoginUser.UserNumber), FacebookPersons.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return facebokPerson;
	}

	@Override
	public User GetFacebookInfoByUserNumber(String userID) {
		
		User user=null;
		UserRoot userRoot=(UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetFacebookInfoByUserNumber,					
						userID), UserRoot.class);
		if(userRoot!=null)
			user=userRoot.user;
		return user;
	}

	@Override
	public String SetAudio(byte[] selectedFile, int userNumber) {
		String Voicepath="";
		try{
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("userNumber",userNumber);
			jsonObject.put("byteValue",selectedFile!=null? Base64.encodeToString(selectedFile,Base64.NO_WRAP):"");
			
			AudioPlayerRoot audioPlayerRoot=(AudioPlayerRoot) JSONfunctions.retrieveDataFromJsonPost(CommonURL.getInstance().SetAudio,jsonObject, AudioPlayerRoot.class);
			if(audioPlayerRoot!=null && audioPlayerRoot.audioPlayer!=null){
				Voicepath=audioPlayerRoot.audioPlayer.AudioFilePath;
			}
			return Voicepath;
		}catch (Exception e) {
			
		}
		return null;
	}

}
