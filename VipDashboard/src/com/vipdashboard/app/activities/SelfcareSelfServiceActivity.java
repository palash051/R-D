package com.vipdashboard.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;

public class SelfcareSelfServiceActivity extends MainActionbarBase implements OnClickListener{
RelativeLayout rlSelfcare_Selfservice_BalanceCheck,rlSelfcare_Selfservice_PricePlanCharge,rlSelfcare_Selfservice_FnF,rlSelfcare_Selfservice_Balance_Transfer,rlSelfcare_Selfservice_StarStatus,rlSelfcare_Selfservice_CallCustomerCare;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfcare_selfservice);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		rlSelfcare_Selfservice_BalanceCheck = (RelativeLayout) findViewById(R.id.rlSelfcare_Selfservice_BalanceCheck);
		rlSelfcare_Selfservice_PricePlanCharge = (RelativeLayout) findViewById(R.id.rlSelfcare_Selfservice_PricePlanCharge);
		rlSelfcare_Selfservice_FnF = (RelativeLayout) findViewById(R.id.rlSelfcare_Selfservice_FnF);
		rlSelfcare_Selfservice_Balance_Transfer = (RelativeLayout) findViewById(R.id.rlSelfcare_Selfservice_Balance_Transfer);
		rlSelfcare_Selfservice_StarStatus = (RelativeLayout) findViewById(R.id.rlSelfcare_Selfservice_StarStatus);
		rlSelfcare_Selfservice_CallCustomerCare = (RelativeLayout) findViewById(R.id.rlSelfcare_Selfservice_CallCustomerCare);
		
		
		rlSelfcare_Selfservice_BalanceCheck.setOnClickListener(this);
		rlSelfcare_Selfservice_PricePlanCharge.setOnClickListener(this);
		rlSelfcare_Selfservice_FnF.setOnClickListener(this);
		rlSelfcare_Selfservice_Balance_Transfer.setOnClickListener(this);
		rlSelfcare_Selfservice_StarStatus.setOnClickListener(this);
		rlSelfcare_Selfservice_CallCustomerCare.setOnClickListener(this);		
	}
	
	@Override
	 protected void onPause() {
	  MyNetApplication.activityPaused();
	  super.onPause();
	 }

	 @Override
	 protected void onResume() {
	  MyNetApplication.activityResumed();
	  super.onResume();
	 }
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.rlSelfcare_Selfservice_BalanceCheck){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(v.getId() == R.id.rlSelfcare_Selfservice_PricePlanCharge){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(v.getId() == R.id.rlSelfcare_Selfservice_FnF){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(v.getId() == R.id.rlSelfcare_Selfservice_Balance_Transfer){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(v.getId() == R.id.rlSelfcare_Selfservice_StarStatus){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(v.getId() == R.id.rlSelfcare_Selfservice_CallCustomerCare){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}
	}
}
