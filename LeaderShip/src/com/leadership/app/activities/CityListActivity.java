package com.leadership.app.activities;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.leadership.app.R;
import com.leadership.app.adapter.CityAdapter;
import com.leadership.app.asynchronoustask.DownloadableAsyncTask;
import com.leadership.app.entities.City;
import com.leadership.app.entities.CityRoot;
import com.leadership.app.interfaces.IAsynchronousTask;
import com.leadership.app.interfaces.ICompanySetUP;
import com.leadership.app.manager.CompanySetUpManager;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

public class CityListActivity extends FragmentActivity implements IAsynchronousTask, OnItemClickListener, OnClickListener{
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	CityAdapter cityAdapter;
	ListView lvCityList;
	ImageView ivNext;
	View currentSelectedItem;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_wise_comparision_city_list);
		lvCityList=(ListView) findViewById(R.id.lvCityList);
		
		lvCityList.setOnItemClickListener(this);
		ivNext=(ImageView) findViewById(R.id.ivNext);
		ivNext.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		CommonValues.getInstance().SelectedCity=null;
		LoadOperator();
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
		return companySetUP.GetCity();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			CityRoot companySetups = (CityRoot) data;
			cityAdapter = new CityAdapter(this, R.layout.city_item_layout, new ArrayList<City>(companySetups.citySetup));
			lvCityList.setAdapter(cityAdapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		if(currentSelectedItem!=null)
			currentSelectedItem.setBackgroundColor(getResources().getColor(R.color.list_item_normal));
		v.setBackgroundColor(getResources().getColor(R.color.list_item_selected));		
		CommonValues.getInstance().SelectedCity=cityAdapter.getItemById(Integer.parseInt(String.valueOf(v.getTag())));
		currentSelectedItem=v;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ivNext){
			if(CommonValues.getInstance().SelectedCity!=null){
				CityWiseReportActivity.isCompare=false;
				Intent intent = new Intent(this, CityWiseReportActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else{
				Toast.makeText(this, "Please select a city.", Toast.LENGTH_SHORT).show();				
			}
		}
		
	}
}
