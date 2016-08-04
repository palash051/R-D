package com.khareeflive.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.GetMessageTask;
import com.khareeflive.app.asynchronoustask.SendMessageTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.Message;
import com.khareeflive.app.manager.UserManager;
import com.khareeflive.app.utils.CommonValues;

public class ChatActivity extends Activity implements OnClickListener {
	
	StringBuilder sbMessage = new StringBuilder();
	private static Thread recieveMessageThread;
	private static Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	GetMessageTask getMessageTask;
	SendMessageTask sendMessageTask;
	TextView tvMessageBox;
	Button bSendMessage;
	EditText etMessage;

	Message message = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warroom);
		tvMessageBox = (TextView) findViewById(R.id.tvMessageBox);
		bSendMessage = (Button) findViewById(R.id.bSendMessage);
		etMessage = (EditText) findViewById(R.id.etMessage);
		bSendMessage.setOnClickListener(this);
		recieveMessageHandler = new Handler();
		initThread();
	}

	private void initThread() {
		if (recieveMessageThread == null) {

			recieveMessageRunnable = new Runnable() {
				public void run() {
					if (message == null ) {						
						if (getMessageTask != null) {
							getMessageTask.cancel(true);
						}
						getMessageTask = new GetMessageTask(
								ChatActivity.this);
						getMessageTask.execute();
					}
					recieveMessageHandler.postDelayed(recieveMessageRunnable,
							2000);
				}
			};
		}
		recieveMessageThread = new Thread(recieveMessageRunnable);
		recieveMessageThread.start();
	}	
	public Message GetMessage() {		
		return UserManager.GetMessage(
				CommonValues.getInstance().selectedUser.userID,
				CommonValues.getInstance().LoginUser.userName);
	}

	public boolean SendMessage() {
		return UserManager.SendMessage(
				CommonValues.getInstance().LoginUser.userName, CommonValues
						.getInstance().selectedUser.userID, etMessage.getText()
						.toString());
	}

	public void processGetMessage(Message _message) {
		message = _message;
		if (message != null) {
			sbMessage.append("\r\n"
					+ CommonValues.getInstance().selectedUser.userID + " : "
					+ message.message);
			tvMessageBox.setText(sbMessage.toString());
			message=null;
		}
	}

	public void procesSendData(boolean success) {
		if (success) {
			sbMessage.append("\r\n" + "Me : " + etMessage.getText().toString());
			tvMessageBox.setText(sbMessage.toString());
			etMessage.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.bSendMessage) {
			if (sendMessageTask != null) {
				sendMessageTask.cancel(true);
			}
			sendMessageTask = new SendMessageTask(ChatActivity.this);
			sendMessageTask.execute();
		}

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
