/* 
 * Copyright � 2011 Konoplev Kirill
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
import java.util.ArrayList;
import java.util.Date;

public class PerfomanceReport implements Serializable {
	
	private ArrayList<Operation> operations = new ArrayList<Operation>(100);
	
	public PerfomanceReport(){			 
	}
	
	public synchronized void addResult(String operation, Date date, double delay){
		
		// find operation in List
		Operation op = findOperation(operation);
		
		// if no such opeation
		if (op == null) {
			//create
			ResponseTime rTime = new ResponseTime(date, delay);
			op = new Operation(operation);			
			op.getResponseTimes().add(rTime);
			operations.add(op);
		} else {
			// add result			
			ResponseTime rTime = new ResponseTime(date, delay);
			op.getResponseTimes().add(rTime);	
		}	
		
	}
	
	public int getSize(){
		return operations.size();
	}


	public Operation findOperation(String operation) {

		for (Operation op: operations){
			if (op.getName().equalsIgnoreCase(operation))
				return op;			
		}
		
		return null;
	}


	public ArrayList<Operation> getOperations() {
		return operations;
	}
	
		

}
