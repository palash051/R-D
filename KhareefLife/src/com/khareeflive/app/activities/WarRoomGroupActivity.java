package com.khareeflive.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.ChatGroup;
import com.khareeflive.app.entities.ChatGroupAdapter;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.manager.LatestUpdateManager;
import com.khareeflive.app.utils.CommonValues;

public class WarRoomGroupActivity extends Activity implements
		IDownloadProcessorActicity, OnItemClickListener {

	ChatGroupAdapter chatGroupAdapter;
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

	}

	@Override
	public void hideProgressLoader() {

	}

	@Override
	public Object doBackgroundDownloadPorcess() {

		return LatestUpdateManager.GetChatRoom();

	}

	@Override
	public void processDownloadedData(Object data) {

		ArrayList<ChatGroup> adapterData = (ArrayList<ChatGroup>) data;		
		/*chatGroupAdapter = new ChatGroupAdapter(this, R.layout.userlistiteminfo,adapterData);
		lvUserlist.setAdapter(chatGroupAdapter);*/
		CommonValues.getInstance().selectedGroupName = (String) adapterData.get(0).message;
		Intent intent = new Intent(this, GroupChatActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		/*CommonValues.getInstance().selectedGroupName = (String) v.getTag();
		Intent intent = new Intent(this, GroupChatActivity.class);
		startActivity(intent);*/
	}

}
