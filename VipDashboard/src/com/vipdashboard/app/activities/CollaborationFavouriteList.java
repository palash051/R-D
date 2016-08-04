package com.vipdashboard.app.activities;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.UserGroupFavouritListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserFamilyMember;
import com.vipdashboard.app.entities.UserFamilyMembers;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserRelationship;
import com.vipdashboard.app.fragments.CollaborationMainFragment;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollaborationFavouriteList extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask, OnItemClickListener,
		OnItemLongClickListener {

	private ListView lvCollaborationDiscussionList;
	private TextView titleText, tvManualSync;
	private RelativeLayout rlChatToFavourites, rlChatToRecents,
			rlChatUserStatus, rlChatUserSettings, rlChatToContacts;
	DownloadableAsyncTask downloadableAsyncTask;
	UserFamilyMembers userFamilyMembers;
	ProgressDialog progress;
	UserGroupFavouritListAdapter userGroupListAdapter;
	UserFamilyMember userGroupUnionLongPress;
	ImageView ivChatToFavourites, ivChatToContacts;
	boolean isCallFromRemoveUserFromFevourite, isCalledFromRemoveFromFavorite;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collaboration_discussion_list);

		lvCollaborationDiscussionList = (ListView) findViewById(R.id.lvCollaborationDiscussionList);
		lvCollaborationDiscussionList.setOnItemClickListener(this);
		lvCollaborationDiscussionList.setOnItemLongClickListener(this);
		registerForContextMenu(lvCollaborationDiscussionList);

		titleText = (TextView) findViewById(R.id.tvCollaborationDiscussionTitle);
		tvManualSync = (TextView) findViewById(R.id.tvManualSync);
		tvManualSync.setVisibility(View.GONE);

		rlChatToFavourites = (RelativeLayout) findViewById(R.id.rlChatToFavourites);
		rlChatToContacts = (RelativeLayout) findViewById(R.id.rlChatToContacts);
		rlChatToRecents = (RelativeLayout) findViewById(R.id.rlChatToRecents);
		rlChatUserStatus = (RelativeLayout) findViewById(R.id.rlChatUserStatus);
		rlChatUserSettings = (RelativeLayout) findViewById(R.id.rlChatUserSettings);
		ivChatToFavourites = (ImageView) findViewById(R.id.ivChatToFavourites);
		ivChatToContacts = (ImageView) findViewById(R.id.ivChatToContacts);
		ivChatToFavourites.setBackground(getResources().getDrawable(
				R.drawable.user_favorite_selected));
		ivChatToContacts.setBackground(getResources().getDrawable(
				R.drawable.user_contacts));
		rlChatToRecents.setOnClickListener(this);
		rlChatUserStatus.setOnClickListener(this);
		rlChatUserSettings.setOnClickListener(this);
		rlChatToContacts.setOnClickListener(this);
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
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		MyNetApplication.activityResumed();
		titleText.setText("Favourite List");
		LoadUserList();
	
	}

	private void LoadFebUserList() {
		MyNetDatabase db = new MyNetDatabase(this);
		db.open();
		UserFamilyMembers UserFamilyMembers = db.GetFebUserList();
		db.close();
		if (UserFamilyMembers.userFamilyMemberList != null
				&& userFamilyMembers.userFamilyMemberList.size() > 0) {
			userGroupListAdapter = new UserGroupFavouritListAdapter(this,
					R.layout.group_item_layout,
					new ArrayList<UserFamilyMember>(
							userFamilyMembers.userFamilyMemberList));
			lvCollaborationDiscussionList.setAdapter(userGroupListAdapter);
		} else {
			lvCollaborationDiscussionList
					.setAdapter(new UserGroupFavouritListAdapter(this,
							R.layout.group_item_layout,
							new ArrayList<UserFamilyMember>()));
		}

	}

	private void LoadUserList() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlChatToRecents) {
			Intent intent = new Intent(this, CollaborationMainFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlChatUserStatus) {
			Intent intent = new Intent(this,
					CollaborationUserStatusActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlChatToContacts) {
			Intent intent = new Intent(this,
					CollaborationDiscussionListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.rlChatUserSettings) {
			Intent intent = new Intent(this, AddGroupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	@Override
	public void showProgressLoader() {
		progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		// progress= ProgressDialog.show(this, "","Processing...", true);
		progress.setCancelable(false);
		progress.setMessage("Processing...");
		progress.setIcon(null);
		progress.show();
	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		/*
		 * IUserManager userManager = new UserManager(); return
		 * userManager.GetUserToAddinMyFavourite();
		 */
		IUserManager userManager = new UserManager();
		if (isCallFromRemoveUserFromFevourite) {
			return userManager.UserRelationShipInactiveById(
					userGroupUnionLongPress.UserNumber,
					CommonValues.getInstance().LoginUser.UserNumber);
		} else {

			if (isCalledFromRemoveFromFavorite) {
				MyNetDatabase db = new MyNetDatabase(this);
				db.open();
				db.updateFavourite(
						Integer.valueOf(userGroupUnionLongPress.UserNumber),
						false);
				db.close();
				userManager.RemoveMyFavouriteUser(String
						.valueOf(userGroupUnionLongPress.UserNumber));
			}
			
			MyNetDatabase db = new MyNetDatabase(this);
			db.open();
			UserFamilyMembers UserFamilyMembers = db.GetFebUserList();
			db.close();
			return UserFamilyMembers;
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (isCallFromRemoveUserFromFevourite) {
				UserRelationship relationship = (UserRelationship) data;
				MyNetDatabase database = new MyNetDatabase(this);
				database.open();
				database.updateUserList(relationship.FriendNumber);
				database.close();
				isCallFromRemoveUserFromFevourite = false;
				LoadFebUserList();
			} else {
				isCalledFromRemoveFromFavorite=false;
				userFamilyMembers = (UserFamilyMembers) data;
				if (userFamilyMembers.userFamilyMemberList != null) {
					userGroupListAdapter = new UserGroupFavouritListAdapter(
							this, R.layout.group_item_layout,
							new ArrayList<UserFamilyMember>(
									userFamilyMembers.userFamilyMemberList));
					lvCollaborationDiscussionList
							.setAdapter(userGroupListAdapter);
				} else {
					lvCollaborationDiscussionList
							.setAdapter(new UserGroupFavouritListAdapter(this,
									R.layout.group_item_layout,
									new ArrayList<UserFamilyMember>()));
				}

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
		UserFamilyMember userGroupUnion = (UserFamilyMember) v.getTag();
		CollaborationDiscussionActivity.selectedUserGroupUnion = new UserGroupUnion();
		CollaborationDiscussionActivity.selectedUserGroupUnion.ID = userGroupUnion.UserNumber;
		CollaborationDiscussionActivity.selectedUserGroupUnion.Name = userGroupUnion.Name;
		CollaborationDiscussionActivity.selectedUserGroupUnion.userOnlinestatus = userGroupUnion.OnlineStatus;
		CollaborationDiscussionActivity.selectedUserGroupUnion.Type = "U";
		Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_longpress_favorite, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.profile) {
			ContactActivity.selectUserGroupUnion = new UserGroupUnion();
			ContactActivity.selectUserGroupUnion.ID = userGroupUnionLongPress.UserNumber;
			ContactActivity.selectUserGroupUnion.Name = userGroupUnionLongPress.Name;
			ContactActivity.selectUserGroupUnion.userOnlinestatus = userGroupUnionLongPress.OnlineStatus;
			ContactActivity.selectUserGroupUnion.Type = "U";
			Intent intent = new Intent(this, ContactActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (item.getItemId() == R.id.communicaiton) {
			CollaborationDiscussionActivity.selectedUserGroupUnion = new UserGroupUnion();
			CollaborationDiscussionActivity.selectedUserGroupUnion.ID = userGroupUnionLongPress.UserNumber;
			CollaborationDiscussionActivity.selectedUserGroupUnion.Name = userGroupUnionLongPress.Name;
			CollaborationDiscussionActivity.selectedUserGroupUnion.userOnlinestatus = userGroupUnionLongPress.OnlineStatus;
			CollaborationDiscussionActivity.selectedUserGroupUnion.Type = "U";
			Intent intent = new Intent(this,
					CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (item.getItemId() == R.id.remove) {
			isCallFromRemoveUserFromFevourite = true;
			showDialog();

		} else if (item.getItemId() == R.id.removefromfavorite) {
			isCalledFromRemoveFromFavorite=true;
			LoadUserList();
		}
		return true;
	}

	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(R.string.app_name)
				.setMessage("Do you confirm to delete?").setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LoadUserList();
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int arg2,
			long arg3) {
		userGroupUnionLongPress = (UserFamilyMember) v.getTag();
		openContextMenu(lvCollaborationDiscussionList);
		return true;
	}

}
