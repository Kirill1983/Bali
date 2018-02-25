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


import java.io.File;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import org.kkonoplev.bali.init.BaliProperties;
import org.kkonoplev.bali.suiteexec.SuiteMdl;


/**

 */
public class SuiteScheduledJob implements Job {

    private static Logger log = Logger.getLogger(SuiteScheduledJob.class);

    
    public SuiteScheduledJob() {
    }

  
    public void execute(JobExecutionContext context)        throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();
        
        //filename of suite
        String filename = context.getJobDetail().getDescription();
        log.info("Starting suite:"+filename+" jobKey="+jobKey);
        
        SuiteMdl suiteMdl = new SuiteMdl();
        suiteMdl.setFilename(filename);
        SuiteService suiteSvc = BaliServices.getSuiteService();
        suiteSvc.loadSuiteMdlFromFile(BaliProperties.getBaliSuitesDir()+File.separator+filename, suiteMdl);
        
        try {
			suiteSvc.runSuite(suiteMdl);
		} catch (Exception e) {
			log.error(e);
		}
        
    }

}
