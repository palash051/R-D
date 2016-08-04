package com.mobilink.app.activities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.google.android.gms.internal.fa;
import com.mobilink.app.R;
import com.mobilink.app.utils.CommonTask;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class KPI_ReportActivity extends FragmentActivity{
	
	WebView wv;
	TextView tvReportHeader,tvReportSubHeader,tvReportDateFrom,tvReportDateTo,tvReportView;
	ImageView ivReportDateFrom,ivReportDateTo;
	public static String selectedHeader,selectedSubheader,selectedURL;
	Calendar myCalender;
	String finalUrl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kpi_report);
		wv = (WebView) findViewById(R.id.wvReport);
		tvReportHeader = (TextView) findViewById(R.id.tvReportHeader);
		tvReportSubHeader= (TextView) findViewById(R.id.tvReportSubHeader);	
		tvReportDateFrom= (TextView) findViewById(R.id.tvReportDateFrom);	
		tvReportDateTo= (TextView) findViewById(R.id.tvReportDateTo);	
		tvReportView= (TextView) findViewById(R.id.tvReportView);	
		ivReportDateFrom= (ImageView) findViewById(R.id.ivReportDateFrom);	
		ivReportDateTo= (ImageView) findViewById(R.id.ivReportDateTo);	
		ivReportDateFrom.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pickTime(true);
				
			}
		});
		ivReportDateTo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pickTime(false);
				
			}
		});
		
		wv.getSettings().setBuiltInZoomControls(true);		
		myCalender=Calendar.getInstance();
		tvReportView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(tvReportDateFrom.getText().toString().equals("DD-MM-YYYY") || tvReportDateTo.getText().toString().equals("DD-MM-YYYY")){
					Toast.makeText(KPI_ReportActivity.this, "Please select report date", Toast.LENGTH_SHORT).show();
					return;
				}
					
				finalUrl=selectedURL+"|"+tvReportDateFrom.getText().toString().trim()+","+tvReportDateTo.getText().toString().trim();
				LoadReport();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			Toast.makeText(this, "Network connection error.Please check your internet connection.", Toast.LENGTH_SHORT).show();
			return;	
		}
		finalUrl=selectedURL;
		
		LoadReport();
	}

	private void LoadReport() {
		if (!CommonTask.isOnline(this)) {
			Toast.makeText(this, "Network connection error.Please check your internet connection.", Toast.LENGTH_SHORT).show();
			return;	
		}
		tvReportHeader.setText(selectedHeader);
		tvReportSubHeader.setText(selectedSubheader);		
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(getScale());
		wv.loadUrl(finalUrl);
	}
	
	private int getScale(){
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    int width = display.getWidth(); 
	    Double val = new Double(width)/new Double(350);
	    val = val * 100d;
	    return val.intValue();
	}
	
	private void pickTime(boolean isFromDate) {
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
				isFromDate?fromDate:toDate, myCalender.get(Calendar.YEAR),
				myCalender.get(Calendar.MONTH),
				myCalender.get(Calendar.DAY_OF_MONTH));
		Calendar minDate=Calendar.getInstance();		
		minDate.add(Calendar.DAY_OF_YEAR, -1);
		datePickerDialog.getDatePicker().setMaxDate(minDate.getTimeInMillis());
		minDate.add(Calendar.DAY_OF_YEAR, -13);
		datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
		datePickerDialog.setTitle("Select Date");
		datePickerDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.cancel();
			}
		});
		datePickerDialog.show();
		
	}

	public DatePickerDialog.OnDateSetListener fromDate = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			Calendar cal = new GregorianCalendar(years, monthOfYear, dayOfMonth);
			tvReportDateFrom.setText(CommonTask.convertDateToStringWithCustomFormat(cal.getTimeInMillis()));
			
		}
	};
	
	public DatePickerDialog.OnDateSetListener toDate = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			Calendar cal = new GregorianCalendar(years, monthOfYear, dayOfMonth);
			tvReportDateTo.setText(CommonTask.convertDateToStringWithCustomFormat(cal.getTimeInMillis()));
			
		}
	};
}
