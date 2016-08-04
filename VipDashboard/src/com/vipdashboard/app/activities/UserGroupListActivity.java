package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.GroupInformationAdapter;
import com.vipdashboard.app.adapter.GroupUserInformationAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.GroupRoot;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserGroup;
import com.vipdashboard.app.entities.UserGroups;
import com.vipdashboard.app.entities.Users;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;

public class UserGroupListActivity extends MainActionbarBase implements OnClickListener, OnItemClickListener, IAsynchronousTask {
	
	DownloadableAsyncTask downloadableAsyncTask;
	SearchView searchView;
	ListView listView;
	TextView tvAddMember;
	boolean isCallFromAddButton;
	public static GroupRoot userGroups;
	
	GroupUserInformationAdapter adapter;
	private boolean userList_first_check = false;
	public String selectedUsers = new String();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usergroup_list_layout);
		searchView = (SearchView) findViewById(R.id.svCollaborationList);
		listView = (ListView) findViewById(R.id.lvUserGroupList);
		tvAddMember = (TextView) findViewById(R.id.tvAddMember);
		
		
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
	  MyNetApplication.activityResumed();
	  isCallFromAddButton = false;
	  LoadDownloadData();
	  super.onResume();
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
		User usergroup = (User) view.getTag();
		String id = String.valueOf(usergroup.UserNumber);
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
			return userManager.SetUserGroupByGroupID(String.valueOf(userGroups.group.GroupID), selectedUsers);
		}else{
			return userManager.getUsersByGroupId(String.valueOf(userGroups.group.GroupID));
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(isCallFromAddButton){
				isCallFromAddButton = false;
				onBackPressed();
			}else{
				Users users = (Users) data;
				ArrayList<User> userlist=new ArrayList<User>();
				ContentResolver cr = getContentResolver();
				Cursor cursor =null;
				for (User user : users.userList) {					
					cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + user.Mobile.substring(3) +"%'", null, null);					
					if(cursor.getCount() > 0){
						cursor.moveToFirst();
						userlist.add(user);				
					}
				}
				cursor.close();
				adapter = new GroupUserInformationAdapter(this, R.layout.userlist_item_layout, userlist);
				listView.setAdapter(adapter);	
			}
			
		}
	}

}
