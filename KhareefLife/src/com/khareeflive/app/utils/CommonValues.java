package com.khareeflive.app.utils;

import com.khareeflive.app.entities.LoginAuthentication;
import com.khareeflive.app.entities.UserInformation;


/**
 * Singleton class
 * Use for initializing some common values used in application
 */

public class CommonValues {	
	
	public boolean IsServerConnectionError = false;	
	
	static CommonValues commonValuesInstance;
	
	public UserInformation LoginUser;
	public LoginAuthentication selectedUser;
	public String selectedGroupName;
	public String lastWarMessageTime;
	
	
	public int ExceptionCode = CommonConstraints.NO_EXCEPTION;

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
