package com.leadership.app.utils;

import com.androidquery.callback.ImageOptions;
import com.leadership.app.entities.City;
import com.leadership.app.entities.CompanySetup;
import com.leadership.app.entities.UserInfo;



/**
 * Singleton class
 * Use for initializing some common values used in application
 */

public class CommonValues {	
	
	public boolean IsServerConnectionError = false;	
	static CommonValues commonValuesInstance;
	public int ExceptionCode = CommonConstraints.NO_EXCEPTION;
	
	public int CompanyId=1;
	public CompanySetup SelectedCompany = null;
	public City SelectedCity = null;
	public String SelectedGraphItem = null;
	public ImageOptions defaultImageOptions;
	
	public UserInfo LoginUser;

	
	public static CommonValues getInstance() {		
		return commonValuesInstance;
	}

	
	public static void initializeInstance() {
		if (commonValuesInstance == null)
			commonValuesInstance = new CommonValues();
	}
	// Constructor hidden because of singleton
	private CommonValues(){
		
	}
}
