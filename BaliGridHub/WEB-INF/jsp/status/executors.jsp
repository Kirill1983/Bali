<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
  
<form action="/bali/form/status/executors/update" name="RunForm">
 
<table style="padding-left:40px; padding-top:30px;">
<tr><td class="svcHdr" style="width: 140px;">	Threads count: &nbsp;&nbsp;<input name=newThreadsCount size=6 value=${threadsCount} /> </td>  
     <td class="svcHdr" style="width: 50px;">
	 
		<table cellspacing="0" cellpadding="0" id="paneBtns" class="InfButton">
			<tr>
			<td onclick="document.RunForm.submit();" onmouseout="pnBtn.setClass(this,'btn')" onmouseover="pnBtn.setClass(this,'hoverbtn')" title="Update" id="viewBtn" class="btn"><span>Update</span></td>
			</tr>
		</table>		
	</td>
	<td> &nbsp; <font color=red> ${msg} </font> </td>
	
</tr>
</table>

</form>
  
<TABLE style="padding-left:0px; padding-top:0px;" WIDTH="90%">
<tr>
	<td>
		<iframe src='/bali/form/status/executors/refresh' width="100%" height=700 frameborder=0 > </iframe>
	</td>
</tr>  
</TABLE>





