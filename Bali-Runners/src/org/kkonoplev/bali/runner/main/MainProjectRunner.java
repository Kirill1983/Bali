package org.kkonoplev.bali.runner.main;

import java.lang.reflect.Method;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;

public class MainProjectRunner implements ProjectRunner {

	protected static final Logger log = LogManager.getLogger(MainProjectRunner.class);
	
	@Override
	public void run(RunnableItem runnableItem) throws Exception {
		MainRunnableItem item = (MainRunnableItem) runnableItem;
		Thread.currentThread().setContextClassLoader(item.getProject().getClassLoader());
		
		Class<?> c = item.getTestClass();
		Class[] argTypes = new Class[] { String[].class };
		Method main = c.getDeclaredMethod("main", argTypes);
	  	String[] mainArgs = new String[]{};
		log.info("invoking %s.main()%n"+ c.getName());
		main.invoke(null, (Object)mainArgs);
		
	}

}
