package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.utils.CommonValues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class UserGroupListCheckAdapter extends ArrayAdapter<UserGroupUnion> {
	Context context;
	UserGroupUnion usergroup;
	ArrayList<UserGroupUnion> userGroupUnionList;
	ArrayList<UserGroupUnion> userGroupUnion;

	public UserGroupListCheckAdapter(Context _context, int textViewResourceId,
			ArrayList<UserGroupUnion> _userGroupUnionList) {
		super(_context, textViewResourceId, _userGroupUnionList);
		context = _context;
		userGroupUnionList = _userGroupUnionList;
		userGroupUnion = new ArrayList<UserGroupUnion>();
		userGroupUnion.addAll(userGroupUnionList);
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
				R.layout.userlist_item_layout, null);

		/*ImageView ivUserGroup = (ImageView) usergroupItemView
				.findViewById(R.id.ivUserGroup);*/

		CheckedTextView ck = (CheckedTextView) usergroupItemView
				.findViewById(R.id.usergroupListCheckedTextView);
		/*if (usergroup.Type.equals("G")) {
			ivUserGroup.setImageResource(R.drawable.user_group);
		} else {
			ivUserGroup.setImageResource(R.drawable.user_icon);
		}*/
		ck.setText(usergroup.Name.trim());
		if(usergroup.Type.equals("G")){
			ck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_group, 0, 0, 0);
		}else{
			ck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_icon, 0, 0, 0);
		}
		usergroupItemView.setTag(usergroup);
		return usergroupItemView;
	}
	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		userGroupUnionList.clear();
		if (charText.length() == 0) {
			userGroupUnionList.addAll(userGroupUnion);
		} else {
			for (UserGroupUnion wp : userGroupUnion) {
				if(wp.Name != null){
					if (wp.Name.toLowerCase(Locale.getDefault()).contains(charText)) {
						userGroupUnionList.add(wp);
					}
				}
			}
		}
		notifyDataSetChanged();
	}

}
