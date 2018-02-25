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

import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;



public class FrequencyController {
	
	private static final Logger log = LogManager.getLogger(FrequencyController.class);
	
	protected int count = 0;	
	
	// threshold
	protected int limit = 30;
	
	// millisec
	protected long lastTimeLine = 0;
	
	// ms
	protected long interval = 1000;	
	
	protected String target = "";
	
	public FrequencyController(String target_){
		lastTimeLine = System.currentTimeMillis();	
		target = target_;
	}
	
	public FrequencyController(){
		lastTimeLine = System.currentTimeMillis();			
	}
	
	public FrequencyController(int limit_, long interval_){
		lastTimeLine = System.currentTimeMillis();	
		limit = limit_;
		interval = interval_;
	}
	
	public FrequencyController(int limit_, long interval_, String target_){
		lastTimeLine = System.currentTimeMillis();	
		limit = limit_;
		interval = interval_;
		target = target_;
	}
	
		
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	private long needWait(){
		
		
		long nowTimeLine = System.currentTimeMillis();
		long diff = nowTimeLine - lastTimeLine;
		
		
		if (diff < interval)
			
			if (count+1 > limit)
				return (interval-diff+10);
			else {
				count++;
				return 0;
			}
		
		else{
			
			Date dt = new Date(lastTimeLine);
			log.info(target+", "+count);
			lastTimeLine = nowTimeLine;
			count = 1;
			return 0;
			
		}
		
	}
	
	
	public synchronized void waitIfLimit(){
		
		long needwait;
		int c = 1;
		while (((needwait=needWait()) > 0) && (c < 10)){
    		try {
    			wait(needwait);
    			c++;
    		} catch (Exception e){
    			log.warn("waitIfLimit() error");
    			log.warn(e,e);   			
    		}   		
    	}
		
		
	}
	
	
	
	
	
	

}
