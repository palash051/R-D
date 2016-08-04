package com.vipdashboard.app.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.DemoScreenActivity;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.utils.CommonValues;

public class AlarmMainFragment extends MainActionbarBase implements OnItemClickListener, OnQueryTextListener{
	/*public static Fragment newInstance(Context context) {
		AlarmMainFragment f = new AlarmMainFragment();		
		return f;
	}*/
	//Button bShowCriticalAlarms,bCeasedLastHour;
	
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.alarm_main,container, false);
		
		
		
		return root;
	}*/
	
	ListView lvAlarmMenu;
	TextView lvNameText;
	SearchView searchView;
	ReportAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_main);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
		lvAlarmMenu = (ListView) findViewById(R.id.lvAlarmTitleList);
		lvNameText = (TextView) findViewById(R.id.text);
		searchView = (SearchView) findViewById(R.id.svCollaborationList);
		
		AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.BLACK);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
		
		searchView.setOnQueryTextListener(this);
		
		/*String[] menuListvalue = new String[]{"All Alarms","Critical Alarms","Ceased In Last Hour","Search Alarm"};
		ArrayList<String> menuList = new ArrayList<String>();
		for(int i=0;i<menuListvalue.length;i++){
			menuList.add(menuListvalue[i]);
		}*/
		ArrayList<Report> reportList = new ArrayList<Report>();
		reportList.add(new Report("All Alarms",1));
		reportList.add(new Report("Critical Alarms",2));
		reportList.add(new Report("Ceased In Last Hour",3));
		reportList.add(new Report("Search Alarm",4));
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
		lvNameText.setTag(adapter);
		lvAlarmMenu.setAdapter(adapter);
		
		lvAlarmMenu.setOnItemClickListener(this);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		
		/*bShowCriticalAlarms=(Button) findViewById(R.id.bShowCriticalAlarms);
		bCeasedLastHour=(Button) findViewById(R.id.bCeasedLastHour);
		bShowCriticalAlarms.setOnClickListener(this);
		bCeasedLastHour.setOnClickListener(this);*/
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
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		int ID = Integer.parseInt(String.valueOf(view.getTag()));
		if(ID ==1){
			Intent intent = new Intent(this,AllAlarmsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_SHORT).show();
		}else if(ID == 2){
			Intent intent = new Intent(this,CriticalAlarmsFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(ID == 3){
			Intent intent = new Intent(this,CeasedInLastHourActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_SHORT).show();
		}else if(ID == 4){
			Intent intent = new Intent(this,SearchAlarmActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	public boolean onQueryTextChange(String newText) {
		adapter.Filter(newText);
		return true;
	}
	@Override
	public boolean onQueryTextSubmit(String query) {
		
		return false;
	}
	

	/*@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.bShowCriticalAlarms){
			
			FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.replace(R.id.alarm_fragment_root_id, CriticalAlarmsFragment.newInstance(getActivity()));
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
            
            Intent intent = new Intent(this,CriticalAlarmsFragment.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}if(id==R.id.bCeasedLastHour){
			((CollaborationActivity) getActivity()).isCallFromNotification=true;
			FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.replace(R.id.first_fragment_root_id, EnterToDiscussionGroupListFragment.newInstance(getActivity()));
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
		}
		
	}*/
}
