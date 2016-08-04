package com.vipdashboard.app.activities;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.interfaces.IAsynchronousTask;

public class DailyKPIActivity extends MainActionbarBase implements IAsynchronousTask{
	
	DownloadableAsyncTask downloadAsync;
	ProgressBar pbGraph;
	ReportAdapter adapter;
	public static String URL;
	public static String name;
	int listPosition;
	WebView wv;
	TextView TitleText;
	AndroidHttpClient client;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_kpi_report);
		wv = (WebView)findViewById(R.id.dwvSpeedometer);		
		wv.getSettings().setBuiltInZoomControls(true);
		pbGraph = (ProgressBar) findViewById(R.id.pbDailyKPI);
		TitleText = (TextView) findViewById(R.id.tvCollaborationMainTitle);
		TitleText.setText(name);
		//LoadGraphData();
		
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
		LoadUrl();
	}
	
	
	private void LoadUrl() {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.loadUrl(URL); 
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			          view.loadUrl(url);
			          return true;
			}});
	}

	private void LoadGraphData(){
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
		
		
	}

	@Override
	public void showProgressLoader() {
		//pbGraph.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//pbGraph.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data == null){
			
			
		}
		
	}
}
