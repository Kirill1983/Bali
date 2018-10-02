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

package org.kkonoplev.bali.classifyreport.model;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.IBug;


public class ClassifyReport implements Serializable {

    private static final Logger log = Logger.getLogger(ClassifyReport.class);

    public static int MAX_BUGS = 100;
    public static int MAX = 500;
    public static String FILENAME = "classifyNG";

    private HashMap<String, IBug> bug = new HashMap<String, IBug>(MAX_BUGS);
    private File reportDir = null;
    private File htmlReportFile = null;
    private Map<String, String> environments = new HashMap<String, String>(0);
    private String pre = "";
    private int warnId = 0;
    
    
    protected ArrayList<Warning> warnings = new ArrayList<Warning>(MAX);

    public ClassifyReport(File resultDir, String pre_) {    	
    	pre = pre_;
        init(resultDir, true);        
    }
 
	public ClassifyReport(File resultDir) {
		init(resultDir, true);
    }
	
    public ClassifyReport(File resultDir, String name, boolean load) {    	
        init(resultDir, name, load);        
    }
    
   
	public ClassifyReport(File resultDir, boolean load) {
		init(resultDir, load);        
    }

	private void init(File resultDir, String name, boolean load) {
    	reportDir = resultDir;
        htmlReportFile = new File(resultDir, name + ".html");
        log.debug("ClassifyReport created");
    }
    
    private void init(File resultDir, boolean load) {
    	init(resultDir, FILENAME, false);
    }
    
    
    public File getHtmlReportFile() {
		return htmlReportFile;
	}
    
    

	public File getReportDir() {
		return reportDir;
	}

	public void clear() {
        warnings.clear();
    }

       
    public void updateWarningCasesWithClassifyReportLink(){
    	for (Warning warning: warnings)
    		for (WarningCase warningCase: warning.getWarningCases())
    			warningCase.setClassifyReport(this);
    }
    
 

    public ArrayList<Warning> getWarnList() {
        return warnings;
    }

    public boolean contains(Warning newWarning) {
    	for (Warning warning : warnings) {
            boolean eq = warning.equals(newWarning);
            if (eq) {
                warning.addWarningCasesFrom(newWarning);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean removeIfContains(Warning newWarning) {
        for (Warning warning : warnings) {
            boolean w = warning.equals(newWarning);
            if (w) {
                warning.removeWarnsFrom(newWarning);
                return true;
            }
        }
        return false;
    }

    
    public synchronized void addWarning(Warning newWarning) {
      
    	int id = indexOf(newWarning);
    	
    	if (id == -1){
    		newWarning.setId(nextWarnId());
        	warnings.add(newWarning);
        	return;
        }
        
        Warning curWarning = warnings.get(id);
        curWarning.getOnNodeSuites().addAll(newWarning.getOnNodeSuites());
        curWarning.addWarningCasesFrom(newWarning);
            
    }
    
    public int indexOf(Warning newWarning){
    	int i = 0;
    	for (Warning warning : warnings) {
            boolean eq = warning.equals(newWarning);
            if (eq)	return i;
            i++;
        }
        return -1;
    }

    
    public void addWarningTracker(ClassifyReport classifyReport) {
    	classifyReport.updateWarningCasesWithClassifyReportLink();
        for (Warning warning : classifyReport.getWarnList())
            addWarning(warning);
    }

	public void clearFromTest(String test) {
		
		Iterator it = warnings.iterator();
		while (it.hasNext()){		
			Warning warning = (Warning) it.next();
			warning.clearFromTest(test);
			if (warning.getWarningCases().size() == 0)
				it.remove();
		}
		
	}

	public Map<String, String> getEnvironments() {
		return environments;
	}

	public void setEnvironments(Map<String, String> environments) {
		this.environments = environments;
	}


    public synchronized int nextWarnId(){
    	warnId++;
    	return warnId;
    }

	public Warning getWarnById(int warnId){ 
	
		for (Warning warn : warnings){
			if (warn.getId() == warnId)
				return warn;
			
		}
		
		return null;
	}
	
          
}
