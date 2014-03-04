package com.qmonix.sdk;

import com.qmonix.sdk.exceptions.TimeIntervalException;

/**
 * This class deals with time interval which has start and stop time stamps. Time stamps that were
 * not set are called uninitialized. They are marked with a constant {@value UNINITIALIZED_TIME}.
 */
class TimeInterval
{
	public static final long UNINITIALIZED_TIME = -1;

	private long start = UNINITIALIZED_TIME;
	private long stop = UNINITIALIZED_TIME;

	/**
	 * Default constructor. Makes invalid time interval.
	 */
	public TimeInterval()
	{
	}

	/**
	 * Constructs time interval with a specified start and stop values.
	 *
	 * @param start	time interval start time.
	 * @param stop	time interval stop time.
	 * @throws TimeIntervalException if time interval stop stamp is bigger than start stamp.
	 */
	public TimeInterval(long start, long stop) throws TimeIntervalException
	{
		if (stop < start)
		{
			String msg = "time interval stop stamp is bigger than start stamp";
			throw new TimeIntervalException(msg);
		}

		this.start = start;
		this.stop = stop;
	}

	/**
	 * Constructs time interval with a specified start value and invalid stop value.
	 *
	 * @param start	time interval start time.
	 */
	public TimeInterval(long start)
	{
		this.start = start;
	}

	/**
	 * @return time interval start.
	 */
	public long getStart()
	{
		return this.start;
	}

	/**
	 * @return time interval stop.
	 */
	public long getStop()
	{
		return this.stop;
	}

	/**
	 * Sets time interval start stamp.
	 *
	 * @param start	time interval start stamp.
	 * @throws TimeIntervalException if time interval stop stamp is bigger than start stamp.
	 */
	public void setStart(long start) throws TimeIntervalException
	{
		if ((this.stop != UNINITIALIZED_TIME) && (this.stop < start))
		{
			String msg = "time interval stop stamp is bigger than start stamp";
			throw new TimeIntervalException(msg);
		}

		this.start = start;
	}

	/**
	 * Sets time interval stop stamp.
	 *
	 * @param stop	time interval stop stamp.
	 * @throws TimeIntervalException if time interval stop stamp is bigger than start stamp.
	 */
	public void setStop(long stop) throws TimeIntervalException
	{
		if (stop < this.start)
		{
			String msg = "time interval stop stamp is bigger than start stamp";
			throw new TimeIntervalException(msg);
		}

		this.stop = stop;
	}

	/**
	 * Calculates time interval length when start point is included and stop point is not.
	 * E.g. time interval - [15-26); size = 26 - 15 = 12.
	 *
	 * @return time interval size.
	 * @throws TimeIntervalException if one or both of time stamps were not set.
	 */
	public long size()
	{
		if ((this.stop == UNINITIALIZED_TIME) || (this.start == UNINITIALIZED_TIME))
		{
			String msg = "one or both of time stamps were not set";
			throw new TimeIntervalException(msg);
		}

		long size = this.stop - this.start;
		return size;
	}
}

