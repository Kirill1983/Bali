package org.kkonoplev.bali.runner.main;

import java.util.ArrayList;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.runner.RunnableItemBuilder;
import org.kkonoplev.bali.suiteexec.annotation.UseResources;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public class MainRunnableItemBuilder implements RunnableItemBuilder {

	
	@Override
	public RunnableItem build(String className, BaseProject proj) throws Exception {
		
		MainProject project = (MainProject) proj;
		Thread.currentThread().setContextClassLoader(project.getClassLoader());
		Class testClass = Class.forName(className, true, project.getClassLoader());
		MainRunnableItem item  = new MainRunnableItem();
		return item;
		
	}

	@Override
	public ArrayList<Class<? extends TestExecResource>> getRequiredResourcesList(
			RunnableItem runnableItem) {
		
		MainRunnableItem item = (MainRunnableItem) runnableItem;
		ArrayList<Class<? extends TestExecResource>> resources = new ArrayList<Class<? extends TestExecResource>>(); 
		Class testclass = item.getTestClass();
		
		return getRequiredClassResourcesList(testclass);
		
	}
	
	private ArrayList<Class<? extends TestExecResource>> getRequiredClassResourcesList(
			Class testclass){

		ArrayList<Class<? extends TestExecResource>> resources = new ArrayList<Class<? extends TestExecResource>>(); 
		if (testclass == null)
			return resources;
		
		UseResources useResources = (UseResources) testclass.getAnnotation(UseResources.class);
		if (useResources != null)
			for (int i = 0; i < useResources.list().length; i++)
				resources.add(useResources.list()[i]);				
		
		ArrayList<Class<? extends TestExecResource>> resourcesChild;
		
		resourcesChild = getRequiredClassResourcesList(testclass.getSuperclass());
		resources.addAll(resourcesChild);

		return resources;
	}

}
