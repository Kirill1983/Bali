package org.kkonoplev.bali.suiteexec;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
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


public class SuiteExecContextBuilder {
	
	private static final Logger log = LogManager.getLogger(SuiteExecContextBuilder.class);
	
	public SuiteExecContext build(SuiteMdl suiteMdl, SuiteExecProcessor execProcessor, String resDir, String propsDir, Map<String, String> envMap) throws Exception{
		
		SuiteExecContext suiteExecContext = new SuiteExecContext();
		
		suiteExecContext.setSuiteMdl(suiteMdl);
		suiteExecContext.setExecProcessor(execProcessor);
		
		File resultDirFile = buildResultDir(suiteMdl, resDir);
		suiteExecContext.setResultDir(resultDirFile.getName());
		suiteExecContext.setResultDirFile(resultDirFile);
		
		suiteExecContext.setStartDate(new Date());
		
		suiteExecContext.setLoadmode(suiteMdl.getLoadmode());
		if (suiteMdl.getLoadmode())
			suiteExecContext.setLoadminutes(Integer.valueOf(suiteMdl.getLoadminutes()));
		
		suiteExecContext.setPerfomanceReport(new PerfomanceReport());
		
		suiteExecContext.setClassifyReport(buildClassifyReport(resultDirFile, envMap));
		suiteExecContext.setCaptureReport(buildCaptureReport(resultDirFile));
		suiteExecContext.setProperties(buildProperties(suiteMdl, propsDir));
		suiteExecContext.setTestExecContexts(buildTestExecContexts(suiteExecContext, execProcessor.getProjectSvc(), suiteMdl));
		suiteExecContext.setExporterMetaData(buildExportMetaData(suiteExecContext, execProcessor.getProjectSvc()));
		
		return suiteExecContext;
		
	}

	private File buildResultDir(SuiteMdl suiteMdl, String resDir) {
		//result dir

		if (suiteMdl.getResultDir().equals("")){
			String dirName = new Date().toString().replaceAll("[ |:]","_").substring(0, 19);
			dirName = dirName.substring(4);
			dirName += "_"+RandomStringUtils.randomNumeric(2);
			dirName += "_"+suiteMdl.getOptions()+"_"+suiteMdl.getName().replaceAll(" ","_");
			suiteMdl.setResultDir(dirName);
		}
		
		File resultDirFile = new File(resDir, suiteMdl.getResultDir());
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
		TestExecContextBuilder testExecContextBuilder = new TestExecContextBuilder(); 
		for (String testDesc: suiteMdl.getTestList()){
			ArrayList<TestExecContext> newTestExecContextsPortion = testExecContextBuilder.build(suiteExecContext, testDesc, testExecContexts);
			testExecContexts.addAll(newTestExecContextsPortion);
		}
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
