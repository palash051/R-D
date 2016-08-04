package com.mobilink.app.activities;

import java.util.ArrayList;

import com.mobilink.app.R;
import com.mobilink.app.adapter.OperatorAdapter;
import com.mobilink.app.asynchronoustask.DownloadableAsyncTask;
import com.mobilink.app.entities.CompanySetup;
import com.mobilink.app.entities.CompanySetups;
import com.mobilink.app.interfaces.IAsynchronousTask;
import com.mobilink.app.interfaces.ICompanySetUP;
import com.mobilink.app.manager.CompanySetUpManager;
import com.mobilink.app.utils.CommonConstraints;
import com.mobilink.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class MobilinkOperatorActivity extends Activity implements IAsynchronousTask, OnClickListener, OnItemClickListener {
	
	GridView gvOperator;
	TextView tvCompare;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	OperatorAdapter operatorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_operator);
		
		initialization();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LoadOperator();
	}

	private void initialization() {
		gvOperator = (GridView) findViewById(R.id.grid);
		tvCompare = (TextView) findViewById(R.id.tvCompare);
		
		tvCompare.setOnClickListener(this);
		gvOperator.setOnItemClickListener(this);
	}
	
	private void LoadOperator() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		ICompanySetUP companySetUP = new CompanySetUpManager();
		return companySetUP.GetAllOperators();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			CompanySetups companySetups = (CompanySetups) data;
			operatorAdapter = new OperatorAdapter(this, R.layout.operator_item, new ArrayList<CompanySetup>(companySetups.companySetupList));
			gvOperator.setAdapter(operatorAdapter);
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.tvCompare){
			Intent intent = new Intent(this, MobilinkCompareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
		try{
			Object object = view.getTag();
			if(object instanceof CompanySetup){
				CommonValues.getInstance().SelectedCompany = (CompanySetup) object;
				Intent intent = new Intent(this, MobilinkIndividualActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
