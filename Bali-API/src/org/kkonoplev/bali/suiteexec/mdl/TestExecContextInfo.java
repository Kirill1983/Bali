package org.kkonoplev.bali.suiteexec.mdl;

public class TestExecContextInfo {
	 private String resultDir = "";
	 private String lineViewLabel = "";
	 private String className = "";
	 private String threadId = "";
	 private String errorCount = "";
	 private String status = "";
	 private String loops = ""; //" value="${testExecContext.loops}"/>
	 private String startDate = ""; //" value="${testExecContext.startDateFmt}"/>
	 private String endDate = ""; //" value="${testExecContext.endDateFmt}"/>
	 private String elapsed = ""; //" value="${testExecContext.elapsedFmt}"/>
	 private boolean needStop = false;  //" value="${testExecContext.needStop}"/>
	 private boolean debugMode = false; //" value="${testExecContext.debugMode}"/>
	 private boolean captureMode = false; //" value="${testExecContext.captureMode}"/>
	 private String resourcesNames = ""; //" value="${testExecContext.resourcesNames}"/>
	 private String executorName = "";//" value="${testExecContext.testExecutor.name}"/>
	 private String executorState = "";//" value="${testExecContext.testExecutor.state}"/>
	 private boolean completed = false;//" val/ue="${testExecContext.completed}"/>
	 private String callBackOld = "";//" value="${testExecContext.callBackOld}"/>
	 private String exportResultText = "";//" value="${testExecContext.exportResultMessage.text}"/>
	 private String exportResultTooltip = "";//" value="${testExecContext.exportResultMessage.tooltip}"/>
	 
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	public String getLineViewLabel() {
		return lineViewLabel;
	}
	public void setLineViewLabel(String lineViewLabel) {
		this.lineViewLabel = lineViewLabel;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public String getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLoops() {
		return loops;
	}
	public void setLoops(String loops) {
		this.loops = loops;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getElapsed() {
		return elapsed;
	}
	public void setElapsed(String elapsed) {
		this.elapsed = elapsed;
	}
	public boolean getNeedStop() {
		return needStop;
	}
	public void setNeedStop(boolean needStop) {
		this.needStop = needStop;
	}
	public boolean getDebugMode() {
		return debugMode;
	}
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	public boolean getCaptureMode() {
		return captureMode;
	}
	public void setCaptureMode(boolean captureMode) {
		this.captureMode = captureMode;
	}
	public String getResourcesNames() {
		return resourcesNames;
	}
	public void setResourcesNames(String resourcesNames) {
		this.resourcesNames = resourcesNames;
	}
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	public String getExecutorState() {
		return executorState;
	}
	public void setExecutorState(String executorState) {
		this.executorState = executorState;
	}
	public boolean getCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getCallBackOld() {
		return callBackOld;
	}
	public void setCallBackOld(String callBackOld) {
		this.callBackOld = callBackOld;
	}
	public String getExportResultText() {
		return exportResultText;
	}
	public void setExportResultText(String exportResultText) {
		this.exportResultText = exportResultText;
	}
	public String getExportResultTooltip() {
		return exportResultTooltip;
	}
	public void setExportResultTooltip(String exportResultTooltip) {
		this.exportResultTooltip = exportResultTooltip;
	}
	 
	 
	 
	 

}
