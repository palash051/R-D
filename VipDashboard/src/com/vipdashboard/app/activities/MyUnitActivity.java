package com.vipdashboard.app.activities;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.UserGroupItemListAdapter;
import com.vipdashboard.app.adapter.UserGroupListCheckAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonValues;

public class MyUnitActivity extends MainActionbarBase implements
		IAsynchronousTask, OnClickListener, OnItemClickListener {

	Button bSeeAll;
	ListView groupListView;
	RadioGroup rgMyUnit;
	RadioButton rbCollaboration, rbNotification;
	DownloadableAsyncTask downloadAsync;
	ProgressBar agProgress;
	private boolean DownloadAllGroup = false;
	UserGroupItemListAdapter userGroupAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myunit);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(false);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");

		groupListView = (ListView) findViewById(R.id.lGroupList);
		bSeeAll = (Button) findViewById(R.id.bEnterToDiscussion);
		rgMyUnit = (RadioGroup) findViewById(R.id.rgGroupStyle);
		rbCollaboration = (RadioButton) findViewById(R.id.CollaborationGraphButton);
		rbNotification = (RadioButton) findViewById(R.id.NotificationGraphButton);
		agProgress = (ProgressBar) findViewById(R.id.agProgress);

		bSeeAll.setOnClickListener(this);
		rgMyUnit.setOnClickListener(this);
		rbCollaboration.setOnClickListener(this);
		rbNotification.setOnClickListener(this);
		groupListView.setOnItemClickListener(this);

	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		mSupportActionBar.setSubtitle("Welcome " + CommonValues.getInstance().LoginUser.Name.toString());
		DownloadAllGroup = false;
		runDownloadable();
		
	}

	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bEnterToDiscussion) {
			DownloadAllGroup = true;
			runDownloadable();
		}
	}

	private void runDownloadable() {
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}

	@Override
	public void showProgressLoader() {
		agProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		agProgress.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.getGroupsByUserNumber();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			UserGroupUnions userGroupUnions = (UserGroupUnions) data;
			if (DownloadAllGroup) {
				userGroupAdapter = new UserGroupItemListAdapter(this,
						R.layout.group_item_layout,
						new ArrayList<UserGroupUnion>(
								userGroupUnions.userGroupUnionList));
				groupListView.setAdapter(userGroupAdapter);
				userGroupAdapter.notifyDataSetChanged();
				DownloadAllGroup = false;
			} else {
				if (userGroupUnions != null
						&& userGroupUnions.userGroupUnionList.size() > 0) {
					ArrayList<UserGroupUnion> userGroup = new ArrayList<UserGroupUnion>();
					userGroupAdapter = new UserGroupItemListAdapter(this,
							R.layout.userlist_item_layout, userGroup);
					int count=userGroupUnions.userGroupUnionList.size()>3?3:userGroupUnions.userGroupUnionList.size();
					for (int i = 0; i < count; i++) {
						
						userGroupAdapter
								.addItem(userGroupUnions.userGroupUnionList
										.get(i));
					}
					groupListView.setAdapter(userGroupAdapter);
					userGroupAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		if (rbCollaboration.isChecked()) {
			CollaborationDiscussionActivity.selectedUserGroupUnion = (UserGroupUnion) view
					.getTag();
			Intent intent = new Intent(this,
					CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(rbNotification.isChecked()){
			NotificationActivity.selectedUserGroupUnion=(UserGroupUnion)view.getTag();		
			Intent intent = new Intent(this, NotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else{
			Toast.makeText(this, "Choose Any One (Collaboration / Notification)", Toast.LENGTH_SHORT).show();
		}
	}

}
