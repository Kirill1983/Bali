/* 
 * Copyright © 2011 Konoplev Kirill
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

package org.kkonoplev.bali.services;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.gridhub.GridHubService;
import org.kkonoplev.bali.init.BaliProperties;


public class BaliServices {

	 private static final Logger LOG = Logger.getLogger(BaliServices.class);
	 private static BaliServices INSTANCE;
	 
	 private GridHubService gridHubSvc;
	 private SuiteService suiteSvc;
	 private ProjectService projectSvc;
	 private SchedulerService schedulerSvc;
	 
	 //disable constructor
	 private BaliServices() {
	 }
	 
	 public static synchronized void init() {
		 
		 try {
			 
		   LOG.info("initializing Bali Test Central services...");
	       INSTANCE = new BaliServices();
	       
	       INSTANCE.projectSvc = new ProjectService(BaliProperties.getBaliProjectsConfig());
	       INSTANCE.suiteSvc = new SuiteService();
	       INSTANCE.schedulerSvc = new SchedulerService();
	       INSTANCE.gridHubSvc = new GridHubService();
	       
	       LOG.info("initialized Bali Test Central services successfully"); 
	       
		 } catch (Exception ex) {
			 LOG.warn("error initializing repo services");
			 LOG.warn(ex, ex);
			 
			 try {
				 Thread.sleep(6500);
			 } catch (Exception e){
			 }
		 }
		 
	 }
	 
	 public static synchronized void dinit() throws Exception {
		 LOG.info("destroying bali services...");
		 if ( INSTANCE != null) {
			 
			 INSTANCE.schedulerSvc.dinit();
			 INSTANCE.suiteSvc.dinit();
			 INSTANCE.projectSvc.dinit();
			 
			 INSTANCE.suiteSvc = null;
			 INSTANCE.schedulerSvc = null;
			 INSTANCE = null;
		 }
		 
		 LOG.info("destroyed Bali services successfully");
		 
	 }
	 
	 
	 
	 public static SuiteService getSuiteService(){
		 return INSTANCE.suiteSvc;
	 }
	 
	 public static ProjectService getProjectService(){
		 return INSTANCE.projectSvc;
	 }
	 
	 public static SchedulerService getSchedulerService(){
		 return INSTANCE.schedulerSvc;
	 }
	 
	 public static GridHubService getGridHubService(){
		 return INSTANCE.gridHubSvc;
	 }    
	 
}
