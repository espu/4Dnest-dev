package org.fourdnest.androidclient.comm;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;

import java.util.ArrayList;


public interface Protocol {
    
    public String sendEgg(Egg egg);
    public ArrayList<Tag> topTags(int count);
    public void setNest(Nest nest);

}
