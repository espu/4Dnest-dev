package org.fourdnest.androidclient.ui;

import java.util.ArrayList;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EggListAdapter extends BaseAdapter {
	private ArrayList<Egg> eggs;
	private ViewGroup parent;
	
	public EggListAdapter(ViewGroup parent) {
		this.eggs = new ArrayList<Egg>();
		this.parent = parent;
	}

	public int getCount() {
		return this.eggs.size();
	}

	public Object getItem(int arg0) {
		return this.eggs.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		if (arg1 == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			arg1 = inflater.inflate(R.layout.egg_element_small, parent, false);
		}
		Egg egg = (Egg) this.getItem(arg0);
		
		ImageView thumbnail = (ImageView) arg1.findViewById(R.id.thumbnail);
		TextView message = (TextView) arg1.findViewById(R.id.message);
		TextView date = (TextView) arg1.findViewById(R.id.date);
		
		return arg1;
	}
	
	public void setEggs(ArrayList<Egg> eggs) {
		//TODO: Get a real implementation for this
		this.eggs = new ArrayList<Egg>();
		this.eggs.add(new Egg());
	}

}
