package com.qmonix.sdk;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.qmonix.sdk.helpers.LogHelper;
import com.qmonix.sdk.utils.Utils;

/**
 * Holds all information that is necessary to send a valid event message to the Server.
 * <p>
 * {@code EventMessage} collects event objects with a method {@code addEvent} and is able to
 * encode JSON formated legal event message with {@code toJson}.
 */
public class EventMessage
{
	private ArrayList<Event> eventList;

	/**
	 * Constructs a new event message object. Initially event list is empty. Use {@code addEvent}
	 * to update event list.
	 */
	public EventMessage()
	{
		eventList = new ArrayList<Event>();
	}

	/**
	 * Adds new event to the event list.
	 *
	 * @param event event object to add to the list.
	 * @see Event
	 */
	public void addEvent(Event event)
	{
		this.eventList.add(event);
	}

	/**
	 * Encodes event message to JSON string which meets Server protocol. Sets 'whenSent' property
	 * to the time when this function is being executed.
	 *
	 * @return event message in JSON format.
	 * @throws JSONException if fails to encode event message to JSON formatted string.
	 */
	public String toJson() throws JSONException
	{
		JSONObject json = new JSONObject();
		JSONArray jsonEvents = new JSONArray();
		for (Event e : this.eventList)
		{
			jsonEvents.put(e.toJson());
		}

		long time_now = Utils.getUnixTime();

		json.put("events", jsonEvents);
		json.put("whenSent", time_now);

		String retval = json.toString(4);

		return retval;
	}
}
