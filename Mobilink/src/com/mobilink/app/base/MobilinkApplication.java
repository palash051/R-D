package com.mobilink.app.base;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;
import com.mobilink.app.R;
import com.mobilink.app.utils.CommonURL;
import com.mobilink.app.utils.CommonValues;
import android.app.Application;

public class MobilinkApplication extends Application{
	private static boolean _isApplicationAlive;
	@Override
	public void onCreate() {		
		super.onCreate();		
		initializeCommonInstance();	
		
        setUpDefaultImageOptions();
		
	}
	private void initializeCommonInstance() {
		CommonURL.initializeInstance();
		CommonValues.initializeInstance();
	}
	
	public static boolean isApplicationAlive() {
		return _isApplicationAlive;
	}

	public static void activityResumed() {
		_isApplicationAlive = true;
	}

	public static void activityPaused() {
		_isApplicationAlive = false;
	}
	
	@Override
    public void onLowMemory(){  

        //clear all memory cached images when system is in low memory
        //note that you can configure the max image cache count, see CONFIGURATION
        BitmapAjaxCallback.clearCache();
    }
	
	private void setUpDefaultImageOptions(){
		//set the max number of concurrent network connections, default is 4
        AjaxCallback.setNetworkLimit(8);

        //set the max number of icons (image width <= 50) to be cached in memory, default is 20
        BitmapAjaxCallback.setIconCacheLimit(20);

        //set the max number of images (image width > 50) to be cached in memory, default is 20
        BitmapAjaxCallback.setCacheLimit(40);

        //set the max size of an image to be cached in memory, default is 1600 pixels (ie. 400x400)
       // BitmapAjaxCallback.setPixelLimit(AQuery.RATIO_PRESERVE);
        
        //set the max size of the memory cache, default is 1M pixels (4MB)
        BitmapAjaxCallback.setMaxPixelLimit(2000000);
        
		CommonValues.getInstance().defaultImageOptions = new ImageOptions();		
		CommonValues.getInstance().defaultImageOptions.memCache=false;
		CommonValues.getInstance().defaultImageOptions.fileCache=false;
		CommonValues.getInstance().defaultImageOptions.targetWidth=0;
		CommonValues.getInstance().defaultImageOptions.fallback=R.drawable.mobilink;
		CommonValues.getInstance().defaultImageOptions.preset=null;
		CommonValues.getInstance().defaultImageOptions.animation=0;	
		CommonValues.getInstance().defaultImageOptions.ratio=AQuery.RATIO_PRESERVE;
		CommonValues.getInstance().defaultImageOptions.round = 0;
	}
	
	

}
