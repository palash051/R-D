package com.vipdashboard.app.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CareIMMessegingActivity;
import com.vipdashboard.app.activities.ContactActivity;
import com.vipdashboard.app.adapter.UserGroupListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
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

public class ChatToContactFragment  extends SherlockFragment implements IAsynchronousTask, OnItemClickListener,OnItemLongClickListener{
	ListView lvImList;
	UserGroupListAdapter userGroupListAdapter;
	UserGroupUnion userGroupLongPress;
	DownloadableAsyncTask downloadableAsyncTask;
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
		LoadUserList();
	}
	public void LoadUserList() {		
		MyNetDatabase db=new MyNetDatabase(getActivity());
		db.open();
		UserGroupUnions userGroupUnions=db.GetUserGroupList();
		db.close();
		if (userGroupUnions.userGroupUnionList != null) {
			userGroupListAdapter = new UserGroupListAdapter(getActivity(),
					R.layout.group_item_layout,
					new ArrayList<UserGroupUnion>(
							userGroupUnions.userGroupUnionList));
		}
		lvImList.setAdapter(userGroupListAdapter);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {	
		UserGroupUnion user=(UserGroupUnion) v.getTag();
		Intent intent = new Intent(getActivity(), CareIMMessegingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("UserID", user.ID);
		intent.putExtra("UserType", user.Type);
		startActivity(intent);
		
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
			getActivity().openContextMenu(lvImList);
			registerForContextMenu(lvImList);
		}
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater =getActivity().getMenuInflater();
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
			Intent intent = new Intent(getActivity(), ContactActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(item.getItemId() == R.id.communicaiton){
			
			Intent intent = new Intent(getActivity(), CareIMMessegingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("UserID", userGroupLongPress.ID);
			intent.putExtra("UserType", "U");
			startActivity(intent);
		}else if(item.getItemId() == R.id.remove){
			showDialog();
		}
		return true;
	}	
	
	public void showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(R.string.app_name).setMessage("Do you confirm to delete?")
				.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (downloadableAsyncTask != null) {
							downloadableAsyncTask.cancel(true);
						}
						downloadableAsyncTask = new DownloadableAsyncTask(ChatToContactFragment.this);
						downloadableAsyncTask.execute();
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
	@Override
	public void showProgressLoader() {
	}
	@Override
	public void hideProgressLoader() {
	}
	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.UserRelationShipInactiveById(userGroupLongPress.ID, CommonValues.getInstance().LoginUser.UserNumber);
	}
	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			UserRelationship relationship = (UserRelationship) data;
			MyNetDatabase database = new MyNetDatabase(getActivity());
			database.updateUserList(relationship.FriendNumber);
			LoadUserList();
		}
		
	}
	
}
