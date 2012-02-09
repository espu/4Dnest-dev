package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.EggTimeComparator;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListDraftEggsActivity extends NestSpecificActivity {

	private EggManager draftManager;
	ListView draftListView;
	Button sendAllButton;

	/** Called when this Activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.draftManager = ((FourDNestApplication) getApplication())
				.getDraftEggManager();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		initializeDraftList(this.draftManager);
		super.onResume();
	}

	@Override
	public View getContentLayout(View view) {
		this.sendAllButton = (Button) view.findViewById(R.id.send_all_button);
		this.draftListView = (ListView) view.findViewById(R.id.draft_list);
		initializeDraftList(this.draftManager);
		initializeSendAllButton(this.sendAllButton);
		return view;
	}

	@Override
	public int getLayoutId() {
		return R.layout.list_drafts_activity;
	}

	private void initializeDraftList(EggManager manager) {
		EggAdapter adapter = new EggAdapter(draftListView,
				R.layout.egg_element_draft, manager.listEggs());
		draftListView.setAdapter(adapter);
		((EggAdapter) draftListView.getAdapter()).sort(new EggTimeComparator());
		((EggAdapter) draftListView.getAdapter()).notifyDataSetChanged();
		draftListView.setOnItemClickListener(new DraftListOnItemClickListener(
				this.draftListView));
	}

	private void initializeSendAllButton(Button button) {
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EggAdapter draftAdapter = (EggAdapter) ListDraftEggsActivity.this.draftListView
						.getAdapter();
				Egg current = null;
				while (!draftAdapter.isEmpty()) {
					current = draftAdapter.getItem(0);
					// TODO: Wait for sendAll in Services. Use that instead of
					// sending each egg individually.
					// SendQueueService.sendEgg(getApplicationContext(),
					// current, true);
					draftAdapter.remove(current);
				}
				draftAdapter.notifyDataSetChanged();

			}
		});
	}

}
