package org.kkonoplev.bali.restdemo;

import java.util.ArrayList;

import org.kkonoplev.bali.gridhub.GridProjectBuilder;
import org.kkonoplev.bali.gridhub.TreeBuilder;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.services.ProjectTests;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

public class RestReq {

	public static void main(String[] args) {
		
		ArrayList<ProjectTests> prTests = new ArrayList<ProjectTests>();
		ProjectTests prTests1 = new ProjectTests();
		prTests1.setProject("one");
		prTests1.getTests().add("demo1.CounterTest");
		prTests1.getTests().add("demo1.Demo1_JTest");
		prTests1.getTests().add("demo2.Demo1_JTest");
		prTests1.getTests().add("demo1.Demo2_JTest");
		prTests1.getTests().add("demo1.Demo3_JTest");
		prTests1.getTests().add("demo1.Demo4_JTest");
		
		prTests1.getTests().add("demo2.Demo2_JTest");

		prTests.add(prTests1);
				
		ProjectTests prTests2 = new ProjectTests();
		prTests2.setProject("two");
		prTests2.getTests().add("com.test.qw");
		prTests2.getTests().add("com.test.we");
		prTests2.getTests().add("com.test.er");
		prTests.add(prTests2);
		
		ProjectTests[] prTestsReq = getTests();
		TreeBuilder builder = new TreeBuilder();
		GridProjectBuilder prBuilder = new GridProjectBuilder();
		TreeNode rootNode = builder.build(prTestsReq[1].getTests());
		
		BaseProject bp = prBuilder.build(prTestsReq[1]);
		
		int a = 5;

	}
	
	public static ProjectTests[] getTests(){
		RestTemplate restTemplate = new RestTemplate();
		
		String resturl
		  = "http://localhost:8080/bali/form/status/projects/json";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(resturl, String.class);
		int a = 5;

		Gson gson = new Gson();
		String x = response.getBody();
		ProjectTests[] prTestsOut = gson.fromJson(x, ProjectTests[].class);
		System.out.println(gson.toJson(prTestsOut));
		
		return prTestsOut;
		
	}
	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		String resturl
		  = "http://localhost:8080/bali/form/status/projects/json";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(resturl, String.class);
		int a = 5;
		
		ArrayList<ProjectTests> prTests = new ArrayList<ProjectTests>();
		ProjectTests prTests1 = new ProjectTests();
		prTests1.setProject("one");
		prTests1.getTests().add("com.test.ab");
		prTests1.getTests().add("com.test.bc");
		prTests1.getTests().add("com.test.cd");
		prTests.add(prTests1);
				
		ProjectTests prTests2 = new ProjectTests();
		prTests2.setProject("two");
		prTests2.getTests().add("com.test.qw");
		prTests2.getTests().add("com.test.we");
		prTests2.getTests().add("com.test.er");
		prTests.add(prTests2);

		Gson gson = new Gson();
		int[] ints = {1, 2, 3, 4, 5};
		String[] strings = {"abc", "def", "ghi"};

		// Serialization
		System.out.println(gson.toJson(ints));     // ==> [1,2,3,4,5]
		System.out.println(gson.toJson(strings));  // ==> ["abc", "def", "ghi"]
		String json = gson.toJson(prTests);
		System.out.println(json);  // ==> ["abc", "def", "ghi"]

		// Deserialization
		int[] ints2 = gson.fromJson("[1,2,3,4,5]", int[].class);
		ProjectTests[] prTestsOut = gson.fromJson(json, ProjectTests[].class);
		
		//String newJson = "[{\\"name\\":\\"commons\\",\\"tests\\":[]},{\\"name\\":\\"junit\\",\\"tests\\":[{\\"test\\":\\"demo1.CounterTest\\"},{\\"test\\":\\"demo1.Demo1_JTest\\"},{\\"test\\":\\"demo2.Demo1_JTest\\"},{\\"test\\":\\"demo2.Demo2_JTest\\"},{\\"test\\":\\"demo2.Demo6_2res_JTest\\"}]},{\\"name\\":\\"main\\",\\"test\\":[{\\"test\\":\\"demo1.Demo1_TestMain\\"},{\\"test\\":\\"demo1.Demo2_TestMain\\"},{\\"test\\":\\"demo2.Demo1_TestMain\\"}]}]";
		String x = "[{'project':'junit', 'tests':['demo1.CounterTest','demo1.Demo1_JTest']},"+
		 "{'project':'main', 'tests':['demo1.Demo1_TestMain','demo1.Demo2_TestMain']}]";
		 
		x = response.getBody();
		ProjectTests[] prTestsOutN = gson.fromJson(x, ProjectTests[].class);
		System.out.println(gson.toJson(prTestsOutN));
		int f = 5;
		
		
	}

}
