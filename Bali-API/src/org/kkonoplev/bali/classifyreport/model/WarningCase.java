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

package org.kkonoplev.bali.classifyreport.model;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseArtifact;

public class WarningCase implements Serializable {

    private static final Logger log = Logger.getLogger(Warning.class);
    
    private String suite = "";
    private String test = "";
    private String testLabel = "";
    private String method="";
    private String msgAdd="";
    private String threadId="1";
     
   
    private ArrayList<WarningCaseArtifact> artifacts = new ArrayList<WarningCaseArtifact>();
    
    ClassifyReport classifyReport = null;

    public WarningCase(String suite, String test, String testLabel, String msgAdd){
    	this.suite = suite;
    	this.test = test;
    	this.testLabel = testLabel;
    	this.msgAdd = msgAdd;
    }
    
   
    public ArrayList<WarningCaseArtifact> getArtifacts() {
		return artifacts;
	}
    
    
	public void setClassifyReport(ClassifyReport classifyReport) {
		this.classifyReport = classifyReport;
	}


	public String getThreadId() {
		return threadId;
	}

	public String getTest() {
		return test;
	}
	
	public String getTestLabel(){
		return testLabel;
	}

	public String getSuite() {
		return suite;
	}
   
    public String getMsgAdd(){
        return msgAdd;
    }

    public boolean equals(WarningCase warningCase){
       
    	boolean msgAddEqual = getMsgAdd().equals(warningCase.getMsgAdd());
    	boolean other = this.test.equals(warningCase.getTest());
    	boolean eq = msgAddEqual & other;
    	return eq;
    	
    }


    public String p(String a){
        if (a == null) return "''";

        try {
            String b = a.replaceAll("\\\\","\\\\\\\\");
            b = b.replaceAll("'","");
            b = b.replaceAll("\"","");
            return "'"+b+"'";
        } catch (Exception e ){
            log.warn("WarnInfo::p "+e);
        }
        return "''";
    }

      
    public ClassifyReport getClassifyReport() {
		return classifyReport;
	}

	
}
