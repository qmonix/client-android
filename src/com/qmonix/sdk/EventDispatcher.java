package com.qmonix.sdk;


/**
 * Event dispatcher is responsible for collecting and dispatching events. Sample dispatchers:
 * file dispatcher that stores events locally in file system; HTTP dispatcher which sends events to
 * Qmonix Web service, log dispatcher that sends collected events to log output, etc.
 * <p>
 * Use {@link #submit submit} to pass events to event dispatcher. Use {@link #dispatch dispatch}
 * to flush the collected events meaning to act appropriately to specific dispatcher logic.
 *
 * @see Event
 */
public interface EventDispatcher {

	/**
	 * Passes a specified event object to the dispatcher.
	 *
	 * @param event event object to dispatch.
	 */
	public void submit(Event event);

	/**
	 * Send, store, print collected events.
	 *
	 * @param handler successful or failed dispatch handler.
	 */
	public void dispatch(EventDispatchHandler handler);
}
