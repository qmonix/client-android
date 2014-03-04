package com.qmonix.sdk;

/**
 * Event dispatcher interface which is responsible for dispatching events to the server. Exact
 * behaviour depends on specific implementation, which can store event locally, send them to the
 * server, etc..
 * <p>
 * Dispatcher interface has a single method {@link #submit submit} which accepts event object for
 * further processing.
 *
 * @see Event
 */
public interface EventDispatcher
{
	/**
	 * Passes a specified event object to the dispatcher.
	 *
	 * @param event event object to dispatch.
	 */
	public void submit(Event event);
}
