package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.UserGroupListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.asynchronoustask.UserGroupSyncAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.entities.UserRelationship;
import com.vipdashboard.app.fragments.CollaborationMainFragment;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class CollaborationDiscussionListActivity extends MainActionbarBase
		implements IAsynchronousTask, OnItemClickListener, OnClickListener, OnQueryTextListener,OnItemLongClickListener, OnCloseListener {
	//private Runnable recieveMessageRunnable;
	//private Handler recieveMessageHandler;
	ListView lvCollaborationDiscussionList;
	SearchView svAllUsers;
	TextView titleText,tvManualSync;
	
	DownloadableAsyncTask downloadableAsyncTask;
	UserGroupUnions userGroupUnions;
	
	UserGroupListAdapter userGroupListAdapter;
	UserGroupUnion userGroupLongPress;
	public boolean isDownloadRunning;
	public boolean isSendingOrAllRunning;
	RelativeLayout rlChatToFavourites, rlChatToRecents,rlChatUserStatus,rlChatUserSettings;
	boolean isCallFromRemoveUserFromFevourite;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collaboration_discussion_list);
		lvCollaborationDiscussionList = (ListView) findViewById(R.id.lvCollaborationDiscussionList);
		titleText = (TextView) findViewById(R.id.tvCollaborationDiscussionTitle);
		tvManualSync = (TextView) findViewById(R.id.tvManualSync);
		rlChatToFavourites = (RelativeLayout) findViewById(R.id.rlChatToFavourites);
		rlChatToRecents	= (RelativeLayout) findViewById(R.id.rlChatToRecents);
		rlChatUserStatus = (RelativeLayout) findViewById(R.id.rlChatUserStatus);
		rlChatUserSettings = (RelativeLayout) findViewById(R.id.rlChatUserSettings);
		
		tvManualSync.setOnClickListener(this);
		rlChatToFavourites	.setOnClickListener(this);
		rlChatToRecents	.setOnClickListener(this);
		rlChatUserStatus	.setOnClickListener(this);
		rlChatUserSettings	.setOnClickListener(this);
		lvCollaborationDiscussionList.setOnItemClickListener(this);
		lvCollaborationDiscussionList.setOnItemLongClickListener(this);
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			return;	
		}
		MyNetApplication.activityResumed();
		LoadUserList();
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}
	private void LoadUserList() {
		/*if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();*/
		MyNetDatabase db=new MyNetDatabase(this);
		db.open();
		UserGroupUnions userGroupUnions=db.GetUserGroupList();
		db.close();
		if (userGroupUnions.userGroupUnionList != null) {
			userGroupListAdapter = new UserGroupListAdapter(this,
					R.layout.group_item_layout,
					new ArrayList<UserGroupUnion>(
							userGroupUnions.userGroupUnionList));
		}
		lvCollaborationDiscussionList.setAdapter(userGroupListAdapter);
	}
	
	private void LoadUserListInformation() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		/*if(isDownloadRunning && isSendingOrAllRunning)
			pbCollaborationDiscussionList.setVisibility(View.VISIBLE);*/

	}

	@Override
	public void hideProgressLoader() {
		/*if(isDownloadRunning && isSendingOrAllRunning)
			pbCollaborationDiscussionList.setVisibility(View.GONE);*/

	}

	@Override
	public Object doBackgroundPorcess() {
		/*String numbers="";
		String number="";
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER, null, null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{	
				number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("+", "").replace(" ", "").replace("-", "");
				if(numbers.indexOf(number)<0)
					numbers = numbers+number+",";				
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
/*		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}*/
		if(isCallFromRemoveUserFromFevourite){
			IUserManager userManager = new UserManager();
			return userManager.UserRelationShipInactiveById(userGroupLongPress.ID, CommonValues.getInstance().LoginUser.UserNumber);
		}else{
			MyNetDatabase db=new MyNetDatabase(this);
			db.open();
			UserGroupUnions userGroupUnions=db.GetUserGroupList();
			db.close();
			return userGroupUnions;
		}
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if(isCallFromRemoveUserFromFevourite){
				UserRelationship relationship = (UserRelationship) data;
				MyNetDatabase database = new MyNetDatabase(this);
				database.open();
				database.updateUserList(relationship.FriendNumber);
				database.close();
				isCallFromRemoveUserFromFevourite = false;
				LoadUserList();
			}else{
				userGroupUnions = (UserGroupUnions) data;
				if (userGroupUnions.userGroupUnionList != null) {
					userGroupListAdapter = new UserGroupListAdapter(this,
							R.layout.group_item_layout,
							new ArrayList<UserGroupUnion>(
									userGroupUnions.userGroupUnionList));
				}
				lvCollaborationDiscussionList.setAdapter(userGroupListAdapter);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		UserGroupUnion userGroup = (UserGroupUnion) v.getTag();
		if(userGroup.Type.equals("G")){
			CollaborationDiscussionActivity.selectedUserGroupUnion = (UserGroupUnion) v
					.getTag();
			Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else{
			CollaborationDiscussionActivity.selectedUserGroupUnion = (UserGroupUnion) v
				.getTag();
			Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.rlChatToRecents){
			Intent intent = new Intent(this,
					CollaborationMainFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (v.getId() == R.id.rlChatUserStatus) {			
			Intent intent = new Intent(this,
					CollaborationUserStatusActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if (v.getId() == R.id.rlChatToFavourites) {			
			Intent intent = new Intent(this,
					CollaborationFavouriteList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(v.getId() == R.id.rlChatUserSettings){
			Intent intent = new Intent(this,AddGroupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(v.getId() == R.id.tvManualSync){
			new UserGroupSyncAsyncTask(this).execute();
			LoadUserList();
		}
		
	}
	@Override
	public boolean onQueryTextChange(String value) {
		userGroupListAdapter.filter(value);
		return true;
		
	}
	
	public void LoadData(Object data){
		userGroupUnions = (UserGroupUnions) data;
		if (userGroupUnions.userGroupUnionList != null) {
			userGroupListAdapter = new UserGroupListAdapter(this,
					R.layout.group_item_layout,
					new ArrayList<UserGroupUnion>(
							userGroupUnions.userGroupUnionList));
		}
		lvCollaborationDiscussionList.setAdapter(userGroupListAdapter);
	}
	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		userGroupLongPress = (UserGroupUnion) view.getTag();
		if(userGroupLongPress.Type.equals("G")){
			/*GroupInformationActivity.selectUserGroupUnion = (UserGroupUnion) view.getTag();
			Intent intent = new Intent(this, GroupInformationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);*/
		}else{
			openContextMenu(lvCollaborationDiscussionList);
			registerForContextMenu(lvCollaborationDiscussionList);
		}
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_longpress_profile, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.profile){
			ContactActivity.selectUserGroupUnion = new UserGroupUnion();
			ContactActivity.selectUserGroupUnion.ID=userGroupLongPress.ID;
			ContactActivity.selectUserGroupUnion.Name=userGroupLongPress.Name;
			ContactActivity.selectUserGroupUnion.userOnlinestatus=userGroupLongPress.userOnlinestatus;
			ContactActivity.selectUserGroupUnion.Type="U";
			Intent intent = new Intent(this, ContactActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(item.getItemId() == R.id.communicaiton){
			CollaborationDiscussionActivity.selectedUserGroupUnion = new UserGroupUnion();
			CollaborationDiscussionActivity.selectedUserGroupUnion.ID=userGroupLongPress.ID;
			CollaborationDiscussionActivity.selectedUserGroupUnion.Name=userGroupLongPress.Name;
			CollaborationDiscussionActivity.selectedUserGroupUnion.userOnlinestatus=userGroupLongPress.userOnlinestatus;
			CollaborationDiscussionActivity.selectedUserGroupUnion.Type="U";
			Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(item.getItemId() == R.id.remove){
			isCallFromRemoveUserFromFevourite = true;
			showDialog();
		}
		return true;
	}
	
	@Override
	public boolean onClose() {
		titleText.setVisibility(View.VISIBLE);
		return false;
	}
	
	public void showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(R.string.app_name).setMessage("Do you confirm to delete?")
				.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoadUserListInformation();
					}
				});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
