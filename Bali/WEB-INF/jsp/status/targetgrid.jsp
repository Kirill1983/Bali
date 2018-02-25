<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">

<h2><BR> </h2> 

<form:form action="/bali/form/status/targetgrid" method="get" name="ViewForm" modelAttribute="suiteMdl">
<table style="padding-left:40px; padding-top:40px;">

<tr><td class="svcHdr">	Target options:</td></tr>
<tr>
	<td class="svcBody">
		<form:select path="options" onchange="ViewForm.submit();" items="${optionsList}" />		
		
	</td>	

</tr>
</form:form>

<table style="padding-left:40px; padding-top:40px;">
<tr><td colspan=2 class="svcHdr" style="width:600px;"> options properties </td></tr>
<c:forEach items="${props}" var="entry">
    	<tr>
    		<td class="svcBody" style="width:160px;">  ${entry.key} </td>
    		<td class="svcBody" style="width:440px;"> ${entry.value} </td>
    	</tr>
</c:forEach>	

</table>
	