package org.fourdnest.androidclient.ui;


import org.fourdnest.androidclient.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * The starting Activity of the application. Contains a ViewPager element for
 * serving Fragments that can be scrolled horizontally. Layout is defined in
 * main.
 */
public class MainTabActivity extends FragmentActivity {

	/**
	 * Reference to the ViewPager element in the layout.
	 */
	private ViewPager mainPager;

	/**
	 * Adapter that serves Fragments to the ViewPager element.
	 */
	private FragAdapter fragadapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mainPager = (ViewPager) findViewById(R.id.main_pager);
		fragadapter = new FragAdapter(getSupportFragmentManager(),
				this.getApplicationContext());
		mainPager.setAdapter(fragadapter);
		mainPager.setCurrentItem(1);
		this.getApplicationContext();

	}
}