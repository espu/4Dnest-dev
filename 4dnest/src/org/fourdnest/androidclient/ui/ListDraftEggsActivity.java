package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListDraftEggsActivity extends NestSpecificActivity {

	private EggManager draftManager;
	
	/** Called when this Activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.draftManager = ((FourDNestApplication) getApplication())
				.getDraftEggManager();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View getContentLayout(View view) {
		initializeDraftList(this.draftManager, (ListView) view.findViewById(R.id.draft_list));
		return view;
	}

	@Override
	public int getLayoutId() {
		return R.layout.list_drafts_activity;
	}
	
	private void initializeDraftList(EggManager manager, ListView draftListView) {
		EggAdapter adapter = new EggAdapter(draftListView,
				R.layout.egg_element_draft, manager.listEggs());
		draftListView.setAdapter(adapter);
		draftListView.setOnItemClickListener(new EggItemOnClickListener(draftListView));
	}
	
	private void initializeSendAllButton(Button button) {
		button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				while (!ListDraftEggsActivity.this.draftManager.listEggs().isEmpty()) {
					//TODO: Send all the things!
				}
				
			}
		});
	}

}
