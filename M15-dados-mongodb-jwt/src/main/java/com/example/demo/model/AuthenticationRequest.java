package com.example.demo.model;

public class AuthenticationRequest {
	
	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUsername(String name) {
		this.userName = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AuthenticationRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
