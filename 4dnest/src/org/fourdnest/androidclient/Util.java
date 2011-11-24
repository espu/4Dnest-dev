package org.fourdnest.androidclient;

/**
 * A class for static utility methods. 
 */
public final class Util {
	/** Private constructor to prevent instantiation */
	private Util() { }
	
	/**
	 * Null-safe comparison between two objects.
	 * @param o1 One of the objects to compare
	 * @param o2 The other object to compare
	 * @return true if both objects are equal or both objects are null
	 */
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
