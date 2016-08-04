package com.khareeflive.app.entities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.khareeflive.app.R;

public class GroupUserAdapter extends ArrayAdapter<GroupUser> {
	Context context;
	GroupUser GroupUser;
	ArrayList<GroupUser> groupUserList;

	public GroupUserAdapter(Context _context, int resource,
			ArrayList<GroupUser> _groupUserList) {
		super(_context, resource, _groupUserList);
		context = _context;
		groupUserList = _groupUserList;
	}

	public int getCount() {
		return groupUserList.size();
	}

	public GroupUser getItem(int position) {

		return groupUserList.get(position);
	}

	public long getItemId(int position) {

		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		GroupUser = groupUserList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.userlistiteminfo,
				null);
		TextView headingText = (TextView) userItemView
				.findViewById(R.id.tvUserName);
		headingText.setText(GroupUser.message);
		userItemView.setTag(GroupUser.message);
		return userItemView;
	}

}