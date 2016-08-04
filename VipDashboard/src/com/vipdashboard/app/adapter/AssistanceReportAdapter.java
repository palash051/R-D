package com.vipdashboard.app.adapter;

import com.vipdashboard.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AssistanceReportAdapter extends BaseAdapter{
	Context context; 
	
	public Integer[] AppImage = {
			  R.drawable.call_report,R.drawable.data_report,R.drawable.text_text,R.drawable.device_report,
			  R.drawable.app_report
			 };    
	 
	 public String[] AppImageTitle = {
			   "Call","Data","Text","Device","App"
			 };
	 public AssistanceReportAdapter(Context _conContext){
		  context = _conContext;
		 }
	@Override
	public int getCount() {
		return AppImage.length;
	}

	@Override
	public Object getItem(int p) {
		return AppImage[p];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View appBaseItemView = inflater.inflate(
				R.layout.assistance_item_layout, null);

		ImageView imageView = (ImageView) appBaseItemView
				.findViewById(R.id.Image);
		imageView.setBackgroundResource(AppImage[position]);

		TextView textView = (TextView) appBaseItemView
				.findViewById(R.id.tvfacebooktext);
		textView.setText(AppImageTitle[position]);
		return appBaseItemView;
	}

}
