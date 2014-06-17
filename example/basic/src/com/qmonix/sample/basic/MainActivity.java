package com.qmonix.sample.basic;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;

import com.qmonix.sdk.Tracker;
import com.qmonix.sdk.TimingEvent;
import com.qmonix.sdk.Event;
import com.qmonix.sdk.DefaultEventDispatcher;
import com.qmonix.sdk.exceptions.DefaultEventDispatcherException;


public class MainActivity extends Activity implements OnItemSelectedListener {
	private static final String EVENTS_LIST_CAPTION = "Events to dispatch:\n";

	// UI controls.
	private TextView logTextView;
	private TextView eventsText;
	private Spinner timingEventSpinner;
	private ArrayAdapter<String> timingEventAdapter;
	private ScrollView eventScrollView;
	private ScrollView logScrollView;
	private EditText singleEventTagEditText;

	// Timing event control buttons.
	private Button pauseButton;
	private Button resumeButton;
	private Button stopButton;

	private String singleEventDefaultTag = "qmonix/android/sample/basic/single_event";
	private String timingEventTagBase = "qmonix/android/sample/basic/timing_event";

	private HashMap<String, TimingEvent> timingEvents = new HashMap<String, TimingEvent>();
	private HashMap<String, String> timingEventsState = new HashMap<String, String>();

	private TimingEvent selectedTimingEvent;

	private String logText = "";
	private int logElementId = 0;


	/**
	 * Timing events are named from 1 to n increasing each event by 1 every time new timing
	 * event is created.
	 */
	private int newTimingEventId = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Tracker.init(new DefaultEventDispatcher("http://demo.qmonix.com/event/"));

		this.eventScrollView = (ScrollView)this.findViewById(R.id.eventScrollView);
		this.logScrollView = (ScrollView)this.findViewById(R.id.logScrollView);

		this.logTextView = (TextView)this.findViewById(R.id.logText);

		this.eventsText = (TextView)this.findViewById(R.id.eventsToDispatchText);
		this.eventsText.setText(this.EVENTS_LIST_CAPTION);

		this.timingEventAdapter = new ArrayAdapter(this,
			android.R.layout.simple_spinner_item);

		this.timingEventSpinner = (Spinner)this.findViewById(R.id.timingEventSpinner);
		this.timingEventSpinner.setAdapter(this.timingEventAdapter);
		this.timingEventSpinner.setOnItemSelectedListener(this);

		this.pauseButton = (Button)this.findViewById(R.id.pauseEventButton);
		this.pauseButton.setEnabled(false);
		this.resumeButton = (Button)this.findViewById(R.id.resumeEventButton);
		this.resumeButton.setEnabled(false);
		this.stopButton = (Button)this.findViewById(R.id.stopEventButton);
		this.stopButton.setEnabled(false);

		this.singleEventTagEditText = (EditText)this.findViewById(R.id.singleEventTag);
		this.singleEventTagEditText.setText(this.singleEventDefaultTag);

		this.log("Sample started.");
	}

	/**
	 * Button "Fire event" onClick handler. Fires a single event.
	 */
	public void onFireEventClick(View view) {
		String tag = this.singleEventTagEditText.getText().toString();
		Tracker.fire(tag);

		this.log("Single event fired: " + tag);
		eventsText.append(tag + "\n");
	}

	/**
	 * Button "Dispatch" onClick handler. Tries to send events to the Server. On success it
	 * clears the list of events that must be dispatched. On error this list remains unchanged.
	 */
	public void onDispatchClick(View view) {
		this.log("Dispatching events...");

		DefaultEventDispatcher dispatcher = (DefaultEventDispatcher)Tracker.getDispatcher();
		try {
			dispatcher.sendToServer();

			this.log("Events were successfully sent to the server.");
			eventsText.setText(this.EVENTS_LIST_CAPTION);

		} catch (DefaultEventDispatcherException e) {
			this.log("Failed to dispatch events: " + e.getMessage());
		}
	}

	/**
	 * Button "Start timing event" onClick handler. Creates and starts a new timing event,
	 * adds it to the timing event list which is displayed in a Spinner view. You can pick
	 * a running timing event from the list, pause, resume and stop it. When events is stopped
	 * it is removed from timing event list and is added to the event list thats will be sent
	 * to the server when a user hits "Dispatch" button.
	 */
	public void onStartTimingEventClick(View view) {
		this.newTimingEventId += 1;
		String tag = this.timingEventTagBase + Integer.toString(this.newTimingEventId);

		TimingEvent event = Tracker.start(tag);
		this.timingEvents.put(tag, event);
		this.timingEventsState.put(tag, "running");
		this.timingEventAdapter.add(tag);

		this.log("Timing event started: " + tag);
	}

	/**
	 * Button "Pause" onClick handler. Pauses currently chosen timing event.
	 */
	public void onPauseEventClick(View view) {
		this.selectedTimingEvent.pause();

		String selected_tag = this.selectedTimingEvent.getTag();
		this.timingEventsState.put(selected_tag, "paused");

		this.log("Timing event was paused: " + selectedTimingEvent.getTag());

		this.pauseButton.setEnabled(false);
		this.resumeButton.setEnabled(true);
	}

	/**
	 * Button "Resume" onClick handler. Resumes currently chosen timing event.
	 */
	public void onResumeEventClick(View view) {
		this.selectedTimingEvent.resume();

		String selected_tag = this.selectedTimingEvent.getTag();
		this.timingEventsState.put(selected_tag, "running");

		this.log("Timing event was resumed: " + selectedTimingEvent.getTag());

		this.resumeButton.setEnabled(false);
		this.pauseButton.setEnabled(true);
	}

	/**
	 * Button "Stop" onClick handler. Stops currentcly chosen timing event. It is removed
	 * from timing event list and added to the list of events that will be sent to the server
	 * whenever "Dispatch" button is clicked.
	 */
	public void onStopEventClick(View view) {
		this.selectedTimingEvent.fire();

		String tag = this.selectedTimingEvent.getTag();
		this.log("Timing event was stopped: " + tag);
		eventsText.append(tag + "\n");

		this.timingEvents.remove(tag);
		this.timingEventsState.remove(tag);
		this.timingEventAdapter.remove(tag);

		// If there still are some running timing events, update currently chosen timing event.
		if (this.timingEvents.size() > 0) {
			this.timingEventSpinner.setSelection(0);
			String selected_tag = (String)this.timingEventSpinner.getSelectedItem();
			this.selectedTimingEvent = this.timingEvents.get(selected_tag);

			// Depending on timing event state enabled different buttons.
			String state = this.timingEventsState.get(selected_tag);
			if (state == "running") {
				this.pauseButton.setEnabled(true);
				this.resumeButton.setEnabled(false);

			} else if (state == "paused") {
				this.pauseButton.setEnabled(false);
				this.resumeButton.setEnabled(true);
			}
		} else {
			this.pauseButton.setEnabled(false);
			this.resumeButton.setEnabled(false);
			this.stopButton.setEnabled(false);
		}
	}

	/**
	 * Timing event spinner on item selected handler. This is called when you select new timing
	 * event.
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String selected_tag = (String)this.timingEventAdapter.getItem(position);
		this.selectedTimingEvent = this.timingEvents.get(selected_tag);

		// Depending on timing event state enabled different buttons.
		this.prepareTimingEventControls(selected_tag);
	}

	/**
	 * Does nothing. Just implements an interface,
	 */
	public void onNothingSelected(AdapterView<?> parent)
	{
	}

	/**
	 * Depending on current timing event state enable or disable different buttons. Stop button
	 * is always enabled. If event is pause, then pause button is disabled and resume button is
	 * enabled. If event is running (just started or resumed), then resume button is disabled.
	 *
	 * @param selected_tag tag of the currently selected timing event.
	 */
	private void prepareTimingEventControls(String selected_tag) {
		String state = this.timingEventsState.get(selected_tag);
		if (state == "running") {
			this.pauseButton.setEnabled(true);
			this.resumeButton.setEnabled(false);

		} else if (state == "paused") {
			this.pauseButton.setEnabled(false);
			this.resumeButton.setEnabled(true);
		}

		this.stopButton.setEnabled(true);
	}

	/**
	 * This function inserts new text element into log. Each log element has an integer value
	 * which shows the sequence when element was inserted into log. Lower sequence value notes
	 * that log element is older whereas newest log elements appear at the top of the log.
	 *
	 * @param text text to insert into log.
	 */
	private void log(String text) {
		this.logElementId += 1;
		this.logText = Integer.toString(this.logElementId) + ". " + text + "\n"
			+ this.logText;

		this.logTextView.setText("Log: \n" + this.logText);
	}
}
