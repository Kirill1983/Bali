<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
<script src="/bali/data/tree/jquery.js" type="text/javascript"></script>

<script type="text/javascript">

 var grid = {
		 "nodes":[
		           	   <c:forEach items="${nodes}" var="node">
		                {"nodeId":"${node.id}", "url":"${node.url}" },
		               </c:forEach>		
			   		   ]
 };
 
 function updateSuiteExecContext(test, property, checked, threadId, nodeUrl, resultDir) {
	  $.get(nodeUrl+"/form/status/update?"+property+"="+checked+"&className="+test+"&threadId="+threadId+"&resultDir="+resultDir, function(data) {});
 }
 
 </script>
 	
 
 	<table  style="padding-left:40px; padding-top:20px; boarder=1px;" >
 		<tr >
 		<td  class="svcHdr" style="width:30px;"> Threads </td><td class="svcHdr" style="width:40px; "> <input type="radio" checked onclick="refreshAllNodeThreads();" name="currentNode" value="0"> <b> All  &nbsp;</b> </td>  
	    <c:forEach items="${nodes}" var="node">
   			<td class="svcHdr" style="width:40px;"> <input type="radio" onclick="refreshNodeThreads('${node.url}', '${node.id}');" name="currentNode" value="${node.id}">  <b> ${node.id} &nbsp</b> </td>	     
	 	</c:forEach>
	 	</tr>
	 	
	 	<tr id=threadsCount>
	 	<td class="svcAll" style="width:40px; "> total </td>
 		<td class="svcAll" style="width:30px;"> 0	 </td>  
	    <c:forEach items="${nodes}" var="node">
   		<td class="svcAll" style="width:30px;"> 0</td>
	 	</c:forEach>
	 	</tr>
	 	
	 	 <tr id=threadsCountBusy>
	 	 <td class="svcAll" style="width:40px;"> busy </td>
 		<td class="svcAll" style="width:30px;"> 0 </td>  
	    <c:forEach items="${nodes}" var="node">
   		<td class="svcAll" style="width:30px;"> 0  </td>
	 	</c:forEach>
	 	</tr>
</table>	
	
<div id=executors> 
</div>

<script>

var timeout;

function renderExecutorsTableHeader(id){
	var src = [];
     
    src.push('<TR>'); 
	src.push('<td class="svcHdr" style="width:60px;"> Node '+id+' </td>');
	src.push('<td class="svcHdr" style="width:50px;"> Status </td>');
	src.push('<td class="svcHdr" style="width:60px;"> Suite Context </td>');
	src.push('<td class="svcHdr" style="width:15px;"> Options </td>');
	src.push('<td class="svcHdr" style="width:80px;"> Test Context </td>');
	src.push('<td class="svcHdr" style="width:10px;"> errs </td>');
	src.push('<td class="svcHdr" style="width:30px;"> status </td>');
	src.push('<td class="svcHdr" style="width:60px;"> start time </td>');
	src.push('<td class="svcHdr" style="width:60px;"> end time </td>');
	src.push('<td class="svcHdr" style="width:15px;" > elaps (min) </td>');
	src.push('<td class="svcHdr" style="width:10px;"> Stop </td>');
	src.push('<td class="svcHdr" style="width:10px;"> wait On Error </td>');
	src.push('<td class="svcHdr" style="width:10px;"> capture screen </td>');
	src.push('<td class="svcHdr" style="width:100px;"> Use Resources </td>');
	src.push('</TR>');
	return src.join('');	
}



function renderExecutorsTable(response, nodeUrl){
		
	var items = [];
	
	$.each(response, function(i, testExecContext) {
	
	items.push('<TR>');
	items.push('<td class="svcBody" style="width:60px;">'+(i+1)+'</TD>');
	items.push('<td class="svcBody" style="width:20px;">'+testExecContext.executorState+'</TD>');
	items.push('<td class="svcBody" style="width:20px;">'+testExecContext.resultDir+'</TD>');
	items.push('<td class="svcBody" style="width:20px;"> - </TD>'); // options
	items.push('<td class="svcBody" style="width:20px;">'+testExecContext.lineViewLabel+'</TD>');
	items.push('<td class="svcBody" style="width:20px;">'+testExecContext.errorCount+'</TD>');
	items.push('<td class="svcBody" style="width:30px;">'+testExecContext.status+'</TD>');
	
	items.push('<td class="svcBody" style="width:60px;">'+testExecContext.startDate+'</TD>');
	items.push('<td class="svcBody" style="width:60px;">'+testExecContext.endDate+'</TD>');
	items.push('<td class="svcBody" style="width:20px;">'+testExecContext.elapsed+'</TD>');
	
	var disabled = '';

	if (testExecContext.resultDir == "" || testExecContext.completed == true)
		disabled = ' disabled=true ';
		
	var debugChecked = '';
	if (testExecContext.debugMode == true)
		debugChecked = ' checked ';
		
	var needStopChecked = '';
	if (testExecContext.needStop == true)
		needStopChecked = ' checked ';

	var captureChecked = '';
	if (testExecContext.captureMode == true)
		captureChecked = ' checked ';
	
	var needStop = '<input type="checkbox" '+disabled+' name="needStop" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+testExecContext.resultDir+'\');" '+needStopChecked+' ></input>';
	var debugMode   = '<input type="checkbox" '+disabled+' name="debugMode" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+testExecContext.resultDir+'\');" '+debugChecked+' ></input>';
	var captureMode = '<input type="checkbox" '+disabled+' name="captureMode" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+testExecContext.resultDir+'\');" '+captureChecked+' ></input>';
	
	items.push('<TD class="svcBody" style="width:10px;">' + needStop + '</TD>');
	items.push('<TD class="svcBody" style="width:10px;">' + debugMode + '</TD>');
	items.push('<TD class="svcBody" style="width:10px;">' + captureMode + '</TD>');
	items.push('<TD class="svcBody" style="width:10s0px;">' + testExecContext.resourcesNames + '</TD>');
	
	items.push('</TR>');
	});
	
	return items.join('');
	
}	


function refreshThreadsInfoTable(){
	
	var total = 0, busy = 0, responses = 0;
	grid.nodes.forEach(function(node, i, arr) {
		$.getJSON(node.url+'/form/status/threads/shortjson', 
				  
				  function(execInfo) {
			  		$("tr[id=threadsCount] > td:nth-child("+(i+3)+")").html(execInfo.total);
			  		$("tr[id=threadsCountBusy] > td:nth-child("+(i+3)+")").html(execInfo.busy);
			  		
			  		total += execInfo.total;
			  		busy += execInfo.busy;
			  		responses++;
			  		
			  		if (responses == arr.length){ // on last count update sum
			  			$("tr[id=threadsCount] > td:nth-child(2)").html(total);
			  			$("tr[id=threadsCountBusy] > td:nth-child(2)").html(busy);
			  		}	
			  		
				 }
		  
		  	);
		
	});

}
  

 
function refreshNodeThreads(nodeUrl,nodeId){

	  $.getJSON(nodeUrl+'form/status/threads/json', 
			  
			  function(response) {
		 		var src = [];
		  		src.push('<TABLE style="padding-left:40px; padding-top:20px;">');
		  		src.push(renderExecutorsTableHeader(nodeId));
		  		src.push(renderExecutorsTable(response, node.url));
		  		src.push('<TABLE>');
				$("#executors").html(src.join(''));
			 }
	  
	  	);
		
	  clearTimeout(timeout); 
	  timeout = window.setTimeout("refreshNodeThreads('"+nodeUrl+"','"+nodeId+"');", 5000);
	  
}

function refreshAllNodeThreads(){
	
	clearTimeout(timeout);
	var src = [];
	var responses = 0;

	
	grid.nodes.forEach(function(node, i, arr) {
		$.getJSON(node.url+'/form/status/threads/json', 
				  
				  function(response) {
			  		var req = [];
			  		req.push('<TABLE style="padding-left:40px; padding-top:20px;">');
			  		req.push(renderExecutorsTableHeader(i));
			  		req.push(renderExecutorsTable(response, node.url));
			  		req.push('<TABLE>');
			  		src[i] = req.join('');
			  		responses++;
			  		if (responses == grid.nodes.length)
			  			$("#executors").html(src.join(''));
			  	 }
		  
		  	);
		
	});
	
	timeout = window.setTimeout("refreshAllNodeThreads();", 5000);

}

function refreshAll(){
	
	refreshThreadsInfoTable();
	
	window.setTimeout("refreshAll();", 5000);
}

refreshAll();
refreshAllNodeThreads();

</script>
 


