package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.R.id;
import org.fourdnest.androidclient.R.layout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class AwesomePagerActivity extends Activity {

	private ViewPager awesomePager;
	private static int NUM_AWESOME_VIEWS = 3;
	private Context cxt;
	private AwesomePagerAdapter awesomeAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		cxt = this;

		awesomeAdapter = new AwesomePagerAdapter();
		awesomePager = (ViewPager) findViewById(R.id.awesomepager);
		awesomePager.setAdapter(awesomeAdapter);
		awesomePager.setCurrentItem(1);
	}

	private class FragAdapter extends FragmentPagerAdapter {

		public FragAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private class AwesomePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return NUM_AWESOME_VIEWS;
		}

		/**
		 * Create the page for the given position. The adapter is responsible
		 * for adding the view to the container given here, although it only
		 * must ensure this is done by the time it returns from
		 * {@link #finishUpdate()}.
		 * 
		 * @param container
		 *            The containing View in which the page will be shown.
		 * @param position
		 *            The page position to be instantiated.
		 * @return Returns an Object representing the new page. This does not
		 *         need to be a View, but can be some other container of the
		 *         page.
		 */
		@Override
		public Object instantiateItem(View collection, int position) {
			View defaultView = findViewById(R.layout.default_view);
			if (defaultView == null) {
				LayoutInflater inflater = LayoutInflater.from(awesomePager
						.getContext());
				defaultView = inflater.inflate(R.layout.default_view,
						awesomePager, false);
			}
			TextView tex = (TextView) defaultView.findViewById(R.id.text_field);
			tex.setText("Testies! :D ");
			for (int i = 0; i < 1000; i++) {
				tex.append("OL");
			}

			// TextView tv = new TextView(cxt);
			// tv.setText("Bonjour PAUG " + position);
			// tv.setTextColor(Color.WHITE);
			// tv.setTextSize(30);

			((ViewPager) collection).addView(defaultView, 0);

			return defaultView;
		}

		/**
		 * Remove a page for the given position. The adapter is responsible for
		 * removing the view from its container, although it only must ensure
		 * this is done by the time it returns from {@link #finishUpdate()}.
		 * 
		 * @param container
		 *            The containing View from which the page will be removed.
		 * @param position
		 *            The page position to be removed.
		 * @param object
		 *            The same object that was returned by
		 *            {@link #instantiateItem(View, int)}.
		 */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		/**
		 * Called when the a change in the shown pages has been completed. At
		 * this point you must ensure that all of the pages have actually been
		 * added or removed from the container as appropriate.
		 * 
		 * @param container
		 *            The containing View which is displaying this adapter's
		 *            page views.
		 */
		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

}