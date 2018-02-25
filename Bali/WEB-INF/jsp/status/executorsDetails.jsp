<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<meta http-equiv=refresh content=15 />

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
<link type="text/css" href="/bali/data/css/Default.css" rel="StyleSheet">

<script type="text/javascript">
 

  function updateSuiteExecContext(suite, test, property, checked) {
	  document.getElementById("nameid").name = property;
	  document.getElementById("nameid").value = checked;
	  document.getElementById("className").value = test;
	  document.getElementById("resultDir").value = suite;
	  updateForm.submit();
  }

</script>

<form name=updateForm action="/bali/form/status/update" method="post">
 <input type=hidden id=nameid name="" value="">
 <input type=hidden id=className name="className" value="">
 <input type=hidden id=resultDir name="resultDir" value="">
 <input type=hidden name="redirect" value="/form/status/executors/refresh">
</form>

<TABLE style="padding-left:40px; padding-top:40px;">

<tr><td class="svcHdr" style="width:60px;"> Executor </td>
	<td class="svcHdr" style="width:50px;"> Status </td>
	<td class="svcHdr" style="width:60px;"> Suite Context </td>
	<td class="svcHdr" style="width:15px;"> Options </td>
	<td class="svcHdr" style="width:80px;"> Test Context </td>
	<td class="svcHdr" style="width:10px;"> errs </td>
	<td class="svcHdr" style="width:30px;"> status </td> 
	<td class="svcHdr" style="width:60px;"> start time </td>
	<td class="svcHdr" style="width:60px;"> end time </td>
	<td class="svcHdr" style="width:15px;" > elaps (min) </td>
	<td class="svcHdr" style="width:10px;"> wait On Error </td>
	<td class="svcHdr" style="width:10px;"> capture screen </td>
	<td class="svcHdr" style="width:100px;"> Use Resources </td>
	<td class="svcHdr" style="width:10px;"> Act </td>
	<td class="svcHdr" style="width:10px;"> Cmd </td>
</tr>

<c:forEach var="testExecutor" items="${execProcessor.testExecutors}">

<c:set var="testExecContext" value="${testExecutor.testExecContext}" />
 
<tr >

	<td class="svcBody" style="width:60px;"> ${testExecutor.name} </td>
	<td class="svcBody" style="width:20px;"> ${testExecutor.state} </td>
	<td class="svcBody" style="width:20px;"> <a href="#"> ${testExecContext.suiteExecContext.resultDir} </a> </td>
	<td class="svcBody" style="width:20px;"> ${testExecContext.suiteExecContext.suiteMdl.options} </td>
    <td class="svcBody" style="width:140px;"> ${testExecContext.shortClassName} </td>

    
	<td class="svcBody" style="width:20px;">
		${testExecContext.errorCount} 
    </td>
	 
	 <c:set var="startDate" value="${testExecContext.startDate}"/>
	 
     <td class="svcBody" style="width:30px;"> 
     	<c:choose>
  			<c:when test="${testExecContext.status.desc == 'success'}">
  			 	<b> ${testExecContext.status.desc} </b> 
  			</c:when>
  			<c:when test="${testExecContext.status.desc == 'fail'}">
  			 	<i> ${testExecContext.status.desc} </i> 
  			</c:when>
			<c:otherwise>   
				${testExecContext.status.desc}
 		 	</c:otherwise>
	 	</c:choose>
     	
     </td>
     <td class="svcBody" style="width:60px;"> ${testExecContext.startDateFmt} </td> 
     <td class="svcBody" style="width:60px;"> ${testExecContext.endDateFmt} </td>
     <td class="svcBody" style="width:20px;"> ${testExecContext.elapsedFmt} </td>
     <td class="svcBody" style="width:10px;"> <input type="checkbox"  disabled=true  name="debugMode" onclick="updateSuiteExecContext('${testExecContext.suiteExecContext.resultDir}','${testExecContext.className}', this.name, this.checked);"  <c:if test="${testExecContext.debugMode == true}"> checked </c:if>> </input> </td>
     <td class="svcBody" style="width:10px;"> <input type="checkbox"  disabled=true  name="captureMode" onclick="updateSuiteExecContext('${testExecContext.suiteExecContext.resultDir}','${testExecContext.className}', this.name, this.checked);" <c:if test="${testExecContext.captureMode == true}"> checked </c:if>> </input> </td>
     <td class="svcBody" style="width:100px;" > ${testExecContext.resourcesNames} </td>
    
     <td class="svcBody" style="width:10px;">  
     <c:if test="${testExecContext.testExecutor.state == 'WAITING'}"> <img src='/bali/data/images/add_single.gif' onclick="updateSuiteExecContext('${testExecContext.suiteExecContext.resultDir}','${testExecContext.className}','testExecutorCmd','play');" > </c:if>  
     </td>
     <td class="svcBody" style="width:10px;"> 
     
     	<c:choose>
  			<c:when test="${testExecContext.completed == true}">
  			 	 <img src='/bali/data/images/replay.gif' onclick="updateSuiteExecContext('${testExecContext.suiteExecContext.resultDir}', '${testExecContext.className}','suiteExecutorProcCmd','replay');" > 
  			</c:when>
  			<c:when test="${testExecContext.testExecutor != null && testExecContext.status.desc == 'in progress'}">
  			 	<img src='/bali/data/images/interrupt.gif' onclick="updateSuiteExecContext('${testExecContext.suiteExecContext.resultDir}', '${testExecContext.className}','suiteExecutorProcCmd','interrupt');" > 
  			</c:when>
 		 </c:choose>    
     </td>
  
     
</tr>
</c:forEach>

</TABLE>




