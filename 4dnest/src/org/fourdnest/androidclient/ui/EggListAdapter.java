package org.fourdnest.androidclient.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.R;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An adapter for serving a list of Egg views to Egg lists in
 * ListStoredEggsFragment. The listed Egg view layouts are defined in layout
 * file egg_element_small
 */
public class EggListAdapter extends BaseAdapter {

	/**
	 * Contains the list of Eggs that are served to the list
	 */
	protected ArrayList<Egg> eggs;

	/**
	 * The ViewGroup(e.g. Listview) that uses this adapter.
	 */
	protected ViewGroup parent;

	/**
	 * Instantiates a new EggListAdapter.
	 * 
	 * @param parent
	 *            The ViewGroup(e.g. Listview) that uses this adapter to
	 *            populate its view.
	 */
	public EggListAdapter(ViewGroup parent) {
		this.eggs = new ArrayList<Egg>();
		this.setParent(parent);
	}

	public int getCount() {
		return this.eggs.size();
	}

	public Object getItem(int arg0) {
		return this.eggs.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View arg1t = arg1;
		if (arg1t == null) {
			LayoutInflater inflater = LayoutInflater.from(getParent().getContext());
			arg1t = inflater.inflate(R.layout.egg_element_small, getParent(), false);
		}

		Egg egg = (Egg) this.getItem(arg0);
		Date a = new Date(egg.getLastUpload());

		ImageView thumbnail = (ImageView) arg1t.findViewById(R.id.thumbnail);
		TextView message = (TextView) arg1t.findViewById(R.id.message);
		TextView date = (TextView) arg1t.findViewById(R.id.date);

		return arg1t;
	}

	/**
	 * Sets this adapter to serve the given ArrayList of eggs
	 * 
	 * @param eggs
	 *            An ArrayList that contains the eggs that the parent view
	 *            should display.
	 */
	public void setEggs(ArrayList<Egg> eggs) {
		// TODO: Get a real implementation for this
		this.eggs = new ArrayList<Egg>();
		for (int i = 0; i < 8; i++) {
			this.eggs.add(new Egg());
		}
	}

	public final ViewGroup getParent() {
		return parent;
	}

	public final void setParent(ViewGroup parent) {
		this.parent = parent;
	}

}
