package com.parser.json.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger {
	
	static Logger mLogger = null;
	
	String sFormatLog = "%s  %s  %s";
	
	public Logger(){}
	
	public static Logger getInstance(){
		if(mLogger == null)
			mLogger = new Logger();
		return mLogger;
	}
	
	public void Debug(String formatmessage,  Object ...objects){
		System.out.println(formatLog(formatmessage, objects));
	}
	
	public String formatLog(String formatmessage,  Object ...objects){
		String sLogLine = String.format(sFormatLog, getCurrentDate(), getFunc(), formatmessage);
		sLogLine = String.format(sLogLine, objects);
		return sLogLine;
	}
	
	public String getCurrentDate(){
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date today = Calendar.getInstance().getTime();        
		String reportDate = df.format(today);
		return reportDate;
	}
	
	public String getFunc(){
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if(stacktrace != null){
			StackTraceElement e = stacktrace[4]; //maybe this number needs to be corrected
			String methodName = e.getClassName() + "." + e.getMethodName()+"("+ e.getLineNumber()+")";
			return methodName;
		}
		return "******";
	}
	
}
