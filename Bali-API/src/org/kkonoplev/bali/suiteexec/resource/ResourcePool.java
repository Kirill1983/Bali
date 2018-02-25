package org.kkonoplev.bali.suiteexec.resource;
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



import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.suiteexec.TestExecContext;

public class ResourcePool {
	
	private static final Logger log = Logger.getLogger(ResourcePool.class);

	private ArrayList<TestExecResource> resources = new ArrayList<TestExecResource>();
	private String factoryClassName;
	private ArrayList<String> loadcmds = new ArrayList<String>();
	private Class<? extends ResourcePoolFactory> factoryClass;
	private ResourcePoolFactory factory;
	
	public ResourcePool(){
	}

	public int getSize(){
		return resources.size();
	}

	public int getUpCount(){
		
		int cnt = 0;
		for (TestExecResource res : resources){
			if (res.getStatus().equals("up"))
				cnt++;
		}
		return cnt;
		
	}
	
	public TestExecResource getFreeResource(TestExecContext testExecContext, String properties) {

		// first search in context 
		for (TestExecResource resource: resources){
			if (!resource.isPreLocked() && resource.isFree(testExecContext, properties))				
				return resource;
		}
			
		// then search without
		for (TestExecResource resource: resources){
			if (!resource.isPreLocked() && resource.isFree(properties))				
				return resource;
		}

		// if not found applicable then null
		return null;
	}

	
	public TestExecResource getResource(TestExecContext testExecContext) {
		
		for (TestExecResource resource: resources)
			if ((resource.getTestExecContext() == testExecContext))
				return resource;	
		
		log.warn("Not found required resource "+factory.getResourceClass().getName()+" for "+testExecContext+" !");		
		return null;
	}
	

	public void unlockResourcesUsedBy(TestExecContext testExecContext) {
		
		for (TestExecResource testExecResource: resources)			
			if (testExecResource.getTestExecContext() == testExecContext){
				log.info(testExecResource+" unlocked, used by "+testExecContext);
				testExecResource.setTestExecContext(null);
			}	
		
	}
	public ArrayList<TestExecResource> getResources() {
		return resources;
	}
	public void setResources(ArrayList<TestExecResource> resources) {
		this.resources = resources;
	}
	
	public String getFactoryClassName() {
		return factoryClassName;
	}
	public void setFactoryClassName(String factoryClassName) {
		this.factoryClassName = factoryClassName;
	}
	
	public ArrayList<String> getLoadcmds() {
		return loadcmds;
	}
	public void setLoadcmds(ArrayList<String> loadargs) {
		this.loadcmds = loadargs;
	}
	
	public Class<? extends ResourcePoolFactory> getFactoryClass() {
		return factory.getClass();
	}

	public ResourcePoolFactory getFactory() {
		return factory;
	}
	public void setFactory(ResourcePoolFactory factory) {
		this.factory = factory;
	}
	
	public synchronized void doLoadCmd(String loadcmd) {	
		
		// add only new resources
		for (TestExecResource res: factory.load(loadcmd)){
			if (!resources.contains(res)){
				res.setName((resources.size()+1)+"");
				resources.add(res);
			}
		}
	
	}
	
	public synchronized void doClearCmd(String match) {
		
		Iterator it = resources.iterator();
		
		while (it.hasNext()){
			TestExecResource res = (TestExecResource) it.next();
			
			if (res.getTestExecContext() == null)
				if (res.match(match))
					it.remove();		
			
		}
		
	}
	
	
	
	
}
