package org.kkonoplev.bali.classifyreport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseHREFArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseHREFResDirArtifact;
import org.kkonoplev.bali.suiteexec.TestExecContext;

public class TestLogArtifactsBuilder implements WarningCaseArtifactsBuilder {
	
	private static final Logger log = LogManager.getLogger(TestLogArtifactsBuilder.class);
	private String logname;
	private String addPathWeb = "";
	
	public TestLogArtifactsBuilder(TestExecContext testExecContext){
		logname = "TESTLOG-"+testExecContext.getRunnableNode().getName()+"-"+testExecContext.getThreadId()+".html";
		if (testExecContext.getClassName().lastIndexOf(".") != -1)
			addPathWeb = testExecContext.getClassName().substring(0, testExecContext.getClassName().lastIndexOf(".")).replaceAll("\\.", "/");
	}

	@Override
	public List<WarningCaseArtifact> build() {
		List<WarningCaseArtifact> list = new ArrayList<WarningCaseArtifact>();
		String logPath = logname;
		if (addPathWeb.length() != 0)
			logPath = addPathWeb + "/"+logname;
		list.add(new WarningCaseHREFResDirArtifact("testlog", logPath));
		return list;
	}
	

	
	

}
