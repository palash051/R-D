package com.vipdashboard.app.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.SendNotificationActivity;
import com.vipdashboard.app.adapter.GroupListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonValues;

public class EnterToDiscussionGroupListFragment extends MainActionbarBase
		implements IAsynchronousTask, OnItemClickListener, OnClickListener {
	ListView lvGroupList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar pbGroupList;
	GroupListAdapter groupListAdapter;
	boolean isCallFromNotification;
	Button bUserNotification, bSendNotification;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		/*mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");*/
		pbGroupList = (ProgressBar) findViewById(R.id.pbGroupList);
		lvGroupList = (ListView) findViewById(R.id.lvGroupList);
		isCallFromNotification = CommonValues.getInstance().isCallFromNotification;
		RelativeLayout rlNotificationItem = (RelativeLayout) findViewById(R.id.rlNotificationItem);
		if (isCallFromNotification) {
			rlNotificationItem.setVisibility(RelativeLayout.VISIBLE);
			bUserNotification = (Button) rlNotificationItem
					.findViewById(R.id.bUserNotification);
			bSendNotification = (Button) rlNotificationItem
					.findViewById(R.id.bSendNotification);
			bUserNotification.setOnClickListener(this);
			bSendNotification.setOnClickListener(this);
		} else {
			rlNotificationItem.setVisibility(RelativeLayout.GONE);
		}
		LoadUserList();
		
		
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
	

	private void LoadUserList() {

		lvGroupList.setOnItemClickListener(this);
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		pbGroupList.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {

		pbGroupList.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {

		IUserManager userManager = new UserManager();
		return userManager.getGroupsByCompanyID();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {

			ArrayList<Group> adapterData = (ArrayList<Group>) data;
			groupListAdapter = new GroupListAdapter(this,R.layout.group_item_layout, adapterData);
			lvGroupList.setAdapter(groupListAdapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		DiscussionFragment.selectedGroupId = Integer.parseInt(String.valueOf(v
				.getTag()));
	
		Intent intent = new Intent(this, DiscussionFragment.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.bUserNotification) {
			
			Intent intent = new Intent(this, NotificationFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}if(id == R.id.bSendNotification){
			Intent intent = new Intent(this, SendNotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}

	}

}
