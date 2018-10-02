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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.common.utils.Util;
import org.kkonoplev.bali.classifyreport.IBug;


public class Warning implements Serializable {

    private static final Logger log = Logger.getLogger(Warning.class);
    private ArrayList<ClassifyReport> classifyReports = new ArrayList<ClassifyReport>(MAX);
    private static int MAX = 500;

    private int id = -1;
    private String date = "";
    private String msg = "";
    private String bugID = "";
    private IBug bug;
    private ArrayList<WarningCase> warningCases = new ArrayList<WarningCase>(MAX);
    
    private ArrayList<OnNodeSuite> onNodeSuites = new ArrayList<OnNodeSuite>(); 

    public Warning(String msg) {
    	Date date1 = new Date();
        date = date1.toLocaleString();
        this.msg = msg;
        
        initBugID(msg);

        
    }
    
    public Warning(String suiteDir, String testCase, Error error, String errorId, String pre, String testClassLabelName, String subid) {
        //
        Date date1 = new Date();
        date = date1.toLocaleString();

        msg = error.getMessage();
        msg = msg.replaceAll("\n", "");
        msg = msg.replaceAll(sep, "");
        
        msg = msg.replaceFirst("java.lang.AssertionError:", "");
        msg = msg.replaceFirst("java.lang.Throwable:", "");
        msg = msg.replaceFirst("org.kkonoplev.cloud.lib.common.VerifyFailedError:", "");
        msg = msg.replaceFirst("org.kkonoplev.bali.common.warningtracker.CaptureEvent:", "");
        msg = msg.replaceFirst("junit.framework.AssertionFailedError:", "");
        msg = msg.replaceFirst("com.thoughtworks.selenium.", "");
        msg = msg.replaceFirst("org.kkonoplev.bali.common.warningtracker.NoncriticalAssertionError:", "");
        msg = msg.replaceFirst("org.kkonoplev.bali.common.warningtracker.CriticalAssertionError:", "Critical:");

        initBugID(msg);

        String msgAdd = "";

        int r = msg.indexOf(",,");
        if (r > 0) {
            msgAdd = msg.substring(r + 2);
            msg = msg.substring(0, r);
            if (msgAdd.contains(bugID)) {
                msgAdd = msgAdd.replaceFirst(bugID, "");
                msg += " " + bugID;
            }
        }

        String test = "";
        String method = testCase.substring(0, testCase.indexOf("("));
        test = testCase.substring(testCase.indexOf("(") + 1);
        test = test.replaceFirst("\\)", "");
        test = test.replaceFirst(pre, "");

        String url, id;

    }


    public Warning(String text, ClassifyReport classifyReport) {

    	classifyReports.add(classifyReport);

        int p = 0;
        String[] a = text.split(sep);
        date = a[p++];

        msg = a[p++];
        initBugID(msg);

        int offset = 2;

    }
    
	public void clearFromTest(String test) {
		
		Iterator it = warningCases.iterator();
		
		while (it.hasNext()){
			WarningCase warningCase = (WarningCase) it.next();
			if (warningCase.getTest().equals(test))
				it.remove();
		}
	}
	
	

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	void initBugID(String test) {
    	
        List<String> str = Util.getMatches(bugPattern, test);
        if (str.size() > 0)
            bugID = str.get(0);
        
    }

    public void setBug(IBug bug_) {
        bug = bug_;
    }

    public ArrayList<WarningCase> getWarningCases() {
        return warningCases;
    }

    public boolean contains(ClassifyReport newClassifyReport) {
    	for (ClassifyReport classifyReport : classifyReports)
            if (classifyReport.equals(newClassifyReport))
                return true;
        return false;
    }

    public boolean contains(WarningCase newWarningCase) {
        for (WarningCase elem : warningCases)
            if (elem.equals(newWarningCase))
                return true;
        return false;
    }

    
    public void addWarningCasesFrom(Warning warning){
    	
        for (WarningCase warningCase: warning.getWarningCases())
            	warningCases.add(warningCase);    
        
    }
    
    public int indexOf(WarningCase newWarningCase){
    	int i = 0;
    	for (WarningCase warningCase: warningCases){
    		boolean eq = warningCase.equals(newWarningCase);
    		if (eq) return i;
    		i++;
    	}
    	return -1;
    }
    
    public void removeWarnsFrom(Warning warning) {
        for (WarningCase warningCase: warning.getWarningCases())
            removeWarningCase(warningCase);
	}

    private void removeWarningCase(WarningCase warningCase) {
    	
    	Iterator<WarningCase> it = warningCases.iterator();
    	while (it.hasNext()){
    		WarningCase elem = it.next();
    		if (elem.equals(warningCase))
    			it.remove();    			
    	}
	
    }

	public ArrayList<ClassifyReport> getClassifyReports() {
        return classifyReports;
    }

 
	public String getBugID() {
        return bugID;
    }

    public String getSimpleBugID() {
        String bug = bugID.replaceAll("_", "");

        if (bug.startsWith(unreproduced))
            bug = bug.replaceFirst(unreproduced, "");

        return bug;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgShort() {
        return msg.replaceFirst(bugID, "");
    }

    public boolean equals(Warning warning) {

        if (warning.getClassifyReports().size() == 0) {

        	if (!warning.getBugID().equals(""))
                if (warning.getBugID().equals(bugID))
                    return true;

            String t1 = warning.getMsg().replaceAll(" ", "");
            String t2 = msg.replaceAll(" ", "");
            return t1.equals(t2);

        } else {

            String t1 = warning.getMsg().replaceAll(" ", "").replaceFirst(warning.getBugID(), "");
            String t2 = msg.replaceAll(" ", "").replaceFirst(bugID, "");
            return t1.equals(t2);

        }
    }
    
	public ArrayList<OnNodeSuite> getOnNodeSuites() {
		return onNodeSuites;
	}

	public void setOnNodeSuites(ArrayList<OnNodeSuite> onNodeSuites) {
		this.onNodeSuites = onNodeSuites;
	}

	public String toString() {
        return date + " " + msg + " " + warningCases.get(0);
    }

    public static String sep = "#@#";
    public static String unreproduced = "UNREPRODUCED";
    public static int elems = 2;

    public static String bugPattern = "_([A-Z]+-\\d{1,8})_";



	
}
