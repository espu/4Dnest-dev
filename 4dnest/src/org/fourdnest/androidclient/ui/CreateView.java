package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.ui.AwesomePagerActivity.FragAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CreateView extends Fragment {

	FragAdapter adapter = new FragAdapter(getSupportFragmentManager());
	
	public CreateView() {
		super();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
