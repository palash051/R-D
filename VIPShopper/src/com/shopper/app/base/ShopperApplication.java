package com.shopper.app.base;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;


import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;

import android.app.Application;

/**
 * Automatically call at the application startup
 * Add this class reference as application class at AndroidManifest.xml file 
 * Basically this class use for initialize the global use of classes and variable    
 * @author jib
 *
 */
@ReportsCrashes(formKey = "", mailTo = "mrb@bordingvista.com", mode = ReportingInteractionMode.SILENT)
public class ShopperApplication extends Application{

	@Override
	public void onCreate() {		
		super.onCreate();
		// Create instances so that we can use whole over the application
		initializeCommonInstance();	
		// The following line triggers the initialization of ACRA
        ACRA.init(this);
	}

	// Initialize Instances
	private void initializeCommonInstance() {
		CommonValues.initializeInstance();
		CommonURL.initializeInstance();
		CommonBasketValues.initializeInstance();	
	}
	

}
