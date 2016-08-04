package com.khareeflive.app.entities;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="LatestUpdate")
public class LatestUpdate {

	
		@Element(name="Msg")
		public String Msg;
		
		@Element(name="MsgTitle")
		public String MsgTitle;
		
		@Element(name="UploadedBy")
		public String UploadedBy;
		
		@Element(name="UploadDate")
		public String  UploadDate;
}
