package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="DataVolume")
public class DataVolume {
	@Element(name="ReportTime")
	public String reportTime;
	@Element(name="LTEDLDV")
	public double LTEDLDV;
	@Element(name="DLDV3G")
	public double DLDV3G;
	@Element(name="pDrops_TCH_BQ_DL")
	public double pDrops_TCH_BQ_DL;	

}
