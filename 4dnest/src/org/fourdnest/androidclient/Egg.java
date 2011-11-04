package org.fourdnest.androidclient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one Egg, which is the unit of content in the system.
 * The Egg can be a local stored Egg or a temporary copy of an Egg from the
 * Nest.
 * 
 * For storing the Eggs, @see EggDatabase
 */
public class Egg {
	//private static final String TAG = Egg.class.getSimpleName();
	/** Id of the Nest that this Egg was or will be sent to */
	private int nestId;
	
	/** URI to local media file. Is null for status updates (text only Eggs). */
	private URI localFileURI;
	
	/** URI to remote media file (uploaded to a nest). Is null for text only Eggs
	 * and Eggs that have not been successfully uploaded yet*/
	private URI remoteFileURI;
		
	/** Caption text. */
	private String caption;
	
	/** Tags attached to this Egg. */
	private List<Tag> tags;
	
	// FIXME: automatic metadata.
	
	/**
	 * Creates new Egg with given params
	 * @param id
	 * @param localFileURI
	 * @param caption
	 * @param tags
	 */
	public Egg(int id, URI localFileURI, String caption, List<Tag> tags) {
		this.localFileURI = localFileURI;
		this.caption = caption;
		this.tags = new ArrayList<Tag>(tags);
	}
	
	/**
	 * @param nestId Id of Nest that this Egg was or will be sent to.
	 */
	public void setNestId(int nestId) {
		this.nestId = nestId;
	}
	/**
	 * @return Id of Nest that this Egg was or will be sent to.
	 */
	public int getNestId() {
		return nestId;
	}

	/**
	 * @param mediaFileURI the localFileURI to set
	 */
	public void setLocalFileURI(URI mediaFileURI) {
		this.localFileURI = mediaFileURI;
	}
	/**
	 * @return the localFileURI
	 */
	public URI getLocalFileURI() {
		return localFileURI;
	}
	
	/**
	 * 
	 * @param remoteFileURI the remoteFileURI to set
	 */
	public void setRemoteFileURI(URI remoteFileURI) {
		this.remoteFileURI = remoteFileURI;
	}
	
	/**
	 * 
	 * @return the remoteFileURI
	 */
	public URI getRemoteFileURI() {
		return remoteFileURI;
	}	

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param tags the tag list to replace the current tags with.
	 */
	public void setTags(List<Tag> tags) {
		this.tags = new ArrayList<Tag>(tags);
	}
	/**
	 * @return a shallow copy of the tag list.
	 */
	public List<Tag> getTags() {
		return new ArrayList<Tag>(tags);
	}
	
}
