package com.vipdashboard.app.interfaces;

import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.FacebookFriendses;
import com.vipdashboard.app.entities.FacebookPersons;
import com.vipdashboard.app.entities.GPPersons;
import com.vipdashboard.app.entities.LinkedInPersons;

public interface IFacebookManager {
	GPPersons setGPInformation(String userId, String name, String pp_path,
			String gender, String ralation_status, String dob, String religion,
			String education, String otherName);
	FacebookPersons SetFacebookPersonInformation(String userID, String username,
			String name,String email, String pp_path, String gender, String rel_status, String dob,
			String religion, String education, String about, String pages,
			String groups, String apps, String mobileNumber,
			String alternativeEmail, String zipCode, String country,
			String website, String feedText, String feedTime, String frieldId,
			String qualification);
	LinkedInPersons setLinkedInInformation(String userID, String name, String pp_path,
			String gender, String rel_status, String valueOf,
			String pref_skill, String phone_number, String email,
			String zip_code, String country, String webSite, String feedText,
			String feedTime, String friendsID, String qualification);
	FacebookFriendses CheckedFriendAndFamily();
	
	FacebookPersons GetFacebookProfilePicture();
	
	User GetFacebookInfoByUserNumber(String userID);
	
	String SetAudio(byte[] audioFile, int userNumber);
}
