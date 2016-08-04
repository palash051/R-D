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
import com.khareeflive.app.entities.LatestLteUpdate;
import com.khareeflive.app.manager.LatestUpdateManager;

public class LTEUpdateActivity extends Activity implements IDownloadProcessorActicity{
	ProgressBar pbLteUpdate;
	TextView edLteUpdateUser,edLteUpdateUpdateTime,edLteUpdateMessage;
	DownloadableTask downloadableTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lteupdate);
		initialization();
	}

	private void initialization() {
		pbLteUpdate=(ProgressBar)findViewById(R.id.pbLteUpdate);
		edLteUpdateUser=(TextView)findViewById(R.id.edLteUpdateUser);
		edLteUpdateUpdateTime=(TextView)findViewById(R.id.edLteUpdateUpdateTime);
		edLteUpdateMessage=(TextView)findViewById(R.id.edLteUpdateMessage);
		
		if (downloadableTask != null) {
			downloadableTask.cancel(true);
		}			
		downloadableTask = new DownloadableTask(this);
		downloadableTask.execute();
	}

	@Override
	public void showProgressLoader() {		
		pbLteUpdate.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pbLteUpdate.setVisibility(View.GONE);		
	}

	@Override
	public Object doBackgroundDownloadPorcess() {		
		return LatestUpdateManager.GetLTEUpdate();
	}

	@Override
	public void processDownloadedData(Object data) {
		if(data!=null){
			LatestLteUpdate latestLteUpdate=(LatestLteUpdate) data;
			edLteUpdateUser.setText("Uploaded by : "+latestLteUpdate.uploadedBy);
			edLteUpdateUpdateTime.setText("Uploaded at : "+latestLteUpdate.uploadDate);
			edLteUpdateMessage.setText("Message : "+latestLteUpdate.message);
		}
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
