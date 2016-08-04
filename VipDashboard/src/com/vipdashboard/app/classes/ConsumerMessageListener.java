package com.vipdashboard.app.classes;

import java.io.File;
import java.io.FileOutputStream;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.util.Base64;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CareIMMessegingActivity;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.utils.CommonValues;

public class ConsumerMessageListener implements PacketListener {
	Context context;
	int notifyId=0;
	Intent i=null;
	long previousMsgTime=0,currentMsgTime=0;
	
	public ConsumerMessageListener(Context _context) {
		context = _context;
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) {
			Message message = (Message) packet;
			
			if (message.getBody() != null) {
				/*currentMsgTime= Long.parseLong((String) message.getProperty("PostedDate"));
				if((currentMsgTime- previousMsgTime)<200)
					return;*/
				//previousMsgTime=currentMsgTime;
				Collaboration coll = new Collaboration();
				coll.MsgText = message.getBody();				
				coll.UserType = (String) message.getProperty("UserType");
				coll.GroupID = (Integer) message.getProperty("GroupId");
				coll.MsgFrom = (Integer) message.getProperty("UserIdFrom");
				coll.MsgTo = (Integer) message.getProperty("UserIdTo");
				coll.PostedDate = String.valueOf(System.currentTimeMillis());// (String) message.getProperty("PostedDate");
				coll.Latitude = (Double) message.getProperty("Latitude");
				coll.Longitude = (Double) message.getProperty("Longitude");
				coll.MsgFromName= StringUtils.parseName(message.getFrom());
				
				if(coll.MsgText.contains("FILESTRING:")){
					coll.MsgText=coll.MsgText.replace("FILESTRING:", "");
					File folder = new File(Environment.getExternalStorageDirectory() + "/MumtazCare");
		            if(!folder.exists())
		            	folder.mkdir();
		            File file=new File(folder.getAbsoluteFile()+"/"+ String.valueOf( System.currentTimeMillis())+".jpg");
		            try {
		                FileOutputStream fos=new FileOutputStream(file.getPath());
		                fos.write(Base64.decode(coll.MsgText, 0));
		                fos.close();
		                coll.MsgText="FILEPATH:"+file.getPath();
		              }
		              catch (java.io.IOException e) {
		              }
				}
				
				if(!coll.MsgText.equals("'FILE:START'")){
					UserGroupUnion currentUser=CareIMMessegingActivity.getCurrentUser();
					if(currentUser==null||!((currentUser.Type.equals("G")&&currentUser.ID==coll.GroupID)||(currentUser.Type.equals("U")&&currentUser.ID==coll.MsgFrom))){
						showNotification(coll);
					}else{
						i = new Intent(CommonValues.getInstance().XmppReceiveMessageIntent);	
						i.putExtra("msg", coll);	
						context.sendBroadcast(i);
					}				
					MyNetDatabase db=new MyNetDatabase(context);				
					db.createCollaboration(coll);
				}else{
					i = new Intent(CommonValues.getInstance().XmppReceiveMessageIntent);	
					i.putExtra("msg", coll);	
					context.sendBroadcast(i);
				}
			}
		}
	}

	
	
	private void showNotification(Collaboration coll){		
		
		String userFrom=coll.UserType.equals("G")?(CommonValues.getInstance().XmppConnectedGroup.get(coll.GroupID).Name):(CommonValues.getInstance().ConatactUserList.get(coll.MsgFrom).Name);
		
		Intent intent = new Intent(this.context,CareIMMessegingActivity.class);
		intent.putExtra("UserID", coll.UserType.equals("G")?coll.GroupID:coll.MsgFrom);
		intent.putExtra("UserType", coll.UserType);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent pIntent = PendingIntent.getActivity(
				this.context,notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification mNotification = new Notification.Builder(this.context)
		.setContentTitle("One message from: " + userFrom)
		.setContentText(coll.MsgText)
		.setNumber(1)
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
		android.app.NotificationManager notificationManagerGroup = (android.app.NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManagerGroup.notify(notifyId, mNotification);	
		notifyId++;
	}
	
}
