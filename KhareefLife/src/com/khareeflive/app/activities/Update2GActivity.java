package com.khareeflive.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.entities.Latest2GUpdate;
import com.khareeflive.app.manager.LatestUpdateManager;

public class Update2GActivity extends Activity implements IDownloadProcessorActicity{
	ProgressBar pb2GUpdate;
	TextView ed2GUpdateUser,ed2GUpdateUpdateTime,ed2GUpdateMessage;
	DownloadableTask downloadableTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update2g);
		initialization();
	}

	private void initialization() {
		pb2GUpdate=(ProgressBar)findViewById(R.id.pb2GUpdate);
		ed2GUpdateUser=(TextView)findViewById(R.id.ed2GUpdateUser);
		ed2GUpdateUpdateTime=(TextView)findViewById(R.id.ed2GUpdateUpdateTime);
		ed2GUpdateMessage=(TextView)findViewById(R.id.ed2GUpdateMessage);
		
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
		pb2GUpdate.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pb2GUpdate.setVisibility(View.GONE);		
	}

	@Override
	public Object doBackgroundDownloadPorcess() {		
		return LatestUpdateManager.Get2GUpdate();
	}

	@Override
	public void processDownloadedData(Object data) {
		if(data!=null){
			Latest2GUpdate latest2GUpdate=(Latest2GUpdate) data;
			ed2GUpdateUser.setText("Uploaded by : "+latest2GUpdate.uploadedBy);
			ed2GUpdateUpdateTime.setText("Uploaded at : "+latest2GUpdate.uploadDate);
			ed2GUpdateMessage.setText("Message : "+latest2GUpdate.message);
		}
	}

}
