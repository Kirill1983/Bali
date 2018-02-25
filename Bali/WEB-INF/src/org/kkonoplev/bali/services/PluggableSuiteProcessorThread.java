package org.kkonoplev.bali.services;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;

public class PluggableSuiteProcessorThread extends Thread {
	
	private static final Logger log = Logger.getLogger(PluggableSuiteProcessorThread.class);
	
	private SuitePluggableProcessor suitePlugProcessor;
	private SuiteExecContext suiteExecContext;
	private String className = "";
	
	public PluggableSuiteProcessorThread(SuitePluggableProcessor suitePlugProcessor, SuiteExecContext suiteExecContext) {
		super();
		this.suitePlugProcessor =  suitePlugProcessor;
		this.suiteExecContext = suiteExecContext;
	}
	
	public PluggableSuiteProcessorThread(SuitePluggableProcessor suitePlugProcessor, SuiteExecContext suiteExecContext, String className) {
		super();
		this.suitePlugProcessor =  suitePlugProcessor;
		this.suiteExecContext = suiteExecContext;
		this.className = className;
	}
	
	
	public void run(){

		log.warn("execute thread ext processor");
		BaliServices.getSuiteService().getSuiteExecProcessor().getSuiteExecContexts().add(suiteExecContext);
		runSuiteProcess();
		BaliServices.getSuiteService().getSuiteExecProcessor().getSuiteExecContexts().remove(suiteExecContext);
		log.warn("execute thread ext processor finished");
	
	}



	private void runSuiteProcess() {
		
		try {
			suiteExecContext.setUnderExporting(true);
			
			if (className.equals(""))
				suitePlugProcessor.processSuiteContext(suiteExecContext, BaliServices.getProjectService(), suiteExecContext.getExporterMetaData());
			else 
				suitePlugProcessor.processTestContext(suiteExecContext, className, BaliServices.getProjectService(), suiteExecContext.getExporterMetaData());
				
		} finally {
			suiteExecContext.setUnderExporting(false);
			suiteExecContext.save();
		}
		
	}

}