package com.vipdashboard.app.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.vipdashboard.app.base.CareIMService;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.ConsumerMessageListener;
import com.vipdashboard.app.entities.UserFamilyMembers;

import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnionRoot;

import com.vipdashboard.app.entities.LocalUser;
import com.vipdashboard.app.entities.UserGroupUnionEntityHolder;
import com.vipdashboard.app.entities.UserProfilePictureListRoot;
import com.vipdashboard.app.entities.UserProfilePictureRoot;
import com.vipdashboard.app.entities.Companyes;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.GroupRoot;
import com.vipdashboard.app.entities.Groups;
import com.vipdashboard.app.entities.SocialNetworkFliends;
import com.vipdashboard.app.entities.TempTableUserRoot;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserGroup;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.entities.UserGroups;
import com.vipdashboard.app.entities.UserRelationship;
import com.vipdashboard.app.entities.UserRelationshipRoot;
import com.vipdashboard.app.entities.UserRoot;
import com.vipdashboard.app.entities.UserSetting;
import com.vipdashboard.app.entities.UserSettingsRoot;
import com.vipdashboard.app.entities.Users;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class UserManager implements IUserManager {
	public UserManager() {

	}

	@Override
	public User getUserLoginAuthentication(Context context, String userId,
			String password) {
		User user = null;
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String networkOperator = tm.getNetworkOperator();
		int mcc = 0, mnc = 0;
		if (networkOperator != null) {
			mcc = Integer.parseInt(networkOperator.substring(0, 3));
			mnc = Integer.parseInt(networkOperator.substring(3));
		}
		UserRoot userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().LoginAuthentication,
						CommonValues.getInstance().CompanyId, userId, password,
						String.valueOf(mcc), String.valueOf(mnc),
						tm.getNetworkCountryIso(), "4"), UserRoot.class);
		if (userRoot != null) {
			user = userRoot.user;
			CommonValues.getInstance().LoginUser = (User) user;
			CommonValues.getInstance().LoginUser.UserNumber=user.UserNumber;
			
			PhoneInformationManager pm = new PhoneInformationManager();
			pm.SetPhoneBasicInfo(context);
			//xmppUserRegistration(user);
		}

		return user;
	}

	private void xmppUserRegistration(User user) {
		try {
			XMPPConnection conn=null;
			if(CommonValues.getInstance().XmppConnection==null){
				ConnectionConfiguration connConfig = new ConnectionConfiguration(
						CommonURL.getInstance().CareIMHost,
						CommonURL.getInstance().CareIMPort,
						CommonURL.getInstance().CareIMService);
				conn =new XMPPConnection(connConfig) ;
			}else{
				conn=CommonValues.getInstance().XmppConnection;
			}
			
			conn.connect();
			Registration reg = new Registration();
			reg.setType(IQ.Type.SET);
			reg.setTo(conn.getServiceName());

			reg.addAttribute("username", user.UserID);
			reg.addAttribute("password",CommonValues.getInstance().XmppUserPassword);
			reg.addAttribute("email", user.Email);
			reg.addAttribute("name", user.Name);
			PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
			conn.createPacketCollector(filter);
			conn.sendPacket(reg);
			Thread.sleep(1000);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Group> getGroupsByCompanyID() {
		ArrayList<Group> groupList = null;
		Groups groupRoot = (Groups) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetGroupsByCompanyID,
						CommonValues.getInstance().CompanyId), Groups.class);
		if (groupRoot != null)
			groupList = (ArrayList<Group>) groupRoot.groupList;
		return groupList;
	}

	@Override
	public List<User> getUsers() {
		ArrayList<User> userList = null;
		Users userRoot = (Users) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetUsersExceptLoggedIn,
						CommonValues.getInstance().CompanyId,
						CommonValues.getInstance().LoginUser.UserNumber),
				Users.class);
		if (userRoot != null)
			userList = (ArrayList<User>) userRoot.userList;

		return userList;
	}

	@Override
	public GroupRoot SetGroup(Context context, String groupName, String userIDs) {
		GroupRoot g = null;
		try {
			g = (GroupRoot) JSONfunctions.retrieveDataFromStream(String.format(
					CommonURL.getInstance().SetGroup, CommonValues
							.getInstance().CompanyId, URLEncoder.encode(
							groupName, CommonConstraints.EncodingCode),
					userIDs, CommonValues.getInstance().LoginUser.UserNumber),
					GroupRoot.class);

			ConsumerMessageListener listener = new ConsumerMessageListener(
					context);
			MultiUserChat consumerMuc = new MultiUserChat(
					CommonValues.getInstance().XmppConnection,
					groupName.replace(" ", "_")
							+ CommonURL.getInstance().CareIMConference);
			consumerMuc.create(groupName.replace(" ", "_"));
			consumerMuc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			Form form = consumerMuc.getConfigurationForm();
			Form submitForm = form.createAnswerForm();

			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {

					submitForm.setDefaultAnswer(field.getVariable());
				}
			}

			List<String> owners = new ArrayList<String>();
			/*
			 * owners.add(DataConfig.USERNAME + "@" + DataConfig.SERVICE);
			 * submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			 */
			submitForm.setAnswer("muc#roomconfig_roomname", groupName);
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);

			consumerMuc.sendConfigurationForm(submitForm);

			consumerMuc.join(CommonValues.getInstance().LoginUser.UserID);
			consumerMuc.addMessageListener(listener);
			if (g != null && g.group != null) {
				g.group.MultiUserChatId = consumerMuc;
				CommonValues.getInstance().XmppConnectedGroup.put(
						g.group.GroupID, g.group);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return g;
	}

	@Override
	public UserGroupUnions GetUserGroupUnionList() {
		return (UserGroupUnions) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetUserGroupUnionList,
						CommonValues.getInstance().CompanyId,
						CommonValues.getInstance().LoginUser.UserNumber),
				UserGroupUnions.class);
	}

	@Override
	public UserGroupUnions GetUsersByMobileNumbers(String numbers) {

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userNumber",
					CommonValues.getInstance().LoginUser.UserNumber);
			jsonObject.put("mobileNumbers", numbers);

			return (UserGroupUnions) JSONfunctions.retrieveDataFromJsonPost(
					CommonURL.getInstance().GetUsersByMobileNumbers,
					jsonObject, UserGroupUnions.class);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String GetUserProfilePicturePath(int userNumber) {
		String path = "";
		UserProfilePictureRoot userProfilePictureRoot = (UserProfilePictureRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(
								CommonURL.getInstance().GetProfilePicturePathByUserNumber,
								userNumber), UserProfilePictureRoot.class);
		if (userProfilePictureRoot != null
				&& userProfilePictureRoot.userProfilePicture != null) {
			path = userProfilePictureRoot.userProfilePicture.PPPath;
		}
		return path;
	}

	@Override
	public String GetProfilePicture(int userNumber) {
		String path = "";
		UserProfilePictureRoot userProfilePictureRoot = (UserProfilePictureRoot) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetProfilePicture, userNumber),
						UserProfilePictureRoot.class);
		if (userProfilePictureRoot != null
				&& userProfilePictureRoot.userProfilePicture != null) {
			path = userProfilePictureRoot.userProfilePicture.PPPath;
		}
		return path;
	}

	@Override
	public String SetProfilePictureChange(byte[] selectedFile, int userNumber) {
		String path = "";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userNumber", userNumber);
			jsonObject.put(
					"byteValue",
					selectedFile != null ? Base64.encodeToString(selectedFile,
							Base64.NO_WRAP) : "");

			UserProfilePictureRoot userProfilePictureRoot = (UserProfilePictureRoot) JSONfunctions
					.retrieveDataFromJsonPost(
							CommonURL.getInstance().SetUserProfilePicture,
							jsonObject, UserProfilePictureRoot.class);
			if (userProfilePictureRoot != null
					&& userProfilePictureRoot.userProfilePicture != null) {
				path = userProfilePictureRoot.userProfilePicture.PPPath;
			}
		} catch (Exception e) {

		}
		return path;
	}

	/*
	 * @Override public TempTableUserRoot SetUserSignUp(String userId, String
	 * password, String f_name, String l_name, String mobile, String
	 * designation, String department, String managerEmail) { TempTableUserRoot
	 * user = null; try{ user = (TempTableUserRoot)
	 * JSONfunctions.retrieveDataFromStream
	 * (String.format(CommonURL.getInstance().SetUserSignUp,
	 * URLEncoder.encode(userId, CommonConstraints.EncodingCode),
	 * URLEncoder.encode(password, CommonConstraints.EncodingCode),
	 * URLEncoder.encode(f_name, CommonConstraints.EncodingCode),
	 * URLEncoder.encode(l_name, CommonConstraints.EncodingCode),
	 * URLEncoder.encode(mobile, CommonConstraints.EncodingCode),
	 * URLEncoder.encode(designation, CommonConstraints.EncodingCode),
	 * URLEncoder.encode(department, CommonConstraints.EncodingCode),
	 * CommonValues.getInstance().CompanyId, URLEncoder.encode(managerEmail,
	 * CommonConstraints.EncodingCode)), TempTableUserRoot.class);
	 * }catch(UnsupportedEncodingException e){ e.printStackTrace(); }
	 * 
	 * return user; }
	 */

	@Override
	public UserProfilePictureListRoot GetUserProfilePicturePathList(
			String userNumbers) {

		return (UserProfilePictureListRoot) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetUserProfilePicturePathList,
						userNumbers), UserProfilePictureListRoot.class);

	}

	@Override
	public Companyes getCompanyByCompanyId() {
		return (Companyes) JSONfunctions.retrieveDataFromStream(String.format(
				CommonURL.getInstance().GetCompanyByCompanyId,
				CommonValues.getInstance().CompanyId), Companyes.class);
	}

	@Override
	public UserGroupUnions getGroupsByUserNumber() {
		return (UserGroupUnions) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetGroupByUserNumber,
						CommonValues.getInstance().LoginUser.UserNumber),
				UserGroupUnions.class);
	}

	@Override
	public ArrayList<Object> getGroupMembersByGroupId(String groupID) {
		ArrayList<Object> complexData = new ArrayList<Object>();
		UserGroups userGroups = (UserGroups) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetGroupMembersByGroupID,
						groupID), UserGroups.class);
		complexData.add(userGroups);
		GroupRoot groupRoot = (GroupRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetGroupCreatorByGroupID,
						groupID), GroupRoot.class);
		complexData.add(groupRoot);

		return complexData;
	}

	@Override
	public UserRoot getUser(String userID) {
		return (UserRoot) JSONfunctions.retrieveDataFromStream(String.format(
				CommonURL.getInstance().GetUserInformation, userID),
				UserRoot.class);
	}

	@Override
	public User checkEmailValidationCode(String code) {
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().CheckActivationCode, code),
						UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		return user;
	}

	@Override
	public User getUserByMobileNumber(String mobileNumber) {
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().getUserByMobileNumber,
						mobileNumber), UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		return user;
	}

	@Override
	public UserGroups deleteGroupMemberByGroupid(String groupid, String userIds) {
		UserGroups userGroups = (UserGroups) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().DeleteGroupMemberByGroupID,
						groupid, userIds,
						CommonValues.getInstance().LoginUser.UserNumber),
						UserGroups.class);
		return userGroups;
	}

	@Override
	public Users getUsersByGroupId(String ID) {
		Users users = (Users) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetUsersByGroupID, ID),
				Users.class);
		return users;
	}

	@Override
	public UserGroup SetUserGroupByGroupID(String groupID, String UserIDs) {
		UserGroup users = (UserGroup) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().SetUserGroupByGroupID,
						groupID, UserIDs), UserGroup.class);
		return users;
	}

	@Override
	public TempTableUserRoot UserSignUp(String name, String email,
			String phone, String randomNumber) {
		TempTableUserRoot user = null;
		try {
			user = (TempTableUserRoot) JSONfunctions.retrieveDataFromStream(
					String.format(CommonURL.getInstance().UserSignUp,
							URLEncoder.encode(name,
									CommonConstraints.EncodingCode), URLEncoder
									.encode(email,
											CommonConstraints.EncodingCode),
							URLEncoder.encode(phone,
									CommonConstraints.EncodingCode), URLEncoder
									.encode(randomNumber,
											CommonConstraints.EncodingCode)),
					TempTableUserRoot.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public User setLogoutUserOnlineStatus() {
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(
						CommonURL.getInstance().SetLogoutUserOnlineStatus,
						CommonValues.getInstance().CompanyId,
						CommonValues.getInstance().LoginUser.UserNumber),
				UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		return user;
	}

	@Override
	public User GetUserByID(int userNumber) {
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetUserByID, userNumber),
				UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		return user;
	}

	@Override
	public User setuserInformation(String name, String phNumber, String Gender,
			String dateofBirth, String position, String managerEmail) {
		User user = null;
		UserRoot userRoot;
		try {
			userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(String
					.format(CommonURL.getInstance().SetUserInfo, CommonValues
							.getInstance().CompanyId, CommonValues
							.getInstance().LoginUser.UserNumber, URLEncoder
							.encode(name, CommonConstraints.EncodingCode),
							URLEncoder.encode(phNumber,
									CommonConstraints.EncodingCode), URLEncoder
									.encode(Gender,
											CommonConstraints.EncodingCode),
							URLEncoder.encode(dateofBirth,
									CommonConstraints.EncodingCode), URLEncoder
									.encode(position,
											CommonConstraints.EncodingCode),
							URLEncoder.encode(managerEmail,
									CommonConstraints.EncodingCode)),
					UserRoot.class);
			if (userRoot != null) {
				user = userRoot.user;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User SetUserOnlineAvailbleStatus(String id) {
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(
								CommonURL.getInstance().UserOnlineAvaliableStatusUpdateNew,
								id,
								CommonValues.getInstance().LoginUser.UserNumber),
						UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		return user;
	}

	@Override
	public ArrayList<Object> MyOrganizationInformation() {
		ArrayList<Object> complexData = new ArrayList<Object>();
		UserGroupUnions userGroupUnions = (UserGroupUnions) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetGroupByUserNumber,
						CommonValues.getInstance().LoginUser.UserNumber),
						UserGroupUnions.class);
		complexData.add(userGroupUnions);
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetUserByID,
						CommonValues.getInstance().LoginUser.UserNumber),
				UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		complexData.add(user);
		return complexData;
	}

	@Override
	public Groups GetGroupsNotIncludeOfUser() {
		return (Groups) JSONfunctions.retrieveDataFromStream(String.format(
				CommonURL.getInstance().GetGroupsNotIncludeOfUser,
				CommonValues.getInstance().LoginUser.UserNumber), Groups.class);
	}

	@Override
	public UserGroups SetMembership(String ids) {
		UserGroups userGroups = (UserGroups) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().SetMembership,
						CommonValues.getInstance().LoginUser.UserNumber, ids),
						UserGroups.class);
		return userGroups;
	}

	@Override
	public User GetManagerInformatoinByUserID() {
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetManagerInformation,
						CommonValues.getInstance().LoginUser.UserID),
				UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		return user;
	}

	@Override
	public User setImage(byte[] value) {
		User user = null;
		/*
		 * UserRoot userRoot=(UserRoot) JSONfunctions.retrieveDataFromStream(
		 * String.format(CommonURL.getInstance().ImageConvertToByte,
		 * value),UserRoot.class); if(userRoot!=null) user=userRoot.user;
		 */
		return user;
	}

	@Override
	public User getUserLoginAuthenticationByFacebook(String userID,
			String username, String name, String email, String pp_path,
			String gender, String rel_status, String dob, String religion,
			String education, String about, String pages, String groups,
			String apps, String mobileNumber, String alternativeEmail,
			String zipCode, String country, String website, String feedText,
			String feedTime, String frieldId, String qualification) {
		User user = null;
		try {
			JSONObject jsonObject = new JSONObject();
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
			UserRoot userRoot = (UserRoot) JSONfunctions
					.retrieveDataFromJsonPost(
							CommonURL.getInstance().LoginAuthenticationByFacebookPost,
							jsonObject, UserRoot.class);
			if (userRoot != null)
				user = userRoot.user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public UserSetting setUserSettings(String IsSharLocation,
			String IsSeeMyCallHistory, String IsCareConnectToMyDevice,
			String IsNotifyMeAnyPromotion, String IsNotifyNetworkIncidents,
			String IsSyncDataWithTraffic, String IsSyncOnlyOnWiFi,
			String IsRecordVoiceMemo, String IsPromptTextMemo, String Remarks) {
		UserSetting userSettings = null;
		try {
			UserSettingsRoot userSettingsRoot = (UserSettingsRoot) JSONfunctions
					.retrieveDataFromStream(String.format(CommonURL
							.getInstance().SetUserSettings, CommonValues
							.getInstance().LoginUser.UserNumber, URLEncoder
							.encode(IsSharLocation,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsSeeMyCallHistory,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsCareConnectToMyDevice,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsNotifyMeAnyPromotion,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsNotifyNetworkIncidents,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsSyncDataWithTraffic,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsSyncOnlyOnWiFi,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsRecordVoiceMemo,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(IsPromptTextMemo,
									CommonConstraints.EncodingCode), URLEncoder
							.encode(Remarks, CommonConstraints.EncodingCode)),
							UserSettingsRoot.class);
			if (userSettingsRoot != null)
				userSettings = userSettingsRoot.userSettings;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userSettings;
	}

	@Override
	public UserSetting GetUserSettings() {
		UserSetting userSettings = null;
		UserSettingsRoot userSettingsRoot = (UserSettingsRoot) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetUserSettings,
						CommonValues.getInstance().LoginUser.UserNumber),
						UserSettingsRoot.class);
		if (userSettingsRoot != null)
			userSettings = userSettingsRoot.userSettings;
		return userSettings;
	}

	@Override
	public String SetFBPostPicture(byte[] selectedFile, int UserNumber) {
		String path = "";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userNumber", UserNumber);
			jsonObject.put(
					"byteValue",
					selectedFile != null ? Base64.encodeToString(selectedFile,
							Base64.NO_WRAP) : "");

			SocialNetworkFliends socialNetworkFliends = (SocialNetworkFliends) JSONfunctions
					.retrieveDataFromJsonPost(
							CommonURL.getInstance().SetFBPostPicture,
							jsonObject, SocialNetworkFliends.class);
			if (socialNetworkFliends != null
					&& socialNetworkFliends.socialNetworkFliend != null) {
				path = socialNetworkFliends.socialNetworkFliend.FacebookPath;
			}
		} catch (Exception e) {

		}
		return path;
	}

	@Override
	public UserFamilyMembers GetUserToAddinMyFavourite() {
		UserFamilyMembers userFamilyMembers = null;
		userFamilyMembers = (UserFamilyMembers) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetUserToAddinMyFavourite,
						CommonValues.getInstance().LoginUser.UserNumber),
						UserFamilyMembers.class);

		return userFamilyMembers;
	}

	@Override
	public UserFamilyMembers GetAllMyFavouriteUsers() {
		UserFamilyMembers userFamilyMembers = null;
		userFamilyMembers = (UserFamilyMembers) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetUserToAddinMyFavourite,
						CommonValues.getInstance().LoginUser.UserNumber),
						UserFamilyMembers.class);

		return userFamilyMembers;
	}

	/*
	 * @Override public ArrayList<Object> SetMyFavouriteUser(String friendId) {
	 * ArrayList<Object> complexdata = new ArrayList<Object>();
	 * UserRelationshipRoot userRelationshipRoot = null; userRelationshipRoot =
	 * (UserRelationshipRoot) JSONfunctions.retrieveDataFromStream(String
	 * .format(CommonURL.getInstance().SetMyFavouriteUser,
	 * CommonValues.getInstance().LoginUser.UserNumber, friendId),
	 * UserRelationshipRoot.class); complexdata.add(userRelationshipRoot);
	 * UserGroupUnions userGroupUnions = null; userGroupUnions =
	 * (UserGroupUnions) JSONfunctions .retrieveDataFromStream(String.format(
	 * CommonURL.getInstance().GetUserGroupUnionList,
	 * CommonValues.getInstance().CompanyId,
	 * CommonValues.getInstance().LoginUser.UserNumber), UserGroupUnions.class);
	 * complexdata.add(userGroupUnions); return complexdata; }
	 * 
	 * @Override public ArrayList<Object> RemoveMyFavouriteUser(String friendId)
	 * { ArrayList<Object> complexdata = new ArrayList<Object>();
	 * UserRelationshipRoot userRelationshipRoot = null; userRelationshipRoot =
	 * (UserRelationshipRoot) JSONfunctions.retrieveDataFromStream(String
	 * .format(CommonURL.getInstance().RemoveMyFavouriteUser,
	 * CommonValues.getInstance().LoginUser.UserNumber, friendId),
	 * UserRelationshipRoot.class); complexdata.add(userRelationshipRoot);
	 * UserGroupUnions userGroupUnions = null; userGroupUnions =
	 * (UserGroupUnions) JSONfunctions .retrieveDataFromStream(String.format(
	 * CommonURL.getInstance().GetUserGroupUnionList,
	 * CommonValues.getInstance().CompanyId,
	 * CommonValues.getInstance().LoginUser.UserNumber), UserGroupUnions.class);
	 * complexdata.add(userGroupUnions); return complexdata; }
	 */

	@Override
	public UserRelationshipRoot SetMyFavouriteUser(String friendId) {

		return (UserRelationshipRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().SetMyFavouriteUser,
						CommonValues.getInstance().LoginUser.UserNumber,
						friendId), UserRelationshipRoot.class);
	}

	@Override
	public UserRelationshipRoot RemoveMyFavouriteUser(String friendId) {
		return (UserRelationshipRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().RemoveMyFavouriteUser,
						CommonValues.getInstance().LoginUser.UserNumber,
						friendId), UserRelationshipRoot.class);
	}

	@Override
	public User GetUserByMobileNumber(String mobileNumber) {
		User user = null;
		UserRoot userRoot = (UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetUserByMobileNo,
						mobileNumber), UserRoot.class);
		if (userRoot != null)
			user = userRoot.user;
		return user;
	}

	@Override
	public UserGroupUnion GertUserCurrentlocation(String number) {
		UserGroupUnion userGroupUnion = null;
		UserGroupUnionRoot userRoot = (UserGroupUnionRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(
								CommonURL.getInstance().GertUserCurrentlocation,
								number), UserGroupUnionRoot.class);
		if (userRoot != null)
			userGroupUnion = userRoot.userGroupUnion;
		return userGroupUnion;
	}

	@Override
	public void UpdateUserGroupSync(Context context) {
		MyNetDatabase db = new MyNetDatabase(context);
		try {
			String[] CONTACTS_SUMMARY_PROJECTION = new String[] { Phone.NUMBER };
			String numbers = "";
			String number = "";
			ContentResolver cr = context.getContentResolver();
			Cursor cursor = cr
					.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							CONTACTS_SUMMARY_PROJECTION, null, null, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					number = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					number = number.replace("+", "").replace(" ", "")
							.replace("-", "");
					if (number.length() > 2
							&& number.substring(0, 2).equals("00")) {
						number = number.substring(2, number.length());
					} else if (number.length() > 1
							&& number.substring(0, 1).equals("0")) {
						number = number.substring(1, number.length());
					}
					if (numbers.indexOf(number) < 0)
						numbers = numbers + number + ",";
				} while (cursor.moveToNext());
			}
			cursor.close();
			if (!numbers.isEmpty()) {
				numbers = numbers.substring(0, numbers.length() - 1);
				IUserManager userManager = new UserManager();
				UserGroupUnions userGroupUnions = userManager
						.GetUsersByMobileNumbers(numbers);

				if (userGroupUnions != null
						&& userGroupUnions.userGroupUnionList != null) {
					db.open();
					Group group=null;
					for (UserGroupUnion userGroupUnion : userGroupUnions.userGroupUnionList) {
						if (userGroupUnion.Type.equals("G")) {
							db.createGroup(userGroupUnion.Name,
									userGroupUnion.ID);
							UserGroupUnions userRoot = (UserGroupUnions) JSONfunctions
									.retrieveDataFromStream(
											String.format(
													CommonURL.getInstance().GetUsersByGroupIDExceptLoginUser,
													userGroupUnion.ID,
													CommonValues.getInstance().LoginUser.UserNumber),
											UserGroupUnions.class);
							if (userRoot != null
									&& userRoot.userGroupUnionList != null) {
								for (UserGroupUnion user : userRoot.userGroupUnionList) {
									if (numbers.indexOf(user.Name.substring(3)) == -1) {
										LocalUser localUser = new LocalUser();
										localUser.ReffId = user.ID;
										localUser.UserName = user.Name;
										localUser.MobileNumber = user.Name;
										localUser.isFavourite = 0;
										localUser.UserOnlinestatus = (int) user.userOnlinestatus;
										localUser.IsFriend = 0;
										db.createUser(localUser);
									}
									db.createUserGroup(userGroupUnion.ID,
											user.ID);
								}
							}
							if(CommonValues.getInstance().XmppConnection.isConnected()){
							/*	ConsumerMessageListener listener = new ConsumerMessageListener(context);
								MultiUserChat consumerMuc=null;	
								if(CommonValues.getInstance().XmppConnectedGroup==null)
									CommonValues.getInstance().XmppConnectedGroup=new HashMap<Integer, Group>();
								if(CommonValues.getInstance().XmppConnectedGroup.containsKey(userGroupUnion.ID))
									return;
								consumerMuc = new MultiUserChat(CommonValues.getInstance().XmppConnection, userGroupUnion.Name.replace(" ", "_")+CommonURL.getInstance().CareIMConference);							
							    consumerMuc.join(CommonValues.getInstance().LoginUser.UserID, CommonValues.getInstance().XmppUserPassword);					        
							    consumerMuc.addMessageListener(listener);
							    group=new Group();
							    group.MultiUserChatId=consumerMuc;
							    group.GroupID=userGroupUnion.ID;
							    group.Name=userGroupUnion.Name;
							    CommonValues.getInstance().XmppConnectedGroup.put(group.GroupID, group);	*/							
							}
						} else {
							LocalUser user = new LocalUser();
							user.ReffId = userGroupUnion.ID;
							user.UserName = userGroupUnion.Name;
							user.MobileNumber = userGroupUnion.Name;
							user.isFavourite = userGroupUnion.isFavourite ? 1
									: 0;
							user.UserOnlinestatus = (int) userGroupUnion.userOnlinestatus;
							user.IsFriend = 1;
							db.createUser(user);
						}
					}
					CommonValues.getInstance().ConatactUserList = db
							.GetUserHashMap();
					db.close();
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			if (MyNetDatabase.mynetDatabase.isOpen())
				db.close();
		}
	}

	@Override
	public UserGroupUnionEntityHolder GetMyFavouriteUsersEmail(
			String selectedUserNumber) {
		UserGroupUnionEntityHolder userGroupUnionEntityHolder = (UserGroupUnionEntityHolder) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetMyFavouriteUsersEmail,
						CommonValues.getInstance().LoginUser.UserNumber,
						selectedUserNumber), UserGroupUnionEntityHolder.class);
		return userGroupUnionEntityHolder;
	}

	@Override
	public UserRelationship UserRelationShipInactiveById(int frdNumber,
			int number) {
		UserRelationship userRelationship = null;
		UserRelationshipRoot userRoot = (UserRelationshipRoot) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().UserRelationShipInactiveById,
						frdNumber, number), UserRelationshipRoot.class);
		if (userRoot != null)
			userRelationship = userRoot.Relationship;
		return userRelationship;
	}

}
