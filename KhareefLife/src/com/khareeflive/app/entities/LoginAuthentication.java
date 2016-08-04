package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="LoginAuth")
public class LoginAuthentication {
	@Element(name="Status")
	public int status;
	@Element(name="UserID")
	public String userID;
	
	@Element(name="Mobile", required = false)
	 public String mobile;
}
