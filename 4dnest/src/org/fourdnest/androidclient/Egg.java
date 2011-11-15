package org.fourdnest.androidclient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
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
	
	/** Egg id */
	private int id;
	
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
	
	/** When Egg was last uploaded to associated Nest */
	private Date lastUpload;
	
	// FIXME: automatic metadata.
	
	
	/**
	 * Creates new empty Egg
	 */
	public Egg() {
	}
	
	/**
	 * Creates new Egg with given properties
	 * @param id Egg id
	 * @param nestId Nest id
	 * @param localFileURI Local file URI
	 * @param remoteFileURI Remote file URI
	 * @param caption Caption text
	 * @param tags Tag list
	 */
	public Egg(int id, int nestId, URI localFileURI, URI remoteFileURI, String caption, List<Tag> tags, Date lastUpload) {
		this.id = id;
		this.nestId = nestId;
		this.localFileURI = localFileURI;
		this.remoteFileURI = remoteFileURI;
		this.caption = caption;
		this.tags = tags;
		this.lastUpload = lastUpload;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Egg)) return false;
		
		Egg other = (Egg)o;
		
		boolean equal = (this.id == other.id &&
				this.nestId == other.nestId && 
				Util.ObjectsEqual(this.localFileURI, other.localFileURI) &&
				Util.ObjectsEqual(this.remoteFileURI, other.remoteFileURI) &&
				Util.ObjectsEqual(this.caption, other.caption) &&
				Util.ObjectsEqual(this.tags, other.tags) &&
				Util.ObjectsEqual(this.lastUpload, other.lastUpload)
				);
		
		return equal;
	}
	
	/**
	 * Sets Egg id
	 * @param id New Egg id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns egg id
	 * @return Egg id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Sets id of associated Nest
	 * @param nestId Id of Nest that this Egg was or will be sent to.
	 */
	public void setNestId(int nestId) {
		this.nestId = nestId;
	}
	/**
	 * Returns id of associated Nest
	 * @return Id of Nest that this Egg was or will be sent to.
	 */
	public int getNestId() {
		return this.nestId;
	}

	/**
	 * Sets local file URI
	 * @param mediaFileURI The localFileURI to set
	 */
	public void setLocalFileURI(URI mediaFileURI) {
		this.localFileURI = mediaFileURI;
	}
	/**
	 * Returns local file URI
	 * @return The localFileURI
	 */
	public URI getLocalFileURI() {
		return this.localFileURI;
	}
	
	/**
	 * Sets remote file URI
	 * @param remoteFileURI The remoteFileURI to set
	 */
	public void setRemoteFileURI(URI remoteFileURI) {
		this.remoteFileURI = remoteFileURI;
	}
	
	/**
	 * Returns egg remote file URI
	 * @return Remote file URI
	 */
	public URI getRemoteFileURI() {
		return this.remoteFileURI;
	}	

	/**
	 * Sets Egg caption text
	 * @param caption The caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * Returns Egg caption text
	 * @return Caption text
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * Set Egg tag list
	 * @param tags The tag list to replace the current tags with
	 */
	public void setTags(List<Tag> tags) {
		this.tags = new ArrayList<Tag>(tags);
	}
	/**
	 * Returns a copy of the tag list
	 * @return Copy of tag list.
	 */
	public List<Tag> getTags() {
		return new ArrayList<Tag>(tags);
	}
	
	/**
	 * Set last upload date
	 * @param lastUpload Last upload date
	 */
	public void setLastUpload(Date lastUpload) {
		this.lastUpload = lastUpload;
	}
	
	/**
	 * Returns last upload date
	 * @return Last upload date
	 */
	public Date getLastUpload() {
		return this.lastUpload;
	}
	
}
