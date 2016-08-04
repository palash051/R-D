package com.vipdashboard.app.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllCallsAdapter extends ArrayAdapter<PhoneCallInformation> {
	Context context;
	ArrayList<PhoneCallInformation> callInformationList;
	PhoneCallInformation phoneCallInformation;
	ArrayList<PhoneCallInformation> callInformationSearchItem;
	
	public AllCallsAdapter(Context _context, int textViewResourceId,
			ArrayList<PhoneCallInformation> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		callInformationList = _objects;
		callInformationSearchItem = new ArrayList<PhoneCallInformation>();
		callInformationSearchItem.addAll(callInformationList);
	}
	
	@Override
	public int getCount() {
		return callInformationList.size();
	}

	public void setList(ArrayList<PhoneCallInformation> _userGroupUnionList) {

		callInformationList = _userGroupUnionList;
		notifyDataSetChanged();
	}

	@Override
	public PhoneCallInformation getItem(int position) {
		return callInformationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {

		phoneCallInformation = callInformationList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.all_call_item_layout,
				null);
		TextView tvname = (TextView) userItemView.findViewById(R.id.tvPhoneNumberName);
		TextView tvnumber = (TextView) userItemView.findViewById(R.id.tvPhoneNumber);
		TextView tvduration = (TextView) userItemView.findViewById(R.id.tvPhoneCallDuration);
		ImageView ivUserGroup = (ImageView) userItemView.findViewById(R.id.ivUserGroup);
		
		try {
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneCallInformation.Number));
			Cursor cursor = context.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
			if(cursor != null && cursor.getCount() > 0){
				cursor.moveToFirst();
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
				tvname.setText(name);
				cursor.close();
			}else{
				tvname.setText("Unknown");
			}
			
			int photoId=CommonTask.getContentPhotoId(this.context, phoneCallInformation.Number);
			if(photoId>0){
				ivUserGroup.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
			}
			
			tvnumber.setText(phoneCallInformation.Number);
			int min = 0, sec= 0, hour = 0;
			String durationText = "";
			NumberFormat formatter = new DecimalFormat("00");
			sec = phoneCallInformation.DurationInSec % 60;
			min = phoneCallInformation.DurationInSec / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			durationText = formatter.format(hour) + ":"
					+ formatter.format(min) + ":" + formatter.format(sec);
			tvduration.setText(durationText);
			userItemView.setTag(phoneCallInformation);
		} catch (Exception ex) {

		}
		return userItemView;

	}
	
	public void HistrySocialSearchItem(String charText){
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
		                ContactsContract.CommonDataKinds.Phone.NUMBER};

		Cursor people = context.getContentResolver().query(uri, projection, null, null, null);
		charText = charText.toLowerCase(Locale.getDefault());
		callInformationList.clear();
		if (charText.length() == 0) {
			callInformationList.addAll(callInformationSearchItem);
		}else{
			for(PhoneCallInformation phoneCallInformation:callInformationSearchItem){
				if(phoneCallInformation.Number.toLowerCase(Locale.getDefault()).contains(charText)){
					callInformationList.add(phoneCallInformation);
				}else{
					int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
					int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

					people.moveToFirst();
					do {
					    String Name   = people.getString(indexName);
					    String Number = people.getString(indexNumber);
					    if(Name.toLowerCase(Locale.getDefault()).contains(charText)){
					    	if(phoneCallInformation.Number.toLowerCase(Locale.getDefault()).contains(Number)){
								callInformationList.add(phoneCallInformation);
							}
					    }
					} while (people.moveToNext());
				}
				notifyDataSetChanged();
			}
		}
	}

}
