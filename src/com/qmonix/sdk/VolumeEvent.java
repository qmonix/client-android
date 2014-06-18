package com.qmonix.sdk;

import org.json.JSONObject;
import org.json.JSONException;

import com.qmonix.sdk.utils.Utils;


/**
 * Volume event class extends {@link Event Event}. Besides tag name and time when it was fired,
 * volume event also has a volume (quantity) associated associated with it.
 * <ul>
 * <li>{@link #getTag getTag} - returns event tag name
 * <li>{@link #getTimeArised getTimeArised} - returns event fire time.
 * <li>{@link #getVolume getVolume} - returns event volume.
 * <li>{@link #toJson toJson} encodes event information to JSON object.
 * </ul>
 */
public class VolumeEvent extends Event {

	protected long volume;


	/**
	 * Constructs new single event with a specified tag name.
	 *
	 * @param tag event tag name.
	 * @param timeArised Unix time stamp when event was fired.
	 * @param volume event volume. Positive number.
	 */
	public VolumeEvent(String tag, long timeArised, long volume) {
		super(tag, timeArised);

		if (volume < 0) {
			String msg = "Event volume must be positive integer.";
			throw new IllegalArgumentException(msg);
		}

		this.volume = volume;
	}

	/**
	 * @return event volume.
	 */
	public long getVolume() {
		return this.volume;
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
}
