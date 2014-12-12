package com.example.paypal.adapter;

public class Location {
	private String name;
	private String location;
	
	
	public Location(String name, String location) {
		this.location =  location;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
		
	

}
