package com.shopper.app.custom.controls;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.shopper.app.R;

public class DismissablePopupWindow extends android.widget.PopupWindow {
	Context ctx;
	Button btnDismiss;
	// TextView lblText;
	View popupView;

	public DismissablePopupWindow(Context context) {
		super(context);

		ctx = context;
		popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout,
				null);
		setContentView(popupView);

		btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
		// lblText = (TextView)popupView.findViewById(R.id.terms_conditions);

		setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

		// Closes the popup window when touch outside of it - when looses focus
		setOutsideTouchable(true);
		setFocusable(true);

		// Removes default black background
		setBackgroundDrawable(new BitmapDrawable());

		btnDismiss.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
				
			}
		});

		// Closes the popup window when touch it
		/*
		 * this.setTouchInterceptor(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * 
		 * if (event.getAction() == MotionEvent.ACTION_MOVE) { dismiss(); }
		 * return true; } });
		 */
	} // End constructor

	// Attaches the view to its parent anchor-view at position x and y
	public void show(View anchor, int x, int y) {
		showAtLocation(anchor, Gravity.CENTER, x, y);
	}
}