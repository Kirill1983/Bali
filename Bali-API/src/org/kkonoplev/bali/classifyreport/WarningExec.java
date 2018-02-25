package org.kkonoplev.bali.classifyreport;

import java.io.File;

import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.Warning;

public class WarningExec {
	
	public static void main(String[] args) {
		
		File dir = new File("C:\\projects\\Bali\\results\\demo");	
		ClassifyReport classifyReport = new ClassifyReport(dir, false);
		
		for (int i = 1; i <= 3; i++)
			for (int j = 1; j <= 3; j++){
				Warning warning = WarningBuilderUnit.build(i, j);
				classifyReport.addWarning(warning);
			}
		
		
		classifyReport.clearFromTest("testName1");

		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(classifyReport);
		classifyReportBuilder.save();

	}	

}
