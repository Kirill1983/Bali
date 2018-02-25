package org.kkonoplev.bali.classifyreport.htmlbuilder;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.WarningCase;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseArtifact;


public class WarningCaseHTMLBuilder {
	
	private static final Logger log = Logger.getLogger(WarningCaseHTMLBuilder.class);
	
	public static String getSelectOptionHtml(String jid, WarningCase warningCase){
		
		try {

	        String params = p(jid)+", "+
	        				p(warningCase.getSuite())+", "+
	                        p(warningCase.getTest())+", "+
	                        p(warningCase.getMsgAdd());
	        
	        for (WarningCaseArtifact artifact: warningCase.getArtifacts()){
	        	params += ", "+artifact.toJSON();
	        }

	        String sid = "";
	        String threadId = warningCase.getThreadId();
	        if (!threadId.equals("1"))
	        	sid = "#"+threadId;
	        	
	        return "<option  title=\""+warningCase.getTest()+"\" value=\"var d = new Array("+params+");\">"+warningCase.getTestLabel()+" "+sid+"</option> \n";
	    
		} catch (Exception e){
	            log.warn("WarnInfo::getSelectOptionHtml::"+e);
	    }

		return "";
	 }
	 
	 
	 public static String p(String a){
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



}
