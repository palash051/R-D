package com.vipdashboard.app.activities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.utils.CommonTask;

public class ATCommand extends MainActionbarBase implements OnClickListener {

	EditText etCommandPrompt;
	Button btSendCommand;
	String output = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd__atcommand);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Initialization();
		if (!CommonTask.isOnline(this)) {
			//CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
	}

	private void Initialization() {
		etCommandPrompt = (EditText) findViewById(R.id.etCommandPrompt);
		btSendCommand = (Button) findViewById(R.id.btSendCommand);
		btSendCommand.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.btSendCommand) {

			//parseCommand();
			 etCommandPrompt.setText(parseCommand());
		}

	}

	public String parseCommand() {
		ProcessBuilder cmd;

		String result = "";

		try {

			String[] args = { "/system/bin/service",

			"call", "phone", "2", "s16", "+8801737186095" };

			cmd = new ProcessBuilder(args);

			Process process = cmd.start();

			InputStream in = process.getInputStream();

			byte[] re = new byte[1];

			while (in.read(re) != -1) {

				result = result + new String(re);

			}

			in.close();
		} catch (IOException ex) {

			ex.printStackTrace();

		} catch (Exception e) {

		}
		return result;

		/*
		 * try {
		 * 
		 * // Executes the command.
		 * 
		 * Process process =
		 * Runtime.getRuntime().exec("logcat -b radio");///system/bin/ls /sdcard
		 * 
		 * // Reads stdout. // NOTE: You can write to stdin of the command using
		 * // process.getOutputStream(). BufferedReader reader = new
		 * BufferedReader( new InputStreamReader(process.getInputStream())); int
		 * read; char[] buffer = new char[4096]; StringBuffer output = new
		 * StringBuffer(); while ((read = reader.read(buffer)) > 0) {
		 * output.append(buffer, 0, read); } reader.close();
		 * 
		 * // Waits for the command to finish. process.waitFor();
		 * 
		 * return output.toString(); } catch (IOException e) {
		 * 
		 * throw new RuntimeException(e);
		 * 
		 * } catch (InterruptedException e) {
		 * 
		 * throw new RuntimeException(e); }
		 */

	}
}
