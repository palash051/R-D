package com.vipdashboard.app.fragments;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.AddGroupActivity;
import com.vipdashboard.app.activities.CollaborationDiscussionActivity;
import com.vipdashboard.app.activities.CollaborationDiscussionListActivity;
import com.vipdashboard.app.activities.CollaborationFavouriteList;
import com.vipdashboard.app.activities.CollaborationNewContactActivity;
import com.vipdashboard.app.activities.CollaborationUserStatusActivity;
import com.vipdashboard.app.adapter.SectionedAdapter;
import com.vipdashboard.app.adapter.UserGroupAllListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class CollaborationMainFragment extends MainActionbarBase implements
		IAsynchronousTask, OnClickListener, OnItemClickListener,
		OnQueryTextListener {

	RelativeLayout rlChatToFavourites, rlChatToContacts, rlChatUserStatus,
			rlChatUserSettings;

	ListView lvUserGroupList;
	DownloadableAsyncTask downloadableAsyncTask;

	SearchView svcollaborationSearch;

	SectionedAdapter adapter;
	UserGroupAllListAdapter userGroupListAdapter;
	ProgressDialog progress;
	UserGroupUnions userGroupUnions;
	
	TextView tvNewGroup;
	ImageView ivNewContact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collaboration_main);
		rlChatToFavourites = (RelativeLayout) findViewById(R.id.rlChatToFavourites);
		rlChatToContacts = (RelativeLayout) findViewById(R.id.rlChatToContacts);
		rlChatUserStatus = (RelativeLayout) findViewById(R.id.rlChatUserStatus);
		rlChatUserSettings = (RelativeLayout) findViewById(R.id.rlChatUserSettings);
		tvNewGroup= (TextView) findViewById(R.id.tvNewGroup);
		
		ivNewContact = (ImageView)findViewById(R.id.ivNewContact);

		lvUserGroupList = (ListView) findViewById(R.id.lvUserGroupList);

		svcollaborationSearch = (SearchView) findViewById(R.id.svCollaborationList);
		/*AutoCompleteTextView search_text = (AutoCompleteTextView) svcollaborationSearch
				.findViewById(svcollaborationSearch
						.getContext()
						.getResources()
						.getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.BLACK);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);*/

		svcollaborationSearch.setOnQueryTextListener(this);
		rlChatToFavourites.setOnClickListener(this);
		rlChatToContacts.setOnClickListener(this);
		rlChatUserStatus.setOnClickListener(this);
		rlChatUserSettings.setOnClickListener(this);
		lvUserGroupList.setOnItemClickListener(this);
		tvNewGroup.setOnClickListener(this);
		ivNewContact.setOnClickListener(this);
	}

	@Override
	protected void onPause() {

		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			return;	
		}
		MyNetApplication.activityResumed();
		LoadUserList();
		
	}

	@Override
	public void onBackPressed() {
		backTohome();
	}

	private void LoadUserList() {
		/*if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();*/
			
		try {
			MyNetDatabase db=new MyNetDatabase(this);
			db.open();
			UserGroupUnions userGroupUnions=db.GetLiveCollaborationsHistory();
			db.close();			
			if(userGroupUnions.userGroupUnionList!=null && userGroupUnions.userGroupUnionList.size()>0){
				adapter = new SectionedAdapter() {

					@Override
					protected View getHeaderView(String caption, int index,
							View convertView, ViewGroup parent) {
						convertView = getLayoutInflater().inflate(
								R.layout.section_header, null);
						TextView header = (TextView) convertView
								.findViewById(R.id.header);
						header.setText(caption);
						return convertView;
					}
				};

				if(adapter!=null){
					setAdapterData(new ArrayList<UserGroupUnion>(
						userGroupUnions.userGroupUnionList));
				}
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.rlChatToContacts) {
			CommonValues.getInstance().isCallFromNotification = false;
			Intent intent = new Intent(this,
					CollaborationDiscussionListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (id == R.id.rlChatUserStatus) {
			Intent intent = new Intent(this,
					CollaborationUserStatusActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (id == R.id.rlChatToFavourites) {
			Intent intent = new Intent(this, CollaborationFavouriteList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.rlChatUserSettings){
			Intent intent = new Intent(this,AddGroupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if(id == R.id.tvNewGroup){
			Intent intent = new Intent(this,AddGroupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.ivNewContact){
			Intent intent = new Intent(this,CollaborationNewContactActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		

	}

	@Override
	public void showProgressLoader() {
		progress = ProgressDialog.show(this, "", "Please wait...", true);
		progress.setIcon(null);

	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();

	}

	@Override
	public Object doBackgroundPorcess() {
		/*ILiveCollaborationManager liveCollaborationManager = new LiveCollaborationManager();
		return liveCollaborationManager
				.GetCollaborationGroupUserListByMsgFrom();*/
		MyNetDatabase db=new MyNetDatabase(this);
		db.open();
		UserGroupUnions userGroupUnions=db.GetLiveCollaborationsHistory();
		db.close();
		return userGroupUnions;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			try {
				userGroupUnions = (UserGroupUnions) data;
				if(userGroupUnions!=null && userGroupUnions.userGroupUnionList!=null && userGroupUnions.userGroupUnionList.size()>0){
					adapter = new SectionedAdapter() {
	
						@Override
						protected View getHeaderView(String caption, int index,
								View convertView, ViewGroup parent) {
							convertView = getLayoutInflater().inflate(
									R.layout.section_header, null);
							TextView header = (TextView) convertView
									.findViewById(R.id.header);
							header.setText(caption);
							return convertView;
						}
					};
	
					if(adapter!=null){
						setAdapterData(new ArrayList<UserGroupUnion>(
							userGroupUnions.userGroupUnionList));
					}
				}

			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

	}

	private void setAdapterData(ArrayList<UserGroupUnion> userGroupUnionList)
			throws ParseException {
		ArrayList<UserGroupUnion> adapterData = null;
		String header = "";
		if (userGroupUnionList != null) {
			for (UserGroupUnion userGroupUnionRoot : userGroupUnionList) {
				if (!header.equals(CommonTask.toMessageDateShortFormat(userGroupUnionRoot.PostedDate))) {
					header = CommonTask.toMessageDateShortFormat(userGroupUnionRoot.PostedDate);
					adapterData = new ArrayList<UserGroupUnion>();
					for (UserGroupUnion userGroupUnion : userGroupUnionList) {
						if (header.equals(CommonTask.toMessageDateShortFormat(userGroupUnion.PostedDate))) {
							adapterData.add(userGroupUnion);
						}
					}
					if(adapterData!=null){
						userGroupListAdapter = new UserGroupAllListAdapter(this,R.layout.group_item_lastmessage, adapterData);
						
						adapter.addSection(header, userGroupListAdapter);
					}
				}
			}
			lvUserGroupList.setAdapter(adapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		CollaborationDiscussionActivity.selectedUserGroupUnion = (UserGroupUnion) v
				.getTag();
		Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		List<UserGroupUnion> _userGroupUnionList = new ArrayList<UserGroupUnion>();
		if (charText.length() == 0) {
			_userGroupUnionList = userGroupUnions.userGroupUnionList;
		} else {
			for (UserGroupUnion wp : userGroupUnions.userGroupUnionList) {
				if (wp.Name.toLowerCase(Locale.getDefault()).contains(charText)) {
					_userGroupUnionList.add(wp);
				}
			}
		}
		try {
			adapter.clear();
			setAdapterData(new ArrayList<UserGroupUnion>(_userGroupUnionList));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onQueryTextChange(String value) {
		//filter(value);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}

}
