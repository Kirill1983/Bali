package org.kkonoplev.bali.runner;

import java.util.ArrayList;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public interface RunnableItemBuilder {
	
	public RunnableItem build(String path, BaseProject project) throws Exception;
	public ArrayList<Class<? extends TestExecResource>> getRequiredResourcesList(RunnableItem runnableItem);
	
}
