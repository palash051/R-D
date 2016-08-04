package com.vipdashboard.app.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
	 
Paint paint;
Path path;
 
public MyView(Context context) {
 super(context);
 init();
}
 
public MyView(Context context, AttributeSet attrs) {
 super(context, attrs);
 init();
}
 
public MyView(Context context, AttributeSet attrs, int defStyle) {
 super(context, attrs, defStyle);
 init();
}
 
private void init(){
 paint = new Paint();
 paint.setColor(Color.BLUE);
 paint.setStrokeWidth(10);
 paint.setStyle(Paint.Style.STROKE);
 
}
 
@Override
protected void onDraw(Canvas canvas) {
 // TODO Auto-generated method stub
 super.onDraw(canvas);
 
 paint.setStyle(Paint.Style.STROKE);
 canvas.drawCircle(50, 50, 30, paint);
 
 paint.setStyle(Paint.Style.FILL);
 canvas.drawCircle(300, 300, 200, paint);
 //drawCircle(cx, cy, radius, paint)
 
}
 
}
