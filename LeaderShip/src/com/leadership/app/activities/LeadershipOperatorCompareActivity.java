package com.leadership.app.activities;

import java.util.ArrayList;

import com.leadership.app.R;
import com.leadership.app.adapter.OperatorAdapter;
import com.leadership.app.asynchronoustask.DownloadableAsyncTask;
import com.leadership.app.entities.CompanySetup;
import com.leadership.app.entities.CompanySetups;
import com.leadership.app.interfaces.IAsynchronousTask;
import com.leadership.app.interfaces.ICompanySetUP;
import com.leadership.app.manager.CompanySetUpManager;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeadershipOperatorCompareActivity extends Activity implements IAsynchronousTask, OnItemClickListener, OnClickListener {
	
	ListView lvOperatorNameList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	OperatorAdapter operatorAdapter;
	String selectedUsers="",selectedOperatorNames="";
	
	public static String ReportType="Finance";	
	ImageView ivOperatorLogo;
	TextView ivCompareLogo,tvCompareSelectAll,tvHeader,ivOperatorIconText;
	
	boolean isSelectedAll=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leadership_individual_opetaror_compare);
		Initialization();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		selectedUsers="";
		selectedOperatorNames="";
		isSelectedAll=false;
		tvHeader.setText(CommonValues.getInstance().SelectedGraphItem);
		/*if(ReportType.equals("SWMI"))
			tvHeader.setVisibility(View.GONE);
		else{
			tvHeader.setVisibility(View.VISIBLE);
			tvHeader.setText(CommonValues.getInstance().SelectedGraphItem);
		}*/
		
		if(CommonValues.getInstance().LoginUser.UserMode==3){
			ivOperatorIconText.setVisibility(View.VISIBLE);
			ivOperatorLogo.setVisibility(View.GONE);
			ivOperatorIconText.setText(CommonValues.getInstance().SelectedCompany.CompanyName);
		}else{
			ivOperatorIconText.setVisibility(View.GONE);
			ivOperatorLogo.setVisibility(View.VISIBLE);
			ivOperatorLogo.setBackgroundResource(CommonValues.getInstance().SelectedCompany.CompanyLogoId);
		}
		
		LoadOperator();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private void Initialization(){
		lvOperatorNameList = (ListView) findViewById(R.id.lvOperatorNameList);
		ivCompareLogo = (TextView) findViewById(R.id.ivCompareLogo);
		ivOperatorLogo= (ImageView) findViewById(R.id.ivOperatorLogo);
		tvCompareSelectAll= (TextView) findViewById(R.id.tvCompareSelectAll);
		ivOperatorIconText= (TextView) findViewById(R.id.ivOperatorIconText);
		tvHeader= (TextView) findViewById(R.id.tvHeader);
		lvOperatorNameList.setOnItemClickListener(this);
		ivCompareLogo.setOnClickListener(this);	
		tvCompareSelectAll.setOnClickListener(this);	
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
			operatorAdapter = new OperatorAdapter(this, R.layout.operator_item_layout, new ArrayList<CompanySetup>(companySetups.companySetupList));
			lvOperatorNameList.setAdapter(operatorAdapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
		try{
			if(isSelectedAll){
				selectedOperatorNames="";
				selectedUsers="";
				for (CompanySetup companySetup : operatorAdapter.getItemList()) {
					selectedOperatorNames=selectedOperatorNames+(selectedOperatorNames.isEmpty()?"":",")+companySetup.CompanyName;
					selectedUsers=selectedUsers+(selectedUsers.isEmpty()?"":",")+companySetup.CompanyID;
				}
			}else{
				CompanySetup companySetup = (CompanySetup) view.getTag();
				isSelectedAll=false;
				if(companySetup != null){
					String id = String.valueOf(companySetup.CompanyID);
					if(!selectedOperatorNames.contains(companySetup.CompanyName)){
						selectedOperatorNames=selectedOperatorNames+companySetup.CompanyName+",";
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
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.ivCompareLogo){			
			if(!selectedUsers.isEmpty()){
				LeadershipCompareDetailsActivity.selectedUsers=selectedUsers;
				if(!selectedOperatorNames.isEmpty()){
					selectedOperatorNames=selectedOperatorNames.substring(0, selectedOperatorNames.length()-1);
				}
				LeadershipCompareDetailsActivity.selectedOperatorNames=selectedOperatorNames;
				Intent intent = new Intent(this, LeadershipCompareDetailsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else{
				Toast.makeText(this, "Please select operators", Toast.LENGTH_SHORT).show();
			}
		}else if(id==R.id.tvCompareSelectAll){
			isSelectedAll=true;
			//operatorAdapter.notifyDataSetChanged();
			for(int i=0;i<operatorAdapter.getCount();i++)
				lvOperatorNameList.performItemClick(lvOperatorNameList.getChildAt(i), i, i);
		}
	}
	
	

}
