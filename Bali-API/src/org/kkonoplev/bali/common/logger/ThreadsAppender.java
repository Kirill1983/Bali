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

package org.kkonoplev.bali.common.logger;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;



public class ThreadsAppender extends AppenderSkeleton {
	
	public static final String FILE = "threadfile";
	
	HashMap<String, FileAppender> appenders = new HashMap<String, FileAppender>(15);
	
	public ThreadsAppender(){
		
	}
	
	public ThreadsAppender(Layout layout){
		setLayout(layout);
	}

	@Override
	public void close() {

		Iterator it = appenders.entrySet().iterator();
		while (it.hasNext()){
			Entry<String,FileAppender> entry = (Entry<String,FileAppender>)it.next();
			FileAppender fa = entry.getValue();
			fa.close();
		}
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return true;
		
	}

	@Override
	protected void append(LoggingEvent logEvent) {
		
		String filepath = (String) MDC.get(FILE);
		
		// if not defined - then we'll not log here
		if (filepath == null)
			return;
		
		FileAppender fa = appenders.get(filepath);
		
		// if appender is available for file
		if (fa != null){
			fa.doAppend(logEvent);
		} else {
		// if not - then 
			FileAppender newAppender;
			
			try {
				
				newAppender = new FileAppender(layout, filepath, false);	
				newAppender.doAppend(logEvent);
				appenders.put(filepath, newAppender);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	

}
