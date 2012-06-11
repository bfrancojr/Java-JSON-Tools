/*Dec 4, 2006 Danny
*/
package com.sdicons.json.mapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sdicons.json.mapper.helper.impl.DateMapper;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestIso8601Date extends TestCase {
	
	public void testIt(){
		
		//test date from java to json
		Date date1=new Date();
		String  string=DateMapper.toRFC3339(date1);
		System.out.println(string);
		string=DateMapper.toRFC3339(date1,false);
		System.out.println(string);
		
		//test date from json to java		
		try {
			Date date2;
			date2 = DateMapper.fromISO8601(string,false);
			Assert.assertEquals(date1, date2);
			
			string="2006-12-05";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006-12-05 12:23";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006-12-05T12:23";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006-12-05T12:23:54.667";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006-12-05T12:23:54.667+08:00";
			date2=DateMapper.fromISO8601(string,false);
			System.out.println(date2+"<--"+string);
			
			string="2006W02";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006W021";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006W027";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006W022T12:23:54.667+08:00";
			date2=DateMapper.fromISO8601(string,false);
			System.out.println(date2+"<--"+string);
			
			string="2006007";
			date2=DateMapper.fromISO8601(string);
			System.out.println(date2+"<--"+string);
			
			string="2006014T12:23:54.667+08:00";
			date2=DateMapper.fromISO8601(string,false);
			System.out.println(date2+"<--"+string);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
	}
