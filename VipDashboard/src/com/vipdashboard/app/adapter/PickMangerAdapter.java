package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.vipdashboard.app.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PickMangerAdapter extends ArrayAdapter<PickItem> {
	
	Context context;
	PickItem pi;
	ArrayList<PickItem>  piList;
	
//	ImageView imageView;
//    TextView txtTitle;
//    TextView txtDesc;

	public PickMangerAdapter(Context context, int textViewResourceId,ArrayList<PickItem> items) {
		super(context, textViewResourceId);
		this.context=context;
		piList=items;
		// TODO Auto-generated constructor stub
	}
	
//	private void ViewHolder() {
//		// TODO Auto-generated method stub
//		ImageView imageView;
//        TextView txtTitle;
//        TextView txtDesc;
//
//	}
	
	 public int getCount() {
	        return piList.size();
	    }
	 
	 public PickItem getItem(int position) {
	        return piList.get(position);
	    }
	 
	    public long getItemId(int position) {
	        return position;
	    }
	 
	
	private class ViewHolder {
		 ImageView imageView;
	        TextView txtTitle;
	        TextView txtDesc;
	    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder ;
        PickItem pickItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
		if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picklayout, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
                 
        holder.txtDesc.setText(pickItem.getDesc());
        holder.txtTitle.setText(pickItem.getTitle());
        holder.imageView.setImageResource(pickItem.getImageId());
         
        return convertView;
    }

}
