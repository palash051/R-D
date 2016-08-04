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

public class MyOrganizaitonMemberAdapter extends ArrayAdapter<UserGroupUnion>{
	Context context;
	UserGroupUnion userGroupUnion;
	ArrayList<UserGroupUnion> userGroupUnionList;

	public MyOrganizaitonMemberAdapter(Context _context, int resource,
			ArrayList<UserGroupUnion> _userGroupUnionList) {
		super(_context, resource, _userGroupUnionList);
		context = _context;
		userGroupUnionList = _userGroupUnionList;
	}
	
	@Override
	public int getCount() {
		return userGroupUnionList.size();
	}
	
	public void setList(ArrayList<UserGroupUnion> _userGroupUnionList){
		
		userGroupUnionList=_userGroupUnionList;
		notifyDataSetChanged();
	}

	@Override
	public UserGroupUnion getItem(int position) {
		return userGroupUnionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {

		userGroupUnion = userGroupUnionList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.memberof_item_layout, null);

		TextView tvGroupName = (TextView) userItemView
				.findViewById(R.id.tvGroupName);

		ImageView ivUserGroup = (ImageView) userItemView
				.findViewById(R.id.ivUserGroup);
		try{
			tvGroupName.setText(userGroupUnion.Name);
			ivUserGroup.setImageResource(R.drawable.user_group);
			userItemView.setTag(userGroupUnion);
		}catch(Exception ex){
			
		}
		return userItemView;
	}

}
