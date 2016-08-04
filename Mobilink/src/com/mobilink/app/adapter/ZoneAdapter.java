package com.mobilink.app.adapter;

import java.util.ArrayList;

import com.mobilink.app.R;
import com.mobilink.app.entities.CompareListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;


public class ZoneAdapter extends BaseAdapter {

	Context context;
	ArrayList<CompareListItem> CommpareList;
	CompareListItem ListItem;

	public ZoneAdapter(Context _context, int resourceID,
			ArrayList<CompareListItem> _companySetupList) {
		context = _context;
		CommpareList = _companySetupList;
	}

	@Override
	public int getCount() {
		return CommpareList.size();
	}

	public ArrayList<CompareListItem> getItemList() {
		return CommpareList;
	}
	
	public ArrayList<CompareListItem> getItemListById(int id) {
		ArrayList<CompareListItem> _CommpareList=new ArrayList<CompareListItem>();
		for (CompareListItem item : CommpareList) {
			if(item.ItemId!=id)
				_CommpareList.add(item);
		}
		return _CommpareList;
	}

	@Override
	public CompareListItem getItem(int position) {
		return CommpareList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ListItem = CommpareList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View operatorItem = inflater.inflate(R.layout.city_item_layout,
				null);
		TextView tvCity=(TextView) operatorItem
				.findViewById(R.id.tvCity);		
		tvCity.setText(ListItem.ItemName.toUpperCase());
		operatorItem.setTag(ListItem);
		return operatorItem;
	}

}
