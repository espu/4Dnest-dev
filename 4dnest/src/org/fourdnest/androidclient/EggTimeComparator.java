package org.fourdnest.androidclient;

import java.util.Comparator;

/**
 * A simple Comparator implementation for sorting Eggs by creation time. Used in
 * EggAdapters to sort lists to display the latest entry on the top of the list
 * view.
 */
public class EggTimeComparator implements Comparator<Egg> {

	// Note the minus sign. Reverses sorting order so that latest entry is
	// displayed on top.
	public int compare(Egg arg0, Egg arg1) {
		return -arg0.getCreationDate().compareTo(arg1.getCreationDate());
	}

}
