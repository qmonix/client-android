package com.qmonix.sdk;

import java.lang.String;
import java.lang.RuntimeException;

import org.json.JSONObject;
import org.json.JSONException;

import com.qmonix.sdk.EventDispatcher;
import com.qmonix.sdk.Tracker;

import com.qmonix.sdk.utils.Utils;

import com.qmonix.sdk.exceptions.IllegalEventStateException;


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
	private State state = State.INITIAL;

	protected long timeArised = TimeInterval.UNINITIALIZED_TIME;
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
	 * Captures single event fire time. After this method is called event must be in unusable
	 * state. Meaning it should not be fired anymore or change it's state in any way.
	 * Otherwise the exception should be thrown.
	 */
	public void fire() throws IllegalEventStateException {
		if (this.state != State.INITIAL) {
			String msg = "event cannot be fired more than once";
			throw new IllegalEventStateException(msg);
		}

		this.timeArised = Utils.getUnixTime();
		this.state = State.FIRED;

		Tracker.getDispatcher().submit(this);
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

	/**
	 * All possible single event states.
	 * <ul>
	 * <li> {@code INITIAL} - when single event object is just created. It's fire time is
	 * {@value TimeInterval.UNINITIALIZED_TIME}.
	 * <li> {@code FIRED} - event becomes fired after {@code fire} is invoked. In this state
	 * event cannot be fired anymore. It can only remain in this state and be used to store
	 * information.
	 * </ul>
	 */
	private enum State {
		INITIAL, FIRED
	}
}
