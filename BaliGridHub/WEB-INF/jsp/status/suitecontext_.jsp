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
 
 function addTime(mins) {
	 
	  $.get("/bali/form/status/suitecontext/addloadtime?resultDir=${suiteExecContext.resultDir}&time="+mins,
			  function(data) {
		  		refreshPage();
			  }
	  		);
	  		
  }

  function addThreads(count) {
	 
	  $.get("/bali/form/status/suitecontext/addthreads?resultDir=${suiteExecContext.resultDir}&threads="+count,
			  function(data) {
		  		refreshPage();
			  }
	  		);
	  		
  }

  function invokeExporter(metaData) {
	 
	  $.get("/bali/form/status/exportresults?resultDir=${suiteExecContext.resultDir}&metaData="+escape(metaData),
			  function(data) {
		  		refreshPage();
			  }
	  		);
	  		
  }

  function invokeExporterForTest(test, metaData) {
	 
	  $.get("/bali/form/status/exportresults?resultDir=${suiteExecContext.resultDir}&className="+test+"&metaData="+escape(metaData),
			  function(data) {
		  		refreshPage();
			  }
	  		);
	  		
  }

  function updateSuiteExecContext(test, property, checked, threadId) {
	 
	  $.get("/bali/form/status/update?"+property+"="+checked+"&className="+test+"&threadId="+threadId+"&resultDir=${suiteExecContext.resultDir}",
			  function(data) {
		  		refreshPage();
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

<div id=suitecontext>
</div>

<script>

function refreshPage(){
	

  $.getJSON('/bali/form/status/suitecontext/json?resultDir=${suiteExecContext.resultDir}', 
		  
		  function(suiteExecContext) {
	  
 			var items = []; 			
 
 			items.push('<TABLE style="padding-left:40px; padding-top:20px;">');
 			items.push('<tr>');
 			items.push('	<td class="svcAll" style="width: 400px;"> ');
 			items.push('		<a target="_blank" href="/bali/results/${suiteExecContext.resultDir}/classifyNG.html"> classify report </a> &nbsp&nbsp');
 			items.push('		<a target="_blank" href="/bali/results/${suiteExecContext.resultDir}/captureclassifyNG.html"> screen capture report </a> &nbsp');
   		    items.push('		<a href="/bali/form/status/perfomancereport?resultDir=${suiteExecContext.resultDir}"> metrics report </a> &nbsp');
 			items.push('	</td>  ');  
 			
 			if (suiteExecContext.completed == false){
 				items.push('<td class="svcAll" style="width: 100px;"> <a href="#" onclick="ajax(\'/bali/form/status/cancelcontext?resultDir=${suiteExecContext.resultDir}\'); return true;" > call threads stop </a> </td>');
 				items.push('<td class="svcAll" style="width: 80px;"> <a href="#" onclick="ajax(\'/bali/form/status/softstopcontext?resultDir=${suiteExecContext.resultDir}\'); return true;" > soft stop </a> </td>');
 			}
 				 	
 			if (suiteExecContext.completed == true)
 				items.push('<td class="svcAll" style="width: 80px;"> <a href="#" onclick="ajax(\'/bali/form/status/deletecontext?resultDir=${suiteExecContext.resultDir}\'); return true;"> delete suite </a> </td>');
 					
 			items.push('</tr>');
 			items.push('</TABLE>');
 			
 			items.push('<BR> <TABLE style="padding-left:40px;">');
 			items.push('<TR>');
 			
 			items.push('<TD class="svcAll">');
 			items.push('<input id=metadatabox type=text value=\"'+suiteExecContext.exporterMetaData+'\" size=70>');
 			items.push('</TD>');
 			
 			items.push('<TD class="svcAll" style="width: 100px;">');
 			items.push('<a href="#" onclick="invokeExporter(document.getElementById(\'metadatabox\').value);" > export results </a> &nbsp;&nbsp;');
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
  				items.push('<TR> <td> <A href="#" onclick="addThreads(1);"> add threads 1 </A> &nbsp; <A href="#" onclick="addThreads(5);"> 5 </A> &nbsp; <A href="#" onclick="addThreads(10);"> 10 </A> &nbsp; <A href="#" onclick="addThreads(20);"> 20 </A> &nbsp; <A href="#" onclick="addThreads(50);"> 50 </A> </td> </TR>');
  				items.push('<TR> <td> <A href="#" onclick="addTime(1);"> add time 1 </A> &nbsp; <A href="#" onclick="addTime(5);"> 5 </A> &nbsp; <A href="#" onclick="addTime(10);"> 10 </A> &nbsp; <A href="#" onclick="addTime(20);"> 20 </A> &nbsp; <A href="#" onclick="addTime(50);"> 50 </A> </td> </TR>');
  			
  			}
  	
 			items.push('</TABLE>');
 			
 			
 			
  			items.push('<TABLE style="padding-left:40px; padding-top:40px;">');
  
  			items.push('<TR>');
  			items.push('<td class="svcHdr">  Execution list ( Options: <a href="/bali/form/status/targetgrid?options='+suiteExecContext.options+'\" > '+suiteExecContext.options+' </a> ) </td>');
  			
  			if (suiteExecContext.loadMode == true){
  				items.push('<td class="svcHdr" style="width:20px;"> loops </td>');
  			}
  			
  			items.push('<td class="svcHdr" style="width:20px;"> errors </td>');
  			items.push('<td class="svcHdr" style="width:30px;"> status </td> ');
			items.push('<td class="svcHdr" style="width:10px;"> Cmd </td>');
  			items.push('<td class="svcHdr" style="width:100px;"> start time </td>');
  			items.push('<td class="svcHdr" style="width:100px;"> end time </td>');
  			items.push('<td class="svcHdr" style="width:20px;" > elapsed (min) </td>');
  			items.push('<td class="svcHdr" style="width:10px;"> Need Stop </td>');
  			items.push('<td class="svcHdr" style="width:10px;"> wait On Error </td>');
  			items.push('<td class="svcHdr" style="width:10px;"> capture screen </td>');
  			items.push('<td class="svcHdr" style="width:120px;"> Use Resources </td>');
  			//items.push('<td class="svcHdr" style="width:60px;"> Executor </td>');
  			items.push('<td class="svcHdr" style="width:20px;"> Actions </td>');
  			items.push('<td class="svcHdr" style="width:60px;"> State </td>');
  			items.push('<td class="svcHdr" style="width:150px;"> Export </td>');
  			
	
  			items.push('</TR>');
  
  			$.each(suiteExecContext.testExecContexts, function(i, testExecContext) {
  				items.push('<TR>');
   				items.push('<TD class="svcBody">' + testExecContext.lineViewLabel + '</TD>');
   				
   				if (suiteExecContext.loadMode == true){
  					items.push('<TD class="svcBody">' + testExecContext.loops + '</TD>');
  				}
   				
    			items.push('<TD class="svcBody" style="width:20px;">' + testExecContext.errorCount + '</TD>');
    			status = testExecContext.status;
    			
    			if (testExecContext.status == 'fail')
    				status = '<font color=red> <i>'+status+'</i> </font>';
    				
    			if (testExecContext.status == 'success')
    				status = '<font color=green> <b>'+status+'</b> </font>';
    				
    			items.push('<TD class="svcBody" style="width:30px;">' + status + '</TD>');
    			
    			cmd = '';
    			if (testExecContext.completed == true)
    				cmd =  '<img src=\'/bali/data/images/replay.gif\' onclick="updateSuiteExecContext(\''+testExecContext.className+'\',\'suiteExecutorProcCmd\',\'replay\', \''+testExecContext.threadId+'\');" >';
    			
    			if (testExecContext.status == 'in progress' || testExecContext.status == 'initializing')
    				cmd =  '<img src=\'/bali/data/images/interrupt.gif\' onclick="updateSuiteExecContext(\''+testExecContext.className+'\',\'suiteExecutorProcCmd\',\'interrupt\',\''+testExecContext.threadId+'\');" >';
    		
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
    			
    			
    			needStop = '<input type="checkbox" '+disabled+' name="needStop" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\');" '+needStopChecked+' ></input>';
    			debugMode = '<input type="checkbox" '+disabled+' name="debugMode" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\');" '+debugChecked+' ></input>';
    			captureMode = '<input type="checkbox" '+disabled+' name="captureMode" onclick="updateSuiteExecContext(\''+testExecContext.className+'\', this.name, this.checked, \''+testExecContext.threadId+'\');" '+captureChecked+' ></input>';
  			
    			items.push('<TD class="svcBody" style="width:10px;">' + needStop + '</TD>');
    			items.push('<TD class="svcBody" style="width:10px;">' + debugMode + '</TD>');
    			items.push('<TD class="svcBody" style="width:10px;">' + captureMode + '</TD>');
    			items.push('<TD class="svcBody" style="width:120px;">' + testExecContext.resourcesNames + '</TD>');
    			//items.push('<TD class="svcBody" style="width:60px;">' + testExecContext.executorName + '</TD>');
    			
    			act = '';
    			if (testExecContext.executorState == 'WAITING')
    				act = '<img src=\'/bali/data/images/add_single.gif\' onclick="updateSuiteExecContext(\''+testExecContext.className+'\',\'testExecutorCmd\',\'play\', \''+testExecContext.threadId+'\');" >';
    				
    			
    			items.push('<TD class="svcBody" style="width:20px;">'+act+'</TD>');
    			items.push('<TD class="svcBody" style="width:60px;"> ' + testExecContext.executorState + '</TD>');        		
    			
    			exportcmd = '';
    			if (testExecContext.completed == true)
    				exportcmd =  '<img src=\'/bali/data/images/replay.gif\' onclick="invokeExporterForTest(\''+testExecContext.className+'\',document.getElementById(\'metadatabox\').value);" >';
    				
    			color = 'green';

    			if (testExecContext.callBackOld == true)
    				color = 'blue';
    			
    			if (testExecContext.exportResultText.substring(0, 4) == 'FAIL' || testExecContext.exportResultText.substring(0, 8) == 'no refer'){
    				color = 'red';
    			}
    			
    			items.push('<TD class="svcBody" style="width:150px;"> '+exportcmd+' <a href=\'#\' title="'+testExecContext.exportResultTooltip+'" > <font color='+color+'> ' + testExecContext.exportResultText + '</font>  </a>  </TD>');
    			
    			items.push('</TR>');
    			
    			
  			});
  
  			items.push('</TABLE>');
  			
 			$("#suitecontext").html(items.join('')); 	
 			
 			if (suiteExecContext.completed == false)
 				window.setTimeout("refreshPage();", 5000);
 			
 			
		 }
  	);

 }
 
 refreshPage();
 

</script>
