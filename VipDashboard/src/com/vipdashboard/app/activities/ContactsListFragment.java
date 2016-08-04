/*
* Copyright (C) 2011 by Ngewi Fet <ngewif@gmail.com>
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

package com.vipdashboard.app.activities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SectionIndexer;

public class ContactsListFragment extends ListFragment implements OnQueryTextListener{

	private OnContactSelectedListener mContactsListener;
	private IndexedListAdapter mAdapter;
	private String mCurrentFilter = null;
	public static String searchValue = "";
	
	private static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
			Phone._ID, 
			Phone.DISPLAY_NAME, 
			Phone.HAS_PHONE_NUMBER,
			Phone.NUMBER,
			Phone.PHOTO_ID
	};
	SearchView svContactList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup root =  (ViewGroup) inflater.inflate(R.layout.fragment_contacts_list, container, false);
		svContactList =(SearchView) root.findViewById(R.id.svContactList);
		svContactList.setOnQueryTextListener(this);
		
		return root;
	}
	
	
	@Override
	public void onResume() {
		//MyNetApplication.activityResumed();
		super.onResume();
		searchValue = "";
		setAdapterValue();
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	private void setAdapterValue() {
		//getLoaderManager().initLoader(0, null, this);
		
		Uri baseUri;
		
		if (mCurrentFilter != null) {
            baseUri = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI,
            		mCurrentFilter);
        } else {
            baseUri = Phone.CONTENT_URI;
        }
		
		String selection = "((" + Phone.DISPLAY_NAME + " NOTNULL)" + " AND (" + Phone.NUMBER + " NOTNULL)" + " AND ("
	            + Phone.HAS_PHONE_NUMBER + "=1) AND ("
	            + Phone.DISPLAY_NAME + " != '' )";
		selection = selection + ")";
		String sortOrder = Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		Cursor c = getActivity().getContentResolver().query(baseUri, CONTACTS_SUMMARY_PROJECTION, selection, null, sortOrder);
		//return new CursorLoader(getActivity(), baseUri, CONTACTS_SUMMARY_PROJECTION, selection, null, sortOrder);
		
		mAdapter = new IndexedListAdapter(
				this.getActivity(),
				R.layout.contact_list_item,
				c,
				new String[] {Phone.DISPLAY_NAME, Phone.NUMBER},
				new int[] {R.id.tvContactListName, R.id.tvContactListNumber});
		setListAdapter(mAdapter);
		getListView().setFastScrollEnabled(true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		/* Retrieving the phone numbers in order to see if we have more than one */
		
		
		String phoneNumber = null;
		String name = null;
		
		String[] projection = new String[] {Phone.DISPLAY_NAME, Phone.NUMBER};
    	final Cursor phoneCursor = getActivity().getContentResolver().query(
			Phone.CONTENT_URI,
			projection,
			Phone._ID + "=?",
			new String[]{String.valueOf(id)},
			null);
    	
    	if(phoneCursor.moveToFirst() && phoneCursor.isLast()) {
    		final int contactNumberColumnIndex 	= phoneCursor.getColumnIndex(Phone.NUMBER);    			
   			phoneNumber = phoneCursor.getString(contactNumberColumnIndex);
   			name = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.DISPLAY_NAME));
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
		intent.putExtra("CALL_FROM_CONTACT_LIST", true);
		startActivity(intent);
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
			 
			 TextView tvname = (TextView) view.findViewById(R.id.tvContactListName);
			 TextView tvnumber = (TextView) view.findViewById(R.id.tvContactListNumber);
			 ImageView tvimage = (ImageView) view.findViewById(R.id.icon);
			 
			 String name = c.getString(c.getColumnIndex(Phone.DISPLAY_NAME));
			 String number = c.getString(c.getColumnIndex(Phone.NUMBER));
			 
			 tvname.setText(name);
			 tvnumber.setText(number);
			 
			 int photoId=CommonTask.getContentPhotoId(getActivity(), number);
				if(photoId>0){
					tvimage.setImageBitmap(CommonTask.fetchContactImageThumbnail(getActivity(),photoId));
				}else{
					Drawable drawable = getActivity().getResources().getDrawable(R.drawable.user_icon);
					Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
					tvimage.setImageBitmap(bmp);
				}
		}	
	}

	@Override
	public boolean onQueryTextChange(String value) {
		mAdapter = null;
		if(value.isEmpty())
			mCurrentFilter = null;
		else
			mCurrentFilter = value;
		//getLoaderManager().restartLoader(0, null, this);
		setAdapterValue();
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String value) {
		return false;
	}
}
