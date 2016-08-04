package com.khareeflive.app.entities;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name="ArrayOfSiteDownGoogleMap")
public class ArrayOfSiteDownGoogleMap {
	
	@ElementList(inline=true, name="SiteDownGoogleMap")
	public List<SiteDownGoogleMap> siteDownGoogleMap;
	
	public ArrayOfSiteDownGoogleMap()
	{
		siteDownGoogleMap=new ArrayList<SiteDownGoogleMap>();
		
	}

}
