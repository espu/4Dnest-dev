package org.fourdnest.androidclient.ui;

import java.util.ArrayList;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.R;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An adapter for serving a list of Egg views to Egg lists in
 * ListStoredEggsFragment. The listed Egg view layouts are defined in layout
 * file egg_element_large.
 * 
 * @see EggListAdapter.
 */
public class EggReaderAdapter extends EggListAdapter {

	public EggReaderAdapter(ViewGroup parent) {
		super(parent);
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View arg1t = arg1;
		if (arg1t == null) {
			LayoutInflater inflater = LayoutInflater.from(getParent().getContext());
			arg1t = inflater.inflate(R.layout.egg_element_large, getParent(), false);
		}

		Egg egg = (Egg) this.getItem(arg0);

		ImageView thumbnail = (ImageView) arg1t.findViewById(R.id.thumbnail);
		TextView message = (TextView) arg1t.findViewById(R.id.message);
		TextView date = (TextView) arg1t.findViewById(R.id.date);
		
		message.setText(egg.getCaption());

		return arg1t;
	}

	/**
	 * Sets this adapter to serve the given List of eggs
	 * 
	 * @param eggs
	 *            A List that contains the eggs that the parent view
	 *            should display.
	 */
	public void setEggs(List<Egg> eggs) {
		this.eggs = eggs;
	}

}
