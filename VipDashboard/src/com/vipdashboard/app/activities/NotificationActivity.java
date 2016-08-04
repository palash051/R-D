package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.NotificationListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Notification;
import com.vipdashboard.app.entities.Notifications;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.manager.NotificationManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class NotificationActivity extends MainActionbarBase implements
		IAsynchronousTask, OnClickListener {

	ProgressBar pbNotificationList;

	RelativeLayout bNotificationDiscussion, bCollaborationChat;
	ListView lvNotificationList;
	TextView tvNotificationTitle;
	public static UserGroupUnion selectedUserGroupUnion;
	DownloadableAsyncTask downloadableAsyncTask;
	NotificationListAdapter notoficationListAdapter;
	public static Notification _notification;
	int LoadEarlierCount,count;
	TextView notificationLoadEarlier;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		
		android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
		
		notificationLoadEarlier = (TextView) findViewById(R.id.tvNotificationShowMeMore);
		bNotificationDiscussion = (RelativeLayout) findViewById(R.id.rlEnterToDiscussionNotification);
		bCollaborationChat = (RelativeLayout) findViewById(R.id.rlCollaborationChat);

		lvNotificationList = (ListView) findViewById(R.id.lvNotificationList);
		pbNotificationList = (ProgressBar) findViewById(R.id.pbNotificationList);

		tvNotificationTitle = (TextView) findViewById(R.id.tvNotificationTitle);

		tvNotificationTitle.setText(selectedUserGroupUnion.Name);
		
		bNotificationDiscussion.setOnClickListener(this);
		bCollaborationChat.setOnClickListener(this);
		notificationLoadEarlier.setOnClickListener(this);
/*		if(Context.NOTIFICATION_SERVICE!=null){
			android.app.NotificationManager notificationMamager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationMamager.cancelAll();
		}*/
		

	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		count = 0;
		LoadEarlierCount = (++count)*CommonConstraints.USER_MESSAGE_COUNT;
		runDownloadable();
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.rlEnterToDiscussionNotification){
			CommonValues.getInstance().isCallFromNotification = true;
			Intent intent = new Intent(this, AddGroupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(v.getId() == R.id.rlCollaborationChat){
			CollaborationDiscussionActivity.selectedUserGroupUnion = selectedUserGroupUnion;
			Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(v.getId() == R.id.tvNotificationShowMeMore){
			LoadEarlierCount = (count++)*CommonConstraints.USER_MESSAGE_COUNT;
			runDownloadable();
		}
	}

	private void runDownloadable() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(
				NotificationActivity.this);
		downloadableAsyncTask.execute();
	}

	

	@Override
	public void showProgressLoader() {

		pbNotificationList.setVisibility(View.VISIBLE);

	}

	@Override
	public void hideProgressLoader() {

		pbNotificationList.setVisibility(View.GONE);

	}

	@Override
	public Object doBackgroundPorcess() {
		INotificationManager notificationManager = new NotificationManager();

		return notificationManager
				.GetLiveNotificationByUser(selectedUserGroupUnion,LoadEarlierCount);
	}

	@Override
	public void processDataAfterDownload(Object data) {

		if (data != null) {
			Notifications notifications = (Notifications) data;
			if (notifications.notificationList != null) {
				notoficationListAdapter = new NotificationListAdapter(this,
						R.layout.notification, new ArrayList<Notification>(
								notifications.notificationList));
				lvNotificationList.setAdapter(notoficationListAdapter);
				if (notoficationListAdapter.getCount() > 0) {
					lvNotificationList.setSelection(notoficationListAdapter
							.getCount() - 1);
					CommonValues.getInstance().notificationMessageTime = CommonTask.convertJsonDateToLong
							(notoficationListAdapter.getLastItem().UpdatedDate);
				}
			}
		}

	}	
}
