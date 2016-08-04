package com.shopper.app.entities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopper.app.R;
import com.shopper.app.activities.DisplayItemDetails;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.JSONfunctions;

public class DiscoutWidgetImageTask extends
		AsyncTask<Void, Void, Boolean> {
//	SearchDetails searchDetails;
	LinearLayout lldiscount;
	Activity parentActivity;
	ArrayList<RelativeLayout> imageList = null;
	String articleId;
	String discountGroupId;
	OnClickListener subitemlistener;
	DisplayItemDetails itemDetailsManager;

	public DiscoutWidgetImageTask(Activity parentActivity, LinearLayout llDiscountLayout, String articleId, String discountgroupid, OnClickListener subitemlistener, DisplayItemDetails initiator) {
//		searchDetails = sd;
		this.parentActivity = parentActivity;
		this.lldiscount = llDiscountLayout;
		this.articleId = articleId;
		this.discountGroupId = discountgroupid;
		this.subitemlistener = subitemlistener;
		this.itemDetailsManager = initiator;
				
	}

	@Override
	protected void onPreExecute() {
//		searchDetails.stopProgress();
		itemDetailsManager.startDiscountProgress();
		
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		/*
		 * while (!searchDetails.isArticleDetailsReady) { }
		 */
		return downloadImage();

	}

	@Override
	protected void onPostExecute(Boolean result) {
		lldiscount.setVisibility(View.INVISIBLE);
		itemDetailsManager.stopDiscountProgress();
		for (int i = 0; i < imageList.size(); i++) {
			lldiscount.addView(imageList.get(i));
		}
		Animation animation = AnimationUtils.loadAnimation(parentActivity,
				R.anim.right_to_left);
		lldiscount.startAnimation(animation);
		lldiscount.setVisibility(View.VISIBLE);
//		stopDiscountProgressBar();
	}

	private Boolean downloadImage() {
		// searchDetails.isArticleDetailsReady = false;
		imageList = new ArrayList<RelativeLayout>();

		// String id = "";
		try {
			SubArticleInquieryRoot subArticleInquieryRoot = (SubArticleInquieryRoot) JSONfunctions
					.retrieveDataFromStream(
							String.format(
									CommonURL.getInstance().BasketItemWithSameFamilyURL,
									CommonTask.getShopNumber(parentActivity),
									CommonConstraints.ClientId,
									discountGroupId),
							SubArticleInquieryRoot.class);

			if (subArticleInquieryRoot != null
					&& subArticleInquieryRoot.subArticleInquiery != null) {
				ImageView imageView = null;
				TextView textLine1 = null;
				TextView textLine2 = null;
//				final int displayIndex = searchDetails.getFlipperDisplayIndex();
				for (ArticleForSearch articleForSearch : subArticleInquieryRoot.subArticleInquiery.subArticleFamily.articleList) {
					/*
					 * if (!Search.isBackToArticle) { return false; }
					 */
					if (!articleId.equals(articleForSearch.id)) {

						LayoutInflater inflater = (LayoutInflater) parentActivity
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						RelativeLayout basketDetails = (RelativeLayout) inflater
								.inflate(R.layout.article_sub_item_view, null);
						basketDetails.setTag(articleForSearch.id);
						
						basketDetails
						.setOnClickListener(subitemlistener);
						
//						basketDetails.setBackgroundColor(Color
//								.parseColor("#FFFFFF"));
//						basketDetails
//								.setOnClickListener(new View.OnClickListener() {
//									public void onClick(View v) {
//										if (!Search.isBackToArticle) {
//											if (searchDetails.selectedSubitemCollectionSearch == null) {
//												searchDetails.selectedSubitemCollectionSearch = new Hashtable<Object, Object>();
//											} else {
//												if (searchDetails.selectedSubitemCollectionSearch
//														.containsKey(displayIndex)) {
//													RelativeLayout basketDetailsOld = (RelativeLayout) searchDetails.selectedSubitemCollectionSearch
//															.get(displayIndex);
//												}
//											}
//											searchDetails.selectedSubitemCollectionSearch
//													.put(displayIndex, v);
//											searchDetails.updateBasket();
//											searchDetails.articleId = (String) v
//													.getTag();
//											searchDetails
//													.LoadArticleDetailsContent();
//										}
//									}
//								});

						imageView = (ImageView) basketDetails
								.findViewById(R.id.ivSubItemImage);
						imageView
								.setBackgroundDrawable(CommonTask
										.getDrawableImage(
												String.format(
														CommonURL.getInstance().ProductImageURL,
														articleForSearch.id),
												articleForSearch.id)
										.getConstantState().newDrawable());
						imageView.setScaleType(ImageView.ScaleType.FIT_XY);

						textLine1 = (TextView) basketDetails
								.findViewById(R.id.tvSubItemText1);
						textLine2 = (TextView) basketDetails
								.findViewById(R.id.tvSubItemText2);
						textLine1.setText(articleForSearch.text);
						textLine2.setText(articleForSearch.text2);
						imageList.add(basketDetails);
					}
				}
				// Search.isBackToArticle = false;
			}

		} catch (Exception e) {
			imageList = new ArrayList<RelativeLayout>();
			return false;

		}
		return true;
	}
}
