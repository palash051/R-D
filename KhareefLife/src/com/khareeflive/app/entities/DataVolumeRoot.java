package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfDataVolume")
public class DataVolumeRoot {
	@ElementList(inline=true, name="DataVolume")
	public List<DataVolume> dataVolumeList;

}
