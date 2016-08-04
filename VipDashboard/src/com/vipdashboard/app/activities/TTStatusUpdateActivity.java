package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.LavelListAdapter;
import com.vipdashboard.app.adapter.TTStatusUpdateSpinnerAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.TTStatu;
import com.vipdashboard.app.entities.TTStatus;
import com.vipdashboard.app.entities.TTStatuses;
import com.vipdashboard.app.entities.TTrequest;
import com.vipdashboard.app.fragments.TTRequestDetailsFragment;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveAlarmManager;
import com.vipdashboard.app.manager.LiveAlarmManager;
import com.vipdashboard.app.utils.CommonValues;

public class TTStatusUpdateActivity extends MainActionbarBase implements OnClickListener, IAsynchronousTask, OnItemSelectedListener {
	
	EditText etcomments, etStatusset;
	Button bupdate;
	Spinner spinner;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar pbTTStatus;
	public static TTrequest TTrequest;
	TTStatusUpdateSpinnerAdapter spinnerAdapter;
	RelativeLayout rlupdate, rlset;
	
	boolean isCallFrombuttonStatus;
	boolean isCallFrombuttonUpdate;
	TTStatuses ttStatuses;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ttstatus_update);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		etcomments = (EditText) findViewById(R.id.etCommentsEditText);
		etStatusset = (EditText) findViewById(R.id.etStatusset);
		spinner = (Spinner) findViewById(R.id.spinnerStatus);
		bupdate = (Button) findViewById(R.id.bTTStatusUpdate);
		pbTTStatus = (ProgressBar) findViewById(R.id.pbTTStatusUpdateList);
		rlupdate = (RelativeLayout) findViewById(R.id.rlTTUpdate);
		rlset = (RelativeLayout) findViewById(R.id.rlTTset);
		
		bupdate.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
	}
	
	

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		if(CommonValues.getInstance().isCallFromTTUpdateSet){
			bupdate.setText("Update");
			rlupdate.setVisibility(RelativeLayout.VISIBLE);
			rlset.setVisibility(RelativeLayout.GONE);
			LoadData();
		}else{
			bupdate.setText("Set Status");
			rlupdate.setVisibility(RelativeLayout.GONE);
			rlset.setVisibility(RelativeLayout.VISIBLE);
		}
		
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.bTTStatusUpdate){
			if(bupdate.getText().toString().equals("Update")){
				isCallFrombuttonUpdate = true;
				Intent intent = new Intent(this,DemoScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else{
				Intent intent = new Intent(this,DemoScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
		}
	}
	
	private void LoadData() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(TTStatusUpdateActivity.this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		pbTTStatus.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pbTTStatus.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveAlarmManager liveAlarmManager = new LiveAlarmManager();
		if(CommonValues.getInstance().isCallFromTTUpdateSet){
			if(isCallFrombuttonUpdate){
				return liveAlarmManager.SetTTComments(String.valueOf(TTrequest.TTCode), etcomments.getText().toString());
			}else{
				return liveAlarmManager.GetTTStatusByTTCode(TTrequest.TTCode);	
			}
		}else if(CommonValues.getInstance().isCallFromTTStatusSet){
			if(isCallFrombuttonStatus){
				return liveAlarmManager.SetTTComments(String.valueOf(TTrequest.TTCode), etcomments.getText().toString());
			}else{
				return liveAlarmManager.GetTTStatusByTTCode(TTrequest.TTCode);
			}
			
		}else{
			return null;
		}
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(CommonValues.getInstance().isCallFromTTUpdateSet){
				ttStatuses = (TTStatuses) data;
				spinnerAdapter = new TTStatusUpdateSpinnerAdapter(this,
						R.layout.lavel_item_layout, new ArrayList<TTStatus>(ttStatuses.TTstatusList));
				spinner.setAdapter(spinnerAdapter);
				CommonValues.getInstance().isCallFromTTStatusSet = false;
			}else if(CommonValues.getInstance().isCallFromTTStatusSet){
				
			}
		}
	}



	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		TTStatus ttStatus = (TTStatus) view.getTag();
		etcomments.setText(ttStatus.Comments);
	}



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	
}
