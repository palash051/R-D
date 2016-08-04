package com.shopper.app.entities;

import android.view.View;
import android.view.animation.Animation;

public final class AlphaAnimationListener implements
		Animation.AnimationListener {

	View alphaView;
	boolean visibility;

	public AlphaAnimationListener(View v, boolean visible) {
		alphaView = v;
		visibility = visible;
	}

	public void onAnimationStart(Animation animation) {
	}

	public void onAnimationEnd(Animation animation) {
		alphaView.setVisibility(visibility ? View.VISIBLE : View.GONE);
	}

	public void onAnimationRepeat(Animation animation) {
	}
}