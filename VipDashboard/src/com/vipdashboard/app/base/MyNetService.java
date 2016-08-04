package com.vipdashboard.app.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CollaborationDiscussionActivity;
import com.vipdashboard.app.activities.NotificationActivity;
import com.vipdashboard.app.asynchronoustask.DownloadPhoneinfoAsyncTask;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.asynchronoustask.PhoneAppsDataAsyncTask;
import com.vipdashboard.app.asynchronoustask.ProblemTrackingIntegrationAsyncTask;
import com.vipdashboard.app.asynchronoustask.SpeedTestAsyncTask;
import com.vipdashboard.app.asynchronoustask.UserGroupSyncAsyncTask;
import com.vipdashboard.app.classes.ConsumerMessageListener;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.WebDataRequestEntityHolder;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IDownloadPhoneinfoAsyncTask;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.manager.NotificationManager;
import com.vipdashboard.app.manager.PhoneInformationManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.TrafficSnapshot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class MyNetService extends Service implements IAsynchronousTask,
		LocationListener, IDownloadPhoneinfoAsyncTask {

	private static Runnable recieveMessageRunnable, phoneStatusRunnable,
			phoneLocationRunnable, phoneDataSpeedRunnable,
			dataIntegrationRunnable, appsDataIntegrationRunnable,
			problemIntegrationRunnable, userGroupSyncRunnable;
	private Handler recieveMessageHandler, phoneStatusHandler,
			phoneLocationHandler, phoneDataSpeedHandler, userGroupSyncHandler,
			dataIntegrationHandler, appsDataIntegrationHandler,
			problemIntegrationHandler;
	DownloadableAsyncTask downloadableTask;
	DownloadPhoneinfoAsyncTask downloadPhoneinfoAsyncTask;

	Intent intent;
	PendingIntent pendingIntent;
	int notificationCount = 0, alarmCount = 0, collaborationCount = 0,
			intServiceState = 0;
	public static int phoneState = 0, phoneCallId = 0, lastSmsId = 0;

	long previousTime = 0, priviousDownloadData = 0, currentDownloadData = 0,
			priviousUploadData = 0, currentUploadData = 0,
			lastPhoneInfoSyncTime = 0;

	public static PhoneBasicInformation phoneBasicInformation;

	public static android.location.Location currentLocation;
	public static String currentLocationName = "";
	public static String currentCountryName = "";
	public static int currentSignalStrenght = 31, previousSignal = 0;
	public static int currentDownloadSpeedInKbPS = 0,
			currentUploadSpeedInKbPS = 0;
	private static long currentSignalTime = 0, previousSignalTime = 0;
	CallInformationReceiver receiver = null;
	BatteryInfomrationReceiver batteryReceiver = null;

	public static int phoneId = 0;

	boolean isGetPhoneInfo = false, isDowloadRunning = false,
			isLocationUpdating = false, isDataInfoUpdating = false,
			isPhoneInfoUpdating = false;

	boolean isFirstCall = true;
	public static String LastphoneState = "";

	ArrayList<PhoneCallInformation> callList = null;
	ArrayList<PhoneSMSInformation> smsList = null;
	PhoneDataInformation dataInfo = null;
	ArrayList<PhoneSignalStrenght> signalInfo = null;

	public static ArrayList<PhoneCallInformation> callLogList = null;
	public static ArrayList<PhoneSMSInformation> smsInboxList = null;

	SharedPreferences sharedPreferences = null;

	boolean IsEngineeringMode, IsDatatest, IsURLTest, IsStreamingTest, IsGPS,
			IsNFC;

	String ConverageSeconds, ServicetestSeconds;

	private double phoneStatusInterval = 1, phoneDataStatusInverval = 30;

	public MyNetService() {

	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		recieveMessageHandler = new Handler();
		phoneStatusHandler = new Handler();
		phoneLocationHandler = new Handler();
		phoneDataSpeedHandler = new Handler();
		dataIntegrationHandler = new Handler();
		appsDataIntegrationHandler = new Handler();
		problemIntegrationHandler = new Handler();
		userGroupSyncHandler = new Handler();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, -365);
		lastPhoneInfoSyncTime = cal.getTimeInMillis();
	}

	private void updateCurrentLocation() {
		isLocationUpdating = true;
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		currentLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		isLocationUpdating = false;

		currentLocationName = "";
		currentCountryName="";
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());

		try {
			List<Address> addresses = geocoder.getFromLocation(
					currentLocation.getLatitude(),
					currentLocation.getLongitude(), 1);
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				for (int lineIndex = 0; lineIndex < address
						.getMaxAddressLineIndex(); lineIndex++) {
					currentLocationName = currentLocationName
							+ address.getAddressLine(lineIndex) + ", ";
				}
				currentLocationName = currentLocationName
						+ address.getLocality() + ", "
						+ address.getCountryName();
				
				currentCountryName=address.getCountryName();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
	}

	public void updateSharedPreference() {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(MyNetService.this);

		IsEngineeringMode = sharedPreferences.getBoolean(
				CommonValues.ENGINEERING_MODE, false);
		IsDatatest = sharedPreferences.getBoolean(CommonValues.IS_DATA_TEST,
				true);
		IsURLTest = sharedPreferences
				.getBoolean(CommonValues.IS_URL_TEST, true);
		IsStreamingTest = sharedPreferences.getBoolean(
				CommonValues.IS_STREAMING_TEST, true);
		IsGPS = sharedPreferences.getBoolean(CommonValues.IS_GPS, true);
		IsNFC = sharedPreferences.getBoolean(CommonValues.IS_NFC, false);

		ConverageSeconds = sharedPreferences.getString(
				CommonValues.CONVERAGE_SECONDS, "30");
		ServicetestSeconds = sharedPreferences.getString(
				CommonValues.SERVICE_TEST_SECONDS, "300");
		if (IsEngineeringMode) {
			phoneStatusInterval = Double.parseDouble(ConverageSeconds) / 60;
		}
		if (IsEngineeringMode == true && IsDatatest == true) {
			phoneDataStatusInverval = Double.parseDouble(ServicetestSeconds) / 60;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			if (CommonTask.isOnline(this)) {
	
			if (CommonValues.getInstance().CollaborationMessageTime == 0l) {
				CommonValues.getInstance().CollaborationMessageTime = new Date()
						.getTime();
			}
			
			updateCurrentLocation();

			CommonValues.getInstance().LastTrafficSnapshot = new TrafficSnapshot(
					MyNetService.this);
		
			updatePhoneCallId();
			updatePhoneInformation();
			startSignalLevelListener();
			isGetPhoneInfo = false;
			// runSync();
			processPhoneInfo();

			initThread();

			receiver = new CallInformationReceiver();
			batteryReceiver = new BatteryInfomrationReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.provider.Telephony.SMS_SENT");
			filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
			registerReceiver(receiver, filter);

			IntentFilter batteryLevelFilter = new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(batteryReceiver, batteryLevelFilter);

			ContentResolver contentResolver = getContentResolver();
			contentResolver.registerContentObserver(Uri.parse("content://sms"),
					true, new SMSObserver(this, new Handler()));

			// UpdateCallAnsSMSInformation();

			Log.e("Restart", "On Restart");
			// return super.onStartCommand(intent, flags, startId);

			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 + 1) {
				Log.e("service", "START_REDELIVER_INTENT");
				return START_REDELIVER_INTENT;
			}
			Log.e("service", "START_STICKY");

			return START_REDELIVER_INTENT;
			}
		}
			 catch (Exception e) {
					// TODO: handle exception
				}

			
		
				return 0;
	}

	private void UpdateCallAnsSMSInformation() {
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		callLogList = database.getCallInfoList();
		smsInboxList = database.getSMSInfoList();
		database.close();
	}

	private void updatePhoneCallId() {
		ContentResolver resolver = getContentResolver();
		Cursor cur = resolver.query(CallLog.Calls.CONTENT_URI, null, null,
				null, CallLog.Calls._ID + " DESC");
		if (cur != null && cur.moveToFirst()) {
			phoneCallId = cur.getInt(cur.getColumnIndex(CallLog.Calls._ID));
		}
		Uri uriSMSURI = Uri.parse("content://sms");
		cur = resolver.query(uriSMSURI, null, null, null, " _id DESC");
		if (cur != null && cur.moveToFirst()) {
			lastSmsId = cur.getInt(cur.getColumnIndex("_id"));
		}
		cur.close();
	}

	private void updatePhoneInformation() {
		if (phoneBasicInformation == null) {
			MyNetDatabase mynetDatabase = new MyNetDatabase(this);
			mynetDatabase.open();
			phoneBasicInformation = mynetDatabase.getEntry();
			mynetDatabase.close();
		}
	}

	private void addPhoneInformation() {
		// while (!isDataInfoUpdating) {
		isPhoneInfoUpdating = true;
		MyNetDatabase mynetDatabase = new MyNetDatabase(this);
		mynetDatabase.open();

		currentSignalTime = new Date().getTime();

		if (currentSignalTime - previousSignalTime > 10000) {
			PhoneSignalStrenght phoneSignalStrenght = new PhoneSignalStrenght();

			phoneSignalStrenght.SignalLevel = currentSignalStrenght;
			phoneSignalStrenght.ServiceState = intServiceState;
			if (currentLocation != null) {
				phoneSignalStrenght.Latitude = currentLocation.getLatitude();
				phoneSignalStrenght.Longitude = currentLocation.getLongitude();
			}
			phoneSignalStrenght.Time = new Date();

			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			phoneSignalStrenght.CellID = String
					.valueOf(location.getCid() % 0xffff);
			phoneSignalStrenght.LAC = String
					.valueOf(location.getLac() % 0xffff);
			phoneSignalStrenght.LocationName = MyNetService.currentLocationName;

			mynetDatabase.createSignalStrenght(phoneSignalStrenght);
			previousSignalTime = currentSignalTime;
		}
		mynetDatabase.close();
		isPhoneInfoUpdating = false;
		// break;
		// }
	}

	private void startSignalLevelListener() {

		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
				| PhoneStateListener.LISTEN_DATA_ACTIVITY
				| PhoneStateListener.LISTEN_CELL_LOCATION
				| PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
				| PhoneStateListener.LISTEN_SERVICE_STATE;

		tm.listen(phoneListener, events);

	}

	private final PhoneStateListener phoneListener = new PhoneStateListener() {

		public void onCallForwardingIndicatorChanged(boolean cfi) {

			super.onCallForwardingIndicatorChanged(cfi);
		}

		/*
		 * @Override public void onCellInfoChanged(List<CellInfo> cellInfo) {
		 * super.onCellInfoChanged(cellInfo); }
		 */

		@Override
		public void onMessageWaitingIndicatorChanged(boolean mwi) {

			super.onMessageWaitingIndicatorChanged(mwi);
		}

		/*
		 * Call State Changed
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			phoneState = state;
			super.onCallStateChanged(state, incomingNumber);

		}

		/*
		 * Cell location changed event handler
		 */
		public void onCellLocationChanged(CellLocation location) {

			String strLocation = location.toString();

			super.onCellLocationChanged(location);
		}

		/*
		 * Cellphone data connection status
		 */

		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			super.onDataConnectionStateChanged(state, networkType);
			String phoneState = "UNKNOWN";

			switch (state) {

			case TelephonyManager.DATA_CONNECTED:
				// Count Connected Mode
				if (LastphoneState == "Disconnected"
						|| LastphoneState == "Suspended") {
					CommonValues.totalDataConnectionInConnectedMode += 1;
				}
				if (isFirstCall == true) {
					CommonValues.totalDataConnectionInConnectedMode += 1;
					isFirstCall = false;
				}

				phoneState = "Connected";
				LastphoneState = phoneState;
				break;
			case TelephonyManager.DATA_CONNECTING:
				phoneState = "Connecting..";
				isFirstCall = false;
				break;
			case TelephonyManager.DATA_DISCONNECTED:
				// Count Disconnected Mode
				if (LastphoneState == "Connected") {
					CommonValues.totalDataConnectionInDisconnectedMode += 1;
				}
				isFirstCall = false;
				phoneState = "Disconnected";
				LastphoneState = phoneState;
				break;
			case TelephonyManager.DATA_SUSPENDED:
				phoneState = "Suspended";
				LastphoneState = phoneState;
				isFirstCall = false;
				break;
			}
		}

		/*
		 * Data activity handler
		 */
		public void onDataActivity(int direction) {

			String strDirection = "NONE";

			switch (direction) {

			case TelephonyManager.DATA_ACTIVITY_IN:
				strDirection = "IN";
				break;
			case TelephonyManager.DATA_ACTIVITY_INOUT:
				strDirection = "IN-OUT";
				break;
			case TelephonyManager.DATA_ACTIVITY_DORMANT:
				strDirection = "Dormant";
				break;
			case TelephonyManager.DATA_ACTIVITY_NONE:
				strDirection = "NONE";
				break;
			case TelephonyManager.DATA_ACTIVITY_OUT:
				strDirection = "OUT";
				break;

			}

			super.onDataActivity(direction);
		}

		/*
		 * Cellphone Service status
		 */
		public void onServiceStateChanged(ServiceState serviceState) {

			intServiceState = serviceState.getState();

			super.onServiceStateChanged(serviceState);

		}

		/*
 * 
 * */
		public void onSignalStrengthChanged(int asu) {

			long currentTime = new Date().getSeconds();
			if (currentTime - previousTime > 2 && phoneBasicInformation != null) {
				currentSignalStrenght = asu;

			}

			super.onSignalStrengthChanged(asu);
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			long currentTime = cal.get(Calendar.SECOND);

			if (currentTime - previousTime > 5) {
				currentSignalStrenght = signalStrength.getGsmSignalStrength();
				previousTime = currentTime;
			}

			super.onSignalStrengthsChanged(signalStrength);
		}
	};

	private void initThread() {
		try {
			recieveMessageRunnable = new Runnable() {
				public void run() {

					updateSharedPreference();
					
					/*try {
						if(CommonValues.getInstance().LoginUser==null)
							return;
						if(CommonValues.getInstance().XmppConnection==null){
							ConnectionConfiguration connConfig = new ConnectionConfiguration(
									CommonURL.getInstance().CareIMHost, CommonURL.getInstance().CareIMPort, CommonURL.getInstance().CareIMService);
							CommonValues.getInstance().XmppConnection =new XMPPConnection(connConfig) ;						
						}
						if(!CommonValues.getInstance().XmppConnection.isConnected()){
							CommonValues.getInstance().XmppConnection.connect();					
							CommonValues.getInstance().XmppConnection.login(CommonValues.getInstance().LoginUser.UserID, CommonValues.getInstance().XmppUserPassword);					
							Presence presence = new Presence(Presence.Type.available);
							CommonValues.getInstance().XmppConnection.sendPacket(presence);
							PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
							ConsumerMessageListener listener = new ConsumerMessageListener(MyNetService.this);
							CommonValues.getInstance().XmppConnection.addPacketListener(listener, filter);
							
							MyNetDatabase db=new MyNetDatabase(MyNetService.this);
							List<Group>groupList=db.GetGroupList();
							MultiUserChat consumerMuc=null;
							DiscussionHistory history = new DiscussionHistory();
						    history.setMaxStanzas(50);
							for (Group group : groupList) {
								consumerMuc = new MultiUserChat(CommonValues.getInstance().XmppConnection, group.Name+CommonURL.getInstance().CareIMConference);							
							    consumerMuc.join(CommonValues.getInstance().LoginUser.UserID, CommonValues.getInstance().XmppUserPassword, history, SmackConfiguration.getPacketReplyTimeout());					        
							    consumerMuc.addMessageListener(listener);
							}
						}
						
						
						XMPPConnection connection=null;
						if(connection==null){
							ConnectionConfiguration connConfig = new ConnectionConfiguration(
									CommonURL.getInstance().CareIMHost, CommonURL.getInstance().CareIMPort, CommonURL.getInstance().CareIMService);
							connection =new XMPPConnection(connConfig) ;						
						}
						if( !connection.isConnected()){
							connection.connect();
						}
						connection.login("8801678366466", CommonValues.getInstance().XmppUserPassword);					
						presence = new Presence(Presence.Type.available);
						CommonValues.getInstance().XmppConnection.sendPacket(presence);
						filter = new MessageTypeFilter(Message.Type.chat);
						//ConsumerMUCMessageListener listener = new ConsumerMUCMessageListener();
						connection.addPacketListener(listener, filter);
						
					} catch (XMPPException ex) {					
						CommonValues.getInstance().XmppConnection=	null;
					} */

					/*while (!isDowloadRunning) {
						if (CommonTask.isOnline(MyNetService.this)
								&& !CommonValues.getInstance().IsChatingContinue) {
							if (downloadableTask != null) {
								downloadableTask.cancel(true);
							}
							downloadableTask = new DownloadableAsyncTask(
									MyNetService.this);
							downloadableTask.execute();
						}
						break;
					}*/
					recieveMessageHandler.postDelayed(recieveMessageRunnable,
							1 * 15000);
				}
			};
			recieveMessageHandler
					.postDelayed(recieveMessageRunnable, 1 * 15000);

			phoneLocationRunnable = new Runnable() {

				public void run() {
					updateCurrentLocation();
					if (phoneId == 0 && CommonTask.isOnline(MyNetService.this)) {
						isGetPhoneInfo = true;
						runSync();
					}
					phoneLocationHandler.postDelayed(phoneLocationRunnable,
							1 * 60000);

				}
			};
			phoneLocationHandler.postDelayed(phoneLocationRunnable, 1 * 60000);

			phoneDataSpeedRunnable = new Runnable() {

				public void run() {

					try {
						if (CommonTask.isOnline(MyNetService.this)) {
							isDataInfoUpdating = false;
							new SpeedTestAsyncTask(MyNetService.this).execute();
						}
						phoneDataSpeedHandler.postDelayed(
								phoneDataSpeedRunnable,
								(long) (phoneDataStatusInverval * 60 * 1000));
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			};
			phoneDataSpeedHandler.postDelayed(phoneDataSpeedRunnable,
					(long) (phoneDataStatusInverval * 60 * 1000));

			appsDataIntegrationRunnable = new Runnable() {

				public void run() {

					try {
						if (CommonTask.isOnline(MyNetService.this)) {
							new PhoneAppsDataAsyncTask(MyNetService.this)
									.execute();
						}
						appsDataIntegrationHandler.postDelayed(
								appsDataIntegrationRunnable, 30 * 60 * 1000);
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			};
			appsDataIntegrationHandler.postDelayed(appsDataIntegrationRunnable,
					30 * 60 * 1000);

			phoneStatusRunnable = new Runnable() {

				public void run() {
					addPhoneInformation();
					phoneStatusHandler.postDelayed(phoneStatusRunnable,
							(long) (phoneStatusInterval * 60000));
				}
			};
			phoneStatusHandler.postDelayed(phoneStatusRunnable,
					(long) (phoneStatusInterval * 60000));

			dataIntegrationRunnable = new Runnable() {

				public void run() {
					if (!isGetPhoneInfo
							&& CommonTask.isOnline(MyNetService.this)) {
						processPhoneInfo();
						dataIntegrationHandler.postDelayed(
								dataIntegrationRunnable, 5 * 60000);
					}
				}
			};
			dataIntegrationHandler.postDelayed(dataIntegrationRunnable,
					5 * 60000);

			problemIntegrationRunnable = new Runnable() {

				public void run() {
					if (!isGetPhoneInfo
							&& CommonTask.isOnline(MyNetService.this)) {
						new ProblemTrackingIntegrationAsyncTask(
								MyNetService.this).execute();
					}
					problemIntegrationHandler.postDelayed(
							problemIntegrationRunnable, 30 * 60000);
				}
			};
			problemIntegrationHandler.postDelayed(problemIntegrationRunnable,
					30 * 60000);

			userGroupSyncRunnable = new Runnable() {

				public void run() {
					if (CommonTask.isOnline(MyNetService.this)) {
						new UserGroupSyncAsyncTask(MyNetService.this).execute();
					}
					userGroupSyncHandler.postDelayed(userGroupSyncRunnable,
							5 * 60000);
				}
			};
			userGroupSyncHandler.postDelayed(userGroupSyncRunnable, 5 * 60000);

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private void processPhoneDataInfo() throws InterruptedException {
		while (!isLocationUpdating) {
			isDataInfoUpdating = true;
			priviousDownloadData = TrafficStats.getTotalRxBytes();
			priviousUploadData = TrafficStats.getTotalTxBytes();

			Thread.sleep(1000);
			currentDownloadData = TrafficStats.getTotalRxBytes();
			currentUploadData = TrafficStats.getTotalTxBytes();
			currentDownloadSpeedInKbPS = (int) (currentDownloadData - priviousDownloadData) / 128;
			currentUploadSpeedInKbPS = (int) (currentUploadData - priviousUploadData) / 128;

			if ((currentDownloadSpeedInKbPS > 0 || currentUploadSpeedInKbPS > 0)
					&& (phoneBasicInformation != null && currentLocation != null)) {
				PhoneDataInformation phoneDataInformation = new PhoneDataInformation();
				phoneDataInformation.PhoneId = MyNetService.phoneBasicInformation.PhoneId;
				phoneDataInformation.DownLoadSpeed = currentDownloadSpeedInKbPS;
				phoneDataInformation.UpLoadSpeed = currentUploadSpeedInKbPS;
				phoneDataInformation.DataTime = new Date();
				phoneDataInformation.Latitude = MyNetService.currentLocation
						.getLatitude();
				phoneDataInformation.Longitude = MyNetService.currentLocation
						.getLongitude();
				phoneDataInformation.LocationName = MyNetService.currentLocationName;
				MyNetDatabase mynetDatabase = new MyNetDatabase(
						MyNetService.this);
				mynetDatabase.open();
				mynetDatabase.createPhoneDataInformation(phoneDataInformation);
				mynetDatabase.close();

			}
			isDataInfoUpdating = false;
			break;
		}
	}

	private void processPhoneInfo() {
		try {
			// while(!isPhoneInfoUpdating){
			// if(new Date().getTime()- lastPhoneInfoSyncTime>60000){
			MyNetDatabase mynetDatabase = new MyNetDatabase(this);
			mynetDatabase.open();
			callList = mynetDatabase.getCallInfoListForSync();
			smsList = mynetDatabase.getSMSInfoListForSync();
			// dataInfo =
			// mynetDatabase.getAvgDataInfoByTime(lastPhoneInfoSyncTime);
			signalInfo = mynetDatabase.getSignalStrenghtListForSync();
			if (signalInfo.size() == 0) {
				PhoneSignalStrenght phoneSignalStrenght = new PhoneSignalStrenght();

				phoneSignalStrenght.SignalLevel = currentSignalStrenght;
				phoneSignalStrenght.ServiceState = intServiceState;
				if (currentLocation != null) {
					phoneSignalStrenght.Latitude = currentLocation
							.getLatitude();
					phoneSignalStrenght.Longitude = currentLocation
							.getLongitude();
				}
				phoneSignalStrenght.Time = new Date();

				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				GsmCellLocation location = (GsmCellLocation) tm
						.getCellLocation();
				phoneSignalStrenght.CellID = String
						.valueOf(location.getCid() % 0xffff);
				phoneSignalStrenght.LAC = String
						.valueOf(location.getLac() % 0xffff);
				phoneSignalStrenght.LocationName = MyNetService.currentLocationName;

				mynetDatabase.createSignalStrenght(phoneSignalStrenght);
				previousSignalTime = currentSignalTime;
				signalInfo = mynetDatabase.getSignalStrenghtListForSync();
			}
			mynetDatabase.close();
			phoneBasicInformation.PhoneId = phoneId;

			if (signalInfo.size() > 0) {
				lastPhoneInfoSyncTime = signalInfo.get(signalInfo.size() - 1).Time
						.getTime();
			} else if (callList.size() > 0) {
				lastPhoneInfoSyncTime = callList.get(callList.size() - 1).CallTime
						.getTime();
			} else if (smsList.size() > 0) {
				lastPhoneInfoSyncTime = smsList.get(smsList.size() - 1).SMSTime
						.getTime();
			}
			runSync();

			// }
		} catch (Exception e) {

		}
	}

	private void runSync() {
		if (downloadPhoneinfoAsyncTask != null) {
			downloadPhoneinfoAsyncTask.cancel(true);
		}
		downloadPhoneinfoAsyncTask = new DownloadPhoneinfoAsyncTask(
				MyNetService.this);
		downloadPhoneinfoAsyncTask.execute();
	}

	@Override
	public void onDestroy() {
		recieveMessageHandler.removeCallbacks(recieveMessageRunnable);
		unregisterReceiver(receiver);
	}

	@Override
	public void showProgressLoader() {

	}

	@Override
	public void hideProgressLoader() {

	}

	@Override
	public Object doBackgroundPorcess() {
		Log.e("Started", "Notification background process called");
		isDowloadRunning = true;
/*		INotificationManager notificationManager = new NotificationManager();
		return notificationManager.GetLiveNotifications(CommonValues
				.getInstance().CollaborationMessageTime.toString());
			
*/
		return null;
		}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
		/*	intent = new Intent(this, NotificationActivity.class);
			pendingIntent = PendingIntent.getService(this, 0, intent, 0);
			ArrayList<Object> dataList = (ArrayList<Object>) data;
			Collaborations collaborations = (Collaborations) dataList.get(0);
			if (collaborations != null
					&& collaborations.collaborationList != null
					&& collaborations.collaborationList.size() > 0) {
				Log.e("Got Collaboration", "Got Collaboration");
				showCollaborationNotificaiton(collaborations);
			} else {
				CommonValues.getInstance().CollaborationMessageTime = System
						.currentTimeMillis() + 500;
				collaborationCount = 0;
			}
			if (dataList.size() > 1) {
				if (dataList.get(1) instanceof WebDataRequestEntityHolder
						&& ((WebDataRequestEntityHolder) dataList.get(1)).webDataRequest != null
						&& ((WebDataRequestEntityHolder) dataList.get(1)).webDataRequest.IsUpdate > 0) {
					MyNetDatabase mynetDatabase = new MyNetDatabase(this);
					mynetDatabase.open();
					callList = mynetDatabase.getCallInfoListForSync();
					smsList = mynetDatabase.getSMSInfoListForSync();
					// dataInfo =
					// mynetDatabase.getAvgDataInfoByTime(lastPhoneInfoSyncTime);
					signalInfo = mynetDatabase.getSignalStrenghtListForSync();
					mynetDatabase.close();
					IPhoneInformationService oPhoneInformationService = new PhoneInformationManager();
					oPhoneInformationService.SetPhoneBasicInfo(this, phoneId,
							callList, smsList, null, signalInfo);
				}
			}

		} else {
			CommonValues.getInstance().CollaborationMessageTime = new Date()
					.getTime() + 50;
			collaborationCount = 0;*/
		}
		isDowloadRunning = false;
	}

	/*public void showCollaborationNotificaiton(Collaborations collaborations) {
		try {
			CollaborationDiscussionActivity.selectedUserGroupUnion = new UserGroupUnion();
			if (collaborations.collaborationList.size() > 0)
				CommonValues.getInstance().CollaborationMessageTime = CommonTask
						.convertJsonDateToLong(collaborations.collaborationList
								.get(collaborations.collaborationList.size() - 1).PostedDate) + 500;
			else
				CommonValues.getInstance().CollaborationMessageTime = System
						.currentTimeMillis() + 500;

			for (Collaboration collaboration : collaborations.collaborationList) {

				CollaborationDiscussionActivity.selectedUserGroupUnion.ID = collaboration.GroupID > 0 ? collaboration.GroupID
						: collaboration.MsgFrom;
				CollaborationDiscussionActivity.selectedUserGroupUnion.Name = collaboration.GroupID > 0 ? collaboration.group.Name
						: collaboration.user.FirstName;
				CollaborationDiscussionActivity.selectedUserGroupUnion.Type = collaboration.GroupID > 0 ? "G"
						: "U";
				CollaborationDiscussionActivity.selectedUserGroupUnion.PostedDate = "";
				Intent intent = new Intent(MyNetService.this,
						CollaborationDiscussionActivity.class);
				PendingIntent pIntent = PendingIntent.getActivity(
						MyNetService.this, 0, intent, 0);

				ContentResolver cr = getContentResolver();
				Cursor cursor = cr
						.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								" replace(data1,' ', '') like '%"
										+ collaboration.user.Mobile
												.substring(3) + "%'", null,
								null);
				String Name = "";
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					Name = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

				} else {
					Name = collaboration.user.FullName;
				}
				cursor.close();

				Notification mNotification = new Notification.Builder(this)
						.setContentTitle(Name)
						.setContentText(collaboration.MsgText)
						.setNumber(++collaborationCount)
						.setSmallIcon(R.drawable.mynet)
						.setContentIntent(pIntent).build();

				mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				mNotification.defaults = Notification.DEFAULT_SOUND
						| Notification.DEFAULT_VIBRATE
						| Notification.DEFAULT_LIGHTS;
				mNotification.ledARGB = Color.BLUE;
				mNotification.ledOnMS = 500;
				mNotification.ledOffMS = 500;
				mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
				android.app.NotificationManager notificationManagerGroup = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				notificationManagerGroup.notify(0, mNotification);

			}
		} catch (Exception e) {

		}

	}
*/
	
	@Override
	public void onLocationChanged(Location arg0) {
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	@Override
	public Object doBackgroundTask() {
		IPhoneInformationService phoneInfoManager = new PhoneInformationManager();
		if (!isGetPhoneInfo) {
			isDowloadRunning = true;
			return phoneInfoManager.SetPhoneBasicInfo(this, phoneId, callList,
					smsList, dataInfo, signalInfo);
		} else {
			isDowloadRunning = true;
			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			return phoneInfoManager.GetPhoneBasicInfo(tm.getDeviceId(),
					tm.getSimSerialNumber());
		}
	}

	@Override
	public void processPostDataDownload(Object data) {

		if (data != null) {
			if (isGetPhoneInfo) {
				PhoneBasicInformation phoneBasicInfo = (PhoneBasicInformation) data;
				lastPhoneInfoSyncTime = CommonTask
						.convertJsonDateToLong(phoneBasicInfo.LastSyncTime);
				phoneId = phoneBasicInfo.PhoneId;
			} else {
				// PhoneBasicInformation phoneBasicInfo =
				// (PhoneBasicInformation) data;
				if (signalInfo.size() > 0) {
					lastPhoneInfoSyncTime = signalInfo
							.get(signalInfo.size() - 1).Time.getTime();
				}
				// phoneId = phoneBasicInfo.PhoneId;
				callList = null;
				smsList = null;
				dataInfo = null;
				signalInfo = null;

			}
		} else {
			MyNetDatabase mynetDatabase = new MyNetDatabase(this);
			mynetDatabase.open();
			mynetDatabase.updateDataSyncInfo(callList, smsList, signalInfo);
			mynetDatabase.close();
		}
		isDowloadRunning = false;
		isGetPhoneInfo = false;

	}
}
