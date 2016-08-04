package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.UserFamilyMember;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserGroupFavouritListAdapter extends BaseAdapter {
	
	Context context;
	UserFamilyMember userGroupUnion;
	ArrayList<UserFamilyMember> userGroupUnionList = null;
	ArrayList<UserFamilyMember> arraylist;
	
	ImageOptions imgOptions;
	
	public UserGroupFavouritListAdapter(Context _context, int resource,
			ArrayList<UserFamilyMember> _userGroupUnionList) {		
		context = _context;
		userGroupUnionList = _userGroupUnionList;
		arraylist = new ArrayList<UserFamilyMember>();
		arraylist.addAll(userGroupUnionList);		
	}

	@Override
	public int getCount() {
		return userGroupUnionList.size();
	}

	@Override
	public UserFamilyMember getItem(int position) {
		return userGroupUnionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		userGroupUnion = userGroupUnionList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.group_item_layout, null);
		
		
		ImageView ivUserGroup = (ImageView)userItemView.findViewById(R.id.ivUserGroup);
		TextView tvGroupName = (TextView)userItemView.findViewById(R.id.tvGroupName);
		ImageView ivUserGroupstatus = (ImageView)userItemView.findViewById(R.id.ivUserGroupstatus);		
		ImageView ivUserFavourites = (ImageView)userItemView.findViewById(R.id.ivUserFavourites);
		
		ContactUser user=CommonValues.getInstance().ConatactUserList.get(userGroupUnion.UserNumber);							
		if(user!=null){
			tvGroupName.setText(user.Name);
			if(user.Image!=null)
				ivUserGroup.setImageBitmap(user.Image);	
			else
				ivUserGroup.setImageResource(R.drawable.user_icon);
		}

		if(userGroupUnion.OnlineStatus == CommonConstraints.OFFLINE){
			ivUserGroupstatus.setImageResource(R.drawable.user_status_offline);
		}else if(userGroupUnion.OnlineStatus == CommonConstraints.AWAY){
			ivUserGroupstatus.setImageResource(R.drawable.user_status_away);
		}else if(userGroupUnion.OnlineStatus == CommonConstraints.ONLINE){
			ivUserGroupstatus.setImageResource(R.drawable.user_status_online);
		}else if(userGroupUnion.OnlineStatus == CommonConstraints.DO_NOT_DISTURB){
			ivUserGroupstatus.setImageResource(R.drawable.user_status_busy);
		}else if(userGroupUnion.OnlineStatus == CommonConstraints.BUSY){
			ivUserGroupstatus.setImageResource(R.drawable.user_status_busy);
		}else{
			ivUserGroupstatus.setImageResource(R.drawable.user_status_offline);
		}
		
		//ivUserGroupstatus.setVisibility(ImageView.GONE);
		ivUserFavourites.setVisibility(ImageView.VISIBLE);
		userItemView.setTag(userGroupUnion);
		return userItemView;
	}

}
