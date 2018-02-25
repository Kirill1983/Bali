<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<script src="/bali/data/js/cookies.js" type="text/javascript"></script>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
<link type="text/css" href="/bali/data/css/Default.css" rel="StyleSheet">


<script type="text/javascript">

  function Run() {
  	  if (document.getElementById("testsValues").value != ""){
  	    document.getElementById("runErrorMsg").innerHTML = "";
      	document.RunForm.submit();
      } else
        document.getElementById("runErrorMsg").innerHTML = " <font color=red> please, select tests in tree </font>";
  }

 
  function saveEmail(obj){
	  SetCookie(obj.id, obj.value, expiryLong);
  } 
  
  function loadEmail(){
	  if (document.getElementById('email'))
	  	email.value = GetCookie('email');
  } 
  
  function clickLoad(id) {
	  	//alert(document.getElementById('loadmins').style.visibility);
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
  
<form:form action="/baligridhub/form/pad/run" target="_blank" method="post" name="RunForm" modelAttribute="suiteMdl">
<table style="padding-left:40px; padding-top:40px;">

<tr><td class="svcHdr">	Target options:</td></tr>
<tr>
	<td class="svcBody">
		<form:select path="options" items="${optionsList}" />		
    </td>
</tr>

<tr><td class="svcHdr">	Browser:</td></tr>
<tr>
	<td class="svcBody">
		<form:select path="browser" items="${browserList}" />
	</td>
</tr>

<tr><td class="svcHdr">	common options:</td></tr>
<tr>
	<td class="svcBody">
		<form:checkbox id="loadmode" path="loadmode" onclick="clickLoad('testsValues');" />    load/stress mode &nbsp;
		<form:checkbox path="capturemode" />  capture slide show &nbsp;
		<form:checkbox path="debugmode" />   stop on error 	<BR>		
		<div style="visibility:hidden;" id=loadmins > load scenario duration: <form:input path="loadminutes" size="3"/> minutes &nbsp;&nbsp;
		rump up: <form:input path="rumpup" size="4"/> ms </div>
	
	</td>
</tr>

<tr><td class="svcHdr">	Test list: </td></tr>

<tr>
	<td class="svcBody"> <pn:projectstree projects="${projects}" suiteMdl="${suiteMdl}" treeId="tests"/></td>
</tr>

<tr><td class="svcHdr">	Action:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;email result: <form:input path="email" onkeyup="saveEmail(this);"/> </td></tr>
<tr>
	<td td class="svcBody">
		<table cellspacing="0" cellpadding="0" id="paneBtns" class="InfButton">
			<tr>
			 <td onclick="Run()" onmouseout="pnBtn.setClass(this,'btn')" onmouseover="pnBtn.setClass(this,'hoverbtn')" title="Edit" id="runBtn" class="btn"><span>Run</span></td>
			    <td> &nbsp;&nbsp; </td>
			 <td ><div id=runErrorMsg></div></td>
			</tr>
		</table>
	</td>
</tr>

<script type="text/javascript">
	loadEmail();
</script>
</table>

</form:form>

