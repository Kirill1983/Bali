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
 


  var grid = {
		 "nodes":[
		           	   <c:forEach items="${nodes}" var="node">
		                {"nodeId":"${node.id}", "url":"${node.url}"},
		               </c:forEach>		
			   		   ]
  };

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
  
  function renderNodeResources(projects, nodeId){
	  
	  	var items = [];
		$.each(projects, function(i, project) {
				items.push('<BR><BR><BR>');
				items.push('<h2 align=center> '+project.name+' project </h2>');
				items.push('<h5 align=center> Node '+nodeId+'</h5>');
				items.push('<BR>');
				items.push('<div align=center> loaded: '+project.loadDate+', parent project: '+project.parentprojectname+', tests:'+ project.testcount);
				items.push('<BR><BR>');			
				
				$.each(project.storages, function(i, storage) {
					
					items.push('<h5 align=center> '+storage.resourceClassSimpleName+' Pool </h5>');
					items.push('<BR>');

					items.push('<div style="padding-left:40px;"> ');
					
					// remember value after reload
					var value = "";
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
		
			return items.join('');

  }
  
  function renderAllNodesResources(resourcesTree){
	  
	  	var items = [];
		$.each(resourcesTree.projects, function(i, project) {
				items.push('<BR><BR><BR>');
				items.push('<h2 align=center> '+project.name+' project </h2>');
				items.push('<h5 align=center> Nodes ...</h5>');
				items.push('<BR>');
				items.push('<div align=center> loaded: '+project.projectRef.loadDate+', parent project: '+project.projectRef.parentprojectname+', tests:'+ project.projectRef.testcount);
				items.push('<BR><BR>');			
				
				$.each(project.storages, function(i, storage) {
					
					items.push('<h5 align=center> '+storage.storage.resourceClassSimpleName+' Pool </h5>');
					items.push('<BR>');

					items.push('<div style="padding-left:40px;"> ');
					
					// remember value after reload
					var value = "";
					if (document.getElementById(project.name+storage.storage.resourceClassSimpleName) != null)
						value = document.getElementById(project.name+storage.storage.resourceClassSimpleName).value;
					
					
					items.push(' <input id='+project.name+storage.storage.resourceClassSimpleName+' value=\''+value+'\' size=35>');
					
					items.push(' <a href="" onclick="updateResourceStorage(\''+project.name+'\', \''+storage.storage.resourceClassName+'\', \'loadcmd\', \''+project.name+storage.resourceClassSimpleName+'\');"> add </a> &nbsp;');
					items.push(' <a href="" onclick="updateResourceStorage(\''+project.name+'\', \''+storage.storage.resourceClassName+'\', \'clearmatch\', \''+project.name+storage.resourceClassSimpleName+'\');"> clear </a> &nbsp;');
					items.push(' <a href="" onclick="updateResourceStorage(\''+project.name+'\', \''+storage.storage.resourceClassName+'\', \'init\', \''+project.name+storage.resourceClassSimpleName+'\');"> init </a> &nbsp;');
					items.push('<BR>');
					items.push('<BR>');
					items.push('<div style="padding-left:40px;">'+storage.storage.loadCmdHelp+'</div>');
					items.push('<div style="padding-left:40px;"> up/total count: .../...</div>');
					
					items.push('<TABLE style="padding-left:40px; padding-top:10px;">');
				items.push('<tr>');
				
				items.push('	<td class="svcHdr" style="width:15px;"> name </td>');	
				items.push('	<td class="svcHdr" style="width:15px;"> node </td>');
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
						
						items.push('<td class="svcBody" style="width:15px;"> '+resource.name+'</td>');     
						items.push('<td class="svcBody" style="width:15px;" > '+resource.nodeId+'</td>');
						items.push('<td class="svcBody" style="width:20px;"> '+resource.resstatus+'</td>');     
						items.push('<td class="svcBody" style="width:500px;"> '+resource.description+'</td>');
						
						items.push('<TD class="svcBody" style="width:60px;">' + resource.executorName + '</TD>');
						items.push('<td class="svcBody" style="width:20px;"> <a href="/baligridhub/form/status/suitecontext?resultDir='+resource.resultDir+'"> '+resource.resultDir+' </a> </td>');
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
		
			return items.join('');
	}
  
</script>


<body id="InfHtmlBody">

 <div class="clearBoth" style="padding-left:40px; padding-top:20px;">
   	   <input type="radio" checked onclick="refreshAllResourcesPage();" name="currentNode" value="0"> <b> All  &nbsp;</b>
       <c:forEach items="${nodes}" var="node">
         <input type="radio" onclick="refreshResourcesNodePage('${node.url}', '${node.id}');" name="currentNode" value="${node.id}">  <b> Node ${node.id} &nbsp</b>	     
	   </c:forEach>		
	</div>
	
<div id=resources>
</div>

<script>

var timeout;

function ResourceStorage(storage){
	this.name = storage.resourceClassName;
	this.storage = storage;
	this.resources = new Array();
	
	this.addResources = function(resources, nodeId){
		for (var i = 0; i < resources.length; i++){
			resources[i].nodeId = nodeId;
			this.resources.push(resources[i]);
		}
	};
}

function Project(project){
	this.name = project.name;
	this.projectRef = project;
	
	this.storages = new Array();
	
	this.addStorages = function(storages, nodeId){
		for (var i = 0; i < storages.length; i++)
			this.addStorage(storages[i], nodeId);
	};
	
	this.addStorage = function (storage, nodeId){
		store = this.findStorageByName(storage.resourceClassName);
		if (store != null){
			console.log("add resources from storage "+storage.resourceClassName)
			store.addResources(storage.resources, nodeId);
		} else {
			store = new ResourceStorage(storage);
			console.log("add resources from storage "+storage.resourceClassName)
			store.addResources(storage.resources, nodeId);
			this.storages.push(store);
		}
	};
	
	this.findStorageByName = function(name){
		for (var i = 0; i < this.storages.length; i++){
			if (this.storages[i].name == name)
				return this.storages[i];
		}
		return null;
	};
	
}

function ResourcesTree(){
	this.projects = new Array();
	
	this.findProjectByName = function(name){
		for (var i = 0; i < this.projects.length; i++){
			if (this.projects[i].name == name)
				return this.projects[i];
		}
		return null;
	};
	
	this.addProject = function(project){
		proj = this.findProjectByName(project.name);
		if (proj != null)
			proj.addStorages(project.storages, project.nodeId);
		else {
			proj = new Project(project);
			proj.addStorages(project.storages, project.nodeId);
			this.projects.push(proj);
		}
		
	};
	
	
	
}



function joinProjects(resourcesTree, projectsOnNode, nodeId){
	var joinedProjects;
	
	$("#resources").html('');
	projectsOnNode.forEach(function(projects, i, arr) {
		$("#resources").append("<BR> <BR> Node "+(i+1));
		$("#resources").append("<BR> projects size = "+projects.length);
		$("#resources").append("<BR>");
		projects.forEach(function(project, j, arr) {
			$("#resources").append("<BR> <BR> add project "+(j+1)+" = "+project.name+", node "+i);
			project.nodeId = (i+1);
			resourcesTree.addProject(project);
			//project.storages.forEach(function(storage, k, arr) {
			//	$("#resources").append("<BR> add storage "+(j+1)+" = "+storage.resourceClassSimpleName);
			//});
		});
		$("#resources").append("<BR>");
	});
}



function refreshAllResourcesPage(){	
	
	clearTimeout(timeout);
    projectsOnNode = [];
	var responses = 0;
	var resourcesTree = new ResourcesTree();
	
	grid.nodes.forEach(function(node, i, arr) {
		$.getJSON(node.url+'/form/status/resources/json', 
				  
				  function(projects) {
					console.log("i="+i);
					projectsOnNode[i] = projects;
			  		responses++;
			  		
			  		if (responses == grid.nodes.length){
			  			joinProjects(resourcesTree, projectsOnNode);
			  			$("#resources").html(renderAllNodesResources(resourcesTree));	
			  		}		
		
			  		
				 }
		  
		  	);
		
	});
	
	timeout = window.setTimeout("refreshAllResourcesPage();", 5000);
}	

function refreshResourcesNodePage(nodeUrl, nodeId){
  clearTimeout(timeout); 
  $.getJSON(nodeUrl+'/form/status/resources/json', 		  
		 function(projects) {
 			$("#resources").html(renderNodeResources(projects, nodeId)); 	
		 }
  );
  timeout = window.setTimeout("refreshResourcesNodePage('"+nodeUrl+"','"+nodeId+"');", 5000);
}

refreshAllResourcesPage();

</script>
