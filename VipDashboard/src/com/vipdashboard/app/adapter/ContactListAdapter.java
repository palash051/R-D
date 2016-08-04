package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.classes.ContactList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactListAdapter extends ArrayAdapter<ContactList> {
	
	private Context context;
	private ArrayList<ContactList> contactListInformation;
	private ArrayList<ContactList> contactListSearchItemList;
	private ContactList contactList;

	public ContactListAdapter(Context _context, int textViewResourceId,
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
		View contactListItemView = inflater.inflate(R.layout.contact_list_item,null);
		TextView name = (TextView) contactListItemView.findViewById(R.id.tvContactListName);
		TextView number = (TextView) contactListItemView.findViewById(R.id.tvContactListNumber);
		ImageView image = (ImageView) contactListItemView.findViewById(R.id.ivContactListImage);
		
		name.setText(contactList.getName().toString());
		number.setText(contactList.getNumber().toString());
		if(contactList.getPicture() != null){
			Bitmap picture = contactList.getPicture();
			image.setImageBitmap(picture);
		}
		contactListItemView.setTag(contactList);
		return contactListItemView;
	}
	
	public void ContactListFilter(String value){
		value = value.toLowerCase(Locale.getDefault());
		contactListInformation.clear();
		if (value.length() == 0) {
			contactListInformation.addAll(contactListSearchItemList);
		}else{
			for (ContactList contactList : contactListSearchItemList) {
				if(contactList.getName().toLowerCase(Locale.getDefault()).contains(value)){
					contactListInformation.add(contactList);
				}
				if(contactList.getNumber().toLowerCase(Locale.getDefault()).contains(value)){
					contactListInformation.add(contactList);
				}
				notifyDataSetChanged();
			}
		}
	}

}
