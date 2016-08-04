package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfLatestNOCRosterUpdate")
public class NOCRosterRoot {
	@ElementList(inline=true, name="LatestNOCRosterUpdate")
	public List<NOCRoster> nocRosterList;
}
