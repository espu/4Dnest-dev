package org.fourdnest.androidclient.comm;

public class ProtocolFactory {
	public static final int PROTOCOL_4DNEST = 0;

	/** Private constructor to to prevent instantiation */
	private ProtocolFactory() { };
	
	/**
	 *  Creates a new Protocol object of the requested type
	 *  @param protocolName The type of protocol.
	 *  @throws UnknownProtocolException 
	 *   */
	public static Protocol createProtocol(int protocolName) throws UnknownProtocolException {
		switch(protocolName) {
		case PROTOCOL_4DNEST:
			return new FourDNestProtocol();
		}
		throw new UnknownProtocolException(protocolName);
	}
}
