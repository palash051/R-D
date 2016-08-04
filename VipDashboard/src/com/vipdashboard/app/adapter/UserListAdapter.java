package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.utils.CommonTask;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class UserListAdapter extends ArrayAdapter<UserGroupUnion>{
	
	Context context;
	UserGroupUnion user;
	ArrayList<UserGroupUnion> userList;
	ArrayList<UserGroupUnion> userTempList;
	boolean first_checked = false;

	public UserListAdapter(Context _context, int textViewResourceId,ArrayList<UserGroupUnion> _userlist) {
		super(_context, textViewResourceId, _userlist);
		context = _context;
		userList = _userlist;
		userTempList = new ArrayList<UserGroupUnion>();
		userTempList.addAll(userList);
	}
	
	public int getCount() {
		return userList.size();
	}

	public UserGroupUnion getItem(int position) {

		return userList.get(position);
	}

	public long getItemId(int position) {

		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		user = userList.get(position);		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.userlist_item_layout, null);
		CheckedTextView ckUserList = (CheckedTextView) userItemView.findViewById(R.id.usergroupListCheckedTextView);
		
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + user.Name.substring(3) +"%'", null, null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			ckUserList.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			Bitmap bit=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
			if(bit!=null)
				ckUserList.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(context.getResources(),bit), null, null, null);
			else{
				ckUserList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_icon, 0, 0, 0);
			}
		}else{
			ckUserList.setText(user.Name);
			
		}
		cursor.close();
		userItemView.setTag(user.ID);
		return userItemView;
	}
	
	public void GroupMemberListFilter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		userList.clear();
		if (charText.length() == 0) {
			userList.addAll(userTempList);
		} else {
			for (UserGroupUnion user : userTempList) {
				if(user.Name.toLowerCase(Locale.getDefault()).contains(charText)){
					userList.add(user);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	

}
