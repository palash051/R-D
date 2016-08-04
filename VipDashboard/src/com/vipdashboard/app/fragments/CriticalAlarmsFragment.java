package com.vipdashboard.app.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.LiveAlarmListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.LiveAlarm;
import com.vipdashboard.app.entities.LiveAlarms;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveAlarmManager;
import com.vipdashboard.app.manager.LiveAlarmManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CriticalAlarmsFragment extends MainActionbarBase implements
		IAsynchronousTask , OnItemClickListener, OnClickListener, OnCloseListener, OnQueryTextListener{
	ListView lvAlarmList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar pbAlarmList;
	LiveAlarmListAdapter liveAlarmListAdapter;
	
	SearchView svAlarmManagement;
	TextView alarmManagerTitleText;
	TextView titleText;
	/*public static Fragment newInstance(Context context) {
		CriticalAlarmsFragment f = new CriticalAlarmsFragment();
		return f;
	}*/

/*	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.critical_alarms,
				container, false);
		pbAlarmList = (ProgressBar) root.findViewById(R.id.pbAlarmList);
		lvAlarmList = (ListView) root.findViewById(R.id.lvAlarmList);
		
		lvAlarmList.setOnItemClickListener(this);
		LoadAlarmList();
		return root;
	}*/
	
	int criticalAlarmCount,count;
	TextView loadMessageCritical;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.critical_alarms);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(1);
		
		pbAlarmList = (ProgressBar) findViewById(R.id.pbAlarmList);
		lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);
		svAlarmManagement = (SearchView) findViewById(R.id.svAlarm);
		alarmManagerTitleText = (TextView) findViewById(R.id.tvAlarmManagerTitle);
		titleText = (TextView) findViewById(R.id.tvCriticalALarmsTitle);
		titleText.setText("Critical Alarms");
		loadMessageCritical = (TextView) findViewById(R.id.tvShowMeMore);
		CommonTask.makeLinkedTextview(this, loadMessageCritical, loadMessageCritical.getText().toString());
		
		AutoCompleteTextView search_text = (AutoCompleteTextView) svAlarmManagement.findViewById(svAlarmManagement.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.BLACK);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
		
		//svAlarmManagement.setOnSearchClickListener(this);
		svAlarmManagement.setOnQueryTextListener(this);
		//svAlarmManagement.setOnCloseListener(this);
		lvAlarmList.setOnItemClickListener(this);
		loadMessageCritical.setOnClickListener(this);
		
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		count = 0;
		criticalAlarmCount = (++count)*CommonConstraints.USER_MESSAGE_COUNT;
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
		pbAlarmList.setVisibility(View.VISIBLE);

	}

	@Override
	public void hideProgressLoader() {
		pbAlarmList.setVisibility(View.GONE);

	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveAlarmManager liveAlarmManager = new LiveAlarmManager();
		return liveAlarmManager.GetCriticalLiveAlarmByCompanyID(criticalAlarmCount);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {

			LiveAlarms adapterData = (LiveAlarms) data;
			liveAlarmListAdapter = new LiveAlarmListAdapter(this,
					R.layout.alarm_list_item,new ArrayList<LiveAlarm>(adapterData.liveAlarmList));
			lvAlarmList.setAdapter(liveAlarmListAdapter);
			if(liveAlarmListAdapter.getCount() > 0){
				/*lvAlarmList.setSelection(liveAlarmListAdapter
						.getCount() - 1);*/
				CommonValues.getInstance().LastAlarmTime = CommonTask.convertJsonDateToLong
						(liveAlarmListAdapter.getLastItem().EventTime);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		LiveAlarm liveAlarm=liveAlarmListAdapter.getItemByAlarmId(Integer.parseInt(String.valueOf(v.getTag())));
		if(liveAlarm.TTrequests!=null && liveAlarm.TTrequests.size()>0){
			TTRequestDetailsFragment.TTrequest=liveAlarm.TTrequests.get(0);	

            Intent intent = new Intent(this,TTRequestDetailsFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			/*Fragment f=TTRequestDetailsFragment.newInstance(getActivity());
			FragmentTransaction trans = getFragmentManager().beginTransaction();			
	        trans.replace(R.id.alarm_fragment_root_id, f);
	        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        trans.addToBackStack(null);
	        //trans.show(f);
	        
	        trans.commit();*/
	       //this.getFragmentManager().executePendingTransactions();
		}else{
			Toast.makeText(this, "Trouble Ticket not create yet.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.svAlarm){
			alarmManagerTitleText.setVisibility(View.GONE);
		}else if(view.getId() == R.id.tvShowMeMore){
			count++;
			criticalAlarmCount = (count)*CommonConstraints.USER_MESSAGE_COUNT;
			LoadAlarmList();
		}
	}

	@Override
	public boolean onClose() {
		alarmManagerTitleText.setVisibility(View.VISIBLE);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String value) {
		liveAlarmListAdapter.AlarmListFilter(value);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
}
