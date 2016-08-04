package com.vipdashboard.app.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.OnSwipeTouchListener;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.manager.FacebookManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SocialActivity extends Activity implements OnClickListener,LocationListener, IAsynchronousTask {
	ViewFlipper vfSocial;
	private GoogleMap map;
	ListView listView;
	RelativeLayout rlSocialMap, rlSocialFeed, rlInviteFriends;
	TextView tvSocialMap, tvSocialFeed, tvInviteFriends;
	TextView tvUserExperinceStart,tvUserExperinceExperience,tvUserExperinceHistory,tvUserExperinceAssistance;
	RelativeLayout rlInviteFacebookFriends, rlInviteLinkedInFriends, rlInviteGooglePlusFriends;
	TextView tvAddFriendsAndFamily, tvImprtFriendAndFamily;
	ImageView ivLinkedIn, ivGooglePlus;
	LoginButton facebook;
	DownloadableAsyncTask asyncTask;
	ProgressBar progressBar;
	@Override
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social);
		vfSocial = (ViewFlipper) findViewById(R.id.vfSocial);
		Initialization();
		vfSocial.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeTop() {
			}
			public void onSwipeRight() {
				if (vfSocial.getDisplayedChild() != 0) {					
					switch (vfSocial.getDisplayedChild()) {					
					case 1:
						arrangeMap();
						break;
					case 2:
						arrageFeed();
						break;
					default:
						break;
					}
					vfSocial.showPrevious();
				}else{
					Intent intent = new Intent(SocialActivity.this,StatisticsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}
			public void onSwipeLeft() {
				if (vfSocial.getDisplayedChild() != vfSocial.getChildCount() - 1) {
					switch (vfSocial.getDisplayedChild()) {					
					case 0:
						arrageFeed();
						break;
					case 1:
						arrageInviteFriends();
						break;
					default:
						break;
					}
					vfSocial.showNext();
				}
			}			
			public void onSwipeBottom() {
			}
		});	
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		vfSocial.setDisplayedChild(0);
		initializeMap();
		LoadInformation();
		super.onResume();
	}
	
	private void Initialization() {
		
		tvAddFriendsAndFamily = (TextView) findViewById(R.id.tvAddFriendAndFamily);
		tvImprtFriendAndFamily = (TextView) findViewById(R.id.tvImportAddFriendAndFamily);
		ivLinkedIn = (ImageView) findViewById(R.id.linkedIn);
		ivGooglePlus = (ImageView) findViewById(R.id.google_plus);
		facebook = (LoginButton) findViewById(R.id.authButton);
		
		progressBar = (ProgressBar) findViewById(R.id.pbStatisticsSocial);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapSocialMap)).getMap();
		tvUserExperinceExperience= (TextView) findViewById(R.id.tvUserExperinceExperience);
		tvUserExperinceStart= (TextView) findViewById(R.id.tvUserExperinceStart);
		tvUserExperinceHistory= (TextView) findViewById(R.id.tvUserExperinceHistory);
		
		tvUserExperinceAssistance= (TextView) findViewById(R.id.tvUserExperinceAssistance);
		
		tvUserExperinceExperience.setOnClickListener(this);
		tvUserExperinceStart.setOnClickListener(this);
		tvUserExperinceHistory.setOnClickListener(this);
		tvUserExperinceAssistance.setOnClickListener(this);
		
		rlInviteFacebookFriends = (RelativeLayout) findViewById(R.id.rlFacebookButton);
		rlInviteLinkedInFriends = (RelativeLayout) findViewById(R.id.rlLinkedINButton);
		rlInviteGooglePlusFriends = (RelativeLayout) findViewById(R.id.rlGooglePlusButton);
		
		rlSocialMap = (RelativeLayout) findViewById(R.id.rlSocialMap);
		rlSocialFeed = (RelativeLayout) findViewById(R.id.rlSocialFeed);
		rlInviteFriends = (RelativeLayout) findViewById(R.id.rlInviteFriends);
		
		tvSocialMap = (TextView) findViewById(R.id.tvSocialMap);
		tvSocialFeed = (TextView) findViewById(R.id.tvSocialFeed);
		tvInviteFriends = (TextView) findViewById(R.id.tvInviteFriends);
		
		listView = (ListView) findViewById(R.id.lvSocialFeed);
		
		rlSocialMap.setOnClickListener(this);
		rlSocialFeed.setOnClickListener(this);
		rlInviteFriends.setOnClickListener(this);
		
		rlInviteFacebookFriends.setOnClickListener(this);
		rlInviteLinkedInFriends.setOnClickListener(this);
		rlInviteGooglePlusFriends.setOnClickListener(this);
		
		tvAddFriendsAndFamily.setOnClickListener(this);
		facebook.setOnClickListener(this);
		ivLinkedIn.setOnClickListener(this);
		ivGooglePlus.setOnClickListener(this);
	}	

	private void arrangeMap() {
		tvSocialMap.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvSocialFeed.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvInviteFriends.setBackgroundColor(getResources().getColor(
				R.color.value_text));
	}
	private void arrageInviteFriends() {
		tvSocialMap.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvSocialFeed.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvInviteFriends.setBackgroundColor(getResources().getColor(
				R.color.header_text));
	}
	private void arrageFeed() {
		tvSocialMap.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvSocialFeed.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvInviteFriends.setBackgroundColor(getResources().getColor(
				R.color.value_text));
	}
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlSocialMap) {
			arrangeMap();
			vfSocial.setDisplayedChild(0);
		} else if (view.getId() == R.id.rlSocialFeed) {
			arrageFeed();
			vfSocial.setDisplayedChild(1);
		} else if (view.getId() == R.id.rlInviteFriends) {
			arrageInviteFriends();
			vfSocial.setDisplayedChild(2);
		}else if(view.getId()==R.id.tvUserExperinceExperience){
			Intent intent = new Intent(this,NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvUserExperinceStart){
			Intent intent = new Intent(this,NetworkSelfcareMyExperinceShowMoreActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvUserExperinceHistory){
			Intent intent = new Intent(this,StatisticsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.tvUserExperinceAssistance){
			Intent intent = new Intent(this,AssistanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.rlFacebookButton){
			InviteInFacebook();
		}else if(view.getId()==R.id.rlLinkedINButton){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId()==R.id.rlGooglePlusButton){
			Intent intent = new Intent(this,DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.tvAddFriendAndFamily){
			
		}else if(view.getId() == R.id.authButton){
			
		}else if(view.getId() == R.id.linkedIn){
			
		}else if(view.getId() == R.id.google_plus){
			
		}
	}
	
	private void InviteInFacebook() {
		try{
			com.facebook.Session session = com.facebook.Session.getActiveSession();
			if(session != null && session.isOpened()){
				com.facebook.Session.openActiveSession(this, false, new StatusCallback() {					
					@Override
					public void call(final com.facebook.Session session, SessionState state,
							Exception exception) {
						if(session.isOpened()){
							Request.newMeRequest(session, new GraphUserCallback() {								
								@Override
								public void onCompleted(GraphUser user, Response response) {
									publishFeedDialog();
								}
							}).executeAsync();
						}
					}
				});
			}else{
				try{
					com.facebook.Session.openActiveSession(this, true, new com.facebook.Session.StatusCallback() {
							@Override
							public void call(final com.facebook.Session session, SessionState state, Exception exception) {
								if(session.isOpened()){									
									Request.newMeRequest(session, new GraphUserCallback() {								
										@Override
										public void onCompleted(GraphUser user, Response response) {
											publishFeedDialog();
										}
									}).executeAsync();
								}
						}
					});
					}catch (Exception e) {
						Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
						onBackPressed();
					}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		com.facebook.Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private void publishFeedDialog() {		
		Bundle params = new Bundle();
	    params.putString("message", "Learn how to make your Android apps social");

	    WebDialog requestsDialog = (
	        new WebDialog.RequestsDialogBuilder(SocialActivity.this,
	            com.facebook.Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                    FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(SocialActivity.this, 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(SocialActivity.this, 
	                                "Network Error", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(SocialActivity.this, 
	                                "Request sent",  
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(SocialActivity.this, 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    }   
	                }

	            })
	            .build();
	    requestsDialog.show();
	}
	private void initializeMap() {

		String addressText = "";
		double defaultLatitude = 0, defaultLongitude = 0;

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (loc != null) {

			
			Bitmap bmp1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.google_custom_pin);
			Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),
					R.drawable.user_icon);
			bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth()-12,bmp1.getHeight()- 55, true);			
			Bitmap bmp = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(),
					bmp1.getConfig());
			Canvas canvas = new Canvas(bmp);			
			Paint color = new Paint();
			color.setTextSize(22);
			color.setColor(Color.BLACK);
			color.setTextAlign(Paint.Align.CENTER);		
			canvas.drawBitmap(bmp1, new Matrix(), null);
			canvas.drawBitmap(bmp2, 8, 10, null);
			canvas.drawText("User", canvas.getWidth() / 2, bmp1.getHeight()- 25 , color);

			LatLng Location = new LatLng(loc.getLatitude(), loc.getLongitude());
			defaultLatitude = Location.latitude;
			defaultLongitude = Location.longitude;
			double latitude = defaultLatitude, longitude = defaultLongitude;
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			for (int index = 0; index < 5; index++) {
				try {
					List<Address> addresses = geocoder.getFromLocation(
							latitude, longitude, 1);
					if (addresses != null && addresses.size() > 0) {
						Address address = addresses.get(0);
						for (int lineIndex = 0; lineIndex < address
								.getMaxAddressLineIndex(); lineIndex++) {
							addressText = addressText
									+ address.getAddressLine(lineIndex) + ", ";
						}
						addressText = addressText + address.getLocality()
								+ ", " + address.getCountryName();
						map.addMarker(new MarkerOptions().position(Location)
								.title(addressText)
								.icon(BitmapDescriptorFactory.fromBitmap(bmp)));
						latitude = latitude + 0.004;
						longitude = longitude + 0.005;
						Location = new LatLng(latitude, longitude);
					}

				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);

		map.moveCamera(CameraUpdateFactory
				.newLatLngZoom(Defaultlocation, 14.0f));

		map.animateCamera(CameraUpdateFactory.zoomIn());

		map.animateCamera(CameraUpdateFactory.zoomTo(10));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(Defaultlocation).zoom(14).bearing(90).tilt(30).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

	}
	@Override
	public void onLocationChanged(android.location.Location arg0) {
		
	}
	@Override
	public void onProviderDisabled(String arg0) {
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
	public void LoadInformation(){
		if(asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}
	
	@Override
	public void showProgressLoader() {
		//progressBar.setVisibility(View.VISIBLE);
	}
	@Override
	public void hideProgressLoader() {
		//progressBar.setVisibility(View.GONE);
	}
	@Override
	public Object doBackgroundPorcess() {
		IFacebookManager facebookManager = new FacebookManager();
		
		return null;
	}
	@Override
	public void processDataAfterDownload(Object data) {
		
	}

}
