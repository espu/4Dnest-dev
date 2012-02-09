package org.fourdnest.androidclient.comm;

import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;


/**
 * Interface for communications protocols. Protocol objects are created by
 * the @see ProtocolFactory. Each Protocol object is bound to one specific Nest.
 * For return values, @see ProtocolResult.
 */
public interface Protocol {
    /**
     * Attempts to send an Egg to the Nest that this Protocol is bound to.
     * @param egg The Egg to send.
     * @return Success value of the request
     */
    ProtocolResult sendEgg(Egg egg);
    /**
     * Attempts to update an Egg that resides on the Nest that this Protocol is bound to.
     * @param egg The new state of the Egg being updated.
     * @return Success value of the request
     */
    ProtocolResult overwriteEgg(Egg egg);
    /**
     * Attempts to retrieve an Egg from Nest that this Protocol is bound to.
     * FIXME Errorhandling!
     * @param uid The unique id of the Egg, as used on the Nest.
     * @return The requested Egg or null if unable to retrieve the Egg.
     */
    Egg getEgg(String uid);
    /**
     * Attempts to retrieve a media file from the Nest that this Protocol is bound to.
     * FIXME Errorhandling! should return ProtocolResult
     * @param uri URI of the media file
     * @param localPath A path on the device in which to store the file
     * @return Success value of the request 
     */
    boolean getMediaFile(String uri, String localPath);
    /**
     * Attempts to retrieve the stream of new Eggs from the Nest that this Protocol is bound to.
     * FIXME Errorhandling!
     * @return The Eggs currently in the stream, or an empty list if unable to retrieve stream.
     */
    List<Egg> getStream(int size);
    /**
     * Attempts to retrieve the top tags from the Nest that this Protocol is bound to.
     * FIXME Errorhandling!
     * @param count How many tags to return.
     * @return The top tags, or an empty list if unable to retrieve stream.
     */
    List<Tag> topTags(int count);
    /**
     * Bind this Protocol object to the given Nest.
     * @param nest The nest to bind to.
     */
    void setNest(Nest nest);
    /**
     * Returns the unique id of the type of protocol this Protocol object uses.
     * @return the protocol id.
     */
	int getProtocolId();
	
    /**
     * Can be called to make sure thumbnail is in memory card, thumbnail is downloaded from 4dnest server or
     * OSM static maps api when applicable.
     * @param Egg whose thumbnail is in question 
     * @return boolean whether thumbnail can be found in predefined location
     */
    boolean getThumbnail(Egg egg, String size);
    
    boolean getMedia(Egg egg);
}
