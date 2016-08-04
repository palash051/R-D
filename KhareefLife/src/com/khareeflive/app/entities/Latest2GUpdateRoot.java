package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfLatest2GUpdate")
public class Latest2GUpdateRoot {
	@Element(name="Latest2GUpdate")
	public Latest2GUpdate latest2GUpdate;
}
