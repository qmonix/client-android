package com.qmonix.sdk.helpers.exceptions;

import java.lang.String;

/**
 * Exception that is thrown when error occurs while doing HTTP related operations.
 * <p>
 * Exception constructor accepts error detail message parameter which later can be retrieved
 * with {@link Throwable#getMessage getMessage}.
 *
 * @see com.qmonix.sdk.helpers.HttpHelper
 */
public class HttpHelperException extends Exception
{
	/* @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
	 * Constructs new exception object with the current stack trace and specified error message.
	 *
	 * @param detailMessage the detail message for this exception.
	 */
	public HttpHelperException(String detailMessage)
	{
		super(detailMessage);
	}
}
