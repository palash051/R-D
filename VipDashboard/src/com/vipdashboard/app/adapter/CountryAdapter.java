package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CountryAdapter extends BaseAdapter{
	Context content;
	ArrayList<Country> countryList;
	Country country;
	public CountryAdapter(Context _context, int textViewResourceId,
			ArrayList<Country> _objects) {
		content = _context;
		countryList = _objects;
	}

	@Override
	public int getCount() {
		return countryList.size();
	}

	@Override
	public Country getItem(int position) {
		return countryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		country = countryList.get(position);
		LayoutInflater inflater = (LayoutInflater) content
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View appBaseItemView = inflater.inflate(R.layout.country_layout, null);
		
		TextView tvCountryName=(TextView)appBaseItemView.findViewById(R.id.tvCountryName);
		tvCountryName.setText(country.CountryName);
		
		appBaseItemView.setTag(country.CountryPhoneCode);
		return appBaseItemView;
	}
}
