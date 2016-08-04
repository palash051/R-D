package com.shopper.app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopper.app.R;

public class CustomCursorAdapter extends CursorAdapter{

	@SuppressWarnings("deprecation")
	public CustomCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		TextView maintext = (TextView)view.findViewById(R.id.tvarticleItemText);
		maintext.setText(cursor.getString(cursor.getColumnIndex("text")));

		String subtext = cursor.getString(cursor.getColumnIndex("subtitle"));
		if(subtext!=null && !subtext.equals("")){
			TextView summary = (TextView)view.findViewById(R.id.tvsubarticleItemText);
			summary.setText(subtext);
			summary.setVisibility(View.VISIBLE);
		}
		view.setBackgroundResource(R.color.list_item_bg);
	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = inflater.inflate(R.layout.search_article, parent,
				false);
		convertView.setPadding(7, 5, 7, 5);	
		convertView.setOnTouchListener(touchListener);
		convertView.setBackgroundResource(R.color.list_item_bg);
		return convertView;
	}
	View oldView = null;
	RelativeLayout.OnTouchListener touchListener = new RelativeLayout.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			
			if (oldView != null) {
				oldView.setBackgroundResource(R.color.list_item_bg);
			}
			v.setBackgroundResource(R.drawable.list_pressed);
			oldView = v;	
			    return false;
		}
	};


}
