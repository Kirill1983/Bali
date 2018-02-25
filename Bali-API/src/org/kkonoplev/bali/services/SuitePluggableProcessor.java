package org.kkonoplev.bali.services;

import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;

public interface SuitePluggableProcessor {
	
	public enum ExportStatus {
		OK, FAIL, ADDING;
	}
	
	public void processSuiteContext(SuiteExecContext suiteContext, ProjectService projectSvc, String metaData);
	public void processTestContext(SuiteExecContext suiteContext, String className, ProjectService projectSvc, String metaData);
	
	public String getStartButtonUIText();
	public String getMetaDataDescription();
	
	

}
