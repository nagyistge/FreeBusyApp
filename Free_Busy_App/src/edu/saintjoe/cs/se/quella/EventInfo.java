/*
 * EventInfo.java
 * Steven Quella
 * Fall 2013
 * Purpose: create a data model for the events used in the application
 */

package edu.saintjoe.cs.se.quella;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object to contain the basic information of the events in the application
 * @author Steven Quella
 */
public class EventInfo implements Parcelable {

	/** the event id */
	long id;
	/** the beginning time of the event, formatted */
	String begin;
	/** the ending time of the event, formatted */
	String end;
	/** the title of the event */
	String title;
	/** the description of the event */
	String description;
	
	// these are set later in the application
	// not in constructor or parceled
	/** the calendar id where the event is */
	long calendarId = 0;
	/** the name of the calendar the event is located */
	String calendarName = null;
	
	/**
	 * Constructor to create the object, majority of the fields are required.
	 * 
	 * @param eventId the id of the event
	 * @param beginTime the beginning time of the event
	 * @param endTime the ending time of the event
	 * @param eventTitle the title of the event
	 * @param eventDesc the description of the event
	 */
	public EventInfo(long eventId, String beginTime, String endTime, String eventTitle, String eventDesc) {
		
		// assign the values 
		id = eventId;
		begin = beginTime;
		end = endTime;
		title = eventTitle; 
		description = eventDesc;
	}
	
	/**
	 * Constructor to create the object from the Parcel 
	 * 
	 * @param in the Parcel used when passing between activities
	 */
	private EventInfo(Parcel in) {
		id = in.readLong();
		begin = in.readString();
		end = in.readString();
		title = in.readString();
		description = in.readString();
	}
	
	// not used
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * used to write the contents to the Parcel object
	 * 
	 * @param dest the Parcel to write to
	 * @param flags
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(begin);
		dest.writeString(end);
		dest.writeString(title);
		dest.writeString(description);
	}
	
	/**
	 * CREATOR method used to create the objects, depending on type
	 */
	public static final Parcelable.Creator<EventInfo> CREATOR
		= new Parcelable.Creator<EventInfo>() {
			/**
			 * creates a singular object from the Parcel
			 */
			public EventInfo createFromParcel(Parcel in) {
				return new EventInfo(in);
			}
	
			/**
			 * creates an array of objects
			 */
			public EventInfo[] newArray(int size) {
				return new EventInfo[size];
			}
	};  	
	
	/**
	 * prints some of the basic information of the object
	 * 
	 * @return the string to display
	 */
	public String toString() {
		
		String string = title + "(" + id + ")" + " " + begin + " - " + end;
		return string;
	}
	
	// GETTERS
	
	/**
	 * get the event id
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * get the formatted event beginning
	 * 
	 * @return the string for beginning
	 */
	public String getBegin() {
		return begin;
	}
	
	/**
	 * get the formatted event ending
	 * 
	 * @return the string for ending
	 */
	public String getEnd() {
		return end;
	}
	
	/**
	 * get the title of the event
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * get the event description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * get the calendar id for the event
	 * 
	 * @return the calendar id
	 */
	public long getCalendarId() {
		return calendarId;
	}
	
	/**
	 * get the name of the calendar for the event
	 * 
	 * @return the calendar name
	 */
	public String getCalendarName() {
		return calendarName;
	}
	
	// SETTERS
	// these are not associated with the content provider
	// as they do not alter any of the calendar application event values
	// used to set values after later queries are issued
	
	/**
	 * set the calendar id for the event
	 * 
	 * @param calID the calendar id
	 */
	public void setCalendarId(long calID) {
		calendarId = calID;
	}
	
	/**
	 * set the calendar name for the event
	 * 
	 * @param name the name of the calendar
	 */
	public void setCalendarName(String name) {
		calendarName = name;
	}

}


