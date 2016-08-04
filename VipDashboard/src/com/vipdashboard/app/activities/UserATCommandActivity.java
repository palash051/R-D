package com.vipdashboard.app.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

public class UserATCommandActivity extends MainActionbarBase {
	
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	
	TextView tvStartProcess,tvHistroy;
	
	double balance=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.useratcommand);
		
		//tvHistroy= (TextView) findViewById(R.id.tvHistroy);
		//tvStartProcess= (TextView) findViewById(R.id.tvStartProcess);
		
		ReadCPUinfo();
		//LoadInformation();
	}
	
	private void ReadCPUinfo() {
		ProcessBuilder cmd;
		String result = "";
		
		try {
			
			/*ReadATCommand obj=new ReadATCommand();
			obj.start();
			*/
			
			//do something with the output
		    Runtime r = Runtime.getRuntime();
		    
		    //echo -e "ATD123456789;\r"
		    //Process process = r.exec("su -c 'echo -e \"AT+CEER;\\r\" > /dev/smd0; cat /dev/smd0' ");
		    
		    //echo -e "AT+CFUN=?\r\n" > /dev/ttyUSB0
		    Process process = r.exec(" su -c 'echo -e \"AT\\r\" > /dev/smd0;' ");
		    BufferedReader in = new BufferedReader(  
		                        new InputStreamReader(process.getInputStream()));  
		    String line = null;  
		    String everything="";
		    while ((line = in.readLine()) != null) {  
		    	everything=everything+line;
		    }  
		    //tvHistroy.setText(String.valueOf(in));
		    
		} catch (IOException e) {  
		    //e.printStackTrace();  
		} 
		
		
		
		
		
		
		/*try {   
		   // Preform su to get root privledges  
		   p = Runtime.getRuntime().exec("su");   
		     
		   // Attempt to write a file to a root-only   
		   DataOutputStream os = new DataOutputStream(p.getOutputStream());   
		   os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");  
		     
		   // Close the terminal  
		   os.writeBytes("exit\n");   
		   os.flush();   
		   try {   
		      p.waitFor();   
		           if (p.exitValue() != 255) {   
		              // TODO Code to run on success  
		        	   tvHistroy.setText("Root");
		        	  Toast.makeText(this, "Root", Toast.LENGTH_LONG); 
		        	  
		              //toastMessage("root");  
		           }   
		           else {   
		               // TODO Code to run on unsuccessful  
		        	   tvHistroy.setText("not root");
		        	   Toast.makeText(this, "not root", Toast.LENGTH_LONG);
		               //toastMessage("not root");      
		           }   
		   } catch (InterruptedException e) {   
		      // TODO Code to run in interrupted exception  
			   tvHistroy.setText("not root");
			   Toast.makeText(this, "not root", Toast.LENGTH_LONG);
		       //toastMessage("not root");   
		   }   
		} catch (IOException e) {   
		   // TODO Code to run in input/output exception  
			tvHistroy.setText("not root");
			Toast.makeText(this, "not root", Toast.LENGTH_LONG);
		    //toastMessage("not root");   
		}*/
		
		
		
		
		/*try { 
			
			Runtime r = Runtime.getRuntime();


            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("echo -e \"AT\\r\" > /dev/smd0\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
			
		    Runtime r = Runtime.getRuntime();
		    Process process = r.exec(" su -c 'echo -e \"AT\" > /dev/smd0; cat /dev/smd0' ");

		    BufferedReader in = new BufferedReader(  
		                        new InputStreamReader(process.getInputStream()));  
		    String line = null;  
		    String everything="";
		    while ((line = in.readLine()) != null) {  
		    	everything.concat(line);  
		    	 tvHistroy.setText(line);
		    } 
		  tvHistroy.setText(everything);
				} catch (IOException e) {  
		    e.printStackTrace();  
		}  */

		/*try {
			
			Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
			DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
			//from here all commands are executed with su permissions
			stdin.writeBytes("ls /data\n"); // \n executes the command
			InputStream stdout = p.getInputStream();
			byte[] buffer = new byte[100];
			int read;
			String out = new String();
			//read method will wait forever if there is nothing in the stream
			//so we need to read it in another way than while((read=stdout.read(buffer))>0)
			while(true){
			    read = stdout.read(buffer);
			    out += new String(buffer, 0, read);
			    if(read<100){
			    	
			        //we have read everything
			        break;
			    }
			}

            String[] args = { "/system/bin/service",
            		"call", "phone", "1", "s16", "+8801680262068" };
            cmd = new ProcessBuilder(args);

    		Process process = cmd.start();
    		InputStream in = process.getInputStream();

    		byte[] re = new byte[1];

    			while (in.read(re) != -1) {
    				result = result + new String(re);
    			}
    		in.close();
    		
    		tvHistroy.setText(result);

		} catch (IOException ex) {

			ex.printStackTrace();
		}catch(Exception e){

		}*/

	}

	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
	}
	

	
}
