/* 

 * Copyright � 2011 Kirill Konoplev
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.kkono.bali.standalone;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.common.logger.LocalRunLogger;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.report.JUnitReportBuilder;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.SuiteExecContextBuilder;
import org.kkonoplev.bali.suiteexec.SuiteExecProcessor;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;

public class SuiteRunner {

	public enum SuiteTag {
		NAME("name"), DESC("description"), BROWSER("browser"), EMAIL("email"), CRON("cron"), OPTIONS("options"), TESTS(
				"tests"), RUMPUP("rumpup"), LOADMINUTES(
						"loadminutes"), LOADMODE("loadmode"), DEBUGMODE("debugmode"), CAPTUREMODE("capturemode");
		String name;
		private SuiteTag(String name_) {
			name = name_;
		}
		public String getName() {
			return name;
		}
	};

	protected static final Logger log = LogManager.getLogger(SuiteRunner.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		LocalRunLogger.initThreadsAndStdOutLogger();

		initSystemProps();

		String projectsXmlPath = (System.getenv("bali.projectsXml.path") != null)
				? System.getenv("bali.projectsXml.path") : "..\\conf\\projects_localrun.xml";
		String propsDir = (System.getenv("bali.props.dir") != null) ? System.getenv("bali.props.dir")
				: "..\\conf\\options\\";
		String reportDir = (System.getenv("bali.report.dir") != null) ? System.getenv("bali.report.dir")
				: "output\\";
		String suitePath = (System.getenv("bali.suite.path") != null) ? System.getenv("bali.suite.path")
				: "..\\conf\\suites\\webreview.conf";
		String threadsCount = (System.getenv("bali.threads.count") != null) ? System.getenv("bali.threads.count") : "2";
		String tags = (System.getenv("suite.run.tags") != null) ? System.getenv("suite.run.tags") : "";

		log.info("bali.projectsXml.path: " + projectsXmlPath);
		log.info("bali.props.dir: " + propsDir);
		log.info("bali.report.dir: " + reportDir);
		log.info("bali.suite.path: " + suitePath);
		log.info("bali.threads.count: " + threadsCount);
		log.info("suite.run.tags: " + tags);
		
		int threadsPoolSize = Integer.valueOf(threadsCount);
		Thread.sleep(1000);

		SuiteMdl suiteMdl = new SuiteMdl();
		loadSuiteMdlFromFile(suitePath, suiteMdl);

		ProjectService projectSvc = new ProjectService(projectsXmlPath);
		
		initTags(suiteMdl, tags);
		initTagsTest(projectSvc, suiteMdl);

		SuiteExecProcessor suiteExecProcessor = new SuiteExecProcessor(projectSvc, threadsPoolSize);
		SuiteExecContext suiteContext = (new SuiteExecContextBuilder()).build(suiteMdl, suiteExecProcessor, reportDir,
				propsDir, new HashMap<String, String>(0));
		suiteExecProcessor.addSuiteToJobQueque(suiteContext);
		suiteExecProcessor.notifyExecutors();

		do {
			Thread.sleep(3000);
		} while (!suiteContext.getCompleted());

		String junitXmlPath = reportDir + suiteContext.getResultDir() + File.separator + "junit.xml";
		JUnitReportBuilder.build(junitXmlPath, suiteContext);

		closeResources(projectSvc);

		log.info("Exit. suite completed.");
		System.exit(0);

	}

	private static void initTagsTest(ProjectService projectSvc, SuiteMdl suiteMdl) {
		if (suiteMdl.getTags().size()+suiteMdl.getDisabledTags().size() == 0){
			log.info("No tags defined. will use list of tests from conf file.");
			return;
		}
		log.info("Tags defined. will search tests to comply with.");
		StringBuilder tests = new StringBuilder();
		for (BaseProject bp : projectSvc.getProjects())
			addTestsFromProject(tests, bp.getRootFolderNode(), bp.getName(), suiteMdl);
		suiteMdl.setTests(tests.toString());
	}

	private static void addTestsFromProject(StringBuilder tests, TreeNode node, String name, SuiteMdl suiteMdl) {
		
		if (node == null)
			return;
		
		if (node.isLeaf() && nodeWithTags(node.getTags(), suiteMdl.getTags(), suiteMdl.getDisabledTags())){
			String test = name+"#"+node.getPath()+"#1,";
			log.info("Add test: "+test);
			tests.append(test);
		}
		else 
			for (TreeNode child : node.getChilds())
				addTestsFromProject(tests, child, name, suiteMdl);
		
	}

	private static boolean nodeWithTags(List<String> nodeTags, List<String> tags, List<String> disabledTags) {
		if (disabledTags.size() > 0 && nodeTags.size() == 0)
			return true;
		
		if (nodeTags.size() == 0)
			return false;
		
		for (String nodeTag : nodeTags)
			if (!tags.contains(nodeTag) || disabledTags.contains(nodeTag))
				return false;
		
		return true;
	}

	private static void initTags(SuiteMdl suiteMdl, String tags) {
		if (tags.equals(""))
			return;
		
		String[] tagsList = tags.split(",");
		for (String tag: tagsList){
			
			if (tag.startsWith("!")){
				suiteMdl.getDisabledTags().add(tag.substring(1));
				log.info("Exclude tests with tag: "+tag.substring(1));
			}
			else {
				suiteMdl.getTags().add(tag);
				log.info("Include tests with tag: "+tag);
			}
		}
		
	}

	private static void closeResources(ProjectService projectSvc) {
		log.info("Close used resources");
		for (int i = 0; i < projectSvc.getProjects().size(); i++)
			for (ResourcePool rp : projectSvc.getProjects().get(i).getResourceStorages())
				for (int j = 0; j < rp.getResources().size(); j++) {
					try {
						log.info("Close " + rp.getResources().get(j).getName());
						rp.getResources().get(j).close();
					} catch (Exception e) {
						log.error(e, e);
					}
				}

	}

	/*
	 * init variables for browsers 
	 */
	private static void initSystemProps() {
		
		log.info("---- init system properties ------ ");
		initProperty("webdriver.chrome.driver", "..\\drivers\\chromedriver.exe");
		initProperty("phantomjs.binary.path", "..\\drivers\\phantomjs.exe");
		initProperty("webdriver.ie.driver", "..\\drivers\\IEDriverServer.exe");
		initProperty("webdriver.gecko.driver", "..\\drivers\\geckodriver.exe");
		initProperty("webdriver.firefox.profile", "default");
		
	}

	private static void initProperty(String key, String def) {
		
		if (System.getProperty(key) == null){
			log.info("Sys.getProperty for "+key+" is not set");

			String val = def;
			if (System.getenv(key) == null){
				log.info("Sys.genenv for "+key+" is not set also, will use default value '"+def+"'");
			} else {
				log.info("System.genenv for "+key+" found, will use value '"+System.getenv(key)+"'");
				val = System.getenv(key);
			}
			System.setProperty(key, val);
		} else {
			log.info("Sys.getProperty for "+key+" is already set with '"+System.getProperty(key)+"'");
		}
		
	}

	public static void loadSuiteMdlFromFile(String srcFile, SuiteMdl suiteMdl) throws IOException {

		Properties props = new Properties();
		File file = new File(srcFile);
		FileReader fr = new FileReader(srcFile);
		props.load(fr);

		suiteMdl.setName(props.getProperty(SuiteTag.NAME.getName()));
		suiteMdl.setDescription(props.getProperty(SuiteTag.DESC.getName()));
		suiteMdl.setBrowser(props.getProperty(SuiteTag.BROWSER.getName()));
		suiteMdl.setEmail(props.getProperty(SuiteTag.EMAIL.getName()));
		suiteMdl.setCron(props.getProperty(SuiteTag.CRON.getName()));
		suiteMdl.setOptions(props.getProperty(SuiteTag.OPTIONS.getName()));
		suiteMdl.setTests(props.getProperty(SuiteTag.TESTS.getName()));
		suiteMdl.setRumpup(props.getProperty(SuiteTag.RUMPUP.getName()));
		suiteMdl.setLoadminutes(props.getProperty(SuiteTag.LOADMINUTES.getName()));
		suiteMdl.setLoadmode(props.getProperty(SuiteTag.LOADMODE.getName()).equals("true") ? true : false);
		suiteMdl.setDebugmode(props.getProperty(SuiteTag.DEBUGMODE.getName()).equals("true") ? true : false);
		suiteMdl.setCapturemode(props.getProperty(SuiteTag.CAPTUREMODE.getName()).equals("true") ? true : false);
		suiteMdl.setFilename(file.getName());

		fr.close();

	}

}
