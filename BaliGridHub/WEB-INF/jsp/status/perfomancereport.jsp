<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html>

<meta http-equiv=refresh content=60 />
<head>
	<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
	<link type="text/css" href="/bali/data/css/Default.css" rel="StyleSheet">
    <script language="javascript" type="text/javascript" src="/bali/data/js/flot/jquery.js"></script>
    <script language="javascript" type="text/javascript" src="/bali/data/js/flot/jquery.flot.js"></script>
    <script language="javascript" type="text/javascript" src="/bali/data/js/flot/jquery.flot.navigate.js"></script>
    <script language="javascript" type="text/javascript" src="/bali/data/js/flot/jquery.flot.time.js"></script>
</head>

<body>

<TABLE style="padding-left:40px; padding-top:20px;">
<tr>
<td class="svcAll" style="width: 200px;"> 
	<a  href="/baligridhub/form/status/suitecontext?resultDir=${resultDir}"> Suite Execution Context View </a>
</td>
</tr>
</table>


<BR>


<h3 align=center> Metrics Report </h3> 
<br>
<BR><BR>

<CENTER>
<TABLE>

<tr>
    <td class="svcHdr" style="width:120px;"> Operation name </td>
    <td class="svcHdr" style="width:400px;"> <center>Responses</center> </td>
	<td class="svcHdr" style="width:40px;"> Avg respone </td>
</tr>

<c:forEach var="operation" items="${perfomanceReport.operations}">
<tr>
	<td class="svcBody" style="width:120px;"> ${operation.name} </td>
	
	
	<td class="svcBody" style="width:600px;">
	
		<div id="placeholder${operation.id}" style="width:600px;height:300px;"></div>

    	<script type="text/javascript">
			$(function () {
			
			var d1 = [
			<c:forEach var="responseTime" items="${operation.responseTimes}">
	 			[${responseTime.time.time}, ${responseTime.delay}], 
	 		</c:forEach>
	 		];	 
	 
    			    
		    $.plot($("#placeholder${operation.id}"), [d1], 
		    		{
		    				xaxis: { mode: "time"},
		    				grid: { hoverable: true, clickable: true },
		    				zoom: { interactive: true },
        					pan: { interactive: true }
		    		});
			
			});
		</script>
	
	<center>
	 <SELECT>
	 <c:forEach var="responseTime" items="${operation.responseTimes}">
	 	<OPTION> ${responseTime.timeFmt}   &nbsp; &nbsp;   ${responseTime.delay} </OPTION>	 
	 </c:forEach>	 
	 </SELECT>
	 </center> 	
	</td>
	
	<td class="svcBody" style="width:40px;"> ${operation.avgDelayFmt} </td>
</tr>
</c:forEach>

</TABLE>
</CENTER>

</body>
</html>