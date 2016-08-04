package com.leadership.app.activities;


import java.util.ArrayList;

import com.leadership.app.R;
import com.leadership.app.adapter.CityAdapter;
import com.leadership.app.adapter.CityCheckedAdapter;
import com.leadership.app.asynchronoustask.DownloadableAsyncTask;
import com.leadership.app.entities.City;
import com.leadership.app.entities.CityRoot;
import com.leadership.app.entities.CompanySetup;
import com.leadership.app.interfaces.IAsynchronousTask;
import com.leadership.app.interfaces.ICompanySetUP;
import com.leadership.app.manager.CompanySetUpManager;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

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
import android.widget.TextView;
import android.widget.Toast;

public class CityWiseComparisionActivity extends FragmentActivity implements IAsynchronousTask, OnClickListener, OnItemClickListener{
	
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	CityCheckedAdapter cityAdapter;
	ListView lvCityList;
	ImageView ivNext;
	String selectedOperatorNames="";
	String selectedUsers="";
	TextView tvCitySubHeader;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_wise_comparision_city_list);
		lvCityList=(ListView) findViewById(R.id.lvCityList);
		lvCityList.setOnItemClickListener(this);
		ivNext = (ImageView) findViewById(R.id.ivNext);
		tvCitySubHeader= (TextView) findViewById(R.id.tvCitySubHeader);
		ivNext.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		selectedOperatorNames="";
		selectedUsers="";
		tvCitySubHeader.setText(CommonValues.getInstance().SelectedCity.CityName.toUpperCase());
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
			for (City city : companySetups.citySetup) {
				if(city.CityID==CommonValues.getInstance().SelectedCity.CityID){
					companySetups.citySetup.remove(city);
					break;
				}
			}
			cityAdapter = new CityCheckedAdapter(this, R.layout.operator_item_layout, new ArrayList<City>(companySetups.citySetup));
			lvCityList.setAdapter(cityAdapter);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ivNext){
			if (!CommonTask.isOnline(this)) {
				CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
				return;
			}
			if(selectedUsers.isEmpty()){
				Toast.makeText(this, "Please select city", Toast.LENGTH_SHORT).show();
				return;	
			}
			CityWiseReportActivity.isCompare=true;			
			CityWiseReportActivity.selectedCityNames=selectedOperatorNames.toUpperCase();
			CityWiseReportActivity.selectedCityIds=selectedUsers;
			Intent intent = new Intent(this, CityWiseReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		try{
			/*if(isSelectedAll){
				selectedOperatorNames="";
				selectedUsers="";
				for (CompanySetup companySetup : operatorAdapter.getItemList()) {
					selectedOperatorNames=selectedOperatorNames+(selectedOperatorNames.isEmpty()?"":",")+companySetup.CompanyName;
					selectedUsers=selectedUsers+(selectedUsers.isEmpty()?"":",")+companySetup.CompanyID;
				}
			}else{*/
				City companySetup = (City) view.getTag();
				if(companySetup != null){
					String id = String.valueOf(companySetup.CityID);
					if(!selectedOperatorNames.contains(companySetup.CityName)){
						selectedOperatorNames=selectedOperatorNames+companySetup.CityName+",";
					}
					if (!id.equals("")) {
						
						if(selectedUsers.isEmpty()){
							selectedUsers=id;						
						}else{
							String newIds="";
							String[] ids =selectedUsers.split(",");
							boolean isAdded=false;
							for (String st : ids) {
								if(!st.equalsIgnoreCase(id)){
									newIds=newIds+st+",";
								}else{
									isAdded=true;
								}
							}
							if(!isAdded)
								newIds=newIds+id+",";
							if(newIds.length()>0)
								newIds=newIds.substring(0, newIds.length()-1);
							selectedUsers=newIds;
						}
					}
				}
			//}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
