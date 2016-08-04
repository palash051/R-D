package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ChatInfo")
public class ChatGroup {

	@Element(name = "Msg")
	public String message;
}
