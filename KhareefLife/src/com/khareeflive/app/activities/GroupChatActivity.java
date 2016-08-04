package com.khareeflive.app.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.asynchronoustask.GetGroupMessageTask;
import com.khareeflive.app.asynchronoustask.SendGroupMessageTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.GroupUser;
import com.khareeflive.app.entities.GroupUserAdapter;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.entities.LoginAuthentication;
import com.khareeflive.app.entities.Message;
import com.khareeflive.app.entities.RoomWiseChat;
import com.khareeflive.app.manager.LatestUpdateManager;
import com.khareeflive.app.utils.CommonValues;

public class GroupChatActivity extends Activity implements OnClickListener,IDownloadProcessorActicity, OnItemClickListener {

	String sbMessage = "";
	private static Thread recieveMessageThread;
	private static Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	GetGroupMessageTask getMessageTask;
	SendGroupMessageTask sendMessageTask;
	TextView tvMessageBox;
	Button bSendMessage;
	EditText etMessage;

	Message message = null;
	
	GroupUserAdapter userListAdapter;
	ListView lvUserlist;
	DownloadableTask downloadableTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warroomgroup);
		tvMessageBox = (TextView) findViewById(R.id.tvMessageBoxGroup);
		bSendMessage = (Button) findViewById(R.id.bSendMessageGroup);
		etMessage = (EditText) findViewById(R.id.etMessageGroup);
		bSendMessage.setOnClickListener(this);
		recieveMessageHandler = new Handler();
		initThread();
		LoadUserList();
	}

	private void initThread() {
		if (recieveMessageThread == null) {

			recieveMessageRunnable = new Runnable() {
				public void run() {
					if (message == null) {
						if (getMessageTask != null) {
							getMessageTask.cancel(true);
						}
						getMessageTask = new GetGroupMessageTask(
								GroupChatActivity.this);
						getMessageTask.execute();
					}
					recieveMessageHandler.postDelayed(recieveMessageRunnable,
							10000);
				}
			};
		}
		recieveMessageThread = new Thread(recieveMessageRunnable);
		recieveMessageThread.start();
	}

	public ArrayList<RoomWiseChat> GetMessage() {
		return LatestUpdateManager
				.GetRoomWiseChat(CommonValues.getInstance().selectedGroupName);
	}

	public boolean SendMessage() {		
		return LatestUpdateManager.LoadRoomChat(CommonValues.getInstance().selectedGroupName, CommonValues.getInstance().LoginUser.userName, etMessage.getText().toString());
	}

	public void processGetMessage(ArrayList<RoomWiseChat> _roomWiseChatList) {
		if (_roomWiseChatList != null) {
			sbMessage = "";
			for (RoomWiseChat roomWiseChat : _roomWiseChatList) {
				sbMessage = roomWiseChat.from +"("+ roomWiseChat.msgTime.substring(9)  + ") : " + roomWiseChat.message +  "\r\n"+ sbMessage;
			}
			tvMessageBox.setText(sbMessage.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			CommonValues.getInstance().lastWarMessageTime=sdf.format(date);
		}
	}

	public void procesSendData(boolean success) {
		if (success) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			String reportDate = df.format(Calendar.getInstance().getTime());
			sbMessage = sbMessage+"Me("+ reportDate +"): " + etMessage.getText().toString() +  "\r\n";
			tvMessageBox.setText(sbMessage.toString());
			etMessage.setText("");			
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.bSendMessageGroup) {
			if(!etMessage.getText().toString().equals("")){
				if (sendMessageTask != null) {
					sendMessageTask.cancel(true);
				}
				sendMessageTask = new SendGroupMessageTask(GroupChatActivity.this);
				sendMessageTask.execute();
			}
		}

	}

	@Override
	public void showProgressLoader() {
		
		
	}

	@Override
	public void hideProgressLoader() {
		
		
	}

	@Override
	public Object doBackgroundDownloadPorcess() {
		
		return LatestUpdateManager.GetChatRoomUserID(CommonValues.getInstance().selectedGroupName);
	}

	@Override
	public void processDownloadedData(Object data) {
		
		ArrayList<GroupUser > adapterData=(ArrayList<GroupUser>) data;
		if(CommonValues.getInstance().LoginUser!=null){
			for (GroupUser groupUser : adapterData) {			
				if(groupUser.message.trim().toLowerCase().toString().equals(CommonValues.getInstance().LoginUser.userName.trim().toLowerCase().toString())){
					adapterData.remove(groupUser);
					break;
				}
			}
		}
		userListAdapter = new GroupUserAdapter(this,	R.layout.userlistiteminfo, adapterData);
		lvUserlist.setAdapter(userListAdapter);		
	}
	
	private void LoadUserList() {
		
		lvUserlist=(ListView)findViewById(R.id.lvUserlistGroup);
		lvUserlist.setVisibility(View.GONE);
		lvUserlist.setOnItemClickListener(this);
		if (downloadableTask != null) {
			downloadableTask.cancel(true);
		}			
		downloadableTask = new DownloadableTask(this);
		downloadableTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		CommonValues.getInstance().selectedUser=new LoginAuthentication();
		CommonValues.getInstance().selectedUser.userID=(String) v.getTag();
		CommonValues.getInstance().selectedUser.status=1;
		Intent intent = new Intent(this,ChatActivity.class);			
		startActivity(intent);
		
	}
	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}
}
