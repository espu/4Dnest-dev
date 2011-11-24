package org.fourdnest.androidclient.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class EggElementOnClickListener implements OnItemClickListener{

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(arg1.getContext(),
				ViewEggActivity.class);
		arg0.getContext().startActivity(intent);
		
	}

}
