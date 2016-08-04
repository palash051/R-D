package com.vipdashboard.app.activities;

import java.util.ArrayList;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.GroupInformationAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.GroupRoot;
import com.vipdashboard.app.entities.Groups;
import com.vipdashboard.app.entities.UserGroup;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroups;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GroupInformationActivity extends MainActionbarBase implements IAsynchronousTask, OnClickListener, OnItemClickListener {
	
	TextView groupName, adminName, createDate;
	ImageView image;
	ListView groupMamberList;
	TextView tvAddMember, tvDeleteMember;
	DownloadableAsyncTask downloadAsync;
	ProgressBar pbLoadGroupInformation;
	public static UserGroupUnion selectUserGroupUnion;
	GroupInformationAdapter adapter;
	
	private boolean userList_first_check = false;
	public String selectedUsers = new String();
	boolean isCallFromAddButton;
	boolean isCallFromDeleteButton;
	
	GroupRoot groupRoot;
	UserGroups usergroups;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_information);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		initialization();
		tvAddMember.setOnClickListener(this);
		tvDeleteMember.setOnClickListener(this);
		groupMamberList.setOnItemClickListener(this);
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
		userList_first_check = isCallFromAddButton = isCallFromDeleteButton = false;
		LoadInformation();
		
		selectedUsers = new String();
		
	}
	
	private void initialization() {
		groupName = (TextView) findViewById(R.id.groupName);
		adminName = (TextView) findViewById(R.id.tvgroupAdminName);
		createDate = (TextView) findViewById(R.id.tvGroupCreateDate);
		groupMamberList = (ListView) findViewById(R.id.lvGroupMemberList);
		pbLoadGroupInformation = (ProgressBar) findViewById(R.id.pbGroupInformation);
		image  = (ImageView) findViewById(R.id.groupImage);
		tvAddMember = (TextView) findViewById(R.id.tvAddMember);
		tvDeleteMember = (TextView) findViewById(R.id.tvDeleteMember);
	}
	
	private void LoadInformation() {
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(GroupInformationActivity.this);
		downloadAsync.execute();
	}

	@Override
	public void showProgressLoader() {
		//pbLoadGroupInformation.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//pbLoadGroupInformation.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		if(isCallFromDeleteButton){
			/*MyNetDatabase db=new MyNetDatabase(this);
			db.open();
			db.deleteLocalUserGroup(groupRoot.group.GroupID,selectedUsers);
			db.close();*/
			if(selectedUsers.length()>0 && selectedUsers.endsWith(","))
			{
				selectedUsers=selectedUsers.substring(0,selectedUsers.length()-1);
			}
			return userManager.deleteGroupMemberByGroupid(String.valueOf(groupRoot.group.GroupID), selectedUsers);
		}else if(isCallFromAddButton){
			return null;
		}else{
			return userManager.getGroupMembersByGroupId(String.valueOf(selectUserGroupUnion.ID));
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){ 
			 if(isCallFromDeleteButton){
				isCallFromDeleteButton = false;
				UserGroups usergroups = (UserGroups) data;
				adapter = new GroupInformationAdapter(this, R.layout.userlist_item_layout,
						new ArrayList<UserGroup>(usergroups.userGroupList));
				//adapter.notifyDataSetChanged();
				//groupMamberList.setAdapter(adapter);
				selectedUsers = new String();
				
				tvAddMember.setVisibility(View.VISIBLE);
				tvDeleteMember.setVisibility(View.GONE);
				LoadInformation();				
			}else{
				ArrayList<Object> userGroupList = (ArrayList<Object>) data;
				usergroups = (UserGroups) userGroupList.get(0);
				groupRoot = (GroupRoot) userGroupList.get(1);
				
				try{
					if(usergroups.userGroupList != null){
						adapter = new GroupInformationAdapter(this, R.layout.userlist_item_layout,
								new ArrayList<UserGroup>(usergroups.userGroupList));
						groupMamberList.setAdapter(adapter);
					}
					for (UserGroup usergroup : usergroups.userGroupList) {
						if (usergroup.Group.Name.length() > 15) {
							String text = usergroup.Group.Name.substring(0, 15);
							text = text.replaceAll("\n", " ");
							groupName.setText(text + "...");
						} else {
							groupName.setText(usergroup.Group.Name.toString());
						}
						createDate.setText(CommonTask.convertJsonDateToUserGroup(usergroup.Group.CreatedDate));
					}
					if(groupRoot.group.user.FullName != null){
						String name = CommonTask.getContentName(this, groupRoot.group.user.FullName);
						if(name.isEmpty()){
							adminName.setText(groupRoot.group.user.FullName);
						}else{
							adminName.setText(name);
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}else{
			Toast.makeText(this, "You are not allowed to delete this member from group", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.tvAddMember){
			UserGroupListActivity.userGroups = groupRoot;
			Intent intent = new Intent(this, UserGroupListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(view.getId() == R.id.tvDeleteMember){
			isCallFromDeleteButton = true;
			LoadInformation();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long ID) {
		UserGroup usergroup = (UserGroup) view.getTag();
		String id = String.valueOf(usergroup.User.UserNumber);
		/*if (!id.equals("")) {
			selectedUsers += id + ",";
		}*/
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
			tvAddMember.setVisibility(View.VISIBLE);
			tvDeleteMember.setVisibility(View.GONE);
		}else{
			tvAddMember.setVisibility(View.GONE);
			tvDeleteMember.setVisibility(View.VISIBLE);
		}
	}
}
