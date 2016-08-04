package com.khareeflive.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.LatestUpdateTask;
import com.khareeflive.app.asynchronoustask.NetworkUpdateTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.ArrayOfLatestUpdate;
import com.khareeflive.app.entities.ArrayOfSiteDownGoogleMap;
import com.khareeflive.app.manager.LatestUpdateManager;

public class NetworkUpdateActivity extends Activity{
	
	private TextView networkupdateslist;
	private ArrayOfLatestUpdate latestupdatecollection;
	private NetworkUpdateTask networkupdatetask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.networkupdate);	
		initializeControl();
		
	}
	
	private void initializeControl()
	{
		networkupdateslist=(TextView) findViewById(R.id.ednetworktupdates);
		
		if (networkupdatetask != null) {
			networkupdatetask.cancel(true);
		}			
		networkupdatetask = new NetworkUpdateTask(this);
		networkupdatetask.execute();
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
	
	public Boolean GetData()
	{
		
		try
		{
			LatestUpdateManager lstupdatenews=new LatestUpdateManager();
			latestupdatecollection =new ArrayOfLatestUpdate();
			latestupdatecollection=lstupdatenews.GetNetworkUpdates();
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public void ShowData()
	{
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < latestupdatecollection.latestupdate.size(); i++) {
			sb.append(latestupdatecollection.latestupdate.get(i).UploadedBy + " Update at " 
									+ latestupdatecollection.latestupdate.get(i).UploadDate + " : " 
									+ latestupdatecollection.latestupdate.get(i).Msg +"\r\n\r\n");
		}
		networkupdateslist.setText(sb.toString());
	}

}
