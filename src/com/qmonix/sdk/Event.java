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
 * {@link #fire fire} captures event fire time which cannot be changed in the future. After firing
 * an event, it becomes unusable. If one tries to invoke {@link #fire fire} more than once, the
 * exception will be thrown.
 * <p>
 * {@link #toJson toJson} encodes event information to JSON object.
 *
 * @see EventDispatcher
 * @see exceptions.IllegalEventStateException
 */
public class Event {

	private boolean fired = false;

	protected long timeArised;
	protected String tag;


	/**
	 * Constructs new single event with a specified tag name.
	 *
	 * @param tag event tag name.
	 */
	public Event(String tag) {
		if (tag == null) {
			throw new IllegalArgumentException("Tag name cannot be null.");
		}

		this.tag = tag;
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
	 * Captures single event fire time. Event can be fired only once.
	 *
	 * @return true if event is successful fired, false if it was already fired.
	 */
	public boolean fire() {
		boolean result = false;

		if (!this.fired) {
			this.timeArised = Utils.getUnixTime();
			this.fired = true;
			Tracker.getDispatcher().submit(this);

			result = true;
		}

		return result;
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
