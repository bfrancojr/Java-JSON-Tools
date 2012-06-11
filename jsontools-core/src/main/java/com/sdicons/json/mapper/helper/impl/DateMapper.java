package com.sdicons.json.mapper.helper.impl;
/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006-2008 S.D.I.-Consulting BVBA
    http://www.sdi-consulting.com
    mailto://nospam@sdi-consulting.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class DateMapper
extends AbstractMapper
{
	private static boolean timeZoneIgnored=true;
	
    public static boolean isTimeZoneIgnored() {
		return timeZoneIgnored;
	}

	public static void setTimeZoneIgnored(boolean timeZoneIgnored) {
		DateMapper.timeZoneIgnored = timeZoneIgnored;
	}

	public Class getHelpedClass()
    {
        return Date.class;
    }

    public JSONValue toJSON(Object aPojo) throws MapperException
    {
    	if(DateMapper.isTimeZoneIgnored())
    		return new JSONString(toRFC3339((Date) aPojo));
    	else
    		return new JSONString(toRFC3339((Date) aPojo,false));
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if (!aValue.isString()) throw new MapperException("DateMapper cannot map class: " + aValue.getClass().getName());
        if(DateMapper.isTimeZoneIgnored())
        	return fromISO8601(((JSONString) aValue).getValue().trim());
        else
        	return fromISO8601(((JSONString) aValue).getValue().trim(),false);
    }
    //Created on 5/12/2006 by Changdong Li
	public static String toRFC3339(Date date){
		return toRFC3339(date,true);
	}
    public static String toRFC3339(Date date,boolean timezoneIgnored)
    {     
    	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");        
    	dateFormat.setLenient(false);
    	String dateString=dateFormat.format(date);
    	int length=dateString.length();
    	if(timezoneIgnored){
    		dateString=dateString.substring(0,length-5);
    	}else{
    		dateString=dateString.substring(0,length-2)+":"+dateString.substring(length-2);
    	}    	
        return dateString;
    }
    
    public static Date fromISO8601(String timestampString) throws MapperException{
    	return fromISO8601(timestampString,true);
    }
	public static Date fromISO8601(String timestampString,boolean timezoneIgnored) throws MapperException{
		if(timestampString==null||timestampString.trim().length()<1){
			throw new MapperException("time stamp string can't be empty.");
		}
		timestampString=timestampString.trim();
		
		GregorianCalendar calendar=new GregorianCalendar(); 		
		String separator=" ";
		if(timestampString.indexOf("T")!=-1){
			separator="T";
		}
		String[] dateAndTime=timestampString.split(separator);
		String dateString=dateAndTime[0];
		String timeString=null;
		if(dateAndTime.length>1){
			timeString=dateAndTime[1];
		}
		
		Pattern timePattern=Pattern.compile("^(\\d{4})((-?(\\d{2})(-?(\\d{2}))?)|(-?(\\d{3}))|(-?W(\\d{2})(-?([1-7]))?))?$");		
		Matcher timeMatcher=timePattern.matcher(dateString);
		if(timeMatcher.find()){
			int year=Integer.parseInt(timeMatcher.group(1));
	        int month =(timeMatcher.group(4)==null)?0:Integer.parseInt(timeMatcher.group(4));
	        int dayOfMonth = (timeMatcher.group(6)==null)?1:Integer.parseInt(timeMatcher.group(6));
	        int dayOfYear = (timeMatcher.group(8)==null)?-1:Integer.parseInt(timeMatcher.group(8));
	        int week = (timeMatcher.group(10)==null)?-1:Integer.parseInt(timeMatcher.group(10));	        
	        int dayOfWeek =(timeMatcher.group(12)==null)?Calendar.MONDAY:Integer.parseInt(timeMatcher.group(12))+1;
	        if(dayOfWeek==8) {dayOfWeek=1;week=week+1;}

            calendar.set(Calendar.YEAR, year);
	        if(week!=-1){
	        	calendar.set(Calendar.WEEK_OF_YEAR,week);	        	
	        	calendar.set(Calendar.DAY_OF_WEEK,dayOfWeek);
	        }else if(dayOfYear!=-1){
	        	calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
	        }else{
	        	calendar.set(Calendar.MONTH,month-1);
	        	calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	        }
		}else{
			throw new MapperException("invalid date string:"+dateString);
		}		
        
    	int hour=0;
    	int minute=0;
    	int second=0;
    	int milliSecond=0;
    	String timezoneString=null;
    	String localTimeString=timeString;
        if(timeString!=null){        	
        	Pattern timezonePattern=Pattern.compile("(([-+])(\\d{2})(:?(\\d{2}))?)$");        
        	Matcher timezoneMatcher=timezonePattern.matcher(timeString);          	
    		if(timezoneMatcher.find()){
    			timezoneString=timezoneMatcher.group(0);
    			localTimeString=timeString.substring(0,timeString.length()-timezoneString.length());
    		}
    		Pattern localTimePattern=Pattern.compile("^(\\d{2})(:?(\\d{2})(:?(\\d{2})(.(\\d+))?)?)?$");        
        	Matcher localTimeMatcher=localTimePattern.matcher(localTimeString);
        	if(localTimeMatcher.find()){        		
        		if(localTimeMatcher.group(1)!=null) hour=Integer.parseInt(localTimeMatcher.group(1));
        		if(localTimeMatcher.group(3)!=null) minute=Integer.parseInt(localTimeMatcher.group(3));
        		if(localTimeMatcher.group(5)!=null) second=Integer.parseInt(localTimeMatcher.group(5));
        		if(localTimeMatcher.group(7)!=null) milliSecond=(int)(Float.parseFloat("0."+localTimeMatcher.group(7))*1000);        		
        	}
        }
    	calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, milliSecond);
		
    	if(timezoneString!=null && !timezoneIgnored){
    		TimeZone timeZone=TimeZone.getTimeZone("GMT"+timezoneString);
    		calendar.setTimeZone(timeZone);
    		return calendar.getTime();
    	}

        return calendar.getTime();
	}

}