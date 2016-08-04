package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="TrafficReport")
public class TrafficReport {
	@Element(name="ReportTime")
	public String reportTime;
	@Element(name="Traffic2G")
	public double traffic2G;
	@Element(name="Traffic3G")
	public double traffic3G;	
}
