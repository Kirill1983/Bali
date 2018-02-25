package org.kkonoplev.bali.gridhub;

import org.kkonoplev.bali.common.perfomance.PerfomanceReport;

import com.google.gson.Gson;


public class RESTTry {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
//		 String res = RequestAPI.getURL("http://localhost:8080/bali/form/status/projects/json");
//	     
//	     Gson gson = new Gson();
//	     System.out.println(res);
//		 ProjectTests[] prTestsOut = gson.fromJson(res, ProjectTests[].class);
//		 System.out.println(gson.toJson(prTestsOut));
//	     
//		 Map<String, String> rargs = new HashMap<String, String>();
//		 rargs.put("options", "ENV_A");
//		 rargs.put("browser", "chrome");
//		 rargs.put("_loadmode", "on");
//		 rargs.put("capturemode", "true");
//		 rargs.put("_capturemode", "on");
//		 rargs.put("_debugmode", "on");
//		 rargs.put("loadminutes", "0");
//		 rargs.put("rumpup", "3000");
//		 rargs.put("tests", "junit#demo1.Demo1_JTest#1,junit#demo2.Demo1_JTest#1,");
//		 rargs.put("email", "");
//		 
//		 
//		 RequestAPI.postURL("http://localhost:8080/bali/form/pad/run", rargs);
//	
//		SuiteMdl suiteMdl = new SuiteMdl();
//		
//		suiteMdl.setTests("junit#demo1.Demo1_JTest#1,junit#demo2.Demo1_JTest#1,");
//		suiteMdl.setBrowser("chrome");
//		suiteMdl.setOptions("ENV_A");
//		
//		
//		
//		GridHubService ghSvc = new GridHubService();
//		ghSvc.runSuite(suiteMdl);
//		int a =5;
//		String res = RequestAPI.getURL("http://localhost:8080/bali/form/status/suitecontext/perf/json?resultDir=Feb_14_11_40_18_887_ENV_A_grid40");
//		Gson gson = new Gson();
//	    System.out.println(res);
//	    PerfomanceReport pr = gson.fromJson(res, PerfomanceReport.class);
//	    System.out.println(gson.toJson(pr));
	    
		
		System.out.println("2%3 "+(3 %3));
		int nodes = 2;
		int threads = 2;
		System.out.println((double)threads/nodes+0.49);
		int threadsPerNode = (int)Math.round(threads/nodes+0.49);
		System.out.println("per node = "+threadsPerNode);
		
		if (threadsPerNode == 0)
			threadsPerNode = 1;
		
		int threadsCnt = threads;
		for (int i = 1; i <= nodes; i++){
			int curNode = 0;
			
			if (i == nodes)
				curNode = threadsCnt;
			else 
				curNode = threadsPerNode;
			System.out.println(curNode);
			threadsCnt -= curNode;
			if (threadsCnt == 0)
				break;
		}
			}

}
