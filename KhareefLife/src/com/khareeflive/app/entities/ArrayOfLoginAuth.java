package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root(name="ArrayOfLoginAuth")
public class ArrayOfLoginAuth {

	@Element(name="LoginAuth")
	public LoginAuth loginAuth;
	
	public ArrayOfLoginAuth()
	{
		loginAuth=new LoginAuth();
		
	}
}
