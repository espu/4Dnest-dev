package org.fourdnest.androidclient.comm;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public final class ProtocolFactory {
	/** The 4DNest protocol */
	public static final int PROTOCOL_4DNEST = 0;
	private static final String TAG = "ProtocolFactory";
	
	private static final Map<Integer, Class<? extends Protocol>> protocols =
		new HashMap<Integer, Class<? extends Protocol>>();

	static {
		protocols.put(Integer.valueOf(PROTOCOL_4DNEST), FourDNestProtocol.class);
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
				Log.e(TAG, e.getMessage());
			} catch (InstantiationException e) {
				// This should never happen
				Log.e(TAG, e.getMessage());
			}
		}
		throw new UnknownProtocolException(protocolName);
	}
	
	public static void registerProtocol(int protocolName, Class<? extends Protocol> c) {
		protocols.put(Integer.valueOf(protocolName), c);
	}
}
