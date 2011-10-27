package org.fourdnest.androidclient;

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
	/** URI to the media file. Is null for status updates (text only Eggs). */
	private String mediaFileURI;
	/** Caption text. */
	private String caption;
	/** Tags attached to this Egg. */
	private List<Tag> tags;
	// FIXME: automatic metadata.
	
	public Egg(String mediaFileURI, String caption, List<Tag> tags) {
		this.mediaFileURI = mediaFileURI;
		this.caption = caption;
		this.tags = new ArrayList<Tag>(tags);
	}
	
	/**
	 * @param mediaFileURI the mediaFileURI to set
	 */
	public void setMediaFileURI(String mediaFileURI) {
		this.mediaFileURI = mediaFileURI;
	}
	/**
	 * @return the mediaFileURI
	 */
	public String getMediaFileURI() {
		return mediaFileURI;
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
