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

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;



/*
 * schedule runs
 */
public class SchedulerService {
	
	Scheduler scheduler;
	
	Logger log = Logger.getLogger(SchedulerService.class);
	public static String GROUP_ID="BALI";
	
	
	public SchedulerService() throws Exception{
		
		System.setProperty("org.quartz.threadPool.threadCount", "2");
		
		SchedulerFactory sf = new StdSchedulerFactory();
	
		scheduler = sf.getScheduler();	

		
		log.info("Starting scheduler.");
		scheduler.start();
       
        log.info("------- Scheduler Initialization Completed. --------");
        
        scheduleAllSuites();
        
	}
	
	public void scheduleAllSuites() throws Exception{
		
		log.info("------- Schedule All Suites Started. --------");
		
		SuiteService suiteSvc = BaliServices.getSuiteService();
		File[] files = suiteSvc.getSuitesFileList();
		
		for (File f: files){
			SuiteMdl suiteMdl = new SuiteMdl();
			suiteSvc.loadSuiteMdlFromFile(f, suiteMdl);
			scheduleSuite(suiteMdl);
		}
		
		log.info("------- Schedule All Suites Finished. --------");
		
	}
	
	public void dinit() throws Exception{
		
		log.info("------- Shutting Down Scheduler ---------------------");
	    scheduler.shutdown(true);

	    log.info("------- Shutdown Complete -----------------");

	    SchedulerMetaData metaData = scheduler.getMetaData();
	    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
        
	}
	
	public void scheduleSuite(SuiteMdl suiteMdl) throws Exception {
		
		log.info("Suite for shchedule:"+suiteMdl);
		log.info("CRON:"+suiteMdl.getCron());
		
		if (suiteMdl.getCron().equals("")){
			JobKey jobKey = hasAssignedSchedule(suiteMdl);
			
			if (jobKey != null)
				deleteSchedule(jobKey);
			
			return;
		}
		
		try {
			JobDetail job = newJob(SuiteScheduledJob.class)
        		.withIdentity(suiteMdl.getFilename(), GROUP_ID)
        		.withDescription(suiteMdl.getFilename())
        		.build();
    
			
			
			CronTrigger trigger = newTrigger()
				.withIdentity(suiteMdl.getFilename()+"_trigger", GROUP_ID)
				.withSchedule(cronSchedule(suiteMdl.getCron()))
				.build();
			Date ft = new Date();
    
			// if already assigned
			if (scheduler.getJobDetail(job.getKey()) != null){
				
				CronTrigger ct = (CronTrigger) scheduler.getTriggersOfJob(job.getKey()).get(0);
				if (!ct.getCronExpression().equals(trigger.getCronExpression())){
					ft = scheduler.rescheduleJob(trigger.getKey(), trigger);
					log.info(job.getKey() + " has been Rescheduled to run at: " + ft
			            + " and repeat based on expression: "
			            + trigger.getCronExpression());
				} 
			
			} else {
				ft = scheduler.scheduleJob(job, trigger);
				log.info(job.getKey() + " has been scheduled to run at: " + ft
						+ " and repeat based on expression: "
		     	      + trigger.getCronExpression());
			}
		} catch (Exception e){
			log.error(e);
		}
		
		printInfo();
		
	}
	
	
	public JobKey hasAssignedSchedule(SuiteMdl suiteMdl){
		
		try {
			
			JobDetail job = newJob(SuiteScheduledJob.class)
        		.withIdentity(suiteMdl.getFilename(), GROUP_ID)
        		.withDescription(suiteMdl.getFilename())
        		.build();    
						
			
			if (scheduler.getJobDetail(job.getKey()) != null)				
				return job.getKey();
				
			
		} catch (Exception e){
			log.error(e);
		}
		
		return  null;		
	}
	
	public void deleteSchedule(SuiteMdl suiteMdl) throws Exception {		
	
		log.info("Deleting suite schedule if it exists: "+suiteMdl);

		JobKey jobKey = hasAssignedSchedule(suiteMdl);
		
		if (jobKey != null){
			deleteSchedule(jobKey);
			printInfo();
		}
		
	}
	
	public void deleteSchedule(JobKey jobKey) throws Exception {			
	
		try {			
			
			if (scheduler.getJobDetail(jobKey) != null){					
				scheduler.deleteJob(jobKey);
				log.info("Deleted schedule: "+jobKey);				
			} else {
				log.info("Will not delete, dont find job with key="+jobKey);
			}
				
				
		} catch (Exception e){
			log.error(e);
		}
				
	}
	
	
	public void printInfo() throws SchedulerException {
		
		log.info(" Job List");
		
		Iterator<JobKey> it = scheduler.getJobKeys(GroupMatcher.groupEquals(GROUP_ID)).iterator();
		while (it.hasNext()){
			JobKey key = it.next();
			JobDetail job = scheduler.getJobDetail(key);
		
			log.info("Current job:"+key.getGroup()+"."+key.getName());
			log.info("trigger: "+scheduler.getTriggersOfJob(key).get(0));
					
		
		}
		
		log.info(" Job End");
	}
	
	public HashMap<String, String> getScheduledSuiteList() throws Exception {
		
		HashMap<String, String> ls = new LinkedHashMap<String, String>(20);
		
		Iterator<JobKey> it = scheduler.getJobKeys(GroupMatcher.groupEquals(GROUP_ID)).iterator();
		while (it.hasNext()){
			JobKey key = it.next();
			JobDetail job = scheduler.getJobDetail(key);
		
			CronTrigger ct = (CronTrigger) scheduler.getTriggersOfJob(job.getKey()).get(0);
			String suite = key.getName();
			String nextCron = "cron: "+ct.getCronExpression()+", next Run: "+scheduler.getTriggersOfJob(key).get(0).getNextFireTime().toString().substring(0,19);
			
			log.info(suite+" -> "+nextCron);
			ls.put(suite, nextCron);			
		}
		
		
		return ls;
	}
	
	

}
