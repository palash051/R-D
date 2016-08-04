package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfLatest3GUpdate")
public class Latest3GUpdateRoot {
	@Element(name="Latest3GUpdate")
	public Latest3GUpdate latest3GUpdate;
}
