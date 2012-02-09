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
	    if (arg0.getCreationDate() == null) {
	        return 1;
	    }
	    if (arg1.getCreationDate() == null) {
	        return 0;
	    }
		int value = -arg0.getCreationDate().compareTo(arg1.getCreationDate());
		
		if (value == 0) {
			value = -arg0.getId().compareTo(arg1.getId());
		}
		if (value == 0) {
			value = -arg0.getAuthor().compareTo(arg1.getAuthor());
		}
		if (value == 0) {
			value = -arg0.getCaption().compareTo(arg1.getCaption());
		}
		
		return value;
	}

}