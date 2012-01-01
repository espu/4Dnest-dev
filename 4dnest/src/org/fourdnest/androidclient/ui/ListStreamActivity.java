package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Util;
import org.fourdnest.androidclient.services.RouteTrackService;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	
		ToggleButton trackButton = (ToggleButton) view.findViewById(R.id.route_tracker_button);
		trackButton.setChecked(Util.isServiceRunning(view.getContext(), RouteTrackService.class));
		
		trackButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), RouteTrackService.class);
				if(Util.isServiceRunning(v.getContext(), RouteTrackService.class)) {
					v.getContext().stopService(intent);
				} else {
					v.getContext().startService(intent);
				}				
			}
		});
		
		Button createButton = (Button) view.findViewById(R.id.create_button);
		createButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						NewEggActivity.class);
				v.getContext().startActivity(intent);

			}
		});

		ListView streamList = (ListView) view.findViewById(R.id.egg_list);
		EggReaderAdapter adapter = new EggReaderAdapter(streamList);
		adapter.setEggs(null);
		streamList.setAdapter(adapter);
		streamList.setOnItemClickListener(new EggElementOnClickListener());
		
		return view;

	}

	@Override
	public int getLayoutId() {
		return R.layout.list_stream_view;
	}

}
