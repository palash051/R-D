package com.androidexample.gcm;

public interface Config {

	
	// CONSTANTS
	static final String YOUR_SERVER_URL =  "YOUR_SERVER_URL/gcm_server_files/register.php";
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
    static final String GOOGLE_SENDER_ID = "9432966778899";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";
		
	
}
