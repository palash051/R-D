package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.LavelListAdapter;
import com.vipdashboard.app.adapter.UserGroupListCheckAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.NotificationManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonValues;

public class SendNotificationActivity extends MainActionbarBase implements
		IAsynchronousTask, OnClickListener, OnItemClickListener, OnItemSelectedListener, OnQueryTextListener, OnCloseListener {

	private DownloadableAsyncTask downloadAsync;
	private ProgressBar pdialog;

	/*
	 * private ListView groupList, userList,lView; private Button send_button;
	 * private EditText notification_message; private GroupListCheckAdapter
	 * groupAdapter; private UserListAdapter userAdapter; private
	 * LavelListAdapter lavelAdaptrer;
	 * 
	 * String getUsers = new String(); String getGroups = new String(); int
	 * getLavel_ID = 0;
	 * 
	 * 
	 * //public static boolean sendNotification_group = false; boolean group =
	 * false; private boolean isButtonPressed = false; boolean dialog_open =
	 * false; boolean groupList_first_check = false; boolean
	 * userList_first_check = false;
	 */

	LavelListAdapter lavelListAdapter;
	UserGroupListCheckAdapter userGroupListCheckAdapter;

	boolean groupList_first_check = false;
	String getusergroups = new String();
	Spinner spinner;
	Button bSendNotification;
	EditText messageText;
	ListView userGroupList;
	SearchView searchView;
	TextView titleText;
	private UserGroupUnion selectusergroupUnion;

	boolean lavellist = false, usergroup = false, isButtonPressed = false;
	String lavelID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_notification);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");

		spinner = (Spinner) findViewById(R.id.spinnerList);
		bSendNotification = (Button) findViewById(R.id.bSendMessage);
		messageText = (EditText) findViewById(R.id.etMessageText);
		userGroupList = (ListView) findViewById(R.id.lvChatList);
		searchView = (SearchView) findViewById(R.id.svSendNotification);
		//titleText = (TextView) findViewById(R.id.tvSendNotificationTitle);
		
		AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.BLACK);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
		
		pdialog = (ProgressBar) findViewById(R.id.sendNotificationProcess);

		bSendNotification.setOnClickListener(this);

		

		//searchView.setOnSearchClickListener(this);
		searchView.setOnQueryTextListener(this);
		//searchView.setOnCloseListener(this);
		spinner.setOnItemSelectedListener(this);

		userGroupList.setOnItemClickListener(this);

		/*
		 * send_button = (Button) findViewById(R.id.bSendNotificaiton);
		 * notification_message = (EditText) findViewById(R.id.nMessageText);
		 * groupList = (ListView) findViewById(R.id.nGroupList); userList =
		 * (ListView) findViewById(R.id.UsersList); pdialog = (ProgressBar)
		 * findViewById(R.id.sendNotificationProcess); LoadLavel(); //Load Lavel
		 * Name in Dialog Box
		 * 
		 * send_button.setOnClickListener(this); // Message Send Button OnClick
		 * Event
		 * 
		 * //GroupList for Select Multiple Group
		 * groupList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View view, int
		 * arg2, long arg3) { String id = view.getTag().toString(); Log.e("id",
		 * id); if(!id.equals("")){ if(!groupList_first_check){ getGroups =
		 * getGroups + id; groupList_first_check = true; }else{
		 * if(getGroups.contains(id)){ getGroups = getGroups.replace(id, "");
		 * }else{ getGroups = getGroups + "," + id; } } } if(getGroups.length()
		 * == 1 && getGroups.contains(",")){ getGroups = getGroups.replace(",",
		 * ""); }else if(getGroups.length() > 1 && getGroups.startsWith(",")){
		 * getGroups = getGroups.substring(1); }else if(getGroups.length() > 1
		 * && getGroups.endsWith(",")){ getGroups = getGroups.substring(0,
		 * getGroups.lastIndexOf(",")); }else{ getGroups =
		 * getGroups.replace(",,", ","); } Log.e("pressed value", getGroups); }
		 * });
		 * 
		 * //UserList for select Multiple Users
		 * userList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View view, int
		 * arg2, long arg3) { String id = view.getTag().toString(); Log.e("id",
		 * id); if(!id.equals("")){ if(!userList_first_check){ getUsers =
		 * getUsers + id; userList_first_check = true; }else{
		 * if(getUsers.contains(id)){ getUsers = getUsers.replace(id, "");
		 * }else{ getUsers = getUsers + "," + id; } } } if(getUsers.length() ==
		 * 1 && getUsers.contains(",")){ getUsers = getUsers.replace(",", "");
		 * }else if(getUsers.length() > 1 && getUsers.startsWith(",")){ getUsers
		 * = getUsers.substring(1); }else if(getUsers.length() > 1 &&
		 * getUsers.endsWith(",")){ getUsers = getUsers.substring(0,
		 * getUsers.lastIndexOf(",")); }else{ getUsers = getUsers.replace(",,",
		 * ","); } Log.e("pressed value", getUsers); } });
		 */
		
		DownloadedData();
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
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.menu_userstatus, menu);
		return true;
	}*/

	

	// LoadLavel Method for open Dialogbox and show LavelList
	/*
	 * private void LoadLavel() { final Dialog d = new
	 * Dialog(SendNotificationActivity.this); d.setTitle("Lavel List");
	 * d.setContentView(R.layout.lavel_list);
	 * 
	 * lView = (ListView) d.findViewById(R.id.nLavelList);
	 * lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); Button cancel =
	 * (Button) d.findViewById(R.id.cancel_button); Button ok = (Button)
	 * d.findViewById(R.id.ok_button); dialog_open = true; loadListData();
	 * lView.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> arg0, View view, int
	 * arg2, long arg3) { getLavel_ID =
	 * Integer.parseInt(view.getTag().toString()); } });
	 * ok.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) { dialog_open = false;
	 * d.dismiss(); LoadUsersData(); } }); cancel.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { d.dismiss(); onBackPressed(); }
	 * });
	 * 
	 * d.show(); }
	 */

	// AsyncTask for Load Lavel
	private void DownloadedData() {
		//Log.e("downloadered", "enter");
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}

	// AsyncTask for Load Users
	/*
	 * private void LoadUsersData() { if(downloadAsync != null){
	 * downloadAsync.cancel(true); } downloadAsync = new
	 * DownloadableAsyncTask(this); downloadAsync.execute(); }
	 * 
	 * // AsyncTask for Load Groups private void LoadGroupData() {
	 * if(downloadAsync != null){ downloadAsync.cancel(true); } downloadAsync =
	 * new DownloadableAsyncTask(this); downloadAsync.execute(); }
	 */

	// AsyncTask for Send Message
	private void sendNotificationMessage() {
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}

	@Override
	public void showProgressLoader() {
		//pdialog.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//pdialog.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager usermanager = new UserManager();
		INotificationManager notification = new NotificationManager();
		if(isButtonPressed){
			return notification.SendNotificationMessage(messageText.getText().toString(), 
					lavelID, getusergroups);
		} else {
			if (!lavellist) {
				return notification.GetLevelConfigByCompanyID();
			} else {
				// Log.e("background", "enter");
				return usermanager.GetUserGroupUnionList();
			}
		}
		/*
		 * if(dialog_open){ return notification.GetLevelConfigByCompanyID();
		 * }else{ if(isButtonPressed){ return
		 * notification.SendNotificationMessage
		 * (notification_message.getText().toString(),
		 * String.valueOf(getLavel_ID), getGroups, getUsers); }else{ if(group){
		 * return usermanager.getGroupsByCompanyID(); }else{ return
		 * usermanager.getUsers(); } } }
		 */

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (isButtonPressed) {
				isButtonPressed = false;
				onBackPressed();
			} else {

				if (!lavellist) {
					ArrayList<Lavel> adapterData = (ArrayList<Lavel>) data;
					lavelListAdapter = new LavelListAdapter(this,
							R.layout.label_item_lauout, adapterData);
					/*lavelListAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
					spinner.setAdapter(lavelListAdapter);
				} else {
					//Log.e("usergrouplist", "enter");
					UserGroupUnions userGroupUnions = (UserGroupUnions) data;
					if (userGroupUnions.userGroupUnionList != null) {
						userGroupListCheckAdapter = new UserGroupListCheckAdapter(
								this, R.layout.userlist_item_layout,
								new ArrayList<UserGroupUnion>(
										userGroupUnions.userGroupUnionList));
					} else {
						Log.e("usergrouplist", "data empty");
					}

					userGroupList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					userGroupList.setAdapter(userGroupListCheckAdapter);
					usergroup = true;
				}
			}

			/*
			 * if(dialog_open){ ArrayList<Lavel> adapterData =
			 * (ArrayList<Lavel>) data; lavelAdaptrer = new
			 * LavelListAdapter(this,R.layout.lavel_item_layout, adapterData);
			 * lView.setAdapter(lavelAdaptrer);
			 * 
			 * 
			 * }else{ if(isButtonPressed){ isButtonPressed = false;
			 * onBackPressed(); }else{ if(group){ ArrayList<Group> adapterData =
			 * (ArrayList<Group>) data; groupAdapter = new
			 * GroupListCheckAdapter(this, R.layout.grouplist_item_layout,
			 * adapterData); groupList.setAdapter(groupAdapter); }else{
			 * ArrayList<User> adapterData = (ArrayList<User>) data; userAdapter
			 * = new UserListAdapter(this, R.layout.userlist_item_layout,
			 * adapterData); userList.setAdapter(userAdapter); group = true;
			 * //sendNotification_group = true; LoadGroupData(); } } }
			 */
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.bSendMessage) {
			if (!messageText.getText().toString().equals("")) {
				isButtonPressed = true;
				sendNotificationMessage();
			}
		}else if(id == R.id.svSendNotification){
			titleText.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		selectusergroupUnion = (UserGroupUnion) view.getTag();
		String id = selectusergroupUnion.Type + ","
				+ String.valueOf(selectusergroupUnion.ID);
		if (!id.equals("")) {
			if (!groupList_first_check && getusergroups.isEmpty()) {
				getusergroups = getusergroups + id;
				groupList_first_check = true;
			} else {
				if (getusergroups.contains("|" + id + "|")) {
					getusergroups = getusergroups.replace("|" + id + "|", "|");
				} else if (getusergroups.contains(id + "|")) {
					getusergroups = getusergroups.replace(id + "|", "");
				} else if (getusergroups.contains("|" + id)) {
					getusergroups = getusergroups.replace("|" + id, "");
				} else {
					if ((getusergroups.startsWith("G") || getusergroups.startsWith("U")) && (!getusergroups.contains("|"))
							&& getusergroups.contains(id)) {
						getusergroups = getusergroups.replace(id, "");
						groupList_first_check = false;
					} else {
						getusergroups = getusergroups + "|" + id;
					}
				}
			}
		}
		/*if (getusergroups.length() == 1 && getusergroups.contains("|")) {
			getusergroups = getusergroups.replace("|", "");
		} else if (getusergroups.length() > 1 && getusergroups.startsWith("|")) {
			getusergroups = getusergroups.substring(1);
		} else if (getusergroups.length() > 1 && getusergroups.endsWith("|")) {
			getusergroups = getusergroups.substring(0,
					getusergroups.lastIndexOf("|"));
		} else {
			getusergroups = getusergroups.replace("||", ",");
		}*/
		Log.e("pressed value", getusergroups);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		lavelID = String.valueOf(view.getTag());
		Log.e("ID", lavelID);
		if (!usergroup) {
			lavellist = true;
			DownloadedData();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public boolean onQueryTextChange(String value) {
		userGroupListCheckAdapter.filter(value);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String value) {
		
		return false;
	}

	@Override
	public boolean onClose() {
		titleText.setVisibility(View.VISIBLE);
		return false;
	}
}
