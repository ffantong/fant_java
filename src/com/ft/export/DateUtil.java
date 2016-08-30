package com.ft.export;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static Date parse(String dateString) throws ParseException{
		if(dateString == null || dateString.equals("")){
			return null;
		}
		dateString = dateString.replaceAll("-", "");
		DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		Date dt = null;
		try{
			dt = fmt.parse(dateString);
		}catch(Exception e){
			return null;
		}
		return dt;
	}
}
