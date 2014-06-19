package com.qmonix.sdk;

import java.lang.String;

import java.net.URISyntaxException;

import org.json.JSONException;

import com.qmonix.sdk.helpers.HttpHelper;
import com.qmonix.sdk.helpers.exceptions.HttpHelperException;


/**
 * Event dispatcher which collects events and sends them to the Qmonix Web service over HTTP.
 * Server address name and port are specified in constructor.
 * <p>
 * Server address and port number must conform to TCP/IP addressing scheme. Domain name also might
 * be used as an address, e.g. example.com.
 * <p>
 * {@link #submit submit} inserts new event to the collected event list. It accepts {@link Event}
 * object which describes specific event. {@link #dispatch dispatch} sends those collected
 * events to the Server and clears event list. {@link #clear clear} clears collected event
 * list without dispatching them to the Server.
 * <p>
 * Before sending events out {@code DefaultEventDispatcher} encodes current time to the message that
 * is going to bet sent to the server. Using this time stamp server is able to ajust collected
 * events time with server time. All events are eventually registered using server time.
 * <p>
 * {@link #dispatch dispatch}, {@link #submit submit}, {@link #clear clear} are
 * thread safe.
 *
 * @see EventDispatcher
 * @see Event
 */
public class HttpEventDispatcher implements EventDispatcher {

	private EventMessage eventMessage;
	private HttpHelper httpHelper;


	/**
	 * Creates new dispatcher object which sends collected events to the Server.
	 * Uri to which events are sent must be specified in the parameters.
	 *
	 * @param eventUri server uri to which events must be posted. E.g.
	 *	http://qmonix.com:8337/event/. qmonix.com should be replaced with your server
	 *	hostname.
	 */
	public HttpEventDispatcher(String eventUri) throws URISyntaxException {
		this.eventMessage = new EventMessage();
		this.httpHelper = new HttpHelper(eventUri);
	}

	/**
	 * Adds event to the collected event list. Does not send events to the server. To send
	 * collected events use {@code dispatch}.
	 *
	 * @param event event object.
	 */
	@Override
	synchronized public void submit(Event event) {
		this.eventMessage.addEvent(event);
	}

	/**
	 * Sends all events to the Server, clears dispatcher event list. On failure throws
	 * an exception. In such case events are not cleared, but one can do it manually with
	 * {@link #clear clear}.
	 *
	 * Events are sent over HTTP syncrhonously, meaning this method will block until it finishes
	 * sendind or an error happens.
	 */
	@Override
	synchronized public void dispatch(EventDispatchHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("Dispatch handler cannot be null.");
		}

		try {
			String jsonEvent = this.eventMessage.toJson();
			QLog.debug(jsonEvent);

			this.httpHelper.uiPostMessage(jsonEvent);
			this.clear();
			handler.onSuccess();

		} catch (JSONException e) {
			String errMsg = "Failed to encode events to JSON: " + e.toString();
			handler.onError(errMsg);

		} catch (HttpHelperException e) {
			String errMsg = "Failed to send events to server: " + e.toString();
			handler.onError(errMsg);
		}
	}

	/**
	 * Clears collected event list. In no case collected events are sent to the server. So
	 * {@link #submit submit} is not invoked.
	 */
	synchronized public void clear() {
		this.eventMessage = new EventMessage();
	}
}
