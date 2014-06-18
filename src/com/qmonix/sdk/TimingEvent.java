package com.qmonix.sdk;

import com.qmonix.sdk.utils.Utils;


/**
 * Timing event is the event that is continuous in time, it has event start time and length. Timing
 * events are identified by unique tag name. This class does not assure that tag names are unique
 * so the user must choose unique tags themselves.
 * <p>
 * Timing event is used to track when particular events started and how long they lasted. E.g.
 * it could be used to measure how long it took a user to fill in registration form. Event would be
 * started once the registration form was displayed to the user and stopped when the user hit
 * "Register" button.
 * <p>
 * Timing event is created with a specified tag name. Event is started on creation. Later it can be
 * paused, resumed, and stopped. timeArised value is the same as timing event construction time.
 * <ul>
 * <li>{@link #pause pause} pauses timing event. Later it can be resumed. E.g. this might be used
 * when application is minimized and one does not want to include this time into measurements. If
 * event was not started or was already paused, exception is thrown.
 * <li>{@link #resume resume} resumes a paused event. After this method call event tracking is
 * continued. If event was not started or pause, exception is thrown.
 * <li>{@link #stop stop} stops tracking the event. After this operation timing event cannot be
 * resumed anymore.
 * </ul>
 *
 * @see Event
 */
public class TimingEvent extends VolumeEvent {

	private State state;
	private long timeStarted;


	/**
	 * Creates a new timing event object by a specified event tag name.
	 *
	 * @param tag even tag name. This is a unique name for every different event. It is used
	 *	as event ID in database. User is responsible to make sure different events
	 *	have different names.
	 */
	public TimingEvent(String tag) {
		super(tag, Utils.getUnixTime(), 0);

		this.state = State.STARTED;
		this.timeStarted = this.timeArised;
	}

	/**
	 * Pauses event tracking if event was started or resumed. Otherwise does nothing.
	 *
	 * @see #resume
	 */
	public void pause() {
		if (this.state == State.STARTED) {
			this.volume = this.volume + (Utils.getUnixTime() - this.timeStarted);
			this.state = State.PAUSED;
		}
	}

	/**
	 * Resumes event tracking if it was paused.
	 *
	 * @see #pause
	 */
	public void resume() {
		if (this.state == State.PAUSED) {
			this.timeStarted = Utils.getUnixTime();
			this.state = State.STARTED;
		}
	}

	/**
	 * Stops a timing event. If was already stops, this method has no effect.
	 */
	public void stop() {
		if (this.state != State.STOPPED) {
			if (this.state != State.PAUSED) {
				this.volume = this.volume + (Utils.getUnixTime() - this.timeStarted);
			}

			this.state = State.STOPPED;
		}
	}


	private enum State {
		STARTED, PAUSED, STOPPED
	}
}
