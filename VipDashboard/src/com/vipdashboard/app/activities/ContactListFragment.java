package com.vipdashboard.app.activities;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.ContactsListFragment.IndexedListAdapter;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactListFragment extends ListFragment {
	
	private OnContactSelectedListener mContactsListener;
	private IndexedListAdapter mAdapter;
	private String mCurrentFilter = null;
	
	private static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
		Phone._ID, 
		Phone.DISPLAY_NAME, 
		Phone.HAS_PHONE_NUMBER,
		Phone.NUMBER,
		Phone.PHOTO_ID
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root =  (ViewGroup) inflater.inflate(R.layout.contact_list_fragment, container, false);
		return root;
	}
	@Override
	public void onResume() {
		//MyNetApplication.activityResumed();
		super.onResume();
		setAdapterValue();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	private void setAdapterValue() {
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
				R.layout.care_friends_contact,
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
    	phoneNumber = phoneNumber.replace(" ", "");
    	
    	Intent intent = new Intent(Intent.ACTION_VIEW);
		String smsbody = CommonValues.getInstance().LoginUser.FullName + " invited and recommended you to use Care.";
		intent.putExtra("sms_body",smsbody);
		intent.putExtra("address", phoneNumber);
		intent.setType("vnd.android-dir/mms-sms"); 
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
			 ImageView tvimage = (ImageView) view.findViewById(R.id.ivContactListImage);
			 
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

}
