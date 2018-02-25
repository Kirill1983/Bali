package org.kkonoplev.bali.gridhub;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.MergeClassifyReports;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.OnNodeSuite;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.classifyreport.model.WarningCase;
import org.kkonoplev.bali.common.perfomance.PerfomanceReport;
import org.kkonoplev.bali.suiteexec.SuiteMdl;


public class GridHubService {
	
	private static final Logger log = Logger.getLogger(GridHubService.class);
	
	private GridHub gridHub = new GridHub();
	private ArrayList<GridSuiteExecContext> gsuiteExecContexts = new ArrayList<GridSuiteExecContext>();

	public GridHubService(){
		gridHub.getNodes().add(new GridNode("1", "http://localhost:8080/bali/"));
		gridHub.getNodes().add(new GridNode("2", "http://localhost:8080/bali/"));
	}

	public GridHub getGridHub() {
		return gridHub;
	}

	public GridSuiteExecContext runSuite(SuiteMdl allSuiteMdl, String baliResultDir) throws Exception {
		
		log.info("Run Suite on Grid Hub");
		
		GridSuiteMdl gridSuiteMdl = splitSuiteToNodes(allSuiteMdl);
		GridSuiteExecContext gsuiteContext = new GridSuiteExecContext();
		gsuiteContext.setGsuiteMdl(gridSuiteMdl);
		gsuiteContext.setStartDate(new Date());
		gsuiteContext.setResultDir(genResultDir(gridSuiteMdl));
		getgsuiteExecContexts().add(gsuiteContext);
		
		
		for (NodeSuiteMdl nodeSuite : gridSuiteMdl.getNodeSuites())
			 runNodeSuite(nodeSuite);
		
		saveGridContext(gsuiteContext, baliResultDir);
		
		return gsuiteContext;
		 	 
	}
	
	private void saveGridContext(GridSuiteExecContext gsuiteContext,
			String baliResultDir) {

		String dir = baliResultDir+File.separator+gsuiteContext.getResultDir()+File.separator;
		(new File(dir)).mkdirs();
		log.info("Mk dir:"+dir);
		gsuiteContext.serialize(new File(dir+GridSuiteExecContext.SUITE));
		
	}

	private String genResultDir(GridSuiteMdl gridSuiteMdl) {

		String dirName = new Date().toString().replaceAll("[ |:]","_").substring(0, 19);
		dirName = dirName.substring(4)
		 + "_"+RandomStringUtils.randomNumeric(2)
		 + "_"+gridSuiteMdl.getNodeSuites().get(0).getSuiteMdl().getOptions();
	
		return dirName;
		
	}

	private void runNodeSuite(NodeSuiteMdl nodeSuite) throws Exception {
		
		 log.info("-----------------------------------------");
		 log.info("Start run on Node: "+nodeSuite.getNode().getId());
		 log.info("Tests: "+nodeSuite.getSuiteMdl().getTests());
		 
		 SuiteMdl suiteMdl = nodeSuite.getSuiteMdl();
		 Map<String, String> rargs = new HashMap<String, String>();
		 rargs.put("options", suiteMdl.getOptions());
		 rargs.put("browser", suiteMdl.getBrowser());
		 
		 rargs.put("_capturemode", "on");
		 rargs.put("_debugmode", "on");
		 rargs.put("_loadmode", "on");

		 if (suiteMdl.isCapturemode())
			 rargs.put("capturemode", suiteMdl.isCapturemode()+"");
		 
		 if (suiteMdl.isDebugmode())
			 rargs.put("debugmode", suiteMdl.isDebugmode()+"");
		 
		 if (suiteMdl.getLoadmode())
			 rargs.put("loadmode", suiteMdl.getLoadmode()+"");

		 rargs.put("loadminutes", suiteMdl.getLoadminutes());
		 rargs.put("rumpup", suiteMdl.getRumpup());
		 rargs.put("tests", suiteMdl.getTests());
		 rargs.put("email",suiteMdl.getEmail());
		 rargs.put("resultDir",suiteMdl.getResultDir());
		 rargs.put("name", "grid");
		 rargs.put("nodeId", nodeSuite.getNode().getId());

		 GridNode gnode = nodeSuite.getNode();
	 	 RequestAPI.postURL(gnode.getUrl()+ "form/pad/run", rargs);
	 	 
	}

	private GridSuiteMdl splitSuiteToNodes(SuiteMdl suiteMdl) {
		
		ArrayList<StringBuffer> testsOnNode = splitTests(suiteMdl.getTestList());
		GridSuiteMdl gridSuiteMdl = new GridSuiteMdl();
		
		int i = 0;
		for (StringBuffer testOnNode : testsOnNode){

			if (testOnNode.toString().length() == 0)
				continue;
			
			SuiteMdl suite = suiteMdl.clone();
			suite.setResultDir(genResultDir(suite));
			suite.setTests(testOnNode.toString());
			NodeSuiteMdl nodeSuite = new NodeSuiteMdl(suite, gridHub.getNode(i));
			log.info(nodeSuite.toString());
			gridSuiteMdl.getNodeSuites().add(nodeSuite);
			i++;
	
		}
		
		return gridSuiteMdl;
	}

	private String genResultDir(SuiteMdl suiteMdl) {
		String dirName = new Date().toString().replaceAll("[ |:]","_").substring(0, 19);
		dirName = dirName.substring(4);
		dirName += "_"+RandomStringUtils.randomNumeric(3);
		dirName += "_"+suiteMdl.getOptions()+"_"+suiteMdl.getName().replaceAll(" ","_");
		return dirName;
	}

	private ArrayList<StringBuffer> splitTests(String[] testList) {
		int size = gridHub.getNodes().size();
		ArrayList<StringBuffer> testsOnNode = new ArrayList<>(gridHub.getNodes().size());
		for (int i = 0; i < size; i++)
			testsOnNode.add(new StringBuffer());
		
		int nodeNum = 0;
		
		for (String test: testList){
		
			String[] testInfo = test.split("#");
			String projectName = testInfo[0];
			String path = testInfo[1];
			int threads = Integer.valueOf(testInfo[2]);
			
			if (threads > 1){
				nodeNum = splitLoadThreadsOnNodes(projectName, path, threads, testsOnNode, nodeNum);
				continue;
			}
		
			nodeNum = nodeNum % size;
			testsOnNode.get(nodeNum).append(test+",");
			nodeNum++; 
		}
		
		return testsOnNode;
	}

	private int splitLoadThreadsOnNodes(String projectName, String path,
			int threads, ArrayList<StringBuffer> testsOnNode, int freeNode) {
	
		int nodes = testsOnNode.size();
		int threadsPerNode = (int)Math.round(threads/nodes+0.49);
		
		if (threadsPerNode == 0)
			threadsPerNode = 1;
		
		int threadsCnt = threads;
		int n = 0;
		
		for (int i = 1; i <= nodes; i++){
			
			int curNode = 0;
			if (i == (nodes))
				curNode = threadsCnt;
			else
				curNode = threadsPerNode;
			
			n = (i+freeNode-1) % nodes;
			String test = projectName+"#"+path+"#"+curNode;
			testsOnNode.get(n).append(test+",");	
		
			threadsCnt -= threadsPerNode;
			if (threadsCnt == 0)
				break;
		}
		
		return n+1;
		
	}

	public ArrayList<GridSuiteExecContext> getgsuiteExecContexts() {
		return gsuiteExecContexts;
	}
	
	

	public GridSuiteExecContext getGridSuiteExecContext(String resultDir) {
		for (GridSuiteExecContext gsuiteCtx : this.gsuiteExecContexts)
			if (gsuiteCtx.getResultDir().equals(resultDir))
				return gsuiteCtx;
		
		return null;
	}

	public PerfomanceReport genUnitedPerfReport(GridSuiteMdl gsuiteMdl) throws Exception {
		return PerfMerge.genUnitedPerfReport(gsuiteMdl);
	}

	public ClassifyReport saveUnitedClassifyReport(GridSuiteMdl gsuiteMdl, String baliResultsDir, String resultDir) throws Exception {
		ClassifyReport rpt = MergeClassifyReports.genUnitedClassifyReport(gsuiteMdl, baliResultsDir+File.separator+resultDir+File.separator);
		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(rpt);
		classifyReportBuilder.save();
		return rpt;
	}

	public void rerunWarnings(GridSuiteExecContext gsuiteExecContext,  String warningsIds) throws Exception {
		String resultDir = gsuiteExecContext.getResultDir();
		ClassifyReport classifyReport = gsuiteExecContext.getClassifyReport();
		
		log.info("Rerun resultDir ="+resultDir+", warnings "+warningsIds);
		String warnIDs[] = warningsIds.split(",");
		ArrayList<String> rerunTestNames = new ArrayList<String>();
		
		for (String warnId : warnIDs){
			int id = Integer.valueOf(warnId);
			Warning warning = classifyReport.getWarnById(id);
			
			if (warning == null)
				continue;
			
			for (OnNodeSuite onNodeSuite :warning.getOnNodeSuites())
				RequestAPI.getURL(onNodeSuite.getNodeUrl()+"/form/status/rerun?resultDir="+onNodeSuite.getResultDir()+"&warningIDs="+onNodeSuite.getId());
			
		}

		log.info("items to rerun:"+rerunTestNames.size());

	}
	
	
	
	
}
