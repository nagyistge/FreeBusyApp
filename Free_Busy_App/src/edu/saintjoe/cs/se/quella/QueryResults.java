/*
 * QueryResults.java
 * Steven Quella
 * Fall 2013
 * Purpose: Display the results to the query and calendar selection
 */
package edu.saintjoe.cs.se.quella;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Display the resultes to the query and the calendar selection
 * @author Steven Quella
 */
public class QueryResults extends Activity {

	/** the key to get the calendar array */
	private static final String CALENDARS = "calendarArray";
	/** the key to get the events array */
	private static final String EVENTS = "eventArray";
	/** the key to get the starting range */
	private static final String START = "start";
	/** the key to get the end range */
	private static final String END = "end";
	/** the max events that are queried */
	private static final int MAX_EVENTS = 25;
	/** the amount of event rows to display */
	private static final int EVENT_ROWS = 5;
	
	/** the columns to find from the events */
	private static final String[] EVENTCAL_INFO = new String[] {
			Events.CALENDAR_ID
	};
	/** the index of the calendar id column */
	private static final int CALENDAR_ID_INDEX = 0;
	
	/** the formatted start of range */
	String formattedStart;
	/** the formatted end of range */
	String formattedEnd;
	/** the array of selected calendars */
	CalendarInfo[] calendars;
	/** the array of the events in the range */
	EventInfo[] events;
	/** the array of events from the selected calendars in the range */
	EventInfo[] eventsParsed;
	/** the total amount of events parsed */
	int parsedValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_results);
			
		// get the values put from the last activity
		getValues();
		// find the events that are from the calendars selected
		intersectCalEvents();
		// display the event found
		displayEvents();
	}
	
	/**
	 * Get the values from the last activity
	 * @return void
	 */
	public void getValues() {
		
		// the bundle containing the extras passed
		Bundle extras = getIntent().getExtras();
		
		// if there is something there
		if (extras != null) {
		
			// get the strings of the range
			formattedStart = extras.getString(START);
			formattedEnd = extras.getString(END);

			// get the calendars selected from the last activity
			Parcelable[] calendarParcel = extras.getParcelableArray(CALENDARS);
			calendars = new CalendarInfo[calendarParcel.length];
			for (int i = 0; i < calendarParcel.length; i++) {
			    calendars[i] = (CalendarInfo) calendarParcel[i];
			}
			
			// get the event rows from the last activity
			Parcelable[] eventParcel = extras.getParcelableArray(EVENTS);
			events = new EventInfo[eventParcel.length];
			for (int k = 0; k < eventParcel.length; k++) {
			    events[k] = (EventInfo) eventParcel[k];
			}
		}
	}
	
	/**
	 * Find the events that are in the time range and only from the selected calendars.
	 * @return void
	 */
	public void intersectCalEvents() {
	
		// the amount of times to loop
		int loopMax = 0;
		// the content resolver
		ContentResolver cr = getContentResolver();
		
		Uri uri = Events.CONTENT_URI;
		
		if (events.length >= MAX_EVENTS) {
			Toast message = Toast.makeText(this, "Queried maximum events.", Toast.LENGTH_LONG);
			message.show();
			
			loopMax = MAX_EVENTS;
			eventsParsed = new EventInfo[MAX_EVENTS];
		} else {
			loopMax = events.length;
			eventsParsed = new EventInfo[events.length];
		}
		// for every event
		for (int i = 0; i < loopMax; i++) {
		
			// the only value we get
			long calendarId = 0;
			// the query string
			String query = "(" + Events._ID + " = ?)";
			// the parameters for the query
			String[] queryArgs = new String[] { "" + events[i].getId() };
			
			// query the events uri, get the calendar id for the event id
			Cursor cur = cr.query(uri, EVENTCAL_INFO, query, queryArgs, null);
			
			// check if the value is there, only one row each time so just get the one value
			if (cur.moveToFirst()) {
				calendarId = cur.getLong(CALENDAR_ID_INDEX);
				cur.close();
			}
			
			// if the calendar id is in the options selected, store it
			for (int m = 0; m < calendars.length; m++) {
				
				if (calendars[m].getID() == calendarId) {
					// put the event in the parsed events array
					eventsParsed[parsedValue] = events[i];
					// set the remaining values of the event
					eventsParsed[parsedValue].setCalendarId(calendarId);
					eventsParsed[parsedValue].setCalendarName(calendars[m].getDisplayName());
					parsedValue++;
				}
			}
		}
	}
	
	/**
	 * Display the events found on the layout
	 * @return void
	 */
	public void displayEvents() {
		
		// get the linear layout to add to
		LinearLayout eventView = (LinearLayout) findViewById(R.id.event_view);
		// get the textview to display the time range
		TextView timeRange = (TextView) findViewById(R.id.time_range);
		// get the textview to display the amount of events
		TextView eventCount = (TextView) findViewById(R.id.event_count);
		
		// set the text for the textviews
		timeRange.setText(formattedStart + "\n\t - " + formattedEnd);
		eventCount.setText("Events Found: " + parsedValue);
		
		// each event will be displayed as a table of values
		// with five rows
		TableLayout[] eventTables = new TableLayout[parsedValue];
		TableRow[][] eventRows = new TableRow[parsedValue][EVENT_ROWS];
		
		// for all the parsed events
		for (int i = 0; i < parsedValue; i++) {
			// create the table for the event
			eventTables[i] = new TableLayout(this);
			// for all the rows in the event
			for (int j = 0; j < EVENT_ROWS; j++) {
				
				// do something different for each row
				switch (j) {
				
					// for each index, depending on the row
					// create the row
					// get the value to display
					// set the text corresponding to the value
					// add the value 
					// add the views to the row
					// done
				
					// the first row is the calendar name
					case 0: eventRows[i][j] = new TableRow(this);
							TextView calendarName = new TextView(this);
							TextView theId = new TextView(this);
							calendarName.setText("Calendar: ");
							theId.setText(eventsParsed[i].getCalendarName());
							eventRows[i][j].addView(calendarName, 0);
							eventRows[i][j].addView(theId, 1);
							break;
					
					// the second row is the event name
					case 1: eventRows[i][j] = new TableRow(this);
							TextView eventName = new TextView(this);
							TextView theName = new TextView(this);
							eventName.setText("Event: ");
							theName.setText(eventsParsed[i].getTitle());
							eventRows[i][j].addView(eventName, 0);
							eventRows[i][j].addView(theName, 1);
							break;
							
					// the third row is the beginning time
					case 2: eventRows[i][j] = new TableRow(this);
							TextView eventBegins = new TextView(this);
							TextView theBeginTime = new TextView(this);
							eventBegins.setText("Begins: ");
							theBeginTime.setText(eventsParsed[i].getBegin());
							eventRows[i][j].addView(eventBegins, 0);
							eventRows[i][j].addView(theBeginTime, 1);
							break;
							
					// the fourth row is the ending time
					case 3: eventRows[i][j] = new TableRow(this);
							TextView eventEnds = new TextView(this);
							TextView theEndTime = new TextView(this);
							eventEnds.setText("Ends: ");
							theEndTime.setText(eventsParsed[i].getEnd());
							eventRows[i][j].addView(eventEnds, 0);
							eventRows[i][j].addView(theEndTime, 1);
							break;
					
					// the fifth row is the description
					case 4: eventRows[i][j] = new TableRow(this);
							TextView eventDesc = new TextView(this);
							TextView theDesc = new TextView(this);
							eventDesc.setText("Description: ");
							theDesc.setText(eventsParsed[i].getDescription() + "\n");
							eventRows[i][j].addView(eventDesc, 0);
							eventRows[i][j].addView(theDesc, 1);
							break;
				}
				// add each row to the table, created depending on index
				eventTables[i].addView(eventRows[i][j], j);
			}
			// add the event table to the layout
			eventView.addView(eventTables[i], i);
		}
	}
		
	/**
	 * On the click of the button go back to the button
	 * @param view the view of the layout, mandatory
	 */
	public void onClick(View view) {
		
		// create intent to go back to query again
		Intent intent = new Intent(this, QueryCalendars.class);
		// start the activity
		startActivity(intent);
		// done here
		finish();
	}
	
}
		
	

