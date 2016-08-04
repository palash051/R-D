package com.shopper.app.entities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopper.app.R;

/**
 * @author Tanvir Ahmed Chowdhury use for loading search group article
 *         information in search screen Asynchronously
 * 
 */

public class SearchGroupAdapter extends ArrayAdapter<Group> {

	Group grpEntity;
	ArrayList<Group> groupInquieryList;
	Context context;
	int _position = -1;
	View oldView = null;

	public SearchGroupAdapter(Context context, int resource,
			ArrayList<Group> _groupInquieryList) {
		super(context, resource, _groupInquieryList);
		this.context = context;
		groupInquieryList = _groupInquieryList;

	}

	static class ViewHolder {
		public TextView text;
		int itemPosition;

	}

	@Override
	// prepare the search view
	public View getView(int position, View convertView, ViewGroup parent) {
		grpEntity = (Group) groupInquieryList.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.search_group, parent, false);
//			convertView.setPadding(7, 5, 7, 5);
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.tvgroupItemText);
			convertView.setTag(viewHolder);
		}

		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.text.setText(String.valueOf(grpEntity.GroupText));
		viewHolder.itemPosition = position;
		convertView.setBackgroundResource(R.color.transparent);
		
		convertView.setOnTouchListener(touchlistener);
		return convertView;
	}

	public void setSelection(int pos) {
		_position = pos;
	}
	RelativeLayout.OnTouchListener touchlistener = new RelativeLayout.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			try {
				if (oldView != null) {
					oldView.setBackgroundResource(R.color.transparent);
				}
				setSelection(((ViewHolder) v.getTag()).itemPosition);
				v.setBackgroundResource(R.drawable.list_pressed);
//				v.setPadding(7, 5, 7, 5);
				oldView = v;

			} catch (Exception e) {

			}
			return false;
		}
	};
	
}
