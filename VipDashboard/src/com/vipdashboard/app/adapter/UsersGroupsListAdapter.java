package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.User;

public class UsersGroupsListAdapter extends ArrayAdapter<Group> {
	Context context;
	Group group;
	ArrayList<Group> userGroupList;
	
	public UsersGroupsListAdapter(Context _context, int resource, ArrayList<Group> arrayList) {
		super(_context, resource, arrayList);
		context = _context;
		userGroupList = arrayList;
	}

	@Override
	public int getCount() {
		return userGroupList.size();
	}

	@Override
	public Group getItem(int position) {
		return userGroupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		group = userGroupList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.userlist_item_layout, null);
		//TextView memberName = (TextView) userItemView.findViewById(R.id.tvGroupName);
		//memberName.setText(userGroup.User.FullName);
		CheckedTextView ck  = (CheckedTextView) userItemView.findViewById(R.id.usergroupListCheckedTextView);
		ck.setText(group.Name);
		ck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_group, 0, 0, 0);
		userItemView.setTag(group);
		return userItemView;
	}
}
