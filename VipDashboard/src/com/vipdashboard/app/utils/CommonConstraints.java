package com.vipdashboard.app.utils;

import android.graphics.Bitmap;
import android.widget.ListView;

public class CommonConstraints {
	public static final int NO_EXCEPTION = -1;
	public static final int GENERAL_EXCEPTION = 0;
	public static final int CLIENT_PROTOCOL_EXCEPTION=1;
	public static final int ILLEGAL_STATE_EXCEPTION=2;
	public static final int UNSUPPORTED_ENCODING_EXCEPTION=3;
	public static final int IO_EXCEPTION=4;	
	public final static String EncodingCode = "UTF8";
	
	//Use for http Header content-type
	public final static String CONTENT_TYPE_CSV = "text/csv";
	public final static String CONTENT_TYPE_APPLICATION_XML = "application/xml";
	public final static String CONTENT_TYPE_TEXT_XML = "text/xml";
	public final static String LOGIN_USERPASS_SHAREDPREF_KEY = "login_user_pref";
	public final static String CALL_NOC = "call_to_noc";
	public final static String CALL_MANAGER = "call_to_manager";
	public final static String OAUTH_PREF = "LIKEDIN_OAUTH";
	public final static String LINKEEDIN_API_KEY = "753ngn83uzsj0i";
	public final static String LINKEDIN_SECRET_KEY = "ZMMhvssyjlFrhR1Z";
	public final static String LINKEDIN_USER_TOKEN = "305290cc-8e7d-4927-b9d0-c2e35bd95a87";
	public final static String LINKEDIN_USER_SECRET = "135b23b6-02fe-4014-97ba-5a3c98506f7c";
	
	public static final int INFO_TYPE_ALL = 0;
	public static final int INFO_TYPE_LASTHOUR = 1;
	public static final int INFO_TYPE_TODAY = 2;	
	public static final int INFO_TYPE_YESTERDAY = 3;
	public static final int INFO_TYPE_WEEK = 4;
	public static final int INFO_TYPE_MONTH = 5;
	
	public static final int THIS_HOUR = 1;
	public static final int TODAY = 2;
	public static final int YESTERDAY = 3;
	public static final int THIS_WEEK = 4;
	
	
	//User Online Availability Status
	   public final static int ONLINE = 1;
       public final static int AWAY = 2;
       public final static int DO_NOT_DISTURB = 3;
       public final static int INVISIBLE = 4;
       public final static int OFFLINE = 5;
       public final static int PHONEOFF = 6;
       public final static int BUSY=7;

       public final static int AVAILABLE = 8;
       public final static int AT_OFFICE = 9;
       public final static int IN_A_MEETING = 10;
       public final static int SLEEPING = 11;
       public final static int EMERGENCY_CALL_ONLY = 12;
       public final static int CARE_ONLY = 13;
       public final static int LOW_BATTERY = 14;

       
       public static int ISPRESSED_INCLUCE_VOICE_CALL = 0;

     
       
    //Current User Collaboration Status
       public final static int USER_MESSAGE_COUNT = 20;
       
       public static Bitmap ProfilePicture = null;
       
       public static boolean isCheckFbFriendsList = false;
       
       public static ListView fb_friendsList=null;
       
       
       
       public static boolean IsRecordVoiceMemo = false;
       public static boolean IsPromotTextMemo = true;
       
       public static boolean IS_BD_LOW_POWER_NOTIFIATION = true;
       public static String BD_LOW_POWER_NOTIFIATION_PERCENTAGE = "20%";//20%
       
       public static boolean IS_BD_LOW_POWER_SWITCH_NOTIFIATION = false;
       public static String BD_LOW_POWER_SWITCH_NOTIFIATION_PERCENTAGE = "20%";
       
       public static boolean IS_BD_MORE_OPTION_SCREEN_SAVING = false;
       public static boolean IS_BD_MORE_OPTION_DISABLE_WIFI = false;
       
       public static boolean IS_ALREADY_CALLED_POPUP = false;
       
       public static boolean IS_SCREEN_OFF_SAVING = false;
       public static boolean IS_DISABLE_WIFI = false;
       public static boolean IS_ALREADY_CALLED_SCREEN_OFF_SAVING = false;
       public static boolean IS_ALREADY_CALLED_DISABLE_WIFI = false;
       
     //User Status
	   public final static int WAITING_FOR_APPROVAL = 1;
       public final static int ACTIVE = 2;
       public final static int SUSPENDED = 3;
       public final static int DEACTIVED = 4;
       //Baterry Doctor
       public static long DEVICE_OPTIMIZED ;
       public static double  BATTERY_ESTIMATED;
       public static int LAST_BATTERY_SCALE; 
       
}
