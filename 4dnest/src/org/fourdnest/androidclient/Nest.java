package org.fourdnest.androidclient;

public class Nest {
	
	private static final String TAG = Nest.class.getSimpleName();
	
	private int id;
	private String name;
	private String description;	
	private String baseURL;
	private int protocolId;
	
	public Nest(int id,
				String name,
				String description,
				String address,
				int protocolName) {
		
		this.id = id;
		this.name = name;
		this.description = description;		
		this.baseURL = address;		
		this.setProtocolId(protocolName);
	}

	public boolean isEqual(Nest nest) {
		
		if(nest == null) {
			return false;
		}
		
		boolean eq = (this.id == nest.id &&
				Util.objectsEqual(this.name, nest.name) &&
				Util.objectsEqual(this.description, nest.description) &&
				Util.objectsEqual(this.baseURL, nest.baseURL) &&
				this.getProtocolId() == nest.getProtocolId()
		);
		
		return eq;
	}	

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBaseURL() {
		return this.baseURL;
	}

	public void setBaseURL(String address) {
		this.baseURL = address;
	}

	public int getProtocolName() {
		return this.getProtocolId();
	}

	public void setProtocolName(int protocolName) {
		this.setProtocolId(protocolName);
	}

	/**
	 * @param protocolId the protocolId to set
	 */
	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}

	/**
	 * @return the protocolId
	 */
	public int getProtocolId() {
		return protocolId;
	}


}
