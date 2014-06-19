package com.qmonix.sdk;

import com.qmonix.sdk.utils.Utils;



/**
 * Main event tracking class. It has static methods to track single and create timing events. Events
 * are identified by their unique tag name. Each different event has a different tag name. It is up
 * to the user to make sure tags are unique.
 * <p>
 * An event is called single, if it has a single time stamp - when it was fired. This type of event
 * can be used for button clicks, page views, etc. {@link #fire(String) fire} method is used to track
 * single events.
 * <p>
 * An event is called volume event, if it has a single time stamp - when it was fired and volume
 * (quantity) associated with it. {@link #fire(String, long) fire} method is used to track volume
 * events.
 * <p>
 * Timing events have two values: one when the event was started, second how long it lasted. So this
 * type of event is used to track continuous user activities. E.g. it might be be used to track how
 * much time user took to fill registration form. Such event would be started when registration form
 * was displayed to the user. In this case the event could be fired when the user hit "Register"
 * button. Timing events show when specific event started and for how long it lasted.
 * {@link #start start} method creates and returns a new fireable timing event object which can be
 * paused, resumed and fired. More detailed description is in {@link FireableTimingEvent} class.
 * <p>
 * The smallest unit of time is a second. Every event represents a corresponding second - time
 * when it was fired (single events) or started (timing events).
 * <p>
 * By default {@link LogEventDispatcher} is assigned to {@code Tracker}. You can change dispatcher
 * with {@link #setDispatcher}. An actually when using Tracker in production it is advised to set it
 * to {@link HttpEventDispatcher} which sends events to Qmonix Web service.
 * <p>
 * NOTE. You must explicitly tell EventDispatcher to {@link EventDispatcher#dispatch dispatch}
 * events. E.g. Tracker.getDispatcher().dispatch();
 *
 * @see Event
 * @see TimingEvent
 * @see EventDispatcher
 */
public class Tracker {

	private static EventDispatcher dispatcher = new LogEventDispatcher();


	/**
	 * Prevents from instantiating this class.
	 */
	private Tracker() {
	}

	/**
	 * Adds new volume event to the event dispatcher. Event fire time is when this method is
	 * invoked. Throws exception if tracker was not initialized.
	 *
	 * @param tag unique event tag name.
	 * @param volume event volume.
	 */
	public static void fire(String tag, long volume) {
		VolumeEvent event = new VolumeEvent(tag, Utils.getUnixTime(), volume);
		Tracker.dispatcher.submit(event);
	}

	/**
	 * Adds new single event to the event dispatcher. Event fire time is when this method is
	 * invoked. Throws exception if tracker was not initialized.
	 *
	 * @param tag unique event tag name.
	 */
	public static void fire(String tag) {
		Event event = new Event(tag, Utils.getUnixTime());
		Tracker.dispatcher.submit(event);
	}

	/**
	 * Creates and returns a new timing event with a specified tag name. Before returning timing
	 * event it is started. If tracker was not initialized, throws exception.
	 *
	 * @param tag event tag name.
	 * @return new timing event object associated with specified tag.
	 * @see TimingEvent
	 */
	public static FireableTimingEvent start(String tag) {
		FireableTimingEvent result = new FireableTimingEvent(tag, Tracker.dispatcher);
		return result;
	}

	/**
	 * Replaces current dispatcher. Does not execute old dispatcher finalization process. E.g.
	 * in default event dispatcher case, user should manually submit it's events before setting
	 * a new dispatcher, otherwise they would never be sent to the server.
	 *
	 * @param dispatcher new dispatcher.
	 */
	public static void setDispatcher(EventDispatcher dispatcher) {
		Tracker.dispatcher = dispatcher;
	}

	/**
	 * Returns dispatcher currently used by the Trakcer. If Tracker was not initliazed, throws
	 * exception.
	 *
	 * @return dispatcher which is currently used by Tracker.
	 */
	public static EventDispatcher getDispatcher() {
		return Tracker.dispatcher;
	}
}
