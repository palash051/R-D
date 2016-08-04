package com.khareeflive.app.entities;

import java.util.ArrayList;

import com.khareeflive.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NOCRosterDataAdapter extends ArrayAdapter<NOCRoster>{
	Context context;
	NOCRoster nocRoster;
	ArrayList<NOCRoster> nocRosterList;

	public NOCRosterDataAdapter(Context _context, int resource,
			ArrayList<NOCRoster> _nocRosterList) {
		super(_context, resource, _nocRosterList);
		context = _context;
		nocRosterList = _nocRosterList;
	}
	public int getCount() {
		return nocRosterList.size();
	}

	public NOCRoster getItem(int position) {

		return nocRosterList.get(position);
	}

	public long getItemId(int position) {

		return 0;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {

		nocRoster = nocRosterList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.nocrosteritem,
				null);
		TextView tv = (TextView) userItemView.findViewById(R.id.tvNocResource);
		tv.setText(nocRoster.resource);
		tv = (TextView) userItemView.findViewById(R.id.tvNocMobile);
		tv.setText(nocRoster.mobile);
		tv = (TextView) userItemView.findViewById(R.id.tvNocUpdatedDate);
		tv.setText("Updated at: "+nocRoster.updatedDate);
		tv = (TextView) userItemView.findViewById(R.id.tvNocStartTime);
		tv.setText("Start time: "+nocRoster.startTime);
		tv = (TextView) userItemView.findViewById(R.id.tvNocEndTime);
		tv.setText("End time: "+nocRoster.endTime);
		return userItemView;
	}
}
