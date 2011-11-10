package org.fourdnest.androidclient.ui;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;
	
	public FragAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		this.fragments.add(new ReadFragment());
		this.fragments.add(new CreateFragment());
		this.fragments.add(new StoredEggsFragment());
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}