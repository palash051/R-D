package com.vipdashboard.app.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;

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
			
			FileExplorer explorer = new FileExplorer(i, this);
			Thread t = new Thread(explorer);
			t.start();
		}

		Thread waitToFinish = new Thread(new Runnable() {

			@Override
			public void run() {

				boolean working = true;
				while (working) {
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
		Log.i("THREAD", "THREAD " + id + " SCAN DONE");
		threads[id] = counter;
	}

	public boolean isFinished() {
		for (int i = 0; i < threads.length; i++) {
			if (threads[i] == -1) {
				return false;
			}
		}
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
			while (!owner.exploreList.isEmpty()) {

				// get the first from the list
				try {
					File file = (File) owner.exploreList.remove();

					if (file.exists()) {

						if (!file.isDirectory()) {
							count();
							listFile.add(file);
						} else {

							// add the files to the queue
							File[] arr = file.listFiles();
							if (arr != null) {
								for (int i = 0; i < arr.length; i++) {
									owner.exploreList.add(arr[i]);
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