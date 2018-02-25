package org.kkonoplev.bali.suiteexec;

public enum TestExecContextState {
	
	WAITING("waiting"),
	DETACHED("detached"),
	INIT_CONTEXT("initializing"),
	STARTED("in progress"),
	INTERRUPPED("interruped"),
	SOFT_STOP("soft stop"),
	SUCC("success"),
	FAIL("fail"),
	CANCELED("canceled"),
	RUNERROR("runtime error"), 
	PROJECT_MISSING("project missing"), 
	ERROR_RUNNABLE_UNIT("error runable item"),
	THREAD_STOP("do thread stop"), 
	TERMINATED("terminated");
	
	
	private String state = "";
	
	TestExecContextState(String state){
		this.state = state;
	}
	
	public String getDesc(){
		return state;
	}

}
