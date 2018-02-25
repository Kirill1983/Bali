/* 
 * Copyright � 2011 Konoplev Kirill
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
 */

package org.kkonoplev.bali.controllers;


import java.io.File;
import java.lang.Thread.State;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.MergeClassifyReports;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.common.perfomance.PerfomanceReport;
import org.kkonoplev.bali.gridhub.GridHubService;
import org.kkonoplev.bali.gridhub.GridSuiteExecContext;
import org.kkonoplev.bali.init.BaliProperties;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.services.BaliServices;
import org.kkonoplev.bali.services.CommunityEdition;
import org.kkonoplev.bali.services.PluggableSuiteProcessorThread;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.services.SchedulerService;
import org.kkonoplev.bali.services.SuitePluggableProcessor;
import org.kkonoplev.bali.services.SuitePluggableProcessorStorage;
import org.kkonoplev.bali.services.SuiteService;
import org.kkonoplev.bali.services.utils.BuildRunnerThread;
import org.kkonoplev.bali.services.utils.ReaderUtil;
import org.kkonoplev.bali.suiteexec.RerunOnNewEnvsInfo;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.SuiteExecProcessor;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextState;
import org.kkonoplev.bali.suiteexec.TestExecutor;
import org.kkonoplev.bali.suiteexec.resource.InitResourcesThread;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping(value="/form/status")
public class StatusController {
	
	private static final Logger log = Logger.getLogger(StatusController.class);
	
	public static final String activitystatusTile = "activitystatus";
	public static final String scenarioTile = "scenariostatus";
	
	
	public static final String resultsstatusTile = "resultsstatus";
	public static final String summaryreportTile = "summaryreport";		
	
	public static final String execstatusTile = "executorsstatus";
	public static final String execstatusRefreshTile = "executorsstatusrefresh";
	
	public static final String resourcesstatusTile = "resourcesstatus";
	public static final String resourcesstatusnewTile = "resourcesstatusnew";
	public static final String resourcesstatusRefreshTile = "resourcesstatusrefresh";
	public static final String resourcesstatusjsonTile = "resourcesstatusjson";
	
	
	public static final String cronstatusTile = "cronstatus";
	
	public static final String targetgridstatusTile = "targetgridstatus";
	public static final String projectsstatusTile = "projectsstatus";
	
	public static final String nodesstatusTile = "gridnodesstatus";
	
	public static final String suiteContextTile = "suitecontextstatus";
	public static final String perfomanceReportTile = "perfomancereport";
	
	public static final String suiteContextNewTile = "suitecontextstatusnew";
	
	public static final String suiteContextRefreshTile = "suitecontextstatusrefresh";
	public static final String suiteContextJsonTile = "suitecontextstatusjson";
	
	public static final String emailSuiteContextTile = "suitecontextemailstatus";

	private static final String redirectsuitecontextrefresh = "redirect:/form/status/suitecontext/refresh?resultDir=";
	private static final String redirectActivityStatus = "redirect:/form/status/activity";
	

		@RequestMapping(method=RequestMethod.GET)
	    public String display(HttpServletRequest req) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();
			SchedulerService schedulerSvc = BaliServices.getSchedulerService();
			
			req.setAttribute("execContexts", suiteSvc.getSuiteExecProcessor().getSuiteExecContexts());
			req.setAttribute("scheduledSuiteList", schedulerSvc.getScheduledSuiteList());
		 	return execstatusTile;		 	
	    }
		
		@ModelAttribute("optionsList")
		public Map<String,String> populateOptionsList() {	 
			return BaliProperties.getOptionsList(BaliProperties.getBaliOptionsDir());
		}

		
		@RequestMapping(value="/activity")
	    public String activityresults(HttpServletRequest req) throws Exception {	
			
			GridHubService gridSvc = BaliServices.getGridHubService();			
			req.setAttribute("gridExecContexts", gridSvc.getgsuiteExecContexts());

		 	return activitystatusTile;		
		}
		
		@RequestMapping(value="/results")
	    public String resultsstatus(HttpServletRequest req) throws Exception {
			req.setAttribute("history", "");
			req.setAttribute("lasttenlist", BaliServices.getSuiteService().getLastTenGridSuiteContexts());
		 	return resultsstatusTile;
	    }
		
		@RequestMapping(value="/summaryreport")
	    public String summaryreport(HttpServletRequest req) throws Exception {
			
			String reportList = req.getParameter("reportlist");
			req.setAttribute("reportList", reportList);
		 	return summaryreportTile;
	    }
		
		@RequestMapping(value="/executors")
	    public String execstatus(HttpServletRequest req) throws Exception {	
			req.setAttribute("threadsCount", BaliServices.getSuiteService().getSuiteExecProcessor().getTestExecutors().size());
			return execstatusTile;		
		}
		
		@RequestMapping(value="/executors/update")
	    public String execupdate(HttpServletRequest req) throws Exception {
			int threadsCount = Integer.valueOf(req.getParameter("newThreadsCount"));
			String msg = "";
			if (CommunityEdition.isEnabled() && threadsCount > CommunityEdition.MAX_THREADS){
				threadsCount = CommunityEdition.MAX_THREADS;
				msg = "Community edition has limit up to "+CommunityEdition.MAX_THREADS+" threads! Please <a href='mailto:kirill-konoplev@yandex.ru'> contact author </a> for full version.";
			}
			
			SuiteExecProcessor execProcessor = BaliServices.getSuiteService().getSuiteExecProcessor();
			synchronized (this){
				execProcessor.checkAddThreads(threadsCount);
			}
			req.setAttribute("threadsCount", execProcessor.getTestExecutors().size());
			req.setAttribute("msg", msg);
			return execstatusTile;		
		}
		
		@RequestMapping(value="/executors/refresh")
	    public String execstatusrefresh(HttpServletRequest req) throws Exception {	
			
			SuiteService suiteSvc = BaliServices.getSuiteService();
			req.setAttribute("execProcessor", suiteSvc.getSuiteExecProcessor());
			
		 	return execstatusRefreshTile;		
		}
		
		@RequestMapping(value="/resources")
	    public String resourcesstatus(HttpServletRequest req) throws Exception {		 	
		 	return resourcesstatusTile;		
		}
		
		
		@RequestMapping(value="/resourcesnew")
	    public String resourcesstatusnew(HttpServletRequest req) throws Exception {		 	
		 	return resourcesstatusnewTile;		
		}
		@RequestMapping(value="/resources/refresh")
	    public String resourcesstatusrefresh(HttpServletRequest req) throws Exception {		
			
			SuiteService suiteSvc = BaliServices.getSuiteService();
			req.setAttribute("projects", suiteSvc.getSuiteExecProcessor().getProjectSvc().getProjects());
		 	return resourcesstatusRefreshTile;		
		}
		
		@RequestMapping(value="/resources/json")
	    public String resourcesstatusjson(HttpServletRequest req) throws Exception {		
			
			SuiteService suiteSvc = BaliServices.getSuiteService();
			req.setAttribute("projects", suiteSvc.getSuiteExecProcessor().getProjectSvc().getProjects());
		 	return resourcesstatusjsonTile;		
		}


			
		@RequestMapping(value="/resources/update")
	    public String update(HttpServletRequest req) throws Exception {
			
			ProjectService projectSvc = BaliServices.getProjectService();
			String projectname = req.getParameter("projectname");
			log.info("project="+projectname);
			BaseProject proj = projectSvc.getProject(projectname);
			
			if (proj == null){
				log.warn("project with name "+projectname+" not found.");
				return resourcesstatusRefreshTile;
			}
			
			String resourceclassname = req.getParameter("resourceclassname");
			log.info("resourceclassname="+resourceclassname);
			ResourcePool storage = projectSvc.findStorage(proj, resourceclassname);
			
			if (storage == null){
				log.warn("Storage with resource class "+resourceclassname+" not found in project"+projectname);
				return resourcesstatusRefreshTile;
			}
			
			String loadcmd = req.getParameter("loadcmd");
			log.info("loadcmd="+loadcmd);
			
			String clearmatch = req.getParameter("clearmatch");
			log.info("clearmatch="+clearmatch);
			
			String init = req.getParameter("init");
			log.info("init="+init);
			
			if (loadcmd != null)
				storage.doLoadCmd(loadcmd);
			
			if (clearmatch!= null)
				storage.doClearCmd(clearmatch);
			
			if (init != null){
				
				int tCount = BaliProperties.getInitExecutorCount();
				int resSize = storage.getResources().size();
				
				if (tCount > resSize)
					tCount = resSize;
				
				double section = (double) resSize/tCount;
				
				for (int i = 0; i < tCount; i++){
					InitResourcesThread initThread = new InitResourcesThread("thread "+i, init, storage.getResources(), 
							(int)Math.ceil(i*section), 
							(int)Math.ceil((i+1)*section-1));
					initThread.start();
				}
				
			}
			
						
			return resourcesstatusRefreshTile;		
		}
		
		@RequestMapping(value="/cron")
	    public String cronstatus(HttpServletRequest req) throws Exception {	
			
			SchedulerService schedulerSvc = BaliServices.getSchedulerService();
			req.setAttribute("scheduledSuiteList", schedulerSvc.getScheduledSuiteList());
		 	return cronstatusTile;		
		}
		
		@RequestMapping(value="/targetgrid")
	    public String targetgridstatus(HttpServletRequest req, @ModelAttribute("SuiteMdl") SuiteMdl suiteMdl) throws Exception {
			
			log.info("Target grid:"+suiteMdl.getOptions());			
			
			req.setAttribute("suiteMdl", suiteMdl);			
			
			SuiteService suiteSvc = BaliServices.getSuiteService();
			
			Properties props = suiteSvc.loadSuiteProperties(suiteMdl);
			req.setAttribute("props", props.entrySet());
		 
		 	return targetgridstatusTile;		
		}
		
		
		@RequestMapping(value="/projects")
	    public String projectsstatus(HttpServletRequest req) throws Exception {
			ProjectService projectSvc = BaliServices.getProjectService();
			req.setAttribute("projects", projectSvc.getProjects());		
		 	return projectsstatusTile;		
		}

		@RequestMapping(value="/gridnodes")
	    public String nodesstatus(HttpServletRequest req) throws Exception {
			
			GridHubService gridSvc = BaliServices.getGridHubService();
			req.setAttribute("gridHub", gridSvc.getGridHub());
		 	return nodesstatusTile;		
		}


		@RequestMapping(value="/projects/infoupdate", method = RequestMethod.POST)
	    public String projectInfoUpdateReload(HttpServletRequest req) throws Exception {
			
			ProjectService projectSvc = BaliServices.getProjectService();
			
			String projectname = "";
			if (req.getParameter("projectname") != null){
				projectname = req.getParameter("projectname");
			} else {
				
				log.warn("projectname is not set in request");
				return projectsstatusTile;
			}

			String content = ReaderUtil.getStringSource(req.getReader());
			projectSvc.updateInfo(projectname, content);
			
			return projectsstatusTile;
			
		}

		
		@RequestMapping(value="/projects/reload")
	    public String projectsstatus(HttpServletRequest req, @ModelAttribute("Project") BaseProject project) throws Exception {
			
			ProjectService projectSvc = BaliServices.getProjectService();
			projectSvc.reload(project.getName());
			
			req.setAttribute("projects", projectSvc.getProjects());		
		 	return projectsstatusTile;		
		}
		
		@RequestMapping(value="/projects/buildrun")
	    public String projectbuildrun(HttpServletRequest req, @ModelAttribute("Project") BaseProject proj) throws Exception {
			
			ProjectService projectSvc = BaliServices.getProjectService();
			BaseProject project = projectSvc.getProject(proj.getName());
			
			BuildRunnerThread buildRunnerThread = new BuildRunnerThread(project.getBuildConfig());
			buildRunnerThread.start();
			
			req.setAttribute("projects", projectSvc.getProjects());		
		 	return projectsstatusTile;		
		}
		
		
		
		@RequestMapping(value="/suitecontext")
	    public String suiteContext(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("GridSuiteExecContext") GridSuiteExecContext gsuiteExecContext) throws Exception {
			
			log.info("Open gsuite: "+gsuiteExecContext.getResultDir());
			GridSuiteExecContext ctx = BaliServices.getSuiteService().getGridSuiteExecContext(gsuiteExecContext.getResultDir());
			if (ctx == null)
				log.info("NULL not found gsuite context");
			else
				log.info("found gsuite context");
			
			req.setAttribute("gsuiteExecContext", ctx);
			
			
			return suiteContextTile;		 	
		}
		
		@RequestMapping(value="/perfomancereport")
	    public String perfomanceReport(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("GridSuiteExecContext") GridSuiteExecContext gsuiteExecContext) throws Exception {

			GridSuiteExecContext ctx = BaliServices.getSuiteService().getGridSuiteExecContext(gsuiteExecContext.getResultDir());
			PerfomanceReport pr = BaliServices.getGridHubService().genUnitedPerfReport(ctx.getGsuiteMdl());
			req.setAttribute("perfomanceReport", pr);
			req.setAttribute("resultDir", gsuiteExecContext.getResultDir());

			
			return perfomanceReportTile;		 	
		 }

		@RequestMapping(value="/classifyreport")
	    public String classifyReport(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("GridSuiteExecContext") GridSuiteExecContext gsuiteExecContext) throws Exception {

			
			GridSuiteExecContext ctx = BaliServices.getSuiteService().getGridSuiteExecContext(gsuiteExecContext.getResultDir());
			log.info("gsuiteExecContext.getResultDir() "+gsuiteExecContext.getResultDir());
			log.info("BaliProperties.getBaliResultsDir() "+BaliProperties.getBaliResultsDir());
			
			log.info("ctx = "+ctx);
			log.info("ctx getSuiteMdl "+ctx.getGsuiteMdl());
			
			
			ClassifyReport rpt = MergeClassifyReports.genUnitedClassifyReport(ctx.getGsuiteMdl(), BaliProperties.getBaliResultsDir()+File.separator+gsuiteExecContext.getResultDir()+File.separator);
			ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(rpt);
			ctx.setClassifyReport(rpt);
			res.getWriter().write(classifyReportBuilder.save());
			
			return  null; //"redirect:/results/"+ctx.getResultDir()+"/classifyNG.html";		 	
		 }
		
		@RequestMapping(value="/suitecontextnew")
	    public String suiteContextNew(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext ctx = suiteSvc.getSuiteExecContext(suiteExecContext.getResultDir());
			req.setAttribute("suiteExecContext", ctx);
			
		 	return suiteContextNewTile;		 	
		 }
		
		@RequestMapping(value="/suitecontext/refresh")
	    public String suiteContextRefresh(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext ctx = suiteSvc.getSuiteExecContext(suiteExecContext.getResultDir());
			System.out.println("completed="+ctx.getCompleted());
			req.setAttribute("suiteExecContext", ctx);
			
		 	return suiteContextRefreshTile;
		 	
		 }
		
		@RequestMapping(value="/suitecontext/json")
		 public String suiteContextJson(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext ctx = suiteSvc.getSuiteExecContext(suiteExecContext.getResultDir());
			req.setAttribute("suiteExecContext", ctx);			
		 	
			return suiteContextJsonTile;
		}
		
		@RequestMapping(value="/suitecontext/addthreads")
		 public String suiteContextAddThreads(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext ctx = suiteSvc.getSuiteExecContext(suiteExecContext.getResultDir());
			
			String threads = req.getParameter("threads");
			int threadCount = Integer.valueOf(threads);
			
			suiteSvc.addNewThreads(ctx, threadCount);
			
			String rumpup = ctx.getSuiteMdl().getRumpup();
			int rumpupms = Integer.valueOf(rumpup);
			suiteSvc.getSuiteExecProcessor().notifyExecutors(rumpupms);

			return null;
			
		}
		
		@RequestMapping(value="/suitecontext/addloadtime")
		 public String suiteContextAddTime(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext ctx = suiteSvc.getSuiteExecContext(suiteExecContext.getResultDir());
			
			String time = req.getParameter("time");
			int timeMins = Integer.valueOf(time);
			ctx.setLoadminutes(ctx.getLoadminutes()+timeMins);

			return null;
			
		}
		
		
		
		@RequestMapping(value="/suitecontextemail")
	    public String email(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
						
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext ctx = suiteSvc.getSuiteExecContext(suiteExecContext.getResultDir());
			System.out.println("completed="+ctx.getCompleted());
			req.setAttribute("suiteExecContext", ctx);
			
		 	return emailSuiteContextTile;
		 	
		} 	
		
		@RequestMapping(value="/exportresults")
		 public String exportResults(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			

			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext suiteContext = suiteSvc.getSuiteExecContext(suiteExecContext.getResultDir());
			
			String projectName = suiteContext.getTestExecContexts().get(0).getProjectName();
			BaseProject project = BaliServices.getProjectService().getProject(projectName);
		
			if (project.getPluggableProcessorStore().isEmpty())
				return null;
			
			if (suiteContext.getUnderExporting())
				return null; 
			
			SuitePluggableProcessorStorage exporter = project.getPluggableProcessorStore();
			String metaData = req.getParameter("metaData");
			String className = req.getParameter("className");
			if (className == null)
				className = "";
			
			suiteContext.setExporterMetaData(metaData);
			SuitePluggableProcessor pluggableProcessor = exporter.getSuitePluggableProcessor();
			
			PluggableSuiteProcessorThread thread = new PluggableSuiteProcessorThread(pluggableProcessor, suiteContext, className);
			thread.start();
			
			return null;
			
		}

		@RequestMapping(value="/rerunonenvs")
	    public String rerunOnNewEnvs(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("RerunNewEnvsInfo") RerunOnNewEnvsInfo rerunOnNewEnvsInfo) throws Exception {
			
			log.info("Rerunnewenvs"+rerunOnNewEnvsInfo.toString());
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			suiteSvc.rerunOnNewEnvironments(rerunOnNewEnvsInfo);	
			suiteSvc.getSuiteExecProcessor().notifyExecutors();
	
			return redirectActivityStatus;
		}
		
		@RequestMapping(value="/rerun")
	    public String rerun(HttpServletRequest req, HttpServletResponse res) throws Exception {
			
			String warningsIds = req.getParameter("warningIDs");
			String resDir = req.getParameter("resultDir");
			BaliServices.getGridHubService().rerunWarnings(resDir, warningsIds);	
			
			return "redirect:/form/status/classifyreport?resultDir="+resDir;
		}
		
		@RequestMapping(value="/update")
	    public String update(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("TestExecContext") TestExecContext testExecContextBean) throws Exception {
						
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			SuiteExecContext suiteExecContext = suiteSvc.getSuiteExecContext(req.getParameter("resultDir"));
			req.setAttribute("suiteExecContext", suiteExecContext);
			
			TestExecContext testExecContext = suiteExecContext.findByClassNameThreadId(testExecContextBean.getClassName(), testExecContextBean.getThreadId());
			
			if (testExecContext == null)
				return suiteContextRefreshTile;
				
			if (req.getParameter("debugMode") != null)
				testExecContext.setDebugMode(testExecContextBean.getDebugMode());
			
			if (req.getParameter("needStop") != null)
				testExecContext.setNeedStop(testExecContextBean.getNeedStop());
			
			if (req.getParameter("captureMode") != null)
				testExecContext.setCaptureMode(testExecContextBean.getCaptureMode());
			
				
			if (req.getParameter("testExecutorCmd") != null){
				
				String cmd = req.getParameter("testExecutorCmd");
				if (cmd.equalsIgnoreCase("play")){
					TestExecutor testExecutor =  testExecContext.getTestExecutor();
					testExecutor.notifyMonitorDirectly();				
				}
				
			}
			
			if (req.getParameter("suiteExecutorProcCmd") != null){
				
				String cmd = req.getParameter("suiteExecutorProcCmd");
				if (cmd.equalsIgnoreCase("replay")){
															
		
					suiteSvc.getSuiteExecProcessor().rerunTestExecContext(testExecContext);		
					suiteSvc.getSuiteExecProcessor().notifyExecutors();
					
										
				} else
				if (cmd.equalsIgnoreCase("interrupt")){
					TestExecutor testExecutor = testExecContext.getTestExecutor();
					
					boolean b = (testExecutor != null) && (testExecContext.getStatus().equals(TestExecContextState.STARTED));
					if (b){
						//testExecContext.interrupt();
						testExecContext.setStatus(TestExecContextState.THREAD_STOP);
						try {
							testExecutor.stop();
							Thread.sleep(200);
							//testExecutor.interrupt();
							if (testExecutor.getState() == State.TERMINATED){
								testExecContext.setStatus(TestExecContextState.TERMINATED);
								testExecContext.onFinished();
								suiteSvc.getSuiteExecProcessor().replaceWithNew(testExecutor.getName());
							}
							
						} catch (Exception e){
							log.error(e, e);
						}
					}
				}
				
			}
			
			/*
			String redirect = req.getParameter("redirect");
			if (redirect !=null){
				System.out.println("redirect after command:"+redirect);
				return "redirect:"+redirect;
			}
			*/
						
			return null;		
		}

		
		@RequestMapping(value="/cancelcontext")
	    public String cancelcontext(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			suiteSvc.getSuiteExecProcessor().cancelExecution(suiteExecContext.getResultDir());	
			return redirectsuitecontextrefresh+suiteExecContext.getResultDir();
			
	    }
		
		@RequestMapping(value="/softstopcontext")
	    public String softstopcontext(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			suiteSvc.getSuiteExecProcessor().softStopExecution(suiteExecContext.getResultDir());	
			return redirectsuitecontextrefresh+suiteExecContext.getResultDir();
			
	    }

		
		@RequestMapping(value="/deletecontext")
	    public String deletecontext(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteExecContext") SuiteExecContext suiteExecContext) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();	
			suiteSvc.deleteSuiteContext(suiteExecContext);	
			req.setAttribute("suiteExecContext", suiteSvc.getLastTenGridSuiteContexts().get(0));			
		 	return suiteContextRefreshTile;
		 	
	    }
		
		@ModelAttribute("historyList")
		public Map<String,String> populateHistoryList() {
			return BaliServices.getSuiteService().getHistoryList();
		}

}
