package com.qmonix.sdk;

import org.json.JSONObject;
import org.json.JSONException;

import com.qmonix.sdk.utils.Utils;


/**
 * Single event class that holds it's tag name and a and a single value - it's fire time. Every
 * different logic event must have a unique tag name. Two different event objects might have the
 * same names but their collected information will be aggregated. Event tag name might be retrieved
 * by {@link #getTag getTag}. {@link #getTimeArised getTimeArised} returns event fire time.
 * <p>
 * {@link #toJson toJson} encodes event information to JSON object.
 *
 * @see EventDispatcher
 */
public class Event {

	private boolean fired = false;

	protected long timeArised;
	protected String tag;


	/**
	 * Constructs new single event with a specified tag name and Unix time stamp when it was
	 * fired.
	 *
	 * @param tag event tag name.
	 * @param timeArised time when event was fired.
	 */
	public Event(String tag, long timeArised) {
		if (tag == null) {
			throw new IllegalArgumentException("Tag name cannot be null.");
		}

		this.tag = tag;
		this.timeArised = timeArised;
	}

	/**
	 * @return event tag name.
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * Returns time when event was fired.
	 *
	 * @return event fire time.
	 */
	public long getTimeArised() {
		return this.timeArised;
	}

	/**
	 * Serializes event to JSON object which is ready to be encoded to event message. Event
	 * fire time and tag name are used.
	 *
	 * @return event encoded in JSON format.
	 * @throws JSONException if fails to encode event message to JSON object.
	 */
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("tag", this.getTag());
		json.put("whenArised", this.getTimeArised());

		return json;
	}
}
