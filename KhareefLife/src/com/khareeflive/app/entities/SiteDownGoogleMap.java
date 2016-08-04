package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="SiteDownGoogleMap")
public class SiteDownGoogleMap {

	@Element(name="SiteID")
	public String SiteID;
	
	@Element(name="Address")
	public String Address;
	
	@Element(name="Lat")
	public Double Lat;
	
	@Element(name="Lang")
	public Double Lang;
}
