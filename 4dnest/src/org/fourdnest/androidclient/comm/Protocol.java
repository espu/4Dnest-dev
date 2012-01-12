package org.fourdnest.androidclient.comm;

import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;


public interface Protocol {
    
    ProtocolResult sendEgg(Egg egg);
    Egg getEgg(String uid);
    boolean getMediaFile(String uri, String localPath);
    List<Egg> getStream();
    List<Tag> topTags(int count);
    void setNest(Nest nest);
	int getProtocolId();

}
