package com.vipdashboard.app.base;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.LoginActivity;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.volley.LruBitmapCache;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

public class MyNetApplication extends Application{
	private static boolean _isApplicationAlive;
	private UncaughtExceptionHandler defaultUEH;
	
	// for Volley
	public static final String TAG = MyNetApplication.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private com.android.volley.toolbox.ImageLoader mImageLoader;
	LruBitmapCache mLruBitmapCache;

	private static MyNetApplication mInstance;
	public static synchronized MyNetApplication getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public com.android.volley.toolbox.ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			getLruBitmapCache();
			mImageLoader = new com.android.volley.toolbox.ImageLoader(this.mRequestQueue, mLruBitmapCache);
		}

		return this.mImageLoader;
	}

	public LruBitmapCache getLruBitmapCache() {
		if (mLruBitmapCache == null)
			mLruBitmapCache = new LruBitmapCache();
		return this.mLruBitmapCache;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	
	public MyNetApplication() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        // setup handler for uncaught exception 
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();		
		initializeCommonInstance();	
		
		CommonValues.getInstance().TotalDownloadData	= TrafficStats.getTotalRxBytes();
		CommonValues.getInstance().TotalUploadData	= TrafficStats.getTotalTxBytes();
		
		setUpDefaultImageOptions();
		
		//CommonValues.getInstance().database=new MyNetDatabase(this.getApplicationContext());
		
		initImageLoader(getApplicationContext());
		
		CommonValues.getInstance().imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.mynet)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.delayBeforeLoading(1000)
				.resetViewBeforeLoading(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.considerExifParams(true)
				
				.displayer(new FadeInBitmapDisplayer(300))
				.build();	
		
		configureXMPP();
		mInstance = this;
	}
	
	
	@Override
	public void onTerminate() {
		
		super.onTerminate();
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
		CommonValues.getInstance().defaultImageOptions.fallback=R.drawable.user_icon;
		CommonValues.getInstance().defaultImageOptions.preset=null;
		CommonValues.getInstance().defaultImageOptions.animation=0;	
		CommonValues.getInstance().defaultImageOptions.ratio=AQuery.RATIO_PRESERVE;
		CommonValues.getInstance().defaultImageOptions.round = 0;
	}
	
	private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
	        new Thread.UncaughtExceptionHandler() {
	            @Override
	            public void uncaughtException(Thread thread, Throwable ex) {
	            	writeApplicationException("Time:" +(new Date()).toLocaleString()+"\r\n"+ ex.getMessage()+"\r\n");
	            	Intent intent = new Intent(MyNetApplication.this,
	    					LoginActivity.class);
	    			PendingIntent pIntent = PendingIntent.getActivity(
	    					MyNetApplication.this, 0, intent, 0);
	    			
	            	Notification mNotification = new Notification.Builder( getApplicationContext())
					.setContentTitle("Application Error")
					.setContentText("Due to some problem application stoped. please reopen again.")
					.setNumber(1)
					.setSmallIcon(R.drawable.mynet).setContentIntent(pIntent)
					.build();

	            	mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
	            	mNotification.defaults = Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE
					| Notification.DEFAULT_LIGHTS;
			            	mNotification.ledARGB = Color.BLUE;
					mNotification.ledOnMS = 500;
					mNotification.ledOffMS = 500;
					mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
					android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.notify(2, mNotification);
					//android.os.Process.killProcess(android.os.Process.myPid());
					intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					Runtime.runFinalizersOnExit(true);
	            	System.exit(0);	            	
	                defaultUEH.uncaughtException(thread, ex);
	            }
	        };
	        
    private void writeApplicationException(String data) {
    	try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "CareLog");
            if (!root.exists()) 
            {
                root.mkdirs();
            }

            File gpxfile = new File(root, "ErrorLog.txt");             
            
            BufferedWriter bW;

            bW = new BufferedWriter(new FileWriter(gpxfile,true));
            bW.write(data);
            bW.newLine();
            bW.flush();
            bW.close();
            

        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
    }
    
    private  void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
    public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
    
    public void configureXMPP() {		
		ProviderManager pm=ProviderManager.getInstance();
		SmackAndroid.init(this);
	    // Private Data Storage
	    pm.addIQProvider("query", "jabber:iq:private",
	            new PrivateDataManager.PrivateDataIQProvider());

	    // Time
	    try {
	        pm.addIQProvider("query", "jabber:iq:time",
	                    Class.forName("org.jivesoftware.smackx.packet.Time"));
	    } catch (ClassNotFoundException e) {
	        Log.w("TestClient",
	                "Can't load class for org.jivesoftware.smackx.packet.Time");
	    }

	    // Roster Exchange
	    pm.addExtensionProvider("x", "jabber:x:roster",
	            new RosterExchangeProvider());

	    // Message Events
	    pm.addExtensionProvider("x", "jabber:x:event",
	            new MessageEventProvider());

	    // Chat State
	    pm.addExtensionProvider("active",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("composing",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("paused",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("inactive",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("gone",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());

	    // XHTML
	    pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
	            new XHTMLExtensionProvider());

	    // Group Chat Invitations
	    pm.addExtensionProvider("x", "jabber:x:conference",
	            new GroupChatInvitation.Provider());

	    // Service Discovery # Items
	    pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
	            new DiscoverItemsProvider());

	    // Service Discovery # Info
	    pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
	            new DiscoverInfoProvider());

	    // Data Forms
	    pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

	    // MUC User
	    pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
	            new MUCUserProvider());

	    // MUC Admin
	    pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
	            new MUCAdminProvider());

	    // MUC Owner
	    pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
	            new MUCOwnerProvider());

	    // Delayed Delivery
	    pm.addExtensionProvider("x", "jabber:x:delay",
	            new DelayInformationProvider());

	    // Version
	    try {
	        pm.addIQProvider("query", "jabber:iq:version",
	                Class.forName("org.jivesoftware.smackx.packet.Version"));
	    } catch (ClassNotFoundException e) {
	        // Not sure what's happening here.
	    }

	    // VCard
	    pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

	    // Offline Message Requests
	    pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
	            new OfflineMessageRequest.Provider());

	    // Offline Message Indicator
	    pm.addExtensionProvider("offline",
	            "http://jabber.org/protocol/offline",
	            new OfflineMessageInfo.Provider());

	    // Last Activity
	    pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

	    // User Search
	    pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

	    // SharedGroupsInfo
	    pm.addIQProvider("sharedgroup",
	            "http://www.jivesoftware.org/protocol/sharedgroup",
	            new SharedGroupsInfo.Provider());

	    // JEP-33: Extended Stanza Addressing
	    pm.addExtensionProvider("addresses",
	            "http://jabber.org/protocol/address",
	            new MultipleAddressesProvider());

	    // FileTransfer
	    pm.addIQProvider("si", "http://jabber.org/protocol/si",
	            new StreamInitiationProvider());

	    pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
	            new BytestreamsProvider());

	    // Privacy
	    pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
	    pm.addIQProvider("command", "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider());
	    pm.addExtensionProvider("malformed-action",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.MalformedActionError());
	    pm.addExtensionProvider("bad-locale",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.BadLocaleError());
	    pm.addExtensionProvider("bad-payload",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.BadPayloadError());
	    pm.addExtensionProvider("bad-sessionid",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.BadSessionIDError());
	    pm.addExtensionProvider("session-expired",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.SessionExpiredError());
	    pm.addIQProvider("open", "http://jabber.org/protocol/ibb", new OpenIQProvider());
	    pm.addIQProvider("data", "http://jabber.org/protocol/ibb", new DataPacketProvider());
	    pm.addIQProvider("close", "http://jabber.org/protocol/ibb", new CloseIQProvider());
	    //connect();
	}
    
    private void connect() {
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create a connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(
						CommonURL.getInstance().CareIMHost, CommonURL.getInstance().CareIMPort, CommonURL.getInstance().CareIMService);
				XMPPConnection conn =new XMPPConnection(connConfig) ;
				
				try {
					conn.connect();
				} catch (XMPPException ex) {					
					CommonValues.getInstance().XmppConnection=	null;
				} 			
				
			}
		});
		t.start();		
	}
}
