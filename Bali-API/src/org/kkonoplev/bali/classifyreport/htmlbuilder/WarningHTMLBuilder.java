package org.kkonoplev.bali.classifyreport.htmlbuilder;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.classifyreport.model.WarningCase;


public class WarningHTMLBuilder {
	
	private static final Logger log = Logger.getLogger(WarningHTMLBuilder.class);
	
	public static String buildWarning(Warning warning, int id){
		
        String loc = "";
        try {

            //test List - select element
            loc = " on forming select warnsInfo list";
            String jid = "id" + id;
            String updateEvent = "\"warningSelectObj=this; updateMsgAddMainPage(); updateWarningCaseLayerIfShown(); return true;\"";
            String warnCasesList = "<select style='max-width: 300px;' id=id" + id + "s class=" + id + " onfocus=" + updateEvent + " onmousemove=" + updateEvent + " onclick=" + updateEvent + " onchange=" + updateEvent + " tabindex=" + id + " >\n";

            for (WarningCase warningCase : warning.getWarningCases())
                warnCasesList += WarningCaseHTMLBuilder.getSelectOptionHtml(jid, warningCase);

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
            String layerUpdateOpenLink = "<a onclick=\"warningSelectObj="+jid+"s; showWarningCaseLayer();\" href=\"javascript::return%20false;\" > view details </a>";
            		
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
	
	public static String getJiraLink(Warning warning, String jid) {
        
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

	
	
	protected static int calcID(String val) {
		int v = 0;

	    for (int i = 0; i < val.length(); i++) {
	    	v += val.charAt(i);
	    }

	    return v;
	}

	


}

