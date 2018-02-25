<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page import="java.io.*" %>
<%@ page import="com.informatica.cloud.bali.common.*" %>
<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">

<script type="text/javascript">
 
  function onView() {
	  viewForm.submit();
  }
  
  function makeReportList() {
  
  		document.getElementById('reportlist').value = "";
	 	tags=document.body.getElementsByTagName('input');
	    reportList = "";
	    
	  	for(var j=0; j<tags.length; j++) 
	 		if ((tags[j].className == 'reportCheckbox') && (tags[j].checked == true)) 
	 				reportList += tags[j].value+",";
	 				
	 	document.getElementById('reportlist').value = reportList;
		return;	    
  }
  
  
  function onSummaryReport() {
  	 makeReportList();
  	 reportForm.submit();	
  }
  
</script>
  
<form name=viewForm action="/bali/form/status/suitecontext" target='_blank' method="get">
<table style="padding-left:40px; padding-top:40px;">
<tr><td class="svcHdr">	Run History: </td></tr>
<tr>
	<td class="svcSmall">
		<form:select id='history' name="resultDir" path="history" items="${historyList}" />	
		<BR><BR>

		<table cellspacing="0" cellpadding="0" id="paneBtns" class="InfButton">
			<tr><td onclick="onView();" onmouseout="pnBtn.setClass(this,'btn')" onmouseover="pnBtn.setClass(this,'hoverbtn')" title="View" id="viewBtn" class="btn"><span>View</span></td></tr>
		</table>		
	</td>
</tr>
</table>
</form>

<form name=reportForm action="/bali/form/status/summaryreport" target='_blank' method="get">

<table style="padding-left:40px; padding-top:40px;">
<tr><td colspan=3 class="svcHdr" style="width:200px;"> Active and last 7 suites context's history: </td></tr>
		
	<c:forEach items="${lasttenlist}" var="entry">
    	<tr>
    		<td class="svcBody" style="width:15px;"> <input type="checkbox" class="reportCheckbox" checked="checked" value="${entry.key}"> </td>
    		<td class="svcBody" style="width:160px;">  <a href="/bali/form/status/suitecontext?resultDir=${entry.key}"> ${entry.key} </a> </td>
    		<td class="svcBody" style="width:40px;"> ${entry.value}</td>
    	</tr>
	</c:forEach>		
	
	<tr>
  	<td colspan=3> <BR>
		<table cellspacing="0" cellpadding="0" id="paneBtns" class="InfButton">
			<tr><td onclick="onSummaryReport();" onmouseout="pnBtn.setClass(this,'btn')" onmouseover="pnBtn.setClass(this,'hoverbtn')" title="View" id="viewBtn" class="btn"><span>summary report</span></td></tr>
		</table>
		<input type=hidden value="" size=100 name="reportlist" id="reportlist">		
	</td>
	</tr>
	
</table>
</form>



