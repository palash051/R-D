package com.shopper.app.custom.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

	public CustomTextView(Context context) {
		super(context);
		initialize(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Regular.ttf"));		
	}

}