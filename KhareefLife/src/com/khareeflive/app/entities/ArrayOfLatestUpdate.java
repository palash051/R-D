package com.khareeflive.app.entities;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfLatestUpdate")
public class ArrayOfLatestUpdate {

	@ElementList(inline=true, name="LatestUpdate")
	public List<LatestUpdate> latestupdate;
	
	public ArrayOfLatestUpdate()
	{
		latestupdate=new ArrayList<LatestUpdate>();
		
	}
}
