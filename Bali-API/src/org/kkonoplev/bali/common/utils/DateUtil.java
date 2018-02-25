/* 
 * Copyright © 2011 Konoplev Kirill
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kkonoplev.bali.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;



/**
 * @author Kirill Konoplev
 */

public class DateUtil {
	 private static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String shortMask = "MMM d, yyyy";

	public static Date strToDateTime(String text) {
		Date dt = null;
		try {
			DateFormat dateFormat;
			
			if (text.contains("AM") || text.contains("PM"))
				dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
			else
				dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
			
			dt = dateFormat.parse(text);
		} catch (Exception e){
			System.out.println("DateUtil.strToDateTime error:"+e);
		}
		return dt;
	}
	
	public static String dateFormat(String mask, Date dt_){
		if (dt_ == null)
			return "";
		
		String dt = "";
		
		try {
			dt = dt_.toString();
			DateFormat dateFormat = new SimpleDateFormat(mask);
			dt = dateFormat.format(dt_);
		} catch (Exception e){
			System.out.println("DateUtil.strToDateTime error:"+e+" return empty string");
		}
		
		return dt;
		
	}

	public static Date nextMonthDay() {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
	
		
	
		
		return gc.getTime();
	}

	public static Date lastDayOfMonth() {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.DAY_OF_MONTH, gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
	
		return gc.getTime();
	}

	public static Date shiftHour(Date date, int i) {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();	
		gc.add(gc.HOUR, i);
		
		return gc.getTime();
	}

	public static Date shiftDays(Date date, int i) {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();	
		gc.add(gc.DAY_OF_MONTH, i);
		
		return gc.getTime();
	}

	public static DateFormat getDateOnlyFormat() {
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(DATE_PATTERN);
        return df;
    }
	
	public static DateFormat getDateFormat(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(pattern);
        return df;
    }
	
	public static Date getDateOnly(Date d, DateFormat df) {
	     Date date = null;
	     
	     if (d != null && df != null) {
	            try {
	                date = df.parse(df.format(d));
	            } catch (Exception ex) {
	                System.out.println(ex);
	            }
	        }
	        return date;
	    
	}

}
