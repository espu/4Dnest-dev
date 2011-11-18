package org.fourdnest.androidclient;

public final class Util {
	//Hiding the Constructor
	private Util() {}
	public static boolean objectsEqual(Object o1, Object o2) {
		if(o1 != null) {
			return o1.equals(o2);
		} else if(o2 == null) {
			return true;
		} else {
			return false; // One null, other not -> fail
		}
	}
	
}
