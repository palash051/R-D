package com.shopper.app.utils;

import android.content.Intent;
import com.actionbarsherlock.view.Menu;
import com.shopper.app.entities.UserInformation;
import com.shopper.app.enums.CameraMessageStatus;

/**
 * Singleton Class
 * use for initializing some common values used in application
 * @author 
 * 
 */

public class CommonValues {
	
	public CameraMessageStatus CameraMessage;
	public boolean IsServerConnectionError = false;
	public boolean IsAnyNewBasketItemAdded = false;	
	public boolean IsArticleDetailsRecordFound = false;
	public boolean IsBasketUpdateInProgress = false;
	public UserInformation loginuser = new UserInformation();
	public boolean IsCallFromBasket = false;
	public Menu menuList = null;
	public Intent homeIntent = null;
	public static boolean beforeJB=false; //before Jelly Bean
	//public Typeface applicationFont;
	
	public int ErrorCode = CommonConstraints.NO_EXCEPTION;

	static CommonValues commonValuesInstance;

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
		if (commonValuesInstance == null) {
			commonValuesInstance = new CommonValues();
			checkApiVersion();
		}
	}
	// Constructor hidden because of singleton
	private CommonValues(){
		
	}
	private static void checkApiVersion(){
//		int sdk = android.os.Build.VERSION.SDK_INT;
//		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//			beforeJB=true;
//		} else {
//			beforeJB=false;
//		}
	}
	

}
