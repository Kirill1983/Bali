package org.kkonoplev.bali.classifyreport.htmlbuilder;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.common.utils.Util;

public class ClassifyReportHTMLBuilder {
	
	private static final Logger log = Logger.getLogger(ClassifyReportHTMLBuilder.class);
	
	private ClassifyReport classifyReport;
	private ClassifyReportViewConfig viewConfig = ClassifyReportViewConfig.DEFAULT;
	
	public ClassifyReportHTMLBuilder(ClassifyReport classifyReport){
		this.classifyReport = classifyReport;		
	}
	
	public ClassifyReportHTMLBuilder(ClassifyReportViewConfig viewConfig, ClassifyReport classifyReport){
		this.classifyReport = classifyReport;		
		this.viewConfig = viewConfig;
	}
	
	
	public String save(){
	      try {
	            String text = "";
	            text = buildHtml();
	            Util.saveTextToFile(text, classifyReport.getHtmlReportFile(), false);
	            return text;
	        } catch (Exception e) {
	        	log.warn(e, e);
	            log.warn("ClassifyReportBuilder::saveToHtml");
	        }
	      
		return "";

	}
	
	public String buildHtml(){
		
		String text = buildHeader();
		text += buildReport();
		return text;

	}

	public String buildHeader(){
		
		String text = "<html> <meta charset='UTF-8'> <head>\n";
		text += "<title> "+viewConfig.getHeaderText()+"  </title>\n";
		text += "<script src=\"../../data/js/flot/jquery.js\"> </script>\n";
		text += "<script src=\"../../data/tree/jquery.tree.js\"> </script>\n";
		text += "<link href=\"../../classify.css\" rel=\"stylesheet\" type=\"text/css\" title=\"Style\" >\n";
		text += "<script src=\"../../classify.js\"> </script>\n";
		text += "</head> <body onload=\"onPageLoad();\" >\n";
		text += "<br> <h1> "+viewConfig.getHeaderText()+" </h1> <br>\n";
		text += "<hr size='1'/>\n ";
		text += "<h2> Results </h2>\n";
		  
		return text;

	}
	
	public String buildReport(){
		
		String text = "";
	    text += "<table WIDTH=95% class='detail'> \n";
	    text += "<tr>  <th width=5%> <h4> "+viewConfig.getColumn1Text()+" </h4> </th> <th width=70%> <h4>"+viewConfig.getColumn2Text()+" </h4> </th> <th width=20%>  <h4> "+viewConfig.getColumn3Text()+" </h4> </th>  </tr>\n";
	
	    int n = 0;
	    String textBug = "";
	    String textNoBug = "";

	    for (Warning warning : classifyReport.getWarnList())
	    	if (warning.getBugID().equals(""))
	    		textNoBug += WarningHTMLBuilder.buildWarning(warning, n++);
	        else 
	            textBug += WarningHTMLBuilder.buildWarning(warning, n++);
	        
	    text += (textBug + textNoBug);
        text += "</table> \n";

	    if (classifyReport.getWarnList().size() == 0)
	            text += "<br> <h2> <font color=blue> No errors found! </font> </h2> <br><br> \n";
	    else 
	    	text += rerunModule();
	     
	    
	    text += "<div id=warningCasesLayer style=\"visibility: hidden; position: absolute; background:#E0E0FF; left: 0px; top: 0px;\"> warning cases </div>";
	    text += "<div id=treelog onclick='this.style.visibility = hidden;' style='width: auto; height: auto; visibility: hidden; background: #E7EEF7; position: absolute; border: solid 1px black;'></div>";
	    text += "<body> <html> \n";

	    return text;

	}


	public String rerunModule(){
		
		if (!viewConfig.isRerunBlockShow()){
			return "";
		}
		
		StringBuilder text = new StringBuilder();
		
    	text.append("<form  action='/bali/form/status/rerunonenvs' method=get target='_blank'>");
    	text.append("<br><input type=hidden id=resultDir name=resultDir value='"+classifyReport.getReportDir().getName()+"'");
    	text.append("<br><input type=hidden name=warningIDs id=warningIDs value=''>");
        
    	text.append("<br>&nbsp;&nbsp; <input type=button onclick=\"rerunMarkedWarnings();\" value='Rerun marked warnings'> <br><br>");
    	text.append("&nbsp;&nbsp; &nbsp;&nbsp;");
    	
    	if (classifyReport.getEnvironments().size() > 0){
    		text.append("<select size=4 multiple name='environments'>");
    		Iterator it = classifyReport.getEnvironments().entrySet().iterator();
    		while (it.hasNext()){
    			Entry<String, String> entry = (Entry<String, String>) it.next();
    			text.append("<option value="+entry.getKey()+" >"+entry.getValue()+"</option>");
    		}
    		text.append("</select>");	
    	}
    	text.append("<br><br>&nbsp;&nbsp; <input onmouseover='updateWarningIdsList();' type=submit value='Rerun on envs'> <br><br>");
        	
    	text.append("</form>");
    	
    	return text.toString();
	}

}
