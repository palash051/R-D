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
import com.khareeflive.app.entities.Latest3GUpdate;
import com.khareeflive.app.manager.LatestUpdateManager;

public class Update3GActivity extends Activity implements IDownloadProcessorActicity{
	ProgressBar pb3GUpdate;
	TextView ed3GUpdateUser,ed3GUpdateUpdateTime,ed3GUpdateMessage;
	DownloadableTask downloadableTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update3g);
		initialization();
	}

	private void initialization() {
		pb3GUpdate=(ProgressBar)findViewById(R.id.pb3GUpdate);
		ed3GUpdateUser=(TextView)findViewById(R.id.ed3GUpdateUser);
		ed3GUpdateUpdateTime=(TextView)findViewById(R.id.ed3GUpdateUpdateTime);
		ed3GUpdateMessage=(TextView)findViewById(R.id.ed3GUpdateMessage);
		
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
		pb3GUpdate.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pb3GUpdate.setVisibility(View.GONE);		
	}

	@Override
	public Object doBackgroundDownloadPorcess() {		
		return LatestUpdateManager.Get3GUpdate();
	}

	@Override
	public void processDownloadedData(Object data) {
		if(data!=null){
			Latest3GUpdate latest3GUpdate=(Latest3GUpdate) data;
			ed3GUpdateUser.setText("Uploaded by : "+latest3GUpdate.uploadedBy);
			ed3GUpdateUpdateTime.setText("Uploaded at : "+latest3GUpdate.uploadDate);
			ed3GUpdateMessage.setText("Message : "+latest3GUpdate.message);
		}
	}

}
