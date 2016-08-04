package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorReviewHolder {
	@SerializedName("OperatorReview")
	public OperatorReview operatorReview;
	public OperatorReviewHolder() {
		operatorReview=new OperatorReview();
	}
}
