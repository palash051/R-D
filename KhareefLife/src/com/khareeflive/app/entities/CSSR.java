package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="CSSR")
public class CSSR {
	@Element(name="ReportTime")
	public String reportTime;
	@Element(name="CSSR2G")
	public double CSSR2G;
	@Element(name="CSSR3G")
	public double CSSR3G;
	@Element(name="CSSR3GHS")
	public double CSSR3GHS;
	@Element(name="LTESSSR")
	public double LTESSSR;
}
