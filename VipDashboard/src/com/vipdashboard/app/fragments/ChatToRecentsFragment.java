package com.vipdashboard.app.fragments;

import java.text.ParseException;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CareIMMessegingActivity;
import com.vipdashboard.app.adapter.SectionedAdapter;
import com.vipdashboard.app.adapter.UserGroupAllListAdapter;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatToRecentsFragment  extends SherlockFragment implements OnItemClickListener{
	ListView lvImList;
	SectionedAdapter adapter;
	UserGroupAllListAdapter userGroupListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root =  (ViewGroup) inflater.inflate(R.layout.chat_to_list_layout, container, false);
		
		lvImList = (ListView)root.findViewById(R.id.lvImList);
		lvImList.setOnItemClickListener(this);
		return root;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadUserListForHistory();
	}
	
	private void loadUserListForHistory(){
		try{
			MyNetDatabase db=new MyNetDatabase(getActivity());
			db.open();
			UserGroupUnions userGroupUnions=db.GetLiveCollaborationsHistory();
			db.close();
			
			if(userGroupUnions!=null && userGroupUnions.userGroupUnionList!=null && userGroupUnions.userGroupUnionList.size()>0){
				adapter = new SectionedAdapter() {
	
					@Override
					protected View getHeaderView(String caption, int index,
							View convertView, ViewGroup parent) {
						convertView =getActivity().getLayoutInflater().inflate(
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
		}catch (Exception e) {
			
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
						userGroupListAdapter = new UserGroupAllListAdapter(getActivity(),R.layout.group_item_lastmessage, adapterData);
						
						adapter.addSection(header, userGroupListAdapter);
					}
				}
			}
			lvImList.setAdapter(adapter);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {	
		UserGroupUnion user=(UserGroupUnion) v.getTag();
		Intent intent = new Intent(getActivity(), CareIMMessegingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("UserID", user.ID);
		intent.putExtra("UserType", user.Type);
		startActivity(intent);
		
	}
}
