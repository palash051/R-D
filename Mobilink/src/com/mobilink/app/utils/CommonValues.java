package com.mobilink.app.utils;

import com.androidquery.callback.ImageOptions;
import com.mobilink.app.entities.City;
import com.mobilink.app.entities.CompanySetup;



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
