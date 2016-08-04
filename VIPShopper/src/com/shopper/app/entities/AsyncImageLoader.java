package com.shopper.app.entities;

import com.shopper.app.R;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

/**
 * @author use for loading product image Asynchronously
 * 
 */

public class AsyncImageLoader extends AsyncTask<View, Void, BitmapDrawable> {
	View view;

	@Override
	protected BitmapDrawable doInBackground(View... tvs) {
		view = tvs[0];
		return downloadImage(view.getTag().toString());
	}

	@Override
	protected void onPostExecute(BitmapDrawable result) {
		
		if(result!=null && view!=null){
			if(view instanceof ImageView){
				((ImageView)view).setImageDrawable(result.getConstantState().newDrawable());
			}else {
//				view.setBackgroundDrawable(result.getConstantState().newDrawable());
				view.setBackgroundDrawable(new BitmapDrawable(null, CommonTask.getRoundedCornerBitmap(result.getBitmap())));
			}
		}
		else if(result == null && view != null)
			view.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.empty));
	}

	private BitmapDrawable downloadImage(String id) {
		// Load the prodct image
		return CommonTask.getDrawableImage(
				String.format(CommonURL.getInstance().ProductImageURL, id), id);
	}
	
	 
}
