package com.vipdashboard.app.activities;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DashboardReportActivity extends MainActionbarBase implements OnClickListener{
	
	Button _2g_graph, _3g_graph, LTE_garph,complex_graph;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_main_layout);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		
		_2g_graph = (Button) findViewById(R.id.b2gReport);
		_3g_graph = (Button) findViewById(R.id.b3gReport);
		LTE_garph = (Button) findViewById(R.id.bLTEReport);
		complex_graph = (Button) findViewById(R.id.bcomplex);
		
		_2g_graph.setOnClickListener(this);
		_3g_graph.setOnClickListener(this);
		LTE_garph.setOnClickListener(this);
		complex_graph.setOnClickListener(this);
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		
	}
	

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.b2gReport){
			Intent intent = new Intent(this, Two_G_GraphActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.b3gReport){
			Intent intent = new Intent(this, Three_G_GraphActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.bLTEReport){
			Intent intent = new Intent(this, LET_GraphActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id == R.id.bcomplex){
			Intent intent = new Intent(this, ComplexGraphActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

}
