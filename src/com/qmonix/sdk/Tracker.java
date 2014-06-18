package com.qmonix.sdk;

import java.lang.String;

import com.qmonix.sdk.utils.Utils;

import com.qmonix.sdk.exceptions.UninitializedTrackerException;


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
 * {@link #start start} method creates and returns a new timing event object which can be paused,
 * resumed and fired. More detailed description is in {@link TimingEvent} class.
 * <p>
 * The smallest unit of time is a second. Every event represents a corresponding second - time
 * when it was fired (single events) or started (timing events).
 * <p>
 * Before starting to use {@code Tracker} {@link #init init} must be called, otherwise any attempts
 * to call {@link #start start}, {@link #fire fire}, {@link #getDispatcher getDispatcher} will throw
 * an exception.
 * <p>
 * When the events are fired they are forwarded to event dispatcher which is reponsible for
 * collecting and dispatching the events. Initially {@code Tracker} uses default dispatcher which is
 * described in {@link DefaultEventDispatcher}. But it is possible to use custom dispatcher which
 * should implement {@link EventDispatcher} interface. {@link #setDispatcher setDispatcher} is used
 * to replace default dispatcher. It's up to the user to {@link DefaultEventDispatcher#submit submit}
 * events, otherwise if default dispathcer had collected events, they would not be sent to the
 * Server.
 *
 * @see Event
 * @see TimingEvent
 * @see EventDispatcher
 * @see DefaultEventDispatcher
 * @see UninitializedTrackerException
 */
public class Tracker {
	private static EventDispatcher dispatcher;

	/**
	 * Prevents from instantiating this class.
	 */
	private Tracker() {
	}

	/**
	 * Checks if {@code Tracker} singleton was initialized. If it was, returns silently,
	 * otherwise exception is thrown.
	 */
	private static void checkInitialized() {
		if (Tracker.dispatcher == null) {
			String msg = "Tracker was not initialized";
			throw new UninitializedTrackerException(msg);
		}
	}

	/**
	 * Initializes {@code Tracker} singleton by assigning specified event dispatcher object.
	 * This must bet called before starting to use {@code Tracker}. Any attempts to call methods
	 * like {@link #fire fire}, {@link #start start} before initializing tracker will result in
	 * exception.
	 *
	 * @param dispatcher event dispatcher object.
	 */
	public static void init(EventDispatcher dispatcher) {
		Tracker.dispatcher = dispatcher;
	}

	/**
	 * Adds new volume event to the event dispatcher. Event fire time is when this method is
	 * invoked. Throws exception if tracker was not initialized.
	 *
	 * @param tag unique event tag name.
	 * @param volume event volume.
	 * @throws UninitializedTrackerException if {@link #init} was never invoked.
	 * @see #init
	 */
	public static void fire(String tag, long volume) {
		checkInitialized();
		VolumeEvent event = new VolumeEvent(tag, volume);
		event.fire();
	}


	/**
	 * Adds new single event to the event dispatcher. Event fire time is when this method is
	 * invoked. Throws exception if tracker was not initialized.
	 *
	 * @param tag unique event tag name.
	 * @throws UninitializedTrackerException if {@link #init} was never invoked.
	 * @see #init
	 */
	public static void fire(String tag) {
		checkInitialized();
		Event event = new Event(tag);
		event.fire();
	}


	/**
	 * Creates and returns a new timing event with a specified tag name. Before returning timing
	 * event it is started. If tracker was not initialized, throws exception.
	 *
	 * @param tag event tag name.
	 * @return new timing event object associated with specified tag.
	 * @see TimingEvent
	 * @throws UninitializedTrackerException if {@link #init} was never invoked.
	 * @see #init
	 */
	public static TimingEvent start(String tag) {
		checkInitialized();
		TimingEvent newEvent = new TimingEvent(tag);
		newEvent.start();
		return newEvent;
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
	 * @throws UninitializedTrackerException if {@link #init} was never invoked.
	 * @see #init
	 */
	public static EventDispatcher getDispatcher() {
		checkInitialized();
		return Tracker.dispatcher;
	}
}
