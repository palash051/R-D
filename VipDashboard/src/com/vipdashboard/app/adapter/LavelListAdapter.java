package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Lavel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class LavelListAdapter extends BaseAdapter {
	
	Context context;
	Lavel lavel;
	ArrayList<Lavel> lavelList;
	String Old_ID ;
	

	public LavelListAdapter(Context _context, int textViewResourceId,
			ArrayList<Lavel> _lavelList) {
		//super(_context, textViewResourceId, _lavelList);
		context = _context;
		lavelList = _lavelList;
	}
	
	@Override
	public int getCount() {
		return lavelList.size();
	}

	@Override
	public Lavel getItem(int position) {

		return lavelList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		lavel = lavelList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View lavelItemView = inflater.inflate(R.layout.label_item_lauout, null);
		//CheckedTextView text = (CheckedTextView) lavelItemView.findViewById(R.id.checkedTextView);
		//text.setTag(lavel.LevelID);
		//text.setText(lavel.LevelName);
		TextView lavelText = (TextView) lavelItemView.findViewById(R.id.tvLavelList);
		//String name = lavel.LevelName;
		lavelText.setText(lavel.LevelName.toString());
		lavelItemView.setTag(lavel.LevelID);
		
		return lavelItemView;
	}
}
