package com.qmonix.sdk;

import java.lang.String;

import org.json.JSONException;

import com.qmonix.sdk.EventDispatcher;

import com.qmonix.sdk.helpers.HttpHelper;
import com.qmonix.sdk.helpers.LogHelper;

import com.qmonix.sdk.helpers.exceptions.HttpHelperException;
import com.qmonix.sdk.exceptions.DefaultEventDispatcherException;

/**
 * Default event dispatcher which collects events and sends them to the Server. Server address name
 * and port are specified in constructor.
 * <p>
 * Server address and port number must conform to TCP/IP addressing scheme. Domain name also might
 * be used as an address, e.g. example.com.
 * <p>
 * {@link #submit submit} inserts new event to the collected event list. It accepts {@link Event}
 * object which describes specific event. {@link #sendToServer sendToServer} sends those collected
 * events to the Server and clears event list. {@link #dropEvents dropEvents} clears collected event
 * list without dispatching them to the Server.
 * <p>
 * Before sending events out {@code DefaultEventDispatcher} encodes current time to the message that
 * is going to bet sent to the server. Using this time stamp server is able to ajust collected
 * events time with server time. All events are eventually registered using server time.
 * <p>
 * {@link #sendToServer sendToServer}, {@link #submit submit}, {@link #dropEvents dropEvents} are
 * synchronized and safe to use in multiple threads.
 *
 * @see EventDispatcher
 * @see Event
 */
public class DefaultEventDispatcher implements EventDispatcher
{
	private EventMessage eventMessage;

	private HttpHelper httpHelper;

	/**
	 * Creates new dispatcher object which sends collected events to the Server.
	 * Server address and port are specified in the parameters.
	 *
	 * @param serverAddress Server address.
	 * @param serverPort Server port.
	 */
	public DefaultEventDispatcher(String serverAddress, int serverPort)
	{
		this.eventMessage = new EventMessage();

		this.httpHelper = new HttpHelper(serverAddress, serverPort);
	}

	/**
	 * Adds event to the collected event list. Does not send events to the server. To send
	 * collected events use {@code sendToServer}.
	 *
	 * @param event event object.
	 */
	synchronized public void submit(Event event)
	{
		this.eventMessage.addEvent(event);
	}

	/**
	 * Sends all events to the Server, clears dispatcher event list. On failure throws
	 * an exception. In such case events are not cleared, but one can do it manually with
	 * {@link #dropEvents dropEvents}.
	 *
	 * @throws DefaultEventDispatcherException if fails to send events to the Server.
	 */
	synchronized public void sendToServer() throws DefaultEventDispatcherException
	{
		try
		{
			String jsonEvent = this.eventMessage.toJson();

			LogHelper.v(jsonEvent);

			this.httpHelper.uiPostMessage(jsonEvent);
			this.dropEvents();
		}
		catch (JSONException e)
		{
			LogHelper.e(e.toString());
			throw new DefaultEventDispatcherException("JSON encode exception.");
		}
		catch (HttpHelperException e)
		{
			LogHelper.e(e.toString());
			throw new DefaultEventDispatcherException("Error happened while sending " +
				"events to the server.");
		}
		catch (Exception e)
		{
			String msg = e.toString();
			LogHelper.e(msg);
			throw new DefaultEventDispatcherException("Unknown error: " + msg);
		}
	}

	/**
	 * Clears collected event list. In no case collected events are sent to the server. So
	 * {@link #submit submit} is not invoked.
	 */
	synchronized public void dropEvents()
	{
		this.eventMessage = new EventMessage();
	}
}

