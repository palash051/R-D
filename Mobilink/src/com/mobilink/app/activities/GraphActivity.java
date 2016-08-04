package com.mobilink.app.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.mobilink.app.R;
import com.mobilink.app.asynchronoustask.DownloadableAsyncTask;
import com.mobilink.app.interfaces.IAsynchronousTask;
import com.mobilink.app.utils.CommonConstraints;
import com.mobilink.app.utils.CommonTask;
import com.mobilink.app.utils.CommonURL;
import com.mobilink.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GraphActivity extends Activity implements IAsynchronousTask{
	private static String imageUrl=CommonURL.getInstance().GoogleBarChartUrl+ "chds=0,70&chxr=1,0,70,5&chm=N,FF0000,0,-1,10|N,0000FF,1,-1,10&chdl=1|2&chco=407FCA,CC403D&chxl=0:|0~50|50~70|70~85|85~95|95%3E&chd=t:20,30,60,38,50|50,39,55,30,60";
	String operatorImageUrl="";
	ImageView ivGraph,ivOperatorGraphIcon,ivOperatorIcon;
	AQuery aq;
	ImageOptions imgOptions;
	ProgressDialog progress;
	DownloadableAsyncTask downloadableAsyncTask ; 
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_layout);		
		ivGraph=(ImageView)findViewById(R.id.ivGraph);	
		ivOperatorGraphIcon=(ImageView)findViewById(R.id.ivOperatorGraphIcon);	
		ivOperatorIcon=(ImageView)findViewById(R.id.ivOperatorIcon);	
	}

	@Override
	protected void onResume() {		
		super.onResume();
		if(!imageUrl.isEmpty()){
			if(!CommonValues.getInstance().SelectedCompany.CompanyLogo.isEmpty()){
				ivOperatorGraphIcon.setVisibility(View.VISIBLE);
				//ivOperatorIcon.setVisibility(View.VISIBLE);
				aq = new AQuery(ivOperatorGraphIcon);
				imgOptions = CommonValues.getInstance().defaultImageOptions;
				imgOptions.fileCache=false;
				imgOptions.memCache=false;
				imgOptions.ratio=0;				
				aq.id(ivOperatorGraphIcon).image(CommonURL.getInstance().getImageServer+CommonValues.getInstance().SelectedCompany.CompanyLogo, imgOptions);
				//aq.id(ivOperatorIcon).image(CommonURL.getInstance().getImageServer+CommonValues.getInstance().SelectedCompany.CompanyLogo, imgOptions);
				
			}else{
				ivOperatorGraphIcon.setVisibility(View.GONE);
				ivOperatorIcon.setVisibility(View.GONE);
			}
			if(downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();
		}
	}
	
	public static void setUrl(String _url){
		imageUrl=_url;
	}

	@Override
	public void showProgressLoader() {
		progress= ProgressDialog.show(this, "","", true);
		progress.setIcon(null);	
		
	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();
		
	}

	@Override
	public Object doBackgroundPorcess() {
		
		
			return CommonTask.getBitmapImage( imageUrl );
		
		
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null)
		{		
			ivGraph.setImageBitmap((Bitmap)data);
			
		}else{
			ivGraph.setImageBitmap(null);
		}
		
		
		
	}

}
