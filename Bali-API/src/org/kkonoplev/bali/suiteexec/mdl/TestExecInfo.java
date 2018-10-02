package org.kkonoplev.bali.suiteexec.mdl;

import org.kkonoplev.bali.suiteexec.TestExecContext;

public class TestExecInfo {
	
	private String name;
	private String state;
	private TestExecContext testExecContext = null;
	
	public TestExecInfo(String name, String state,
			TestExecContext testExecContext) {
		super();
		this.name = name;
		this.state = state;
		this.testExecContext = testExecContext;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

	public TestExecContext getTestExecContext() {
		return testExecContext;
	}

}
