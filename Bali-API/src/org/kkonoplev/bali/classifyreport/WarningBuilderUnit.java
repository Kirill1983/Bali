package org.kkonoplev.bali.classifyreport;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.classifyreport.model.WarningCase;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseHREFArtifact;
	
public class WarningBuilderUnit {
	
	private static final Logger log = Logger.getLogger(WarningBuilderUnit.class);
	
	
	public static Warning build(int i, int j){
		 
		Warning warning = new Warning("this is main error text _AAAA-125_ "+j);

		WarningCase warningCase = new WarningCase("suiteName", "testName"+i, "testName"+i, "this is added text "+i);
		
		warningCase.getArtifacts().add(new WarningCaseHREFArtifact("url", "http://yandex.ru/"+i));
		warningCase.getArtifacts().add(new WarningCaseHREFArtifact("testlog", "http://google.ru/"+i));
		//warningCase.getArtifacts().add(new WarningCaseIMGArtifact("a"+i+".jpg"));
		//warningCase.getArtifacts().add(new WarningCaseXMLViewArtifact("my.xml"));
		warning.getWarningCases().add(warningCase);
		return warning;
		 
	}
	
	
}
