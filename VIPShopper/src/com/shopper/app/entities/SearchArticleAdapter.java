package com.shopper.app.entities;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopper.app.R;

/**
 * @author Tanvir Ahmed Chowdhury use for loading search article information in
 *         search screen Asynchronously
 * 
 */

public class SearchArticleAdapter extends ArrayAdapter<ArticleForSearch> {
	ArticleForSearch articleEntity;
	ArrayList<ArticleForSearch> articleInquieryList;
	Context context;
	int _position = -1;
	View oldView = null;
	public SearchArticleAdapter(Context context, int resource,
			ArrayList<ArticleForSearch> _articleInquieryList) {
		super(context, resource, _articleInquieryList);
		this.context = context;
		articleInquieryList = _articleInquieryList;
	}
	static class ViewHolder {
		public TextView text;
		public TextView text2;
		public int pos;
	}
	@Override
	// prepare the search view
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_article, parent,
					false);
//			convertView.setPadding(7, 5, 7, 5);			
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.tvarticleItemText);
			viewHolder.text2 = (TextView) convertView
					.findViewById(R.id.tvsubarticleItemText);
			convertView.setTag(viewHolder);
		}
		articleEntity = (ArticleForSearch) articleInquieryList.get(position);
		convertView.setBackgroundColor(Color.TRANSPARENT);
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.text.setText(String.valueOf(articleEntity.text));
		viewHolder.pos = position;
		
		
		if (articleEntity.text2 != "") {
			viewHolder.text2.setVisibility(View.VISIBLE);
			viewHolder.text2.setText(String.valueOf(articleEntity.text2));
		} else {
			viewHolder.text2.setVisibility(View.GONE);
			viewHolder.text2.setText(String.valueOf(""));
		}
//		if (_position == position) {
//			convertView.setBackgroundResource(R.drawable.selectedrow);
//		}
		convertView.setOnTouchListener(touchListener);
		return convertView;
	}
	public void setSelection(int pos) {
		_position = pos;
	}
	
	private RelativeLayout.OnTouchListener touchListener = new RelativeLayout.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			try {
				if (oldView != null) {
					oldView.setBackgroundResource(R.color.transparent);
				}
				setSelection(((ViewHolder)v.getTag()).pos);
				v.setBackgroundResource(R.drawable.list_pressed);
//				v.setPadding(7, 5, 7, 5);
				oldView = v;
			} catch (Exception e) {
			}
			return false;
		}
	};
}
