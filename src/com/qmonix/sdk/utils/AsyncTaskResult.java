package com.qmonix.sdk.utils;


/**
 * Class that holds information for AsyncTask.doInBackground() result. It might be either
 * a valid result or an exception.
 */
public class AsyncTaskResult<T> {

	private T result;
	private Exception error;


	/**
	 * Constructs a valid result object.
	 *
	 * @param result AsyncTask.doInBackground() method result.
	 */
	public AsyncTaskResult(T result) {
		this.result = result;
	}

	/**
	 * Constructs a result object that indicates an exception.
	 *
	 * @param error exception object that was thrown during AsyncTask.doInBackground().
	 */
	public AsyncTaskResult(Exception error) {
		this.error = error;
	}

	public T getResult() {
		return this.result;
	}

	public Exception getError() {
		return this.error;
	}

	/**
	 * Checks if result oject is an exception.
	 *
	 * @return true if object is an excetion, otherwise false.
	 */
	public boolean isException() {
		if (this.error != null){
			return true;

		} else {
			return false;
		}
	}

	/**
	 * Checks if object is a valid result. If the result was null it will return false,
	 * no matter that no exception was raised. To be sure better use isException()
	 * to check if AsyncTask.doInBackground() failed or not.
	 *
	 * @return true if object is a valid result, otherise false.
	 */
	public boolean isValidResult() {
		if (this.result != null) {
			return true;

		} else{
			return false;
		}
	}
}
