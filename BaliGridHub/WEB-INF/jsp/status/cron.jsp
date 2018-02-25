<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">

<table style="padding-left:40px; padding-top:40px;">
<tr><td colspan=2 class="svcHdr" style="width:520px;"> Scheduled suite list: </td></tr>
			
	<c:forEach items="${scheduledSuiteList}" var="entry">
    	<tr>
    		<td class="svcBody" style="width:180px;">  <a href="/bali/form/pad/suites?filename=${entry.key}"> ${entry.key} </a> </td>
    		<td class="svcBody" style="width:340px;"> ${entry.value}</td>
    	</tr>
	</c:forEach>		
	
</table>



