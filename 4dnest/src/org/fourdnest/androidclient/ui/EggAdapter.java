package org.fourdnest.androidclient.ui;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An adapter for serving a list of Egg views to Egg lists in
 * ListStoredEggsFragment. The listed Egg view layouts are defined in layout
 * file egg_element_small
 */
public class EggAdapter extends ArrayAdapter<Egg> {

	/**
	 * Contains the list of Eggs that are served to the list
	 */
	protected List<Egg> eggs;

	/**
	 * The ID of the layout resource that defines a single list element.
	 */
	private int resourceId;

	/**
	 * The ViewGroup(e.g. Listview) that uses this adapter.
	 */
	protected ViewGroup parent;

	/**
	 * Constructs a new EggAdapter.
	 * 
	 * @param parent
	 *            The parent view of the layout element(e.g. ListView) which
	 *            will display the adapter listing.
	 * @param resourceId
	 *            The ID of the layout resource that defines a single list
	 *            element.
	 * @param objects
	 *            List of Eggs that are to be displayed.
	 */
	public EggAdapter(ViewGroup parent, int resourceId, List<Egg> objects) {
		super(parent.getContext(), resourceId, objects);
		this.resourceId = resourceId;
		this.parent = parent;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {

		if (this.resourceId == R.layout.egg_element_large) {
			return bindLargeEggView(arg0, arg1);
		} else if (this.resourceId == R.layout.egg_element_small) {
			return bindSmallEggView(arg0, arg1);
		} else {
			return null;
		}

	}

	/**
	 * Binds and returns a large egg view element. Layout defined in
	 * egg_element_large
	 * 
	 * @param arg0
	 *            The array order of this element.
	 * @param arg1
	 *            The view that is to be inflated and binded.
	 * @return An inflated and binded view.
	 */
	private View bindLargeEggView(int arg0, View arg1) {
		View view = arg1;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(this.resourceId, getParent(), false);
		}
		Egg egg = (Egg) this.getItem(arg0);
		TextView message = (TextView) view.findViewById(R.id.message);
		TextView date = (TextView) view.findViewById(R.id.date);

		// TODO: Proper implementation after thumbnail fetching
		// functionality.
		ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
		TextView author = (TextView) view.findViewById(R.id.author);
		TextView time = (TextView) view.findViewById(R.id.time);
		TextView tags = (TextView) view.findViewById(R.id.tags);

		author.setText(egg.getAuthor());
		message.setText(egg.getCaption());
		date.setText(new Date(egg.getLastUpload()).toString());
		time.setText(new Time(egg.getLastUpload()).toString());

		if (egg.getTags().size() > 0) {
			String eggTags = "";
			for (Tag current : egg.getTags()) {
				eggTags += current.getName() + " ";
			}
			tags.setText(eggTags);
		}

		return view;
	}

	/**
	 * Binds and returns a small egg view element. Layout defined in
	 * egg_element_small
	 * 
	 * @param arg0
	 *            The array order of this element.
	 * @param arg1
	 *            The view that is to be inflated and binded.
	 * @return An inflated and binded view.
	 */
	private View bindSmallEggView(int arg0, View arg1) {
		View view = arg1;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(this.resourceId, getParent(), false);
		}
		//TODO: Value binding
		Egg egg = (Egg) this.getItem(arg0);
		TextView message = (TextView) view.findViewById(R.id.message);
		TextView date = (TextView) view.findViewById(R.id.date);

		// TODO: Proper implementation after thumbnail fetching
		// functionality.
		ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
		return view;
	}

	/**
	 * Returns the ViewGroup, which displays the Egg elements.
	 * 
	 * @return
	 */
	public final ViewGroup getParent() {
		return parent;
	}

}
