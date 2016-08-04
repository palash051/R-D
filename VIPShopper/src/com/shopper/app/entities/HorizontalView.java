package com.shopper.app.entities;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.shopper.app.activities.Catalogue;

public class HorizontalView extends HorizontalScrollView {
	Catalogue catalogue;

	public HorizontalView(Context context) {
		super(context);
		catalogue = (Catalogue) context;
			}

	@Override
	/**
	 * Return: What does this mean?
	 */
	public boolean onTouchEvent(MotionEvent ev) {

		/* Ignore user input if we are performing an animation */
		if (catalogue.isAnimating()) {
			return false;
		} else
			return handleOnTouchevnt(ev);
	}

	private boolean handleOnTouchevnt(MotionEvent ev) {
		/* maybe the user is not allowed to change page. Check it */
		if (catalogue.allowPageChange(ev)) {
			boolean status = false;
			try {			
				if((ev.getAction() & MotionEvent.ACTION_MASK) ==MotionEvent.ACTION_MOVE)
				{
					status = super.onTouchEvent(ev);
				}
				/*
				 * if the user leaves the screen halfway between pages, we need
				 * to adjust back when user lifts finger
				 */
				else if ((ev.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					catalogue.adjustPagePosition(catalogue
							.getDragPageSlidingDistance());

				}
			} catch (Exception e) {
				/*  Log about this exception. */
			}
			return status;

		} else {
			catalogue.setOnTouch(catalogue.selectedCataloguePage, ev);
			return false; // :sim
		}
	}

}
