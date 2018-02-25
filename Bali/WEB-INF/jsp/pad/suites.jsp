<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<script type="text/javascript">

  function Save() {
  
    if (document.getElementById("testsValues").value != ""){
  	    document.getElementById("saveErrorMsg").innerHTML = "";
        var form = document.ScheduleForm;
   	    form.action = "/bali/form/pad/suites/save";
        form.submit();
     } else
        document.getElementById("saveErrorMsg").innerHTML = " <font color=red> please, select tests in tree </font>";
    
  }
  
  function Delete() {
	  var form = document.ScheduleForm;
   	  form.action = "/bali/form/pad/suites/delete";
      form.submit();
  }
  
  function Run() {
	  
    if (document.getElementById("testsValues").value != ""){
  	    document.getElementById("saveErrorMsg").innerHTML = "";
    	var form = document.ScheduleForm;
   	    form.action = "/bali/form/pad/run";
        form.submit();
     } else
        document.getElementById("saveErrorMsg").innerHTML = " <font color=red> please, select tests in tree </font>";
  
  }
  
  function clickLoad(id) {

		if (document.getElementById('loadmode').checked == true){
			document.getElementById('loadmins').style.visibility="visible";
			document.getElementById('rumpup').style.visibility="visible";
			
		}
		else{
			document.getElementById('loadmins').style.visibility="hidden";
			document.getElementById('rumpup').style.visibility="hidden";
		}
		
		tags=document.body.getElementsByTagName('a'); 

		for(var j=0; j<tags.length; j++)
			if ((tags[j].id != '') && (tags[j].name == id)) 
				if (document.getElementById('loadmode').checked == true)
					document.getElementById(tags[j].id+'.threads').type='text';
				else
					document.getElementById(tags[j].id+'.threads').type='hidden';				

		return false;	
	}
		
</script>
      	
<form:form action="/bali/form/pad/suites" name="ScheduleForm" method="POST" modelAttribute="suiteMdl">

	<table  style="padding-left:40px; padding-top:40px;">
      <tr>
        <td class="svcHdr">Select schedule:</td>
      </tr>
      <tr>
      	<td class="svcBody"> 
      		<div style="float: left;">
      	 	<form:select path="filename" id="nameid" onchange="ScheduleForm.submit();" items="${schedulesList}"/>
      	 	</div>
      	 	<div> <font color=red> &nbsp; ${msg} </font> </div>
		</td>
      </tr>
     </table>

    <table  style="padding-left:40px; padding-top:0px;">
      
      <tr>
        <td class="svcHdr"  colspan="2" >Schedule Details &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/bali/form/pad/linkrun?filename=${suiteMdl.filename}">kick-off link</a></td>
      </tr>
	  <tr>
		<td class="svcBody">
			<table>
				<tr>
					<td> Name: </td> <td> <form:input path="name"/></td>
				</tr>
				<tr>
					<td> Description: </td> <td> <form:input  path="description"/> </td>
				</tr>
				<tr>
					<td> Emails: </td> <td> <form:input  path="email"/> </td>
				</tr>	
				<tr>
					<td> CRON: </td> <td> <form:input  path="cron" /> </td>
				</tr>
				<tr>
					<td> Browser: </td>	<td> <form:select path="browser" items="${browserList}" /></td>
				</tr>
				<tr>	
					<td> Options: </td>	<td> <form:select path="options" items="${optionsList}"/></td>
				</tr>
				
				<tr> <td colspan=2>&nbsp; </td></tr>
				<tr>
					<td colspan=2> 
						<form:checkbox id="loadmode" path="loadmode" onclick="clickLoad('testsValues');"/>    load/stress mode &nbsp;
						<form:checkbox path="capturemode" />  capture slide show &nbsp;
						<form:checkbox path="debugmode" />   stop on error 	
						<div style="visibility:hidden;" id=loadmins > load scenario duration: <form:input path="loadminutes" size="3"/> minutes 
						 rump up: <form:input path="rumpup" size="4"/> ms </div>
					</td>
				</tr>
					
			</table>
		</td>			
	  </tr>
	  
	</table>
			

<table style="padding-left:40px; padding-top:0px;">
<tr><td class="svcHdr">	Test list:</td></tr>

<tr>
	<td class="svcBody"> <pn:projectstree projects="${projects}" suiteMdl="${suiteMdl}" treeId="tests"/></td>
</tr>
<tr><td class="svcHdr">	Action:</td></tr>
<tr>
	<td td class="svcBody">
		<table cellspacing="0" cellpadding="0" id="paneBtns" class="InfButton">
			<tr><td onclick="Save()" onmouseout="pnBtn.setClass(this,'btn')" onmouseover="pnBtn.setClass(this,'hoverbtn')" title="Save" id="saveBtn" class="btn"><span>Save</span></td>
			    <td>&nbsp;&nbsp;</td>
			    <td onclick="Delete()" onmouseout="pnBtn.setClass(this,'btn')" onmouseover="pnBtn.setClass(this,'hoverbtn')" title="Delete" id="delBtn" class="btn"><span>Delete</span></td>
			    <td>&nbsp;&nbsp;</td>
			    <td onclick="Run()" onmouseout="pnBtn.setClass(this,'btn')" onmouseover="pnBtn.setClass(this,'hoverbtn')" title="Run" id="runBtn" class="btn"><span>Run</span></td>
			    <td>&nbsp;&nbsp;</td>
			    <td ><div id=saveErrorMsg></div></td>
			</tr>
		</table>
	</td>
</tr>

<script type="text/javascript">
	clickLoad('testsValues');
</script>
</table>
<input type=hidden name="method" value="save">
</form:form>