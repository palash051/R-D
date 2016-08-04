package com.vipdashboard.app.adapter;

import com.vipdashboard.app.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends BaseAdapter{
	Context context; 
	
	public Integer[] AppImage = {
			   R.drawable.notifications, R.drawable.products, R.drawable.balance, R.drawable.manage_service,
			   R.drawable.my_neqaty, R.drawable.balance_and_bill, R.drawable.nearest_mobility, R.drawable.network,
			   R.drawable.latest_news, R.drawable.manage_others, R.drawable.qible, R.drawable.nequaty
			 };    
	 
	 public String[] AppImageTitle = {
			   "Notifications and Updates", "Products and Service","Balance and Bill",
			   "Manage Service","My Reward point","Credit Transfer", "Nearest Store","Network Problem",
			   "Latest News","Manage Other","Qible Locator","Channel Partner",
			 };
	 
	 public Adapter(Context _conContext){
		  context = _conContext;
		 }
	 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return AppImage.length;
	}

	@Override
	public Object getItem(int p) {
		// TODO Auto-generated method stub
		return AppImage[p];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int  position, View arg1, ViewGroup arg2) {
		 LayoutInflater inflater = (LayoutInflater) context
				    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  View appBaseItemView = inflater.inflate(R.layout.appsbase_griditem_layout, null);
				  
				  
				  ImageView imageView =(ImageView)appBaseItemView.findViewById(R.id.ivAppBaseGridItem);
				        imageView.setBackgroundResource(AppImage[position]);
				        //GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				       // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				       // imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				        TextView textView =(TextView)appBaseItemView.findViewById(R.id.tvAppBaseGridItemTitle);
				        textView.setTextAlignment(4);
				        textView.setText(AppImageTitle[position]);
				        return appBaseItemView;
	}

}
