package com.shopper.app.entities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopper.app.R;

/**
 * @author Tanvir Ahmed Chowdhury use for loading search family article
 *         information in search screen
 * 
 */

public class SearchFamilyAdapter extends ArrayAdapter<Department> {

	Department familyInquiery;
	ArrayList<Department> familyInquieryList;
	Context context;
	int _position=-1;
	View oldView = null;

	public SearchFamilyAdapter(Context context, int resource,
			ArrayList<Department> _familyInquieryList) {
		super(context, resource, _familyInquieryList);
		this.context = context;
		familyInquieryList = _familyInquieryList;

	}

	static class ViewHolder {
		TextView text;
		ImageView famImg;
		int itemPositionIndex;
	}

	@Override
	// prepare the search view
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		familyInquiery = (Department) familyInquieryList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_family, parent,false);
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) convertView.findViewById(R.id.tvfamilyItemText);
			viewHolder.famImg = (ImageView) convertView.findViewById(R.id.ivSearchFamilyImage);
			convertView.setTag(viewHolder);
		} 
			
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.itemPositionIndex=position;
		viewHolder.text.setText(String.valueOf(familyInquiery.DeptText));
		setFamilyNomalBackbround(Integer.parseInt(familyInquiery.DeptNo),viewHolder.famImg);
		convertView.setBackgroundResource(R.color.transparent);
		viewHolder.famImg.setTag(familyInquiery.DeptNo);
		
		convertView.setOnTouchListener(touchListener);

		return convertView;
	}

	public void setSelection(int pos) {
		_position = pos;
	}

	private void setFamilyNomalBackbround(int DeptNo, ImageView famImg) {
		switch (DeptNo) {
		case 10:
			famImg.setBackgroundResource(R.drawable.family10);
			break;
		case 25:
			famImg.setBackgroundResource(R.drawable.family25);
			break;
		case 30:
			famImg.setBackgroundResource(R.drawable.family30);
			break;
		case 35:
			famImg.setBackgroundResource(R.drawable.family35);
			break;
		case 40:
			famImg.setBackgroundResource(R.drawable.family40);
			break;
		case 44:
			famImg.setBackgroundResource(R.drawable.family44);
			break;
		case 45:
			famImg.setBackgroundResource(R.drawable.family45);
			break;
		case 50:
			famImg.setBackgroundResource(R.drawable.family50);
			break;
		case 60:
			famImg.setBackgroundResource(R.drawable.family60);
			break;
		case 80:
			famImg.setBackgroundResource(R.drawable.family80);
			break;
		default:
			famImg.setBackgroundResource(R.drawable.family10);
		}
	}

	private void setFamilySelectedBackbround(int DeptNo, ImageView famImg) {
		switch (DeptNo) {
		case 10:
			famImg.setBackgroundResource(R.drawable.selected_family10);
			break;
		case 25:
			famImg.setBackgroundResource(R.drawable.selected_family25);
			break;
		case 30:
			famImg.setBackgroundResource(R.drawable.selected_family30);
			break;
		case 35:
			famImg.setBackgroundResource(R.drawable.selected_family35);
			break;
		case 40:
			famImg.setBackgroundResource(R.drawable.selected_family40);
			break;
		case 44:
			famImg.setBackgroundResource(R.drawable.selected_family44);
			break;
		case 45:
			famImg.setBackgroundResource(R.drawable.selected_family45);
			break;
		case 50:
			famImg.setBackgroundResource(R.drawable.selected_family50);
			break;
		case 60:
			famImg.setBackgroundResource(R.drawable.selected_family60);
			break;
		case 80:
			famImg.setBackgroundResource(R.drawable.selected_family80);
			break;
		default:
			famImg.setBackgroundResource(R.drawable.selected_family10);
		}
	}
	
	RelativeLayout.OnTouchListener touchListener = new RelativeLayout.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			try {
				if (oldView != null) {
					oldView.setBackgroundResource(R.color.transparent);
					ViewHolder vh = (ViewHolder)oldView.getTag();
					setFamilyNomalBackbround(Integer.parseInt(vh.famImg.getTag().toString()), vh.famImg);
				}
				setSelection(((ViewHolder)v.getTag()).itemPositionIndex);
				v.setBackgroundResource(R.drawable.list_pressed);
				ViewHolder vh = (ViewHolder)v.getTag();
				setFamilySelectedBackbround(Integer
						.parseInt(vh.famImg.getTag()
								.toString()),vh.famImg);
				oldView = v;
			} catch (Exception e) {

			}
			
			return false;
		}
	};

}
