package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="LatestNOCRosterUpdate")
public class NOCRoster {
	@Element(name="Resource")
	public String resource;
	@Element(name="Mobile")
	public String mobile;
	@Element(name="UpdatedDate")
	public String updatedDate;
	@Element(name="StartTime")
	public String startTime;
	@Element(name="EndTime")
	public String endTime;
	
}
