package com.example.demo.model;

public class PlayerDetails {
	
	private String name;
	private long id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public PlayerDetails(String name, long id) {
		super();
		this.name = name;
		this.id = id;
	}
	public PlayerDetails() {
	}
	public PlayerDetails(String name) {
		super();
		this.name = name;
	}
	

}
