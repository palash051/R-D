package com.vipdashboard.app.interfaces;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.vipdashboard.app.entities.Companyes;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.GroupRoot;
import com.vipdashboard.app.entities.Groups;
import com.vipdashboard.app.entities.OperatorReview;
import com.vipdashboard.app.entities.TempTableUserRoot;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserFamilyMembers;
import com.vipdashboard.app.entities.UserGroup;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnionEntityHolder;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.entities.UserGroups;
import com.vipdashboard.app.entities.UserRelationship;
import com.vipdashboard.app.entities.UserRelationshipRoot;
import com.vipdashboard.app.entities.UserRoot;
import com.vipdashboard.app.entities.UserSetting;
import com.vipdashboard.app.entities.UserSettingsRoot;
import com.vipdashboard.app.entities.Users;

public interface IUserManager {	
	
	List<Group> getGroupsByCompanyID();
	GroupRoot SetGroup(Context context,String groupName,String userIDs);
	List<User> getUsers();
	UserGroupUnions GetUserGroupUnionList();	
	User getUserLoginAuthentication(Context context, String userId,
			String password);
	/*TempTableUserRoot SetUserSignUp(String userId, String password, String f_name, String l_name, String mobile, String designation, String department, String manageremail);*/
	Companyes getCompanyByCompanyId();
	UserGroupUnions getGroupsByUserNumber();
	ArrayList<Object> getGroupMembersByGroupId(String groupID);
	UserRoot getUser(String userID);
	User checkEmailValidationCode(String code);
	UserGroups deleteGroupMemberByGroupid(String groupid, String userIds);
	Users getUsersByGroupId(String ID);
	UserGroup SetUserGroupByGroupID(String groupID, String UserIDs);
	TempTableUserRoot UserSignUp(String name, String email, String phone,String randomNumber);
	User setLogoutUserOnlineStatus();
	User GetUserByID(int userNumber);
	User setuserInformation(String name, String phNumber, String Gender, String dateofBirth,String position, String managerEmail);
	User SetUserOnlineAvailbleStatus(String statusID);
	ArrayList<Object> MyOrganizationInformation();
	Groups GetGroupsNotIncludeOfUser();
	UserGroups SetMembership(String ids);
	User GetManagerInformatoinByUserID();
	User setImage(byte[] value);
	User GetUserByMobileNumber(String mobileNumber);
	
	User getUserLoginAuthenticationByFacebook(String userID, String username,
			String name,String email, String pp_path, String gender, String rel_status, String dob,
			String religion, String education, String about, String pages,
			String groups, String apps, String mobileNumber,
			String alternativeEmail, String zipCode, String country,
			String website, String feedText, String feedTime, String frieldId,
			String qualification);
	User getUserByMobileNumber(String mobileNumber);
	UserSetting GetUserSettings();
	String SetFBPostPicture(byte[] selectedFile, int userNumber);
	String GetUserProfilePicturePath(int userNumber);
	String GetProfilePicture(int userNumber);
	String SetProfilePictureChange(byte[] selectedFile, int userNumber);
	com.vipdashboard.app.entities.UserProfilePictureListRoot GetUserProfilePicturePathList(
			String userNumbers);

	UserFamilyMembers GetUserToAddinMyFavourite();
	UserGroupUnions GetUsersByMobileNumbers(String numbers);
	UserRelationshipRoot SetMyFavouriteUser(String friendId);
	UserRelationshipRoot RemoveMyFavouriteUser(String friendId);
	/*ArrayList<Object> SetMyFavouriteUser(String friendId);
	ArrayList<Object>  RemoveMyFavouriteUser(String friendId);*/

	UserGroupUnion GertUserCurrentlocation(String number);
	UserFamilyMembers GetAllMyFavouriteUsers();
	void UpdateUserGroupSync(Context context);
	UserSetting setUserSettings(String IsSharLocation,
			String IsSeeMyCallHistory, String IsCareConnectToMyDevice,
			String IsNotifyMeAnyPromotion, String IsNotifyNetworkIncidents,
			String IsSyncDataWithTraffic, String IsSyncOnlyOnWiFi,
			String IsRecordVoiceMemo, String IsPromptTextMemo, String Remarks);
	UserGroupUnionEntityHolder GetMyFavouriteUsersEmail(String selectedUserNumber);
	UserRelationship UserRelationShipInactiveById(int frdNumber, int number);

}
