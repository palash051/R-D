package com.vipdashboard.app.activity.assistance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;

import android.app.Activity;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;


public class AssistanceMainActivity extends Activity implements OnDateChangeListener, IAsynchronousTask{
	
	 
	 /*CalendarView cvNetworkUsageHistory;
	 TextView tvDateTimeViewHeader;
	 long pressedDate;
	 DownloadableAsyncTask downloadableAsyncTask;
	 ProgressBar progressBar;
	 TextView tvNetworkUsageHistoryCallReceived, tvNetworkUsageHistoryCallMade, tvNetworkUsageHistoryCallDroped, tvNetworkUsageHistoryCallSetupFail, tvNetworkUsageHistorySMSSent, tvNetworkUsageHistorySMSReceived,
	 			tvNetworkUsageHistoryMaxStrength, tvNetworkUsageHistoryMinStrength, tvNetworkUsageHistoryAvgStrength, tvNetworkUsageHistoryMaxDuration, tvNetworkUsageHistoryMinDuration, tvNetworkUsageHistoryAvgDuration,
	 			tvNetworkUsageHistoryMaxLatency, tvNetworkUsageHistoryMinLatency, tvNetworkUsageHistoryAvgLatency, tvNetworkUsageHistoryWIFIData, tvNetworkUsageHistoryWIFIDownloadAvgSpeed;
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.network_update_history);
		initialization();*/
		
	}
	 
	 @Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	 
	@Override
	protected void onResume() {
		/*MyNetApplication.activityResumed();	
		String value = DateUtils.formatDateTime(
				AssistanceMainActivity.this, Calendar.getInstance().getTimeInMillis(), 
				DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
		pressedDate = Calendar.getInstance().getTimeInMillis();
		tvDateTimeViewHeader.setText(value + " day average");
		LoadInformation();*/
		super.onResume();
	}

	private void initialization() {
		
		/*
		progressBar = (ProgressBar) findViewById(R.id.pbNetworkUpdateHistory);
		*/
	}

	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
			int dayOfMonth) {
		/*Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
		String value = DateUtils.formatDateTime(
				AssistanceMainActivity.this, cal.getTimeInMillis(), 
				DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
		pressedDate = cal.getTimeInMillis();
		tvDateTimeViewHeader.setText(value + " day average");
		LoadInformation();*/
	}
	
	public void LoadInformation(){
		/*if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();*/
	}

	@Override
	public void showProgressLoader() {
		//progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//progressBar.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		/*MyNetDatabase database = new MyNetDatabase(AssistanceMainActivity.this);
		database.open();
		ArrayList<Object> obj = database.getUsersHistry(pressedDate);
		database.close();*/
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void processDataAfterDownload(Object data) {
		/*if(data != null){
			ArrayList<Object> complexData = (ArrayList<Object>) data;			
			
		}*/
	}
}
