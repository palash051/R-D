package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.UserGroup;
import com.vipdashboard.app.utils.CommonTask;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupInformationAdapter extends ArrayAdapter<UserGroup>{
	Context context;
	UserGroup userGroup;
	ArrayList<UserGroup> userGroupList;
	
	
	public GroupInformationAdapter(Context _context, int resource, ArrayList<UserGroup> _objects) {
		super(_context, resource, _objects);
		context = _context;
		userGroupList = _objects;
	}

	@Override
	public int getCount() {
		return userGroupList.size();
	}

	@Override
	public UserGroup getItem(int position) {
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
		
		CheckedTextView ck  = (CheckedTextView) userItemView.findViewById(R.id.usergroupListCheckedTextView);
		ck.setText(userGroup.User.FullName);
		ck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_icon, 0, 0, 0);
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + userGroup.User.Mobile.substring(3) +"%'", null, null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			ck.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			Bitmap bit=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
			if(bit!=null)
				ck.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(context.getResources(),bit), null, null, null);
		}else{
			ck.setText(userGroup.User.FullName);
			
			
		}
		cursor.close();
		
		userItemView.setTag(userGroup);
		return userItemView;
	}

}
