package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.entities.Lavel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportMainAdapter extends BaseAdapter{
	Context context;
	Report report;
	ArrayList<Report> reportList;
	String Old_ID ;
	

	public ReportMainAdapter(Context _context, int textViewResourceId,
			ArrayList<Report> _lavelList) {
		//super(_context, textViewResourceId, _lavelList);
		context = _context;
		reportList = _lavelList;
	}
	
	@Override
	public int getCount() {
		return reportList.size();
	}

	@Override
	public Report getItem(int position) {

		return reportList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		report = reportList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View lavelItemView = inflater.inflate(R.layout.label_item_lauout, null);
		//CheckedTextView text = (CheckedTextView) lavelItemView.findViewById(R.id.checkedTextView);
		//text.setTag(lavel.LevelID);
		//text.setText(lavel.LevelName);
		TextView lavelText = (TextView) lavelItemView.findViewById(R.id.tvLavelList);
		//String name = lavel.LevelName;
		lavelText.setText(report.getName());
		lavelItemView.setTag(report.getID());
		
		return lavelItemView;
	}
}
