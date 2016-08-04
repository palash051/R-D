package com.vipdashboard.app.base;

import java.io.File;
import java.io.IOException;

import android.R.bool;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneCallReceiver extends BroadcastReceiver {
	File audiofile = null;
	final MediaRecorder Callrecorder = new MediaRecorder();
	boolean recordStarted;
	TelephonyManager telManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		/*telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);*/
	}

	private final PhoneStateListener phoneListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			try {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING: {
					//
					break;
				}
				case TelephonyManager.CALL_STATE_OFFHOOK: {
					File sampleDir = Environment.getExternalStorageDirectory();
					try {
						audiofile = File.createTempFile("MyNet", ".3gp", sampleDir);
					} catch (IOException e) {
						// Log.e(TAG,"sdcard access error");
						return;
					}

					Callrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
					Callrecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
					Callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					Callrecorder.setOutputFile(audiofile.getAbsolutePath());

					try {
						Callrecorder.prepare();
					} catch (IllegalStateException e) {
						System.out.println("Error is happened here in Prepare Method1");
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {

						// throwing I/O Exception
						System.out.println("Error is happened here in Prepare Method2");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {

						Callrecorder.start();
						recordStarted = true;
					} catch (IllegalStateException e) {
						e.printStackTrace();
						// Here it is thorowing illegal State exception
						System.out.println("Error is happened here in Start Method");
					}
					break;
				}
				case TelephonyManager.CALL_STATE_IDLE: {
					if (recordStarted) {
						Callrecorder.stop();
						Callrecorder.release();
						recordStarted = false;
					}
					break;
				}
				default: {
				}
				}
			} catch (Exception ex) {
			}
		}
	};

	/*
	 * protected void processaudiofile(File audiofile ) { ContentValues values =
	 * new ContentValues(3); long current = System.currentTimeMillis();
	 * values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
	 * values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
	 * values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
	 * values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
	 * ContentResolver contentResolver = getContentResolver();
	 * 
	 * Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; Uri newUri =
	 * contentResolver.insert(base, values);
	 * 
	 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	 * }
	 */
}
