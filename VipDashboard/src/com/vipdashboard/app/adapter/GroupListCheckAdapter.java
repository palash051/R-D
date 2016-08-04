package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class GroupListCheckAdapter extends ArrayAdapter<Group>{
	Context context;
	Group group;
	ArrayList<Group> groupList;
	boolean first_checked = false;
	
	public GroupListCheckAdapter(Context _context, int resource,
			ArrayList<Group> _groupList) {
		super(_context, resource, _groupList);
		context = _context;
		groupList = _groupList;
	}
	
	public int getCount() {
		return groupList.size();
	}

	public Group getItem(int position) {

		return groupList.get(position);
	}

	public long getItemId(int position) {

		return 0;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		group = groupList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View groupItemView = inflater.inflate(R.layout.grouplist_item_layout, null);
		CheckedTextView ckGroupList = (CheckedTextView) groupItemView.findViewById(R.id.groupListCheckedTextView);
		
		ckGroupList.setText(group.Name);
		
		/*TextView tvGroupName = (TextView) groupItemView
				.findViewById(R.id.tvGroupsList);
		CheckBox cb = (CheckBox) groupItemView
				.findViewById(R.id.cbGroupslist);
		cb.setTag(group.GroupID);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String groups=String.valueOf(buttonView.getTag());
				if(isChecked){
					if(SendNotificationActivity.getGroups.indexOf(groups) == -1){
						if(!first_checked){
							SendNotificationActivity.getGroups=SendNotificationActivity.getGroups+groups;
							first_checked = true;
						}else{
							SendNotificationActivity.getGroups=SendNotificationActivity.getGroups+","+groups;
						}
						
					}else{
						SendNotificationActivity.getGroups = "," + SendNotificationActivity.getGroups + groups;
					}
				}else{
					if(SendNotificationActivity.getGroups.indexOf(groups.toString()) > -1){
						SendNotificationActivity.getGroups = SendNotificationActivity.getGroups.replace(groups, ""); 
					}
				}
				if(SendNotificationActivity.getGroups.length() == 1 && SendNotificationActivity.getGroups.contains(",")){
					SendNotificationActivity.getGroups = SendNotificationActivity.getGroups.replace(",", "");
				}else if(SendNotificationActivity.getGroups.length() > 1 && SendNotificationActivity.getGroups.startsWith(",")){
					SendNotificationActivity.getGroups = SendNotificationActivity.getGroups.replace(",", "");
				}else if(SendNotificationActivity.getGroups.length() > 1 && SendNotificationActivity.getGroups.endsWith(",")){
					SendNotificationActivity.getGroups = SendNotificationActivity.getGroups.substring(0, 
							SendNotificationActivity.getGroups.lastIndexOf(","));
				}else
					SendNotificationActivity.getGroups = SendNotificationActivity.getGroups.replace(",,", ",");
				Log.e("GroupList", SendNotificationActivity.getGroups);
			}
		});
		
		tvGroupName.setText(group.Name);*/
		groupItemView.setTag(group.GroupID);
		return groupItemView;
	}
}
