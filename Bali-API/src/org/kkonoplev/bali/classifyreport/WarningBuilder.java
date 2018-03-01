package org.kkonoplev.bali.classifyreport;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.classifyreport.model.WarningCase;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseTreeLogArtifact;
import org.kkonoplev.bali.suiteexec.TestExecContext;
	
public class WarningBuilder {
	
	private static final Logger log = Logger.getLogger(WarningBuilder.class);
	
	private static String DELIM = ",,";
	
	public static Warning build(String errorText, TestExecContext testExecContext, WarningCaseArtifactsBuilder[] artifactsBuilders){

		String mainAndAddtext[] = splitMainAndAddText(errorText);
        
		Warning warning = new Warning(mainAndAddtext[0]);
		WarningCase warningCase = new WarningCase(
				testExecContext.getSuiteExecContext().getResultDir(), 
				testExecContext.getRunnableNode().getFullName(), 
				testExecContext.getRunnableNode().getFullName(), 
				mainAndAddtext[1]
			);
		
		warningCase.getArtifacts().add(new WarningCaseTreeLogArtifact(testExecContext.getErrorCount()+"", testExecContext.getThreadId()+""));
		
		for (WarningCaseArtifactsBuilder artifactsBuilder: artifactsBuilders)
			warningCase.getArtifacts().addAll(artifactsBuilder.build());
		
		warning.getWarningCases().add(warningCase);
		return warning; 
	}

	private static String[] splitMainAndAddText(String errorText) {
		
		String mainErrorText = errorText;
		String addErrorText = "";

        int indx = errorText.indexOf(DELIM);
        if (indx > 0) {
            addErrorText  = errorText.substring(indx + 2);
            mainErrorText = errorText.substring(0, indx);
        }
        
        String[] t = new String[] {mainErrorText, addErrorText};
        return t;

	}
	
}
