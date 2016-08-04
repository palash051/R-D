package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.UserGroupUnion;

public class UserGroupItemListAdapter extends ArrayAdapter<UserGroupUnion> {
	
	Context context;
	UserGroupUnion usergroup;
	ArrayList<UserGroupUnion> userGroupUnionList;
	
	public UserGroupItemListAdapter(Context _context, int textViewResourceId,
			ArrayList<UserGroupUnion> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		userGroupUnionList = _objects;
	}
	
	@Override
	public int getCount() {
		return userGroupUnionList.size();
	}

	@Override
	public UserGroupUnion getItem(int position) {

		return userGroupUnionList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}
	
	public void addItem(UserGroupUnion item){
		userGroupUnionList.add(item);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		usergroup = userGroupUnionList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View usergroupItemView = inflater.inflate(
				R.layout.group_item_layout, null);

		TextView tvGroupName = (TextView) usergroupItemView
				.findViewById(R.id.tvGroupName);

		ImageView ivUserGroup = (ImageView) usergroupItemView
				.findViewById(R.id.ivUserGroup);

		tvGroupName.setText(usergroup.Name);
		if (usergroup.Type.equals("G")) {
			ivUserGroup.setImageResource(R.drawable.user_group);
		} else {
			ivUserGroup.setImageResource(R.drawable.user_icon);
		}
		usergroupItemView.setTag(usergroup);
		return usergroupItemView;
	}
}
