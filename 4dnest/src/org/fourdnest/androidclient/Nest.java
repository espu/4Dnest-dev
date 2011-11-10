

package org.fourdnest.androidclient;

public class Nest {
	
	private static final String TAG = Nest.class.getSimpleName();
	
	int id;
	String name;
	String description;	
	String baseURL;
	String protocolName;
	
	public Nest(int id,
				String name,
				String description,
				String address,
				String protocolName) {
		
		this.id = id;
		this.name = name;
		this.description = description;		
		this.baseURL = address;		
		this.protocolName = protocolName;
	}
	
	public boolean equals(Nest nest) {
		
		if(nest == null) return false;
		
		boolean eq = (this.id == nest.id &&
				this.name.equals(nest.name) &&
				this.description.equals(nest.description) &&
				this.baseURL.equals(nest.baseURL) &&
				this.protocolName.equals(nest.protocolName)
		);
		
		return eq;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
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

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}


}
