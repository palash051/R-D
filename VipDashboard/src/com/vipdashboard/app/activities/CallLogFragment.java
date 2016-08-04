package com.vipdashboard.app.activities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.ContactsListFragment.IndexedListAdapter;
import com.vipdashboard.app.base.MyNetDatabase;
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
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ListFragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class CallLogFragment extends ListFragment implements
		OnQueryTextListener {

	private OnContactSelectedListener mContactsListener;
	private IndexedListAdapter mAdapter;
	private String mCurrentFilter = null;
	private static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
			CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
			CallLog.Calls.DURATION, CallLog.Calls.DATE, CallLog.Calls.TYPE };
	SearchView svCallLogList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.contact_list_fragment, container, false);
		svCallLogList = (SearchView) root.findViewById(R.id.svCallLogList);
		svCallLogList.setOnQueryTextListener(this);
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

		Uri baseUri;

		if (mCurrentFilter != null) {
			baseUri = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI,
					mCurrentFilter);
		} else {
			baseUri = CallLog.Calls.CONTENT_URI;
		}
		String sortOrder = CallLog.Calls.DATE + " DESC";
		Cursor c = getActivity().getContentResolver().query(baseUri,
				CONTACTS_SUMMARY_PROJECTION, null, null, sortOrder);

		mAdapter = new IndexedListAdapter(getActivity(),
				R.layout.call_dialer_item, c, new String[] {
						CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
						CallLog.Calls.DATE, CallLog.Calls.DURATION },
				new int[] { R.id.tvPhoneNumberName, R.id.tvPhoneNumber,
						R.id.tvPhoneCallDate, R.id.tvPhoneCallDuration });

		setListAdapter(mAdapter);
		getListView().setFastScrollEnabled(true);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mContactsListener = (OnContactSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnContactSelectedListener");
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
			this.layout = layout;
			this.mContext = context;
			this.inflater = LayoutInflater.from(context);
			this.cr = c;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(layout, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			super.bindView(view, context, c);

			TextView tvName = (TextView) view
					.findViewById(R.id.tvPhoneNumberName);
			TextView tvNumber = (TextView) view
					.findViewById(R.id.tvPhoneNumber);
			TextView tvDate = (TextView) view
					.findViewById(R.id.tvPhoneCallDate);
			TextView tvDuration = (TextView) view
					.findViewById(R.id.tvPhoneCallDuration);
			TextView tvdistance = (TextView) view.findViewById(R.id.tvdistance);
			ImageView callTypeImage = (ImageView) view
					.findViewById(R.id.ivCallType);
			ImageView ivPhoneCallerIamge = (ImageView) view
					.findViewById(R.id.ivUserGroup);

			int type = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
			String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
			long date = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
			int duration = c.getInt(c.getColumnIndex(CallLog.Calls.DURATION));
			String name = c.getString(c
					.getColumnIndex(CallLog.Calls.CACHED_NAME));

			tvName.setText(name);
			tvNumber.setText(number);
			
			//Bitmap bits=CommonTask.getContactImage(getActivity(), "01715364824");
			/*Bitmap bits=CommonTask.loadContactPhotoThumbnail(getActivity(), "01715364824");
			
			if(bits!=null){
				ivPhoneCallerIamge.setImageBitmap(bits);
			}else {
				Drawable drawable = getActivity().getResources().getDrawable(
						R.drawable.user_icon);
				Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
				ivPhoneCallerIamge.setImageBitmap(bmp);
			}*/

			int photoId = CommonTask.getContentPhotoId(getActivity(), number);
			if (photoId > 0) {
				// ivPhoneCallerIamge.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,c.getInt(c.getColumnIndex(CallLog.Calls._ID))));
				Bitmap bits = CommonTask.fetchContactImageThumbnail(
						getActivity(), photoId);
				ivPhoneCallerIamge.setImageBitmap(bits);
			} else {
				Drawable drawable = getActivity().getResources().getDrawable(
						R.drawable.user_icon);
				Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
				ivPhoneCallerIamge.setImageBitmap(bmp);
			}

			// ivPhoneCallerIamge.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,c.getInt(c.getColumnIndex(CallLog.Calls._ID))));
			String dateTime = (String) DateUtils.getRelativeTimeSpanString(
					date, new Date().getTime(), 0);
			tvDate.setText(dateTime);
			int min = 0, sec = 0, hour = 0;
			String durationText = "";
			NumberFormat formatter = new DecimalFormat("00");
			sec = duration % 60;
			min = duration / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			durationText = formatter.format(hour) + ":" + formatter.format(min)
					+ ":" + formatter.format(sec);
			tvDuration.setText(durationText);
			if (type == CallLog.Calls.INCOMING_TYPE)
				callTypeImage.setImageResource(R.drawable.call_arrow_received);
			else if (type == CallLog.Calls.OUTGOING_TYPE)
				callTypeImage.setImageResource(R.drawable.call_arrow_dialed);
			else {
				callTypeImage.setImageResource(R.drawable.call_arrow_missed);
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String phoneNumber = null;
		String name = null;
		String[] projection = new String[] { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER };
		final Cursor phoneCursor = getActivity().getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection,
				CallLog.Calls._ID + "=?", new String[] { String.valueOf(id) },
				null);
		if (phoneCursor.moveToFirst() && phoneCursor.isLast()) {
			final int contactNumberColumnIndex = phoneCursor
					.getColumnIndex(CallLog.Calls.NUMBER);
			phoneNumber = phoneCursor.getString(contactNumberColumnIndex);
			name = phoneCursor.getString(phoneCursor
					.getColumnIndex(CallLog.Calls.CACHED_NAME));
		}

		if (phoneNumber != null) {
			mContactsListener.onContactNumberSelected(phoneNumber, name);
		} else {
			mContactsListener.onContactNameSelected(id);
		}
		if (phoneNumber != null)
			phoneNumber = phoneNumber.replace(" ", "");
		Intent intent = new Intent(getActivity(), MakeCallActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("CONTACT_NAME", name);
		intent.putExtra("CONTACT_NUMBER", phoneNumber);
		intent.putExtra("CALL_FROM_CALL_LOG", true);
		startActivity(intent);
	}

	@Override
	public boolean onQueryTextChange(String value) {
		mCurrentFilter = value;
		if (mCurrentFilter.isEmpty())
			mCurrentFilter = null;
		setAdapterValue();
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
}
