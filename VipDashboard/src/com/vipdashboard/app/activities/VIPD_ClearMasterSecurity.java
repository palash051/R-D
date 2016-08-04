package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.List;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.PickItem;
import com.vipdashboard.app.adapter.PickMangerAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class VIPD_ClearMasterSecurity extends MainActionbarBase implements
		OnItemClickListener {

	public static final int[] id = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	public static final String[] titles = new String[] {"AntiVirus Security - FREE",
			 "Mobile Security & Antivirus",
			"Kaspersky Internet Security", "Avira Antivirus Security",
			"CM Security- AppLock&AntiVirus", "Norton Security and Antivirus",
			"360 Security - Antivirus FREE", "Antivirus Free",
			 };

	public static final String[] descriptions = new String[] {
			"It is an aggregate accessory fruit",
			"It is the largest herbaceous flowering plant", "Citrus Fruit",
			"Mixed Fruits" };

	public static final Integer[] images = { R.drawable.antivirussecurity,
			R.drawable.mobilesecurityantivirus, R.drawable.kasperskyinternetsecurity,
			R.drawable.aviraantivirussecurity, R.drawable.cmsecurityapplockantivirus,
			R.drawable.nortonsecurityandantivirus, R.drawable.threesecurityantivisfree,
			R.drawable.antivirusfree };

	ListView listView;
	List<PickItem> rowItems;

	/**
	 * Called when the activity is first created.
	 * 
	 * @return
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_manager_fragment);
		Initialization();
	}
	@Override
	public void onResume() {
		super.onResume();
		listInfo();

	}

	/*
	 * @Override public void onActivityCreated(Bundle savedInstanceState) {
	 * super.onActivityCreated(savedInstanceState); setHasOptionsMenu(true); }
	 */

	private void listInfo() {

		rowItems = new ArrayList<PickItem>();
		for (int i = 0; i < titles.length; i++) {
			PickItem item = new PickItem(id[i], images[i], titles[i]);
			rowItems.add(item);
		}
		PickMangerAdapter adapter = new PickMangerAdapter(this,
				R.layout.picklayout, (ArrayList<PickItem>) rowItems);

		listView.setAdapter(adapter);
	}

	private void Initialization() {
		listView = (ListView) findViewById(R.id.lvAppListpick);
		listView.setOnItemClickListener(this);
	}

	// private void setContentView(int pickManagerFragment) {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (rowItems.get(position).getId() == 1) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=com.antivirus"));
			startActivity(browserIntent);

		} else if (rowItems.get(position).getId() == 2) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=com.avast.android.mobilesecurity"));
			startActivity(browserIntent);}
			else if(rowItems.get(position).getId() == 3){
				   
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=com.kms.free"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 4){
				   
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						   Uri.parse("https://play.google.com/store/apps/details?id=com.avira.android"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 5){
				   
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						   Uri.parse("https://play.google.com/store/apps/details?id=com.cleanmaster.security"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 6){
				  
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=com.symantec.mobilesecurity"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 7){
				     Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=com.qihoo.security"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 8){
				     Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=com.zrgiu.antivirus"));
				   startActivity(browserIntent);
				   
				  }
//			else if(rowItems.get(position).getId() == 9){
//				     Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//				     Uri.parse("https://play.google.com/store/apps/details?id=pl.skowronek.internetboost"));
//				   startActivity(browserIntent);
//				  }
//			else if(rowItems.get(position).getId() == 10){
//				    
//				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//				     Uri.parse("https://play.google.com/store/apps/details?id=com.antispycell.connmonitor"));
//				   startActivity(browserIntent);
//				   
//				  }
		}



//		Toast toast = Toast.makeText(getActivity(), "Item " + (position + 1)
//				+ ": " + rowItems.get(position), Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//		toast.show();
//	
}


