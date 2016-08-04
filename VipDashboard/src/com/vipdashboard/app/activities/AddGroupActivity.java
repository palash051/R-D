package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.UserGroupListAdapter;
import com.vipdashboard.app.adapter.UserListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.GroupRoot;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class AddGroupActivity extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask, OnItemClickListener, OnQueryTextListener {

	TextView tvAddGroup, tvCancle;
	public ListView lvUserList;
	private EditText etGroup;

	DownloadableAsyncTask downloadAsync;
	ProgressBar agDialog;
	SearchView svAddGroup;
	
	UserListAdapter userListAdapter;

	private boolean isAddButtonPressed = false;
	private boolean userList_first_check = false;
	public String selectedUsers = new String();
	boolean isCallFromNotification;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_group);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);

		tvAddGroup = (TextView) findViewById(R.id.tvAddGroup);
		tvCancle = (TextView) findViewById(R.id.tvCancle);
		etGroup = (EditText) findViewById(R.id.agEditText);
		agDialog = (ProgressBar) findViewById(R.id.agProgress);
		lvUserList = (ListView) findViewById(R.id.agUserListView);
		svAddGroup = (SearchView) findViewById(R.id.svAddGroupList);
		isCallFromNotification = CommonValues.getInstance().isCallFromNotification;
		etGroup.setHintTextColor(Color.parseColor("#6E85A4"));
		
		AutoCompleteTextView search_text = (AutoCompleteTextView) svAddGroup.findViewById(svAddGroup.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.BLACK);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
		
		svAddGroup.setOnQueryTextListener(this);
		tvAddGroup.setOnClickListener(this);
		tvCancle.setOnClickListener(this);
		lvUserList.setOnItemClickListener(this);
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
		etGroup.setText("");
		selectedUsers="";
		 if (!CommonTask.isOnline(this)) {
				if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		LoadUserList();

		
	}

	
	private void LoadUserList() {		
		MyNetDatabase db=new MyNetDatabase(this);
		db.open();
		UserGroupUnions userGroupUnions=db.GetUserList();
		db.close();	
		
		if (userGroupUnions.userGroupUnionList != null) {			
			userListAdapter = new UserListAdapter(this,
					R.layout.userlist_item_layout,new ArrayList<UserGroupUnion>(
							userGroupUnions.userGroupUnionList));
		}
		
		lvUserList.setAdapter(userListAdapter);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.tvAddGroup) {
			if (!etGroup.getText().toString().equals("") && !selectedUsers.isEmpty()) {
				this.isAddButtonPressed = true;
				saveGroup();
			}else if(etGroup.getText().toString().equals("")){
				etGroup.setError("Enter Group Name");
			}else if(selectedUsers.isEmpty()){
				etGroup.setError("Select Group Members");
			}
		}else if(id == R.id.tvCancle){
			onBackPressed();
		}
	}

	private void saveGroup() {
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}

	@Override
	public void showProgressLoader() {
		//agDialog.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//agDialog.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager usermanager = new UserManager();
		if (this.isAddButtonPressed) {
			return usermanager.SetGroup(this,etGroup.getText().toString(),
					selectedUsers);
		} else {
			/*String numbers="";
			ContentResolver cr = getContentResolver();
			Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER, null, null);
			
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				do{				
					numbers = numbers+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("+", "").replace(" ", "").replace("-", "")+",";				
				}while(cursor.moveToNext());
			}
			cursor.close();
			if(!numbers.isEmpty()){
				numbers=numbers.substring(0,numbers.length()-1);			
				IUserManager userManager = new UserManager();
				return userManager.GetUsersByMobileNumbers(numbers);
			}else{
				return null;
			}*/
		}
		return null;
	}
	
	/**
	 * 
	 */

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (this.isAddButtonPressed) {
				GroupRoot grps = (GroupRoot) data;
				MyNetDatabase db=new MyNetDatabase(this);
				db.open();
				if (isCallFromNotification && grps.group != null
						&& !grps.group.Name.equals("")
						&& grps.group.GroupID > -1) {
					db.createGroup(grps.group.Name, grps.group.GroupID);
					for (String id : selectedUsers.split(",")) {
						if(!id.isEmpty()){
							db.createUserGroup(grps.group.GroupID, Integer.valueOf(id));
						}
					}
					CommonValues.getInstance().isCallFromNotification = false;
					/*UserGroupUnion selectedUserGroupUnion = new UserGroupUnion();
					selectedUserGroupUnion.ID = grps.group.GroupID;
					selectedUserGroupUnion.Name = grps.group.Name;
					selectedUserGroupUnion.Type = "G";
					selectedUserGroupUnion.PostedDate = "";*/
					Intent intent = new Intent(this,
							CareIMMessegingActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("UserID",grps.group.GroupID);
					intent.putExtra("UserType","G");
					startActivity(intent);
				}else if(grps.group.Name.equals("Duplicate")){
					DuplicateErrorDialog();
				}else {
					//onBackPressed();
					db.createGroup(grps.group.Name, grps.group.GroupID);
					for (String id : selectedUsers.split(",")) {
						if(!id.isEmpty()){
							db.createUserGroup(grps.group.GroupID, Integer.valueOf(id));
						}
					}
					Toast.makeText(AddGroupActivity.this, grps.group.Name + " Successfully Create", Toast.LENGTH_SHORT).show();
					/*UserGroupUnion selectedUserGroupUnion = new UserGroupUnion();
					selectedUserGroupUnion.ID = grps.group.GroupID;
					selectedUserGroupUnion.Name = grps.group.Name;
					selectedUserGroupUnion.Type = "G";
					selectedUserGroupUnion.PostedDate = "";*/
					Intent intent = new Intent(this,
							CareIMMessegingActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("UserID",grps.group.GroupID);
					intent.putExtra("UserType","G");
					startActivity(intent);
				}
				db.close();
			} else {
				UserGroupUnions userGroupUnions = (UserGroupUnions) data;				
				if (userGroupUnions.userGroupUnionList != null) {
					 ArrayList<UserGroupUnion> list=new ArrayList<UserGroupUnion>();
					for (UserGroupUnion UserGroupUnion : userGroupUnions.userGroupUnionList) {
						if(!UserGroupUnion.Type.equals("G"))
							list.add(UserGroupUnion);
					}
					userListAdapter = new UserListAdapter(this,
							R.layout.group_item_layout,list);
				}
				
				lvUserList.setAdapter(userListAdapter);
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

	private void DuplicateErrorDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AddGroupActivity.this);
		alertDialogBuilder.setTitle("Add Group");
		alertDialogBuilder.setMessage("Someone already has that group name. Try another?");
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								onBackPressed();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		String id = view.getTag().toString();
		  if (!id.equals("")) {
		   
		   if(selectedUsers.isEmpty()){
		    selectedUsers=id;
		   }else{
		    String newIds="";
		    String[] ids =selectedUsers.split(",");
		    boolean isAdded=false;
		    for (String st : ids) {
		     if(!st.equalsIgnoreCase(id)){
		      newIds=newIds+st+",";
		     }else{
		      isAdded=true;
		     }
		    }
		    if(!isAdded)
		     newIds=newIds+id+",";
		    if(newIds.length()>0)
		     newIds=newIds.substring(0, newIds.length()-1);
		    selectedUsers=newIds;
		   }
		  }
		//Log.e("pressed value", selectedUsers);
	}

	@Override
	public boolean onQueryTextChange(String value) {
		userListAdapter.GroupMemberListFilter(value);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String value) {
		// TODO Auto-generated method stub
		return false;
	}
}
