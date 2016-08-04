package com.leadership.app.adapter;

import java.util.ArrayList;

import com.leadership.app.R;
import com.leadership.app.entities.City;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class CityCheckedAdapter extends BaseAdapter {

	Context context;
	ArrayList<City> cityList;
	City city;

	public CityCheckedAdapter(Context _context, int resourceID,
			ArrayList<City> _citySetupList) {
		context = _context;
		cityList = _citySetupList;
	}

	@Override
	public int getCount() {
		return cityList.size();
	}

	public ArrayList<City> getItemList() {
		return cityList;
	}
	
	public City getItemById(int id) {
		for (City city : cityList) {
			if(city.CityID==id)
				return city;
		} 
		return null;
	}

	@Override
	public City getItem(int position) {
		return cityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		city = cityList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View operatorItem = inflater.inflate(R.layout.operator_item_layout, null);

		CheckedTextView OperatorName = (CheckedTextView) operatorItem
				.findViewById(R.id.usergroupListCheckedTextView);
		OperatorName.setText(city.CityName.toUpperCase());
		operatorItem.setTag(city);
		return operatorItem;
	}

}
