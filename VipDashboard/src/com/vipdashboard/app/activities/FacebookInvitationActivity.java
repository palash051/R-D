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

public class FacebookInvitationActivity extends Activity {
	String userID = "";
	private static List<String> permissions;
	Session.StatusCallback statusCallback = new SessionStatusCallback();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null && savedInstanceState.containsKey("ID")){
			userID = savedInstanceState.getString("ID");
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
					FacebookInvitationActivity.this).setCallback(statusCallback)
					.setPermissions(permissions));
		} else {
			Session.openActiveSession(FacebookInvitationActivity.this, true,
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
							if(userID.isEmpty())
								InviteAllFriends();
							else
								InviteIndividualFriends();
						}
					}
				}).executeAsync();
			} else {
				session.requestNewReadPermissions(new Session.NewPermissionsRequest(
						FacebookInvitationActivity.this, permissions));
			}
		}
	}

	/********** Activity Methods **********/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("FbLogin", "Result Code is - " + resultCode + "");
		Session.getActiveSession().onActivityResult(FacebookInvitationActivity.this,
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
		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.containsKey("ID")){
			userID = bundle.getString("ID");
		}
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

	private void InviteAllFriends() {
		Bundle params = new Bundle();
		params.putString("message","https://www.facebook.com/pages/Care-Solution/588513517897786");
		//params.putString("redirect_uri","http://www.conio.org/");
		//params.putString("redirect_uri","https://www.facebook.com/pages/Care-Solution/588513517897786");

		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(
				FacebookInvitationActivity.this,
				com.facebook.Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error != null) {
							if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(FacebookInvitationActivity.this,
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							} else {
								Toast.makeText(FacebookInvitationActivity.this,
										"Network Error", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							}
						} else {
							final String requestId = values
									.getString("request");
							if (requestId != null) {
								Toast.makeText(FacebookInvitationActivity.this,
										"Request sent", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							} else {
								Toast.makeText(FacebookInvitationActivity.this,
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							}
						}
					}

				}).build();
		requestsDialog.show();
	}
	
	private void InviteIndividualFriends(){
		Bundle params = new Bundle();
		params.putString("message","https://www.facebook.com/pages/Care-Solution/588513517897786");
		//params.putString("redirect_uri","http://www.conio.org/");
		//params.putString("redirect_uri","https://www.facebook.com/pages/Care-Solution/588513517897786");

		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(
				FacebookInvitationActivity.this,
				com.facebook.Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error != null) {
							if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(FacebookInvitationActivity.this,
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							} else {
								Toast.makeText(FacebookInvitationActivity.this,
										"Network Error", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							}
						} else {
							final String requestId = values
									.getString("request");
							if (requestId != null) {
								Toast.makeText(FacebookInvitationActivity.this,
										"Request sent", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							} else {
								Toast.makeText(FacebookInvitationActivity.this,
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
								onBackPressed();
							}
						}
					}

				}).setTo(userID).build();
		requestsDialog.show();
	}
}
