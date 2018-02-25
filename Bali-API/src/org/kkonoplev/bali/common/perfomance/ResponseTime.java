package org.kkonoplev.bali.common.perfomance;

import java.io.Serializable;
import java.util.Date;

import org.kkonoplev.bali.common.utils.DateUtil;

public class ResponseTime implements Serializable {
	
	private double delay;
	private Date time;
	
	private static final String fmt = "MM/dd HH:mm:ss";
	
	public ResponseTime(Date time, double delay) {
		super();
		this.delay = delay;
		this.time = time;
	}
	
	
	public double getDelay() {
		return delay;
	}
	public void setDelay(Long delay) {
		this.delay = delay;
	}
	public Date getTime() {
		return time;
	}
	
	public String getTimeFmt() {
		return DateUtil.dateFormat(fmt, time);
	}
	
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	

}
