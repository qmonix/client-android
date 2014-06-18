package com.qmonix.sdk;


/**
 * Extends timing event so that it would be easier to fire it to the dispatcher. Adds fire() method.
 */
public class FireableTimingEvent extends TimingEvent {

	private EventDispatcher dispatcher;

	/**
	 * Constructs new fireable timing event and associates the specified dispatcher with it.
	 *
	 * @param tag event name.
	 * @param dispatcher event dispatcher that accepts event after fire() is invoked.
	 */
	public FireableTimingEvent(String tag, EventDispatcher dispatcher) {
		super(tag);

		if (dispatcher == null) {
			throw new IllegalArgumentException("Event dispatcher cannot be null.");
		}

		this.dispatcher = dispatcher;
	}

	/**
	 * Stops timing event and submits it to the event dispatcher specified in the constructor.
	 */
	public void fire() {
		this.stop();
		this.dispatcher.submit(this);
	}

}
