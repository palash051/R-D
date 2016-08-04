package com.vipdashboard.app.activities;

import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class PlayVoiceMemoActivity extends MainActionbarBase {

	public TextView songName, startTimeField, endTimeField;
	private MediaPlayer mediaPlayer;
	private double startTime = 0;
	private double finalTime = 0;
	private Handler myHandler = new Handler();;
	private int forwardTime = 5000;
	private int backwardTime = 5000;
	private SeekBar seekbar;
	private ImageButton playButton, pauseButton;
	public int oneTimeOnly = 0;
	public static PhoneCallInformation phoneCallInformation;
	String voicepath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_memo_play);		
		if(savedInstanceState != null)
		{
			if(savedInstanceState.containsKey("VOICE_PATH")){
				voicepath = savedInstanceState.getString("VOICE_PATH");
			}
		}
		songName = (TextView) findViewById(R.id.textView4);
		startTimeField = (TextView) findViewById(R.id.textView1);
		endTimeField = (TextView) findViewById(R.id.textView2);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		playButton = (ImageButton) findViewById(R.id.imageButton1);
		pauseButton = (ImageButton) findViewById(R.id.imageButton2);
		songName.setText("Voice Record");
		if(voicepath != null)
			mediaPlayer = MediaPlayer.create(this,
					Uri.parse(voicepath));
		seekbar.setClickable(false);
		pauseButton.setEnabled(false);
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
	 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
		mediaPlayer.stop();
	}

	public void play(View view) {
		try{
			Toast.makeText(getApplicationContext(), "Playing sound",
					Toast.LENGTH_SHORT).show();
			mediaPlayer.start();
			finalTime = mediaPlayer.getDuration();
			startTime = mediaPlayer.getCurrentPosition();
			if (oneTimeOnly == 0) {
				seekbar.setMax((int) finalTime);
				oneTimeOnly = 1;
			}

			endTimeField.setText(String.format(
					"%d min, %d sec",
					TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
					TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes((long) finalTime))));
			startTimeField.setText(String.format(
					"%d min, %d sec",
					TimeUnit.MILLISECONDS.toMinutes((long) startTime),
					TimeUnit.MILLISECONDS.toSeconds((long) startTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes((long) startTime))));
			seekbar.setProgress((int) startTime);
			myHandler.postDelayed(UpdateSongTime, 100);
			pauseButton.setEnabled(true);
			playButton.setEnabled(false);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private Runnable UpdateSongTime = new Runnable() {
		public void run() {
			startTime = mediaPlayer.getCurrentPosition();
			startTimeField.setText(String.format(
					"%d min, %d sec",
					TimeUnit.MILLISECONDS.toMinutes((long) startTime),
					TimeUnit.MILLISECONDS.toSeconds((long) startTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes((long) startTime))));
			seekbar.setProgress((int) startTime);
			myHandler.postDelayed(this, 100);
		}
	};

	public void pause(View view) {
		try{
			Toast.makeText(getApplicationContext(), "Pausing sound",
					Toast.LENGTH_SHORT).show();

			mediaPlayer.pause();
			pauseButton.setEnabled(false);
			playButton.setEnabled(true);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void forward(View view) {
		try{
			int temp = (int) startTime;
			if ((temp + forwardTime) <= finalTime) {
				startTime = startTime + forwardTime;
				mediaPlayer.seekTo((int) startTime);
			} else {
				Toast.makeText(getApplicationContext(),
						"Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rewind(View view) {
		try{
			int temp = (int) startTime;
			if ((temp - backwardTime) > 0) {
				startTime = startTime - backwardTime;
				mediaPlayer.seekTo((int) startTime);
			} else {
				Toast.makeText(getApplicationContext(),
						"Cannot jump backward 5 seconds", Toast.LENGTH_SHORT)
						.show();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
