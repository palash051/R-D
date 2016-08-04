package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupListAdapter extends ArrayAdapter<Group> {
	Context context;
	Group group;
	ArrayList<Group> groupList;
	

	public GroupListAdapter(Context _context, int resource,
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
			View groupItemView = inflater.inflate(R.layout.group_item_layout, null);
			TextView tvGroupName = (TextView) groupItemView
					.findViewById(R.id.tvGroupName);
			tvGroupName.setText(group.Name);
			groupItemView.setTag(group.GroupID);
			return groupItemView;
	}
}
