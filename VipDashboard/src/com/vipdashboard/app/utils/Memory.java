package com.vipdashboard.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import android.os.Environment;
import android.os.StatFs;

public class Memory {
	
	private static final String ERROR = "Error";
	
	public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
	
	public static String getUseInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long totalBlocks = stat.getBlockCount();
        long useBlocks= totalBlocks - availableBlocks;
        return formatSize(useBlocks * blockSize);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }
    
    public static String getUseExternalMemorySize() {
        if (externalMemoryAvailable()) {
            String path = SDPath();
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long totalBlocks = stat.getBlockCount();
            long useBlocks= totalBlocks - availableBlocks;
            return formatSize(useBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
        	String path = SDPath();
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
        	String path = SDPath();
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return formatSize(totalBlocks * blockSize);
        } else {
            return ERROR;
        }
    }
    
    public static float percentExternalUse()
    {
    	float percent = 0;
    	
    	if (externalMemoryAvailable()) {
            String path = SDPath();
            StatFs stat = new StatFs(path);
            long availableBlocks = stat.getAvailableBlocks();
            long totalBlocks = stat.getBlockCount();
            long useBlocks= totalBlocks - availableBlocks;
            percent = (float) useBlocks / totalBlocks * 100;
            return percent;
        }
    	
    	return percent;
    }
    
    public static float percentInternalUse()
    {
    	float percent = 0;
    	
    	if (externalMemoryAvailable()) {
    		File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long availableBlocks = stat.getAvailableBlocks();
            long totalBlocks = stat.getBlockCount();
            long useBlocks= totalBlocks - availableBlocks;
            percent = (float) useBlocks / totalBlocks *100;
            return percent;
        }
    	
    	return percent;
    }
    

    public static String formatSize(long size) {
        String suffix = null;
        
        double reSize = (double)size;

        if (reSize >= 1024) {
            suffix = "K";
            reSize /= 1024;
            if (reSize >= 1024) {
                suffix = "M";
                reSize /= 1024;
                if (reSize >= 1024) {
                    suffix = "G";
                    reSize /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Double.toString(roundOneDecimals(reSize)));

 //       int commaOffset = resultBuffer.length() - 3;
//        while (commaOffset > 0) {
//            resultBuffer.insert(commaOffset, ',');
//            commaOffset -= 3;
//        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
    
    public static String formatSize(long size, String type) {
        String suffix = null;
        
        double reSize = (double)size;

        if (reSize >= 1024) {
            suffix = "MB";
            reSize /= 1024;
            if (reSize >= 1024) {
                suffix = "G";
                reSize /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Double.toString(roundOneDecimals(reSize)));

 //       int commaOffset = resultBuffer.length() - 3;
//        while (commaOffset > 0) {
//            resultBuffer.insert(commaOffset, ',');
//            commaOffset -= 3;
//        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
    
    private static double roundOneDecimals(double d) {
	    	DecimalFormat twoDForm = new DecimalFormat("#.#");
		return Double.valueOf(twoDForm.format(d));
	
	}
   
   private static String  SDPath(){
	   String path = "";
	   File file = new File("/system/etc/vold.fstab");
	   FileReader fr = null;
	   BufferedReader br = null;
	
	   try {
	       fr = new FileReader(file);
	   } catch (FileNotFoundException e) {
	       e.printStackTrace();
	   } 
	
	   try {
	       if (fr != null) {
	           br = new BufferedReader(fr);
	           String s = br.readLine();
	           while (s != null) {
	               if (s.startsWith("dev_mount")) {
	                   String[] tokens = s.split("\\s");
	                   path = tokens[2]; //mount_point
	                   if (!Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
	                       break;
	                   }
	               }
	               s = br.readLine();
	           }
	       }            
	   } catch (IOException e) {
	       e.printStackTrace();
	   } finally {
	       try {
	           if (fr != null) {
	               fr.close();
	           }            
	           if (br != null) {
	               br.close();
	           }
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	   }
	   return path;
	   
	   }
   

}
