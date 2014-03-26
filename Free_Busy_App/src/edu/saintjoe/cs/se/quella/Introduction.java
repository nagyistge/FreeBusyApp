/*
 * Introduction.java
 * Steven Quella
 * Fall 2013
 * Purpose: opening activity of the application, displays purpose
 */

package edu.saintjoe.cs.se.quella;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Launch activity that displays the opening screen of the application.
 * @author Steven Quella
 */
public class Introduction extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// just dump the preset layout to the screen
		setContentView(R.layout.activity_introduction);
	}
	
	/**
	 * On the click of the button, go on to the QueryCalendars activity
	 * @param view the view of the layout to link the method and button, mandatory
	 * @return void 
	 */
	public void onClick(View view) {
		
		// create the intent to go to the next activity
		Intent i = new Intent(this, QueryCalendars.class);
		// start the new activity
		startActivity(i);
		// once that is done, the app is done
		finish();
		
	}
}
