<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>


<html>
<head>
<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
<link type="text/css" href="/bali/data/css/Default.css" rel="StyleSheet">

<script src="/bali/data/tree/jquery.js" type="text/javascript"></script>
<script src="/bali/data/tree/jquery.tree.js" type="text/javascript"></script>

<script type="text/javascript">
 var grid = {
		 "nodeSuites":[{"nodeId":"1", "url":"/bali/", "resultDir":"${suiteExecContext.resultDir}"}]};
 
 
 function addTime(mins, nodeUrl, resultDir) {
	  $.get(nodeUrl+"/form/status/suitecontext/addloadtime?resultDir="+resultDir+"&time="+mins, function(data) {});
 }

 function addThreads(count, nodeUrl, resultDir) {
	  $.get(nodeUrl+"/form/status/suitecontext/addthreads?resultDir="+resultDir+"&threads="+count, function(data) {});
 }

 function invokeExporter(metaData, nodeUrl, resultDir) {	 
	  $.get(nodeUrl+"/form/status/exportresults?resultDir="+resultDir+"&metaData="+escape(metaData), function(data) {});
 }

 function invokeExporterForTest(test, metaData, nodeUrl, resultDir) {
	  $.get(nodeUrl+"/form/status/exportresults?resultDir="+resultDir+"&className="+test+"&metaData="+escape(metaData), function(data) {});
 }

 function updateSuiteExecContext(test, property, checked, threadId, nodeUrl, resultDir) {
	  $.get(nodeUrl+"/form/status/update?"+property+"="+checked+"&className="+test+"&threadId="+threadId+"&resultDir="+resultDir, function(data) {});
 }

 function previewTreeLog(event, test, threadId, nodeUrl, resultDir) {
	  
	  var treelog = document.getElementById("treelog");

	  if (treelog.style.visibility == 'visible'){
		  treelog.style.visibility = 'hidden';
		  return;
	  }  
	  
	  $.get(nodeUrl+"/form/status/treeloghtml?resultDir="+resultDir+"&className="+test+"&threadId="+threadId,
			  function(data) {
		  		$("#treelog").html(data);
	
		  		treelog.style.left = event.clientX;
		  		treelog.style.top  = event.clientY;
		  		treelog.style.visibility = 'visible'; 

		  		refreshPage();
		  		
			  }
	  		);
	  		
 }
 
 function ajax(req) { 
	  $.get(req, function(data) {});		 
 }
 
 function clickDoc(event){
	  var treelog = document.getElementById("treelog");

	  if (treelog.style.visibility == 'visible'){
		  treelog.style.visibility = 'hidden';
	  }  
	 
 }
</script>


<body id="InfHtmlBody" onclick="clickDoc(event);">


  	
<div id=suitecontext onclick="clickDoc(event);"> </div>
<div id=treelog onclick="this.style.visibility = hidden; return true;" style="width: auto; height: auto; visibility: hidden; background: #E0E0FF; position: absolute; border: solid 1px black; "></div>
	


<script>

function renderNodeSuiteContextHeader(suiteExecContext, resultDir, nodeUrl){
		var items = []; 			
	 	
		items.push('<TABLE style="padding-left:40px; padding-top:20px;">');
		items.push('<tr>');
		items.push('	<td class="svcAll" style="width: 400px;"> ');
		items.push('		<a target="_blank" href="'+nodeUrl+'results/'+resultDir+'/classifyNG.html"> classify report </a> &nbsp&nbsp');
		items.push('		<a target="_blank" href="'+nodeUrl+'results/'+resultDir+'/captureclassifyNG.html"> screen capture report </a> &nbsp');
	    items.push('		<a href="'+nodeUrl+'form/status/perfomancereport?resultDir='+resultDir+'"> metrics report </a> &nbsp');
		items.push('	</td>  ');  
		
		if (suiteExecContext.completed == false){
			items.push('<td class="svcAll" style="width: 100px;"> <a href="#" onclick="ajax(\''+nodeUrl+'/form/status/cancelcontext?resultDir='+resultDir+'\'); return true;" > call threads stop </a> </td>');
			items.push('<td class="svcAll" style="width: 80px;"> <a href="#" onclick="ajax(\''+nodeUrl+'/form/status/softstopcontext?resultDir='+resultDir+'\'); return true;" > soft stop </a> </td>');
		}

		items.push('<td class="svcAll" style="width: 80px;"> <a href="'+nodeUrl+'/form/status/suitecontextemail?resultDir='+resultDir+'" target=blank > email report </a> </td>');
				
		items.push('</tr>');
		items.push('</TABLE>');
		
		items.push('<BR> <TABLE style="padding-left:40px;">');
		items.push('<TR>');
		
		items.push('<TD class="svcAll">');
		items.push('<input id=metadatabox type=text value=\"'+suiteExecContext.exporterMetaData+'\" size=70>');
		items.push('</TD>');
		
		items.push('<TD class="svcAll" style="width: 100px;">');
		items.push('<a href="#" onclick="invokeExporter(document.getElementById(\'metadatabox\').value, \''+nodeUrl+'\',\''+resultDir+'\');" > export results </a> &nbsp;&nbsp;');
		items.push('</TD>');
		
		items.push('<TD class="svcAll" style="width: 120px;">');
		items.push('<a href=\'#\' title="'+suiteExecContext.exportResultTooltip+'" > ' + suiteExecContext.exportResultText + ' </a>');
		items.push('</TD>');
		
		items.push('</TR>');
		items.push('</TABLE>');
		
		
		items.push('<TABLE style="padding-left:40px; padding-top:20px;">');
		items.push('<TR>');
		items.push('<TD>');
		items.push('Start time: '+suiteExecContext.startDate+
				   ', End time:'+suiteExecContext.endDate+
				   ', Suite elapsed:'+ suiteExecContext.elapsed+' mins, '+
			   'Summary per each test elapsed: '+suiteExecContext.summaryElapsed+' mins, '+
			   'accelerate rate: '+ suiteExecContext.timeAccelerateRate);
		items.push('</TD>');
		items.push('</TR>');
		items.push('<TR></TR>');
		items.push('<TR>');
		items.push('<TD>');
		items.push('Scenarios done: '+suiteExecContext.doneCount+
				   ', passed:'+suiteExecContext.passedCount+
				   ', failed: '+ suiteExecContext.failedCount+
				   ', total/unique errors: '+ suiteExecContext.errorCount+'/'+ suiteExecContext.uniqErrorCount);
				   
		if (suiteExecContext.loadMode == true)
			items.push(', load time duration: '+suiteExecContext.loadMinutes+' mins');
		
		
		
		items.push('</TD>');
		items.push('</TR>');
		
		if (suiteExecContext.loadMode == true){
			items.push('<TR> <td> <A href="#" onclick="addThreads(1, \''+nodeUrl+'\',\''+resultDir+'\');"> add threads 1 </A> &nbsp; <A href="#" onclick="addThreads(5,\''+nodeUrl+'\',\''+resultDir+'\');"> 5 </A> &nbsp; <A href="#" onclick="addThreads(10,\''+nodeUrl+'\',\''+resultDir+'\');"> 10 </A> &nbsp; <A href="#" onclick="addThreads(20,\''+nodeUrl+'\',\''+resultDir+'\');"> 20 </A> &nbsp; <A href="#" onclick="addThreads(50,\''+nodeUrl+'\',\''+resultDir+'\');"> 50 </A> </td> </TR>');
			items.push('<TR> <td> <A href="#" onclick="addTime(1,\''+nodeUrl+'\',\''+resultDir+'\');"> add time 1 </A> &nbsp; <A href="#" onclick="addTime(5,\''+nodeUrl+'\',\''+resultDir+'\');"> 5 </A> &nbsp; <A href="#" onclick="addTime(,\''+nodeUrl+'\',\''+resultDir+'\');"> 10 </A> &nbsp; <A href="#" onclick="addTime(20,\''+nodeUrl+'\',\''+resultDir+'\');"> 20 </A> &nbsp; <A href="#" onclick="addTime(50,\''+nodeUrl+'\',\''+resultDir+'\');"> 50 </A> </td> </TR>');
		
		}

		items.push('</TABLE>');

		return items.join('');
	}	

	function renderSuiteTableHeader(suiteExecContext, nodeId, nodeUrl){
		var src = [];
	     
	    src.push('<TR>'); 
	    src.push('<td class="svcHdr">  Execution List   (node '+nodeId+', <a href=\''+nodeUrl+'/form/status/targetgrid?options='+suiteExecContext.options+'\' target=\'_blank\' > '+suiteExecContext.options+' </a> ) </td>');
	    
	    if (suiteExecContext.loadMode == true){
			src.push('<td class="svcHdr" style="width:20px;"> loops </td>');
		}
	    src.push('<td class="svcHdr" style="width:20px;"> errors </td>');
		src.push('<td class="svcHdr" style="width:30px;"> status </td> ');
		src.push('<td class="svcHdr" style="width:10px;"> Cmd </td>');
		src.push('<td class="svcHdr" style="width:100px;"> start time </td>');
		src.push('<td class="svcHdr" style="width:100px;"> end time </td>');
		src.push('<td class="svcHdr" style="width:20px;" > elapsed (min) </td>');
		src.push('<td class="svcHdr" style="width:10px;"> Need Stop </td>');
		src.push('<td class="svcHdr" style="width:10px;"> wait On Error </td>');
		src.push('<td class="svcHdr" style="width:10px;"> capture screen </td>');
		src.push('<td class="svcHdr" style="width:120px;"> Use Resources </td>');
		
		src.push('<td class="svcHdr" style="width:20px;"> Actions </td>');
		src.push('<td class="svcHdr" style="width:60px;"> State </td>');
		src.push('<td class="svcHdr" style="width:100px;"> Export </td>');
		src.push('</TR>');
		return src.join('');	
	}

		
	function renderSuiteTable(suiteExecContext, nodeUrl, resultDir){
		 		
			var items = [];
			
			$.each(suiteExecContext.testExecContexts, function(i, testExecContext) {
			
			items.push('<TR>');
		
			items.push('<TD class="svcBody"> <A href="#" onclick="previewTreeLog(event,\''+testExecContext.className+'\',\''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+resultDir+'\');" >' +(i+1)+'. '+testExecContext.lineViewLabel + ' </a> </TD>');
			if (suiteExecContext.loadMode == true){
				items.push('<TD class="svcBody">' + testExecContext.loops + '</TD>');
			}
				
			items.push('<TD class="svcBody" >' + testExecContext.errorCount + '</TD>');
			status = testExecContext.status;
			
			if (testExecContext.status == 'fail')
				status = '<font color=red> <i>'+status+'</i> </font>';
				
			if (testExecContext.status == 'success')
				status = '<font color=green> <b>'+status+'</b> </font>';
				
			items.push('<TD class="svcBody" style="width:30px;">' + status + '</TD>');
			
			cmd = '';
			if (testExecContext.completed == true)
				cmd =  '<img src=\'/baligridhub/data/images/replay.gif\' onclick="updateSuiteExecContext(\''+testExecContext.className+'\',\'suiteExecutorProcCmd\',\'replay\', \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+resultDir+'\');" >';
			
			if (testExecContext.status == 'in progress' || testExecContext.status == 'initializing')
				cmd =  '<img src=\'/baligridhub/data/images/interrupt.gif\' onclick="updateSuiteExecContext(\''+testExecContext.className+'\',\'suiteExecutorProcCmd\',\'interrupt\', \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+resultDir+'\');" >';
		
			items.push('<TD class="svcBody" style="width:10px;"> '+cmd +' </TD>');
			
			items.push('<TD class="svcBody" style="width:100px;">' + testExecContext.startDate + '</TD>');
			items.push('<TD class="svcBody" style="width:100px;">' + testExecContext.endDate + '</TD>');
			items.push('<TD class="svcBody" style="width:20px;">' + testExecContext.elapsed + '</TD>');
			
			disabled = '';
			if (testExecContext.completed == true)
				disabled = ' disabled=true ';
				
			debugChecked = '';
			if (testExecContext.debugMode == true)
				debugChecked = ' checked ';
				
			needStopChecked = '';
			if (testExecContext.needStop == true)
				needStopChecked = ' checked ';

			captureChecked = '';
			if (testExecContext.captureMode == true)
				captureChecked = ' checked ';
			
			
			needStop = '<input type="checkbox" '+disabled+' name="needStop" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+resultDir+'\');" '+needStopChecked+' ></input>';
			debugMode = '<input type="checkbox" '+disabled+' name="debugMode" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+resultDir+'\');" '+debugChecked+' ></input>';
			captureMode = '<input type="checkbox" '+disabled+' name="captureMode" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\',\''+nodeUrl+'\', \''+resultDir+'\');" '+captureChecked+' ></input>';
			
			items.push('<TD class="svcBody" style="width:10px;">' + needStop + '</TD>');
			items.push('<TD class="svcBody" style="width:10px;">' + debugMode + '</TD>');
			items.push('<TD class="svcBody" style="width:10px;">' + captureMode + '</TD>');
			items.push('<TD class="svcBody" style="width:120px;">' + testExecContext.resourcesNames + '</TD>');
			
			act = '';
			if (testExecContext.executorState == 'WAITING')
				act = '<img src=\'/baligridhub/data/images/replay.gif\' onclick="updateSuiteExecContext(\''+testExecContext.className+'\',\'testExecutorCmd\',\'play\', \''+testExecContext.threadId+'\',\''+nodeUrl+'\',\''+resultDir+'\');" >';
				
			
			items.push('<TD class="svcBody" style="width:20px;">'+act+'</TD>');
			items.push('<TD class="svcBody" style="width:60px;"> ' + testExecContext.executorState + '</TD>');        		
			
			exportcmd = '';
			if (testExecContext.completed == true)
				exportcmd =  '<img src=\'/bali/data/images/replay.gif\' onclick="invokeExporterForTest(\''+testExecContext.className+'\',document.getElementById(\'metadatabox\').value,\''+nodeUrl+'\',\''+resultDir+'\');" >';
				
			color = 'green';

			if (testExecContext.callBackOld == true)
				color = 'blue';
			
			if (testExecContext.exportResultText.substring(0, 4) == 'FAIL' || testExecContext.exportResultText.substring(0, 8) == 'no refer'){
				color = 'red';
			}
			
			items.push('<TD class="svcBody" style="width:100px;"> '+exportcmd+' <a href=\'#\' title="'+testExecContext.exportResultTooltip+'" > <font color='+color+'> ' + testExecContext.exportResultText + '</font>  </a>  </TD>');
			
			items.push('</TR>');
			
			
			});
			
			return items.join('');
			
			
}


var timeout;

function refreshAllPage(){

 $.getJSON('/bali/form/status/suitecontext/json?resultDir=${suiteExecContext.resultDir}', 
		  
		  function(suiteExecContext) {
	 		var src = [];
	 		resultDir = "${suiteExecContext.resultDir}";
	 		nodeUrl = "/bali/";
	 		nodeId = "1";
	        src.push(renderNodeSuiteContextHeader(suiteExecContext, resultDir, nodeUrl));
	  		src.push('<TABLE style="padding-left:40px; padding-top:20px;">');
	  		src.push(renderSuiteTableHeader(suiteExecContext, nodeId, nodeUrl, ));
	  		src.push(renderSuiteTable(suiteExecContext, nodeUrl, resultDir));
	  		src.push('<TABLE>');
			$("#suitecontext").html(src.join(''));
		 }
 
 	);
	
 
 timeout = window.setTimeout("refreshAllPage();", 5000);
}
 
refreshAllPage();


</script>
