package com.vipdashboard.app.adapter;

import com.vipdashboard.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfServiceDetailsAdapter extends BaseAdapter{
	Context context; 
	
	public Integer[] AppImage = {
			  R.drawable.balance_2, R.drawable.recharge, R.drawable.add_ons,
			   R.drawable.recharge_others, R.drawable.call_setup, R.drawable.rewards
	};    
	 
	 public String[] AppImageTitle = {
			   "Balance","Recharge",
			   "Add-on","Recharge Other Mobile","Call Setup", "Rewords"
	};
	
	public SelfServiceDetailsAdapter(Context _conContext){
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
				R.layout.self_service_adpater_item_layout, null);
		
		ImageView imageView = (ImageView) appBaseItemView
				.findViewById(R.id.ivSelfServiceDetailsImage);
		imageView.setBackgroundResource(AppImage[position]);

		TextView textView = (TextView) appBaseItemView
				.findViewById(R.id.tvSelfServiceDetailsName);
		textView.setText(AppImageTitle[position]);
		return appBaseItemView;
	}

}
