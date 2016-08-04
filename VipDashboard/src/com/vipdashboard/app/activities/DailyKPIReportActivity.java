package com.vipdashboard.app.activities;

import com.jjoe64.graphview.LineGraphView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MyNetApplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DailyKPIReportActivity extends Activity{
	public static LineGraphView lineGraphView;
	public static String graph1;
	public static String graph2;
	public static String graph3;
	
	private TextView graphTitle1, graphTitle2, graphTitle3;
	
	//AsyncHttpClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailykpi_report);
		
		//graphTitle1 = (TextView) findViewById(R.id.tvlegend1);
		graphTitle2 = (TextView) findViewById(R.id.tvLegend2);
		graphTitle3 = (TextView) findViewById(R.id.tvLegend3);
		
		graphTitle1.setText(graph1);
		graphTitle2.setText(graph2);
		graphTitle3.setText(graph3);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.dKPIGraph);
		layout.addView(lineGraphView);
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


}
