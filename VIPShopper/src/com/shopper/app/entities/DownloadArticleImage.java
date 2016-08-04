package com.shopper.app.entities;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;

public class DownloadArticleImage extends
		AsyncTask<CaptureActivity, Void, BitmapDrawable> {
	CaptureActivity ca;

	@Override
	protected BitmapDrawable doInBackground(CaptureActivity... caps) {
		ca = caps[0];
		return downloadImage();
	}

	@Override
	protected void onPostExecute(BitmapDrawable result) {
		if (result != null) {
			try {
				CaptureActivity.articleImageList.add(new ArticleImage(
						ca.currentPoolEanForImage, result));
			} catch (Exception oEx) {
			}
		}
		try {
			CaptureActivity.scanImageRemovingStarted = true;
			while (!CaptureActivity.scanImageAddingStarted) {
				CaptureActivity.sacannedItemListForImage.remove(0);
				CaptureActivity.scanImageRemovingStarted = false;
				break;
			}
		} catch (Exception oEx) {
		}
		ca.currentPoolEanForImage = "";

	}

	private BitmapDrawable downloadImage() {

		if (ca.getArticleImage(ca.currentPoolEanForImage) == null) {
			return CommonTask.getDrawableImage(String.format(CommonURL
					.getInstance().ProductImageURL,
					ca.currentPoolEanForImage), ca.currentPoolEanForImage);
		}
		return null;
	}
}
