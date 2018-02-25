package org.kkonoplev.bali.classifyreport;

import java.io.File;

import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.Warning;

public class WarningRun {

	public static void main(String[] args) {
		
		File dir = new File("C:\\tracker\\");
		ClassifyReport classifyReport = new ClassifyReport(dir, false);
		
		String textA1[] = new String[] { "assert 1 group=group 1, type OUTRIGHT ,, error count 0 ",
		"assert 1 group=group 1, type OUTRIGHT ,, error count 2",
		"assert 1 group=group 1, type OUTRIGHT ,, error count 3",
		"assert 1 group=group 3, type BUTTERFLY ,, error count 4",
		"java.lang.RuntimeException: Expected negotiations to be saved: 1.",
		"java.lang.RuntimeException: Expected negotiations to be saved: 1."
		
		};
			
		String text4 = "error1 ,, add1";
		String text5 = "error1 ,, add2";
		int errorId = 1;
		
		for (String text : textA1){
		  Error error1 = new Error(new Throwable(text));
		  errorId++;
		  Warning warn1 = new Warning("DIR", "method (my.test"+errorId+")", error1, errorId+"","", "1", "my -> test "+errorId);
		  classifyReport.addWarning(warn1);
		}
		
		//tracker.save();
		
		
		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(classifyReport);
		classifyReportBuilder.save();
		

	}

}
