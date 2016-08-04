package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfChatInfo")
public class MessageRoot {
	@Element(name="ChatInfo")
	public Message message;

}
