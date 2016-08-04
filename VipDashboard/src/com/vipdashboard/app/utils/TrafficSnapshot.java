package com.vipdashboard.app.utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

public class TrafficSnapshot {
	TrafficRecord device=null;
	public HashMap<Integer, TrafficRecord> apps=
		new HashMap<Integer, TrafficRecord>();
	PackageManager pm ;
	public TrafficSnapshot(Context ctxt) {
		try{
			device=new TrafficRecord();
			pm= ctxt.getPackageManager();
			for (ApplicationInfo app :pm.getInstalledApplications(0)) {		
				if(TrafficStats.getUidTxBytes(app.uid)>0 ||TrafficStats.getUidRxBytes(app.uid)>0)
					apps.put(app.uid, new TrafficRecord(app.uid,app.packageName, pm.getApplicationLabel(app).toString(),convertDrawableToByte(pm.getApplicationIcon(app))));
			}
		}catch (Exception e) {
			
		}
	}
	
	private byte[] convertDrawableToByte(Drawable drawable){
		Bitmap b = ((BitmapDrawable)drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}
	
	public void calculateDelta(TrafficRecord latest_rec,TrafficRecord previous_rec,ArrayList<TrafficRecord> rows){
		if (latest_rec.rx>-1 || latest_rec.tx>-1) {
			latest_rec.deltaRx=previous_rec==null?0:(latest_rec.rx-  previous_rec.rx)/1024;
			latest_rec.deltaTx= previous_rec==null?0: (latest_rec.tx-previous_rec.tx)/1024;
			if(latest_rec.deltaTx>0||latest_rec.deltaRx>0)
				rows.add(latest_rec);
		}
	}
}