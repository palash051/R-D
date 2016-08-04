
package com.vipdashboard.app.utils;

import android.net.TrafficStats;

public class TrafficRecord {
	long tx=0;
	long rx=0;
	public long deltaTx=0;
	public long deltaRx=0;
	public String tag=null;
	public String appLebel=null;
	public byte[] appIcon;
	
	TrafficRecord() {
		tx=TrafficStats.getTotalTxBytes();
		rx=TrafficStats.getTotalRxBytes();
	}
	
	TrafficRecord(int uid, String tag) {
		tx=TrafficStats.getUidTxBytes(uid);
		rx=TrafficStats.getUidRxBytes(uid);
		this.tag=tag;
	}
	
	TrafficRecord(int uid, String tag, String _appLebel,byte[] _appIcon) {
		tx=TrafficStats.getUidTxBytes(uid);
		rx=TrafficStats.getUidRxBytes(uid);
		this.tag=tag;
		this.appIcon=_appIcon;
		this.appLebel=_appLebel;
	}
}