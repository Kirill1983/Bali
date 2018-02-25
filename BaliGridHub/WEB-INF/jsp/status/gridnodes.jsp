<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">

<h2><BR> Grid Nodes </h2> 

<table style="padding-left:40px; padding-top:40px;">
<tr>
    <td class="svcHdr" style="width:50px;"> id </td>
    <td class="svcHdr" style="width:150px;"> url </td>
</tr>
			
	<c:forEach items="${gridHub.nodes}" var="node">
    	<tr>
    		<td class="svcBody">  ${node.id}  </td>
    		<td class="svcBody">  ${node.url}  </td>
    	</tr>
	</c:forEach>		
	
</table>


	