package com.vipdashboard.app.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AppManagerAdapter;

public class VIPD_CleanMaster_SD_MoveFragment extends Fragment implements
OnItemClickListener, OnClickListener {
	
	ListView lvAppList;
	RelativeLayout rlClean;
	private PackageManager packageManager;
	CheckedTextView usergroupListCheckedTextView;
	private ArrayList<ApplicationInfo> applicationInfo;
	AppManagerAdapter adapter;
	String packageName;
	private String selectedUsers = new String();
	boolean isAdded = false;
	Stack<String> stack = new Stack<String>();
	
	
	
	private ProgressBar progressBar;
	File directory;
	private PackageManager pm;
	ProgressDialog progress;
	private Context contex;
	
	
	private void Initialization(View root) {
		// TODO Auto-generated method stub
		packageManager = getActivity().getPackageManager();
		lvAppList = (ListView) root.findViewById(R.id.lvAppList);

		rlClean= (RelativeLayout) root.findViewById(R.id.rlClean);
		rlClean.setOnClickListener(this);
		lvAppList.setOnItemClickListener(this);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.sdmove_fragment,
				container, false);
		Initialization(root);
		return root;
	}


	@Override
	public void onResume() {
		super.onResume();
		GetAppInfo();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	private void GetAppInfo() {
		applicationInfo = checkForLaunchIntent(packageManager
				.getInstalledApplications(PackageManager.GET_META_DATA));
		adapter = new AppManagerAdapter(getActivity(),
				R.layout.vipd_appmanager_uninstall_item, applicationInfo);
		lvAppList.setAdapter(adapter);
	}

	private ArrayList<ApplicationInfo> checkForLaunchIntent(
			List<ApplicationInfo> installedApplications) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo info : installedApplications) {
			try {
				if (null != packageManager
						.getLaunchIntentForPackage(info.packageName)) {
					applist.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return applist;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long ID) {
		try {
			ApplicationInfo info = (ApplicationInfo) view.getTag();
			packageName = info.packageName;
			if (!packageName.equals("")) {

				if (selectedUsers.isEmpty()) {
					selectedUsers = packageName;
				} else {
					String newIds = "";
					String[] ids = selectedUsers.split(",");
					 isAdded = false;
					for (String st : ids) {
						if (!st.equalsIgnoreCase(packageName)) {
							newIds = newIds + st + ",";
						} else {
							isAdded = true;
						}
					}
					if (!isAdded)
						newIds = newIds + packageName + ",";
					if (newIds.length() > 0)
						newIds = newIds.substring(0, newIds.length() - 1);
					selectedUsers = newIds;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
//	public void viewAppinfo(String paramString) {
//		Intent localIntent = new Intent();
//		int i1 = Build.VERSION.SDK_INT;
//		if (i1 >= 9) {
//			localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//			localIntent.setData(Uri.fromParts("package", paramString, null));
//			startActivity(localIntent);
//			return;
//		}
//		if (i1 == 8)
//			;
//		for (String str = "pkg";; str = "com.android.settings.ApplicationPkgName") {
//			localIntent.setAction("android.intent.action.VIEW");
//			localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
//			localIntent.putExtra(str, paramString);
//			break;
//		}
//
//	}
	
	public void SdcardMove() {
		List<String> items = Arrays.asList(selectedUsers.split("\\s*,\\s*"));
		for (String item : items) {

				SDcardMoveByPackage(item);
			}
		}
	
		private void SDcardMoveByPackage(String packName) {
			
			final int apiLevel = Build.VERSION.SDK_INT;
			 Intent intent = new Intent();

			    if (apiLevel >= 9) {
			        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			        intent.setData(Uri.parse("package:" + packName));
			        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        startActivity(intent);
			    } else {
			        final String appPkgName = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");

			        intent.setAction(Intent.ACTION_VIEW);
			        intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			        intent.putExtra(appPkgName, packName);
			        startActivity(intent);
			    }
			    // Start Activity
			/*    startActivity(intent);*/
//			Uri packageUri = Uri.parse("package:" + packName);
//			Intent localIntent = new Intent();
//			 localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//			localIntent.setData(packageUri);
//			startActivity(localIntent);
		}
	
	@Override
	public void onClick(View v) {
//		 TODO Auto-generated method stub
		
		if (v.getId() == R.id.rlClean) {
			

		
			if(!selectedUsers.isEmpty())
			{
				SdcardMove();
				selectedUsers="";
//				lvAppList.clearChoices();
			}
			else{
				Toast.makeText(getActivity(), "Please select application for sdcard move", Toast.LENGTH_LONG).show();
			}
		
		}
		
	}


	

}
