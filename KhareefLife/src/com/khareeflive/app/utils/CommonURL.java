package com.khareeflive.app.utils;

/**
 * Singleton Class Use for initializing common URL values
 */

public class CommonURL {	
	public static String GetLoginAthentication="http://188.75.104.141/ENOC/Service.asmx/LoginAuthentication?struserID=%s&strPassword=%s&";
	public static String GetLatestNews="http://188.75.104.141/ENOC/Service.asmx/GetLatestNews";
	public static String GetUserList="http://188.75.104.141/ENOC/Service.asmx/UserList";
	public static String GetMessage="http://188.75.104.141/ENOC/Service.asmx/GetUnreadMessage?from=%s&to=%s&";
	public static String SendMessage="http://188.75.104.141/ENOC/Service.asmx/LoadChat?from=%s&to=%s&Message=%s&";
	public static String GoogleMap="http://188.75.104.141/ENOC/Service.asmx/SiteDownGoogleMpas";
	public static String Get2GUpdate="http://188.75.104.141/ENOC/Service.asmx/Get2GUpdate";
	public static String Get3GUpdate="http://188.75.104.141/ENOC/Service.asmx/Get3GUpdate";
	public static String GetLTEUpdate="http://188.75.104.141/ENOC/Service.asmx/GetLTEUpdate";
	public static String GetLatestNOCRoster="http://188.75.104.141/ENOC/Service.asmx/GetLatestNOCRoster";
	public static String GetCSSR="http://188.75.104.141/ENOC/Service.asmx/GetCSSR";
	public static String GetTraffic="http://188.75.104.141/ENOC/Service.asmx/GetTraffic";
	public static String GetDROPData="http://188.75.104.141/ENOC/Service.asmx/GetDROP";
	public static String GetDataVolume="http://188.75.104.141/ENOC/Service.asmx/GetDataVolume";
	public static String GetChatRoom="http://188.75.104.141/ENOC/Service.asmx/GetChatRoom";
	public static String GetRoomWiseChat="http://188.75.104.141/ENOC/Service.asmx/GetRoomWiseChat?room=%s&";
	public static String LoadRoomChat="http://188.75.104.141/ENOC/Service.asmx/LoadRoomChat?Room=%s&from=%s&Message=%s&";
	public static String GetChatRoomUserID="http://188.75.104.141/ENOC/Service.asmx/GetChatRoomUserID?room=%s&";
	public static String Offerurl="http://www.omantel.om/default.aspx";
	public static String Graphurl="http://188.75.104.141/khareefliveweb/LatestPerformanceReportMobile.aspx";
	public static String GetLatestUnreadNews="http://188.75.104.141/ENOC/Service.asmx/GetLatestUnreadNews?strDateTime=%s&";
	public static String GetRoomWiseUnreadChat="http://188.75.104.141/ENOC/Service.asmx/GetRoomWiseUnreadChat?strDateTime=%s&";
	
		
			
	

	static CommonURL commonURLInstance;

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
