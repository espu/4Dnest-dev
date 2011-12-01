package org.fourdnest.androidclient.ui;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * An adapter for serving Fragments to the ViewPager ViewGroup in
 * MainTabActivity. The Fragments that belong under this adapter are
 * ListStreamFragment(Read Tab), MainScreenFragment(Create Tab) and
 * ListStoredEggsFragment(Stored Eggs Tab).
 */
public class FragAdapter extends FragmentPagerAdapter {

	/**
	 * Contains all the Fragments that are used to populate the parent
	 * ViewPager.
	 */
	private ArrayList<Fragment> fragments;

	/**
	 * Creates a new instance of FragAdapter. Initiates the Fragment list with
	 * specified tabs.
	 * 
	 * @param fm
	 *            The FragmentManager for this instance.
	 * @param currentContext
	 *            The Context of this instance.
	 */
	public FragAdapter(FragmentManager fm, Context currentContext) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		
		//Add specified tabs(Fragments) to ArrayList.
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
