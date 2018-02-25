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

package org.kkonoplev.bali.common.perfomance;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Operation implements Serializable  {
	
	private String name;
	private ArrayList<ResponseTime> responseTimes = new ArrayList<ResponseTime>(100);
	
	public Operation(String name_){
		name = name_;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String formatted(double d){
		
		DecimalFormat myFormatter = new DecimalFormat("###.##");
		String output = myFormatter.format(d);
		
		return output;	
		
	}

	
	public String getAvgDelayFmt(){
		
		double avg = getAvgDelay();
		String avgStr = formatted(avg);
		
		return avgStr;
		
	}

	public double getAvgDelay(){
		
		double sum = 0.0;
		double size = responseTimes.size();
		for (ResponseTime delay: responseTimes){
			sum += delay.getDelay();						
		}
		
		double avg = sum/size; 
		
		return avg;
	}
	
	
	public double getDispersion(){
		
		double sum = 0;
		for (ResponseTime delay: responseTimes){
			sum += delay.getDelay();						
		}
		
		double avg = sum/responseTimes.size(); 
		
		return avg;		
	}
	

	public ArrayList<ResponseTime> getResponseTimes() {
		return responseTimes;
	}

	public void setResponseTimes(ArrayList<ResponseTime> responseTimes) {
		this.responseTimes = responseTimes;
	}
	
	public int getId(){
		return this.hashCode();
	}
	
	
	

}
