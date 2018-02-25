/* 

 * Copyright � 2011 Kirill Konoplev
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
 * 
 */


package org.kkonoplev.bali.suiteexec;

import java.io.Serializable;



public class SuiteMdl implements Serializable {
	
	public static String TEST_INFO_DELIM = "#";
	
	public String browser="";
	public String options="";
	public String tests="";
	public String resultDir="";
	
	public String name="";
	public String filename="";
	
	public String loadminutes = "0";
	public String rumpup = "3000";
	
	public boolean loadmode = false;
	public boolean capturemode = true;
	public boolean debugmode = false;

	public String description="";
	public String email="";
	public String cron="";
	
	public int nodeId = 1;
	
	
	public SuiteMdl clone(){
		
		SuiteMdl suiteMdl = new SuiteMdl();
		suiteMdl.setBrowser(browser);
		suiteMdl.setOptions(options);
		suiteMdl.setTests(tests);
		suiteMdl.setResultDir(resultDir);
		suiteMdl.setName(name);
		suiteMdl.setFilename(filename);
		suiteMdl.setLoadminutes(loadminutes);
		suiteMdl.setLoadmode(loadmode);
		suiteMdl.setCapturemode(capturemode);
		suiteMdl.setDebugmode(debugmode);
		suiteMdl.setDescription(description);
		suiteMdl.setEmail(email);
		suiteMdl.setCron(cron);
		
		return suiteMdl;
		
	}
	
	
	public int getNodeId() {
		return nodeId;
	}


	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}



	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String targetGrid) {
		this.options = targetGrid;
	}
	
	public String getTests() {
		return tests;
	}
	public void setTests(String tests) {
		this.tests = tests;
	}
	
	public String[] getTestList(){
		return tests.split(",");
	}
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	
	public String toString(){
		return name+" ,"+browser+", "+options;
	}

	public String toStringFull(){
		return ", name:"+name+", b:"+browser+", o:"+options+", rdir:"+resultDir+", tests: "+tests;
	}

	public boolean getLoadmode() {
		return loadmode;
	}

	public void setLoadmode(boolean loadmode) {
		this.loadmode = loadmode;
	}


	public boolean isCapturemode() {
		return capturemode;
	}

	public void setCapturemode(boolean capturemode) {
		this.capturemode = capturemode;
	}

	public boolean isDebugmode() {
		return debugmode;
	}

	public void setDebugmode(boolean debugmode) {
		this.debugmode = debugmode;
	}

	public String getLoadminutes() {
		return loadminutes;
	}

	public void setLoadminutes(String loadminutes) {
		this.loadminutes = loadminutes;
	}

	public String getRumpup() {
		return rumpup;
	}

	public void setRumpup(String rumpup) {
		this.rumpup = rumpup;
	}
	
	
	
	
	

}
