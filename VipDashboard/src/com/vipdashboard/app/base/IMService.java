/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vipdashboard.app.base;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Timer;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.Messaging;
import com.vipdashboard.app.classes.SocketOperator;
import com.vipdashboard.app.interfaces.IAppManager;
import com.vipdashboard.app.interfaces.ISocketOperator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;



/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application.  The {@link LocalServiceController}
 * and {@link LocalServiceBinding} classes show how to interact with the
 * service.
 *
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
public class IMService extends Service implements IAppManager {
//	private NotificationManager mNM;
	
	public static final String TAKE_MESSAGE = "Take_Message";
	public static final String FRIEND_LIST_UPDATED = "Take Friend List";
	public ConnectivityManager conManager = null; 
	private final int UPDATE_TIME_PERIOD = 15000;
//	private static final int LISTENING_PORT_NO = 8956;
	private String rawFriendList = new String();


	ISocketOperator socketOperator = new SocketOperator(this);

	private final IBinder mBinder = new IMBinder();
	private String username;
	private String password;
	private String userKey;
	private boolean authenticatedUser = false;
	 // timer to take the updated data from server
	private Timer timer;
	
	private NotificationManager mNM;
	
	
	public class IMBinder extends Binder {
		public IAppManager getService() {
			return IMService.this;
		}
		
	}
	   
    @Override
    public void onCreate() 
    {   	
         mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
     //   showNotification();
    	conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    	
    	
    	// Timer is used to take the friendList info every UPDATE_TIME_PERIOD;
		timer = new Timer();   
		
		Thread thread = new Thread()
		{
			@Override
			public void run() {			
				
				//socketOperator.startListening(LISTENING_PORT_NO);
				Random random = new Random();
				int tryCount = 0;
				while (socketOperator.startListening(9999)  == 0 )//10000 + random.nextInt(20000)
				{		
					tryCount++; 
					if (tryCount > 10)
					{
						// if it can't listen a port after trying 10 times, give up...
						break;
					}
					
				}
			}
		};		
		thread.start();
    
    }

/*
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }
*/	

	@Override
	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}
	
	/**
	 * Show a notification while this service is running.
	 * @param msg 
	 **/
    private void showNotification(String username, String msg) 
	{       
        // Set the icon, scrolling text and timestamp
    	String title = username + ": " + 
     				((msg.length() < 5) ? msg : msg.substring(0, 5)+ "...");
        Notification notification = new Notification(R.drawable.mynet, 
        					title,
                System.currentTimeMillis());

        Intent i = new Intent(this, Messaging.class);
        i.putExtra("username", username);
        i.putExtra("message", msg);	
        
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                i, 0);

        // Set the info for the views that show in the notification panel.
        // msg.length()>15 ? msg : msg.substring(0, 15);
        notification.setLatestEventInfo(this, "New message from " + username,
                       						msg, 
                       						contentIntent);
        
        //TODO: it can be improved, for instance message coming from same user may be concatenated 
        // next version
        
        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify((username+msg).hashCode(), notification);
    }
	 

	public String getUsername() {		
		return username;
	}

	public boolean sendMessage(String  username, String message) {
		//FriendInfo friendInfo = FriendController.getFriendInfo(username);
		String IP = "192.168.1.6";//friendInfo.ip;
		//IP = "10.0.2.2";
		int port =9999;// Integer.parseInt(friendInfo.port);
		
		String msg = "username=1" +
		 "&userKey=2" +
		 "&message=" + URLEncoder.encode(message) +
		 "&";
		
		return socketOperator.sendMessage(msg, IP,  port);
	}

	public void messageReceived(String message) 
	{				
		String[] params = message.split("&");
		String username= new String();
		String userKey = new String();
		String msg = new String();
		for (int i = 0; i < params.length; i++) {
			String[] localpar = params[i].split("=");
			if (localpar[0].equals("username")) {
				username = URLDecoder.decode(localpar[1]);
			}
			else if (localpar[0].equals("userKey")) {
				userKey = URLDecoder.decode(localpar[1]);
			}
			else if (localpar[0].equals("message")) {
				msg = URLDecoder.decode(localpar[1]);
			}			
		}
		
		Intent i = new Intent(TAKE_MESSAGE);
		
		i.putExtra("username", "1");			
		i.putExtra("message", msg);			
		sendBroadcast(i);
		showNotification(username, msg);
		
		/*FriendInfo friend = FriendController.checkFriend(username, userKey);
		if ( friend != null)
		{			
			Intent i = new Intent(TAKE_MESSAGE);
		
			i.putExtra("username", friend.userName);			
			i.putExtra("message", msg);			
			sendBroadcast(i);
			String activeFriend = FriendController.getActiveFriend();
			if (activeFriend == null || activeFriend.equals(username) == false) 
			{
				showNotification(username, msg);
			}
		}	*/
		
	}  
	
	

	public void setUserKey(String value) 
	{
		this.userKey = value;		
	}

	public boolean isNetworkConnected() {
		return conManager.getActiveNetworkInfo().isConnected();
	}
	
	public boolean isUserAuthenticated(){
		return authenticatedUser;
	}
	
	public String getLastRawFriendList() {		
		return this.rawFriendList;
	}
	
	@Override
	public void onDestroy() {
		Log.i("IMService is being destroyed", "...");
		super.onDestroy();
	}
	
	public void exit() 
	{
		timer.cancel();
		socketOperator.exit(); 
		socketOperator = null;
		this.stopSelf();
	}

	
	
}