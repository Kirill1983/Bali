package org.kkonoplev.bali.suiteexec.resource;

import org.kkonoplev.bali.suiteexec.TestExecContext;

public class EnvResource extends TestExecResource {
	
	public static final String ENVRESOURCE = "ResourceEnv";
	public static final String RESOURCE = "Resource";
	public static final String ALLENV = "ALL ENVS";
	private String env;
	
	public EnvResource(String name, String env){
		this.name = name;
		this.type  = "env resource";
		this.env = env;
	}

	@Override
	public String getDescription() {
		return env;
	}

	public boolean isFree(TestExecContext testExecContext, String properties) {
		
		if (this.testExecContext != null)
			return false;
			
		
		if (properties.contains(name))
			return true;
		else
			return false;
		
	}

	public boolean isFree(String properties) {
		return false;
	}

}
