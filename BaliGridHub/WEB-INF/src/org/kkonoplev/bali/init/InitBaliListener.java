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

package org.kkonoplev.bali.init;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.kkonoplev.bali.services.BaliServices;


public class InitBaliListener implements ServletContextListener {

    private static final String WEBAPP_START_TIME = "webappStartTime";
    private static final Logger LOG = Logger.getLogger(InitBaliListener.class);

    /**
     * Notification that the web application is ready to process requests.
     * @param event an instance of {@link ServletContextEvent}.
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
        	
            BaliLog4j.init();
            LOG.info("loading ac app...");
            ServletContext context = event.getServletContext();
            setWebappStartTime(context, new Date());
            boolean success = BaliProperties.init();
            if (success) {
                LOG.info(BaliProperties.print());
            }  
            
            
            BaliServices.init();            
          
        } catch (Throwable t) {
            LOG.warn("Error initializing InitBaliListener");
            LOG.warn(t, t);
            throw new RuntimeException(t);
        }
    }

    /**
     * Notification that the servlet context is about to be shut down.
     * @param event an instance of {@link ServletContextEvent}.
     */
    public void contextDestroyed(ServletContextEvent event) {
    	
    	try {
			BaliServices.dinit();
			BaliProperties.dinit();
		} catch (Throwable t) {
			LOG.warn("Error destorying InitBaliListener");
			LOG.warn(t,t);
		}
        
        
    }
    
    
    public static Date getWebappStartTime(HttpServletRequest req) {
        ServletContext context = req.getSession(true).getServletContext();
        return (Date) context.getAttribute(WEBAPP_START_TIME);
    }
    static void setWebappStartTime(ServletContext context, Date time) {
        context.setAttribute(WEBAPP_START_TIME, new Date());
    }
}