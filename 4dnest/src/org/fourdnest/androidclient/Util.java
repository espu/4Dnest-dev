package org.fourdnest.androidclient;

public final class Util {

	public static boolean ObjectsEqual(Object o1, Object o2) {
		if(o1 != null) {
			return o1.equals(o2);
		} else if(o1 == null && o2 == null) {
			return true;
		} else return false; // One null, other not -> fail
	}
	
}
