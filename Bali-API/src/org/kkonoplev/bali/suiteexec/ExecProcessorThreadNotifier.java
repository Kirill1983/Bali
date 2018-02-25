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

package org.kkonoplev.bali.suiteexec;

import org.apache.log4j.Logger;

import org.kkonoplev.bali.common.utils.FrequencyController;

public class ExecProcessorThreadNotifier extends Thread {
	
	private static final Logger log = Logger.getLogger(ExecProcessorThreadNotifier.class);
	private SuiteExecProcessor processor;
	private Object monitor = new Object();
	
	private int count = 0;
	private FrequencyController rumpUpFreqController;
	
	public ExecProcessorThreadNotifier(SuiteExecProcessor processor_){
		log.info("Init Processor Thread Notifier");
		processor = processor_; 
		rumpUpFreqController = new FrequencyController(1, 3000, "test rump up");		
	}

	public void setRumpUp(int rumpup) {
		rumpUpFreqController = new FrequencyController(1, rumpup, "suite rump up");		
	}

	public void run() {
		
		log.info("Init Processor TestExecutor Notifier Thread");
		
		while (hasLoops()){
			
			log.info("Notify job executors started. loop count="+count);
			TestExecutor lastExecutor = null;
			boolean needStop = false;
			
			for (TestExecutor testExecutor: processor.getTestExecutors()){
				try {
					if (testExecutor.getTestExecContext() == null && (testExecutor.getState() == State.WAITING || testExecutor.getState() == State.NEW )){
						rumpUpFreqController.waitIfLimit();
						
						if (lastExecutor == null)
							kickThread(testExecutor);
						else{ 
							if (lastExecutor.getTestExecContext() != null)
									kickThread(testExecutor);
							else
								needStop = true;
						}
						
						lastExecutor = testExecutor;
					}					
				} catch (Exception e){
					log.warn("Error in ExecThreadNotifier Thread");
					log.warn(e,e);
				}
				
				if (needStop){
					log.info("All jobs are assigned to runners");
					log.info("No new active ones required.");
					break;
				}
			}
			
			log.info("Notify job executors finished.");			
			
		}
		
		
	}

	private void kickThread(TestExecutor testExecutor) {

		if (testExecutor.getState() == State.NEW)
			testExecutor.start();
		else
			testExecutor.notifyMonitorDirectly();
	}

	private boolean hasLoops() {
		
		synchronized (monitor){
			
			
			if (count > 0){
				count--;
				return true;
			}
			else {
				
				try {
					log.info("wait();");
					monitor.wait();
					count--;
					return true;
				} catch (Exception e){					
				}
				
			}
			
		}
		
		return false;
	}

	public void pingExecutors() {
		
		synchronized (monitor){
			count++;
			monitor.notifyAll();
		}
		
	}

	

}
