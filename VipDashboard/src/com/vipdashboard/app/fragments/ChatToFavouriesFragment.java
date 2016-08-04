package com.vipdashboard.app.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CareIMMessegingActivity;
import com.vipdashboard.app.activities.ContactActivity;
import com.vipdashboard.app.adapter.UserGroupFavouritListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserFamilyMember;
import com.vipdashboard.app.entities.UserFamilyMembers;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserRelationship;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonValues;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ChatToFavouriesFragment  extends SherlockFragment implements OnItemClickListener,OnItemLongClickListener,IAsynchronousTask{
	ListView lvImList;
	UserGroupFavouritListAdapter userGroupListAdapter;
	UserFamilyMember userGroupUnionLongPress;
	DownloadableAsyncTask downloadableAsyncTask;
	boolean isCallFromRemoveUserFromFevourite;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root =  (ViewGroup) inflater.inflate(R.layout.chat_to_list_layout, container, false);
		lvImList = (ListView)root.findViewById(R.id.lvImList);	
		lvImList.setOnItemClickListener(this);
		lvImList.setOnItemLongClickListener(this);
		return root;
	}
	@Override
	public void onResume() {
		super.onResume();
		LoadFebUserList();
	}
	private void LoadFebUserList() {
		MyNetDatabase db = new MyNetDatabase(getActivity());
		db.open();
		UserFamilyMembers userFamilyMembers = db.GetFebUserList();
		db.close();		
		if (userFamilyMembers.userFamilyMemberList != null
				&& userFamilyMembers.userFamilyMemberList.size() > 0 && CommonValues.getInstance().ConatactUserList!=null) {
			userGroupListAdapter = new UserGroupFavouritListAdapter(getActivity(),
					R.layout.group_item_layout,
					new ArrayList<UserFamilyMember>(
							userFamilyMembers.userFamilyMemberList));
		} 
		lvImList.setAdapter(userGroupListAdapter);

	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {	
		UserFamilyMember user=(UserFamilyMember) v.getTag();
		Intent intent = new Intent(getActivity(), CareIMMessegingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("UserID", user.UserNumber);
		intent.putExtra("UserType", "U");
		startActivity(intent);
		
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int arg2,
			long arg3) {
		userGroupUnionLongPress = (UserFamilyMember) v.getTag();
		getActivity().openContextMenu(lvImList);
		registerForContextMenu(lvImList);
		return true;
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
		if (isCallFromRemoveUserFromFevourite) {
			return userManager.UserRelationShipInactiveById(
					userGroupUnionLongPress.UserNumber,
					CommonValues.getInstance().LoginUser.UserNumber);
		}else{
			
			MyNetDatabase db = new MyNetDatabase(getActivity());
			db.updateFavourite(
					Integer.valueOf(userGroupUnionLongPress.UserNumber),
					false);
			userManager.RemoveMyFavouriteUser(String
					.valueOf(userGroupUnionLongPress.UserNumber));
			
			
			UserFamilyMembers UserFamilyMembers = db.GetFebUserList();
			return UserFamilyMembers;
		}
	}
	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (isCallFromRemoveUserFromFevourite) {
				UserRelationship relationship = (UserRelationship) data;
				MyNetDatabase database = new MyNetDatabase(getActivity());
				database.open();
				database.updateUserList(relationship.FriendNumber);
				database.close();
				isCallFromRemoveUserFromFevourite = false;
				LoadFebUserList();
			} else {
				UserFamilyMembers userFamilyMembers = (UserFamilyMembers) data;
				if (userFamilyMembers.userFamilyMemberList != null) {
					userGroupListAdapter = new UserGroupFavouritListAdapter(
							getActivity(), R.layout.group_item_layout,
							new ArrayList<UserFamilyMember>(
									userFamilyMembers.userFamilyMemberList));
					lvImList
							.setAdapter(userGroupListAdapter);
				} else {
					lvImList
							.setAdapter(new UserGroupFavouritListAdapter(getActivity(),
									R.layout.group_item_layout,
									new ArrayList<UserFamilyMember>()));
				}

			}
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
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
			Intent intent = new Intent(getActivity(), ContactActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (item.getItemId() == R.id.communicaiton) {		
			Intent intent = new Intent(getActivity(), CareIMMessegingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("UserID", userGroupUnionLongPress.UserNumber);
			intent.putExtra("UserType", "U");
			startActivity(intent);
		} else if (item.getItemId() == R.id.remove) {	
			isCallFromRemoveUserFromFevourite = true;
			showDialog();

		} else if (item.getItemId() == R.id.removefromfavorite) {
			isCallFromRemoveUserFromFevourite = false;
			LoadUserList();
		}
		return true;
	}

	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
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
	
	private void LoadUserList() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}
}
