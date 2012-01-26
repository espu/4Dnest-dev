package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class ViewEggActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.egg_view);
		
		FrameLayout mediaView = (FrameLayout) findViewById(R.id.media_view);
		
		LayoutInflater inflater = LayoutInflater.from(mediaView.getContext());
		
		//TODO: Inflate based on egg type
		View view = inflater.inflate(R.layout.egg_element_large, mediaView, false);

		mediaView.addView(view);
		
		

	}
	
	/**
	 * Creates the options menu on the press of the Menu button.
	 * 
	 * @param menu The menu to inflate
	 * @return Boolean indicating success of creating the menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_menu, menu);
		return true;
	}
	
	/**
	 * Specifies the action to perform when a menu item is pressed.
	 * 
	 * @param item The MenuItem that was pressed
	 * @return Boolean indicating success of identifying the item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_view_pref:
			startActivity(new Intent(this, PrefsActivity.class));
			return true;
		case R.id.menu_view_help:
			return true;
		case R.id.menu_view_edit:
			return true;
		case R.id.menu_view_delete:
			return true;
		}
		return false;
	}

}
