package com.vipdashboard.app.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.classes.ContactList;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CareFriendsContactList extends ArrayAdapter<ContactList> implements OnClickListener {
	private Context context;
	private ArrayList<ContactList> contactListInformation;
	private ArrayList<ContactList> contactListSearchItemList;
	private ContactList contactList;

	public CareFriendsContactList(Context _context, int textViewResourceId,
			ArrayList<ContactList> _objects) {
		super(_context, textViewResourceId, _objects);
		this.context = _context;
		this.contactListInformation = _objects;
		contactListSearchItemList = new ArrayList<ContactList>();
		contactListSearchItemList.addAll(contactListInformation);
	}

	@Override
	public int getCount() {
		return this.contactListInformation.size();
	}

	@Override
	public ContactList getItem(int position) {
		return this.contactListInformation.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		contactList = contactListInformation.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contactListItemView = inflater.inflate(R.layout.care_friends_contact,
				null);
		TextView name = (TextView) contactListItemView
				.findViewById(R.id.tvContactListName);
		TextView number = (TextView) contactListItemView
				.findViewById(R.id.tvContactListNumber);
		ImageView image = (ImageView) contactListItemView
				.findViewById(R.id.ivContactListImage);
		ImageView ivInvite = (ImageView)contactListItemView.findViewById(R.id.ivInvite);

		ivInvite.setOnClickListener(this);
		name.setText(contactList.getName().toString());
		number.setText(contactList.getNumber().toString());
		if (contactList.getPicture() != null) {
			Bitmap picture = contactList.getPicture();
			image.setImageBitmap(picture);
		}		
		ivInvite.setTag(contactList.Number);
		contactListItemView.setTag(contactList);
		return contactListItemView;
	}

	public void ContactListFilter(String value) {
		value = value.toLowerCase(Locale.getDefault());
		contactListInformation.clear();
		if (value.length() == 0) {
			contactListInformation.addAll(contactListSearchItemList);
		} else {
			for (ContactList contactList : contactListSearchItemList) {
				if (contactList.getName().toLowerCase(Locale.getDefault())
						.contains(value)) {
					contactListInformation.add(contactList);
				}
				if (contactList.getNumber().toLowerCase(Locale.getDefault())
						.contains(value)) {
					contactListInformation.add(contactList);
				}
				notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivInvite){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			String smsbody = CommonValues.getInstance().LoginUser.FullName + " invited and recommended you to use Care.";
			intent.putExtra("sms_body",smsbody);
			intent.putExtra("address", view.getTag().toString());
			intent.setType("vnd.android-dir/mms-sms"); 
			context.startActivity(intent);
		}
	}
}
