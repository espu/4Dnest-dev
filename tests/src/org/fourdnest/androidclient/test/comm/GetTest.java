package org.fourdnest.androidclient.test.comm;

import org.fourdnest.androidclient.comm.Protocol;
import org.fourdnest.androidclient.comm.ProtocolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

public class GetTest extends AndroidTestCase {
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testGetMediaFile() throws Exception {
		Protocol protocol = ProtocolFactory.createProtocol(ProtocolFactory.PROTOCOL_4DNEST);
		boolean res = protocol.getMediaFile("http://users.tkk.fi/~aetolone/test.jpg", "/sdcard/test.jpg");
		assertTrue(res);
	}

}
