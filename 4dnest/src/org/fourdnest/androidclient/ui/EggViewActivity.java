package org.fourdnest.androidclient.ui;

import java.util.Date;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EggViewActivity extends NestSpecificActivity {

	private int eggID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle startingExtras = getIntent().getExtras();
		this.eggID = (Integer) startingExtras
				.get(ListStreamActivity.INTENT_EGG_ID);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View getContentLayout(View view) {
		Egg egg = super.application.getStreamEggManager().getEgg(eggID);

		TextView timestamp = (TextView) view.findViewById(R.id.timestamp);
		TextView message = (TextView) view.findViewById(R.id.message);
		TextView tags = (TextView) view.findViewById(R.id.tags);

		timestamp.setText(new Date(egg.getLastUpload()).toString());
		message.setText(egg.getCaption());
		if (!egg.getTags().isEmpty()) {
			String tagList = "";
			for (Tag current : egg.getTags()) {
				tagList += current.toString();
			}
			tags.setText(tagList);
		}

		return view;
	}

	@Override
	public int getLayoutId() {
		return R.layout.egg_view;
	}
}
