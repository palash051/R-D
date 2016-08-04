package com.vipdashboard.app.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;



import android.support.v4.app.FragmentTransaction;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AppManagerAdapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AppManagerFragmnet extends Fragment implements
		OnItemClickListener, OnClickListener {

	ListView lvAppList;
	RelativeLayout rlBackUp, rlUninstall;
	private PackageManager packageManager;
	CheckedTextView usergroupListCheckedTextView;
	private ArrayList<ApplicationInfo> applicationInfo;
	AppManagerAdapter adapter;
	String packageName;
	private String selectedUsers = new String();
	Stack<String> stack = new Stack<String>();
	
	boolean isAdded = false;
//	private List<AppitemBean> listApps;

//	private List<AppitemBean> listUninstall = new ArrayList<AppitemBean>();
//	public List<AppitemBean> listInstall = new ArrayList<AppitemBean>();
	private ProgressBar progressBar;
	File directory;
	private PackageManager pm;
	ProgressDialog progress;
	private Context contex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.app_manager_fragment,
				container, false);
		Initialization(root);
		return root;
	}

	private void Initialization(View root) {
		packageManager = getActivity().getPackageManager();
		lvAppList = (ListView) root.findViewById(R.id.lvAppList);
		rlBackUp = (RelativeLayout) root.findViewById(R.id.rlBackUp);
		rlUninstall = (RelativeLayout) root.findViewById(R.id.rlUninstall);

		lvAppList.setOnItemClickListener(this);
		rlUninstall.setOnClickListener(this);
		rlBackUp.setOnClickListener(this);

		CheckedTextView checkedTextView = (CheckedTextView) root
				.findViewById(R.id.usergroupListCheckedTextView);
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
	
	
	
	
	
	public void Backup() {
		try
		{
//		Boolean isCheck = false;
		
		
		
		/*for (String item : items) {
				isCheck = true;
		}
		if (!isCheck) {
			return;
		}*/
		progress = ProgressDialog.show(getActivity(), "Care", "Backing up....",
				true);
		
		Toast.makeText(getActivity(),"Backup created successfully",Toast.LENGTH_SHORT).show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				/*listSelected = adapter.listSelected;
				for (AppitemBean item : listSelected) {
					if (item.check) {*/
						//BackupAppByPackage(item.packageName);
				
				List<String> items = Arrays.asList(selectedUsers.split("\\s*,\\s*"));
				selectedUsers="";
				for (String item : items) {
						BackupAppByPackage(item);
				}
					/*}
				}*/
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try
						{
							lvAppList.clearChoices();
							//lvAppList.setAdapter(adapter);
							adapter.notifyDataSetInvalidated();
							progress.dismiss();
//						AlertDialog.Builder aboutDialog = new AlertDialog.Builder(
//								getActivity());
//						aboutDialog.create();
//						File directory = new File(Environment.getExternalStorageDirectory()	+ File.separator+ "MumtazCare/Backup");
//					
//						aboutDialog
//								.setMessage("Apps backuped successfully. Path: "
//										+ directory.toString());
//						aboutDialog.show();
						
//						Toast toast =Toast.makeText(getApplicationContext(), "YOUR BACK IS UP CREATED IN MAMATAZ CARE FOLDER", Toast.LENGTH_LONG);
//						toast.show();
						
					
//						refreshTabApk();
						}
						catch(Exception ex){}

					}
				});
				
				
			}
		}).start();
		}
		catch(Exception ex){
			getActivity(). runOnUiThread(new Runnable() {
	                public void run() {
	                    Toast.makeText(getApplicationContext(),"URL  exeption!",Toast.LENGTH_SHORT).show();
	                }
	            });
		}

	}
	

//	private void refreshTabApk()
//	{
//		String TabOfFragmentAPK = ((AppManager)getActivity()).getAPKFragment();
//		  fragmentAPK = (TabAPK)getActivity()
//				     .getSupportFragmentManager()
//				     .findFragmentByTag(TabOfFragmentAPK);
//		fragmentAPK.reload();
//	}
	
	public void Uninstall() {
		List<String> items = Arrays.asList(selectedUsers.split("\\s*,\\s*"));
		for (String item : items) {

				UninstallAppByPackage(item);
			}
		}
	
		private void UninstallAppByPackage(String packName) {
			Uri packageUri = Uri.parse("package:" + packName);
			Intent localIntent = new Intent(Intent.ACTION_DELETE, packageUri);
			localIntent.setData(packageUri);
			startActivity(localIntent);
		}
	
	private void BackupAppByPackage(String packName) {
		try {
			File directory = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "Care/Backup");
			if (!directory.isDirectory()) {
				directory.mkdirs();
			}
	
			
			ApplicationInfo info =getActivity().getPackageManager().getApplicationInfo(packName, 0);
			String appDir = info.sourceDir;
			String bakDir = directory.getPath() + "/" + info.packageName
					+ ".apk";
			File appFile = new File(appDir);
			File baklFile = new File(bakDir);
			int flag = 9999;
			if ((!appFile.canRead()) || (!appFile.isFile())
					|| (!appFile.exists()))
				flag = 0;

			if (flag != 0) {
				if (!baklFile.getParentFile().exists())
					baklFile.getParentFile().mkdirs();
				if (baklFile.exists()) {
					baklFile.delete();
				}
				FileInputStream localFileInputStream = null;
				FileOutputStream localFileOutputStream = null;
				try {
					localFileInputStream = new FileInputStream(appFile);
					localFileOutputStream = new FileOutputStream(baklFile);
					byte[] arrayOfByte = new byte[1048576];
					while (true) {
						int i2 = localFileInputStream.read(arrayOfByte);
						if (i2 <= 0)
							break;
						localFileOutputStream.write(arrayOfByte, 0, i2);
					}
				} catch (Exception localException) {
					localException.printStackTrace();
					flag = 0;
				}
				try {
					localFileInputStream.close();
					localFileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				flag = 1;
				// continue;s
				

			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Toast toast = Toast.makeText(getApplicationContext(), "Error in getting pacakge name", Toast.LENGTH_SHORT);
	        toast.show();
	        e.printStackTrace();
		}

	}


	

	private Context getApplicationContext() {
	// TODO Auto-generated method stub
	return null;
}


	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlUninstall) {
			

		
			if(!selectedUsers.isEmpty())
			{
			Uninstall();
			selectedUsers="";
			}
			else{
				Toast.makeText(getActivity(), "Please select application first", Toast.LENGTH_LONG).show();
			}
		
		}

		if (view.getId() == R.id.rlBackUp) {
			
			
			if(!selectedUsers.isEmpty())
			{ 
				Backup();
				
				//selectedUsers="";
				lvAppList.clearChoices();
//				lvAppList.setAdapter(adapter);
//				adapter.notifyDataSetInvalidated();
				//adapter.notifyDataSetChanged();
				//usergroupListCheckedTextView.Click += (sender, args) => lvAppList.SetItemChecked(-1, true);
				
			}
			
			else
			{
												
			     Toast.makeText(getActivity(), "Please select application first", Toast.LENGTH_LONG).show();
//                  selectedUsers="";
//                  lvAppList.setAdapter(adapter);
  //				adapter.notifyDataSetInvalidated();

			}
		}
		

	}
	

}
