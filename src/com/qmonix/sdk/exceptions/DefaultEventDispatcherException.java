package com.qmonix.sdk.exceptions;

import java.lang.String;
import java.lang.RuntimeException;

/**
 * Exception is thrown when dispatcher fails to properly perform some tasks.
 * <p>
 * Exception constructor accepts error detail message parameter which later can be retrieved
 * with {@link java.lang.Throwable#getMessage() getMessage}.
 *
 * @see com.qmonix.sdk.DefaultEventDispatcher
 */
public class DefaultEventDispatcherException extends RuntimeException
{
	/* @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
	 * Constructs new exception object with the current stack trace and specified error message.
	 *
	 * @param detailMessage the detail message for this exception.
	 */
	public DefaultEventDispatcherException(String detailMessage)
	{
		super(detailMessage);
	}
}
