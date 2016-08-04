package com.vipdashboard.app.activities;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.R.string;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ActivityManager.MemoryInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorCPUManagementActivity extends MainActionbarBase implements OnClickListener{
	
	private static final String TAG = null;
	RelativeLayout rlbatterydoctorl1,rlCoreOne,rlCoretwo,rlCoreThree,rlCorefour,rlbatterydoctorCpuManagement,rlOpendip,rlTips;
	ImageView ivcr1, ivrcr2,ivcr3,ivcr4,ivBaterydoctorSwitchStatusOn,ivBaterydoctorSwitchStatusOff,iv_arrow;
	TextView tvCr1,tvCr1mh,tvrCr2,tvCr2mh,tvCr3,tvCr3mh,tvCr4,tvCr4mh,tvCPUmange,tvOpenDip,tvTips,tvThisUderlock;
	Button bOk;
	private Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	CpuStat cpustat;
	Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_cpu_management);
		Initialization();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
		
		DisplayCoreUsage();

		initThread();
		
		boolean root = isRooted();
		if(root)
		{
			ivBaterydoctorSwitchStatusOn.setVisibility(View.VISIBLE);
			ivBaterydoctorSwitchStatusOff.setVisibility(View.GONE);
		}
		else
		{
			ivBaterydoctorSwitchStatusOn.setVisibility(View.GONE);
			ivBaterydoctorSwitchStatusOff.setVisibility(View.VISIBLE);
		}
		  if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		
	}
	
	private void DisplayCoreUsage() 
	{
		tvCr1mh.setText("");
		tvCr2mh.setText("");
		tvCr3mh.setText("");
		tvCr4mh.setText("");
	
	}
	
	private void initThread() {
		recieveMessageRunnable = new Runnable() {
			public void run() {				
				tvCr1mh.setText(String.valueOf(cpustat.ReadCPUMhz()+"mhz"));
				tvCr2mh.setText(String.valueOf(cpustat.ReadCPUMhz()+"mhz"));
				tvCr3mh.setText(String.valueOf(cpustat.ReadCPUMhz()+"mhz"));
				recieveMessageHandler.postDelayed(recieveMessageRunnable, 1000);
			}
		};
		recieveMessageHandler.postDelayed(recieveMessageRunnable, 1000);
	}
	
	private void Initialization() 
	{
		cpustat = new CpuStat();
		recieveMessageHandler = new Handler();
		ivBaterydoctorSwitchStatusOn = (ImageView) findViewById(R.id.ivBaterydoctorSwitchStatusOn);
		ivBaterydoctorSwitchStatusOff = (ImageView) findViewById(R.id.ivBaterydoctorSwitchStatusOff);
		tvCr1mh= (TextView) findViewById(R.id.tvCr1mh);
		tvCr2mh= (TextView) findViewById(R.id.tvCr2mh);
		tvCr3mh= (TextView) findViewById(R.id.tvCr3mh);
		tvCr4mh= (TextView) findViewById(R.id.tvCr4mh);
		rlbatterydoctorCpuManagement=(RelativeLayout) findViewById(R.id.rlbatterydoctorCpuManagement);	
	    rlbatterydoctorCpuManagement.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bBack) {
			onBackPressed();
		} else if (view.getId() == R.id.rlbatterydoctorCpuManagement) {
			if ((ivBaterydoctorSwitchStatusOff.getVisibility() == View.VISIBLE)) {
				onCreateDialog(0);
			}
		}
	
	}
	
	private boolean isRooted()  {

	    // get from build info
	    String buildTags = android.os.Build.TAGS;
	    if (buildTags != null && buildTags.contains("test-keys")) {
	      return true;
	    }

	    // check if /system/app/Superuser.apk is present
	    try {
	      File file = new File("/system/app/Superuser.apk");
	      if (file.exists()) {
	        return true;
	      }
	    } catch (Exception e1) {
	      // ignore
	    }

	    // try executing commands
	    return canExecuteCommand("/system/xbin/which su")
	        || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
	  }

	  // executes a command on the system
	  private  boolean canExecuteCommand(String command) {
	    boolean executedSuccesfully;
	    try {
	      Runtime.getRuntime().exec(command);
	      executedSuccesfully = true;
	    } catch (Exception e) {
	      executedSuccesfully = false;
	    }

	    return executedSuccesfully;
	  }

	protected Dialog onCreateDialog(int id) {
		  final Dialog dialog;
		 
		  
		  switch(id) {
		   case 0:
		    dialog = new Dialog(this);
		    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.battery_doctor_cpu_management_tips);
	        
	        bOk = (Button) dialog.findViewById(R.id.bOk);
	        bOk.setOnClickListener(new OnClickListener() 
	        {
                @Override
                public void onClick(View arg0) {
                	ivBaterydoctorSwitchStatusOn.setVisibility(View.VISIBLE);
    				ivBaterydoctorSwitchStatusOff.setVisibility(View.GONE);
                	   dialog.dismiss();
                }
	        });
	        dialog.show();
	        
		   
		    break;
		default:
		    dialog = null;
		}
		return null;
	
}
	/*public void stopThread(){
		  if(myService.getThread()!=null){
		      myService.getThread().interrupt();
		      myService.setThread(null);
		  }
		}*/
	public class CpuStat 
	{
	    private static final String TAG = "CpuUsage";
	    private RandomAccessFile statFile;
	    private CpuInfo mCpuInfoTotal;
	    private ArrayList<CpuInfo> mCpuInfoList;

	    public CpuStat()
	    {
	    }

	    public void update() 
	    {
	        try 
	        {           
	            createFile();
	            parseFile();
	            closeFile();
	        } catch (FileNotFoundException e) 
	        {
	            statFile = null;
	            Log.e(TAG, "cannot open /proc/stat: " + e);
	        } catch (IOException e) {
	            Log.e(TAG, "cannot close /proc/stat: " + e);
	        }
	    }

	    private void createFile() throws FileNotFoundException 
	    {
	        statFile = new RandomAccessFile("/proc/stat", "r");
	    }

	    public void closeFile() throws IOException 
	    {
	        if (statFile != null)
	            statFile.close();
	    }

	    private void parseFile()
	    {
	        if (statFile != null) 
	        {
	            try {
	                statFile.seek(0);
	                String cpuLine = "";
	                int cpuId = -1;
	                do 
	                { 
	                    cpuLine = statFile.readLine();
	                    parseCpuLine(cpuId, cpuLine);
	                    cpuId++;
	                }
	                while (cpuLine != null);
	            }
	            catch (IOException e) {
	                Log.e(TAG, "Ops: " + e);
	            }
	        }
	    }

	    private void parseCpuLine(int cpuId, String cpuLine)
	    {
	        if (cpuLine != null && cpuLine.length() > 0)
	        { 
	            String[] parts = cpuLine.split("[ ]+");
	            String cpuLabel = "cpu";
	            if ( parts[0].indexOf(cpuLabel) != -1)
	            {
	                createCpuInfo(cpuId, parts);
	            }
	        } else
	        {
	            Log.e(TAG, "unable to get cpu line");
	        }
	    }

	    private void createCpuInfo(int cpuId, String[] parts)
	    {
	        if (cpuId == -1) 
	        {                      
	            if (mCpuInfoTotal == null)
	                mCpuInfoTotal = new CpuInfo();
	            mCpuInfoTotal.update(parts);
	        } else {
	            if (mCpuInfoList == null)
	                mCpuInfoList = new ArrayList<CpuInfo>();
	            if (cpuId < mCpuInfoList.size())
	                mCpuInfoList.get(cpuId).update(parts);
	            else {
	                CpuInfo info = new CpuInfo();
	                info.update(parts);
	                mCpuInfoList.add(info);
	            }                               
	        }
	    }

	    public int getCpuUsage(int cpuId) {
	        update();
	        int usage = 0;
	        if (mCpuInfoList != null) {
	            int cpuCount = mCpuInfoList.size();
	            if (cpuCount > 0) {
	                cpuCount--;
	                if (cpuId == cpuCount) { // -1 total cpu usage
	                    usage = mCpuInfoList.get(0).getUsage(); 
	                } else {
	                    if (cpuId <= cpuCount)
	                        usage = mCpuInfoList.get(cpuId).getUsage();
	                    else
	                        usage = -1;
	                }
	            }
	        }
	        return usage;
	    }


	    public int getTotalCpuUsage() {
	        update();           
	        int usage = 0;
	        if (mCpuInfoTotal != null)
	            usage = mCpuInfoTotal.getUsage();
	        return usage;
	    }


	    public String toString()
	    {
	        update();
	        StringBuffer buf = new StringBuffer();
	        if (mCpuInfoTotal != null)
	        {
	            buf.append("Cpu Total : ");
	            buf.append(mCpuInfoTotal.getUsage());
	            buf.append("%");
	        }   
	        if (mCpuInfoList != null) {
	            for (int i=0; i < mCpuInfoList.size(); i++) 
	            {
	                CpuInfo info = mCpuInfoList.get(i); 
	                buf.append(" Cpu Core(" + i + ") : ");
	                buf.append(info.getUsage());
	                buf.append("%");
	                info.getUsage();
	            }
	        }           
	        return buf.toString();
	    }

	    public class CpuInfo 
	    {
	        private int  mUsage;            
	        private long mLastTotal;
	        private long mLastIdle;

	        public CpuInfo() 
	        {
	            mUsage = 0;
	            mLastTotal = 0;
	            mLastIdle = 0;  
	        }

	        private int  getUsage() {
	        	//mUsage=mUsage*100;
	            return mUsage;
	        }

	        public void update(String[] parts) 
	        {
	            // the columns are:
	            //
	            //      0 "cpu": the string "cpu" that identifies the line
	            //      1 user: normal processes executing in user mode
	            //      2 nice: niced processes executing in user mode
	            //      3 system: processes executing in kernel mode
	            //      4 idle: twiddling thumbs
	            //      5 iowait: waiting for I/O to complete
	            //      6 irq: servicing interrupts
	            //      7 softirq: servicing softirqs
	            //
	            long idle = Long.parseLong(parts[4], 10);
	            long total = 0;
	            boolean head = true;
	            for (String part : parts) 
	            {
	                if (head) 
	                {
	                    head = false;
	                    continue;
	                }
	                total += Long.parseLong(part, 10);
	            }
	            long diffIdle   =   idle - mLastIdle;
	            long diffTotal  =   total - mLastTotal;
	            mUsage = (int)((float)(diffTotal - diffIdle));
	            mLastTotal = total;
	            mLastIdle = idle;
	            Log.i(TAG, "CPU total=" + total + "; idle=" + idle + "; usage=" + mUsage);
	        }
	    }
	    
	    private float readUsage() {
		    try {
		        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
		        String load = reader.readLine();

		        String[] toks = load.split(" ");

		        long idle1 = Long.parseLong(toks[4]);
		        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
		              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

		        try {
		            Thread.sleep(360);
		        } catch (Exception e) {}

		        reader.seek(0);
		        load = reader.readLine();
		        reader.close();

		        toks = load.split(" ");

		        long idle2 = Long.parseLong(toks[4]);
		        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
		            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

		        return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }

		    return 0;
		} 
	    
	    private String ReadCPUMhz()
        {
             ProcessBuilder cmd;
             String result="";
             int resultshow = 0;
      
             try{
              String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"};
              
              cmd = new ProcessBuilder(args);

              Process process = cmd.start();
              InputStream in = process.getInputStream();
              byte[] re = new byte[1024];
              while(in.read(re) != -1)
               {
                 result = result + new String(re);

               }

              in.close();
             } catch(IOException ex){
              ex.printStackTrace();
             }
             int kHzValue = Integer.parseInt(result.split("\n")[0]); // result in kHz
             int MHzResult = kHzValue / 1000; // result in MHz
              //return result;
    
         return (String.valueOf(MHzResult));
          
          
        }
		public  int getMaxCPUFreqMHz() {

		    int maxFreq = -1;
		    try {

		        RandomAccessFile reader = new RandomAccessFile( "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state", "r" );
		        	
		        boolean done = false;
		        while ( ! done ) {
		            String line = reader.readLine();
		            if ( null == line ) {
		                done = true;
		                break;
		            }
		            String[] splits = line.split( "\\s+" );
		            assert ( splits.length == 2 );
		            int timeInState = Integer.parseInt( splits[1] );
		            if ( timeInState > 0 ) {
		                int freq = Integer.parseInt( splits[0] ) / 1000;
		                if ( freq > maxFreq ) {
		                    maxFreq = freq;
		                }
		            }
		        }

		    } catch ( IOException ex ) {
		        ex.printStackTrace();
		    }
		    
		    return maxFreq;
		}
		private String showAvailableMemory(){
		    MemoryInfo mi = new MemoryInfo();
		    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		    activityManager.getMemoryInfo(mi);
		    long availableMegs = mi.availMem / 1048576L;
		    long PavailableMegs = (availableMegs*1024)/100;
		    Log.i(TAG, "Available memory = " + availableMegs + " MB");
		    return(String.valueOf( PavailableMegs));
		}
		public  String getTotalRAM() {
		    RandomAccessFile reader = null;
		    String load = null;
		    try {
		        reader = new RandomAccessFile("/proc/meminfo", "r");
		        load = reader.readLine();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    } finally {
		        // Streams.close(reader);
		    }
		    
		    return load;
		}

		public  boolean isRooted() {

		    // get from build info
		    String buildTags = android.os.Build.TAGS;
		    if (buildTags != null && buildTags.contains("test-keys")) {
		      return true;
		    }

		    // check if /system/app/Superuser.apk is present
		    try {
		      File file = new File("/system/app/Superuser.apk");
		      if (file.exists()) {
		        return true;
		      }
		    } catch (Exception e1) {
		      // ignore
		    }

		    // try executing commands
		    return canExecuteCommand("/system/xbin/which su")
		        || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
		  }

		  // executes a command on the system
		  private  boolean canExecuteCommand(String command) {
		    boolean executedSuccesfully;
		    try {
		      Runtime.getRuntime().exec(command);
		      executedSuccesfully = true;
		    } catch (Exception e) {
		      executedSuccesfully = false;
		    }

		    return executedSuccesfully;
		  }
	    private int getNumCores() {

		    //Private Class to display only CPU devices in the directory listing

		    class CpuFilter implements FileFilter {

		        @Override

		        public boolean accept(File pathname) {

		            //Check if filename is "cpu", followed by a single digit number

		            if(Pattern.matches("cpu[0-9]+", pathname.getName())) {

		                return true;

		            }

		            return false;

		        }      

		    }



		    try {

		        //Get directory containing CPU info

		        File dir = new File("/sys/devices/system/cpu/");

		        //Filter to only list the devices we care about

		        File[] files = dir.listFiles(new CpuFilter());

		        Log.d(TAG, "CPU Count: "+files.length);

		        //Return the number of cores (virtual CPU devices)

		        return files.length;

		    } catch(Exception e) {

		        //Print exception

		        Log.d(TAG, "CPU Count: Failed.");

		        e.printStackTrace();

		        //Default to return 1 core

		        return 1;

		    }

		}

	}
	

}



