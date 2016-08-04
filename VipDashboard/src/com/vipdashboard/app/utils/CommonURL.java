package com.vipdashboard.app.utils;

/**+
 * Singleton Class Use for initializing common URL values
 */

public class CommonURL {
   /*private String baseUrl = "http://59.152.91.34:9012/";
	public String getImageServer="http://59.152.91.34:9134/CountryFlags/";
	public String getCareImageServer="http://59.152.91.34:9134/";*/
	//private String baseUrl="http://192.168.1.8:9012/";
	//private String baseUrl = "http://www.mumtazcare.com:9012/";
	//private String baseUrl="http://142.4.8.142:9011/";
    private String baseUrl = "http://www.mumtazcare.com:9012/"; 
	public String getImageServer="http://www.mumtazcare.com:9134/CountryFlags/";
	public String getCareImageServer="http://www.mumtazcare.com:9134/";
	public String getCareImageServerDownload="http://www.mumtazcare.com:9134/MumtazCare/";
	
	//private String baseUrl = "http://192.168.1.26:9012/";
	//private String baseUrl = "http://59.152.91.34:9011/";55
	/*private String baseUrl = "http://59.152.91.34:9012/";
	 * 
	 * 
	*/
	
	/* private String baseUrl = "http://198.57.209.101:9007/"; 
	 public String getImageServer = "http://198.57.209.101:9134/CountryFlags/";
	 public String getCareImageServer = "http://198.57.209.101:9134/";
	 

	public String getCareImageServerDownload = "http://198.57.209.101:9134/MumtazCare/";*/
	public String CareIMHost="59.152.91.34";
	public int CareIMPort=5222;
	public String CareIMService="conio.org";
	public String CareIMConference="@conference.conio.org";
	
/*	private String baseUrl = "http://198.57.209.101:9011/"; 
	public String getImageServer="http://198.57.209.101:9134/CountryFlags/";
	public String getCareImageServer="http://198.57.209.101:9134/";*/
	
	public String LoginAuthentication = baseUrl+"UserService/LoginAuthentication?companyID=%s&mobileNo=%s&password=%s&MCC=%s&MNC=%s&Country=%s&GMTOffsetValue=%s";
	public String LoginAuthenticationJ = baseUrl+"UserService/LoginAuthenticationJ?companyID=%s&mobileNo=%s&password=%s&MCC=%s&MNC=%s&Country=%s&GMTOffsetValue=%s";
	public String GetGroupsByCompanyID =  baseUrl+"UserService/GetGroupsByCompanyID?companyid=%s";
	public String GetLiveCollaborationsByGroupId =  baseUrl+"LiveCollaborationService/GetLiveCollaborationsByGroupId?groupId=%s";
	public String SetLiveCollaborations =  baseUrl+"LiveCollaborationService/SetLiveCollaboration?msgFrom=%s&msgText=%s&msgTo=%s&latitude=%s&longitude=%s&userType=%s";
	public String GetLiveNotificationsByGroupId =  baseUrl+"LiveNotificationService/GetLiveNotificationsByGroupId?groupId=%s";
	public String GetLiveNotificationsByMsgTo =  baseUrl+"LiveNotificationService/GetLiveNotificationsByMsgTo?msgTo=%s";
	public String GetCriticalLiveAlarmByCompanyID =  baseUrl+"LiveAlarmService/GetCriticalLiveAlarmByCompanyID?companyID=%s&rowcount=%s";
	public String GetLatestUpdate =  baseUrl+"LiveAlarmService/GetLatestUpdate?companyID=%s";
	public String SetGroup =  baseUrl+"UserService/SetGroup?companyID=%s&name=%s&userIDs=%s&createdBy=%s";
	public String GetUsers =  baseUrl+"UserService/GetUserByCompanyID?companyID=%s";
	public String SendNotification =  baseUrl+"LiveNotificationService/SendNotification?msgFrom=%s&msgText=%s&levelId=%s&groupuserIDs=%s";
    public String GetCollaborationGroupUserListByMsgFrom= baseUrl+"LiveCollaborationService/GetCollaborationGroupUserListByMsgFrom?companyId=%s&msgFrom=%s";
	public String GetLevelConfigByCompanyID =  baseUrl+"LiveNotificationService/GetLevelConfigByCompanyID?compamyID=%s";
	public String GetLiveCollaborationsByMsgFrom= baseUrl+"LiveCollaborationService/GetLiveCollaborationsByMsgFrom?msgFrom=%s&msgTo=%s&msgToType=%s&longDate=%s&rowcount=%s";
	public String GetUserGroupUnionList= baseUrl+"UserService/GetUserGroupUnionListForMumtazCare?companyId=%s&userNumber=%s";
    public String SetUserSignUp =  baseUrl+"UserService/SetUserSignUp?userID=%s&password=%s&firstname=%s&lastName=%s&mobile=%s&designation=%s&department=%s&companyID=%s&managerEmailAddress=%s";
	public String Get2GByCompanyID =  baseUrl+"StatisticsService/GetStatistic2G?companyID=%s";
	public String Get3GByCompanyID =  baseUrl+"StatisticsService/GetStatistic3G?companyID=%s";
	public String GetLTEByCompanyID =  baseUrl+"StatisticsService/GetLTEKPIStats?companyID=%s";
	public String GetNotificationGroupUserList= baseUrl+"LiveNotificationService/GetNotificationGroupUserList?msgFrom=%s";
	public String GetLiveNotificationByUser= baseUrl+"LiveNotificationService/GetLiveNotificationByUser?msgFrom=%s&msgTo=%s&msgToType=%s&longDate=%s&rowcount=%s";
	public String GetCompanyByCompanyId =  baseUrl+"CompanyService/GetCompany?id=%s";
    public String GetTTStatusByTTCode =  baseUrl+"TTrequestService/GetTTStetusByTTCode?ttCode=%s";
	public String GetSiteNotification =  baseUrl+"TTrequestService/GetSiteInformationBySiteID?companyID=%s&siteID=%s";
	public String GetGroupByUserNumber =  baseUrl+"UserService/GetGroupByUser?userNumber=%s";
	public String GetLiveNotificationByUserDateTime =  baseUrl+"LiveNotificationService/IsNewNotificationFound?from=%s&lastNotifiactionTime=%s";
	public String GetLiveAlarmByCompanyIdDateTime =  baseUrl+"LiveAlarmService/IsLiveAlarmFound?companyId=%s&lastAlarmTime=%s";
	public String GetGroupMembersByGroupID =  baseUrl+"UserService/GetUsersByGroupID?groupId=%s";
	public String GetGroupCreatorByGroupID =  baseUrl+"UserService/GetGroupByIDForCreatedBy?id=%s";
	public String GetUserInformation =  baseUrl+"UserService/GetUserByID?userID=%s";
	public String GetNetworkDataTraffic =  baseUrl+"StatisticsService/GetNetworkDataTraffic";
	public String GetNetworkAvailability  =  baseUrl+"StatisticsService/GetNetworkAvailability";
	public String GetKPIReport =  baseUrl+"StatisticsService/GetDailyKPI";
	public String CheckActivationCode =  baseUrl+"UserService/SetUserByActivationCode?activationcode=%s";
	public String getUserByMobileNumber =  baseUrl+"UserService/getUserByMobileNumber?mobileNumber=%s";
	public String UserOnlineAvaliableStatus =  baseUrl+"UserService/UserOnlineStatusUpdate?statusID=%s&userNumber=%s&companyID=%s";
	//public String SetTTStatusComments =  baseUrl+"TTrequestService/SetTTStatusComments?ttSttID=%s&comments=%s&originator=%s&userName=%s";
	public String DeleteGroupMemberByGroupID =  baseUrl+"UserService/RemoveUserFromUserGroup?groupID=%s&userIDs=%s&userNumber=%s";
    public String GetUsersByGroupID =  baseUrl+"UserService/GetUsersNotMemberOfGroup?groupID=%s";
	public String SetUserGroupByGroupID =  baseUrl+"UserService/SetUserInUserGroup?groupID=%s&userIDs=%s";
	public String GetAllAlarmsByCompanyID =  baseUrl+"LiveAlarmService/GetLiveAlarmByCompanyID?companyID=%s&rowcount=%s";
	public String GetLiveCollaborationByUserDateTime =  baseUrl+"LiveCollaborationService/IsNewCollaborationFound?toMe=%s&lastCollaborationTime=%s";
	public String UserSignUp =  baseUrl+"UserService/SetUserSignUp?fullName=%s&userID=%s&mobile=%s&randomNumber=%s";
	public String SetLogoutUserOnlineStatus =  baseUrl+"UserService/SetLogOut?companyID=%s&userNumber=%s";
	public String GetUserByID =  baseUrl+"UserService/GetUserByID?userID=%s";
	public String SetUserInfo =  baseUrl+"Userservice/SetUserInfo?companyID=%s&userNumber=%s&name=%s&mobileNo=%s&gender=%s&dateOfBirth=%s&position=%s&managerEmail=%s";
	public String GetGroupsNotIncludeOfUser =  baseUrl+"UserService/GetGroupsNotIncludeOfUser?userNumber=%s";
	public String SetMembership =  baseUrl+"UserService/SetMembership?userNumber=%s&groupIDs=%s";
	public String SetTTStatusComments =  baseUrl+"TTrequestService/SetTTStatusComments?ttCode=%s&status=%s&comments=%s&userName=%s";
	public String GetMemberInformation =  baseUrl+"UserService/GetManagerInfoByCurrentUser?userNumber=%s&groupID=%s";
	public String GetCeasedTimeInLastHour = baseUrl+"LiveAlarmService/GetCeaseTimeInLastHour?companyID=%s";
	public String GetManagerInformation = baseUrl+"UserService/GetManagerInfoByCurrentUser?userID=%s";
	public String SetFBPersonalInformation = baseUrl+"SocialNetworkService/SetFBPerson?fB_UserID=%s&fb_UserName=%s&name=%s&primaryEmail=%s&pp_Path=%s&gender=%s&relationship_Status=%s&dateOfBirth=%s&religion=%s&professional_Skills=%s&about=%s&pages=%s&groups=%s&apps=%s&MobileNumber=%s&Alternate_Emails=%s&ZIPCode=%s&Country=%s&Website=%s&FeedText=%s&Feedtime=%s&friendsIDs=%s&qualificationExperiences=%s&userId=%s";
	public String GetDailyKPI = baseUrl+"DashBoardService/GetDashBoardKPIByKPIID?kpiType=%s";
	public String GetParentKPI = baseUrl+"DashBoardService/GetKPIConfig?parentKPIID=%s";
	public String SetLinkedInInformation = baseUrl+"SocialNetworkService/SetLinkedInPerson?in_UserID=%s&name=%s&pp_Path=%s&gender=%s&maritalStatus=%s&DateOfBirth=%s&professional_Skills=%s&MobileNumber=%s&Alternate_Emails=%s&ZIPCode=%s&Country=%s&Website=%s&FeedText=%s&Feedtime=%s&friendsIDs=%s&qualificationExperiences=%s&userId=%s";
	public String SetGooglePlusInformation = baseUrl+"SocialNetworkService/SetGP_Person?gp_UserID=%s&name=%s&pp_Path=%s&gender=%s&relationship_Status=%s&dateOfBirth=%s&religion=%s&professional_Skills=%s&otherNames=%s&MobileNumber=%s&Alternate_Emails=%s&ZIPCode=%s&Country=%s&Website=%s&FeedText=%s&Feedtime=%s&friendsIDs=%s&qualificationExperiences=%s&userId=%s";
	//public String ImageConvertToByte = "http://192.168.101.16:9000/UserService/byteArrayToImage?byteArrayIn=%d";
	public String CheckFriendsAndFamily = baseUrl+"SocialNetworkService/GetFaceBookFriendListByUserId?userNumber=%s";
	public String GoogleChartUrl = "http://chart.apis.google.com/chart?chco=3F48CC,129ACC&cht=p3&chdls=858585,15&";
	public String GoogleBarChartUrl = "http://chart.apis.google.com/chart?chs=500x200&chbh=a&chm=N,000000,0,-1,10&chg=20,20&chco=4D89F9&cht=bvg&chxt=x,y&";
	public String GoogleChartServiceUsage = GoogleChartUrl+"chdlp=bv|l&chds=a&chs=380x350&chdl=%s|%s|%s|%s|%s|%s|%s&chl=%s|%s|%s|%s|%s|%s|%s&chd=t:%s,%s,%s,%s,%s,%d,%s";
	public String GoogleChartTotalCalls = GoogleChartUrl+"chs=300x220&chdlp=bv|&chds=a&chdl=%s|%s|%s&chl=%s|%s|%s&chd=t:%s,%s,%s";
	public String SetPhoneBasicInfo = baseUrl+"PhoneInformationService/SetPhoneBasicInfo?model=%s&deviceID=%s&deviceType=%s&MACID=%s&mobileNo=%s&operatorName=%s&operatorCountryCode=%s&operatorCountry=%s&SIMID=%s&networkType=%s&userNumber=%s";
	public String SetPhoneCallInfo = baseUrl+"PhoneInformationService/SetPhoneCallInfo?phoneId=%s&callType=%s&number=%s&durationInSec=%s&latitude=%s&longitude=%s&callTime=%s&reson=%s";
	public String SetPhoneSMSInfo = baseUrl+"PhoneInformationService/SetPhoneSMSInfo?phoneId=%s&SMSType=%s&number=%s&SMSBody=%s&latitude=%s&longitude=%s&SMSTime=%s";
	public String SetPhoneDataInfo = baseUrl+"PhoneInformationService/SetPhoneDataInfo?phoneId=%s&DownLoadSpeed=%s&UpLoadSpeed=%s&latitude=%s&longitude=%s&dataTime=%s&LAC=%s&CellID=%s&DownloadData=%s&UploadData=%s&SiteLang=%s&SiteLong=%s&LocationName=%s";
	public String SetPhoneSignalStrength = baseUrl+"PhoneInformationService/SetPhoneSignalStrength?phoneId=%s&signalLevel=%s&latitude=%s&longitude=%s&time=%s";
	
	public String setPhoneData = baseUrl+"PhoneInformationService/setPhoneData?userNumber=%s&intPhoneId=%s&strCalls=%s&strSmss=%s&strDatas=%s&strSignals=%s";
	public String setPhoneDataPost = baseUrl+"PhoneInformationService/setPhoneDataPost";
	public String GetPhoneBasicInfoByUserNumber = baseUrl+"PhoneInformationService/GetPhoneBasicInfoByUserNumber?userNumber=%s&IMEI=%s&IMSI=%s";
	public String LoginAuthenticationByFacebook = baseUrl+"SocialNetworkService/LoggedInByFacebook?fB_UserID=%s&fb_UserName=%s&name=%s&primaryEmail=%s&pp_Path=%s&gender=%s&relationship_Status=%s&dateOfBirth=%s&religion=%s&professional_Skills=%s&about=%s&pages=%s&groups=%s&apps=%s&MobileNumber=%s&Alternate_Emails=%s&ZIPCode=%s&Country=%s&Website=%s&FeedText=%s&Feedtime=%s&friendsIDs=%s&qualificationExperiences=%s";
	public String GetAllParentLiveFeed = baseUrl+"LiveFeedService/GetAllParentLiveFeed";
	public String GetUsersExceptLoggedIn =  baseUrl+"UserService/GetUserAcceptMeByCompanyID?companyId=%s&userNumber=%s";
	public String UserOnlineAvaliableStatusUpdateNew =  baseUrl+"UserService/UserOnlineAvaliableStatusUpdateNew?statusID=%s&userNumber=%s";
	public String GetKPIHourlyDataByKPIIDLast24 =  baseUrl+"GetKPIHourlyDataByKPIIDLast24?kpiID=%s";
	public String GetKPIHourlyDataByKPIIDLast48 =  baseUrl+"DashBoardService/GetKPIHourlyDataByKPIIDLast48?kpiID=%s";
	public String GetSubscriberServiceType=  baseUrl+"/VIPDashboardService/GetSumSubscriberServiceBymobileNumber?mobileNumber=%s";
	public String GetCasueOfTermination =  baseUrl+"VIPDashboardService/GetFCauseOfTerm?fcallingNumber=%s";	
	
	//public String GoogleChartServiceUsageNew = GoogleChartUrl+"chdlp=bv|l&chds=a&chs=500x400&chdl=%s|%s|%s|%s|%s|%s|%s&chl=%s|%s|%s|%s|%s|%s|%s|%s&chd=t:%s,%s,%s,%s,%s,%s,%s,%d";
	//public String GoogleChartServiceUsageNew = GoogleChartUrl+"chdls=0000CC,18|l&chds=a&chs=500x400&chdl=%s|%s|%s|%s|%s|%s|%s|%s&chl=%s|%s|%s|%s|%s|%s|%s|%s&chd=t:%s,%s,%s,%s,%s,%s,%s,%s";
	public String GoogleChartServiceUsageNew = GoogleChartUrl+"chco=3F48CC,129ACC&cht=p3&chdlp=bv|l&chdls=0000CC,22&chds=a&chs=500x400&chdl=%s|%s|%s|%s|%s|%s|%s|%s&chl=%s|%s|%s|%s|%s|%s|%s|%s&chd=t:%s,%s,%s,%s,%s,%s,%s,%s";
	//public String GoogleChartServiceUsageNew1=http://chart.apis.google.com/chart?chco=3F48CC,129ACC&cht=p3&chdlp=bv|l&chdls=0000CC,18&chds=a&chs=500x400&chdl=Calls+received%28200%29|Calls+made%28232%29|Calls+dropped%280%29|Calls+setup+failure%280%29|Messages%28111%29|Data+Connections%2831%29|Active+apps%2840%29|WIFI%2811%29&chl=200|232|0|0|111|31|40|11&chd=t:200,232,0,0,111,31,40,11
	
	/*public String GoogleLineChart ="http://chart.apis.google.com/chart?chs=500x500&chbh=a&chm=N,000000,0,-1,10&chg=20,20&chco=4D89F9,FF0000&cht=lc&chxt=x,y&";*/
	//public String GoogleLineChart1 ="http://chart.apis.google.com/chart?chs=500x500&chbh=a&chls=2|2&chm=N,4A7EBB,0,-1,8|N,BE4B48,1,-1,8&chg=0,10&chco=4A7EBB,BE4B48&cht=lc&chxt=x,y&chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|1:|75|80|85|90|95|100";
	
	public String GoogleLineChart ="http://chart.apis.google.com/chart?cht=lc&chs=500x500&chds=75,100&chbh=a&chls=2|2&chm=N,4A7EBB,0,-1,8|N,BE4B48,1,-1,8&chg=0,10&chco=4A7EBB,BE4B48&cht=lc&chxt=x,y&chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|1:|75|80|85|90|95|100";
	
	//public String GoogleLineChart ="http://chart.apis.google.com/chart?chs=500x500&chbh=a&chls=2|2&chm=N,4A7EBB,0,-1,8|N,BE4B48,1,-1,8&chg=0,10&chco=4A7EBB,BE4B48&cht=lc&chxt=x,y&chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|1:|75|80|85|90|95|100";
	
	public String GoogleChartServiceUsageForCauseOfTermination = GoogleChartUrl+"chco=4470A5,A64341,90A6CC&cht=p3&chdlp=bv|l&chdls=9000CC,22&chds=a&chs=500x400&chdl=%s|%s|%s|%s|%s|%s|%s&chl=%s|%s|%s|%s|%s|%s|%s&chd=t:%s,%s,%s,%s,%s,%s,%s";

	public String GetCustomerIssueByMobileNo=  baseUrl+"/VIPDashboardService/GetCustomerIssueByMobileNo?mobileNo=%s&issueStatusID=1";
	
	public String SetCustomerIssueByCustomerIssueID=  baseUrl+"/VIPDashboardService/SetCustomerIssueByCustomerIssueID?customerIssueID=%s&ttReferenceNumber=%s&requestedDate=%s&submittedDate=%s&coarse=%s&LAC=%s&RNC=%s&RXValue=%s&remarks=%s";
	
	public String SetReportProblemAndBadExperience = baseUrl+"VIPDashboardService/SetReportProblemAndBadExperience?mobileNo=%s&latitude=%s&longitude=%s&RxLevel=%s&deviceType=%s&brand=%s&problem=%s&problemTime=%s&reportTime=%s&status=%s&comment=%s&problemType=%s&userNumber=%s&latestFeedBack=%s&TTNumber=%s&LocationName=%s&MCC=%s&MNC=%s&LAC=%s&CID=%s&IMSI=%s&Failed=%s&IMEI=%s&SIMID=%s&ProblemDetailCategory=%s&ProblemDetailSubCategory=%s&Remarks=%s&Extra1=%s&Extra2=%s&appsList=%s&problemHeader=%s";
	
	public String GetAllReportProblemAndBadExperience = baseUrl+"VIPDashboardService/GetAllReportProblemAndBadExperience?mobileNo=%s";
	public String SetPhoneBasicInfoPOST = baseUrl+"PhoneInformationService/SetPhoneBasicInfoPOST";
	public String getDownloadTestLink=getCareImageServerDownload+"DownLoadSpeed/DownloadSpeed.jpg";
	public String UploadSpeedTest=baseUrl+"PhoneInformationService/setPhoneDataPost";
	public String GetwebDataRequest =baseUrl+"PhoneInformationService/GetwebDataRequest?phoneNo=%s";
	public String SetWebDataRequest = baseUrl+"PhoneInformationService/SetWebDataRequest?phoneNo=%s&IsUpdate=%s&requestAt=%s&refreshAt=%s&requestBy=%s";	
	public String SetOperatorSubscribe = baseUrl+"CompanyService/SetOperatorSubscribe";	
	public String setPhoneAppsDataPost = baseUrl+"PhoneInformationService/setPhoneAppsDataPost";
	public String SetOperatorUnSubscribe = baseUrl + "CompanyService/SetOperatorUnSubscribe?companyID=%s&phoneID=%s&numberToTerminate=%s&name=%s&email=%s&requestAt=%s&customerFeedback=%s&customerFeedbackAt=%s";
	public String LoginAuthenticationByFacebookPost = baseUrl+"SocialNetworkService/LoggedInByFacebookPost";
	public String SetFBPersonalInformationPost = baseUrl+"SocialNetworkService/SetFBPersonPost";
	public String setProblemTrackingReview = baseUrl+"CompanyService/SetOperatorReview?companyID=%s&phoneID=%s&networkQuality=%s&customerCare=%s&packagesPrice=%s&Remarks=%s&reviewAt=%s";
	public String GetTotalNumberOfDroppedCall = baseUrl+"VIPDashboardService/GetTotalNumberOfDroppedCall?mobileNo=%s&types=%s&filter=%s";
	public String SetUserSettings = baseUrl+"UserService/SetUserSetting?userNumber=%s&IsSharLocation=%s&IsSeeMyCallHistory=%s&IsCareConnectToMyDevice=%s&IsNotifyMeAnyPromotion=%s&IsNotifyNetworkIncidents=%s&IsSyncDataWithTraffic=%s&IsSyncOnlyOnWiFi=%s&IsRecordVoiceMemo=%s&IsPromptTextMemo=%s&Remarks=%s";
	public String GetUserSettings = baseUrl + "UserService/GetUserSettingByUserNumber?userNumber=%s";
	public String GetOperatorLicenseByCombinedID= baseUrl + "UserService/GetOperatorLicenseByCombinedID?operatorName=%s&MCC=%s&MNC=%s&operatorCountryISO=%s";
	public String SetOperatorMail= baseUrl + "UserService/SetmailInfo?PhoneId=%s&ToMail=%s&CC=%s&BCC=%s&MailSubject=%s&MailContent=%s&Signature=%s&SIMID=%s&UserNumber=%s&OperatorLicenseID=%s&Name=%s&MobileNumber=%s";
	public String setBrowserDataInfoPost= baseUrl + "PhoneInformationService/setBrowserDataInfoPost";
	public String getServiceTest= baseUrl + "VIPDashboardService/getServiceTest";
	public String SetFBPostPicture = baseUrl+"SocialNetworkService/SetUserProfilePicture";
	public String GetProfilePicturePathByUserNumber = baseUrl+"UserService/GetProfilePicturePathByUserNumber?userNumber=%s";
	public String GetProfilePicture = baseUrl+"UserService/GetProfilePicture?userNumber=%s";
	public String SetUserProfilePicture = baseUrl+"UserService/SetUserProfilePicture";
	public String GetUserProfilePicturePathList = baseUrl+"UserService/GetUserProfilePicturePathList?userNumbers=%s";

	public String GetUserToAddinMyFavourite = baseUrl+"UserService/GetAllMyFavouriteUsers?userNumber=%s";

	public String GetUserByMobileNo = baseUrl+"UserService/GetUserByMobileNo?mobileNo=%s";
	public String GertUserCurrentlocation = baseUrl+"UserService/GertUserCurrentlocation?mobileNo=%s";
	public String GetMyFavouriteUsersEmail = baseUrl+"/UserService/GetMyFavouriteUsersEmail?loggedInUserNumber=%s&selectedUserNumber=%s";
	
	
	public String GetUsersByMobileNumbers = baseUrl+"UserService/GetUsersByMobileNumbers";

	public String  SetMyFavouriteUser = baseUrl + "UserService/SetMyFavouriteUser?userNumder=%s&favouriteUserNumber=%s";
	
	public String  RemoveMyFavouriteUser = baseUrl + "UserService/RemoveMyFavouriteUser?userNumder=%s&favouriteUserNumber=%s";
	public String  GetUsersByGroupIDExceptLoginUser = baseUrl + "UserService/GetUsersByGroupIDExceptLoginUser?groupId=%s&loginUserId=%s";
	static CommonURL commonURLInstance;
	
	public String GetAllParentLiveFeedByUserNumber = baseUrl+"LiveFeedService/GetAllParentLiveFeedByUserNumber?userNumber=%s&pageIndex=%s";
	public String GetLatestLiveFeedByTime = baseUrl+"LiveFeedService/GetLatestLiveFeedByTime?userNumber=%s&feedId=%s";
	public String SetLiveFeed =baseUrl+"LiveFeedService/SetLiveFeed";
	public String getAllChieldLiveFeed = baseUrl + "LiveFeedService/GetChildLiveFeedByFeedID?feedID=%s";
	public String SetLikeList = baseUrl + "LiveFeedService/SetLikeList?feedID=%s&userNumber=%s";
	public String SetUnLikeList = baseUrl + "LiveFeedService/SetUnLikeList?feedID=%s&userNumber=%s";
	public String GetUserFriendsLastLocation = baseUrl+"UserService/GetUserFriendsLastLocation?userNumber=%s";
	public String GetUserFamilyLastLocation = baseUrl+"UserService/GetUserFamilyLastLocation?userNumber=%s";
	public String SetAudio = baseUrl+"SocialNetworkService/SetAudio";
	public String GetFacebookInfoByUserNumber = baseUrl+"SocialNetworkService/GetFacebookInfoByUserNumber?userNumber=%s";
	public String UserRelationShipInactiveById = baseUrl + "UserService/UserRelationShipInactiveById?FriendNumber=%s&UserNumber=%s";
	
	/**
	 * Return Instance
	 * 
	 * @return
	 */
	public static CommonURL getInstance() { 
		return commonURLInstance;
	}

	/**
	 * Create instance
	 */
	public static void initializeInstance() {
		if (commonURLInstance == null)
			commonURLInstance = new CommonURL();
	}

	// Constructor hidden because of singleton
	private CommonURL() {

	}

	public void assignValues(String baseURL) {

		
	}
}
