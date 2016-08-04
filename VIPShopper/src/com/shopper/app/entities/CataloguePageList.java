package com.shopper.app.entities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.shopper.app.utils.CommonTask;

public class CataloguePageList {
	
	public ArrayList<CataloguePage> pageList = new ArrayList<CataloguePage>(); 
	public CataloguePageList(Context context)
	{
		XmlPullParser parser = Xml.newPullParser();
		try {		   
		    InputStreamReader isr = new InputStreamReader(context.getAssets().open("catalogue_page.xml"));
		    
		    // auto-detect the encoding from the stream
		    parser.setInput(isr);
		    CataloguePage cp = null;		  
		    int eventType = parser.getEventType();
		  	//CataloguePage currentCategory = null;
		    boolean done = false;
		    while (eventType != XmlPullParser.END_DOCUMENT && !done){
		        String name = null;
		        switch (eventType){
		            case XmlPullParser.START_DOCUMENT:
		                break;
		            case XmlPullParser.START_TAG:
		                name = parser.getName();
		                if (name.equalsIgnoreCase("page"))
		                {
		                	cp =  new CataloguePage(parser.getAttributeValue("","name"),parser.getAttributeValue("","imagesrc"));
		                	pageList.add(cp);
		                }
		                else if (cp!=null && name.equalsIgnoreCase("button"))
		                {		                	
		                	 cp.addCatalogueButton(new CatalogueButton(parser.getAttributeValue("","name"),parser.getAttributeValue("","imagesrc"),CommonTask.convertToDimensionValue(context,Integer.parseInt(parser.getAttributeValue("","x"))),
		                			CommonTask.convertToDimensionValue(context,Integer.parseInt(parser.getAttributeValue("","y"))),CommonTask.convertToDimensionValue(context,Integer.parseInt(parser.getAttributeValue("","h"))),CommonTask.convertToDimensionValue(context,Integer.parseInt(parser.getAttributeValue("","w"))),parser.getAttributeValue("","ean"),parser.getAttributeValue("","desc")));		                	
		                	
		                }    
		               
		                break;
		            case XmlPullParser.END_TAG:
		                name = parser.getName();
		               if(name.equalsIgnoreCase("pagelist")){
		                    done = true;
		                }
		                break;
		            }
		        eventType = parser.next();
		        }
		} catch (FileNotFoundException e) {
		   
		} catch (IOException e) {
		   
		} catch (Exception e){
		   
		}
	}

public class CataloguePage {
	
	public String imageName;
	public String name;
	public ArrayList<CatalogueButton> buttonList = new ArrayList<CatalogueButton>();
	public CataloguePage(String nam,String img)
	{		
		name = nam;
		imageName = img;
	}
	public void addCatalogueButton(CatalogueButton cb)
	{
		buttonList.add(cb);
	}	
	
}

public class CatalogueButton{	
	public String name;
	public String imageName;
	public int x,y,height,width;
	public String ean;
	public String description;		
	public CatalogueButton(String nam, String img,int dx,int dy,int h,int w,String eanNo, String desc)
	{			
		name = nam;
		imageName = img;
		x = dx;
		y = dy;
		height = h;
		width =w;
		ean = eanNo;
		description = desc;
	}
	
}

}
