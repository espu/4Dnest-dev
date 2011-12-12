package org.fourdnest.androidclient.test.services;

import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.comm.Protocol;
import org.fourdnest.androidclient.comm.ProtocolResult;

public class SendQueueTestProtocol implements Protocol {
	
	public int getProtocolId() {
		return 1024;
	}
	public ProtocolResult sendEgg(Egg egg) {
		return new ProtocolResult(null, 200);
	}
	public void setNest(Nest nest) {
	}
	public List<Tag> topTags(int count) {
		return null;
	}
}

