package com.qmonix.sdk;

import android.util.Log;


/**
 * Qmonix logging class that automates some logging tasks: it prints a log caller class,
 * method names, and line number, it stores and prints an application tag so user would not need to
 * tell it every time.
 */
public class QLog {
	static public int DEBUG_LEVEL = 0;
	static public int INFO_LEVEL = 1;
	static public int WARNING_LEVEL = 2;
	static public int ERROR_LEVEL = 3;

	public static final String applicationTag = "QMONIX";

	static private boolean logClass = true;
	static private boolean logMethod = true;
	static private boolean logLineNr = true;

	static private int logLevel = INFO_LEVEL;


	static public int debug(String msg) {
		if (QLog.logLevel > DEBUG_LEVEL ) {
			return 0;
		}

		StringBuilder log = QLog.getCallerInfo(2);
		log.append(msg);
		return Log.d(QLog.applicationTag, log.toString());
	}

	static public int info(String msg) {
		if (QLog.logLevel > INFO_LEVEL) {
			return 0;
		}

		StringBuilder log = QLog.getCallerInfo(2);
		log.append(msg);
		return Log.i(QLog.applicationTag, log.toString());
	}

	static public int warning(String msg) {
		if (QLog.logLevel > WARNING_LEVEL) {
			return 0;
		}

		StringBuilder log = QLog.getCallerInfo(2);
		log.append(msg);
		return Log.w(QLog.applicationTag, log.toString());
	}

	/**
	 * Sends error log message if verbose logging is ON. Adds additional info like caller
	 * class and method names if QLog feature is turned ON (default).
	 *
	 * @param msg log message.
	 * @return bytes written to output.
	 */
	static public int error(String msg) {
		if (QLog.logLevel > ERROR_LEVEL) {
			return 0;
		}

		StringBuilder log = QLog.getCallerInfo(2);
		log.append(msg);
		return Log.e(QLog.applicationTag, log.toString());
	}

	/**
	 * Sets minimum log level.
	 *
	 * @param level minimum log level.
	 */
	static public void setLogLevel(int level) {
		QLog.logLevel = level;
	}


	/**
	 * Turns on or off log caller class name printing together with log.
	 *
	 * @param log true if log function should print class name, otherwise false.
	 */
	static public void setLogClass(boolean log) {
		QLog.logClass = log;
	}

	/**
	 * Turns on or off log caller method name printing together with log.
	 *
	 * @param log true if log function should print method name, otherwise false.
	 */
	static public void setLogMethod(boolean log) {
		QLog.logMethod = log;
	}

	/**
	 * Turns on or off log caller line number printing together with log.
	 *
	 * @param log true if log function should print line number, otherwise false.
	 */
	static public void setLogLineNr(boolean log) {
		QLog.logLineNr = log;
	}


	// Private methods.

	/**
	 * Depending on set features returns a log caller information: caller class and/or method
	 * name.
	 *
	 * @param level caller level in function call stack.
	 * @return log caller information string.
	 */
	static private StringBuilder getCallerInfo(int level) {
		StringBuilder callerInfo = new StringBuilder();

		try {
			throw new Exception();
		} catch (Exception e) {
			StackTraceElement ste = e.getStackTrace()[level];

			if (QLog.logClass) {
				callerInfo.append(ste.getClassName());
			}

			if (QLog.logMethod) {
				callerInfo.append(".");
				callerInfo.append(ste.getMethodName());
				callerInfo.append("()");
			}

			if (QLog.logLineNr) {
				callerInfo.append(":");
				callerInfo.append(ste.getLineNumber());
			}

			callerInfo.append(": ");
		}

		return callerInfo;
	}
}
