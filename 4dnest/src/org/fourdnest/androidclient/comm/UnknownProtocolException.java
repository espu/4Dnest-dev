package org.fourdnest.androidclient.comm;

public class UnknownProtocolException extends Exception {
	/**
	 * The requested protocol id (protocolName) was not found
	 */
	private static final long serialVersionUID = -4959770447870739918L;

	public UnknownProtocolException(final int protocolName) {
		super("Unknown protocol " + protocolName);
	}
}
