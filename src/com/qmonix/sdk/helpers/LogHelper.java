package com.qmonix.sdk.helpers;

import android.util.Log;


/**
 * This log helper automates some logging tasks: it prints a log caller class, method names, and
 * line number, it stores and prints an application tag so user would not need to tell it every time.
 */
public class LogHelper
{
	static private boolean outputCallerClassName = true;
	static private boolean outputCallerMethodName = true;
	static private boolean outputCallerLineNumber = true;
	static private boolean verboseOn = false;
	static private boolean errorOn = true;

	public static final String applicationTag = "QMONIX";


	/**
	 * Depending on set features returns a log caller information: caller class and/or method
	 * name.
	 *
	 * @param level	caller level in function call stack.
	 * @return log caller information string.
	 */
	static private String getCallerInfo(int level) {
		String callerInfo = "";

		try{
			throw new Exception();
		} catch (Exception e) {
			StackTraceElement ste = e.getStackTrace()[level];

			if (LogHelper.outputCallerClassName == true)
			{
				callerInfo += ste.getClassName();
			}

			if (LogHelper.outputCallerMethodName == true)
			{
				callerInfo += "." + ste.getMethodName() + "()";
			}

			if (LogHelper.outputCallerLineNumber == true) {
				callerInfo += ":" + Integer.toString(ste.getLineNumber());
			}

			callerInfo += ": ";
		}

		return callerInfo;
	}

	/**
	 * Sends verbose log message if verbose logging is ON. Adds additional info like caller
	 * class and method names if this feature is turned ON (default).
	 *
	 * @param msg log message.
	 * @return bytes written to output.
	 */
	static public int v(String msg) {
		if (LogHelper.verboseOn == false) {
			return 0;
		}

		String callerInfo = LogHelper.getCallerInfo(2);
		msg = callerInfo + msg;
		return Log.v(LogHelper.applicationTag, msg);
	}

	/**
	 * Sends error log message if verbose logging is ON. Adds additional info like caller
	 * class and method names if this feature is turned ON (default).
	 *
	 * @param msg log message.
	 * @return bytes written to output.
 	 */
	static public int e(String msg) {
		if (LogHelper.errorOn == false) {
			return 0;
		}

		String callerInfo = LogHelper.getCallerInfo(2);
		msg = callerInfo + msg;
		return Log.e(LogHelper.applicationTag, msg);
	}

	/**
	 * Turns verbose logging on or off.
	 *
	 * @param verbose true if verbose logging is to be turned on, otherwise false.
	 */
	static public void setVerboseOn(boolean verbose) {
		LogHelper.verboseOn = verbose;
	}

	/**
	 * Turns error logging on or off.
	 *
	 * @param error true if verbose logging is to be turned on, otherwise false.
	 */
	static public void setErrorOn(boolean error) {
		LogHelper.errorOn = error;
	}

	/**
	 * Turns on or off log caller class name printing together with log.
	 *
	 * @param on true if log function should print class name, otherwise false.
	 */
	static public void setCallerClassNameOn(boolean on) {
		LogHelper.outputCallerClassName = on;
	}

	/**
	 * Turns on or off log caller method name printing together with log.
	 *
	 * @param on true if log function should print method name, otherwise false.
	 */
	static public void setCallerMethodNameOn(boolean on) {
		LogHelper.outputCallerMethodName = on;
	}

	/**
	 * Turns on or off log caller line number printing together with log.
	 *
	 * @param on true if log function should print line number, otherwise false.
	 */
	static public void setCallerLineNumberOn(boolean on) {
		LogHelper.outputCallerLineNumber = on;
	}

}
