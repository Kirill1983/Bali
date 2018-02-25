package org.kkonoplev.bali.suiteexec;

import java.util.Date;

public class SoftStopException extends Exception {
	
	private Date date;
	
	public SoftStopException(){
		super("thread soft exit");
		date = new Date();
	}

	public Date getDate() {
		return date;
	}
	
	
	

}
