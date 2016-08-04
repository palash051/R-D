package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.List;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.vipdashboard.app.base.MyNetApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class FB_ShareActivity extends Activity{
	String profile_path ="";
	String description = "";
	private static List<String> permissions;
	Session.StatusCallback statusCallback = new SessionStatusCallback();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.containsKey("PIC_LINK") && savedInstanceState.containsKey("DESCRIPTION")){
			profile_path = savedInstanceState.getString("PIC_LINK");
			description = savedInstanceState.getString("DESCRIPTION");
		}
		
		/***** FB Permissions *****/
		permissions = new ArrayList<String>();
		permissions
				.add("email,user_birthday,user_groups,user_relationships,"
						+ "user_about_me,user_religion_politics,user_website,user_work_history,user_education_history");
		/***** End FB Permissions *****/
		
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
				session.openForRead(new Session.OpenRequest(this).setCallback(
						statusCallback).setPermissions(permissions));
			}
		}
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(
					FB_ShareActivity.this).setCallback(statusCallback)
					.setPermissions(permissions));
		} else {
			Session.openActiveSession(FB_ShareActivity.this, true,
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
			if (session.getPermissions().contains("email")) {
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							publishFeedDialog();
						}
					}
				}).executeAsync();
			} else {
				session.requestNewReadPermissions(new Session.NewPermissionsRequest(
						FB_ShareActivity.this, permissions));
			}
		}
	}

	/********** Activity Methods **********/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("FbLogin", "Result Code is - " + resultCode + "");
		Session.getActiveSession().onActivityResult(FB_ShareActivity.this,
				requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Bundle savedInstanceState = getIntent().getExtras();
		if(savedInstanceState != null && savedInstanceState.containsKey("PIC_LINK") && savedInstanceState.containsKey("DESCRIPTION")){
			profile_path = savedInstanceState.getString("PIC_LINK");
			description = savedInstanceState.getString("DESCRIPTION");
		}
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
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
	
	private void publishFeedDialog() {
		//String message = etMessageBoxContact.getText().toString();		
		Bundle postParams = new Bundle();
        postParams.putString("name", "Care Apps");
        postParams.putString("description", description);
        postParams.putString("picture", profile_path);
 
	    WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,com.facebook.Session.getActiveSession(),postParams))
	        .setOnCompleteListener(new OnCompleteListener() {
	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(FB_ShareActivity.this,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                        onBackPressed();
	                    } else {
	                        Toast.makeText(FB_ShareActivity.this, 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                        onBackPressed();
	                    }
	                } 
	                else if (error instanceof FacebookOperationCanceledException) {
	                    Toast.makeText(FB_ShareActivity.this, 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                    onBackPressed();
	                } 
	                else {
	                    Toast.makeText(FB_ShareActivity.this, 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                    onBackPressed();
	                }
	            }
	        })
	        .build();
	    feedDialog.show();
	}

}
