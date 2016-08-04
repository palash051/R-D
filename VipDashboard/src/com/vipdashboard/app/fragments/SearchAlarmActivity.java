package com.vipdashboard.app.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.LiveAlarmListAdapter;
import com.vipdashboard.app.adapter.SearchALarmLavelListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.SearchAlarmItem;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.LiveAlarm;
import com.vipdashboard.app.entities.LiveAlarms;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.manager.NotificationManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;

public class SearchAlarmActivity extends MainActionbarBase implements IAsynchronousTask, TextWatcher, OnClickListener, OnKeyListener, OnItemClickListener {
	
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	AutoCompleteTextView autoCompleteTextView;
	ListView listView;
	int searchAlarmCount,count;
	LiveAlarmListAdapter liveAlarmListAdapter;
	TextView loadMessageText;
	SharedPreferences sharedPreferences;
	
	ArrayList<String> list;
	ArrayList<Lavel> adapterData;
	LiveAlarms liveAlarmadapterData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_alarm);
		
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.actvSearchALarms);
		listView = (ListView) findViewById(R.id.lvSearchAlarmItem);
		progressBar = (ProgressBar) findViewById(R.id.pbSearchAlarms);
		loadMessageText = (TextView) findViewById(R.id.tvShowMeMore);
		CommonTask.makeLinkedTextview(this, loadMessageText, loadMessageText.getText().toString());
		list = new ArrayList<String>();
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
		sharedPreferences = getSharedPreferences("searchAlarm", MODE_PRIVATE);
		autoCompleteTextView.addTextChangedListener(this);
		
		//autoCompleteTextView.setOnKeyListener(this);
		//autoCompleteTextView.setImeActionLabel("Go", KeyEvent.KEYCODE_ENTER);
		listView.setOnItemClickListener(this);
		loadMessageText.setOnClickListener(this);
		
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		listView.setVisibility(ListView.GONE);
		loadMessageText.setVisibility(TextView.GONE);
		count = 0;
		searchAlarmCount = (++count)*CommonConstraints.USER_MESSAGE_COUNT;
		LoadAlarmList();
		super.onResume();
	}

	private void LoadAlarmList() {

		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		INotificationManager notificationManager = new NotificationManager();
		return notificationManager.GetSearchAlarmInformation(searchAlarmCount);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			ArrayList<Object> complexData = (ArrayList<Object>) data;
			
			adapterData = (ArrayList<Lavel>) complexData.get(0);
			liveAlarmadapterData = (LiveAlarms) complexData.get(1);
			liveAlarmListAdapter = new LiveAlarmListAdapter(this,
					R.layout.alarm_list_item,new ArrayList<LiveAlarm>(liveAlarmadapterData.liveAlarmList));
			listView.setAdapter(liveAlarmListAdapter);
		}
	}
	
	

	@Override
	public void afterTextChanged(Editable charEditable) {
		if(!autoCompleteTextView.getText().toString().equals("")){	
			String value = autoCompleteTextView.getText().toString();
			for(int i=0;i<liveAlarmadapterData.liveAlarmList.size();i++){
				if(liveAlarmadapterData.liveAlarmList.get(i).TTrequests != null && liveAlarmadapterData.liveAlarmList.get(i).TTrequests.size() > 0){
					if(liveAlarmadapterData.liveAlarmList.get(i).TTrequests.get(0).Department.equals(value)){
						ArrayList<String> containsValue = CommonTask.getContainStringArrayPref(
								this, "searchValue", value);
						if(containsValue != null && containsValue.size()>0){
							for(int j=0;j<containsValue.size();j++){
								if(containsValue.get(j).toString().equals(liveAlarmadapterData.liveAlarmList.get(i).TTrequests.get(0).Department)){
									break;
								}
							}
						}else{
							list.add(autoCompleteTextView.getText().toString());
							CommonTask.setStringArrayPref(this, "searchValue", list);
						}						
					}
				}
			}
			for(int i=0;i<liveAlarmadapterData.liveAlarmList.size();i++){
				if(liveAlarmadapterData.liveAlarmList.get(i).AlarmText.equals(value)){
					ArrayList<String> containsValue = CommonTask.getContainStringArrayPref(
							this, "searchValue", value);
					if(containsValue != null && containsValue.size()>0){
						for(int j=0;j<containsValue.size();j++){
							if(containsValue.get(j).toString().equals(liveAlarmadapterData.liveAlarmList.get(i).AlarmText)){
								break;
							}
						}
					}else{
						list.add(autoCompleteTextView.getText().toString());
						CommonTask.setStringArrayPref(this, "searchValue", list);
					}					
				}
			}
			for(int i=0;i<adapterData.size();i++){
				if(adapterData.get(i).LevelName.equals(value)){
					ArrayList<String> containsValue = CommonTask.getContainStringArrayPref(
							this, "searchValue", value);
					if(containsValue != null && containsValue.size()>0){
						for(int j=0;j<containsValue.size();j++){
							if(containsValue.get(j).toString().equals(adapterData.get(i).LevelName)){
								break;
							}
						}
					}else{
						list.add(autoCompleteTextView.getText().toString());
						CommonTask.setStringArrayPref(this, "searchValue", list);
					}					
				}
			}
			listView.setVisibility(ListView.VISIBLE);
			loadMessageText.setVisibility(TextView.VISIBLE);
			liveAlarmListAdapter.AlarmListFilter(autoCompleteTextView.getText().toString());
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int arg1, int arg2,
			int arg3) {
		
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {		
		if(charSequence.length() > 0){
			ArrayList<String> containsValue = CommonTask.getContainStringArrayPref(
					this, "searchValue", charSequence.toString());
			if(containsValue != null && containsValue.size() > 0){
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_dropdown_item, containsValue);
				autoCompleteTextView.setThreshold(1);				
				autoCompleteTextView.setAdapter(adapter);				
				listView.setVisibility(ListView.VISIBLE);
				loadMessageText.setVisibility(TextView.VISIBLE);
				liveAlarmListAdapter.AlarmListFilter(charSequence.toString());
			}
		}else{
			listView.setVisibility(ListView.GONE);
			loadMessageText.setVisibility(TextView.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.tvShowMeMore){
			count++;
			searchAlarmCount = (count * CommonConstraints.USER_MESSAGE_COUNT);
		}
	}

	@Override
	public boolean onKey(View textView, int id, KeyEvent keyEvent) {
		if(id == KeyEvent.KEYCODE_ENTER){
			String searchCommend = autoCompleteTextView.getText().toString();
			ArrayList<String> containsValue = CommonTask.getContainStringArrayPref(
					this, "searchValue", searchCommend);
			if(containsValue.size() <= 0){
				list.add(searchCommend);
				CommonTask.setStringArrayPref(this, "searchValue", list);
				listView.setVisibility(ListView.VISIBLE);
				liveAlarmListAdapter.AlarmListFilter(searchCommend);
			}else{
				listView.setVisibility(ListView.VISIBLE);
				liveAlarmListAdapter.AlarmListFilter(searchCommend);
			}
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		LiveAlarm liveAlarm=liveAlarmListAdapter.getItemByAlarmId(Integer.parseInt(String.valueOf(v.getTag())));
		if(liveAlarm.TTrequests!=null && liveAlarm.TTrequests.size()>0){
			TTRequestDetailsFragment.TTrequest=liveAlarm.TTrequests.get(0);	

            Intent intent = new Intent(this,TTRequestDetailsFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else{
			Toast.makeText(this, "Trouble Ticket not create yet.", Toast.LENGTH_SHORT).show();
		}
	}
}
