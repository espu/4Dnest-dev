package org.fourdnest.androidclient.ui;

import java.util.ArrayList;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Util;
import org.fourdnest.androidclient.services.RouteTrackService;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

/**
 * The starting activity of the application. Displays a list of eggs present on
 * the server. Also provides functionality to access the create view and for
 * toggling route tracking.
 */
public class ListStreamActivity extends NestSpecificActivity {

	@Override
	public View getContentLayout(View view) {

		FourDNestApplication application = (FourDNestApplication) getApplication();
		EggManager manager = application.getStreamEggManager();

		ArrayList<Egg> eggs = (ArrayList<Egg>) application.getCurrentNest()
				.getProtocol().getStream();
		if (eggs != null) {
			for (Egg egg : eggs) {
				manager.saveEgg(egg);
			}
		}

		ToggleButton trackButton = (ToggleButton) view
				.findViewById(R.id.route_tracker_button);
		trackButton.setChecked(Util.isServiceRunning(view.getContext(),
				RouteTrackService.class));

		trackButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						RouteTrackService.class);
				if (Util.isServiceRunning(v.getContext(),
						RouteTrackService.class)) {
					v.getContext().stopService(intent);
				} else {
					v.getContext().startService(intent);
				}
			}
		});

		Button createButton = (Button) view.findViewById(R.id.create_button);
		createButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), NewEggActivity.class);
				v.getContext().startActivity(intent);

			}
		});

		ListView streamList = (ListView) view.findViewById(R.id.egg_list);
		EggReaderAdapter adapter = new EggReaderAdapter(streamList);
		adapter.setEggs(manager.listEggs());
		streamList.setAdapter(adapter);
		streamList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(arg1.getContext(),
						ViewEggActivity.class);
				arg0.getContext().startActivity(intent);

			}
		});

		return view;

	}

	@Override
	public int getLayoutId() {
		return R.layout.list_stream_view;
	}
	
	/**
	 * Creates the options menu on the press of the Menu button.
	 * 
	 * @param menu The menu to inflate
	 * @return Boolean indicating success of creating the menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.stream_menu, menu);
		return true;
	}
	
	/**
	 * Specifies the action to perform when a menu item is pressed.
	 * 
	 * @param item The MenuItem that was pressed
	 * @return Boolean indicating success of identifying the item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_stream_pref:
			startActivity(new Intent(this, PrefsActivity.class));
			return true;
		case R.id.menu_stream_help:
			return true;
		case R.id.menu_stream_nests:
			return true;
		case R.id.menu_stream_drafts:
			return true;
		}
		return false;
	}

}
