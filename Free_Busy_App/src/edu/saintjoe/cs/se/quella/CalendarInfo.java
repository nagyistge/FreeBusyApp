/*
 * CalendarInfo.java
 * Steven Quella
 * Fall 2013
 * Purpose: create a data model for the calendars used in the application
 */

package edu.saintjoe.cs.se.quella;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object to contain the basic information needed from the content provider.
 * @author Steven Quella
 */
public class CalendarInfo implements Parcelable {
	
	/** the id of the calendar */
	protected long calendarID = 0;
	/** the display name of the calendar */
	protected String displayName = null;
	/** the account the calendar synced from */
	protected String accountName = null;
	/** the owner name of the calendar */
	protected String ownerName = null;
	/** the flag the see if the calendar is visible or not */
	protected int visible = 0;
	
	/**
	 * Constructor for the class, requires all the fields together
	 * @param id the id of the calendar
	 * @param label the display name of the calendar
	 * @param account the account the calendar synced from
	 * @param owner the owner of the calendar
	 */
	public CalendarInfo(long id, String label, String account, String owner, int canSee) {
		
		// assign the values
		calendarID = id;
		displayName = label;
		accountName = account;
		ownerName = owner;
		visible = canSee;
		
	}
	
	/**
	 * Constructor to create the object from a parcel
	 * @param in the parcel passed usually between activities
	 */
	private CalendarInfo(Parcel in) {
		calendarID = in.readLong();
		displayName = in.readString();
		accountName = in.readString();
		ownerName = in.readString();
		visible = in.readInt();
	}
	
	// not used
	@Override
	public int describeContents() {
		return 0;
	}


	/**
	 * Write the contents of the object to a Parcel
	 * @param dest Parcel object the information is going
	 * @param flags
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(calendarID);
		dest.writeString(displayName);
		dest.writeString(accountName);
		dest.writeString(ownerName);
		dest.writeInt(visible);
	}
	
	/**
	 * CREATOR method to manage the type of objects requested
	 */
	public static final Parcelable.Creator<CalendarInfo> CREATOR
    	= new Parcelable.Creator<CalendarInfo>() {
			/**
			 * creates the object from the Parcel
			 * @param in the Parcel object
			 * @return a singular CalendarInfo object
			 */
			public CalendarInfo createFromParcel(Parcel in) {
				return new CalendarInfo(in);
			}

			/**
			 * creates an array of objects from the Parcel
			 * @param size the size of the array
			 * @return a CalendarInfo array
			 */
			public CalendarInfo[] newArray(int size) {
				return new CalendarInfo[size];
			}
	};  
	
	/**
	 * prints some of the basic information of the object
	 * @return the string to display
	 */
	public String toString() {
		
		String string = displayName + "(" + calendarID + ")" + " " + ownerName;
		return string;
		
	}
	
	
	
	// GETTERS
	
	/**
	 * get the calendar id
	 * @return the id
	 */
	public long getID() {
		return calendarID;
	}
	
	/**
	 * get the display name
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * get the account name of the calendar
	 * @return the account name
	 */
	public String getAccountName() {
		return accountName;
	}
	
	/**
	 * get the owner account of the calendar
	 * @return the owner name
	 */
	public String getOwnerAccount() {
		return ownerName;
	}
	
	/**
	 * get the value if the calendar is/is not visible
	 * @return the visibility
	 */
	public int getVisible() {
		return visible;
	}
	
	// if there were setters they would have no effect on the calendar provider
	
}