package com.vipdashboard.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.classes.DisplayNextView;
import com.vipdashboard.app.classes.Flip3dAnimation;
import com.vipdashboard.app.classes.MyView;

public class OptimizationResultActivity extends MainActionbarBase{

private ImageView image1;
private ImageView image2;

private boolean isFirstImage = true;

TextView ivDone,ivOptimizationMessage;

public static String calledFromInfo="none";
public static String optimizationMessage="none";

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.optimization_message);

image1 = (ImageView) findViewById(R.id.ImageView01);
image2 = (ImageView) findViewById(R.id.ImageView02);
ivDone = (TextView) findViewById(R.id.ivDone);
ivOptimizationMessage= (TextView) findViewById(R.id.ivOptimizationMessage);
image2.setVisibility(View.GONE);

ivOptimizationMessage.setText(optimizationMessage);

//setContentView(new MyView(this));
try {
	Thread.sleep(10);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
if (isFirstImage) {       
    applyRotation(0, -90);
    isFirstImage = !isFirstImage;

   } else {    
    applyRotation(0, 90);
    isFirstImage = !isFirstImage;
   }

image1.setOnClickListener(new View.OnClickListener() {
   public void onClick(View view) {
	   if (isFirstImage) {       
		    applyRotation(0, 90);
		    isFirstImage = !isFirstImage;

		   } else {    
		    applyRotation(0, -90);
		    isFirstImage = !isFirstImage;
		   }
   }
});

ivDone.setOnClickListener(new View.OnClickListener() {
	   public void onClick(View view) {
		   
		   if(calledFromInfo.equals("batterydoctor"))
		   {
			   OptimizationResultActivity.this.finish();
			   VIPD_BatteryDoctor.IsCalledFromOptimization=true;
			   Intent intent = new Intent(OptimizationResultActivity.this, VIPD_BatteryDoctor.class);
			   intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    startActivity(intent);   
		   }
		   
		   else if(calledFromInfo.equals("MemoryBoots"))
		   {
			   OptimizationResultActivity.this.finish();
			   Intent intent = new Intent(OptimizationResultActivity.this,
						HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
		   }
		   else if(calledFromInfo.equals("checkup"))
		   {
			   OptimizationResultActivity.this.finish();
			   Intent intent = new Intent(OptimizationResultActivity.this,
						HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
		   }
	   }
	});

}

private void applyRotation(float start, float end) {
// Find the center of image
final float centerX = image1.getWidth() / 2.0f;
final float centerY = image1.getHeight() / 2.0f;

// Create a new 3D rotation with the supplied parameter
// The animation listener is used to trigger the next animation
final Flip3dAnimation rotation =
       new Flip3dAnimation(start, end, centerX, centerY);
rotation.setDuration(600);
rotation.setFillAfter(true);
rotation.setInterpolator(new AccelerateInterpolator());
rotation.setAnimationListener(new DisplayNextView(isFirstImage, image1, image2));

if (isFirstImage)
{
image1.startAnimation(rotation);
} else {
image2.startAnimation(rotation);
}

}
}
