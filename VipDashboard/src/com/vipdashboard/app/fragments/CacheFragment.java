package com.vipdashboard.app.fragments;

import java.io.File;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.OptimizationResultActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CacheFragment extends Fragment {
	Method getPackageSizeInfo;
	LayoutInflater mInflater;
	Context mContext;
	private ListView listViewCachedApps;
	private ProgressBar progressBar;
	ProgressDialog progress;
	boolean isStop = true;
	int numberOfRescan;

	List<InstalledAppItem> listInstalledApps;
	ListViewCacheApdater adapter;
	Button mBtnClean;
	TextView txtFetchSize;
	public boolean isPause;
	View view;
	boolean isFinish = false;
	FetchCacheAsynctask mAsyncTask;

	String processedInfo="";
	int runningApps=0;
	// BroadcastReceiver fetchFinish = new LoadFinishReceiver();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = this.getActivity();

		Log.e("CacheFragment: onCreateView", "onCreateView");
		view = getActivity().getLayoutInflater().inflate(R.layout.activity_cache, null);
		listViewCachedApps = (ListView) view.findViewById(R.id.listView_cache_app);
		txtFetchSize = (TextView) view.findViewById(R.id.tv_scan_info);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar_scan);
		progressBar.setVisibility(View.VISIBLE);
		mBtnClean = (Button) view.findViewById(R.id.btn_clean);
		mBtnClean.setText("Loading...");
	//	mBtnClean.setText(R.string.btn_stop);
		mBtnClean.setEnabled(false);
		mAsyncTask = new FetchCacheAsynctask();
		mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

		// btnBackup = (Button) view.findViewById(R.id.apk_backup_btn);
		mBtnClean.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String textButtonClean = (String) mBtnClean.getText();
				if (textButtonClean.equalsIgnoreCase("Clean")) {
					deleteCacheApp();
					mBtnClean.setEnabled(true);
				} /*else if (textButtonClean.equalsIgnoreCase("Stop")) {
					isStop = true;
					mAsyncTask.cancel(true);
				} */else {
					progressBar.setVisibility(View.VISIBLE);
					txtFetchSize.setGravity(Gravity.LEFT);
					txtFetchSize.setGravity(Gravity.CENTER_VERTICAL);
					mBtnClean.setText(R.string.btn_stop);
					mAsyncTask.cancel(true);
					isStop = false;
					mAsyncTask = new FetchCacheAsynctask();
					mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

				}

			}
		});

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isStop = false;
		listInstalledApps = new ArrayList<InstalledAppItem>();
		adapter = new ListViewCacheApdater(getActivity(), listInstalledApps);

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("onPause Cache", "Pause!");
		isStop = true;
		mAsyncTask.cancel(true);
		mAsyncTask = null;
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e("onStop Cache", "Stop!");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("onDestroy Cache", "onDestroy!");
	}

	@Override
	public void onResume() {
		super.onResume();
		runningApps=0;
		Log.e("onResume Cache", "onResume!");
	}

	private void finishFetch() {

		progressBar.setVisibility(View.GONE);
		txtFetchSize.setGravity(Gravity.CENTER);
		listViewCachedApps.setAdapter(adapter);
		listViewCachedApps.refreshDrawableState();

		if (listInstalledApps.size() == 0) {
			txtFetchSize.setText("No Cache App On The SDCard");
			mBtnClean.setText("Rescan");
			mBtnClean.setEnabled(true);
			return;
		}
		String stringCache;
		float total_cachesize = 0;
		for (int i = 0; i < listInstalledApps.size(); i++) {
			stringCache = listInstalledApps.get(i).getCache();
			stringCache = stringCache.replace("Cache ", "");
			if (stringCache.contains("Kb")) {
				stringCache = stringCache.replace("Kb", "");
				total_cachesize += Float.parseFloat(stringCache);
			} else {
				stringCache = stringCache.replace("Mb", "");
				total_cachesize += Float.parseFloat(stringCache) * 1024;
			}
		}
		int numberOfCacheApp = listInstalledApps.size();
		String tailUnit;
		if ((total_cachesize / 1024) > 1) {
			tailUnit = "Mb";
			total_cachesize = total_cachesize / 1024;
		} else {
			tailUnit = "Kb";
		}
		DecimalFormat df = new DecimalFormat("0.0");
		df.setRoundingMode(RoundingMode.UP);

		txtFetchSize.setText("Apps: " + numberOfCacheApp + "   Occupied: " + df.format(total_cachesize) + tailUnit);
		processedInfo="Cache cleaned successfully.\n Cleaned :" + runningApps + "   Memory: " + df.format(total_cachesize) + tailUnit;
		mBtnClean.setText("Clean");
		mBtnClean.setEnabled(true);
		isFinish = true;
		listViewCachedApps.setVisibility(view.INVISIBLE);
		listViewCachedApps.setVisibility(view.VISIBLE);
	}

	public void deleteCacheApp() {
		PackageManager pm = this.getActivity().getPackageManager();
		Method[] methods = pm.getClass().getDeclaredMethods();
		for (Method m : methods) {
			if (m.getName().equals("freeStorageAndNotify")) {
				// Found the method I want to use
				try {
					long desiredFreeStorage = getTotalInternalMemorySize();
					m.invoke(pm, desiredFreeStorage, null);
				} catch (Exception e) {
					Log.e("Delete Cache", e.getMessage());
				}
				break;
			}
		}
		listInstalledApps.clear();
		adapter.notifyDataSetChanged();
		// Memory boost will come here
		
		Intent intent = new Intent(getActivity(), OptimizationResultActivity.class);
		OptimizationResultActivity.calledFromInfo="MemoryBoots";
		OptimizationResultActivity.optimizationMessage=processedInfo;
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		
		//mBtnClean.setText(R.string.btn_rescan);
		//txtFetchSize.setText("Apps: 0     Occurpied: 0.0Kb");
	}

	public static long getTotalInternalMemorySize() {

		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return (totalBlocks * blockSize);
	}

	private class FetchCacheAsynctask extends AsyncTask<Void, Integer, Void> {
		int maxIndex = 0;
		List<PackageInfo> packages;
		PackageManager pm;
		private Activity activity;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pm = getActivity().getPackageManager();
			packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);
			maxIndex = packages.size();
			progressBar.setProgress(0);
			activity = getActivity();
			progressBar.setMax(maxIndex);
			try {
				getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class,
						IPackageStatsObserver.class);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//
			for (int i = 0; i < packages.size(); i++) {
				if (isStop) {
					break;
				}
				final PackageInfo pkgInfo = packages.get(i);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!isStop) {
							txtFetchSize.setText(pkgInfo.packageName);
							progressBar.setProgress(packages.indexOf(pkgInfo));
						}
					}
				});

				final String appname = pkgInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
				
				/*private ArrayList<ApplicationInfo> checkForLaunchIntent(
						List<ApplicationInfo> installedApplications) {
					ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
			        for (ApplicationInfo info : installedApplications) {
			            try {
			                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
			                    applist.add(info);
			                }
			                if((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
			                	systemApps.add(info);
			                }
			            } catch (Exception e) {
			                e.printStackTrace();
			            }
			        } 
			        return applist;
				}*/

				try {
					getPackageSizeInfo.invoke(pm, pkgInfo.packageName, new IPackageStatsObserver.Stub() {
						public synchronized void onGetStatsCompleted(final PackageStats pStats, boolean succeeded)
								throws RemoteException {

							if (!isStop) {

								float cachsize = pStats.cacheSize / (1024);
								if (cachsize > 1) {
									String tailUnit;
									if ((cachsize / 1024) > 1) {
										tailUnit = "Mb";
										cachsize = cachsize / 1024;
									} else {
										tailUnit = "Kb";
									}
									DecimalFormat df = new DecimalFormat("0.0");
									df.setRoundingMode(RoundingMode.UP);
									String cacheSize = ("Cache " + df.format(cachsize) + tailUnit);
									Drawable icon = pkgInfo.applicationInfo.loadIcon(getActivity().getPackageManager());
									final InstalledAppItem item = new InstalledAppItem(pkgInfo.packageName, icon,
											appname, cacheSize);

									   if((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
										   runningApps++;
									   }
									listInstalledApps.add(item);
								}

							}
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Fetch Cache", e.getMessage());
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finishFetch();
			//mBtnClean.setText("Clean");

		}

		@Override
		protected void onCancelled(Void result) {
			super.onCancelled(result);
			txtFetchSize.setText(R.string.btn_rescan);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finishFetch();

		}

	}

}
