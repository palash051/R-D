package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="DROPData")
public class DROPData {
	@Element(name="ReportTime")
	public String reportTime;
	@Element(name="SDCCH2G")
	public double SDCCH2G;
	@Element(name="TCHDROP2G")
	public double TCHDROP2G;
	@Element(name="Drop_Speech_3G")
	public double Drop_Speech_3G;
	@Element(name="HSDrop_3G")
	public double HSDrop_3G;
}
