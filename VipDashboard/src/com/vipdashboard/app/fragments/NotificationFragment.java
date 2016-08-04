package com.vipdashboard.app.fragments;

import java.text.ParseException;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ChatListAdapter;
import com.vipdashboard.app.adapter.NotificationCharListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Notification;
import com.vipdashboard.app.entities.Notifications;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.manager.NotificationManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class NotificationFragment extends MainActionbarBase implements
		IAsynchronousTask {

	//TextView tvMessageBoxGroup;
	//ProgressBar pbGroupDiscussion;
	DownloadableAsyncTask downloadableAsyncTask;
	ListView lvChatList;
	NotificationCharListAdapter chatList;

	/*public static Fragment newInstance(Context context) {
		NotificationFragment f = new NotificationFragment();
		return f;
	}*/

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		
		lvChatList = (ListView) findViewById(R.id.lvChatList);
		//pbGroupDiscussion = (ProgressBar) findViewById(R.id.pbGroupDiscussion);
		//tvMessageBoxGroup = (TextView) findViewById(R.id.tvMessageBoxGroup);
		((RelativeLayout) findViewById(R.id.rlMessageSend)).setVisibility(RelativeLayout.GONE);
		runDownloadable();
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
	}
	

	private void runDownloadable() {
		Log.e("runDownloadable", "Execute");
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(
				NotificationFragment.this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		Log.e("ShowProgressLoader", "inside");

	}

	@Override
	public void hideProgressLoader() {
		Log.e("hideProgressLoader", "inside");

	}

	@Override
	public Object doBackgroundPorcess() {
		INotificationManager notificationManager = new NotificationManager();
		Log.e("doBackground", "instance create and then return");
		return notificationManager.GetLiveNotificationsByMsgTo();

	}

	@Override
	public void processDataAfterDownload(Object data) {
		Log.e("precoss data After Download", "inside");
		if(data == null)
			Log.e("After Download: ", "data null");
		if(data != null){
			Notifications notifications = (Notifications) data;
			if(notifications.notificationList != null){
				chatList = new NotificationCharListAdapter(this, R.layout.message_layout, new ArrayList<Notification>(notifications.notificationList));
				lvChatList.setAdapter(chatList);
			}else{
				Log.e("Motification List", "Empty");
			}
		}
		/*if (data != null) {
			try {
				String sbMessage = "";
				Notifications notifications = (Notifications) data;
				for (Notification notification : notifications.notificationList) {
					sbMessage = sbMessage
							+ notification.msgFromUser.Name
							+ "("
							+ CommonTask.toStringDate(CommonTask
									.parseDate(notification.UpdatedDate))
							+ ") : " + notification.NotificationText + "\r\n";
				}
				tvMessageBoxGroup.setText(sbMessage);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}*/
	}

}
