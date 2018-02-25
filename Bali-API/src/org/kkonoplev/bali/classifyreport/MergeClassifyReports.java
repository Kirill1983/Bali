package org.kkonoplev.bali.classifyreport;

import java.io.File;

import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.OnNodeSuite;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.classifyreport.model.WarningCase;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseHREFArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseHREFResDirArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseIMGArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseResultDirArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseIFrameArtifact;
import org.kkonoplev.bali.gridhub.GridSuiteMdl;
import org.kkonoplev.bali.gridhub.NodeSuiteMdl;
import org.kkonoplev.bali.gridhub.RequestAPI;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class MergeClassifyReports {
	
	public static RuntimeTypeAdapterFactory<WarningCaseArtifact> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
		    .of(WarningCaseArtifact.class, "type")
		    .registerSubtype(WarningCaseHREFArtifact.class, "HREF")
		    .registerSubtype(WarningCaseHREFResDirArtifact.class, "HREFDir")
			.registerSubtype(WarningCaseIMGArtifact.class, "IMG")
			.registerSubtype(WarningCaseIFrameArtifact.class, "XML");
	
	public static void main(String[] args) throws Exception {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
			
		SuiteExecContext ctx1 = SuiteExecContext.deserialize("C:\\projects\\Bali\\results\\Feb_15_16_49_32_334_ENV_A_grid31\\suiteExecContext.txt");		
		String res = gson.toJson(ctx1.getClassifyReport());
		System.out.println(res);
		ClassifyReport fromJson = gson.fromJson(res, ClassifyReport.class);
		System.out.println(gson.toJson(fromJson));
		
		
				
	}

	public static void main1(String[] args) throws Exception {
		Gson gson = new Gson();
		SuiteExecContext ctx1 = SuiteExecContext.deserialize("C:\\projects\\Bali\\results\\Feb_15_16_49_32_334_ENV_A_grid31\\suiteExecContext.txt");
		SuiteExecContext ctx2 = SuiteExecContext.deserialize("C:\\projects\\Bali\\results\\Feb_15_16_49_32_019_ENV_A_grid31\\suiteExecContext.txt");
		System.out.println(ctx1.getClassifyReport().getWarnList().size());
		
		String res = gson.toJson(ctx1.getClassifyReport());
		System.out.println(res);
		ClassifyReport pr = gson.fromJson(res, ClassifyReport.class);
		
		ClassifyReport rpM = new ClassifyReport(new File("C:\\projects\\BaliGridHub\\results\\Dec_25_15_37_38_28_ENV_A_manual\\"), "classify.htm");
		
		ClassifyReport rp1 = ctx1.getClassifyReport();
		ClassifyReport rp2 = ctx2.getClassifyReport();
		
		updateHtmlRef(rp1, "http://localhost:8080/bali/", "Feb_15_16_49_32_334_ENV_A_grid31");
		updateHtmlRef(rp2, "http://localhost:8080/bali/", "Feb_15_16_49_32_019_ENV_A_grid31");
		
		rpM.addWarningTracker(rp1);
		rpM.addWarningTracker(rp2);
		
		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(rpM);
		classifyReportBuilder.save();

		
				
	}
	
	public static ClassifyReport genUnitedClassifyReport(GridSuiteMdl gsuiteMdl, String dir) throws Exception {
		
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
		
		ClassifyReport mainRpt = new ClassifyReport(new File(dir));
		
		for (NodeSuiteMdl ns : gsuiteMdl.getNodeSuites()){
			String suiteDir = ns.getSuiteMdl().getResultDir();		
			System.out.println("Requesting classify report for "+suiteDir);
			String res = RequestAPI.getURL(ns.getNode().getUrl()+"form/status/suitecontext/classifyreport/json?resultDir="+suiteDir);
		    ClassifyReport rpt = gson.fromJson(res, ClassifyReport.class);
		    updateHtmlRef(rpt, ns.getNode().getUrl(), ns.getSuiteMdl().getResultDir());
		    mainRpt.addWarningTracker(rpt);
		}
		
		return mainRpt;
		
	}

	private static void updateHtmlRef(ClassifyReport report, String nodeUrl,
			String resultDir) {
		
		for (Warning warn : report.getWarnList()){
			
			System.out.println("warn onNodeSUites :"+warn.getOnNodeSuites());
			
			warn.getOnNodeSuites().add(new OnNodeSuite(warn.getId(), nodeUrl, resultDir));
			
			for (WarningCase warnCase : warn.getWarningCases()){
				
				for (WarningCaseArtifact artfact : warnCase.getArtifacts()){
					if (artfact instanceof WarningCaseResultDirArtifact ){
						WarningCaseResultDirArtifact dirArtfact = ((WarningCaseResultDirArtifact)artfact);
						dirArtfact.setPreUrl(nodeUrl+"results/"+resultDir+"/");	
					}
				}
				
			}
		}
			

		
	}

}
