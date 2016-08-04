package com.vipdashboard.app.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.Memory;
import com.vipdashboard.app.utils.PercentView;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResidualFragment extends Fragment {
	Context mContext;
	Button mBtnBack;
	Button mBtnClean;
	PercentView percentView;
	private BaseExpandableListResidualFileAdapter listAdapter;
	private ArrayList<GroupHeaderDetail> groupList = new ArrayList<GroupHeaderDetail>();
	private List<ResidualFileItem> listAppLeftOver = new ArrayList<ResidualFileItem>();
	private List<ResidualFileItem> listAdsFolder = new ArrayList<ResidualFileItem>();
	private List<ResidualFileItem> listThumbnailFile = new ArrayList<ResidualFileItem>();
	private List<ResidualFileItem> listBigFile = new ArrayList<ResidualFileItem>();
	private ExpandableListView lv_residual;
	List<List<ResidualFileItem>> residual_list = new ArrayList<List<ResidualFileItem>>();

	ProgressBar mProgressBar;
	List<String> listAppName;
	TextView txtResidualFile;
	TextView tv_memory_info;
	int numberOfFiles;
	int processindex;
	boolean isStopped;
	FrameLayout layout_fetch_file;
	LinearLayout layout_no_residual;
	LinearLayout layout_listview;
	View view;
	ArrayList<String> listSystemFile;
	static List<File> listStorage;
	static File fileExternal;
	List<File> listFile;
	boolean isFinish = false;
	FetchResidualAsynctask mAsynctask;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = getActivity().getLayoutInflater().inflate(
				R.layout.activity_residual_files, null);
		layout_fetch_file = (FrameLayout) view
				.findViewById(R.id.layout_fetchFile);
		layout_no_residual = (LinearLayout) view
				.findViewById(R.id.layout_no_residual);
		layout_listview = (LinearLayout) view
				.findViewById(R.id.layout_listview);
		lv_residual = (ExpandableListView) view
				.findViewById(R.id.expande_list_esidual);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_scan);
		layout_no_residual.setVisibility(View.INVISIBLE);
		txtResidualFile = (TextView) view.findViewById(R.id.txt_scan_file);
		percentView = (PercentView) view
				.findViewById(R.id.percentage_view_residual_file);
		tv_memory_info = (TextView) view
				.findViewById(R.id.residual_phone_details_txt);
		txtResidualFile.setVisibility(View.VISIBLE);
		mBtnClean = (Button) view.findViewById(R.id.btn_clean_residual_file);
		mBtnClean.setOnClickListener(btnCleanClickListener);
		// mBtnClean.setEnabled(false);
		mBtnClean.setText(R.string.btn_stop);
		mAsynctask = new FetchResidualAsynctask();
		mAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		// create the adapter by passing your ArrayList data
		listAdapter = new BaseExpandableListResidualFileAdapter(mContext,
				groupList, residual_list);

		// attach the adapter to the list
		lv_residual.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		lv_residual.setOnChildClickListener(ListViewOnChildClicked);

		numberOfFiles = 0;

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
		// isStopped = false;
		listSystemFile = new ArrayList<String>();
		listSystemFile.clear();
		listSystemFile.add("Alarms");
		listSystemFile.add("Android");
		listSystemFile.add("data");
		listSystemFile.add("DCIM");
		listSystemFile.add("Documents");
		listSystemFile.add("Download");
		listSystemFile.add("LOST.DIR");
		listSystemFile.add("Movies");
		listSystemFile.add("Music");
		listSystemFile.add("My Documents");
		listSystemFile.add("Notifications");
		listSystemFile.add("Pictures");
		listSystemFile.add("Podcasts");
		listSystemFile.add("Ringtones");
		listStorage = new ArrayList<File>();
		listAppName = new ArrayList<String>();
		fileExternal = null;
		listFile = new ArrayList<File>();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAsynctask.cancel(true);
		// isStopped = true;
	}

	public void getMemoryInfo() {
		String strUseExternalMemory = Memory.getUseExternalMemorySize();
		String strAvailableExternalMemory = Memory
				.getAvailableInternalMemorySize();
		String htmlPhoneDetails = "Used: " + strUseExternalMemory
				+ "  - Free: " + strAvailableExternalMemory;

		tv_memory_info.setText(Html.fromHtml(htmlPhoneDetails));
		percentView.percent = Memory.percentExternalUse();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void refreshLayout() {
		if (listAdsFolder.size() > 0 || listAppLeftOver.size() > 0
				|| listBigFile.size() > 0 || listThumbnailFile.size() > 0) {
			resetAdapter();

			mProgressBar.setVisibility(View.INVISIBLE);
			txtResidualFile.setVisibility(View.VISIBLE);
			layout_listview.setVisibility(View.VISIBLE);
			layout_no_residual.setVisibility(View.GONE);
			showLayoutListResidualFile();
			expandAll();
			mBtnClean.setText(R.string.btn_clean);
		} else {
			mProgressBar.setVisibility(View.INVISIBLE);
			layout_fetch_file.setVisibility(View.VISIBLE);
			layout_no_residual.setVisibility(View.VISIBLE);
			layout_listview.setVisibility(View.INVISIBLE);
			// percentView.setVisibility(View.VISIBLE);
			getMemoryInfo();
			txtResidualFile.setTextSize(17);
			mBtnClean.setText(R.string.btn_rescan);
			// txtResidualFile.setVisibility(View.VISIBLE);
			txtResidualFile.setText(R.string.No_ResidualFile_Detected);
			txtResidualFile.setGravity(Gravity.CENTER);
		}
		txtResidualFile.setGravity(Gravity.CENTER);
	}

	public void deleteResidualFile() {
		if (!listAdsFolder.isEmpty()) {
			for (Iterator<ResidualFileItem> itrTemp = listAdsFolder.iterator(); itrTemp
					.hasNext();) {
				ResidualFileItem item = itrTemp.next();
				// itr.getClass().
				if (item.isWillBeDelete()) {
					File fileItem = new File(item.getAbsolutePath());
					
					Log.i("File Temp",
							"Delete File:"
									+ fileItem.getAbsolutePath()
									+ "with Result"
									+ fileItem.delete());
					itrTemp.remove();
				}
			}
		}
		if (!listAppLeftOver.isEmpty()) {
			for (Iterator<ResidualFileItem> itrTemp = listAppLeftOver
					.iterator(); itrTemp.hasNext();) {
				ResidualFileItem item = itrTemp.next();
				// itr.getClass().
				if (item.isWillBeDelete()) {
					File fileItem = new File(item.getAbsolutePath());
					Log.i("File Temp",
							"Delete File:"
									+ fileItem.getAbsolutePath()
									+ "with Result"
									+ fileItem.delete());
					itrTemp.remove();
				}
			}

		}

		if (!listBigFile.isEmpty()) {
			for (Iterator<ResidualFileItem> itrTemp = listBigFile.iterator(); itrTemp
					.hasNext();) {
				ResidualFileItem item = itrTemp.next();
				if (item.isWillBeDelete()) {
					File fileItem = new File(item.getAbsolutePath());
					Log.i("File Temp",
							"Delete File:"
									+ fileItem.getAbsolutePath()
									+ "with Result"
									+ fileItem.delete());
					itrTemp.remove();
				}
			}

		}
		if (!listThumbnailFile.isEmpty()) {

			for (Iterator<ResidualFileItem> itr = listThumbnailFile.iterator(); itr
					.hasNext();) {
				ResidualFileItem item = itr.next();
				// itr.getClass().
				if (item.isWillBeDelete()) {
					File fileItem = new File(item.getAbsolutePath());
					Log.i("File Temp",
							"Delete File:"
									+ fileItem.getAbsolutePath()
									+ "with Result"
									+ fileItem.delete());
					itr.remove();
				}
			}
		}
		// mBtnClean.setEnabled(true);
		mBtnClean.setText(R.string.btn_rescan);

	}

	Boolean isNotCheck = true;

	@SuppressLint("NewApi")
	OnClickListener btnCleanClickListener = new OnClickListener() {

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			isNotCheck = true;
			String textBtnClean = (String) mBtnClean.getText();
			if (textBtnClean.equalsIgnoreCase("Clean")) {
				deleteResidualFile();
				refreshLayout();
			} else if (textBtnClean.equalsIgnoreCase("Stop")) {
				isStopped = true;
				mAsynctask.cancel(true);
				refreshLayout();
			} else {
				isStopped = false;
				mBtnClean.setText(R.string.btn_stop);
				txtResidualFile.setGravity(Gravity.LEFT);
				txtResidualFile.setGravity(Gravity.CENTER_VERTICAL);
				mAsynctask = new FetchResidualAsynctask();
				mAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			}
		}
	};

	public boolean checkSizeFile(File file) {
		if (file.canRead() && file.exists()) {
			long fileSize = file.length();
			if (fileSize >= (10 * 1024 * 1024)) {
				return true;
			}
		}
		return false;
	}

	public void getFilePath(final File path) {
		synchronized (this) {
			final String namePath = path.getName();
			if (!isStopped) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!isStopped) {
							mProgressBar.setProgress(++processindex);
							txtResidualFile.setText(namePath);
						} else {
							return;
						}
					}
				});
			}
			if (!isStopped) {

				if (path.canRead() && path.exists()) {

					float sizeFile = path.length();
					ResidualFileItem item = new ResidualFileItem(
							path.getAbsolutePath(), path.getName(), false,
							formatStringSize(sizeFile));
					if (isThumbnailFile(path)) {

						item.setFileName("Gallery Thumbnail");

						item.setAbsolutePath("sdcard/DCIM/.thumbnails/");
						float totalSizeFileThumbnail = 0;
						if (listThumbnailFile.size() > 0) {
							ResidualFileItem itemThumbnail = listThumbnailFile
									.get(0);
							listThumbnailFile.clear();
							totalSizeFileThumbnail = deFormartSizeFile(itemThumbnail
									.getSizeFile());

						}
						// File fileThumbnail = new File("DCIM/.thu")

						totalSizeFileThumbnail += sizeFile;
						Log.i("Add Thumbnail File",
								"File: "
										+ item.getAbsolutePath()
										+ " With Size: "
										+ formatStringSize(totalSizeFileThumbnail));
						item.setSizeFile(formatStringSize(totalSizeFileThumbnail));

						listThumbnailFile.add(item);
						return;
					}
					if (checkSizeFile(path)) {
						listBigFile.add(item);
						return;
					}

				}
			}
		}
	}

	public static List<File> findAllStorage() {
		if (!listStorage.isEmpty()) {
			return listStorage;
		} else {
			listStorage = new ArrayList<File>();
			try {
				Process process = new ProcessBuilder().command("mount")
						.redirectErrorStream(true).start();

				process.waitFor();

				InputStream is = process.getInputStream();
				BufferedReader isr = new BufferedReader(new InputStreamReader(
						is));

				String line = null;
				while ((line = isr.readLine()) != null) {
					if (line.contains("vold") && line.contains("vfat")) {
						String[] blocks = line.split("\\s");
						if (!blocks[1].equals("/mnt/secure/asec")) {
							File tmp = new File(blocks[1]);
							if (tmp.exists()) {
								listStorage.add(tmp);
							}
						}
					}
				}
			} catch (Throwable t) {
				Log.e("ERROR FINDALLSTORAGE", t.toString());
			}
			if (listStorage.isEmpty()) {
				File external = Environment.getExternalStorageDirectory();
				listStorage.add(external);
			}
			return listStorage;
		}
	}

	public static File findRemovableStorage() {

		if (fileExternal != null && fileExternal.exists()) {
			return fileExternal;
		}
		fileExternal = null;
		String result = null;
		try {
			Process process = new ProcessBuilder().command("mount")
					.redirectErrorStream(true).start();

			process.waitFor();

			InputStream is = process.getInputStream();
			BufferedReader isr = new BufferedReader(new InputStreamReader(is));

			String line = null;
			while ((line = isr.readLine()) != null) {
				Log.i("LINE", line);
				// if (-1 != line.indexOf("vold") && -1 != line.indexOf("vfat"))
				// {
				Log.d("findRemovableStorage", line);
				if (line.contains("vold") && line.contains("vfat")) {
					String[] blocks = line.split("\\s");
					if (blocks.length > 2) {
						result = blocks[1];
					}
				}
			}
			if (result != null) {
				fileExternal = new File(result);
				if (!fileExternal.exists()) {
					fileExternal = null;
				}
			}
		} catch (Throwable t) {
			Log.e("ERROR", "Unable to find external mount point");
		}
		if (fileExternal == null) {
			fileExternal = Environment.getExternalStorageDirectory();
		}
		return fileExternal;
	}

	public boolean isThumbnailFile(File file) {
		if (file.getAbsolutePath().contains("DCIM/.thumbnails/")) {

			return true;
		}

		return false;
	}

	public boolean isAndroidFile(File file) {
		for (String namePath : listSystemFile) {
			if (namePath.equalsIgnoreCase(file.getName())) {
				return true;
			}
		}
		if (file.getName().endsWith(".gz")) {
			return true;
		}
		return false;
	}

	public boolean isAdsFolder(File file) {
		if (file.isDirectory()) {
			if (file.getAbsolutePath().endsWith(" Ads")) {
				return true;
			}
		}
		return false;
	}

	public boolean isAppLeftOver(File file) {
		if (file.isDirectory()) {
			for (int i = 0; i < listAppName.size(); i++) {
				if (file.getName().contains(listAppName.get(i))) {
					return true;
				}
			}

		}
		return false;
	}

	@SuppressLint({ "NewApi", "DefaultLocale" })
	public String formatStringSize(float size) {
		DecimalFormat df = new DecimalFormat("0.0");
		df.setRoundingMode(RoundingMode.UP);
		if (size / (1024 * 1024 * 1024) >= 1) {
			return df.format(size / (1024 * 1024 * 1024)) + "GB";
		}
		if (size / (1024 * 1024) >= 1) {
			{

				return df.format(size / (1024 * 1024)) + "MB";
			}
		}
		if (size / (1024) > 0) {

			return df.format(size / 1024) + "KB";
		}
		return null;
	}

	public float deFormartSizeFile(String strSize) {
		float sizeFile = 0;
		if (strSize.endsWith("GB")) {
			sizeFile = Float.parseFloat(strSize.replace("GB", "")) * 1024 * 1024 * 1024;
		} else if (strSize.endsWith("MB")) {
			sizeFile = Float.parseFloat(strSize.replace("MB", "")) * 1024 * 1024;
		} else if (strSize.endsWith("KB")) {
			sizeFile = Float.parseFloat(strSize.replace("KB", ""));
		}

		return sizeFile;
	}

	public void showLayoutListResidualFile() {
		float totalSize = 0;
		float detailSize = 0;
		int numberOfFolders = 0;
		if (listAdsFolder.size() > 0) {
			for (int i = 0; i < listAdsFolder.size(); i++) {
				detailSize = deFormartSizeFile(listAdsFolder.get(i)
						.getSizeFile());
				numberOfFolders++;
				totalSize += detailSize;

			}
		}
		if (listAppLeftOver.size() > 0) {
			for (int i = 0; i < listAppLeftOver.size(); i++) {
				detailSize = deFormartSizeFile(listAppLeftOver.get(i)
						.getSizeFile());
				numberOfFolders++;
				totalSize += detailSize;
			}
		}
		if (listThumbnailFile.size() > 0) {
			for (int i = 0; i < listThumbnailFile.size(); i++) {
				detailSize = deFormartSizeFile(listThumbnailFile.get(i)
						.getSizeFile());
				numberOfFolders++;
				totalSize += detailSize;
			}
		}
		if (listBigFile.size() > 0) {
			for (int i = 0; i < listBigFile.size(); i++) {
				detailSize = deFormartSizeFile(listBigFile.get(i).getSizeFile());
				numberOfFolders++;
				totalSize += detailSize;
			}
		}
		if (formatStringSize(totalSize) == null) {
			txtResidualFile.setText("Folders: " + numberOfFolders
					+ "   Occupied: 0KB");

		} else
			txtResidualFile.setText("Folders: " + numberOfFolders
					+ "   Occupied:" + formatStringSize(totalSize));

	}

	@SuppressLint("NewApi")
	private class FetchResidualAsynctask extends AsyncTask<Void, Integer, Void> {
		FileScanner fileMachine;

		public FileScanner getFileMachine() {
			return fileMachine;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			listAdsFolder.clear();
			listAppLeftOver.clear();
			listBigFile.clear();
			listThumbnailFile.clear();
			// Get List App Name
			List<PackageInfo> packages;
			PackageManager pm;
			if (!isStopped) {
				pm = getActivity().getPackageManager();
				packages = pm
						.getInstalledPackages(PackageManager.GET_META_DATA);
				for (int j = 0; j < packages.size(); j++) {
					listAppName.add((String) packages.get(j).applicationInfo
							.loadLabel(pm));
				}
			}
			findAllStorage();

		}

		public void CalculateFiles() {
			for (int i = 0; i < listStorage.size(); i++) {
				Log.i("No of Files", ": " + listFile.size());
				fileMachine = new FileScanner(25);
				fileMachine.scan(listStorage.get(i));
				do {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} while (!fileMachine.isFinished());
				// listFile.addAll(fileMachine.getListFile());
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			final long start = System.currentTimeMillis();
			CalculateFiles();
			// numberOfFiles = listFile.size();

			processindex = 0;
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					mBtnClean.setText(R.string.btn_rescan);
					mBtnClean.setEnabled(false);
					txtResidualFile.setText("");
					mProgressBar.setVisibility(View.VISIBLE);
					mProgressBar.setProgress(0);
					mBtnClean.setText(R.string.btn_stop);
					mBtnClean.setEnabled(true);
					mProgressBar.setMax(numberOfFiles);
				}
			});

			// for (int i = 0; i < listFile.size(); i++) {
			//
			// }
			long elapsed = System.currentTimeMillis();
			Log.i("elapsed Time", "elapsed do: " + (elapsed - start));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			resetAdapter();
			mProgressBar.setProgress(0);
			refreshLayout();
			isFinish = true;

		}

		@Override
		protected void onCancelled(Void result) {
			super.onCancelled(result);
			resetAdapter();
			mProgressBar.setProgress(0);
			refreshLayout();

		}

		public class FileScanner {

			// subfolders to explore
			private final Queue<File> exploreList = new ConcurrentLinkedQueue<File>();

			private long fileCounter = 0;

			List<File> listFile = new ArrayList<File>();

			public void count() {
				fileCounter++;
			}

			public long getCounter() {
				return this.fileCounter;
			}

			public List<File> getListFile() {
				return this.listFile;
			}

			int[] threads;

			public FileScanner(int numberOfThreads) {
				threads = new int[numberOfThreads];
				for (int i = 0; i < threads.length; i++) {
					threads[i] = -1;
				}
			}

			void scan(File file) {

				// add the first one to the list
				exploreList.add(file);

				for (int i = 0; i < threads.length; i++) {
					if (!isStopped) {

					}
					FileExplorer explorer = new FileExplorer(i, this);
					Thread t = new Thread(explorer);
					t.start();
				}

				Thread waitToFinish = new Thread(new Runnable() {

					@Override
					public void run() {

						boolean working = true;
						while (working && !isStopped) {
							working = false;

							for (int i = 0; i < threads.length; i++) {
								if (threads[i] == -1) {
									working = true;
									break;
								}
							}

							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}
				});

				waitToFinish.start();
			}

			public void done(int id, int counter) {
				threads[id] = counter;
			}

			public boolean isFinished() {
				for (int i = 0; i < threads.length; i++) {
					if (threads[i] == -1) {
						return false;
					}
				}
				Log.i("numberOfFiles", "NUmber: " + getCounter());
				return true;
			}

			class FileExplorer implements Runnable {

				public int counter = 0;
				public FileScanner owner;
				private int id;

				public FileExplorer(int id, FileScanner owner) {
					this.id = id;
					this.owner = owner;
				}

				@Override
				public void run() {
					while (!owner.exploreList.isEmpty() && !isStopped) {

						// get the first from the list
						try {
							File file = (File) owner.exploreList.remove();

							if (file.exists()) {

								if (!file.isDirectory()) {
									count();
									getFilePath(file);
								} else {
									float sizeFile = file.length();
									ResidualFileItem item = new ResidualFileItem(
											file.getAbsolutePath(),
											file.getName(), false,
											formatStringSize(sizeFile));
									if (isAdsFolder(file)) {
										listAdsFolder.add(item);
									} else if (isAppLeftOver(file)) {
										listAppLeftOver.add(item);
									} else {
										File[] arr = file.listFiles();
										if (arr != null) {
											for (int i = 0; i < arr.length; i++) {
												owner.exploreList.add(arr[i]);
											}
										}
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							// silent kill :)
						}
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					owner.done(id, counter);
				}
			}
		}

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isStopped = true;
	}

	private void expandAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			lv_residual.expandGroup(i);
		}
	}

	private void collapseAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			lv_residual.collapseGroup(i);
		}
	}

	private OnChildClickListener ListViewOnChildClicked = new OnChildClickListener() {

		public boolean onChildClick(ExpandableListView parent, View v,
				final int groupPosition, final int childPosition, long id) {
			Log.i("onChildClick", "childPosition: " + childPosition);
			AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
			final ResidualFileItem item = groupList.get(groupPosition)
					.getResidualFileItemsList().get(childPosition);

			alert.setMessage("Name: " + item.getFileName() + "\n" + "Size: "
					+ item.getSizeFile() + "\n" + "Path:"
					+ item.getAbsolutePath() + "\n");

			alert.setPositiveButton("Clean",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							File fileItem = new File(item.getAbsolutePath());
							String nameGrouped = groupList.get(groupPosition)
									.getName();
							if (nameGrouped.equalsIgnoreCase("Big Files")) {
								listBigFile.remove(childPosition);
								Log.i("File Temp",
										"Delete File:"
												+ fileItem.getAbsolutePath()
												+ "with Result"
												+ fileItem.delete());
								refreshLayout();
							} else if (nameGrouped
									.equalsIgnoreCase("Thumbnails")) {
								File ThumbnailFolder = new File(item
										.getAbsolutePath());
								File[] listFileThumbnail = ThumbnailFolder
										.listFiles();
								if (listFileThumbnail != null) {
									for (int i = 0; i < listFileThumbnail.length; i++) {
										File file = new File(
												listFileThumbnail[i]
														.getAbsolutePath());
										Log.i("Thumbnail",
												"Delete File:"
														+ fileItem
																.getAbsolutePath()
														+ "with Result: "
														+ fileItem.delete());
									}
								}
								listThumbnailFile.remove(childPosition);
								refreshLayout();

							} else if (nameGrouped
									.equalsIgnoreCase("App LeftOvers")) {
								listAppLeftOver.remove(childPosition);
								Log.i("File Temp",
										"Delete File:"
												+ fileItem.getAbsolutePath()
												+ "with Result"
												+ fileItem.delete());
								refreshLayout();
							} else {
								listAdsFolder.remove(childPosition);
								Log.i("File Temp",
										"Delete File:"
												+ fileItem.getAbsolutePath()
												+ "with Result"
												+ fileItem.delete());
								refreshLayout();
							}

						}
					});
			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			AlertDialog alertDialog = alert.create();
			alertDialog.show();

			return true;
		}

	};

	public void resetAdapter() {
		groupList.clear();
		residual_list.clear();
		if (listBigFile.size() > 0) {
			GroupHeaderDetail group1 = new GroupHeaderDetail();
			group1.setName("Big Files");
			group1.setResidualFileItemList((ArrayList<ResidualFileItem>) listBigFile);
			groupList.add(group1);
			residual_list.add(listBigFile);
		}

		if (listThumbnailFile.size() > 0) {
			GroupHeaderDetail group = new GroupHeaderDetail();
			group.setResidualFileItemList((ArrayList<ResidualFileItem>) listThumbnailFile);
			group.setName("Thumbnails");
			groupList.add(group);
			residual_list.add(listThumbnailFile);
		}
		if (listAppLeftOver.size() > 0) {
			GroupHeaderDetail group = new GroupHeaderDetail();
			group.setName("App LeftOvers");
			group.setResidualFileItemList((ArrayList<ResidualFileItem>) listAppLeftOver);
			groupList.add(group);
			residual_list.add(listAppLeftOver);
		}
		if (listAdsFolder.size() > 0) {
			GroupHeaderDetail group = new GroupHeaderDetail();
			group.setName("Ads Folder");
			group.setResidualFileItemList((ArrayList<ResidualFileItem>) listAdsFolder);
			groupList.add(group);
			residual_list.add(listAdsFolder);
		}
		listAdapter.notifyDataSetChanged();
	}
}
