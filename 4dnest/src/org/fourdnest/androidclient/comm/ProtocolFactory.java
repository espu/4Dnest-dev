package org.fourdnest.androidclient.comm;

import java.util.HashMap;
import java.util.Map;

public class ProtocolFactory {
	/** The 4DNest protocol */
	public static final int PROTOCOL_4DNEST = 0;
	
	private static final Map<Integer, Class<? extends Protocol>> protocols =
		new HashMap<Integer, Class<? extends Protocol>>();

	static {
		protocols.put(new Integer(PROTOCOL_4DNEST), FourDNestProtocol.class);
	}
	
	/** Private constructor to to prevent instantiation */
	private ProtocolFactory() { };
	
	/**
	 *  Creates a new Protocol object of the requested type
	 *  @param protocolName The type of protocol.
	 *  @throws UnknownProtocolException 
	 *   */
	public static Protocol createProtocol(int protocolName) throws UnknownProtocolException {
		Class<? extends Protocol> c = protocols.get(protocolName);
		if(c != null) {
			try {
				return c.newInstance();
			} catch (IllegalAccessException e) {
				// This should never happen
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				// This should never happen
				throw new RuntimeException(e);
			}
		}
		throw new UnknownProtocolException(protocolName);
	}
	
	public static void registerProtocol(int protocolName, Class<? extends Protocol> c) {
		protocols.put(new Integer(protocolName), c);
	}
}
