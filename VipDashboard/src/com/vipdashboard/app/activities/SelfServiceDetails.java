package com.vipdashboard.app.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.SelfServiceDetailsAdapter;
import com.vipdashboard.app.base.MainActionbarBase;

public class SelfServiceDetails extends MainActionbarBase {
	
	ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_service_details);
		listView = (ListView) findViewById(R.id.lvSelfServiceDetails);
		listView.setAdapter(new SelfServiceDetailsAdapter(this));
	}
}
