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
import org.kkonoplev.bali.classifyreport.TestLogArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.WarningCaseArtifactsBuilder;
import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;



public class TestExecutor extends Thread {
	
	private static final Logger log = Logger.getLogger(TestExecutor.class);
	
	protected SuiteExecProcessor execProcessor;
	protected TestExecContext testExecContext;
	private Monitor monitor = new Monitor();
	
	protected boolean stop = false;
	
	public TestExecutor(SuiteExecProcessor execProcessor, int i){
		log.info("Init Test Executor "+i);
		this.execProcessor = execProcessor;	
		setName("Bali Thread "+i);
	}
	
	public TestExecutor(SuiteExecProcessor execProcessor, String name){
		log.info("Init Test Executor "+name);
		this.execProcessor = execProcessor;	
		setName(name);
	}

	
	public void run(){
		
		log.info("*****"+getName()+" started.");
		
		try {			
		
			do {
				
				TestExecContext testJob;
				try {
					testJob = execProcessor.detachNextFreeJob();			
				} catch (Exception e){
					log.warn("Error run in "+getName());
					log.warn(e,e);
					testJob = null;					
				}
				
				if (testJob == null){
					doWait();
				}
				else {
					doJob(testJob);
				}
				
			} while (!stop);
		
		} catch (Throwable e) {
			log.warn("Error run in "+getName());
			log.warn(e,e);
		}
		
		log.info(getName()+" exited.");		
		
	}


	private void doWait() {
					
		try {
			log.info("****** "+getName()+" has no Job and is waiting!");
			setTestExecContext(null);
			
			waitOnMonitor();
			
			log.info("****** "+getName()+" notified.");
		} catch (Exception e) {
			log.warn("Error while JobExecutor is waiting()");
			log.warn(e,e);
		}
			
	}
	


	public  void waitOnMonitor() {
		try {
			
			synchronized(monitor){
				monitor.wait();
			}
			
		} catch (Exception e) {
			log.warn("Error while JobExecutor is waitOnMonitor()");
			log.warn(e,e);
		}
	}

	private void doJob(TestExecContext testExecContext) throws Exception  {
		
			
		log.info(getName()+" started test job: "+testExecContext);			
		setTestExecContext(testExecContext);
		testExecContext.setTestExecutor(this);
		TestExecContextThreadLocal.setTestExecContext(testExecContext);
		boolean error = false;
		
		// for load mode (repeat until defined time is out)
				
				log.info(getName()+" init logger");
				testExecContext.setCurrentThreadLogAppendersFile();
				
				log.info(getName()+" started initialize test exec context");
				testExecContext.initContext();
				log.info(getName()+" finsished initialize test exec context: "+TestExecContextThreadLocal.getTestExecContext());
				
				log.info(getName()+" started test: "+testExecContext.getRunnableNode().getLineViewLabel());
				testExecContext.start();
				RunnableItem item = testExecContext.getRunnableItem();
				Class<? extends ProjectRunner> runnerClass = item.getRunnerClass();
				ProjectRunner runner = runnerClass.newInstance();
		

		do{
					
			try {
				runner.run(item);
			} catch (ThreadDeath e){
				error = true;
				log.warn(getName()+"error in doJob");
				log.warn(e,e);
				testExecContext.interrupt();
			} catch (Throwable e){
				error = true;
				log.warn(getName()+"error in doJob");
				log.warn(e,e);	
			
				try {
					testExecContext.addError(e.toString(), 
							new WarningCaseArtifactsBuilder[]{ new TestLogArtifactsBuilder(testExecContext) });
				} catch (Throwable t){
					log.error(t,t);
				}
				testExecContext.runtimeError(e);
			} finally {
				testExecContext.setLoops(testExecContext.getLoops()+1);
			}
		
		} while ( (testExecContext.getSuiteExecContext().getElapsed() < testExecContext.getSuiteExecContext().getLoadminutes()) && (!error) && (!testExecContext.getNeedStop()));	
		
		
		if (testExecContext.getNeedStop()){
			log.info(getName()+" need to soft stop. Exited "+testExecContext);
			testExecContext.setStatus(TestExecContextState.SOFT_STOP);
			testExecContext.onFinished();
			return;
		} 

		log.info(getName()+" finisihed test: "+testExecContext.getClassName());
		if (!error){
			testExecContext.done();			
			log.info(getName()+" finished test job: "+testExecContext);	
		}


	}

	public boolean notifyMonitor() {
		
		if (testExecContext != null)
			return false;
		
		notifyMonitorDirectly();
		return true;
		
	}
	
	public void notifyMonitorDirectly() {
		
		if (this.getState().equals("RUNNABLE"))
			return;
		
		synchronized (monitor) {
			monitor.notifyAll();
		}
		
	}	

	public TestExecContext getTestExecContext() {
		return testExecContext;
	}

	public void setTestExecContext(TestExecContext testExecContext) {
		this.testExecContext = testExecContext;
	}
	
	
	


	
}
