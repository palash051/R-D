package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.UsersGroupsListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.GroupRoot;
import com.vipdashboard.app.entities.Groups;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class AddMoreMemberActivity extends MainActionbarBase implements OnClickListener, OnItemClickListener,
					IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	SearchView searchView;
	ListView listView;
	TextView tvAddMember;
	
	boolean isCallFromAddButton;
	public static GroupRoot userGroups;
	
	TextView titleText;
	
	UsersGroupsListAdapter adapter;
	public String selectedUsers = new String();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usergroup_list_layout);
		searchView = (SearchView) findViewById(R.id.svCollaborationList);
		listView = (ListView) findViewById(R.id.lvUserGroupList);
		tvAddMember = (TextView) findViewById(R.id.tvAddMember);
		titleText = (TextView) findViewById(R.id.tvCollaborationMainTitle);
		titleText.setText("Group");
		tvAddMember.setOnClickListener(this);
		listView.setOnItemClickListener(this);
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
		  isCallFromAddButton = false;
		  if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
				{
				if (!isFinishing()) 
				{
					CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
					CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
				}
		  LoadDownloadData();
	
	 }

	private void LoadDownloadData() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.tvAddMember){
			isCallFromAddButton = true;
			LoadDownloadData();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Group usergroup = (Group) view.getTag();
		String id = String.valueOf(usergroup.GroupID);
		if (!id.equals("")) {
			if (selectedUsers.isEmpty()) {
				selectedUsers = id;
			} else {
				String newIds = "";
				String[] ids = selectedUsers.split(",");
				boolean isAdded = false;
				for (String st : ids) {
					if (!st.equalsIgnoreCase(id)) {
						newIds = newIds + st + ",";
					} else {
						isAdded = true;
					}
				}
				if (!isAdded)
					newIds = newIds + id + ",";
				if (newIds.length() > 0)
					newIds = newIds.substring(0, newIds.length() - 1);
				selectedUsers = newIds;
			}
		}
		if(selectedUsers.isEmpty()){
			tvAddMember.setVisibility(View.GONE);
		}else{
			tvAddMember.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showProgressLoader() {
	}

	@Override
	public void hideProgressLoader() {
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		if(isCallFromAddButton){
			return userManager.SetMembership(selectedUsers);
		}else{
			return userManager.GetGroupsNotIncludeOfUser();
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(isCallFromAddButton){
				isCallFromAddButton = false;
				onBackPressed();
			}else{
				Groups groups = (Groups) data;
				adapter = new UsersGroupsListAdapter(this, R.layout.userlist_item_layout, new ArrayList<Group>(groups.groupList));
				listView.setAdapter(adapter);	
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
}
