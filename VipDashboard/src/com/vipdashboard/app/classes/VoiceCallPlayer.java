package com.vipdashboard.app.classes;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.PhoneCallInformation;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class VoiceCallPlayer implements OnClickListener {
	
	Context context;
	Dialog voiceCallRecorderDialog;
	PhoneCallInformation phoneCallInformation;
	
	TextView tvHeaderTextView;
	SeekBar seekBar;
	MediaPlayer mediaPlayer;
	ImageView mPlay, mStop;
	Handler seekHandler;
	
	public VoiceCallPlayer(Context _context) {
		context = _context;
	}
	
	public void PlayVoiceCallRecorder(Object object){
		voiceCallRecorderDialog = new Dialog(context);
		voiceCallRecorderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		voiceCallRecorderDialog.setContentView(R.layout.voice_memo);
		voiceCallRecorderDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		tvHeaderTextView = (TextView) voiceCallRecorderDialog.findViewById(R.id.tvVoiceMemoHeader);
		seekBar = (SeekBar) voiceCallRecorderDialog.findViewById(R.id.seek_bar);
		mediaPlayer = new MediaPlayer();
		mPlay = (ImageView) voiceCallRecorderDialog.findViewById(R.id.play_button);
		mStop = (ImageView) voiceCallRecorderDialog.findViewById(R.id.pause_button);
		seekHandler = new Handler();
		
		if(object instanceof PhoneCallInformation){
			try{
				//tvHeaderTextView.setText(((PhoneCallInformation) object).VoiceRecordPath.toString());
				String path = ((PhoneCallInformation) object).VoiceRecordPath.toString();
				/*mediaPlayer = MediaPlayer.create(context, Uri.parse(path));
				mediaPlayer.setLooping(true);*/
				mediaPlayer.setDataSource(path);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.prepare();
				
				mPlay.setOnClickListener(this);
				mStop.setOnClickListener(this);
			}catch (Exception e) {
				e.printStackTrace();
			}			
		}else{
			try{
				String path = object.toString();
				mediaPlayer.setDataSource(path);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.prepare();
				
				mPlay.setOnClickListener(this);
				mStop.setOnClickListener(this);
			}catch (Exception e) {
				e.printStackTrace();
			}			
		}
		seekUpdation();
		voiceCallRecorderDialog.show();
	}
	
	Runnable run = new Runnable() {		 
        @Override
        public void run() {
            seekUpdation();
        }
    };
	

	private void seekUpdation() {
		seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.play_button){
			mediaPlayer.start();
		}else if(view.getId() == R.id.pause_button){
			mediaPlayer.stop();
			seekBar.setProgress(0);
		}
	}

}
