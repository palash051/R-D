package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings.System;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ChatListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveCollaborationManager;
import com.vipdashboard.app.manager.LiveCollaborationManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;


public class CollaborationDiscussionActivity extends MainActionbarBase implements  IAsynchronousTask, OnClickListener, OnKeyListener, TextWatcher{

	private static Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	
	EditText etMessageText;
	Button bSendMessage;
	ListView lvChatList;
	TextView tvChatMainTitle, tvGroupMemberName, tvVoiceRecorder;
	public static UserGroupUnion selectedUserGroupUnion;
	DownloadableAsyncTask downloadableAsyncTask;
	ChatListAdapter chatListAdapter;
	
	private final int DOWNLOAD_CHAT = 0;
	private final int SEND_CHAT = 1;
	private final int DOWNLOAD_CHAT_ALL = 2;
	private boolean isDownloadRunning = false;
	boolean isSendingOrAllRunning = false;
	int selectedBackgroundProcess = DOWNLOAD_CHAT;
	Collaboration selectedCollaboration;
	TextView loadEarlier;
	long longLastMessageTime;
	int LoadEarlierPress,count;
	
	ImageView ivChatUserImage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);                                                                         
		
		android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);	
		
		longLastMessageTime=0;
		loadEarlier= (TextView) findViewById(R.id.tvShowMeMore);
		lvChatList=(ListView) findViewById(R.id.lvChatList);
		
		etMessageText = (EditText) findViewById(R.id.etMessageText);
		bSendMessage = (Button) findViewById(R.id.bSendMessage);
		tvChatMainTitle=(TextView) findViewById(R.id.tvChatMainTitle);
		tvGroupMemberName = (TextView) findViewById(R.id.tvGroupMemberName);
		tvVoiceRecorder = (TextView) findViewById(R.id.tvVoiceRecorder);
		ivChatUserImage= (ImageView) findViewById(R.id.ivChatUserImage);
		
		bSendMessage.setOnClickListener(this);
		recieveMessageHandler = new Handler();
		
		isSendingOrAllRunning = true;
		isDownloadRunning = true;
		
		tvChatMainTitle.setOnClickListener(this);
		etMessageText.setOnKeyListener(this);
		etMessageText.addTextChangedListener(this);
		CommonTask.makeLinkedTextview(this, loadEarlier, loadEarlier.getText().toString());
		loadEarlier.setOnClickListener(this);
		count = 0;
		LoadEarlierPress = (++count)*CommonConstraints.USER_MESSAGE_COUNT;
	}	
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		/*if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}*/
		CommonValues.getInstance().IsChatingContinue=false;
		recieveMessageHandler.removeCallbacks(recieveMessageRunnable);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			return;	
		}
		
		if(selectedUserGroupUnion==null)
		{
			String prefUserPass=CommonTask.getPreferences(CollaborationDiscussionActivity.this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
			Intent intent = new Intent(CollaborationDiscussionActivity.this,
					HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
			startActivity(intent);
		}
		else
		{
			CommonValues.getInstance().IsChatingContinue=true;		
			MyNetApplication.activityResumed();
			loadEarlier.setVisibility(View.GONE);
			tvChatMainTitle.setText(selectedUserGroupUnion.Name);
			selectedBackgroundProcess = DOWNLOAD_CHAT_ALL;
			runDownloadable();
			manageGroupUserOrUserImage();
			initThread();
		}
		CommonValues.getInstance().CollaborationMessageTime=java.lang.System.currentTimeMillis();
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}
	private void manageGroupUserOrUserImage() {
		if(selectedUserGroupUnion.Type.equals("U")){
			ContentResolver cr = getContentResolver();
			Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + selectedUserGroupUnion.Name.substring(3) +"%'", null, null);
			
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				tvChatMainTitle.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
				Bitmap bit=CommonTask.fetchContactImageThumbnail(this,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
				if(bit!=null)
					ivChatUserImage.setImageBitmap(bit);
				else{
					ivChatUserImage.setImageResource(R.drawable.user_icon);
				}
			}else{
				tvChatMainTitle.setText(selectedUserGroupUnion.Name);
				ivChatUserImage.setImageResource(R.drawable.user_icon);
			}
			cursor.close();
		}else{
			tvChatMainTitle.setText(selectedUserGroupUnion.Name);
			ivChatUserImage.setImageResource(R.drawable.user_group);
		}
	}
	private void initThread() {	
		recieveMessageRunnable = new Runnable() {
			public void run() {	
				if(!isDownloadRunning && !isSendingOrAllRunning){
					isDownloadRunning = true;
					runDownloadable();
				}
				recieveMessageHandler.postDelayed(recieveMessageRunnable, 5000);
			}
		};

		recieveMessageHandler.postDelayed(recieveMessageRunnable, 5000);
	}
	
	private void runDownloadable() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(CollaborationDiscussionActivity.this);
		downloadableAsyncTask.execute();
	}
	
	@Override
	public void showProgressLoader() {
	
	}

	@Override
	public void hideProgressLoader() {
	
	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveCollaborationManager liveCollaborationManager = new LiveCollaborationManager();
		if (selectedBackgroundProcess == DOWNLOAD_CHAT_ALL) {
			MyNetDatabase db=new MyNetDatabase(this);
			Collaborations col=null;
			
			try
			{
				db.open();
				col=db.GetLiveCollaborationsByMsgTo(selectedUserGroupUnion.ID,selectedUserGroupUnion.Type,CommonConstraints.USER_MESSAGE_COUNT);
				db.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			return col;
			//return liveCollaborationManager.GetLiveCollaborationsByMsgTo(selectedUserGroupUnion,0,LoadEarlierPress);
		}else if (selectedBackgroundProcess == DOWNLOAD_CHAT) {
			return liveCollaborationManager.GetLiveCollaborationsByMsgTo(selectedUserGroupUnion,longLastMessageTime,LoadEarlierPress);
		}else if (selectedBackgroundProcess == SEND_CHAT) {			
			return liveCollaborationManager.SendLiveCollaboration(selectedCollaboration);
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			if (selectedBackgroundProcess == DOWNLOAD_CHAT_ALL) {
			    Collaborations collaborations=(Collaborations)data;
				if(collaborations.collaborationList!=null ){
					chatListAdapter=new ChatListAdapter(this, R.layout.message_layout, new ArrayList<Collaboration>(collaborations.collaborationList));
					lvChatList.setAdapter(chatListAdapter);
					if(chatListAdapter.getCount()>0){
						lvChatList.setSelection(chatListAdapter.getCount() - 1);					
						longLastMessageTime=CommonTask.convertJsonDateToLong( chatListAdapter.getLastItem().PostedDate)+500;
						if(longLastMessageTime>CommonValues.getInstance().CollaborationMessageTime)
							CommonValues.getInstance().CollaborationMessageTime = longLastMessageTime;
					}										
				}
				selectedBackgroundProcess = DOWNLOAD_CHAT;
				isDownloadRunning=false;
				isSendingOrAllRunning=false;
				if(chatListAdapter!=null && chatListAdapter.getCount()>29){
					loadEarlier.setVisibility(View.VISIBLE);
				}
				
			}else if (selectedBackgroundProcess == DOWNLOAD_CHAT) {
				Collaborations collaborations=(Collaborations)data;
				if(collaborations.collaborationList!=null && collaborations.collaborationList.size()>0 ){
					MyNetDatabase db=new MyNetDatabase(this);
					db.open();
					if(chatListAdapter==null){
						chatListAdapter=new ChatListAdapter(this, R.layout.message_layout, new ArrayList<Collaboration>(collaborations.collaborationList));						
						for (Collaboration collaboration : collaborations.collaborationList) {
							if(selectedUserGroupUnion.Type.equals("G")){
								collaboration.MsgFromName=  collaboration.user!=null? collaboration.user.Mobile:"";
								collaboration.GroupName= selectedUserGroupUnion.Type.equals("G")?selectedUserGroupUnion.Name: "";
							}else{
								collaboration.MsgFromName=selectedUserGroupUnion.Name;
								collaboration.GroupName="";
							}
							db.createCollaboration(collaboration);
						}
						lvChatList.setAdapter(chatListAdapter);
					}else{
						for (Collaboration collaboration : collaborations.collaborationList) {
							if(selectedUserGroupUnion.Type.equals("G")){
								collaboration.MsgFromName=  collaboration.user!=null? collaboration.user.Mobile:"";
								collaboration.GroupName= selectedUserGroupUnion.Type.equals("G")?selectedUserGroupUnion.Name: "";
							}else{
								collaboration.MsgFromName=selectedUserGroupUnion.Name;
								collaboration.GroupName="";
							}
							chatListAdapter.addItem(collaboration);							
							db.createCollaboration(collaboration);
						}	
					}
					db.close();
					chatListAdapter.notifyDataSetChanged();
					if(chatListAdapter.getCount()>0){
						lvChatList.setSelection(chatListAdapter.getCount() - 1);
						longLastMessageTime=CommonTask.convertJsonDateToLong( chatListAdapter.getLastItem().PostedDate)+500;
						if(longLastMessageTime>CommonValues.getInstance().CollaborationMessageTime)
							CommonValues.getInstance().CollaborationMessageTime = longLastMessageTime;
					}
					selectedBackgroundProcess = DOWNLOAD_CHAT;
					isDownloadRunning=false;
				}else if(!isSendingOrAllRunning){
					selectedBackgroundProcess = DOWNLOAD_CHAT;
					isDownloadRunning=false;
				}
					
				
			}else{	
				Collaboration collaboration=(Collaboration)data;
				chatListAdapter.getLastItem().PostedDate=collaboration.PostedDate;
				chatListAdapter.notifyDataSetChanged();
				lvChatList.setSelection(chatListAdapter.getCount() - 1);
				longLastMessageTime=CommonTask.convertJsonDateToLong(collaboration.PostedDate)+500;
				MyNetDatabase db=new MyNetDatabase(this);
				db.open();
				if(selectedUserGroupUnion.Type.equals("G")){
					collaboration.MsgFromName=  CommonValues.getInstance().LoginUser.Mobile;
					collaboration.GroupName= selectedUserGroupUnion.Type.equals("G")?selectedUserGroupUnion.Name: "";
				}else{
					collaboration.MsgFromName=selectedUserGroupUnion.Name;
					collaboration.GroupName="";
				}
				db.createCollaboration(collaboration);
				db.close();
				if(longLastMessageTime>CommonValues.getInstance().CollaborationMessageTime)
					CommonValues.getInstance().CollaborationMessageTime = longLastMessageTime;
				selectedBackgroundProcess = DOWNLOAD_CHAT;
				isDownloadRunning=false;	
				isSendingOrAllRunning=false;
			}
			
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}
	@Override
	public void onClick(View view) {
		if (!CommonTask.isOnline(this)) {
			return;	
		}
		if(view.getId() == R.id.tvShowMeMore){
			if (!CommonTask.isOnline(this)) {
				return;	
			}
			/*count++;
			LoadEarlierPress = (count)*CommonConstraints.USER_MESSAGE_COUNT;
			selectedCollaboration = new Collaboration();
			selectedCollaboration.UserType=selectedUserGroupUnion.Type;		
			selectedCollaboration.GroupID = selectedUserGroupUnion.ID;		
			selectedCollaboration.MsgTo=selectedUserGroupUnion.ID;
			selectedCollaboration.MsgFrom = CommonValues.getInstance().LoginUser.UserNumber;
			selectedCollaboration.MsgText = etMessageText.getText().toString();
			selectedCollaboration.PostedDate="Sending";
			selectedCollaboration.Latitude = MyNetService.currentLocation.getLatitude();
			selectedCollaboration.Longitude = MyNetService.currentLocation.getLongitude();
			selectedBackgroundProcess = DOWNLOAD_CHAT_ALL;
			etMessageText.setText("");
			isDownloadRunning=true;
			isSendingOrAllRunning=true;
			runDownloadable();*/
			selectedBackgroundProcess=100;
			MyNetDatabase db=new MyNetDatabase(this);
			db.open();
			Collaborations col=db.GetLiveCollaborationsByMsgTo(selectedUserGroupUnion.ID,selectedUserGroupUnion.Type,(++count)*CommonConstraints.USER_MESSAGE_COUNT);
			db.close();
			
			if(col.collaborationList!=null ){
				chatListAdapter=new ChatListAdapter(this, R.layout.message_layout, new ArrayList<Collaboration>(col.collaborationList));
				lvChatList.setAdapter(chatListAdapter);
				if(chatListAdapter.getCount()>0){
					lvChatList.setSelection(chatListAdapter.getCount() - 1);					
					longLastMessageTime=CommonTask.convertJsonDateToLong( chatListAdapter.getLastItem().PostedDate);
					CommonValues.getInstance().CollaborationMessageTime = CommonTask.convertJsonDateToLong( chatListAdapter.getLastItem().PostedDate);
				}										
			}
			selectedBackgroundProcess = DOWNLOAD_CHAT;
			isDownloadRunning=false;
			isSendingOrAllRunning=false;
			if(chatListAdapter.getCount()>=count*CommonConstraints.USER_MESSAGE_COUNT){
				loadEarlier.setVisibility(View.VISIBLE);
			}
		}
		else if(view.getId() == R.id.bSendMessage){
			if (!CommonTask.isOnline(this)) {
				return;	
			}
			if(etMessageText.getText().toString().equals("")) 
				return;
			selectedCollaboration = new Collaboration();
			selectedCollaboration.UserType=selectedUserGroupUnion.Type;		
			selectedCollaboration.GroupID = selectedUserGroupUnion.ID;		
			selectedCollaboration.MsgTo=selectedUserGroupUnion.ID;
			selectedCollaboration.MsgFrom = CommonValues.getInstance().LoginUser.UserNumber;
			selectedCollaboration.MsgText = etMessageText.getText().toString();
			selectedCollaboration.PostedDate="Sending";
			selectedCollaboration.Latitude = MyNetService.currentLocation.getLatitude();
			selectedCollaboration.Longitude = MyNetService.currentLocation.getLongitude();
			if(chatListAdapter==null){
				chatListAdapter=new ChatListAdapter(this, R.layout.message_layout, new ArrayList<Collaboration>());
				lvChatList.setAdapter(chatListAdapter);
			}
			chatListAdapter.addItem(selectedCollaboration);
			chatListAdapter.notifyDataSetChanged();
			lvChatList.setSelection(chatListAdapter.getCount() - 1);
			selectedBackgroundProcess = SEND_CHAT;
			etMessageText.setText("");
			isDownloadRunning=true;
			isSendingOrAllRunning=true;
			runDownloadable();
		}else if(view.getId() == R.id.tvChatMainTitle){
			//UserGroupUnion userGroup = (UserGroupUnion) view.getTag();
			if (selectedUserGroupUnion.Type.equals("G")) {

				GroupInformationActivity.selectUserGroupUnion = selectedUserGroupUnion;
				Intent intent = new Intent(this, GroupInformationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else{
				ContactActivity.selectUserGroupUnion = selectedUserGroupUnion;
				Intent intent = new Intent(this, ContactActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(v.getId()==R.id.etMessageText && keyCode == KeyEvent.KEYCODE_ENTER && 
                event.getAction() == KeyEvent.ACTION_DOWN){
			if (!CommonTask.isOnline(this)) {
				return false;	
			}
			if(etMessageText.getText().toString().equals("")) 
				return false;
			selectedCollaboration = new Collaboration();
			selectedCollaboration.UserType=selectedUserGroupUnion.Type;		
			selectedCollaboration.GroupID = selectedUserGroupUnion.ID;		
			selectedCollaboration.MsgTo=selectedUserGroupUnion.ID;
			selectedCollaboration.MsgFrom = CommonValues.getInstance().LoginUser.UserNumber;
			selectedCollaboration.MsgText = etMessageText.getText().toString();
			selectedCollaboration.PostedDate="Sending";
			if(chatListAdapter==null){
				chatListAdapter=new ChatListAdapter(this, R.layout.message_layout, new ArrayList<Collaboration>());
				lvChatList.setAdapter(chatListAdapter);
			}
			chatListAdapter.addItem(selectedCollaboration);
			chatListAdapter.notifyDataSetChanged();
			lvChatList.setSelection(chatListAdapter.getCount() - 1);
			selectedBackgroundProcess = SEND_CHAT;
			etMessageText.setText("");
			isDownloadRunning=true;
			isSendingOrAllRunning=true;
			runDownloadable();			
		}
		return false;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
		if(charSequence.length() > 0){
			tvVoiceRecorder.setVisibility(View.GONE);
			bSendMessage.setVisibility(View.VISIBLE);
		}else{
			tvVoiceRecorder.setVisibility(View.VISIBLE);
			bSendMessage.setVisibility(View.GONE);
		}
	}

}
