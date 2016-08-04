package com.vipdashboard.app.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.fragments.CollaborationMainFragment;

public class TopContactAdapter extends ArrayAdapter<PhoneCallInformation> implements OnClickListener {
	Context context;
	ArrayList<PhoneCallInformation> callInformationList;
	PhoneCallInformation phoneCallInformation;
	
	public TopContactAdapter(Context _context, int textViewResourceId,
			ArrayList<PhoneCallInformation> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		callInformationList = _objects;
	}
	
	@Override
	public int getCount() {
		return callInformationList.size();
	}

	/*public void setList(ArrayList<PhoneCallInformation> _userGroupUnionList) {

		callInformationList = _userGroupUnionList;
		notifyDataSetChanged();
	}*/

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

		View userItemView = inflater.inflate(R.layout.top_content_item_layout,
				null);
		TextView tvNumber = (TextView) userItemView.findViewById(R.id.tvTopContactPhoneNumber);
		TextView collaboration = (TextView) userItemView.findViewById(R.id.tvExperinceCollaboration);
		TextView dial = (TextView) userItemView.findViewById(R.id.tvExperinceDialer);
		TextView sms = (TextView) userItemView.findViewById(R.id.tvExperinceSMS);
		TextView email = (TextView) userItemView.findViewById(R.id.tvExperinceMail);
		
		collaboration.setOnClickListener(this);
		dial.setOnClickListener(this);
		sms.setOnClickListener(this);
		email.setOnClickListener(this);
		
		try{
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneCallInformation.Number));
			Cursor cursor = context.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
			if(cursor != null && cursor.getCount() > 0){
				cursor.moveToFirst();
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
				tvNumber.setText(name);
				cursor.close();
			}else{
				tvNumber.setText(phoneCallInformation.Number);
			}
		}catch (Exception e) {
			
		}
		userItemView.setTag(phoneCallInformation);
		return userItemView;

	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.tvExperinceCollaboration){
			Intent intent = new Intent(this.context,
					CollaborationMainFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			this.context.startActivity(intent);
		}else if(v.getId() == R.id.tvExperinceDialer){
			Intent calltomanager = new Intent(Intent.ACTION_CALL);
			calltomanager.setData(Uri.parse("tel:" + phoneCallInformation.Number));
			this.context.startActivity(calltomanager);
		}else if(v.getId() == R.id.tvExperinceSMS){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setType("vnd.android-dir/mms-sms"); 
			this.context.startActivity(intent);
		}else if(v.getId() == R.id.tvExperinceMail){
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MyNet");
			emailIntent.setType("message/rfc822");
			this.context.startActivity(Intent.createChooser(emailIntent,"Send"));
		}
	}
}
