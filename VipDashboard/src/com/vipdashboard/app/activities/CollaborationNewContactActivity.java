package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CollaborationUserListAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class CollaborationNewContactActivity extends MainActionbarBase implements OnItemClickListener{
	
	ListView lvCollaborationNewContactList;
	CollaborationUserListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collaboration_new_contact);
		
		lvCollaborationNewContactList = (ListView) findViewById(R.id.lvCollaborationNewContactList);
		lvCollaborationNewContactList.setOnItemClickListener(this);
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
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		MyNetApplication.activityResumed();
		LoadUserList();
		
	}

	private void LoadUserList() {
		MyNetDatabase db=new MyNetDatabase(this);
		db.open();
		UserGroupUnions userGroupUnions=db.GetUserLists();
		db.close();
		adapter = new CollaborationUserListAdapter(this,
					R.layout.group_item_layout,
					new ArrayList<UserGroupUnion>(
							userGroupUnions.userGroupUnionList));
		lvCollaborationNewContactList.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int position, long ID) {
		UserGroupUnion userGroup = (UserGroupUnion) v.getTag();
		if(userGroup.Type.equals("U")){
			CollaborationDiscussionActivity.selectedUserGroupUnion = (UserGroupUnion) v
					.getTag();
			Intent intent = new Intent(this, CollaborationDiscussionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

}
