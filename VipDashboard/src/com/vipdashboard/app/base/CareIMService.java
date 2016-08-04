package com.vipdashboard.app.base;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.vipdashboard.app.classes.ConsumerMessageListener;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class CareIMService extends Service {	

	public CareIMService() {

	}
	
	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	@Override
	public void onCreate() {
	}
	@Override
	public void onStart(Intent intent, int startId) {
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
			
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 + 1) {			
			return START_REDELIVER_INTENT;
		}
		connect();	
		return START_STICKY;
	}
	
	private void connect() {
		ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

		// This schedule a runnable task every 2 minutes
		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
		  public void run() {
			  try {
				  ConsumerMessageListener listener=null;
					if(CommonValues.getInstance().LoginUser==null)
						return;
					if(CommonValues.getInstance().XmppConnection==null){
						ConnectionConfiguration connConfig = new ConnectionConfiguration(
								CommonURL.getInstance().CareIMHost, CommonURL.getInstance().CareIMPort, CommonURL.getInstance().CareIMService);
						CommonValues.getInstance().XmppConnection =new XMPPConnection(connConfig) ;
						ServiceDiscoveryManager.getInstanceFor(CommonValues.getInstance().XmppConnection);
					}
					if(!CommonValues.getInstance().XmppConnection.isConnected()){
						CommonValues.getInstance().XmppConnection.connect();
						CommonValues.getInstance().XmppConnection.login(CommonValues.getInstance().LoginUser.UserID, CommonValues.getInstance().XmppUserPassword);					
						Presence presence = new Presence(Presence.Type.available);
						CommonValues.getInstance().XmppConnection.sendPacket(presence);
						PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
						listener = new ConsumerMessageListener(CareIMService.this);
						CommonValues.getInstance().XmppConnection.addPacketListener(listener, filter);
						
						/* sdm.addFeature("http://jabber.org/protocol/disco#info");
			                sdm.addFeature("jabber:iq:privacy");
			                sdm.addFeature("http://jabber.org/protocol/disco#items");
						sdm.addFeature("http://jabber.org/protocol/disco#info");
		                sdm.addFeature("jabber:iq:privacy");
		                sdm.addFeature("http://jabber.org/protocol/disco#items");*/						
					}
					
					if(CommonValues.getInstance().XmppConnectedGroup==null && CommonValues.getInstance().XmppConnection.isConnected()){
						MyNetDatabase db=new MyNetDatabase(CareIMService.this);
						List<Group>groupList=db.GetGroupList();
						MultiUserChat consumerMuc=null;	
						if(groupList!=null){
							CommonValues.getInstance().XmppConnectedGroup=new HashMap<Integer, Group>();
							/*DiscussionHistory history = new DiscussionHistory();
							history.setMaxStanzas(0);*/
							for (Group group : groupList) {								
								consumerMuc = new MultiUserChat(CommonValues.getInstance().XmppConnection, group.Name.replace(" ", "_")+CommonURL.getInstance().CareIMConference);							
							    consumerMuc.join(CommonValues.getInstance().LoginUser.UserID, CommonValues.getInstance().XmppUserPassword);					        
							    consumerMuc.addMessageListener(listener);	
							    group.MultiUserChatId=consumerMuc;
							    CommonValues.getInstance().XmppConnectedGroup.put(group.GroupID, group);
							}
						}
					}
				} catch (XMPPException ex) {					
					CommonValues.getInstance().XmppConnection=	null;
				} 
		  }
		}, 0, 15, TimeUnit.SECONDS);
		
	}
	
}
