package com.qmonix.sdk;

import org.json.JSONObject;
import org.json.JSONException;

import com.qmonix.sdk.Event;
import com.qmonix.sdk.EventDispatcher;
import com.qmonix.sdk.Tracker;

import com.qmonix.sdk.utils.Utils;

import com.qmonix.sdk.exceptions.IllegalEventStateException;


/**
 * Volume event class extends {@link Event Event}. Besides tag name and time when it was fired,
 * volume event also has a volume (quantity) associated associated with it.
 * <ul>
 * <li>{@link #getTag getTag} - returns event tag name
 * <li>{@link #getTimeArised getTimeArised} - returns event fire time.
 * <li>{@link #getVolume getVolume} - returns event volume.
 * <li>{@link #fire fire} captures event fire time which cannot be changed in the future. After
 * firing an event, it becomes unusable. If one tries to invoke {@link #fire fire} more than once,
 * the exception will be thrown.
 * <li>{@link #toJson toJson} encodes event information to JSON object.
 * </ul>
 *
 * @see EventDispatcher
 * @see exceptions.IllegalEventStateException
 */
public class VolumeEvent extends Event {

	private State state = State.INITIAL;
	private long volume;


	/**
	 * Constructs new single event with a specified tag name.
	 *
	 * @param tag event tag name.
	 * @param volume event volume. Positive number.
	 * @throws IllegalArgumentException if volume is negative number.
	 */
	public VolumeEvent(String tag, long volume) {
		super(tag);

		if (volume < 0) {
			String msg = "Event volume must be positive integer.";
			throw new IllegalArgumentException(msg);
		}

		this.volume = volume;
	}


	/**
	 * Returns event volume.
	 *
	 * @return event volume.
	 */
	public long getVolume() {
		return this.volume;
	}


	/**
	 * Sets event volume.
	 *
	 * @param volume event volume.
	 * @throws IllegalArgumentException if volume is negative number.
	 */
	public void setVolume(long volume) {
		if (volume < 0) {
			String msg = "Event volume must be positive integer.";
			throw new IllegalArgumentException(msg);
		}

		this.volume = volume;
	}


	/**
	 * Serializes event to JSON object which is ready to be encoded to event message. Event
	 * fire time, volume and tag name are used.
	 *
	 * @return event encoded in JSON format.
	 * @throws JSONException if fails to encode event message to JSON object.
	 */
	public JSONObject toJson() throws JSONException {
		JSONObject json = super.toJson();
		json.put("volume", this.volume);

		return json;
	}


	/**
	 * All possible volume event states.
	 * <ul>
	 * <li> {@code INITIAL} - when volume event object is just created. It's fire time is
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
