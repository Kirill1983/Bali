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

import java.util.Date;

import org.kkonoplev.bali.suiteexec.TestExecContext;

public abstract class TestExecResource {
	
	public static String AGENT = "agent";
	public static String BROWSER = "browser";
	
	public static final String WAITINIT = "wait init";
	
	protected TestExecContext testExecContext;
	protected boolean preLocked = false;
	protected Date startedUseDate;
	protected String type = "";
	
	protected String name = "";
	protected String status = WAITINIT;;
	
	//to which project the resource belongs
	protected String project;
	
	
	public TestExecResource(){		
	}
	
	/*
	 * when resource is locked to be used in test
	 *  this method is called to initialize the inner objects.
	 */
	public boolean init(TestExecContext testExecContext) throws Exception {
		
		return true;
	}

	/*
	 * for each Resource we can define logic to match it.
	 * This logic is used, when deleting items from pool.
	 */
	public boolean match(String match) {
		
		return false;
	}

	/*
	 * this method is called to render resource 
	 * status on web 
	 */
	public String getHeadText(){
		String tx = name+" "+type;
		if (status.length() > 0)
			tx += " "+status;
		return tx;
	}

	/*
	 * this method is called to define if current resource
	 * satisfies for requested filters.
	 * override method to implement expected bahaviour.
	 */
	public boolean matchRequirements(String properties){
		if (properties.equals(""))
			return true;
		else
			return false;
	}

	/*
	 * is this resource is free to used in the test context with the properties
	 * override method to implement expected bahaviour.
	 */
	public boolean isFree(TestExecContext testExecContext, String properties) {
		if (testExecContext != null)
			return false;
		return matchRequirements(properties);
	}

	
	public abstract String getDescription();
	

	/* is resource free for use by next scenario */
	public boolean isFree(String properties) {
		return (testExecContext == null);
	}

	public TestExecContext getTestExecContext() {
		return testExecContext;
	}


	public void setTestExecContext(TestExecContext testExecContext) {
		this.testExecContext = testExecContext;
	}


	public Date getStartedUseDate() {
		return startedUseDate;
	}


	public void setStartedUseDate(Date startedUseDate) {
		this.startedUseDate = startedUseDate;
	}
	

	
	public boolean isType(String type){
		if (this.type.equalsIgnoreCase(type))
			return true;
		
		return false;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public boolean isPreLocked() {
		return preLocked;
	}

	public void setPreLocked(boolean preLocked) {
		this.preLocked = preLocked;
	}	
	
	
	

}
