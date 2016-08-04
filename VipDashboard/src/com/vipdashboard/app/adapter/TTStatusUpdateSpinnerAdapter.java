package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.TTStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TTStatusUpdateSpinnerAdapter extends BaseAdapter {
	
	Context context;
	TTStatus ttStatus;
	ArrayList<TTStatus> TTStatuslList;
	String Old_ID ;
	
	public TTStatusUpdateSpinnerAdapter(Context _context, int textViewResourceId,
			ArrayList<TTStatus> _lavelList) {
		//super(_context, textViewResourceId, _lavelList);
		context = _context;
		TTStatuslList = _lavelList;
	}

	@Override
	public int getCount() {
		return TTStatuslList.size();
	}

	@Override
	public TTStatus getItem(int position) {
		return TTStatuslList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		ttStatus = TTStatuslList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View ttStatusItemView = inflater.inflate(R.layout.lavel_item_layout, null);
		//CheckedTextView text = (CheckedTextView) lavelItemView.findViewById(R.id.checkedTextView);
		//text.setTag(lavel.LevelID);
		//text.setText(lavel.LevelName);
		TextView lavelText = (TextView) ttStatusItemView.findViewById(R.id.tvLavelList);
		//String name = lavel.LevelName;
		lavelText.setText(ttStatus.Status);
		ttStatusItemView.setTag(ttStatus);
		
		return ttStatusItemView;
	}

}
