package com.vipdashboard.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AppBaseGridViewAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.fragments.AlarmMainFragment;

public class AppBaseActivity extends MainActionbarBase implements OnItemClickListener, OnClickListener {
	
	
	RelativeLayout bEmail, bCollaboration, bMeetOnline, bDocument;
	TextView tvTitleText, tvInstallApp, tvAllApps;
	GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appbase);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		initialization();
		gridView.setAdapter(new AppBaseGridViewAdapter(this));
	}
	private void initialization() {	
		
		bEmail = (RelativeLayout) findViewById(R.id.rlAppBaseEmail);
		bCollaboration = (RelativeLayout) findViewById(R.id.rlAppBaseCollaboration);
		bMeetOnline = (RelativeLayout) findViewById(R.id.rlAppBaseMeetingOnline);
		bDocument = (RelativeLayout) findViewById(R.id.rlAppBaseDocument);
		
		//tvTitleText = (TextView) findViewById(R.id.tvAppBaseTitle);
		tvInstallApp = (TextView) findViewById(R.id.tvInstallAppTitle);
		tvAllApps = (TextView) findViewById(R.id.tvAllAppTitle);
		
		gridView = (GridView) findViewById(R.id.grid);
		
		gridView.setOnItemClickListener(this);
		tvInstallApp.setOnClickListener(this);
		tvAllApps.setOnClickListener(this);
		
		
		bEmail.setOnClickListener(this);
		bCollaboration.setOnClickListener(this);
		bMeetOnline.setOnClickListener(this);
		bDocument.setOnClickListener(this);
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
		
	}
	
	@Override
	public void onBackPressed() {
		backTohome();
	}

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		
		AppBaseGridViewAdapter adapter = new AppBaseGridViewAdapter(this);
		if(adapter.AppImage[position] == R.drawable.my_alarmmanager){
			Intent intent = new Intent(this,AlarmMainFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(adapter.AppImage[position] == R.drawable.my_dashboard){
			Intent intent = new Intent(this,DashboradActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(adapter.AppImage[position] == R.drawable.my_reports){
			Intent intent = new Intent(this,ReportMainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(adapter.AppImage[position] == R.drawable.my_troubletickets){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(adapter.AppImage[position] == R.drawable.my_remoteconnect){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(adapter.AppImage[position] == R.drawable.my_rolloutmanager){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(adapter.AppImage[position] == R.drawable.my_siteaudit){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(adapter.AppImage[position] == R.drawable.my_sitedatabase){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(adapter.AppImage[position] == R.drawable.my_onlineourses){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(adapter.AppImage[position] == R.drawable.my_nro_manager){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(adapter.AppImage[position] == R.drawable.my_manuals){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.tvInstallAppTitle){
			gridView.setAdapter(new AppBaseGridViewAdapter(this));
		}else if(view.getId() == R.id.tvAllAppTitle){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlAppBaseEmail){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlAppBaseCollaboration){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlAppBaseMeetingOnline){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}else if(view.getId() == R.id.rlAppBaseDocument){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_LONG).show();
		}
	}
	
	

}
