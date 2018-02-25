package org.kkonoplev.bali.services.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.project.BuildConfig;

public class BuildRunner {

  
  private static final Logger log = Logger.getLogger(BuildRunner.class);
	
  public static void main(String args[]) {
	  
	  BuildConfig buildConfig = new BuildConfig();
	  buildConfig.setCommand("C:\\abx1\\test\\gradle_build.bat");
	  buildConfig.setSuccessMark("BUILD SUCCESSFUL");
	  //run(buildConfig);
	  
	  BuildRunnerThread th = new BuildRunnerThread(buildConfig);
	  th.start();
	  
  }
  
  public static void run(BuildConfig buildConfig){
	  
	  try {
		  
		  if (buildConfig == null){
			  log.warn("BuildConfig == null!");
			  return;
		  }
			  
	      String line;
	      
	      System.out.println("Running build command: "+buildConfig.getCommand());
	      System.out.println("Waiting for "+buildConfig.getSuccessMark());
	      Process p = Runtime.getRuntime().exec(buildConfig.getCommand());
	      
	      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
	      
	      String text = "";
	      while ((line = input.readLine()) != null) {
	    	System.out.println(line);
	        text += line+"\n";
	        buildConfig.setLog(text);
	      }
	      input.close();
	    }
	    catch (Exception err) {
	      log.error(err, err);
	    }
	  
	  
  }

}