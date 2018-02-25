package org.kkonoplev.bali.services.utils;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.project.BuildConfig;

public class BuildRunnerThread extends Thread {
	
	
	private static final Logger log = Logger.getLogger(BuildRunnerThread.class);
	private BuildConfig buildConfig;
	
	public BuildRunnerThread(BuildConfig buildConfig) {
		super();
		this.buildConfig = buildConfig;
	}
	
	public void run(){
		
		log.warn("Start build command thread.");
		try {
			BuildRunner.run(buildConfig);
		} catch (Exception e){
			log.warn(e,e);
		}
		log.warn("End build command thread.");
		
	}

}
