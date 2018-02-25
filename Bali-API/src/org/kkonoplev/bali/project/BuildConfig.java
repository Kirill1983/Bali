package org.kkonoplev.bali.project;

import java.util.Date;

public class BuildConfig {
	
	private String command = "";
	private String log = "";
	private String successMark = "";
	private Date date;
	
	public BuildConfig() {
		
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getSuccessMark() {
		return successMark;
	}

	public void setSuccessMark(String successMark) {
		this.successMark = successMark;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
