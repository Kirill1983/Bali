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

package org.kkonoplev.bali.suiteexec.resource;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/*
 * init TestExecResources 
 */

public class InitResourcesThread extends Thread {
	
	private String initcmd = "";
	private int startIdx, endIdx;
	private int done = 0;
	private ArrayList<TestExecResource> resources;
	
	private static final Logger log = Logger.getLogger(InitResourcesThread.class);
	
	public static void main(String[] args){
		
		int tCount = 27;
		int resSize = 27;
		
		double section = (double) resSize/tCount;
		
		for (int i = 0; i < tCount; i++){
			System.out.println((i*section)+" "+
					(int)Math.ceil(i*section)+"-"+ 
					(int)Math.ceil((i+1)*section-1)
					);
			
		}
		
	}
	
	public InitResourcesThread(String name, String initcmd, ArrayList<TestExecResource> resources, int startIdx, int endIdx) {
		super();
		this.initcmd = initcmd;
		this.resources = resources;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		setName(name);
		
		for (int i = startIdx; (i <= endIdx && i < resources.size()); i++){

			TestExecResource res = resources.get(i);
			res.setStatus("wait init "+name+" set");
			
		}

		
	}
	
	public void run(){
		
		log.warn("init of resources from "+startIdx+" to "+endIdx+" started in thread "+this.getName()+" "+this.getId());
		for (int i = startIdx; (i <= endIdx && i < resources.size()); i++){

			TestExecResource res = resources.get(i);
			try {
				res.init(null);
				done++;
			} catch (Exception e){
				log.warn("during init of "+res);
				log.warn(e,e);
			}
			
		}
		
		log.warn("init of resources ended.");
		
	}

	public int getDone() {
		return done;
	}
	

}
