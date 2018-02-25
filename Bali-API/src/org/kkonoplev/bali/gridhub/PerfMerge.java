package org.kkonoplev.bali.gridhub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.kkonoplev.bali.common.perfomance.Operation;
import org.kkonoplev.bali.common.perfomance.PerfomanceReport;
import org.kkonoplev.bali.common.perfomance.ResponseTime;

import com.google.gson.Gson;


public class PerfMerge {
	
	public static void main(String[] args) throws Exception {
		
		int a = 5;
		String[] suiteDirs = new String[]{"Feb_14_14_51_32_176_ENV_A_grid55", "Feb_14_14_51_32_741_ENV_A_grid55"};
		ArrayList<PerfomanceReport> perfReps = new ArrayList<>();
		
		Gson gson = new Gson();
		for (String suiteDir : suiteDirs){
			System.out.println("-----");
			String res = RequestAPI.getURL("http://localhost:8080/bali/form/status/suitecontext/perf/json?resultDir="+suiteDir);
		    System.out.println(res);
		    PerfomanceReport pr = gson.fromJson(res, PerfomanceReport.class);
		    System.out.println(gson.toJson(pr));
		    perfReps.add(pr);
		    
		}
		
		PerfomanceReport mergedPR = mergeReports(perfReps);
		System.out.println("****");
		System.out.println(gson.toJson(mergedPR));
				
	}


	public static PerfomanceReport genUnitedPerfReport(GridSuiteMdl gsuiteMdl) throws Exception {
		
		Gson gson = new Gson();
		ArrayList<PerfomanceReport> perfReps = new ArrayList<>();
		for (NodeSuiteMdl ns : gsuiteMdl.getNodeSuites()){
			String suiteDir = ns.getSuiteMdl().getResultDir();		
			System.out.println("Requesting perf report for "+suiteDir);
			String res = RequestAPI.getURL(ns.getNode().getUrl()+"form/status/suitecontext/perf/json?resultDir="+suiteDir);
		    PerfomanceReport pr = gson.fromJson(res, PerfomanceReport.class);
		    perfReps.add(pr);
		}
	
		PerfomanceReport mergedPR = mergeReports(perfReps);
		
		sumOpsPerMin(mergedPR);
		return mergedPR;
		
	}
	
	private static void sumOpsPerMin(PerfomanceReport perfRpt) {
		for (Operation op : perfRpt.getOperations()){
			op.getName().contains("ops/min");
		}
		
	}


	public static PerfomanceReport mergeReports(
			ArrayList<PerfomanceReport> perfReps) {
		
		PerfomanceReport mainRpt = new PerfomanceReport();
		for (PerfomanceReport perfRpt : perfReps)
			mergeToMain(mainRpt, perfRpt);
		
		sortByTimeAfterMerge(mainRpt);
		return mainRpt;
	}

	public static void sortByTimeAfterMerge(PerfomanceReport mainRpt) {
		
		for (Operation op: mainRpt.getOperations())
			Collections.sort(op.getResponseTimes(), new Comparator<ResponseTime>() {
			    public int compare(ResponseTime rt1, ResponseTime rt2) {
			        return rt1.getTime().compareTo(rt2.getTime());
			    }
			});
	}

	private static void mergeToMain(PerfomanceReport mainRpt,
			PerfomanceReport perfRpt) {

		for (Operation op : perfRpt.getOperations()){
			Operation mop = mainRpt.findOperation(op.getName());
			if (mop == null){
				mainRpt.getOperations().add(op);
			} else {
				mop.getResponseTimes().addAll(op.getResponseTimes());
			}
		}
		
	}

}
