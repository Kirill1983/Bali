package org.kkonoplev.bali.runner;

import java.util.List;

import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public interface RunnableItem {
	
	public List<RequiredTestResource> getRequiredTestResources();
	public Class<? extends ProjectRunner> getRunnerClass();

}
