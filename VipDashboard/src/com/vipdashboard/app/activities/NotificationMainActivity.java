package com.vipdashboard.app.activities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.SectionedAdapter;
import com.vipdashboard.app.adapter.UserGroupAllListAdapter;
import com.vipdashboard.app.adapter.UserGroupListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.manager.NotificationManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class NotificationMainActivity extends MainActionbarBase implements
IAsynchronousTask,OnClickListener, OnItemClickListener, OnQueryTextListener, OnCloseListener{
	
RelativeLayout bAddNotification;
	
	ListView lvNotificationUserGroupList;
	
	ProgressBar pbNotificationUserGroupList;
	DownloadableAsyncTask downloadableAsyncTask;
	SearchView svNotificationSearch;
	TextView titleText;
	UserGroupUnions userGroupUnions;
	SectionedAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_main);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
		
		bAddNotification = (RelativeLayout) findViewById(R.id.rlAddNotification);
		
		lvNotificationUserGroupList=(ListView) findViewById(R.id.lvNotificationUserGroupList);
		pbNotificationUserGroupList=(ProgressBar) findViewById(R.id.pbNotificationUserGroupList);
		svNotificationSearch = (SearchView) findViewById(R.id.svNotificationUserGroupList);
		
		AutoCompleteTextView search_text = (AutoCompleteTextView) svNotificationSearch.findViewById(svNotificationSearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.BLACK);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
		
		svNotificationSearch.setOnQueryTextListener(this);
		//svNotificationSearch.setOnCloseListener(this);
		bAddNotification.setOnClickListener(this);
		lvNotificationUserGroupList.setOnItemClickListener(this);
		
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		LoadUserList();
		super.onResume();
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.menu_userstatus, menu);
		return true;
	}*/
	
	
	
	private void LoadUserList() {

		
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.rlAddNotification) {
			CommonValues.getInstance().isCallFromNotification = false;

			Intent intent = new Intent(this,
					SendNotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.svNotificationUserGroupList){
			titleText.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void showProgressLoader() {
		pbNotificationUserGroupList.setVisibility(View.VISIBLE);		
	}

	@Override
	public void hideProgressLoader() {
		pbNotificationUserGroupList.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		INotificationManager notificationManager=new NotificationManager();
		return notificationManager.GetNotificationGroupUserList();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			try {
			adapter = new SectionedAdapter() {
				@Override
				protected View getHeaderView(String caption, int index, View convertView, ViewGroup parent) {
					convertView = getLayoutInflater().inflate(R.layout.section_header, null);
					TextView header = (TextView) convertView.findViewById(R.id.header);
					header.setText(caption);
					return convertView;
				}
			};			
			
			userGroupUnions=(UserGroupUnions)data;
			setAdapterData(new ArrayList<UserGroupUnion>( userGroupUnions.userGroupUnionList));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
	}
	
	private void setAdapterData(ArrayList<UserGroupUnion> userGroupUnionList){
		try{
			String header="";
			ArrayList<UserGroupUnion> adapterData =null;
			UserGroupAllListAdapter userGroupListAdapter =null;
			if(userGroupUnionList!=null){
				for (UserGroupUnion userGroupUnionRoot : userGroupUnionList) {	
					if(!header.equals(CommonTask.toMessageDateAsString( userGroupUnionRoot.PostedDate))){
						header=CommonTask.toMessageDateAsString(userGroupUnionRoot.PostedDate);
						adapterData=new ArrayList<UserGroupUnion>();
						for (UserGroupUnion userGroupUnion : userGroupUnionList) {	
							if(header.equals(CommonTask.toMessageDateAsString(userGroupUnion.PostedDate))){								
								adapterData.add(userGroupUnion);
							}
						}
						userGroupListAdapter=new UserGroupAllListAdapter(this, R.layout.group_item_lastmessage, adapterData);
						adapter.addSection(header, userGroupListAdapter);
					}
				}
				lvNotificationUserGroupList.setAdapter(adapter);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		NotificationActivity.selectedUserGroupUnion=(UserGroupUnion)v.getTag();		
		Intent intent = new Intent(this, NotificationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
	private void Filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
        List<UserGroupUnion> _userGroupUnionList= new ArrayList<UserGroupUnion>(); 
        if (charText.length() == 0) {
        	_userGroupUnionList=userGroupUnions.userGroupUnionList;
        } else {
            for (UserGroupUnion  wp : userGroupUnions.userGroupUnionList) {
                if (wp.Name.toLowerCase(Locale.getDefault()).contains(charText)) {
                	_userGroupUnionList.add(wp);
                }
            }
        }
        try {
        	adapter.clear();
			setAdapterData(new ArrayList<UserGroupUnion>(_userGroupUnionList));
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onQueryTextChange(String value) {
		Filter(value);
		return true;
	}
	
	@Override
	public boolean onQueryTextSubmit(String value) {
		return false;
	}
	@Override
	public boolean onClose() {
		titleText.setVisibility(View.VISIBLE);
		return false;
	}

}
