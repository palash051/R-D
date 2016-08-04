package com.vipdashboard.app.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.FacebokPerson;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IOperatorManager;
import com.vipdashboard.app.manager.OperatorManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class SubscriberActivity extends MainActionbarBase implements OnClickListener, IAsynchronousTask {
	
	ImageView ivCountryFlag,ivsubscriber_camera,ivsubscriber_galary,ivPhotosubscriber_camera,
			ivPhotosubscriber_galary,ivContinue;
	TextView tvCompanyName, tvCompanyCountry,tvFooter,tvIdentity,tvPhoto;
	TextView tvSelectCountryName;
	EditText etYourName, etEmail, etExistingnumber;
	private String filename;
	private String Urivalue;
	byte[] byteIdentity, bytephoto;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	String imageFilePath="";
	boolean isCallFromsubscriberCamera,isCallFromsubscriberGalary, isCallFromPhotosubscriberCamera,isCallFromPhotosubscriberGalary;
	private String photoGalaryFileName;
	private String IdentityGalaryFileName;
	private String photoImageFileName;
	private String IndentityImageFileName; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe);
		
		Initialization();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		photoGalaryFileName=IdentityGalaryFileName=photoImageFileName=IndentityImageFileName = ""; 
		SetValue();
	}

	private void Initialization() {
		ivCountryFlag = (ImageView) findViewById(R.id.ivCountryFlag);
		ivsubscriber_camera = (ImageView) findViewById(R.id.ivsubscriber_camera);
		ivsubscriber_galary = (ImageView) findViewById(R.id.ivsubscriber_galary);
		ivPhotosubscriber_camera = (ImageView) findViewById(R.id.ivPhotosubscriber_camera);
		ivPhotosubscriber_galary = (ImageView) findViewById(R.id.ivPhotosubscriber_galary);
		ivContinue = (ImageView) findViewById(R.id.ivContinue);
		tvSelectCountryName = (TextView) findViewById(R.id.tvSelectCountryName);
		etYourName = (EditText) findViewById(R.id.etYourName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etExistingnumber = (EditText) findViewById(R.id.etExistingnumber);
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);
		tvFooter = (TextView) findViewById(R.id.tvFooter);
		tvIdentity = (TextView) findViewById(R.id.tvIdentity);
		tvPhoto  = (TextView) findViewById(R.id.tvPhoto);
		
		ivContinue.setOnClickListener(this);
		ivsubscriber_camera.setOnClickListener(this);
		ivsubscriber_galary.setOnClickListener(this);
		ivPhotosubscriber_camera.setOnClickListener(this);
		ivPhotosubscriber_galary.setOnClickListener(this);
		
	}
	
	private void SetValue(){
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tvSelectCountryName.setText(tMgr.getNetworkOperatorName());
		//etYourName.setText(CommonValues.getInstance().LoginUser.FullName);
		//etEmail.setText(CommonValues.getInstance().LoginUser.Email);
		/*if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
				etYourName.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name);
			if(CommonValues.getInstance().LoginUser.Facebook_Person.PrimaryEmail != null)
				etEmail.setText(CommonValues.getInstance().LoginUser.Facebook_Person.PrimaryEmail);
		}else{*/
			MyNetDatabase database = new MyNetDatabase(this);
			database.open();
			FacebokPerson facebokPerson = database.getFacebokPerson();
			database.close();
			if(facebokPerson != null){
				if(facebokPerson.Name != null)
					etYourName.setText(facebokPerson.Name);
				if(facebokPerson.PrimaryEmail != null)
					etEmail.setText(facebokPerson.PrimaryEmail);
			}
		//}
		etExistingnumber.setText(CommonValues.getInstance().LoginUser.Mobile!=null?CommonValues.getInstance().LoginUser.Mobile:"");
		
		if (!CommonValues.getInstance().SelectedCountry.CountryFlagUrl
				.isEmpty()) {
			AQuery aq = new AQuery(ivCountryFlag);
			ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
			imgOptions.targetWidth = 100;
			imgOptions.ratio = 0;
			imgOptions.round = 0;
			aq.id(ivCountryFlag).image(
					CommonValues.getInstance().SelectedCountry.CountryFlagUrl,
					imgOptions);
		}
		
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		tvFooter.setText("We don’t have service contract with your operator "+tMgr.getNetworkOperatorName().toString()+".  Still we will submit your request to the operator through email");
	
		isCallFromsubscriberCamera = isCallFromsubscriberGalary = isCallFromPhotosubscriberCamera = isCallFromPhotosubscriberGalary = false;
	}
	
	private boolean ValidationInput()
	{
		boolean isValid=true;
		
		if(etYourName.getText().toString().isEmpty())
		{
		Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();	
		isValid=false;
		return isValid;
		}
	
		else if(etEmail.getText().toString().isEmpty())
		{
		Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();	
		isValid=false;
		return isValid;
		}
		
		else if(etExistingnumber.getText().toString().isEmpty())
		{
		Toast.makeText(this, "Enter your mobile no", Toast.LENGTH_SHORT).show();	
		isValid=false;
		return isValid;
		}
		
		else if(tvIdentity.getText().toString().equals("Identity"))
		{
		Toast.makeText(this, "Please select identity to send subscribe request", Toast.LENGTH_SHORT).show();	
		isValid=false;
		return isValid;
		}
		
		else if(tvPhoto.getText().toString().equals("Photo"))
			{
			Toast.makeText(this, "Please select photo to send subscribe request", Toast.LENGTH_SHORT).show();	
			isValid=false;
			return isValid;
		}
		return isValid;
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivsubscriber_camera){
			ToakePhotoFromMobileForIdenty();
		}else if(view.getId() == R.id.ivPhotosubscriber_camera){
			ToakePhotoFromMobile();
		}else if(view.getId() == R.id.ivsubscriber_galary){
			getGalaryPhotoForSubscriber();
		}else if(view.getId() == R.id.ivPhotosubscriber_galary){
			getGalaryPhoto();
		}else{
			if(ValidationInput())
			{
				LoadInformation();
			}
		}
		
	}
	
	private void ToakePhotoFromMobileForIdenty() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(CheckExternalStorages()){
			String external_path = Environment.getExternalStorageDirectory().getPath() + "/MyNet/";
			String filePath = String.valueOf("photo.jpg");
			File cduFileDir = new File(external_path);
			if (!cduFileDir.exists())
				cduFileDir.mkdir();
			File pictureFile = new File(cduFileDir, filePath);
			IndentityImageFileName = pictureFile.getName();
			tvIdentity.setText(IndentityImageFileName);
			Uri otuputFile = Uri.fromFile(pictureFile);
			Urivalue = otuputFile.getPath().toString();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, otuputFile);
			startActivityForResult(intent, 101);
		}	
		else{
			Toast.makeText(this, "SDCard Unmounted!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void ToakePhotoFromMobile() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(CheckExternalStorages()){
			String external_path = Environment.getExternalStorageDirectory().getPath() + "/MyNet/";
			String filePath = String.valueOf("photo.jpg");
			File cduFileDir = new File(external_path);
			if (!cduFileDir.exists())
				cduFileDir.mkdir();
			File pictureFile = new File(cduFileDir, filePath);
			photoImageFileName = pictureFile.getName();
			tvPhoto.setText(photoImageFileName);
			Uri otuputFile = Uri.fromFile(pictureFile);
			Urivalue = otuputFile.getPath().toString();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, otuputFile);
			startActivityForResult(intent, 100);
		}	
		else{
			Toast.makeText(this, "SDCard Unmounted!", Toast.LENGTH_LONG).show();
		}
	}

	private boolean CheckExternalStorages() {
		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			return true;
		} else {
			return false;
		}
	}
	
	private void getGalaryPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
	    intent.setType("image/*");
	    intent.putExtra("return-data", true);
	    imageFilePath="";
	    startActivityForResult(intent, 1);
	}
	
	private void getGalaryPhotoForSubscriber(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
	    intent.setType("image/*");
	    intent.putExtra("return-data", true);
	    imageFilePath="";
	    startActivityForResult(intent, 2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		switch (requestCode) {
        case 1:
            if(requestCode == 1 && data != null && data.getData() != null){
                Uri _uri = data.getData();
                if (_uri != null) {
                    Cursor cursor = getContentResolver().query(_uri, new String[] {android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    cursor.moveToFirst();
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if(cursor.getString(column_index)!=null)
                    	imageFilePath = String.valueOf(cursor.getString(column_index));
                    if(!imageFilePath.isEmpty()){
                    	File photos= new File(imageFilePath);
                    	IdentityGalaryFileName = photos.getName();
                    	tvPhoto.setText(IdentityGalaryFileName);
                        Bitmap b = CommonTask.decodeImage(photos);
                        b = Bitmap.createScaledBitmap(b,100, 100, true);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            			//MyNetFacebookActivity.picByteValue = stream.toByteArray();
            			byteIdentity = stream.toByteArray();
                    }else{
                    	Toast.makeText(this, "Please select your image from gallery!", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
            }
            super.onActivityResult(requestCode, responseCode, data);
            break;
        case 2:
        	if(requestCode == 2 && data != null && data.getData() != null){
                Uri _uri = data.getData();
                if (_uri != null) {
                    Cursor cursor = getContentResolver().query(_uri, new String[] {android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    cursor.moveToFirst();
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if(cursor.getString(column_index)!=null)
                    	imageFilePath = String.valueOf(cursor.getString(column_index));
                    if(!imageFilePath.isEmpty()){
                    	File photos= new File(imageFilePath);
                    	photoGalaryFileName = photos.getName();
                    	
                    	tvIdentity.setText(photoGalaryFileName);
                        Bitmap b = CommonTask.decodeImage(photos);
                        b = Bitmap.createScaledBitmap(b,100, 100, true);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            			//MyNetFacebookActivity.picByteValue = stream.toByteArray();
                        bytephoto = stream.toByteArray();
                    } else{
                    	Toast.makeText(this, "Please select your image from gallery!", Toast.LENGTH_SHORT).show();
                    }                   
                    cursor.close();
                }
            }
            super.onActivityResult(requestCode, responseCode, data);
        	break;
        case 100:
        	if(requestCode == 100){
        		int width = 100;
				int height = 100;
				
				BitmapFactory.Options factory = new Options();
				factory.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(Urivalue, factory);
				int imagewidth = factory.outWidth;
				int imageheight = factory.outHeight;
				int scalfactor = Math.min(imagewidth/width, imageheight/height);
				factory.inJustDecodeBounds = false;
				factory.inSampleSize = scalfactor;
				factory.inPurgeable = true;
				Bitmap bitmap = BitmapFactory.decodeFile(Urivalue, factory);
				if(bitmap != null){
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    			//MyNetFacebookActivity.picByteValue = stream.toByteArray();
	    			bytephoto = stream.toByteArray();
				}
    		}
        	super.onActivityResult(requestCode, responseCode, data);
        	break;
        case 101:
        	if(requestCode == 101){
        		int width = 100;
				int height = 100;
				
				BitmapFactory.Options factory = new Options();
				factory.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(Urivalue, factory);
				int imagewidth = factory.outWidth;
				int imageheight = factory.outHeight;
				int scalfactor = Math.min(imagewidth/width, imageheight/height);
				factory.inJustDecodeBounds = false;
				factory.inSampleSize = scalfactor;
				factory.inPurgeable = true;
				Bitmap bitmap = BitmapFactory.decodeFile(Urivalue, factory);
				if(bitmap != null){
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    			//MyNetFacebookActivity.picByteValue = stream.toByteArray();
					byteIdentity = stream.toByteArray();
				}
    		}
        	super.onActivityResult(requestCode, responseCode, data);
        	break;
        }
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Submitting to operator...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IOperatorManager operatorManager = new OperatorManager();
		return operatorManager.setPhoneAppsDataPost(CommonValues.getInstance().CompanyId, MyNetService.phoneId, etExistingnumber.getText().toString(), 
				tvSelectCountryName.getText().toString(), etYourName.getText().toString(), etEmail.getText().toString(), byteIdentity, bytephoto, 0, "", 0);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		
	}

}
