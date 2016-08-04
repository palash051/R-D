package com.vipdashboard.app.activities;

import java.util.Date;

import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.CommonTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class SMSFragment extends ListFragment implements OnQueryTextListener{
	
	private OnContactSelectedListener mContactsListener;
	private IndexedListAdapter mAdapter;
	private String mCurrentFilter = null;
	SearchView svSMSList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root =  (ViewGroup) inflater.inflate(R.layout.sms_list_fragment, container, false);
		svSMSList = (SearchView)root.findViewById(R.id.svSMSList);
		svSMSList.setOnQueryTextListener(this);
		return root;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setAdapterValue();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	private void setAdapterValue() {
		
		 Uri uriSMSURI = Uri.parse("content://sms");
	     Cursor c = getActivity().getContentResolver().query(uriSMSURI, null, null, null,null);

		mAdapter = new IndexedListAdapter(getActivity(),
				R.layout.all_call_sms,
				c,
				new String[] {"address","body","date"},
				new int[] {R.id.tvSMSPersonName,R.id.tvSubSMS,R.id.tvSMSDate});

		setListAdapter(mAdapter);
		getListView().setFastScrollEnabled(true);

	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);		
		try {			
			mContactsListener = (OnContactSelectedListener) activity;
		} catch (ClassCastException	e) {
			throw new ClassCastException(activity.toString() + " must implement OnContactSelectedListener");
		}
	}
	
	class IndexedListAdapter extends SimpleCursorAdapter {
		
		private Context mContext;
        private Context appContext;
        private int layout;
        private Cursor cr;
        private final LayoutInflater inflater;
		
		public IndexedListAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to, 0);		
			this.layout=layout;
            this.mContext = context;
            this.inflater=LayoutInflater.from(context);
            this.cr=c;
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(layout, null);
		}
		
		@Override
		public void bindView(View view, Context context, Cursor c) {
			 super.bindView(view, context, c);
			 
			TextView tvnumber = (TextView) view.findViewById(R.id.tvSMSPersonName);
			TextView tvdate = (TextView) view.findViewById(R.id.tvSMSDate);
			TextView tvsubSMS = (TextView) view.findViewById(R.id.tvSubSMS);
			ImageView ivSMSPersonImage=(ImageView) view.findViewById(R.id.ivSMSPersonImage);
			
			String number = c.getString(c.getColumnIndex("address"));
			long date = c.getLong(c.getColumnIndex("date"));
			String sms = c.getString(c.getColumnIndex("body"));
			
			String name = CommonTask.getContentName(getActivity(), number);
			if(name.isEmpty())
				tvnumber.setText(number);
			else
				tvnumber.setText(name);
			
			int photoId=CommonTask.getContentPhotoId(getActivity(), number);
			if(photoId>0){
				ivSMSPersonImage.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
			}else{
				Drawable drawable = getActivity().getResources().getDrawable(R.drawable.user_icon);
				Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
				ivSMSPersonImage.setImageBitmap(bmp);
			}
			
			//ivPhoneCallerIamge.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,c.getInt(c.getColumnIndex(CallLog.Calls._ID))));
			String dateTime = (String) DateUtils.getRelativeTimeSpanString(
					date, new Date().getTime(), 0);
			tvdate.setText(dateTime);
			
			if (sms.length() > 50) {
				String text = sms.substring(0, 50);
				text = text.replaceAll("\n", " ");
				tvsubSMS.setText(text + "...");
			} else {
				tvsubSMS.setText(sms);
			}
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String phoneNumber = null;
		String name = null;
		String[] projection = new String[] {"address"};
		final Cursor phoneCursor = getActivity().getContentResolver().query(
				Uri.parse("content://sms"),
				projection,
				"_id =?",
				new String[]{String.valueOf(id)},
				null);
		if(phoneCursor.moveToFirst() && phoneCursor.isLast()) {
    		//final int contactNumberColumnIndex 	= phoneCursor.getColumnIndex("address");    			
   			phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex("address"));
   			name = CommonTask.getContentName(getActivity(), phoneNumber);
    	}
		
    	if (phoneNumber != null){    		  		
    		mContactsListener.onContactNumberSelected(phoneNumber, name);
    	}
    	else {
    		mContactsListener.onContactNameSelected(id);
    	}
    	if(phoneNumber != null)
    		phoneNumber = phoneNumber.replace(" ", "");
    	Intent intent = new Intent(getActivity(), MakeCallActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("CONTACT_NAME", name);
		intent.putExtra("CONTACT_NUMBER", phoneNumber);
		intent.putExtra("CALL_FROM_SMS", true);
		startActivity(intent);
	}

	@Override
	public boolean onQueryTextChange(String value) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String value) {
		return false;
	}

}
