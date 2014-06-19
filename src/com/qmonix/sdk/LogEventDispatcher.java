package com.qmonix.sdk;

import org.json.JSONException;


/**
 * Prints events to the log output. Good for testing or as a fallback default dispatcher when
 * other dispatchers fails or are not present.
 *
 * All methods are thread safe.
 */
public class LogEventDispatcher implements EventDispatcher {

	private EventMessage eventMessage = new EventMessage();


	/**
	 * Caches event. It will be dispatched explicitly with dispatch() method.
	 *
	 * @param event event to dispatch.
	 */
	@Override
	synchronized public void submit(Event event) {
		if (event == null) {
			throw new IllegalArgumentException("Event cannot be null.");
		}

		this.eventMessage.addEvent(event);
	}

	/**
	 * Sends events encoded to JSON to log output. Clears collected events on success.
	 *
	 * @param handler success handler.
	 */
	@Override
	synchronized public void dispatch(EventDispatchHandler handler) {
		try {
			QLog.info(this.eventMessage.toJson());
			this.eventMessage = new EventMessage();
			handler.onSuccess();

		} catch (JSONException e) {
			String errMsg = "Failed to encode events to JSON: " + e.toString();
			handler.onError(errMsg);
		}
	}
}
