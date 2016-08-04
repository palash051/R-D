package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfCSSR")
public class CSSRRoot {
	@ElementList(inline=true, name="CSSR")
	public List<CSSR> cssrList;

}
