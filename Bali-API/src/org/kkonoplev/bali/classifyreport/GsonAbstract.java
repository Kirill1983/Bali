package org.kkonoplev.bali.classifyreport;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class GsonAbstract {
	
	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
//		SuiteExecContext ctx = SuiteExecContext.deserialize("C:\\projects\\Bali\\results\\Feb_14_17_18_19_623_ENV_A_grid87\\suiteExecContext.txt");
//		System.out.println(ctx.getClassifyReport().getWarnList().size());
//		
//		String res = gson.toJson(ctx.getClassifyReport());
//		System.out.println(res);
//		ClassifyReport pr = gson.fromJson(res, ClassifyReport.class);
		
		RuntimeTypeAdapterFactory<BaseA> adapter = 
                RuntimeTypeAdapterFactory
               .of(BaseA.class, "typeparam")
               .registerSubtype(AnchorA.class, "a")
			   .registerSubtype(AnchorB.class, "b");

		BaseA a = new AnchorA("newVar", "str");
		List<BaseA> lst = new ArrayList<BaseA>();
		lst.add(new AnchorA("a", "a2"));
		lst.add(new AnchorB("b", "b2"));
		BoxBase boxb = new BoxBase(lst, a);
		
		
		Gson gson2=new GsonBuilder().setPrettyPrinting().registerTypeAdapterFactory(adapter).create();
		
		String res = gson2.toJson(boxb);
		System.out.println(res);
		BoxBase boxba = gson2.fromJson(res, BoxBase.class);
		System.out.println(gson.toJson(boxba));
		int w =5 ;  
		
			
		
//		int a = 5;
//		String[] suiteDirs = new String[]{"Feb_14_14_51_32_176_ENV_A_grid55", "Feb_14_14_51_32_741_ENV_A_grid55"};
//		
//		Gson gson = new Gson();
//		for (String suiteDir : suiteDirs){
//			System.out.println("-----");
//			String res = RequestAPI.getURL("http://localhost:8080/bali/form/status/suitecontext/classifyreport/json?resultDir="+suiteDir);
//		    System.out.println(res);
//		    ClassifyReport pr = gson.fromJson(res, ClassifyReport.class);
//		    System.out.println(gson.toJson(pr));
//		    
//		}
//		
	}

}
