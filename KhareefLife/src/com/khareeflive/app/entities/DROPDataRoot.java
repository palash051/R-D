package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfDROPData")
public class DROPDataRoot {
	@ElementList(inline=true, name="DROPData")
	public List<DROPData> dropDataList;

}
