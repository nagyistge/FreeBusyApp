/*
 * QueryCalendars.java
 * Steven Quella
 * Fall 2013
 * Purpose: get the list of the calendars, display them, and get the parameters to find events
 */

package edu.saintjoe.cs.se.quella;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Instances;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

/**
 * Activity that gets and displays the calendars, and then the dialogs to find events
 * @author Steven Quella
 */
public class QueryCalendars extends Activity {
	
	/** the key to pass the start time of the range */
	private static final String START = "start";
	/** the key to pass the end time of the range */
	private static final String END = "end";
	/** the key for the array of selected calendars */
	private static final String CALENDARS = "calendarArray";
	/** the key for the array of events in the range */
	private static final String EVENTS = "eventArray";
	
	/** the columns to return from the content provider, id, account name, display name, owner name, visibility */
	private static final String[] CALENDAR_INFO = new String[] {
		Calendars._ID,
		Calendars.ACCOUNT_NAME,
		Calendars.CALENDAR_DISPLAY_NAME,
		Calendars.OWNER_ACCOUNT,
		Calendars.VISIBLE
	};
	
	/** index of the returned calendar id */
	private static final int ID_INDEX = 0;
	/** index of the returned account name */
	private static final int ACCOUNT_NAME_INDEX = 1;
	/** index of the returned display name */
	private static final int DISPLAY_NAME_INDEX = 2;
	/** index of the returned owner name */
	private static final int OWNER_ACCOUNT_INDEX = 3;
	/** index of the returned visibility value */
	private static final int VISIBLE = 4;
	
	/** the columns to return from the query, event id, beginning, ending, title, description */
	public static final String[] EVENT_INFO = new String[] {
		Instances.EVENT_ID,
		Instances.BEGIN,
		Instances.END,
		Instances.TITLE,
		Instances.DESCRIPTION
	};
	
	/** the index of the returned event id */
	private static final int EVENT_ID = 0;
	/** the index of the returned beginning time */
	private static final int EVENT_BEGINS = 1;
	/** the index of the returned ending time */
	private static final int EVENT_ENDS = 2;
	/** the index of the returned event title */
	private static final int EVENT_TITLE = 3;
	/** the index of the returned event description */
	private static final int EVENT_DESC = 4;
	/** the amount of the date parameter */
	private static final int DATE_PARAMS = 5;

	/** the index of the year */
	private static final int YEAR = 0;
	/** the index of the month */
	private static final int MONTH = 1;
	/** the index of the day */
	private static final int DAY = 2;
	/** the index of the hour */
	private static final int HOUR = 3;
	/** the index of the minute */
	private static final int MINUTE = 4;
	

	/** CalendarInfo array of the calendars returned */
	private CalendarInfo[] calendars;
	/** CheckBox array of the checkboxes to display */
	private CheckBox[] checkboxes;
	/** CalendarInfo array of the calendars selected */
	private CalendarInfo[] selectedCalendars;
	/** EventInfo array of the events found from the calendars selected */
	private EventInfo[] events;
	/** Dialog to choose start of range */
	private Dialog startSelection;
	/** Dialog to choose end of range */
	private Dialog endSelection;
	/** Button to set the beginning of range */
	private Button setBegin;
	/** Button to set the end of the range */
	private Button setEnd;
	/** the formatted string of the beginning of range */
	private String formattedStart;
	/** the formatted string of the end of the range */
	private String formattedEnd;
	/** TimePicker to pick the start time */
	private TimePicker startTime;
	/** TimePicker to pick the end time */
	private TimePicker endTime;
	/** DatePicker to pick the start date */
	private DatePicker startDate;
	/** DatePicker to pick the end date */
	private DatePicker endDate;
	/** the array of the values for the start date/time */
	private int[] startDateTime = new int[DATE_PARAMS];
	/** the array of the values for the start date/time */
	private int[] endDateTime = new int [DATE_PARAMS];
	/** the string to display in the dialog title */
	private String displayString = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_calendars);

		// set up the dialogs
		setUpDialogs();
		// get the list of calendars
		getCalendars();
		// display the calendars to the screen
		displayCalendars();
	}
	
	/**
	 * Create the dialogs to select the date range to find events
	 * @return void
	 */
	public void setUpDialogs() {
		
		// THE FIRST DIALOG, starting date/time
		
		// set up the first dialog
		startSelection = new Dialog(QueryCalendars.this);
		startSelection.setContentView(R.layout.dialog_layout);
		startSelection.setTitle("Enter Starting Time: (scrollable)");
		// initialize the second dialog
		endSelection = new Dialog(QueryCalendars.this);
		endSelection.setContentView(R.layout.dialog_layout_end);
		
		// initialize the pickers
		startDate = (DatePicker) startSelection.findViewById(R.id.startDate);
		startTime = (TimePicker) startSelection.findViewById(R.id.startTime);
		endDate = (DatePicker) endSelection.findViewById(R.id.endDate);
		endTime = (TimePicker) endSelection.findViewById(R.id.endTime);
		
		// initialize the buttons
		setBegin = (Button) startSelection.findViewById(R.id.selectTimes);
		setEnd = (Button) endSelection.findViewById(R.id.finishTimes);
		
		// create a click listener
		setBegin.setOnClickListener(new View.OnClickListener() {
			
			// when the button inside of the dialog is pressed
			@Override
			public void onClick(View v) {
				
				// get the current state of the values
				startDateTime[YEAR] = startDate.getYear();
				startDateTime[MONTH] = startDate.getMonth();
				startDateTime[DAY] = startDate.getDayOfMonth();
				startDateTime[HOUR] = startTime.getCurrentHour();
				startDateTime[MINUTE] = startTime.getCurrentMinute();
				
				// if the minute is less than 10, add a 0 to the display
				// add 1 to the month regardless, months are 0 based
				if (startDateTime[MINUTE] < 10) {
					displayString = startDateTime[YEAR]+"/"+(startDateTime[MONTH]+1)+"/"+startDateTime[DAY]
									+" "+startDateTime[HOUR]+":0"+startDateTime[MINUTE];
				} else {
					displayString = startDateTime[YEAR]+"/"+(startDateTime[MONTH]+1)+"/"+startDateTime[DAY]
							+" "+startDateTime[HOUR]+":"+startDateTime[MINUTE];
				}
				// after the first dialog button is pressed, close it
				startSelection.dismiss();
				// set the title of the second dialog with the starting date set
				endSelection.setTitle(displayString + " to ");
				// once configured show the second dialog
				endSelection.show();
			}
		});
		
		// THE SECOND DIALOG, ending date/time
		
		// create the click listener
		setEnd.setOnClickListener(new View.OnClickListener() {
			
			// when the button inside of the dialog is pressed
			@Override
			public void onClick(View v) {
				
				// get the current state of the values
				endDateTime[YEAR] = endDate.getYear();
				endDateTime[MONTH] = endDate.getMonth();
				endDateTime[DAY] = endDate.getDayOfMonth();
				endDateTime[HOUR] = endTime.getCurrentHour();
				endDateTime[MINUTE] = endTime.getCurrentMinute();
				
				// once the values are set, close the dialog
				endSelection.dismiss();
				// get all the events in the selected range
				queryCalendars();
				// go on to the results page
				finishActivity();
			}
		});
	}
	
	/**
	 * when the button is pressed, start the dialogs and get the checked calendars
	 * @param view the view of the layout to connect button and click, mandatory
	 */
	public void onClick(View view) {
		
		// get calendars checked
		getChecked();
		// start the dialogs
		startSelection.show();

	}
	
	/**
	 * get the list of the calendars from the content provider
	 * @return void
	 */
	public void getCalendars() {
		
		// comments and query structure put in relation to SQL relation
		
		// the "database" that handles the query
		ContentResolver cr = getContentResolver();
		
		// the SELECT clause
		// defined above CALENDAR_INFO
		
		// the FROM clause
		Uri uri = Calendars.CONTENT_URI;
		
		// the WHERE clause
		// null, because we want all the calendars
		
		// the query goes into the resulting rows
		Cursor cur = cr.query(uri, CALENDAR_INFO, null, null, Calendars._ID);
		
		// set up a counter and allocate our array of calendar information
		int i = 0;
		calendars = new CalendarInfo[cur.getCount()];
		// suggested way to go through the cursor data, moving from one row to the next
		while (cur.moveToNext()) {
			
			// the data for each row
			long calID = 0;
		    String displayName = null;
		    String userName = null;
		    String ownerName = null;
		    int visible = 0;
		    
		    // Get the field values
		    calID = cur.getLong(ID_INDEX);
		    displayName = cur.getString(DISPLAY_NAME_INDEX);
		    userName = cur.getString(ACCOUNT_NAME_INDEX);
		    ownerName = cur.getString(OWNER_ACCOUNT_INDEX);
		    visible = cur.getInt(VISIBLE);
		    
		    // store that data into the created object array, for later use
		    calendars[i] = new CalendarInfo(calID, displayName, userName, ownerName, visible);
		    
		    // next spot in the array please
		    i++;
		}
	}
	
	/**
	 * Once we have the the calendars from the content provider, display to the layout.
	 * @return void
	 */
	public void displayCalendars() {
		
		// for all the calendars on the screen, create a checkbox
		checkboxes = new CheckBox[calendars.length];
		// the view to add the checkboxes to
		LinearLayout layout = (LinearLayout) findViewById(R.id.checkbox_list);
		for (int i = 0; i < calendars.length; i++) {
			// create a checkbox
			checkboxes[i] = new CheckBox(this);
			// set the checkbox text as the calendar display name
			checkboxes[i].setText(calendars[i].getDisplayName());
			// if the calendar is visible in the content provider, set it as checked
			if (calendars[i].getVisible() == 1) {
				checkboxes[i].setChecked(true);
			}
			// add the checkbox to the view
			layout.addView(checkboxes[i]);
		}
	}
	
	/**
	 * Once times are ready to be entered, get the calendars that are checked
	 * @return void
	 */
	public void getChecked() {
		
		// the total amount of calendars checked
		int amountChecked = 0;
		// a counter
		int k = 0;
		
		// need a value to initialize the array, the total amount checked
		for (int i = 0; i < checkboxes.length; i++) {
			if (checkboxes[i].isChecked()) {
				amountChecked++;
			}
		}
		
		// now we can initialize our array
		selectedCalendars = new CalendarInfo[amountChecked];
		// if the certain checkbox is checked, add it to the array
		for (int j = 0; j < checkboxes.length; j++) {
			if (checkboxes[j].isChecked()) {
				selectedCalendars[k] = calendars[j];
				k++;
			}
		}
	}

	/**
	 * Once the selected range is found, find the events in the range
	 * @return void
	 */
	public void queryCalendars() {
		
		// to format the beginning and ending times for viewing
		DateFormat formatter = SimpleDateFormat.getDateTimeInstance();
		
		// once again, the comments are structured to relate to SQL
		// the "database"
		ContentResolver cr = getContentResolver();
		
		// the SELECT clause
		// defined above EVENT_INFO
		
		// the FROM clause
		// for the Instances table this is the contentResolver
		
		// the WHERE clause
		long startMillis = 0;
		long endMillis = 0;
		
		// format the beginning time, for the query(in milliseconds) and to format the value(in a String)
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(startDateTime[YEAR], startDateTime[MONTH], startDateTime[DAY], startDateTime[HOUR], startDateTime[MINUTE], 0);
		startMillis = beginTime.getTimeInMillis();
		formattedStart = formatter.format(beginTime.getTime());
		
		// do the same for the ending time
		Calendar endTime = Calendar.getInstance();
		endTime.set(endDateTime[YEAR], endDateTime[MONTH], endDateTime[DAY], endDateTime[HOUR], endDateTime[MINUTE], 0);
		endMillis = endTime.getTimeInMillis();
		formattedEnd = formatter.format(endTime.getTime());
						
		// the query
		// a built in query method specifically for this task, put in the resulting rows
		Cursor cur = Instances.query(cr, EVENT_INFO, startMillis, endMillis);
		
		// counter to add events
		int i = 0;
		// initialize the events array to the row count
		events = new EventInfo[cur.getCount()];
		// move through the rows
		while (cur.moveToNext()) {
			
			// formatted strings of the start and end of the event
			String eventStart = null;
			String eventEnd = null;
			
			// data for each row
			long id = 0;
			long begin = 0;
			long end = 0;
			String title = null;
			String description = null;
			
			// get the values, using the indices
			id = cur.getLong(EVENT_ID);
			begin = cur.getLong(EVENT_BEGINS);
			end = cur.getLong(EVENT_ENDS);
			title = cur.getString(EVENT_TITLE);
			description = cur.getString(EVENT_DESC);
			
			// format the times from milliseconds
			Calendar calStart = Calendar.getInstance();
			calStart.setTimeInMillis(begin);
			eventStart = formatter.format(calStart.getTime());
			
			// do the same for the ending time
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(end);
			eventEnd = formatter.format(calEnd.getTime());

			// with all the information create an event object
			events[i] = new EventInfo(id, eventStart, eventEnd, title, description);
			// next array slot please
			i++;
			
		}
	}
	
	/**
	 * Once the activity is done, prepare the values and pass them, and done
	 * @return void
	 */
	public void finishActivity() {
		
		// we should now have two arrays ready for the next activity
		// one with the calendars selected, one with the events in the time frame
		
		// create the new intent
		Intent intent = new Intent(this, QueryResults.class);
		
		// put the formatted time range
		intent.putExtra(START, formattedStart);
		intent.putExtra(END, formattedEnd);
		
		// put the selected calendars to the next activity
		intent.putExtra(CALENDARS, selectedCalendars);
		intent.putExtra(EVENTS, events);
		
		// start the next activity
		startActivity(intent);
		// done here
		finish();
	}
}
