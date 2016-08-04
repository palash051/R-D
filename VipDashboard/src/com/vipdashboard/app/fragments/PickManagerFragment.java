package com.vipdashboard.app.fragments;

import java.util.ArrayList;
import java.util.List;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.PickItem;
import com.vipdashboard.app.adapter.PickMangerAdapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class PickManagerFragment extends Fragment implements
		OnItemClickListener {

	public static final int[] id = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	public static final String[] titles = new String[] {
			"Norton Security antivirus", "Smart Utilities",
			"Battery Doctor (Battery Saver)", "Clean Master (Cleaner) - FREE",
			"Battery Dr saver+a task killer", "2x Battery - Battery Saver",
			"Network Signal Info", "Network Provider Widget",
			"Internet Booster", "Network Connections" };

	public static final String[] descriptions = new String[] {
			"It is an aggregate accessory fruit",
			"It is the largest herbaceous flowering plant", "Citrus Fruit",
			"Mixed Fruits" };

	public static final Integer[] images = { R.drawable.norton_security,
			R.drawable.smart_utilities, R.drawable.batary_doctor,
			R.drawable.clean_master, R.drawable.bettary_dr_sever,
			R.drawable.twox_btry_svr, R.drawable.network_signal_info,
			R.drawable.network_provider_widget, R.drawable.internet_buster,
			R.drawable.network_connections };

	ListView listView;
	List<PickItem> rowItems;

	/**
	 * Called when the activity is first created.
	 * 
	 * @return
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.pick_manager_fragment,
				container, false);
		Initialization(rootView);
		return rootView;
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
		PickMangerAdapter adapter = new PickMangerAdapter(getActivity(),
				R.layout.picklayout, (ArrayList<PickItem>) rowItems);

		listView.setAdapter(adapter);
	}

	private void Initialization(View root) {
		listView = (ListView) root.findViewById(R.id.lvAppListpick);
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
					Uri.parse("https://play.google.com/store/apps/details?id=com.symantec.mobilesecurity&hl=en"));
			startActivity(browserIntent);

		} else if (rowItems.get(position).getId() == 2) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=it.bisemanuDEV.smart&hl=en"));
			startActivity(browserIntent);}
			else if(rowItems.get(position).getId() == 3){
				   
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=com.ijinshan.kbatterydoctor_en"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 4){
				   
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						   Uri.parse("https://play.google.com/store/apps/details?id=com.cleanmaster.mguard"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 5){
				   
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						   Uri.parse("https://play.google.com/store/apps/details?id=net.lepeng.batterydoctor"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 6){
				  
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=com.a0soft.gphone.aDataOnOff"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 7){
				     Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=de.android.telnet"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 8){
				     Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=org.prolly.android.widget.networkprovider"));
				   startActivity(browserIntent);
				   
				  }
			else if(rowItems.get(position).getId() == 9){
				     Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=pl.skowronek.internetboost"));
				   startActivity(browserIntent);
				  }
			else if(rowItems.get(position).getId() == 10){
				    
				   Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				     Uri.parse("https://play.google.com/store/apps/details?id=com.antispycell.connmonitor"));
				   startActivity(browserIntent);
				   
				  }
		}



//		Toast toast = Toast.makeText(getActivity(), "Item " + (position + 1)
//				+ ": " + rowItems.get(position), Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//		toast.show();
//	
}


