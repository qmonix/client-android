package com.qmonix.sdk.helpers;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.entity.StringEntity;

import android.os.AsyncTask;

/* AsyncTask.execute() generated exceptions */
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.lang.InterruptedException;

import java.io.UnsupportedEncodingException;
import java.io.IOException;

import com.qmonix.sdk.helpers.exceptions.HttpHelperException;
import com.qmonix.sdk.utils.AsyncTaskResult;
import com.qmonix.sdk.QLog;


/**
 * HTTP helper class that deals with HTTP message transmission. If it is used from the main UI
 * thread {@link #uiPostMessage uiPostMessage} should be used. It uses {@link AsyncTask} to avoid
 * {@link android.os.NetworkOnMainThreadException NetworkOnMainThreadException}. Otherwise it is
 * advised to use {@link #postMessage postMessage}.
 */
public class HttpHelper {

	private String hostname;
	private int port;
	private URI httpPostUri;


	/**
	 * Constructs a new http helper object using a specified server uri.
	 *
	 * @param uri server url.
	 */
	public HttpHelper(String uri) throws URISyntaxException {
		this.httpPostUri = new URI(uri);
		this.hostname = this.httpPostUri.getHost();
		this.port = this.httpPostUri.getPort();
	}

	/**
	 * Does the same as postMessage() except it allows to do network operations on main UI
	 * thread.
	 *
	 * @param message message to bet sent to the server.
	 * @return response from the server.
	 * @see #postMessage
	 */
	public String uiPostMessage(String message) throws HttpHelperException {
		String response = "";

		PostHttpMessage postHttp = new PostHttpMessage();
		postHttp.execute(message);
		try {
			AsyncTaskResult<Object> result = postHttp.get();
			if (result.isException() == false) {
				response = (String)result.getResult();

			} else {
				Exception e = result.getError();
				QLog.error(e.toString());
				throw new HttpHelperException(e.toString());
			}

		} catch(Exception e) {
			QLog.error(e.toString());
			throw new HttpHelperException(e.toString());
		}

		return response;
	}

	/**
	 * Synchronously sends a HTTP POST message meaning that method waits until it receives
	 * a response. The response message is returned. Assumes that a content is in JSON.
	 *
	 * @param message message to bet sent to the server.
	 * @return response from the server.
	 */
	public String postMessage(String message) throws HttpHelperException {
		HttpHost httpHost = new HttpHost(this.hostname, this.port, "http");
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(this.httpPostUri);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String httpResponse = "";

		try {
			StringEntity se = new StringEntity(message);
			httpPost.setEntity(se);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type","application/json");

			httpResponse = httpClient.execute(httpHost, httpPost, responseHandler);

		} catch (UnsupportedEncodingException e) {
			String msg = "Creating http post body entity failed: " + e;
			QLog.error(msg);
			throw new HttpHelperException(msg);

		} catch (IOException e) {
			String msg = "Sending message failed: " + e;
			QLog.error(msg);
			throw new HttpHelperException(msg);

		} catch (Exception e) {
			String msg = "Unknown error: " + e;
			QLog.error(msg);
			throw new HttpHelperException(msg);
		}

		return httpResponse;
	}


	/**
	 * This is a helper class that allows to do network operations on the main UI thread.
	 * It extends AsyncTask class which handles all thread associated activities.
	 */
	private class PostHttpMessage extends AsyncTask<String, Void, AsyncTaskResult<Object>> {

		/**
		 * Sends http POST message.
		 *
		 * @param msgs http messages to send. Only first array element, msgs[0], is used.
		 * @return http response message on success, exception on failure.
		 */
		protected AsyncTaskResult<Object> doInBackground(String... msgs) {
			AsyncTaskResult<Object> retval;

			try{
				String httpResponse = HttpHelper.this.postMessage(msgs[0]);
				retval = new AsyncTaskResult<Object>(httpResponse);

			} catch (Exception e) {
				QLog.error(e.toString());
				retval = new AsyncTaskResult<Object>(e);
			}

			return retval;
		}
	}
}
