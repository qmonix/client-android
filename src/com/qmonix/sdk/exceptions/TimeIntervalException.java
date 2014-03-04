package com.qmonix.sdk.exceptions;

import java.lang.String;
import java.lang.RuntimeException;

/**
 * Exception that is thrown when performing invalid operations on TimeInterval class objects.
 * E.g. this might be when stop time stamp is lower than the start time stamp.
 * <p>
 * Exception constructor accepts error detail message parameter which later can be retrieved
 * with {@link java.lang.Throwable#getMessage() getMessage}.
 */
public class TimeIntervalException extends RuntimeException
{
	/* @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
	 * Constructs new exception object with the current stack trace and specified error message.
	 *
	 * @param detailMessage the detail message for this exception.
	 */
	public TimeIntervalException(String detailMessage)
	{
		super(detailMessage);
	}
}
