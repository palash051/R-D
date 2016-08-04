package com.vipdashboard.app.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.androidquery.callback.ImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.Country;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.User;



/**
 * Singleton class
 * Use for initializing some common values used in application
 */

public class CommonValues {	
	
	public boolean IsServerConnectionError = false;	
	static CommonValues commonValuesInstance;
	public String selectedGroupName;
	public Long notificationMessageTime;
	public String LastCollaborationTime;
	public Boolean isCallFromTTUpdateSet;
	public Boolean isCallFromTTStatusSet;
	public Long LastAlarmTime;
	public Long CollaborationMessageTime=0l;
	public int ExceptionCode = CommonConstraints.NO_EXCEPTION;
	public String FB_Profile_Picture_Path = "";
	//configuration mode for your mode
	public int brightnessvalue = 255;
	public int BatteryDoctorSavedMin = 0;
	public int timeOut = 1800000;
	//configuration mode for supersaving mode
	public float brightnessvalue_supersaving = 38.25f;
	public int timeOut_supersaving = 15000;
	
	public static int totalDataConnectionInConnectedMode=0,totalDataConnectionInDisconnectedMode=0;
	
	public int CompanyId=1;
	
	public User LoginUser;
	
	public Boolean isCallFromNotification=false;
	
	public Boolean isCallFromLoginActivity = false;
	
	public Boolean isIssueSubmitted = false;
	public Country SelectedCountry;
	
	public Long TotalDownloadData;
	public Long TotalUploadData;
	
	public TrafficSnapshot LastTrafficSnapshot; 
	
	public ImageOptions defaultImageOptions;
	public XMPPConnection XmppConnection;
	public String XmppUserPassword="Conio1234";
	public String XmppReceiveMessageIntent="XmppReceiveMessageIntent";
	public HashMap<Integer, Group>XmppConnectedGroup;
	
	
	public MyNetDatabase database;
	
	public static String ENGINEERING_MODE="EngineeringMode";
	public static String IS_DATA_TEST="IsDatatest";
	public static String IS_URL_TEST="IsURLTest";
	public static String IS_STREAMING_TEST="IsStreamingTest";
	public static String IS_GPS="IsGPS";
	public static String IS_NFC="IsNFC";
	public static String CONVERAGE_SECONDS="ConverageSeconds";
	public static String SERVICE_TEST_SECONDS="ServicetestSeconds";
	public static String SERVER_DRY_CONNECTIVITY_MESSAGE ="Care Server not responding, please try after sometimes.";
	
	public static String SELECTED_SWITCH_TO ="Your Mode";

	
	public static String APPLICATION_PACKAGE_NAME="com.vipdashboard.app";
	
	
	public static int COMMON_HOVER_PICTURE_HEIGHT_WIDTH=66;
	public static int COMMON_PICTURE_HEIGHT_WIDTH=64;
	
	public static int COMMON_TEXT_SIZE = 12;
	public static int COMMON_HOVER_TEXT_SIZE = 14;
	
	//public static double BATTERY_DOCTOR_THREE_POINT_SIX_DEFAULT = .5238095238;
	public static double BATTERY_DOCTOR_DEFAULT = 1.69028703;
	public static double BATTERY_DOCTOR_UPPER_THAN_TWO_ONE_ZERO_ZERO= 0.62681159;
	public static double BATTERY_DOCTOR_ONE_ZERO_ZERO_ZERO= 1.954444;
	public static double BATTERY_DOCTOR_TWO_ONE_ZERO_ZERO= 1.14975042;
	public static double BATTERY_DOCTOR_IR= 0.952222222;//.522222222;// 14.28333333333333;
	
	public static Boolean IsOpeatorLicensed = false;
	public static String OperatorHelpDeskMobileNo = "Not Available";
	public static String OperatorHelpDeskEmail = "Not Available";
	public static int OpeatorLicensedID = 0;
	
	public Boolean IsChatingContinue = false;
	
	public DisplayImageOptions imageOptions =null;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	
	public HashMap<Integer, ContactUser>ConatactUserList;

	/**
	 * Return Instance
	 * @return
	 */
	public static CommonValues getInstance() {		
		return commonValuesInstance;
	}

	/**
	 * Create instance 
	 */
	public static void initializeInstance() {
		if (commonValuesInstance == null)
			commonValuesInstance = new CommonValues();
	}
	// Constructor hidden because of singleton
	private CommonValues(){
		
	}
}
