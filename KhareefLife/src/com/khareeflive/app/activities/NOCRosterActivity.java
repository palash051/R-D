package com.khareeflive.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.entities.NOCRoster;
import com.khareeflive.app.entities.NOCRosterDataAdapter;
import com.khareeflive.app.manager.LatestUpdateManager;

public class NOCRosterActivity extends Activity implements IDownloadProcessorActicity{
	ProgressBar pbNOCUpdate;
	
	NOCRosterDataAdapter nocRosterDataAdapter;
	ListView lvNOCRosterlist;
	DownloadableTask downloadableTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nocroster);
		initialization();
		
	}

	private void initialization() {
		pbNOCUpdate=(ProgressBar)findViewById(R.id.pbNOCUpdate);
		lvNOCRosterlist=(ListView)findViewById(R.id.lvNOCRosterlist);
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
		pbNOCUpdate.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void hideProgressLoader() {
		pbNOCUpdate.setVisibility(View.GONE);		
	}

	@Override
	public Object doBackgroundDownloadPorcess() {
		
		return LatestUpdateManager.GetLatestNOCRoster();
	}

	@Override
	public void processDownloadedData(Object data) {
		if(data!=null){
			ArrayList<NOCRoster> adapterData=(ArrayList<NOCRoster>) data;
			nocRosterDataAdapter = new NOCRosterDataAdapter(this,R.layout.nocrosteritem,adapterData);
			lvNOCRosterlist.setAdapter(nocRosterDataAdapter);
		}
		
	}

}
