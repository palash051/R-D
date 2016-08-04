package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="LoginAuth")
public class LoginAuth {
	@Element(name="Status")
	public int status;	
	
}
