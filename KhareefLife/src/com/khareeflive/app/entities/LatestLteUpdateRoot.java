package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfLatestLTEUpdate")
public class LatestLteUpdateRoot {
	@Element(name="LatestLTEUpdate")
	public LatestLteUpdate latestLteUpdate;
}
