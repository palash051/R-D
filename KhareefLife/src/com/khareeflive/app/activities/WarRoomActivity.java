package com.khareeflive.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.asynchronoustask.LoginAuthenticationTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.entities.LoginAuthentication;
import com.khareeflive.app.entities.UserListAdapter;
import com.khareeflive.app.manager.UserManager;
import com.khareeflive.app.utils.CommonValues;

public class WarRoomActivity extends Activity implements
		IDownloadProcessorActicity, OnItemClickListener {

	int DOWNLOAD_USERLIST = 0;
	int backgroundCaller = -1;
	UserListAdapter userListAdapter;
	ListView lvUserlist;
	DownloadableTask downloadableTask;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlist);
		LoadUserList();		
	}

	private void LoadUserList() {
		backgroundCaller = DOWNLOAD_USERLIST;
		lvUserlist=(ListView)findViewById(R.id.lvUserlist);
		lvUserlist.setOnItemClickListener(this);
		if (downloadableTask != null) {
			downloadableTask.cancel(true);
		}			
		downloadableTask = new DownloadableTask(this);
		downloadableTask.execute();
	}
	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}

	@Override
	public void showProgressLoader() {
		if (backgroundCaller == DOWNLOAD_USERLIST) {

		}
	}

	@Override
	public void hideProgressLoader() {
		if (backgroundCaller == DOWNLOAD_USERLIST) {

		}
	}

	@Override
	public Object doBackgroundDownloadPorcess() {
		if (backgroundCaller == DOWNLOAD_USERLIST) {
			return UserManager.GetUserList();
		}
		return null;
	}

	@Override
	public void processDownloadedData(Object data) {
		if (backgroundCaller == DOWNLOAD_USERLIST) {
			ArrayList<LoginAuthentication > adapterData=(ArrayList<LoginAuthentication>) data;
			for (LoginAuthentication loginAuthentication : adapterData) {
				if(loginAuthentication.userID.trim().toLowerCase().toString().equals(CommonValues.getInstance().LoginUser.userName.trim().toLowerCase().toString())){
					adapterData.remove(loginAuthentication);
					break;
				}
			}
			userListAdapter = new UserListAdapter(this,	R.layout.userlistiteminfo, adapterData);
			lvUserlist.setAdapter(userListAdapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {		
		CommonValues.getInstance().selectedUser=(LoginAuthentication) v.getTag();
		Intent intent = new Intent(this,ChatActivity.class);			
		startActivity(intent);	
	}

}
