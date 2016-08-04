package com.khareeflive.app.activities;

import java.util.ArrayList;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.entities.LoginAuthentication;
import com.khareeflive.app.entities.UserListAdapter;
import com.khareeflive.app.manager.UserManager;
import com.khareeflive.app.utils.CommonValues;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DirectoryActivity extends Activity implements
		IDownloadProcessorActicity, OnItemClickListener {

	UserListAdapter userListAdapter;
	ListView lvUserlist;
	DownloadableTask downloadableTask;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlist);
		LoadUserList();
	}

	private void LoadUserList() {

		lvUserlist = (ListView) findViewById(R.id.lvUserlist);
		lvUserlist.setOnItemClickListener(this);
		if (downloadableTask != null) {
			downloadableTask.cancel(true);
		}
		downloadableTask = new DownloadableTask(this);
		downloadableTask.execute();
	}

	@Override
	public void showProgressLoader() {

	}

	@Override
	public void hideProgressLoader() {

	}

	@Override
	public Object doBackgroundDownloadPorcess() {
		return UserManager.GetUserList();	
	}

	@Override
	public void processDownloadedData(Object data) {

		ArrayList<LoginAuthentication> adapterData = (ArrayList<LoginAuthentication>) data;
		for (LoginAuthentication loginAuthentication : adapterData) {
			if (loginAuthentication.userID
					.trim()
					.toLowerCase()
					.toString()
					.equals(CommonValues.getInstance().LoginUser.userName
							.trim().toLowerCase().toString())) {
				adapterData.remove(loginAuthentication);
				break;
			}
		}
		userListAdapter = new UserListAdapter(this, R.layout.userlistiteminfo,
				adapterData);
		lvUserlist.setAdapter(userListAdapter);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		CommonValues.getInstance().selectedUser = (LoginAuthentication) v
				.getTag();
		String number = "tel:" + CommonValues.getInstance().selectedUser.mobile;
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
        startActivity(callIntent);
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
}
