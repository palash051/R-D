package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FB_PostActivity extends Activity {
	ProgressDialog progressBar;
	DownloadableAsyncTask asyncTask;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static List<String> permissions;
	private boolean pendingPublishReauthorization = false;
	Session.StatusCallback statusCallback = new SessionStatusCallback();
	String message="";
	int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null && savedInstanceState.containsKey("MESSAGE")){
			message = savedInstanceState.getString("MESSAGE");
			//PostStatusInFacebook();
		}
		permissions = new ArrayList<String>();
		permissions.add("publish_actions");
		
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			session.addCallback(statusCallback);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForPublish(new Session.OpenRequest(this).setCallback(
						statusCallback).setPermissions(permissions));
			}
		}
		if (!session.isOpened() && !session.isClosed()) {
			session.openForPublish(new Session.OpenRequest(
					FB_PostActivity.this).setCallback(statusCallback)
					.setPermissions(permissions));
		} else {
			Session.openActiveSession(FB_PostActivity.this, true,
					statusCallback);
		}
	}
	
	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			processSessionStatus(session, state, exception);
		}
	}
	
	public void processSessionStatus(final Session session, SessionState state,
			Exception exception) {
		if (session != null && session.isOpened()) {
			if (session.getPermissions().contains(permissions.get(0))) {
				Request.newStatusUpdateRequest(session, message, new Callback() {
					
					@Override
					public void onCompleted(Response response) {
							showPublishResult(message, response.getGraphObject(), response.getError());
					}
				}).executeAsync();
			} else {
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
						FB_PostActivity.this, permissions));
			}
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("FbLogin", "Result Code is - " + resultCode + "");
		Session.getActiveSession().onActivityResult(FB_PostActivity.this,
				requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}
	
	
	private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
	    String title = null;
	    String alertMessage = null;
	    if (error == null) {
	        title = "Success";

	        alertMessage = "Post Successfully!";
	    } else {
	        title = "Error";
	        alertMessage = error.getErrorMessage();
	    }

	    new AlertDialog.Builder(this)
	            .setTitle(title)
	            .setMessage(alertMessage)
	            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    finish();
	                }
	            })
	            .show();
	}

}
