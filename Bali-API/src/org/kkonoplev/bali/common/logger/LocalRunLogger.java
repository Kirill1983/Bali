package org.kkonoplev.bali.common.logger;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class LocalRunLogger {
	
	public static void initThreadsAndStdOutLogger(){
		
		Properties p = new Properties();

		p.setProperty("log4j.rootLogger", "info, threadslog, console");

		p.setProperty("log4j.appender.threadslog", "org.kkonoplev.bali.common.logger.ThreadsAppender"); 
	    p.setProperty("log4j.appender.threadslog.layout", "org.kkonoplev.bali.common.logger.LogHTMLLayout");
	    
		p.setProperty("log4j.appender.console",	"org.apache.log4j.ConsoleAppender");
		p.setProperty("log4j.appender.console.layout",	"org.apache.log4j.PatternLayout");
		p.setProperty("log4j.appender.console.layout.ConversionPattern","%p %m%n");
		
		PropertyConfigurator.configure(p);

	}
		
	public static void initStdoutLogger(){
			
		Properties p = new Properties();

		p.setProperty("log4j.rootLogger", "trace, console");
			
		p.setProperty("log4j.appender.console",	"org.apache.log4j.ConsoleAppender");
		p.setProperty("log4j.appender.console.encoding", "UTF-8");
	    p.setProperty("log4j.appender.console.Threshold", "INFO");
		p.setProperty("log4j.appender.console.layout",	"org.apache.log4j.PatternLayout");
		p.setProperty("log4j.appender.console.layout.ConversionPattern","%p %m%n");

		PropertyConfigurator.configure(p);
		
	}

}
