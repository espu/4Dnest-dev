package org.fourdnest.androidclient;

public class Nest {
	
	private static final String TAG = Nest.class.getSimpleName();
	
	int id;
	public String name;
	public String description;	
	public String address;
	public String protocolName;
	
	public Nest(int id,
				String name,
				String description,
				String address,
				String protocolName) {
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.address = address;
		this.protocolName = protocolName;
	}
	
	public Nest() {
		
	}

}
