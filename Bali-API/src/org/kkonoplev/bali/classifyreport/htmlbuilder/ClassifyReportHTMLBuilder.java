package org.kkonoplev.bali.classifyreport.htmlbuilder;


import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportViewConfig;
import org.kkonoplev.bali.classifyreport.htmlbuilder.WarningHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.classifyreport.model.WarningCase;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseArtifact;
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
		text += "<title> "+viewConfig.getHeaderText()+" NG </title>\n";
		text += "<script src=\"../../data/js/flot/jquery.js\"> </script>\n";
		text += "<script src=\"../../data/tree/jquery.tree.js\"> </script>\n";
		text += "<link href=\"../../classify.css\" rel=\"stylesheet\" type=\"text/css\" title=\"Style\" >\n";
		text += "<script src=\"../../classify.js\"> </script>\n";
		
		text += "<script>"+buildDataVar()+ "</script>\n";
		
		text += "</head> <body onload=\"onPageLoad();\" >\n";
		text += "<br> <h1> "+viewConfig.getHeaderText()+" </h1> <br>\n";
		text += "<hr size='1'/>\n ";
		text += "<h2> Results </h2>\n";
		  
		return text;

	}
	
	public String buildDataVar() {

		StringBuilder var = new StringBuilder();
		var.append("var data = { \n");
		var.append(" 'warns': [ \n");
		
		int id = 0;
	    for (Warning warning : classifyReport.getWarnList()){
	    	var.append("{ \n");
	    	var.append(" id: '"+id+"', \n");
	       	var.append(" msg: '"+warning.getMsgShort()+"', \n");

    		var.append(" cases: [ \n");
    		
	    	for (WarningCase warnCase : warning.getWarningCases()){
	    		var.append("{ \n");
	    		var.append(" suite : '"+warnCase.getSuite()+"',\n");
	    		var.append(" test : '"+warnCase.getTest()+"',\n");
	    		var.append(" msgAdd : '"+warnCase.getMsgAdd()+"',\n");
	    		var.append(" artifacts: [ \n");
	    		for (WarningCaseArtifact warnCaseArtfct : warnCase.getArtifacts())
	    			var.append(" "+warnCaseArtfct.toJSON()+", \n");
	    		var.append(" ] \n");
	    		var.append("},");
		    		
	    	}
	    	
	    	var.append(" ] \n");
	    	
	    	var.append("}, \n");
	    	
	    	id++;
	    }
	    var.append(" ] \n");
	    var.append("}; \n");

		return var.toString();
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
	    		textNoBug += buildWarning(warning, n++);
	        else 
	            textBug += buildWarning(warning, n++);
	        
	    text += (textBug + textNoBug);
        text += "</table> \n";

	    if (classifyReport.getWarnList().size() == 0)
	            text += "<br> <h2> <font color=blue> No errors found! </font> </h2> <br><br> \n";
	    else 
	    	text += rerunModule();
	     
	    
	    text += "<div id=warningCasesLayer class=warnCaseLayer > warning cases </div>";
	    text += "<div id=treelog onclick='this.style.visibility = hidden;' class=treeLog ></div>";
	    text += "<body> <html> \n";

	    return text;

	}

	

	public String buildWarning(Warning warning, int id){
		
        String loc = "";
        try {

            //test List - select element
            loc = " on forming select warnsInfo list";
            String jid = "id" + id;
            String updateEvent = "\"warningSelectObj=this; updateMsgAddMainPage(); updateWarningCaseLayerIfShown(); return true;\"";
            String warnCasesList = "<select style='max-width: 300px;' id=" + id + " class='warnCases' tabindex=" + id + " >\n";

            for (WarningCase warningCase : warning.getWarningCases()){
            	
            	String sid = "";
     	        String threadId = warningCase.getThreadId();
     	        if (!threadId.equals("1"))
     	        	sid = "#"+threadId;
     	        
                warnCasesList +=  "<option  title=\""+warningCase.getTest()+"\">"+warningCase.getTestLabel()+" "+sid+"</option> \n";
            }
        	    

            warnCasesList += "</select>\n";

            //bugID
            String bugID = warning.getBugID();
            loc = " on forming BUG ID";
            String msg = warning.getMsg();
            String msgInfo = msg;
            msgInfo = msgInfo.replaceFirst(bugID, "");
            String checkbx = "";


            String bugIDlink = "";
            checkbx = "<input class='action' name='"+warning.getId()+"' type='checkbox' id='" + calcID(msg) + "c' >";
            if (bugID.equals("")) {
                bugIDlink = " <SELECT class='action' id='" + calcID(msg) + "'> " +
                        "<OPTION SELECTED> </OPTION>" +
                        "<OPTION > test design </OPTION>" +
                        "<OPTION > bug </OPTION>" +
                        "<OPTION > run error </OPTION>" +
                        "</SELECT> \n" +
                        checkbx;
            } else {
                String exitText = "<BR> View </a><br> <span class=smalltxt> press Esc to exit </span> <br><br>";
                bugID = bugID.replaceAll("_", "");
                bugIDlink = "<A target='_blank' href=\"http://jira/browse/" + bugID +"\"> <font>" + bugID + " </font></A>";
                bugIDlink = "<div id='id"+id+"bugdiv'>" + bugIDlink+ "</div>";
            }

            loc = "on forming error line";
            String rerunTest = "&nbsp; <a onclick=\"rerunWarning("+warning.getId()+");\" href='javascript::return%20false;'> rerun </a>";

            String status = "";
            if (!bugID.equals("")) {
                if (warning.getBugID().replaceAll("_", "").startsWith(Warning.unreproduced))
                    status = "U";
                else
                    status = "B";
            } else
                    status = "R";

            int msgid = calcID(msg);
            String link = " <a name='" + msgid + status + "' /> ";

            String cl = "";

            if (id % 2 == 0)
                cl = " class='a' ";
            else
                cl = " class='b' ";

            WarningCase warningCase = warning.getWarningCases().get(0); 
            String layerUpdateOpenLink = "<a warnId='"+id+"' class='view'  href=\"javascript::return%20false;\" > view details </a>";
            		
            String src = "";
            src = "<tr" + cl + ">" +
                    "<td>" + bugIDlink + rerunTest+ "</td> \n" +
                    "<td>   "
                    + "<div id=" + jid + "msg ondblclick=\"this.innerHTML=this.innerHTML.substring(0, this.innerHTML.length/5);\" >"
                    + msgInfo 
                    + "</div> "
                    + "<div id='" + jid + "msgAdd'>"
                    + "<span><font color=grey ondblclick=\"this.innerHTML='hidden';\">" + warningCase.getMsgAdd() + " </font> </span>"
                    + "</div>\n" + layerUpdateOpenLink +"</td>" +
                    "<td>" + warnCasesList + "</td> \n" +
                    "</tr> \n";

            return src;
            
        } catch (Exception e) {
            log.warn("Warning::toHtml error:" + e + " " + loc);
        }

        return "";

		
	}
	
	public String getJiraLink(Warning warning, String jid) {
        
		String bugIDlink = "";
        String bugID = warning.getBugID();
        
        //
        if (warning.getBugID().equals("")) {
            String submitonline = "<BR> <a href='#aabb123' onclick='submit();'>auto submit</a>";
            bugIDlink = "<A target='_blank' href='https://jira.yandex-team.ru/secure/CreateIssue!default.jspa'> <font color='red'> create new issue </font> </A>" + submitonline;

        } else {
            //String status = bug.get("Status");

            bugID = warning.getBugID().replaceAll("_", "");
            //String id = warning.getBugID().replaceFirst(unreproduced, "");
            String onMouse = "onclick='" + jid + "bug.onclick();' href='javascript::return%20false;'";
            
            bugIDlink = "<A target='_blank' href='http://dbatlas.db.com/jira01/browse//" + bugID + "'> " + bugID + "</A>";

        }

        bugIDlink = "<div id='bugdiv'>" + bugIDlink+ "</div>";

        return bugIDlink;
    }

	
	
	protected int calcID(String val) {
		int v = 0;

	    for (int i = 0; i < val.length(); i++) {
	    	v += val.charAt(i);
	    }

	    return v;
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
