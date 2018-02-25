package org.kkonoplev.bali.suiteexec;

import java.io.Serializable;
import java.util.Date;

import org.kkonoplev.bali.common.utils.DateUtil;

public class Message implements Serializable{

	
	public static final String format = "MM/dd HH:mm:ss";
	public static final String shortformat = "HH:mm:ss";
	
	private String text = "";
	private String tooltip = "empty";
	private Date date;
	
	
	public Message(){
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	public void setTextAddTip(String text) {
		setText(text+" "+DateUtil.dateFormat(Message.shortformat, new Date()));
		addTooltipText(DateUtil.dateFormat(Message.format, new Date())+" "+text);		
	}

	
	public String getTooltip() {
		return tooltip;
	}
	
	public void addTooltipText(String addOn) {
		tooltip += addOn+"\n";		
	}



	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}


	public Date getDate() {
		return date;
	}
	
	public String getStartDateFmt() {
		return DateUtil.dateFormat(TestExecContext.fmt, date);
	}



	public void setDate(Date date) {
		this.date = date;
	}


	public void clear() {
		text = "";
		tooltip = "";		
	}




	
	

}
