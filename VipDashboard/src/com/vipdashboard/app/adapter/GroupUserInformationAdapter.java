package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.utils.CommonTask;

public class GroupUserInformationAdapter extends ArrayAdapter<User> {
	
	Context context;
	User userGroup;
	Group group;
	ArrayList<User> userGroupList;
	
	public GroupUserInformationAdapter(Context _context, int resource, ArrayList<User> arrayList) {
		super(_context, resource, arrayList);
		context = _context;
		userGroupList = arrayList;
	}

	@Override
	public int getCount() {
		return userGroupList.size();
	}

	@Override
	public User getItem(int position) {
		return userGroupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		userGroup = userGroupList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.userlist_item_layout, null);
		//TextView memberName = (TextView) userItemView.findViewById(R.id.tvGroupName);
		//memberName.setText(userGroup.User.FullName);
		CheckedTextView ck  = (CheckedTextView) userItemView.findViewById(R.id.usergroupListCheckedTextView);
		ck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_icon, 0, 0, 0);
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + userGroup.Mobile.substring(3) +"%'", null, null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			ck.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			Bitmap bit=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
			if(bit!=null)
				ck.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(context.getResources(),bit), null, null, null);
		}else{
			ck.setText(userGroup.FullName);
			
			
		}
		cursor.close();		
		userItemView.setTag(userGroup);
		return userItemView;
	}
}
