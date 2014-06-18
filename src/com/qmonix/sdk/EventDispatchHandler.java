package com.qmonix.sdk;


/**
 * Used by EventDispatcher.dispatch() to notify when event dispatching is done: either on success or
 * on failure.
 */
public interface EventDispatchHandler {
	public void onSuccess();
	public void onError(String errorMessage);
}
