<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>


<html>
<head>
<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
<link type="text/css" href="/bali/data/css/Default.css" rel="StyleSheet">
<script src="/bali/data/tree/jquery.js" type="text/javascript"></script>

<script type="text/javascript">
 

  function updateResourceStorage(projectname, resourceclassname, property, inputname) {
	 

	  //alert("/bali/form/status/resources/update?"+property+"="+document.getElementById(inputname).value+"&projectname="+projectname+"&resourceclassname="+resourceclassname);
	  
	  $.get("/bali/form/status/resources/update?"+property+"="+document.getElementById(inputname).value+"&projectname="+projectname+"&resourceclassname="+resourceclassname,
			  function(data) {
		  		refreshPage();
		  		//window.setTimeout("refreshPage();", 1000);
		  		//alert("OK /bali/form/status/update?"+property+"="+checked+"&className="+test+"&resultDir=${suiteExecContext.resultDir}");		  		 
		  		
			  }
	  		);
	  
  }
  
  function updateSuiteExecContext(test, property, checked) {
	 
	  $.get("/bali/form/status/update?"+property+"="+checked+"&className="+test+"&resultDir=${suiteExecContext.resultDir}",
			  function(data) {
		  		refreshPage();
		  		//window.setTimeout("refreshPage();", 1000);
		  		//alert("OK /bali/form/status/update?"+property+"="+checked+"&className="+test+"&resultDir=${suiteExecContext.resultDir}");		  		 
		  		
			  }
	  		);
	 
  }

  function ajax(req) {
	  $.get(req,
			  function(data) {
		  		refreshPage();
			  }
	  		);		 
  }
  
</script>


<body id="InfHtmlBody">

<div id=resources>
</div>

<script>

function refreshPage(){
	

  $.getJSON('/bali/form/status/resources/json', 
		  
		 function(projects) {
	  
 			var items = []; 			
 
 		
 			$.each(projects, function(i, project) {
 				
 				items.push('<BR><BR><BR>');
 				items.push('<h2 align=center> '+project.name+' project </h2>');
 				items.push('<BR>');
 				items.push('<div align=center> loaded: '+project.loadDate+', parent project: '+project.parentprojectname+', tests:'+ project.testcount);
 				items.push('<BR><BR>');			
 				
 				$.each(project.storages, function(i, storage) {
 					
 					items.push('<h5 align=center> '+storage.resourceClassSimpleName+' Pool </h5>');
 					items.push('<BR>');

 					items.push('<div style="padding-left:40px;"> ');
 					
 					// remember value after reload
 					value = "";
 					if (document.getElementById(project.name+storage.resourceClassSimpleName) != null)
 						value = document.getElementById(project.name+storage.resourceClassSimpleName).value;
 					
 					
 					items.push(' <input id='+project.name+storage.resourceClassSimpleName+' value=\''+value+'\' size=35>');
 					
 					items.push(' <a href="" onclick="updateResourceStorage(\''+project.name+'\', \''+storage.resourceClassName+'\', \'loadcmd\', \''+project.name+storage.resourceClassSimpleName+'\');"> add </a> &nbsp;');
 					items.push(' <a href="" onclick="updateResourceStorage(\''+project.name+'\', \''+storage.resourceClassName+'\', \'clearmatch\', \''+project.name+storage.resourceClassSimpleName+'\');"> clear </a> &nbsp;');
 					items.push(' <a href="" onclick="updateResourceStorage(\''+project.name+'\', \''+storage.resourceClassName+'\', \'init\', \''+project.name+storage.resourceClassSimpleName+'\');"> init </a> &nbsp;');
 					items.push('<BR>');
 					items.push('<BR>');
 					items.push('<div style="padding-left:40px;">'+storage.loadCmdHelp+'</div>');
 					items.push('<div style="padding-left:40px;"> up/total count: '+storage.upCount+'/'+storage.totalCount+'</div>');
 						
 					items.push('<TABLE style="padding-left:40px; padding-top:10px;">');
					items.push('<tr>');
					items.push('	<td class="svcHdr" style="width:20px;"> type </td>');
					items.push('	<td class="svcHdr" style="width:15px;"> name </td>');	
					items.push('	<td class="svcHdr" style="width:20px;"> status </td>');	
					items.push('	<td class="svcHdr" style="width:500px;"> description </td>	');
							
					items.push('	<td class="svcHdr" style="width:60px;"> Executor </td>');
					items.push('	<td class="svcHdr" style="width:60px;"> Suite Context </td>');
					items.push('	<td class="svcHdr" style="width:15px;"> Grid </td>');
					items.push('	<td class="svcHdr" style="width:80px;"> Test Context</td>');
							
					items.push('	<td class="svcHdr" style="width:10px;"> errs </td>');
					items.push('	<td class="svcHdr" style="width:30px;"> status </td> ');
					items.push('	<td class="svcHdr" style="width:60px;"> start time </td>');
					items.push('	<td class="svcHdr" style="width:60px;"> end time </td>');
					items.push('	<td class="svcHdr" style="width:15px;" > elaps (min) </td>');
					items.push('    </tr>');

 					$.each(storage.resources, function(i, resource) {
 						items.push('<tr>');
 						items.push('<td class="svcBody" style="width:20px;" > '+resource.type+'</td>');
 						items.push('<td class="svcBody" style="width:15px;"> '+resource.name+'</td>');     
 						items.push('<td class="svcBody" style="width:20px;"> '+resource.resstatus+'</td>');     
 						items.push('<td class="svcBody" style="width:500px;"> '+resource.description+'</td>');
 						
 						items.push('<TD class="svcBody" style="width:60px;">' + resource.executorName + '</TD>');
 						items.push('<td class="svcBody" style="width:20px;"> <a href="/bali/form/status/suitecontext?resultDir='+resource.resultDir+'"> '+resource.resultDir+' </a> </td>');
 						items.push('<td class="svcBody" style="width:20px;"> '+resource.options+' </td>');
 						items.push('<td class="svcBody" style="width:140px;">'+resource.shortClassName+'</td>');
 						items.push('<TD class="svcBody" style="width:20px;">' + resource.errorCount + '</TD>');
 						
 						status = resource.status;
 						if (resource.status == 'fail')
 		    				status = '<i>'+status+'</i>';
 		    				
 		    			if (resource.status == 'success')
 		    				status = '<b>'+status+'</b>';
 		    				
 		    			items.push('<TD class="svcBody" style="width:30px;">' + status + '</TD>'); 					    
 		    			items.push('<TD class="svcBody" style="width:100px;">' + resource.startDate + '</TD>');
 		    			items.push('<TD class="svcBody" style="width:100px;">' + resource.endDate + '</TD>');
 		    			items.push('<TD class="svcBody" style="width:20px;">' + resource.elapsed + '</TD>');	
 		    			items.push('</tr>'); 			
 					});
 					items.push('</TABLE>');
 				
 				});
  			});  
  			
 			$("#resources").html(items.join('')); 	
 			
 			
 			window.setTimeout("refreshPage();", 10000);
 			
 			
		 }
  	);

 }
 refreshPage();
 

</script>
