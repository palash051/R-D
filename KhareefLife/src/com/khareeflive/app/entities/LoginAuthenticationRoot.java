package com.khareeflive.app.entities;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfLoginAuth")
public class LoginAuthenticationRoot {
	@ElementList(inline=true, name="LoginAuth")
	public List< LoginAuthentication> loginAuthList;
	
	public LoginAuthenticationRoot()
	{
		loginAuthList=new ArrayList<LoginAuthentication>();
	}
}
