package com.vipdashboard.app.utils;

import com.vipdashboard.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

@SuppressLint("NewApi")
public class PercentView extends View {
	
	public float percent = 0;
	
	private Paint mPaints;
	private Paint mFramePaint;
	private Paint perCentPaint;
		
	private float mStart = 270;
    private float mSweep = -360;
    private float mEnd =0;
    
    private RectF mBigOval;
    
    private static final float SWEEP_INC = 4;
    
    private float centery;
    private float radius;
    float xCenter = 0;
    float yCenter = 0;
	
	 private Bitmap mBitmap = null;
	 Canvas canvas2;

	    public PercentView(Context context) {
	        super(context);
	        init();
	    }
	    public PercentView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init();
	    }
	
	 private void init() {
		 BitmapFactory.Options localOptions = new BitmapFactory.Options() ;

	     mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.percent_small_green_circle, localOptions );
	        
	     perCentPaint = new Paint();
	     perCentPaint.setColor(getResources().getColor(R.color.main_percent_text)); 
	     perCentPaint.setTextSize(getResources().getDimension(R.dimen.percenttext)); 
	     perCentPaint.setAntiAlias(true);
	     perCentPaint.setStyle(Paint.Style.FILL);
				 
		 mPaints = new Paint();
         mPaints.setStyle(Paint.Style.STROKE);
         mPaints.setAntiAlias(true);
         mPaints.setStrokeWidth(getResources().getDimension(R.dimen.width_line));
         mPaints.setColor(getResources().getColor(R.color.percent_background));

         mBigOval = new RectF(2,centery,62,centery + (float) mBitmap.getHeight());
   
		 mFramePaint = new Paint();
         mFramePaint.setAntiAlias(true);
         mFramePaint.setDither(false);
	 }
	 
	
	
	 private void drawArcs(Canvas canvas, RectF oval, boolean useCenter, Paint paint) { 
		 canvas.drawArc(oval, mStart, mSweep, useCenter, mPaints);
	 }

	 @SuppressLint("DrawAllocation")
	@Override
	    protected void onDraw(Canvas canvas) {
		   super.onDraw(canvas);
		   
		   try {
		
		   centery= ((float)(this.getHeight() - mBitmap.getHeight()))/2;
		   radius = ((float)mBitmap.getHeight())/2 - toPx(2);
		   xCenter = ((float)mBitmap.getWidth())/2;
		   yCenter = ((float)this.getHeight())/2;

	        canvas.drawBitmap(mBitmap,0,centery,null);
	        
	        mBigOval = new RectF(xCenter - radius, yCenter - radius, xCenter + radius, yCenter + radius );
	        
	        drawArcs(canvas, mBigOval, false,  mPaints);

	        mEnd = (percent/100) * 360 - 360;
	        
	        int interalPercent = (int) ( (360+ mSweep )/ 360 * 100);
	        if(mSweep>mEnd)
			{
				canvas.drawText((int) percent + "%", xCenter - toPx(15), yCenter + toPx(5), perCentPaint);
			}
	        else
	        {
	        	 canvas.drawText( interalPercent + "%", xCenter - toPx(15), yCenter + toPx(5), perCentPaint);
	        }
	        
	        while (mSweep <= mEnd )
	         {
				mSweep += SWEEP_INC;
			} ;
			
            canvas.restore();
            
			invalidate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	 
	 private float toPx(float db )
	 {
		 float px = 0;
		  px =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, db, getResources().getDisplayMetrics());
		 
		 return px;
	 }
	 


}
