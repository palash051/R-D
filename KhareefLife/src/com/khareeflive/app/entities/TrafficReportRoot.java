package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfTrafficReport")
public class TrafficReportRoot {
	@ElementList(inline=true, name="TrafficReport")
	public List<TrafficReport> trafficReportList;
}
