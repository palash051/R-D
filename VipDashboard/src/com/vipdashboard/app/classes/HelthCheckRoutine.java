package com.vipdashboard.app.classes;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;

import android.app.Dialog;
import android.content.Context;
import android.widget.ListView;

public class HelthCheckRoutine {

	private Context context;
	ReportAdapter adapter;
	
	public HelthCheckRoutine(Context _context) {
		this.context = _context;
	}
	
	public void showHelthCheckRoutineList(){
		Dialog dialog = new Dialog(this.context);
		dialog.setTitle("Check Routine");
		dialog.setContentView(R.layout.health_check_routines);
		ListView healthCheckList = (ListView) dialog.findViewById(R.id.lvHelthCheckRoutines);
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		reportList.add(new Report("Run a thorough check",1));
		reportList.add(new Report("Service test",2));
		reportList.add(new Report("App inspector",3));
		reportList.add(new Report("Device checkup",4));
		reportList.add(new Report("Can not make any call",5));
		reportList.add(new Report("Internet is not working",6));
		reportList.add(new Report("Billing issue",7));
		reportList.add(new Report("Search for more",8));
		adapter = new ReportAdapter(this.context, R.layout.lavel_item_layout, reportList);
		healthCheckList.setAdapter(adapter);
		
		dialog.show();
	}
}
