/* 
 * Copyright © 2011 Konoplev Kirill
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kkonoplev.bali.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;



public class ShellCmd extends Thread {
	
	private static final Logger log = Logger.getLogger(ShellCmd.class);
	
	String cmd = "";
	
	public ShellCmd(String cmd_){
		cmd = cmd_;
	}
	
	public void run(){
		try {
			
			Runtime runtime = Runtime.getRuntime();
			log.info("cmd='"+cmd+"' started.");
			Process proc = runtime.exec(cmd);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	     	     
	        String line;
	        while ((line=in.readLine())!=null){
	        	log.info(line);
	        }   
	        
	      	proc.waitFor();
			log.info("cmd='"+cmd+"' completed.");
			
		} catch (Exception e){
			log.error(e);
		}
	}

}
