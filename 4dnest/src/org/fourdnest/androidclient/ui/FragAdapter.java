package org.fourdnest.androidclient.ui;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;
	
	public FragAdapter(FragmentManager fm, Context currentContext) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		this.fragments.add(new ListStreamFragment());
		this.fragments.add(new MainScreenFragment(currentContext));
		this.fragments.add(new ListStoredEggsFragment());
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