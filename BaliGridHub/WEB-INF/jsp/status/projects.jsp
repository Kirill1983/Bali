<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">

<table style="padding-left:40px; padding-top:40px;">
<tr>
	<td class="svcHdr" style="width:220px;" > Project </td>
	<td class="svcHdr" style="width:170px;"> Build Config </td>
	<td class="svcHdr" style="width:70px;" > Build Date </td>
	<td class="svcHdr" style="width:70px;" > Load Date </td>
	<td class="svcHdr" style="width:100px;" > Parent project </td>
	<td class="svcHdr" style="width:30px;" > Reload </td>

</tr>
			
	<c:forEach items="${projects}" var="project">
    	<tr>
    		<td class="svcBody" style="width:220px;">  <a href="/bali/form/status/projects?name=${project.name}"> ${project.name} </a>  </td>
	   		<td class="svcBody" style="width:170px;">  
    		 											<a href="/bali/form/status/projects/buildrun?name=${project.name}" title="${project.buildConfig.log}" > run </a> command : ${project.buildConfig.command}   
    		 											<BR> <BR> 
    		 											<textarea style='background-color: #E7EEF7' rows=3> ${project.buildConfig.log} </textarea> <BR> 
    		 											 
    		  
    		</td>
    		<td class="svcBody" style="width:70px;">  ${project.buildConfig.date}  </td>
    		
    		<td class="svcBody" style="width:70px;">  ${project.loadDate}  </td>
    		<td class="svcBody" style="width:100px;">  <a href="/bali/form/status/projects?name=${project.parentProject.name}"> ${project.parentProject.name} </a>  </td>
    		<td class="svcBody" style="width:30px;">   <a href="/bali/form/status/projects/reload?name=${project.name}" > <img src='/bali/data/images/replay.gif'> </a>
    		</td>
    	</tr>
	</c:forEach>		
	
</table>
