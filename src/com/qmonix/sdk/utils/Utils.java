package com.qmonix.sdk.utils;


/**
 * This class has some static members that are often used.
 */
public class Utils {

	/**
	 * Returns current time in Unix time format. It is the number of seconds that have elapsed
	 * since midnight Coordinated Universal Time (UTC), 1 January 1970.
	 *
	 * @return unix time.
	 */
	public static long getUnixTime(){
		return System.currentTimeMillis() / 1000;
	}
}
