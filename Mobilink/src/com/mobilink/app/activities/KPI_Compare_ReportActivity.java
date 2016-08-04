package com.mobilink.app.activities;

import com.mobilink.app.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

public class KPI_Compare_ReportActivity extends FragmentActivity{
	
	WebView wv;
	TextView tvCompareFrom,tvCompareTo,tvReportSubHeader;
	public static String compareFrom,compareTo,selectedSubheader,selectedURL;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kpi_compare_report);
		wv = (WebView) findViewById(R.id.wvReport);
		tvCompareFrom = (TextView) findViewById(R.id.tvCompareFrom);
		tvCompareTo = (TextView) findViewById(R.id.tvCompareTo);
		tvReportSubHeader= (TextView) findViewById(R.id.tvReportSubHeader);		
		wv.getSettings().setBuiltInZoomControls(true);		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LoadReport();
	}

	private void LoadReport() {
		
		tvCompareFrom.setText(compareFrom);
		tvCompareTo.setText(compareTo.length()>20?compareTo.substring(20)+"...":compareTo);
		tvReportSubHeader.setText(selectedSubheader);		
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setInitialScale(getScale());
		wv.loadUrl(selectedURL);
	}
	
	private int getScale(){
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    int width = display.getWidth(); 
	    Double val = new Double(width)/new Double(350);
	    val = val * 100d;
	    return val.intValue();
	}
}
