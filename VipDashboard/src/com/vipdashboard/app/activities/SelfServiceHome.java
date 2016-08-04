package com.vipdashboard.app.activities;

import android.os.Bundle;
import android.widget.GridView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.Adapter;
import com.vipdashboard.app.base.MainActionbarBase;

public class SelfServiceHome extends MainActionbarBase {
	GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_service_home);
		gridView = (GridView) findViewById(R.id.grid);
		gridView.setAdapter(new Adapter(this));
	}
}
