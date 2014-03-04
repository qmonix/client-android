package com.qmonix.sdk.exceptions;

import java.lang.String;
import java.lang.RuntimeException;

/**
 * Exception that is thrown when performing any operations before
 * {@link com.qmonix.sdk.Tracker Tracker} was initialized. E.g. one is trying
 * to {@link Tracker#fire fire} and event before {@link Tracker#init init} was invoked.
 * <p>
 * Exception constructor accepts error detail message parameter which later can be retrieved
 * with {@link java.lang.Throwable#getMessage() getMessage}.
 *
 * @see com.qmonix.sdk.Tracker
 */
public class UninitializedTrackerException extends RuntimeException
{
	/* @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
	 * Constructs new exception object with the current stack trace and specified error message.
	 *
	 * @param detailMessage the detail message for this exception.
	 */
	public UninitializedTrackerException(String detailMessage)
	{
		super(detailMessage);
	}
}
