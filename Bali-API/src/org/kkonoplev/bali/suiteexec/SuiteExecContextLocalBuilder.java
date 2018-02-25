package org.kkonoplev.bali.suiteexec;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportViewConfig;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.common.perfomance.PerfomanceReport;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.services.ProjectService;


public class SuiteExecContextLocalBuilder {
	
	private static final Logger log = LogManager.getLogger(SuiteExecContextLocalBuilder.class);
	
	public SuiteExecContext build(SuiteMdl suiteMdl, String resDir, String propsDir) throws Exception{
		
		SuiteExecProcessor execProcessor = new SuiteExecProcessor();
		
		SuiteExecContext suiteExecContext = new SuiteExecContext();
		
		suiteExecContext.setSuiteMdl(suiteMdl);
		suiteExecContext.setExecProcessor(execProcessor);
		
		File resultDirFile = buildResultDir(suiteMdl, resDir);
		suiteExecContext.setResultDir(resultDirFile.getName());
		suiteExecContext.setResultDirFile(resultDirFile);
		
		suiteExecContext.setStartDate(new Date());
		
		suiteExecContext.setLoadminutes(Integer.valueOf(suiteMdl.getLoadminutes()));
		suiteExecContext.setLoadmode(suiteMdl.getLoadmode());
		
		suiteExecContext.setPerfomanceReport(new PerfomanceReport());
		
		suiteExecContext.setClassifyReport(buildClassifyReport(resultDirFile, new HashMap<String, String>()));
		suiteExecContext.setCaptureReport(buildCaptureReport(resultDirFile));
		suiteExecContext.setProperties(buildProperties(suiteMdl, propsDir));
		suiteExecContext.setTestExecContexts(buildTestExecContexts(suiteExecContext, execProcessor.getProjectSvc(), suiteMdl));
	
		
		return suiteExecContext;
		
	}

	private File buildResultDir(SuiteMdl suiteMdl, String resDir) {
		//result dir
		String dirName = new Date().toString().replaceAll("[ |:]","_").substring(0, 19);
		dirName = dirName.substring(4);
		dirName += "_"+RandomStringUtils.randomNumeric(2);
		dirName += "_"+suiteMdl.getOptions()+"_"+suiteMdl.getName().replaceAll(" ","_");		
		
		File resultDirFile = new File(resDir, dirName);
		resultDirFile.mkdirs();
		log.info(this+" result dir:"+resultDirFile.getAbsolutePath());
		
		return resultDirFile;
	}

	private ClassifyReport buildClassifyReport(File resultDirFile, Map<String, String> envMap) {
		
		ClassifyReport classifyReport = new ClassifyReport(resultDirFile, false);
		classifyReport.setEnvironments(envMap);
		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(classifyReport);
		classifyReportBuilder.save();
		return classifyReport;

	}

	private ClassifyReport buildCaptureReport(File resultDirFile) {
		
		ClassifyReport captureReport = new ClassifyReport(resultDirFile, "captureclassifyNG", false);
		ClassifyReportHTMLBuilder captureReportBuilder = new ClassifyReportHTMLBuilder(ClassifyReportViewConfig.CAPTURE, captureReport);
		captureReportBuilder.save();
		return captureReport;

	}

	private Properties buildProperties(SuiteMdl suiteMdl, String propsDir) throws Exception {
		String propsFile = propsDir+suiteMdl.getOptions()+".properties";
		FileReader freader = new FileReader(propsFile);
		Properties properties = new Properties();
		properties.load(freader);
		return properties;
	}
	
	
	private ArrayList<TestExecContext> buildTestExecContexts(SuiteExecContext suiteExecContext, ProjectService projectSvc, SuiteMdl suiteMdl) throws Exception {
		
		log.info("Forming TestExecContext List for "+suiteExecContext.getResultDir());
		ArrayList<TestExecContext> testExecContexts = new ArrayList<TestExecContext>(suiteMdl.getTestList().length);
		TestExecContextLocalBuilder testExecContextBuilder = new TestExecContextLocalBuilder(); 
		for (String testDesc: suiteMdl.getTestList()) 
			testExecContexts.addAll(testExecContextBuilder.build(suiteExecContext, testDesc));	
		return testExecContexts;
		
	}

	private String buildExportMetaData(SuiteExecContext suiteExecContext, ProjectService projectSvc) {
		
		String projectName = suiteExecContext.getTestExecContexts().get(0).getProjectName();
		BaseProject project = projectSvc.getProject(projectName);
		
		if (project.getPluggableProcessorStore().isEmpty())
			return "";
		
		String exporterMetaData = project.getPluggableProcessorStore().getDefaultmeta();
		return exporterMetaData;
		
	}
	

}
