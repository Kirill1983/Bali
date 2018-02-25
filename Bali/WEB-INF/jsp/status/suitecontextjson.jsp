<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<json:object>
  <json:property name="resultDir" value=" ${suiteExecContext.resultDir}"/>
  <json:property name="startDate" value=" ${suiteExecContext.startDateFmt}"/>
  <json:property name="endDate" value="${suiteExecContext.endDateFmt}"/>
  <json:property name="elapsed" value="${suiteExecContext.elapsedFmt}"/>
  <json:property name="summaryElapsed" value="${suiteExecContext.summaryElapsedFmt}"/>
  <json:property name="timeAccelerateRate" value="${suiteExecContext.timeAccelerateRateFmt}"/>
  <json:property name="loadMode" value="${suiteExecContext.loadmode}"/>
  <json:property name="loadMinutes" value="${suiteExecContext.loadminutes}"/>  
  <json:property name="completed" value="${suiteExecContext.completed}"/>  
  <json:property name="options" value="${suiteExecContext.suiteMdl.options}"/>  
  
  <json:property name="exporterMetaData" value="${suiteExecContext.exporterMetaData}"/>
  
  <json:property name="exportResultText" value="${suiteExecContext.exportResultMessage.text}"/>
  <json:property name="exportResultTooltip" value="${suiteExecContext.exportResultMessage.tooltip}"/>
  
  
  <json:property name="doneCount"      value="${suiteExecContext.executedCount}"/>
  <json:property name="passedCount"    value="${suiteExecContext.successCount}"/>
  <json:property name="failedCount"    value="${suiteExecContext.failCount}"/>
  <json:property name="errorCount"     value="${suiteExecContext.errorCount}"/>
  <json:property name="uniqErrorCount" value="${suiteExecContext.uniqErrorCount}"/>
  
<json:array name="testExecContexts" var="testExecContext" items="${suiteExecContext.testExecContexts}">
    <json:object>
      <json:property name="lineViewLabel" value="${testExecContext.runnableNode.lineViewLabel}"/>
      <json:property name="className" value="${testExecContext.className}"/>
      <json:property name="threadId" value="${testExecContext.threadId}"/>
      <json:property name="errorCount" value="${testExecContext.errorCount}"/>
      <json:property name="status" value="${testExecContext.status.desc}"/>
      <json:property name="loops" value="${testExecContext.loops}"/>
      <json:property name="startDate" value="${testExecContext.startDateFmt}"/>
      <json:property name="endDate" value="${testExecContext.endDateFmt}"/>
      <json:property name="elapsed" value="${testExecContext.elapsedFmt}"/>
      <json:property name="needStop" value="${testExecContext.needStop}"/>
      <json:property name="debugMode" value="${testExecContext.debugMode}"/>
      <json:property name="captureMode" value="${testExecContext.captureMode}"/>
      <json:property name="resourcesNames" value="${testExecContext.resourcesNames}"/>
      <json:property name="executorName" value="${testExecContext.testExecutor.name}"/>
      <json:property name="executorState" value="${testExecContext.testExecutor.state}"/>
      <json:property name="completed" value="${testExecContext.completed}"/>
      <json:property name="callBackOld" value="${testExecContext.callBackOld}"/>
      <json:property name="exportResultText" value="${testExecContext.exportResultMessage.text}"/>
  	  <json:property name="exportResultTooltip" value="${testExecContext.exportResultMessage.tooltip}"/>            
    </json:object>
</json:array>
</json:object>

 

	