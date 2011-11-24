package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * This class defines the Fragment that displays the Stored Eggs Tab. Layout is
 * defined in stored_eggs_view.
 */
public class ListStoredEggsFragment extends Fragment {

	/**
	 * Creates a new ListStoredEggsFragment.
	 */
	public ListStoredEggsFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		View view = (View) inflater.inflate(R.layout.stored_eggs_view,
				container, false);
		ListView unsentList = (ListView) view.findViewById(R.id.unsent_eggs);
		ListView sentList = (ListView) view.findViewById(R.id.sent_eggs);

		EggListAdapter unsentListAdapter = new EggListAdapter(unsentList);
		EggListAdapter sentListAdapter = new EggListAdapter(sentList);

		// TODO: proper population of egg lists
		unsentListAdapter.setEggs(null);
		sentListAdapter.setEggs(null);

		unsentList.setAdapter(unsentListAdapter);
		sentList.setAdapter(sentListAdapter);
		// TODO: get context
		// EggManager m = new EggManager(null);

		return view;
	}

}
