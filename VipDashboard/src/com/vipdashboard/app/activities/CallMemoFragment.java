package com.vipdashboard.app.activities;

import java.util.Date;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.CallMemoItemDetails;
import com.vipdashboard.app.utils.CommonTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class CallMemoFragment extends ListFragment implements OnQueryTextListener{
	private OnContactSelectedListener mContactsListener;
	private IndexedListAdapter mAdapter;
	private String mCurrentFilter = null;
	CallMemoItemDetails callMemoItemDetails;
	SearchView svCallMemoList;
	TextView tvMemo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root =  (ViewGroup) inflater.inflate(R.layout.callmemo_fragment, container, false);
		svCallMemoList = (SearchView)root.findViewById(R.id.svCallMemoList);
		svCallMemoList.setOnQueryTextListener(this);
		tvMemo= (TextView)root.findViewById(R.id.tvMemo);

	//	list=(ListView)root.findViewById(list);
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
		MyNetDatabase database = new MyNetDatabase(getActivity());
		try{
			database.open();
			//Cursor c = database.getAllCaLLMemo();
			mAdapter = new IndexedListAdapter(getActivity(),
					R.layout.live_feed_call_memo_item,
					database.getAllCaLLMemo(),
					new String[] {"Number","CallTime","TextCallMemo","LocationName"},
					new int[] {R.id.tvName,R.id.tvDate,R.id.tvCallMemoText,R.id.tvCallMemoLocation});
			database.close();
			setListAdapter(mAdapter);
			getListView().setFastScrollEnabled(true);
			if(mAdapter.getCount()<=0)
			{
				tvMemo.setVisibility(View.VISIBLE);
			}
		}catch (Exception e) {
			e.printStackTrace();
			database.close();
		}finally{
			database.close();
		}
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
	
	class IndexedListAdapter extends SimpleCursorAdapter implements LocationListener, OnClickListener {
		
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
			 
			 TextView tvname = (TextView) view.findViewById(R.id.tvName);
			 TextView tvdate = (TextView) view.findViewById(R.id.tvDate);
			 TextView tvcallMemo = (TextView) view.findViewById(R.id.tvCallMemoText);
			 TextView tvlocation = (TextView) view.findViewById(R.id.tvCallMemoLocation);
			 ImageView ivimage = (ImageView) view.findViewById(R.id.ivLiveFeedImage);
			 ImageView ivvoice = (ImageView) view.findViewById(R.id.ivVoiceMemo);
			 
			 String number = c.getString(1);
			 String textmemo = c.getString(2);
			 String voicepath = c.getString(3);
			 long date = c.getLong(4);
			 String location = c.getString(5);
			 
			 String personname = CommonTask.getContentName(context, number);
			 if(personname.isEmpty()){
				 tvname.setText(number);
			 }else{
				 tvname.setText(personname);
			 }
			 
			 int photoId=CommonTask.getContentPhotoId(getActivity(), number);
				if(photoId>0){
					ivimage.setImageBitmap(CommonTask.fetchContactImageThumbnail(getActivity(),photoId));
				}else{
					Drawable drawable = getActivity().getResources().getDrawable(R.drawable.user_icon);
					Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
					ivimage.setImageBitmap(bmp);
				}
			 
			 String dateTime = (String) DateUtils.getRelativeTimeSpanString(date, new Date().getTime(), 0);
			 tvdate.setText(dateTime);
			 
			 tvcallMemo.setText(textmemo);
			 
			 tvlocation.setText(location);
			 
			 ivvoice.setVisibility(View.GONE);
			 
			 if(voicepath != null) {
				 ivvoice.setVisibility(View.VISIBLE);
				 ivvoice.setTag(voicepath);
				 ivvoice.setOnClickListener(this);
			 }
			else
				ivvoice.setVisibility(View.GONE);

		}

		@Override
		public void onLocationChanged(Location arg0) {
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			
		}

		@Override
		public void onClick(View view) {
			String voicePath = (String) view.getTag();
			Intent intent = new Intent(getActivity(), PlayVoiceMemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("VOICE_PATH", voicePath);
			startActivity(intent);
		}
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

}
