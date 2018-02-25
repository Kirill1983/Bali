package org.kkonoplev.bali.common.utils;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class ReflectionUtil {

	private static final Logger log = Logger.getLogger(ReflectionUtil.class);

	
	public static void setValue(Object object, String name, String value) throws Exception{

		int a  = 5;
		String expectedName = "set"+name;
			
		Class clazz = object.getClass();
		do {
			
			for (Method method: clazz.getDeclaredMethods()){
			  if (method.getName().equalsIgnoreCase(expectedName)){
				  log.info("invoke "+expectedName+"("+value+") for "+object.getClass().getSimpleName());
				  method.invoke(object, (Object)value);
				  return;
			  }
		  	}
			clazz = clazz.getSuperclass();
			
		} while(clazz != null);
		
		throw new RuntimeException("Cant find method "+expectedName+" in "+object.getClass());
		
	}
	
}
