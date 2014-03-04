package com.qmonix.sdk;

import java.lang.String;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONException;

import com.qmonix.sdk.Event;
import com.qmonix.sdk.EventDispatcher;
import com.qmonix.sdk.TimeInterval;
import com.qmonix.sdk.Tracker;

import com.qmonix.sdk.utils.Utils;
import com.qmonix.sdk.exceptions.IllegalEventStateException;
import com.qmonix.sdk.helpers.LogHelper;

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
 * Timing event is created with a specified tag name. This event might be started, paused, resumed,
 * and fired.
 * <ul>
 * <li>{@link #start start} causes to start tracking the timing event. Event start time is captured
 * whenever this method is invoked. If timing event was already started, this method will throw an
 * exception.
 * <li>{@link #pause pause} pauses timing event. Later it can be resumed. E.g. this might be used
 * when application is minimized and one does not want to include this time into measurements. If
 * event was not started or was already paused, exception is thrown.
 * <li>{@link #resume resume} resumes a paused event. After this method call event tracking is
 * continued. If event was not started or pause, exception is thrown.
 * <li>{@link #fire fire} stops tracking the event and forwards it's data to {@link EventDispatcher}.
 * It uses the same dispatcher as {@link Tracker} does. After this method is invoked, event becomes
 * unusable anymore and cannot be restarted. Any further attempts to use it will throw an exception.
 * Methods that provide event information, like {@link #toJson toJson}, {@link #getTag getTag} are
 * allowed after {@link #fire fire} too.
 * </ul>
 * <p>
 * When paused and resumed timing event collects all the time intervals while it was being tracked.
 * {@link #fire fire} sums the total length of all the intervals and passes it together with the
 * event start time to the dispatcher. E.g. in scenario like this: event is started, then paused,
 * then resumed and finally stopped, there would be two time intervals. First - between start and
 * pause time, second - between resume and stop time. Event dispatcher would receive start time
 * stamp and total timing event length. Which would be calculated like this:
 * (pause time - start time) + (stop time - resume time).
 *
 * @see Event
 * @see Tracker
 * @see EventDispatcher
 */
public class TimingEvent extends VolumeEvent
{
	/* event might be started, paused, resumed,... and have many time intervals */
	private ArrayList<TimeInterval> timeIntervals;
	private TimeInterval lastTimeInterval;
	private State state;

	/**
	 * Creates a new timing event object by a specified event tag name.
	 *
	 * @param tag	even tag name. This is a unique name for every different event. It is used
	 *		as event ID in database. User is responsible to make sure different events
	 *		have different names.
	 */
	public TimingEvent(String tag)
	{
		super(tag, 0);

		this.timeIntervals = new ArrayList<TimeInterval>();
		this.state = State.INITIAL;
	}

	/**
	 * Returns time when event was started. If event was not started yet, throws exception.
	 *
	 * @return event start time.
	 * @throws IllegalEventStateException if event was not started yet.
	 */
	@Override
	public long getTimeArised()
	{
		long retval = TimeInterval.UNINITIALIZED_TIME;

		if (this.state == State.INITIAL)
		{
			String msg = "Event was not started yet";
			throw new IllegalEventStateException(msg);
		}

		if (this.timeIntervals.size() > 0)
		{
			retval = this.timeIntervals.get(0).getStart();
		}
		else
		{
			retval = this.lastTimeInterval.getStart();
		}

		return retval;
	}

	/**
	 * Starts tracking timing event. Sets event start time to method invocation time. If timing
	 * event was already started, exception will be thrown.
	 *
	 * @throws IllegalEventStateException if event was already started.
	 */
	public void start()
	{
		if (this.state == State.STARTED)
		{
			String msg = "Event was already started";
			throw new IllegalEventStateException(msg);
		}

		long timeNow = Utils.getUnixTime();
		this.lastTimeInterval = new TimeInterval(timeNow);

		this.state = State.STARTED;
	}

	/**
	 * Stops a timing event and passes it's info to the event dispatcher. Uses the same event
	 * dispatcher that {@link Tracker} does. Timing event stop time is the same to this
	 * method invocation time. If event was paused event stop time is the same as pause time.
	 *
	 * @throws IllegalEventStateException if event was not started.
	 */
	@Override
	public void fire()
	{
		if ((this.state != State.STARTED) &&
			(this.state != State.PAUSED))
		{
			String msg = "Event was not started";
			throw new IllegalEventStateException(msg);
		}

		/* if event was not paused or was paused and resumed */
		if (this.state == State.STARTED)
		{
			long timeNow = Utils.getUnixTime();
			this.lastTimeInterval.setStop(timeNow);
			this.timeIntervals.add(this.lastTimeInterval);
		}

		this.state = State.FIRED;

		Tracker.getDispatcher().submit(this);
	}

	/**
	 * Pauses event tracking. If event was not started or was already paused, throws exception.
	 * Saves time interval, where start is when event was started or resumed and stop is the
	 * time when this method is invoked.
	 *
	 * @throws IllegalEventStateException if event was not started or was already paused.
	 * @see #resume
	 */
	public void pause()
	{
		if (this.state != State.STARTED)
		{
			String msg = "Event was not started or resumed";
			throw new IllegalEventStateException(msg);
		}

		long timeNow = Utils.getUnixTime();
		this.lastTimeInterval.setStop(timeNow);
		this.timeIntervals.add(this.lastTimeInterval);

		this.state = State.PAUSED;
	}

	/**
	 * Resumes event tracking which was paused. If event was not paused, throws exception.
	 *
	 * @throws IllegalEventStateException if event was not started or paused.
	 * @see #pause
	 */
	public void resume()
	{
		if (this.state != State.PAUSED)
		{
			String msg = "Event was not paused";
			throw new IllegalEventStateException(msg);
		}

		long timeNow = Utils.getUnixTime();
		this.lastTimeInterval = new TimeInterval(timeNow);
		this.state = State.STARTED;
	}

	/**
	 * Serializes event to JSON object which is ready to be encoded to event message. Event
	 * tag name and time intervals are used. If event was not started yet, throws exception.
	 *
	 * @return event encoded in JSON format.
	 * @throws IllegalEventStateException if event was never started.
	 * @throws JSONException if fails to encode event message to JSON object.
	 */
	@Override
	public JSONObject toJson() throws JSONException
	{
		if (this.state == State.INITIAL)
		{
			String msg = "Event was not started yet";
			throw new IllegalEventStateException(msg);
		}

		long timeLength = this.sumTimeIntervals();
		this.setVolume(timeLength);
		JSONObject json = super.toJson();

		return json;
	}

	/**
	 * Traverses all event time intervals and sums the total amount of time that event lasted.
	 *
	 * @return total continuous event time.
	 */
	private long sumTimeIntervals()
	{
		long retval = 0;

		for (TimeInterval ti : this.timeIntervals)
		{
			retval += ti.size();
		}

		return retval;
	}

	/**
	 * All possible timing event states.
	 * <ul>
	 * <li> {@code INITIAL} - when timing event object is just created.
	 * <li> {@code STARTED} - event becomes started after {@code start} or {@code resume} is
	 * invoked.
	 * <li> {@code FIRED} - event becomes stopped after {@ode fire} is invoked. In this state
	 * object becomes unusable and any attempts to use it end up in exception.
	 * <li> {@code PAUSED} - event because paused after {@code pause} is invoked. If
	 * {@code resume} is invoked it will goto {@code STARTED} state.
	 * </ul>
	 */
	private enum State
	{
		INITIAL, STARTED, FIRED, PAUSED
	}
}
