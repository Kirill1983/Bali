package org.kkonoplev.bali.classifyreport;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.Warning;

public class WarningExec {
	
	public static void main(String[] args) {
		
		File dir = new File("C:\\tracker\\report\\today");	
		ClassifyReport classifyReport = new ClassifyReport(dir, false);
		
		for (int i = 1; i <= 3; i++)
			for (int j = 1; j <= 3; j++){
				Warning warning = WarningBuilderUnit.build(i, j);
				classifyReport.addWarning(warning);
			}
		
		Map<String, String> envs = new HashMap<String, String>();
		envs.put("env1", "env1");
		envs.put("env2", "env2");

		classifyReport.clearFromTest("testName1");
		classifyReport.setEnvironments(envs);

		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(classifyReport);
		classifyReportBuilder.save();

	}	

}
