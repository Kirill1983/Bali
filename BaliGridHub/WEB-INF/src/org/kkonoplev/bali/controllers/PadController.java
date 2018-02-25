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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.gridhub.GridHubService;
import org.kkonoplev.bali.gridhub.GridSuiteExecContext;
import org.kkonoplev.bali.init.BaliProperties;
import org.kkonoplev.bali.services.BaliServices;
import org.kkonoplev.bali.services.CommunityEdition;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.services.SchedulerService;
import org.kkonoplev.bali.services.SuiteService;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping(value="/form/pad")
public class PadController {
	
		private static final Logger log = Logger.getLogger(PadController.class);
	
		public static final String testsTile = "tests";
		public static final String jsonTile = "json";
		public static final String redirectviewrun = "redirect:/form/status/suitecontext";
		
		 
		@RequestMapping(value="/tests")
	    public String display(HttpServletRequest req) throws Exception {
			
			req.setAttribute("suiteMdl", new SuiteMdl());
			
			ProjectService projectSvc = BaliServices.getProjectService();		
			req.setAttribute("projects", projectSvc.getGridProjects());
			
		 	return testsTile;
	    }
		
		
		@RequestMapping(value="/json")
		public String json(HttpServletRequest req, HttpServletResponse res) {
			res.setContentType("application/x-javascript");
			req.setAttribute("my", "123");
			
		 	return jsonTile;
		}
		
		
		
		@RequestMapping(value="/run")
	    public String run(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteMdl") SuiteMdl suiteMdl) throws Exception {
			
			if (suiteMdl.getName().equals(""))
				suiteMdl.setName("grid"+RandomStringUtils.randomNumeric(2));		
			
			log.info("Tests: "+suiteMdl.getTests());
			log.info("Options: "+suiteMdl.getOptions());
			log.info("Browser: "+suiteMdl.getBrowser());
			
			log.info("Capture: "+suiteMdl.isCapturemode());
			log.info("Debug: "+suiteMdl.isDebugmode());
			log.info("Load:"+suiteMdl.getLoadmode());
			
			GridHubService gridhSvc = BaliServices.getGridHubService();
			
			GridSuiteExecContext gsuiteCtx = gridhSvc.runSuite(suiteMdl, BaliProperties.getBaliResultsDir());
			
//			log.info("path="+"http://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath());
//			suiteSvc.getSuiteExecProcessor().setBaseURL("http://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath());
			
			return redirectviewrun+"?resultDir="+gsuiteCtx.getResultDir();		
			
	    }
		
		@RequestMapping(value="/linkrun")
	    public String linkrun(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteMdl") SuiteMdl suiteMdl) throws Exception {
			
			SuiteService suiteSvc = BaliServices.getSuiteService();
			suiteSvc.loadDefaultMdl(suiteMdl);
			SuiteExecContext suiteExecContext = suiteSvc.runSuite(suiteMdl);
			
			return redirectviewrun+"?resultDir="+suiteExecContext.getResultDir();		
			
	    }
		
		@ModelAttribute("optionsList")
		public Map<String,String> populateOptionsList() {	 
			return BaliProperties.getOptionsList(BaliProperties.getBaliOptionsDir());
		}
		
		@ModelAttribute("browserList")
		public Map<String,String> populateBrowserList() {			
			return BaliProperties.getBrowsersList();
		}
		
		public static final String suitesTile = "suites";
		public static final String redirectSchedules = "redirect:/form/pad/suites";		
			

		@RequestMapping(value="/suites")
	    public String display(HttpServletRequest req, @ModelAttribute("SuiteMdl") SuiteMdl suiteMdl) throws Exception {			
		
			SuiteService suiteSvc = BaliServices.getSuiteService();
			suiteSvc.loadDefaultMdl(suiteMdl);

			req.setAttribute("suiteMdl", suiteMdl);
				 	
			ProjectService projectSvc = BaliServices.getProjectService();
			req.setAttribute("projects", projectSvc.getProjects());
			
		 	return suitesTile;
		 	
	    }
				
		
		@RequestMapping(value="/suites", method=RequestMethod.POST)
	    public String view(HttpServletRequest req, @ModelAttribute("SuiteMdl") SuiteMdl suiteMdl) throws Exception {			
			return redirectSchedules+"?filename="+suiteMdl.getFilename();		
		}
		
		
		@RequestMapping(value="/suites/save")
	    public String save(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("SuiteMdl") SuiteMdl suiteMdl) throws Exception {
			
			int suitesCount = BaliServices.getSuiteService().getSuitesList().size();
			boolean isNew = (!BaliServices.getSuiteService().getSuitesList().containsValue(suiteMdl.getName()));
			
			if (CommunityEdition.isEnabled() && suitesCount >= CommunityEdition.MAX_SUITES && isNew){

				String msg = "Community edition has limit up to "+CommunityEdition.MAX_SUITES+" suites! <BR> &nbsp; Please <a href='mailto:kirill-konoplev@yandex.ru'> contact author </a> for full version.";
				
				SuiteService suiteSvc = BaliServices.getSuiteService();
				suiteSvc.loadDefaultMdl(suiteMdl);
				ProjectService projectSvc = BaliServices.getProjectService();

				req.setAttribute("suiteMdl", suiteMdl);
				req.setAttribute("msg", msg);
				req.setAttribute("projects", projectSvc.getProjects());

				return suitesTile;
				
			} 
			
			SuiteService suiteSvc = BaliServices.getSuiteService();
			suiteSvc.saveSuite(suiteMdl);		
			
			SchedulerService scheduleSvc = BaliServices.getSchedulerService();
			scheduleSvc.scheduleSuite(suiteMdl);	
			
			req.setAttribute("suiteMdl", suiteMdl);					
			return redirectSchedules+"?filename="+suiteMdl.getFilename();  			
	    }
		
		@RequestMapping(value="/suites/delete")
	    public String delete(HttpServletRequest req, @ModelAttribute("SuiteMdl") SuiteMdl suiteMdl) throws Exception {
	
			SuiteService suiteSvc = BaliServices.getSuiteService();
			suiteSvc.deleteSuite(suiteMdl);	
			
			SchedulerService scheduleSvc = BaliServices.getSchedulerService();
			scheduleSvc.deleteSchedule(suiteMdl);
			scheduleSvc.printInfo();
			
			return redirectSchedules;			
	    }
		
	
		@ModelAttribute("schedulesList")
		public Map<String,String> populateshedulesList() {			
			return BaliServices.getSuiteService().getSuitesList();			
		}
	
		
		

}
