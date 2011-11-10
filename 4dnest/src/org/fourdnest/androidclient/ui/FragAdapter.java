package org.fourdnest.androidclient.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragAdapter extends FragmentPagerAdapter {
	private final int NUM_VIEWS = 3;
	private Fragment[] fragments;
	
	public FragAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new Fragment[NUM_VIEWS];
		// TODO
	}

	@Override
	public Fragment getItem(int arg0) {
		return this.fragments[arg0];
	}

	@Override
	public int getCount() {
		return NUM_VIEWS;
	}

}