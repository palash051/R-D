package com.shopper.app.custom.controls;

import com.shopper.app.utils.ButtonTooltipHelper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button{
	public CustomButton(Context context) {
		super(context);
		initialize(context);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Regular.ttf"));
		ButtonTooltipHelper.setup(this);
	}
}