<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html style="background:#E0E0FF;">

<head>

<body>

<br>

<h1 align=center > Bali Suite Execution Report </h1>
<TABLE align=center border=1>
<tr>
	<td class="svcAll"> 
		<a target="_blank" href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/results/${suiteExecContext.resultDir}/classify.html"> errors classify report </a> &nbsp&nbsp
		<a target="_blank" href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/results/${suiteExecContext.resultDir}/captureclassify.html"> screen capture report </a> &nbsp&nbsp
		<a target="_blank" href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/form/status/perfomancereport?resultDir=${suiteExecContext.resultDir}"> perfomance report </a> &nbsp&nbsp
    </td>
</tr>
</TABLE>

<BR><BR>

<TABLE align=center border=1>
<TR> <TD> Target grid: </td> <td> ${suiteExecContext.suiteMdl.options} </TD> </TR>
<TR> <TD> Browser: </td> <td>${suiteExecContext.suiteMdl.browser} </TD> </TR>
<tr> <td> &nbsp; </td> <td> &nbsp; </td>  </tr>

<TR> <TD> Suite start time:</td> <td> ${suiteExecContext.startDateFmt} </TD> </TR>
<TR> <TD> Suite end time: </td> <td> ${suiteExecContext.endDateFmt} </TD></TR>
<TR> <TD> Suite elapsed: </td> <td> ${suiteExecContext.elapsedFmt} mins</TD></TR>
<TR> <td> Sum per each  test elapsed: </td> <td> ${suiteExecContext.summaryElapsedFmt} mins </td> </tr>
<tr> <td> accelerate rate: </td> <td> ${suiteExecContext.timeAccelerateRateFmt}  </TD> </TR>
<tr> <td> &nbsp; </td> <td> &nbsp; </td>  </tr>

<TR> <TD> Scenarios done: </td> <td> ${suiteExecContext.executedCount} </TD> </TR>
<TR> <TD> passed: </td> <td>${suiteExecContext.successCount} </TD> </TR>
<TR> <TD> failed: </td> <td>${suiteExecContext.failCount} </TD> </TR>
<TR> <TD> success rate: </td> <td> ${suiteExecContext.successRateFmt}% </TD> </TR>


</TABLE> <BR> <BR>

<TABLE align=center border=2 cellpadding=5>

<tr>
	<td>  Execution list: </td>
	<td style="width:20px;"> errors </td>
	<td style="width:30px;"> status </td> 
	
	<c:if test="${suiteExecContext.loadmode == true}">
	<td style="width:20px;"> loops </td>
	</c:if>
	
	<td> start time </td>
	<td> end time </td>
	<td style="width:20px;"> elapsed (min) </td>
	<td style="width:10px;"> capture screen </td>
</tr>

<c:forEach var="testExecContext" items="${suiteExecContext.testExecContexts}">
<tr>
	<td> ${testExecContext.shortClassName} </td>

    
	<td style="width:20px;">
		<c:choose>
  			<c:when test="${testExecContext.hasCriticalErrors == false}">
  			 	${testExecContext.errorCount} 
  			</c:when>
			<c:otherwise>   
				<b> ${testExecContext.errorCount} </b>
 		 	</c:otherwise>
	 	</c:choose>
	 </td>
	 
	 <c:set var="startDate" value="${testExecContext.startDate}"/>
	 
     <td style="width:30px;">      	
     	<c:choose>
  			<c:when test="${testExecContext.status == 'success'}">
  			 	<b> ${testExecContext.status} </b> 
  			</c:when>
  			<c:when test="${testExecContext.status == 'fail'}">
  			 	<i> ${testExecContext.status} </i> 
  			</c:when>
			<c:otherwise>   
				${testExecContext.status}
 		 	</c:otherwise>
	 	</c:choose>
     </td>
     
     <c:if test="${suiteExecContext.loadmode == true}">
		<td style="width:20px;"> ${testExecContext.loops} </td>
	 </c:if>
     <td> ${testExecContext.startDateFmt} </td> 
     <td> ${testExecContext.endDateFmt} </td>
     <td style="width:20px; "> ${testExecContext.elapsedFmt} </td>
     <td style="width:10px;"> <c:if test="${testExecContext.captureMode == true}"> yes </c:if> </input> </td>     
</tr>
</c:forEach>

</TABLE>

</body>
</html>