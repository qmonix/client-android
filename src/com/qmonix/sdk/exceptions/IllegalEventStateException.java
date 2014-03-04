package com.qmonix.sdk.exceptions;

import java.lang.RuntimeException;
import java.lang.String;

/**
 * Exception is thrown when some operation is performed in illegal event state. E.g. when timing
 * was started and {@code start} is invoked again, or when {@code pause} is invoked on a stopped
 * event.
 * <p>
 * Exception constructor accepts error detail message parameter which later can be retrieved
 * with {@link java.lang.Throwable#getMessage getMessage}.
 *
 * @see com.qmonix.sdk.TimingEvent
 */
public class IllegalEventStateException extends RuntimeException
{
	/* @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
	 * Constructs new exception object with the current stack trace and specified error message.
	 *
	 * @param detailMessage the detail message for this exception.
	 */
	public IllegalEventStateException(String detailMessage)
	{
		super(detailMessage);
	}
}
