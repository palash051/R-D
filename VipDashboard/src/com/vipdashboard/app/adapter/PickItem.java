package com.vipdashboard.app.adapter;

public class PickItem {
	private int id;
	
	private int imageId;
    private String title;
    private String desc;
     
    public  PickItem(int id,int imageId,String title) {
    	this.id=id;
        this.imageId = imageId;
        this.title = title;
       // this.desc = desc;
        
    }
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
 /*   public void setDesc(String desc) {
        this.desc = desc;
    }*/
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
   /* @Override
    public String toString() {
        return title + "\n" + desc;
    }   
*/
}
